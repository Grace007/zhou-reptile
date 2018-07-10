package com.reptile.util.WaimaiNodesDivideUtils.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiProductTaskJob;
import com.reptile.mobile_eleme.job.ElemeProductTaskJob;
import com.reptile.util.WaimaiNodesDivideUtils.job.Nodes;
import com.reptile.util.WaimaiNodesDivideUtils.job.QueryProductStatusRS;
import com.reptile.util.WaimaiNodesDivideUtils.job.QueryStatusRS;
import com.reptile.util.WaimaiNodesDivideUtils.job.Waimai_task_list;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;

import java.util.List;


public class ProductDivideThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(ProductDivideThread.class);
    private Waimai_task_list task;

    public ProductDivideThread(Waimai_task_list task) {
        this.task = task;
    }

    public void run() {
        tDao.update(Waimai_task_list.class, Chain.make("divide_product_or_not", 1), Cnd.where("id", "=", task.getId()));
        int pr = getProductRemaining(task.getWebsite(), task.getTask_id(), task.getRequest_id());
        String ps = getProductStatus(task.getWebsite(), task.getTask_id(), task.getRequest_id());
        updateTaskList(task.getWebsite(), task.getRequest_id(), task.getTask_id(), task.getDescription(), pr, ps);
    }

    public void updateTaskList(String website, long request_id, long task_id, String description, int product_remaining, String product_status) {
        long time = System.currentTimeMillis();
        String nodes = "";
    	List<Nodes> nodesList = tDao.query(Nodes.class,
				Cnd.where("website", "like", "%" + website + "%").and("description", "like", "%" + description + "%")
				.and("request_id", "=", task.getRequest_id()).and("task_id", "=", task.getTask_id()));
        for (Nodes n : nodesList) {
            nodes += n.getNode() + ",";
        }
        if (StringUtils.isNotEmpty(nodes)) {
            Sql sqlClass = null;
            String sql = "";
            try {
                String[] node_list = nodes.split(",");
                if (product_remaining > 0 && node_list.length > 1) {
                    if ("baidu".equals(task.getWebsite())) {
                        SqlExpressionGroup e1 = Cnd.exps("status", "=", 0).or("status", "=", 1);
                        tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", 888), Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and(e1));
                        logger.info("百度外卖product[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]任务重置耗时：" + (System.currentTimeMillis() - time));
                        int limit_num = product_remaining / node_list.length + 1;
                        for (String s : node_list) {
                            long time1 = System.currentTimeMillis();
                            //tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("node", s).add("status", 0), Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and("status", "=", 888).limit(limit_num));
                            sql = "UPDATE mobile_baidu_waimai_product_task SET `status` = 0,node = '" + s + "' WHERE task_id = " + task_id + " AND request_id = " + request_id + " AND `status` = 888 LIMIT " + limit_num + ";";
                            sqlClass = Sqls.queryEntity(sql);
                            sqlClass.setEntity(tDao.getEntity(BaiduWaimaiProductTaskJob.class));
                            try {
                                tDao.execute(sqlClass);
                            } catch (Exception e) {
                                logger.error("更新百度product节点异常", e);
                            }
                            logger.info("百度外卖product[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]任务分配节点[" + s + "]耗时：" + (System.currentTimeMillis() - time1));
                        }
                    } else if ("eleme".equals(task.getWebsite())) {
                        SqlExpressionGroup e1 = Cnd.exps("product_status", "=", 0).or("product_status", "=", 1);
                        tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", 888), Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and(e1));
                        logger.info("饿了么product[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]任务重置耗时：" + (System.currentTimeMillis() - time));
                        int limit_num = product_remaining / node_list.length + 1;
                        for (String s : node_list) {
                            long time1 = System.currentTimeMillis();
                            //tDao.update(EleMeSearchUrlJob.class, Chain.make("node", s).add("status", 0), Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and("status", "=", 888).limit(limit_num));
                            sql = "UPDATE mobile_eleme_product_task SET `product_status` = 0,node = '" + s + "' WHERE task_id = " + task_id + " AND request_id = " + request_id + " AND `product_status` = 888 LIMIT " + limit_num + ";";
                            sqlClass = Sqls.queryEntity(sql);
                            sqlClass.setEntity(tDao.getEntity(ElemeProductTaskJob.class));
                            try {
                                tDao.execute(sqlClass);
                            } catch (Exception e) {
                                logger.error("更新饿了么product节点异常", e);
                            }
                            logger.info("饿了么product[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]任务分配节点[" + s + "]耗时：" + (System.currentTimeMillis() - time1));
                        }
                    }else if ("meituan".equals(task.getWebsite())) {

                        SqlExpressionGroup e1 = Cnd.exps("product_status", "=", 0).or("product_status", "=", 1);

                        logger.info("饿了么product[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]任务重置耗时：" + (System.currentTimeMillis() - time));
                        int limit_num = product_remaining / node_list.length + 1;
                        for (String s : node_list) {
                            long time1 = System.currentTimeMillis();
                            //tDao.update(EleMeSearchUrlJob.class, Chain.make("node", s).add("status", 0), Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and("status", "=", 888).limit(limit_num));
                            sql = "UPDATE mobile_meituan_url_task SET product_status = 0,node = '" + s + "' WHERE task_id = " + task_id + " AND request_id = " + request_id + " AND product_status = 888 LIMIT " + limit_num + ";";
                            sqlClass = Sqls.queryEntity(sql);

                            try {
                                tDao.execute(sqlClass);
                            } catch (Exception e) {
                                logger.error("更新饿了么product节点异常", e);
                            }
                            logger.info("美团product[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]任务分配节点[" + s + "]耗时：" + (System.currentTimeMillis() - time1));
                        }
                    
					}
                } else {

                }
            } catch (Exception e) {
                logger.error(e);
            }
            nodes = nodes.substring(0, nodes.length() - 1);
        }
        tDao.update(Waimai_task_list.class, Chain.make("nodes", nodes).add("product_remaining", product_remaining).add("product_status", product_status).add("divide_product_or_not", 2), Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and("website", "=", website));
        logger.info("product任务节点分配完成，总耗时：" + (System.currentTimeMillis() - time));
    }


    public int getProductRemaining(String website, long task_id, long request_id) {
        long time = System.currentTimeMillis();
        int remaining = 0;
        if ("baidu".equals(website)) {
            SqlExpressionGroup e1 = Cnd.exps("status", "=", 0).or("status", "=", 1).or("status", "=", 888);
            List<BaiduWaimaiProductTaskJob> list = tDao.query(BaiduWaimaiProductTaskJob.class, Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and(e1));
            remaining = list.size();
            logger.info("查询百度外卖[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]，剩余product任务数量：" + remaining + "，耗时：" + (System.currentTimeMillis() - time));
        } else if ("eleme".equals(website)) {
            SqlExpressionGroup e1 = Cnd.exps("product_status", "=", 0).or("product_status", "=", 1).or("product_status", "=", 888);
            List<ElemeProductTaskJob> list = tDao.query(ElemeProductTaskJob.class, Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and(e1));
            remaining = list.size();
            logger.info("查询饿了么[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]，剩余product任务数量：" + remaining + "，耗时：" + (System.currentTimeMillis() - time));
        } else if ("meituan".equals(website)) {
            SqlExpressionGroup e1 = Cnd.exps("product_status", "=", 0).or("product_status", "=", 1).or("product_status", "=", 888);

            logger.info("查询美团[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]，剩余product任务数量：" + remaining + "，耗时：" + (System.currentTimeMillis() - time));
        
        }
        return remaining;
    }

    public String getProductStatus(String website, long task_id, long request_id) {
        long time = System.currentTimeMillis();
        String status = "";
        Sql sqlClass = null;
        String sql = "";
        if ("baidu".equals(website)) {
            sql = "SELECT `status`,COUNT(0) as amount FROM mobile_baidu_waimai_product_task WHERE task_id = " + task_id + " AND request_id = " + request_id + "  GROUP BY `status`;";
            sqlClass = Sqls.queryEntity(sql);
            sqlClass.setEntity(tDao.getEntity(QueryStatusRS.class));
            try {
                tDao.execute(sqlClass);
                List<QueryStatusRS> list = sqlClass.getList(QueryStatusRS.class);
                for (QueryStatusRS rs : list) {
                    status += rs.getStatus() + "[" + rs.getAmount() + "];";
                }
            } catch (Exception e) {
                logger.error("product状态查询异常", e);
            }
            logger.info("查询百度外卖[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]，剩余product任务状态耗时：" + (System.currentTimeMillis() - time));
        } else if ("eleme".equals(website)) {
            sql = "SELECT `product_status`,COUNT(0) as amount FROM mobile_eleme_product_task WHERE task_id = " + task_id + " AND request_id = " + request_id + "  GROUP BY `product_status`;";
            sqlClass = Sqls.queryEntity(sql);
            sqlClass.setEntity(tDao.getEntity(QueryProductStatusRS.class));
            try {
                tDao.execute(sqlClass);
                List<QueryProductStatusRS> list = sqlClass.getList(QueryProductStatusRS.class);
                for (QueryProductStatusRS rs : list) {
                    status += rs.getProduct_status() + "[" + rs.getAmount() + "];";
                }
            } catch (Exception e) {
                logger.error("product状态查询异常", e);
            }
            logger.info("查询饿了么[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]，剩余product任务状态耗时：" + (System.currentTimeMillis() - time));
        } else if ("meituan".equals(website)) {
            sql = "SELECT product_status,COUNT(0) as amount FROM mobile_meituan_url_task WHERE task_id = " + task_id + " AND request_id = " + request_id + "  GROUP BY product_status;";
            sqlClass = Sqls.queryEntity(sql);
            sqlClass.setEntity(tDao.getEntity(QueryProductStatusRS.class));
            try {
                tDao.execute(sqlClass);
                List<QueryProductStatusRS> list = sqlClass.getList(QueryProductStatusRS.class);
                for (QueryProductStatusRS rs : list) {
                    status += rs.getProduct_status() + "[" + rs.getAmount() + "];";
                }
            } catch (Exception e) {
                logger.error("product状态查询异常", e);
            }
            logger.info("查询美团[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id + "]，剩余product任务状态耗时：" + (System.currentTimeMillis() - time));
        

        }
        return status;
    }

}
