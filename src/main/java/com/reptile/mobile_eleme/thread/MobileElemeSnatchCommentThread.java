package com.reptile.mobile_eleme.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.reptile.mobile_eleme.ElemeHttpBase;
import com.reptile.mobile_eleme.job.ElemeCommentDailyTaskJob;
import com.reptile.mobile_eleme.job.ElemeCommentTaskJob;
import com.reptile.mobile_eleme.job.ElemeProductTaskJob;
import com.reptile.server.MyConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取评论链接
 */
public class MobileElemeSnatchCommentThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileElemeSnatchCommentThread.class);
    private ElemeProductTaskJob task;

    public MobileElemeSnatchCommentThread(ElemeProductTaskJob task) {
        this.task = task;
    }

    DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run() {
        if (MyConstant.MOBILE_ELEME_CONFIG.GENERATE_COMMENT) {
            long time = System.currentTimeMillis();
            tDao.update(ElemeProductTaskJob.class, Chain.make("comment_status", 1), Cnd.where("id", "=", task.getId()));
            String url = task.getUrl();
            // https://www.ele.me/shop/379954
            String shop_id = null;
            if (url.contains("shop")) {
                try {
                    shop_id = task.getShop_id();
                    if (StringUtils.isEmpty(shop_id)) {
                        tDao.update(ElemeProductTaskJob.class, Chain.make("comment_status", -1),
                                Cnd.where("id", "=", task.getId()));
                        return;
                    }
                } catch (Exception e) {
                    tDao.update(ElemeProductTaskJob.class, Chain.make("comment_status", 3),
                            Cnd.where("id", "=", task.getId()));
                    return;
                }
            }
            try {
                // 请求评论数量
                String webUrl4 = null;
                if (shop_id != null) {
                    // webUrl4 = "https://www.ele.me/restapi/ugc/v1/restaurant/"
                    // +
                    // shop_id + "/rating_categories";
                    // webUrl4 =
                    // "https://mainsite-restapi.ele.me/ugc/v1/restaurant/" +
                    // shop_id + "/rating_categories";
                    webUrl4 = "https://mainsite-restapi.ele.me/ugc/v2/restaurants/" + shop_id + "/ratings/tags";
                }
                String result4 = "";
                int total = 0;
                try {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Referer", "https://h5.ele.me/shop/");
                    headers.put("Origin", "https://h5.ele.me");
                    headers.put("Host", "mainsite-restapi.ele.me");
                    result4 = ElemeHttpBase.get(webUrl4, "utf-8", headers).getResult();
                    if (result4.startsWith("[")) {
                        try {
                            JSONArray tags = new JSONArray(result4);
                            // 全部标签
                            if (tags.length() > 0) {
                                try {
                                    total = Integer.parseInt(tags.getJSONObject(0).getString("count"));
                                } catch (Exception e) {
                                    logger.error("", e);
                                }
                            }
                        } catch (JSONException e) {
                            logger.error("", e);
                        }
                    } else {
                        logger.info("重置饿了么评论状态");
                        tDao.update(ElemeProductTaskJob.class, Chain.make("comment_status", 0),
                                Cnd.where("id", "=", task.getId()));
                        return;
                    }
                } catch (Exception e) {
                    logger.info("", e);
                    total = 0;
                }
                int pages = 0;
                pages = calculatePage(total, 10, MyConstant.ELEME_CONFIG.PAGE_MAX);
                if (total > 0) {
                    try {
                        if (StringUtils.isNotEmpty(task.getRemark())) {
                            ElemeCommentTaskJob elemeCommentTaskJob = new ElemeCommentTaskJob();
                            elemeCommentTaskJob.setCity_name(task.getCity_name());
                            elemeCommentTaskJob.setKeyword(task.getKeyword());
                            elemeCommentTaskJob.setRequest_id(task.getRequest_id());
                            elemeCommentTaskJob.setTask_id(task.getTask_id());
                            elemeCommentTaskJob.setCreate_time(new Date());
                            elemeCommentTaskJob.setShop_id(shop_id);
                            elemeCommentTaskJob.setShop_name(task.getShop_name());
                            elemeCommentTaskJob.setNode(task.getNode());
                            elemeCommentTaskJob.setStatus(0);
                            elemeCommentTaskJob.setProject_code(task.getProject_code());
                            elemeCommentTaskJob.setDown_type(task.getDown_type());
                            elemeCommentTaskJob.setPage_from(1);
                            elemeCommentTaskJob.setPage_to(pages);
                            elemeCommentTaskJob.setTotal(total);
                            elemeCommentTaskJob.setRemark("_all");
                            tDao.insert(elemeCommentTaskJob);
                        } else {
                            ElemeCommentDailyTaskJob elemeCommentDailyTaskJob = new ElemeCommentDailyTaskJob();
                            elemeCommentDailyTaskJob.setCity_name(task.getCity_name());
                            elemeCommentDailyTaskJob.setKeyword(task.getKeyword());
                            elemeCommentDailyTaskJob.setRequest_id(task.getRequest_id());
                            elemeCommentDailyTaskJob.setTask_id(task.getTask_id());
                            elemeCommentDailyTaskJob.setCreate_time(new Date());
                            elemeCommentDailyTaskJob.setShop_id(shop_id);
                            elemeCommentDailyTaskJob.setShop_name(task.getShop_name());
                            elemeCommentDailyTaskJob.setNode(task.getNode());
                            elemeCommentDailyTaskJob.setStatus(0);
                            elemeCommentDailyTaskJob.setProject_code(task.getProject_code());
                            elemeCommentDailyTaskJob.setDown_type(task.getDown_type());
                            elemeCommentDailyTaskJob.setPage_from(1);
                            elemeCommentDailyTaskJob.setPage_to(pages);
                            elemeCommentDailyTaskJob.setTotal(total);
                            tDao.insert(elemeCommentDailyTaskJob);
                        }
                    } catch (Exception e) {
                        //logger.error("饿了么评论任务生成异常", e);
                    }
                    tDao.update(ElemeProductTaskJob.class, Chain.make("comment_status", 2), Cnd.where("id", "=", task.getId()));
                    logger.info("==========================评论URL生成完成，耗时：" + (System.currentTimeMillis() - time)
                            + "============================");
                    return;
                } else {
                    tDao.update(ElemeProductTaskJob.class, Chain.make("comment_status", -1), Cnd.where("id", "=", task.getId()));
                    logger.info("==========================店铺：" + url + "，没有评论============================");
                    return;
                }
            } catch (Exception e) {
                logger.info("", e);
                tDao.update(ElemeProductTaskJob.class, Chain.make("comment_status", 3),
                        Cnd.where("id", "=", task.getId()));
                return;
            }
        } else {
            //
        }
    }

    /**
     * 计算 每页显示数量为定值的情况下的 页数,若大于maxPage,则设置为maxPage, 适用于最大页数有限制的网站
     */
    public static int calculatePage(int total, int limit, int maxPage) {
        int result;
        if (total <= limit && total > 0) {
            result = 1;
        } else {
            if (total <= 0) {
                result = -1;
            } else {
                int pageTotalTmp = total % limit;
                if (pageTotalTmp == 0) {
                    result = total / limit;
                } else {
                    result = (total - pageTotalTmp) / limit + 1;
                }
            }
        }

        if (result > maxPage) {
            result = maxPage;
        }
        return result;
    }
}
