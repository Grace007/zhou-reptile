package com.reptile.mobile_baidu_waimai.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiCommentDailyTaskJob;
import com.reptile.mobile_baidu_waimai.model.BaiduWaimaiCommentInfoModel;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MobileBaiduWaimaiCommentDailyThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileBaiduWaimaiCommentDailyThread.class);
    private BaiduWaimaiCommentDailyTaskJob task;

    public MobileBaiduWaimaiCommentDailyThread(BaiduWaimaiCommentDailyTaskJob task) {
        this.task = task;
    }

    DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat DATA_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        TaskToBi tobi = BIDataSource.getBiDataSource("baidu_waimai", task.getProject_code(), task.getDown_type());
        Dao biDao = tobi.getBiDao();
        tDao.update(BaiduWaimaiCommentDailyTaskJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        String shop_id = task.getShop_id();
        if (StringUtils.isNotEmpty(shop_id)) {
            int page_from = task.getPage_from();
            int page_to = task.getPage_to();
            EmojiFilterUtils filterUtils = new EmojiFilterUtils();
            for (int i = page_from; i <= page_to; i++) {
                String url = "http://waimai.baidu.com/waimai/comment/getshop?display=json&shop_id=" + shop_id + "&page=" + i + "&count=60";
                String result;
                try {
                    result = HttpBase.get(url, "utf-8").getResult();
                    //logger.info("百度Daily评论第" + i + "页解析耗时:" + (System.currentTimeMillis() - time));
                    JSONObject comments = new JSONObject(result);
                    JSONArray contents = comments.getJSONObject("result").getJSONArray("content");
                    List<BaiduWaimaiCommentInfoModel> comment_list = new ArrayList<BaiduWaimaiCommentInfoModel>();
                    long comment_time = System.currentTimeMillis();
                    for (int j = 0; j < contents.length(); j++) {
                        JSONObject content = contents.getJSONObject(j);
                        BaiduWaimaiCommentInfoModel baiduWaimaiCommentInfoModel = new BaiduWaimaiCommentInfoModel();
                        baiduWaimaiCommentInfoModel.setCity_name(task.getCity_name());
                        baiduWaimaiCommentInfoModel.setKeyword(task.getKeyword());
                        baiduWaimaiCommentInfoModel.setRequest_id(task.getRequest_id());
                        baiduWaimaiCommentInfoModel.setTask_id(task.getTask_id());
                        baiduWaimaiCommentInfoModel.setCreate_time(new Date());
                        baiduWaimaiCommentInfoModel.setShop_id(task.getShop_id());
                        baiduWaimaiCommentInfoModel.setShop_name(task.getShop_name());
                        // 评论ID
                        String comment_id = "";
                        comment_id = content.getString("comment_id");
                        baiduWaimaiCommentInfoModel.setComment_id(comment_id);
                        // 用户名
                        baiduWaimaiCommentInfoModel.setUser_name(content.getString("pass_name"));
                        // 评分
                        baiduWaimaiCommentInfoModel.setScore(content.getString("score"));
                        // 送餐时间
                        baiduWaimaiCommentInfoModel.setDelivery_time(content.getString("cost_time") + "分钟");
                        // 评价时间
                        baiduWaimaiCommentInfoModel.setComment_time(content.getString("create_time"));
                        // 推荐商品
                        String recommend_dishes = "";
                        try {
                            for (int k = 0; k < content.getJSONArray("recommend_dishes").length(); k++) {
                                recommend_dishes = recommend_dishes + content.getJSONArray("recommend_dishes").getString(k)
                                        + ";";
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();
                        }
                        baiduWaimaiCommentInfoModel.setRecommended_product(recommend_dishes);
                        // 评论标签
                        String comment_labels = "";
                        try {
                            for (int k = 0; k < content.getJSONArray("comment_labels").length(); k++) {
                                comment_labels = comment_labels
                                        + content.getJSONArray("comment_labels").getJSONObject(k).getString("content")
                                        + ";";
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();
                        }
                        baiduWaimaiCommentInfoModel.setComment_labels(comment_labels);
                        // 评论内容
                        baiduWaimaiCommentInfoModel
                                .setComment_content(filterUtils.removeImoji(content.getString("content")));
                        // 商家回复
                        String reply_content = "";
                        if (content.has("reply_content")) {
                            reply_content = content.getString("reply_content");
                            if (StringUtils.isNotEmpty(reply_content)) {
                                baiduWaimaiCommentInfoModel.setReplyContent(reply_content);
                            }
                        }
                        // 商家回复时间
                        String reply_time = "";
                        if (content.has("reply_create_time")) {
                            reply_time = content.getString("reply_create_time");
                            if (!"0".equals(reply_time)) {
                                try {
                                    long m = Long.parseLong(reply_time) * 1000;
                                    Date reply_date = new Date();
                                    reply_date.setTime(m);
                                    baiduWaimaiCommentInfoModel.setReplyTime(DATA_FORMAT2.format(reply_date));
                                } catch (Exception e) {

                                }
                            }
                        }
                        baiduWaimaiCommentInfoModel
                                .setUnique_id(MD5Util.getMD5(task.getShop_id() + comment_id + task.getProject_code()));
                        comment_list.add(baiduWaimaiCommentInfoModel);
                    }
                    try {
                        TableName.set(task.getRemark());
                        biDao.fastInsert(comment_list);
                    } catch (Exception e) {
                    } finally {
                        TableName.clear();
                    }
//                    logger.info("==========================第" + i + "页Daily评论URL解析完成，耗时：" + (System.currentTimeMillis() - comment_time)
//                            + "============================");
                    if (i < page_to) {
                        tDao.update(BaiduWaimaiCommentDailyTaskJob.class, Chain.make("page_from", (i + 1)),
                                Cnd.where("id", "=", task.getId()));
                    }
                } catch (Exception e) {
                    logger.info("", e);
                    tDao.update(BaiduWaimaiCommentDailyTaskJob.class, Chain.make("status", 3),
                            Cnd.where("id", "=", task.getId()));
                    return;
                }
            }
            logger.info("==========================Daily评论URL解析完成，总耗时：" + (System.currentTimeMillis() - time)
                    + "============================");
            tDao.update(BaiduWaimaiCommentDailyTaskJob.class, Chain.make("status", 2),
                    Cnd.where("id", "=", task.getId()));
        } else {
            tDao.update(BaiduWaimaiCommentDailyTaskJob.class, Chain.make("status", -1),
                    Cnd.where("id", "=", task.getId()));
        }
    }

    public static void main(String[] args) {
    }
}
