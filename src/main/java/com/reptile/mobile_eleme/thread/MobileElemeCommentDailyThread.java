package com.reptile.mobile_eleme.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.mobile_eleme.ElemeHttpBase;
import com.reptile.mobile_eleme.job.ElemeCommentDailyTaskJob;
import com.reptile.mobile_eleme.model.ElemeCommentInfoModel;
import com.reptile.util.EmojiFilterUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.TableName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取每日评论
 */
public class MobileElemeCommentDailyThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileElemeCommentDailyThread.class);
    private ElemeCommentDailyTaskJob task;

    public MobileElemeCommentDailyThread(ElemeCommentDailyTaskJob task) {
        this.task = task;
    }

    DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        logger.info("开始下载饿了么Daily评论...");
        TaskToBi tobi = BIDataSource.getBiDataSource("eleme", task.getProject_code(), task.getDown_type());
        Dao biDao = tobi.getBiDao();
        tDao.update(ElemeCommentDailyTaskJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        String shop_id = task.getShop_id();
        // https://www.ele.me/restapi/ugc/v1/restaurant/699346/ratings?limit=10&offset=0
        if (StringUtils.isNotEmpty(shop_id)) {
            int page_from = task.getPage_from();
            int page_to = task.getPage_to();
            EmojiFilterUtils filterUtils = new EmojiFilterUtils();
            for (int i = page_from; i <= page_to; i++) {
                String url = "https://mainsite-restapi.ele.me/ugc/v1/restaurant/" + shop_id + "/ratings?has_content=true&offset=" + 10 * (i - 1) +
                        "&limit=10";
                String result;
                try {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Referer", "https://h5.ele.me/shop/");
                    headers.put("Origin", "https://h5.ele.me");
                    headers.put("Host", "mainsite-restapi.ele.me");
                    result = ElemeHttpBase.get(url, "utf-8", headers).getResult();
                    //logger.info("饿了么评论解析耗时：" + (System.currentTimeMillis() - time));
                    JSONArray comments = new JSONArray(result);
                    List<ElemeCommentInfoModel> comment_list = new ArrayList<ElemeCommentInfoModel>();
                    long comment_time = System.currentTimeMillis();
                    for (int j = 0; j < comments.length(); j++) {
                        JSONObject comment = comments.getJSONObject(j);
                        ElemeCommentInfoModel elemeCommentInfoModel = new ElemeCommentInfoModel();
                        elemeCommentInfoModel.setCity_name(task.getCity_name());
                        elemeCommentInfoModel.setKeyword(task.getKeyword());
                        elemeCommentInfoModel.setRequest_id(task.getRequest_id());
                        elemeCommentInfoModel.setTask_id(task.getTask_id());
                        elemeCommentInfoModel.setCreate_time(new Date());
                        elemeCommentInfoModel.setShop_id(task.getShop_id());
                        elemeCommentInfoModel.setShop_name(task.getShop_name());
                        // 用户名
                        String username = null;
                        username = comment.getString("username");
                        elemeCommentInfoModel.setUser_name(username);
                        // 总体评分
                        String total_rating_star = null;
                        total_rating_star = comment.getString("rating_star");
                        elemeCommentInfoModel.setTotal_score(total_rating_star);
                        // 订单id
                        // String order_id = null;
                        // order_id = comment.getString("order_id");
                        // elemeCommentInfoModel.setOrder_id(order_id);
                        // 评论内容
                        String rating_text = null;
                        rating_text = comment.getString("rating_text");
                        elemeCommentInfoModel.setComment_content(filterUtils.removeImoji(rating_text));
                        // 送达时间
                        String time_spent_desc = null;
                        time_spent_desc = comment.getString("time_spent_desc");
                        elemeCommentInfoModel.setDelivery_time(time_spent_desc);
                        // 评论时间
                        String rated_at = null;
                        rated_at = comment.getString("rated_at");
                        elemeCommentInfoModel.setComment_time(rated_at);
                        String comment_items = "";
                        if (comment.has("item_ratings")) {
                            try {
                                JSONArray items = comment.getJSONArray("item_ratings");
                                for (int k = 0; k < items.length(); k++) {
                                    comment_items += items.getJSONObject(k).getString("food_name") + "("
                                            + items.getJSONObject(k).getString("food_id") + ");";
                                }
                                elemeCommentInfoModel.setComment_items(comment_items);
                            } catch (Exception e) {

                            }
                        }
                        if (comment.has("item_rating_list")) {
                            try {
                                JSONArray items = comment.getJSONArray("item_rating_list");
                                for (int k = 0; k < items.length(); k++) {
                                    comment_items += items.getJSONObject(k).getString("rate_name") + "("
                                            + items.getJSONObject(k).getString("food_id") + ")("
                                            + items.getJSONObject(k).getString("rating_star") + ")("
                                            + items.getJSONObject(k).getString("rating_text") + ");";
                                }
                                elemeCommentInfoModel.setComment_items(comment_items);
                            } catch (Exception e) {

                            }
                        }
                        // 商家回复
                        String reply_content = "";
                        if (comment.has("reply_text")) {
                            reply_content = comment.getString("reply_text");
                            if (StringUtils.isNotEmpty(reply_content)) {
                                elemeCommentInfoModel.setReplyContent(reply_content);
                            }
                        }
                        // 商家回复时间
                        String reply_time = "";
                        if (comment.has("reply_at")) {
                            reply_time = comment.getString("reply_at");
                            if (StringUtils.isNotEmpty(reply_time)) {
                                elemeCommentInfoModel.setReplyTime(reply_time);
                            }
                        }
                        elemeCommentInfoModel.setUnique_id(MD5Util.getMD5(task.getShop_id() + comment_items
                                + time_spent_desc + total_rating_star + username + rated_at + rating_text));
                        comment_list.add(elemeCommentInfoModel);
                    }
                    try {
                        TableName.set(task.getRemark());
                        biDao.fastInsert(comment_list);
                    } catch (Exception e1) {
                    } finally {
                        TableName.clear();
                    }
//                    logger.info("==========================手机端饿了么第" + i + "页Daily评论解析完成，耗时：" + (System.currentTimeMillis() - comment_time)
//                            + "============================");
                    if (i < page_to) {
                        tDao.update(ElemeCommentDailyTaskJob.class, Chain.make("page_from", (i + 1)), Cnd.where("id", "=", task.getId()));
                    }
                } catch (Exception e) {
                    logger.info("", e);
                    tDao.update(ElemeCommentDailyTaskJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
                    return;
                }
            }
            logger.info("==========================手机端饿了么Daily评论解析完成，总耗时：" + (System.currentTimeMillis() - time)
                    + "============================");
            tDao.update(ElemeCommentDailyTaskJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
        }
    }

    public static void main(String[] args) {
    }

}
