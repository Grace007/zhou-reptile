package com.reptile.util.WaimaiNodesDivideUtils.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.reptile.util.WaimaiNodesDivideUtils.job.*;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import java.util.Date;
import java.util.List;

public class QueryNodeDetailStatusResultThread extends Thread {

	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private static Logger logger = Logger.getLogger(QueryNodeDetailStatusResultThread.class);
	private Waimai_task_list task;

	public QueryNodeDetailStatusResultThread(Waimai_task_list task) {
		this.task = task;
	}

	@Override
	public void run() {

		tDao.update(Waimai_task_list.class, Chain.make("divide_node_or_not", 1),
				Cnd.where("id", "=", task.getId()));
		String nodes = "";
		List<Nodes> nodesList = tDao.query(Nodes.class,
				Cnd.where("website", "like", "%" + task.getWebsite() + "%").and("description", "like", "%" + task.getDescription() + "%")
				.and("request_id", "=", task.getRequest_id()).and("task_id", "=", task.getTask_id()));
		
		
		/**
		 * 把每次request_id,task_id加入node监测数据库
		 **/
		
		QueryNodeStatusRS nodeStatusRS = new QueryNodeStatusRS();
		
		for (Nodes n : nodesList) {
			nodes += n.getNode() + ",";
			
			nodeStatusRS.setCity_name(n.getCity_name());
			nodeStatusRS.setCreate_date(new Date());
			nodeStatusRS.setCreate_time(new Date());
			nodeStatusRS.setDescription(n.getDescription());
			nodeStatusRS.setNode(n.getNode());
			nodeStatusRS.setRemark(n.getRemark());
			nodeStatusRS.setRequest_id(n.getRequest_id());
			nodeStatusRS.setTask_id(n.getTask_id());
			nodeStatusRS.setWebsite(n.getWebsite());
			
			try {
				tDao.insert(nodeStatusRS);
			} catch (Exception e) {
				logger.info("已存在抓取任务，稍后将进行每个节点任务的查询..........");
			}
			
		}
		String[] node_list = nodes.split(",");
		
		KeywordNodeStatusResult(task.getWebsite() , task.getTask_id() , task.getRequest_id(), node_list);
		WaimaiListNodeStatusResult(task.getWebsite() , task.getTask_id() , task.getRequest_id(), node_list);
		ShopKeywordNodeStatusResult(task.getWebsite() , task.getTask_id() , task.getRequest_id(), node_list);
		UrlNodeStatusResult(task.getWebsite() , task.getTask_id() , task.getRequest_id(), node_list);
		ProductNodeStatusResult(task.getWebsite() , task.getTask_id() , task.getRequest_id(), node_list);
		tDao.update(Waimai_task_list.class, Chain.make("divide_node_or_not", 2),
				Cnd.where("id", "=", task.getId()));
		logger.info("每个节点获得node的抓取情况success......");
	}
	
	/**
	 * 查询每个节点下Keyword的抓取量
	 **/
	public void KeywordNodeStatusResult(String website, long task_id, long request_id ,String [] nodes) {
		long time = System.currentTimeMillis();
		Sql sqlClass = null;
		String sql = "";
		if ("baidu".equals(website)) {

		} else if ("eleme".equals(website)) {

		} else if ("meituan".equals(website)) {
			
			for (int i = 0; i < nodes.length; i++) {
				sql = "SELECT `status`,COUNT(0) as amount FROM mobile_meituan_waimai_keyword_task WHERE task_id = " + task_id
						+ " AND request_id = " + request_id + " AND node= '"+nodes[i]+"' GROUP BY `status`";
				sqlClass = Sqls.queryEntity(sql);
				sqlClass.setEntity(tDao.getEntity(QueryStatusRS.class));
				try {
					tDao.execute(sqlClass);
					List<QueryStatusRS> list = sqlClass.getList(QueryStatusRS.class);
					String status = "";
					for (QueryStatusRS rs : list) {
						status += rs.getStatus() + "[" + rs.getAmount() + "];";
					}
					tDao.update(QueryNodeStatusRS.class, Chain.make("keyword_status", status).add("update_date", new Date()).add("update_time", new Date()), 
							Cnd.where("request_id", "=", request_id)
							.and("task_id", "=", task_id).and("node", "=", nodes[i])) ;
				} catch (Exception e) {
					logger.error("美团keyword状态查询异常", e);
				}
				logger.info("查询美团keyword[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id
						+ "]，剩余任务状态耗时：" + (System.currentTimeMillis() - time));
			}
			

		}
//		return status;
	}
	
	/**
	 * 查询每个节点下waimailist的抓取量
	 **/
	public void WaimaiListNodeStatusResult(String website, long task_id, long request_id ,String [] nodes) {
		long time = System.currentTimeMillis();
		Sql sqlClass = null;
		String sql = "";
		if ("baidu".equals(website)) {

		} else if ("eleme".equals(website)) {

		} else if ("meituan".equals(website)) {
			
			for (int i = 0; i < nodes.length; i++) {
				sql = "SELECT `status`,COUNT(0) as amount FROM mobile_meituan_waimai_list_task WHERE task_id = " + task_id
						+ " AND request_id = " + request_id + " AND node= '"+nodes[i]+"' AND WheatherCategory = '"+task.getDescription()+"' GROUP BY `status`";
				sqlClass = Sqls.queryEntity(sql);
				sqlClass.setEntity(tDao.getEntity(QueryStatusRS.class));
				try {
					tDao.execute(sqlClass);
					List<QueryStatusRS> list = sqlClass.getList(QueryStatusRS.class);
					String status = "";
					for (QueryStatusRS rs : list) {
						status += rs.getStatus() + "[" + rs.getAmount() + "];";
					}
					tDao.update(QueryNodeStatusRS.class, Chain.make("waimailist_status", status).add("update_date", new Date()).add("update_time", new Date()), Cnd.where("request_id", "=", request_id)
							.and("task_id", "=", task_id).and("node", "=", nodes[i])) ;
				} catch (Exception e) {
					logger.error("美团waimailist状态查询异常", e);
				}
				logger.info("查询美团waimailist[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id
						+ "]，剩余任务状态耗时：" + (System.currentTimeMillis() - time));
			}
			

		}
//		return status;
	}
	
	/**
	 * 查询每个节点下ShopKeyword的抓取量
	 **/
	public void ShopKeywordNodeStatusResult(String website, long task_id, long request_id ,String [] nodes) {
		long time = System.currentTimeMillis();
		Sql sqlClass = null;
		String sql = "";
		if ("baidu".equals(website)) {

		} else if ("eleme".equals(website)) {

		} else if ("meituan".equals(website)) {
			
			for (int i = 0; i < nodes.length; i++) {
				sql = "SELECT `status`,COUNT(0) as amount FROM mobile_meituan_waimai_shop_kerword_task WHERE task_id = " + task_id
						+ " AND request_id = " + request_id + " AND node= '"+nodes[i]+"' GROUP BY `status`";
				sqlClass = Sqls.queryEntity(sql);
				sqlClass.setEntity(tDao.getEntity(QueryStatusRS.class));
				try {
					tDao.execute(sqlClass);
					List<QueryStatusRS> list = sqlClass.getList(QueryStatusRS.class);
					String status = "";
					for (QueryStatusRS rs : list) {
						status += rs.getStatus() + "[" + rs.getAmount() + "];";
					}
					tDao.update(QueryNodeStatusRS.class, Chain.make("shopkeyword_status", status).add("update_date", new Date()).add("update_time", new Date()), Cnd.where("request_id", "=", request_id)
							.and("task_id", "=", task_id).and("node", "=", nodes[i])) ;
				} catch (Exception e) {
					logger.error("美团shopkeyword状态查询异常", e);
				}
				logger.info("查询美团shopkeyword[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id
						+ "]，剩余任务状态耗时：" + (System.currentTimeMillis() - time));
			}
			

		}
//		return status;
	}
	

	
	/**
	 * 查询每个节点下url的抓取量
	 **/
	public void UrlNodeStatusResult(String website, long task_id, long request_id ,String [] nodes) {
		long time = System.currentTimeMillis();
		Sql sqlClass = null;
		String sql = "";
		if ("baidu".equals(website)) {

		} else if ("eleme".equals(website)) {

		} else if ("meituan".equals(website)) {
			
			for (int i = 0; i < nodes.length; i++) {
				sql = "SELECT `status`,COUNT(0) as amount FROM mobile_meituan_url_task WHERE task_id = " + task_id
						+ " AND request_id = " + request_id + " AND node= '"+nodes[i]+"' GROUP BY `status`";
				sqlClass = Sqls.queryEntity(sql);
				sqlClass.setEntity(tDao.getEntity(QueryStatusRS.class));
				try {
					tDao.execute(sqlClass);
					List<QueryStatusRS> list = sqlClass.getList(QueryStatusRS.class);
					String status = "";
					for (QueryStatusRS rs : list) {
						status += rs.getStatus() + "[" + rs.getAmount() + "];";
					}
					tDao.update(QueryNodeStatusRS.class, Chain.make("url_status", status).add("update_date", new Date()).add("update_time", new Date()), Cnd.where("request_id", "=", request_id)
							.and("task_id", "=", task_id).and("node", "=", nodes[i])) ;
				} catch (Exception e) {
					logger.error("美团url状态查询异常", e);
				}
				logger.info("查询美团url[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id
						+ "]，剩余任务状态耗时：" + (System.currentTimeMillis() - time));
			}
			

		}
//		return status;
	}
	
	/**
	 * 查询每个节点下product的抓取量
	 **/
	public void ProductNodeStatusResult(String website, long task_id, long request_id ,String [] nodes) {
		long time = System.currentTimeMillis();
		Sql sqlClass = null;
		String sql = "";
		if ("baidu".equals(website)) {

		} else if ("eleme".equals(website)) {

		} else if ("meituan".equals(website)) {
			
			for (int i = 0; i < nodes.length; i++) {
				sql = "SELECT product_status,COUNT(0) as amount FROM mobile_meituan_url_task WHERE task_id = " + task_id
						+ " AND request_id = " + request_id + " AND node= '"+nodes[i]+"' GROUP BY product_status";
				sqlClass = Sqls.queryEntity(sql);
				sqlClass.setEntity(tDao.getEntity(QueryProductStatusRS.class));
				try {
					tDao.execute(sqlClass);
					List<QueryProductStatusRS> list = sqlClass.getList(QueryProductStatusRS.class);
					String status = "";
					for (QueryProductStatusRS rs : list) {
						status += rs.getProduct_status() + "[" + rs.getAmount() + "];";
					}
					tDao.update(QueryNodeStatusRS.class, Chain.make("product_status", status).add("update_date", new Date()).add("update_time", new Date()), Cnd.where("request_id", "=", request_id)
							.and("task_id", "=", task_id).and("node", "=", nodes[i])) ;
				} catch (Exception e) {
					logger.error("美团product状态查询异常", e);
				}
				logger.info("查询美团product[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id
						+ "]，剩余任务状态耗时：" + (System.currentTimeMillis() - time));
			}
			

		}
//		return status;
	}

}
