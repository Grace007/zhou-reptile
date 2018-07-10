package com.reptile.mobile_eleme.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.reptile.mobile_eleme.ElemeHttpBase;
import com.reptile.mobile_eleme.job.ElemeProductTaskJob;
import com.reptile.mobile_eleme.job.ElemeUrlTaskJob;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 进入店铺，获取商品链接
 * 产生 ElemeProductTaskJob
 */
public class MobileElemeUrlThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileElemeUrlThread.class);
    private ElemeUrlTaskJob task;

    public MobileElemeUrlThread(ElemeUrlTaskJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        logger.info("==========================开始下载手机端饿了么URL============================");
        tDao.update(ElemeUrlTaskJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        String url = task.getUrl();
        // https://www.ele.me/shop/379954
        // https://h5.ele.me/shop/#id=699381
        String shop_id = null;
        if (url.contains("shop")) {
            try {
                shop_id = task.getShop_id();
                if (StringUtils.isEmpty(shop_id)) {
                    tDao.update(ElemeUrlTaskJob.class, Chain.make("status", -1), Cnd.where("id", "=", task.getId()));
                    return;
                }
            } catch (Exception e) {
                tDao.update(ElemeUrlTaskJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
                return;
            }
        }
        //店铺详情url
        String webUrl = null;
        if (shop_id != null) {
            // webUrl = "https://www.ele.me/restapi/v4/restaurants/" + shop_id
            // +
            // "?extras%5B%5D=food_activity&extras%5B%5D=certification&extras%5B%5D=license&extras%5B%5D=identification&extras%5B%5D=statistics&extras%5B%5D=album&extras%5B%5D=flavor&geohash=wtw95wtgv2zf";
            // webUrl = "https://mainsite-restapi.ele.me/shopping/restaurant/" +
            // shop_id
            // +
            // "?extras%5B%5D=activity&extras%5B%5D=license&extras%5B%5D=identification&extras%5B%5D=albums&extras%5B%5D=flavors?latitude=31.27059&longitude=121.47852";
            webUrl = "https://mainsite-restapi.ele.me/shopping/restaurant/" + shop_id
                    + "?extras%5B%5D=activities&extras%5B%5D=albums&extras%5B%5D=license&extras%5B%5D=identification";
        }
        String result = "";
        try {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Referer", "https://h5.ele.me/shop/");
            headers.put("Origin", "https://h5.ele.me");
            headers.put("Host", "mainsite-restapi.ele.me");
            result = ElemeHttpBase.get(webUrl, "utf-8", headers).getResult();
        } catch (Exception e) {
            logger.info("", e);
        }
        //店铺详情url
        String webUrl2 = null;
        if (shop_id != null) {
            // webUrl2 = "https://www.ele.me/restapi/ugc/v1/restaurants/" +
            // shop_id
            // + "/rating_scores?latitude=31.149635&longitude=121.802566";
            // webUrl2 = "https://mainsite-restapi.ele.me/ugc/v1/restaurants/" +
            // shop_id
            // + "/rating_scores?latitude=31.27059&longitude=121.47852";
            webUrl2 = "https://mainsite-restapi.ele.me/ugc/v2/restaurants/" + shop_id + "/ratings/scores";
        }
        String result2 = "";
        try {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Referer", "https://h5.ele.me/shop/");
            headers.put("Origin", "https://h5.ele.me");
            headers.put("Host", "mainsite-restapi.ele.me");
            result2 = ElemeHttpBase.get(webUrl2, "utf-8", headers).getResult();
        } catch (Exception e) {
            logger.info("", e);
        }
        //店铺产品url
        String webUrl3 = null;
        if (shop_id != null) {
            // webUrl3 =
            // "https://www.ele.me/restapi/shopping/v1/menu?restaurant_id=" +
            // shop_id;
            // webUrl3 =
            // "https://mainsite-restapi.ele.me/shopping/v1/menu?restaurant_id="
            // + shop_id;
            webUrl3 = "https://mainsite-restapi.ele.me/shopping/v2/menu?restaurant_id=" + shop_id;
        }
        String result3 = "";
        try {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Referer", "https://h5.ele.me/shop/");
            headers.put("Origin", "https://h5.ele.me");
            headers.put("Host", "mainsite-restapi.ele.me");
            result3 = ElemeHttpBase.get(webUrl3, "utf-8", headers).getResult();
        } catch (Exception e) {
            logger.info("", e);
        }
        //店铺评价url
        String webUrl4 = null;
        if (shop_id != null) {
            // webUrl4 = "https://www.ele.me/restapi/ugc/v1/restaurant/" +
            // shop_id + "/rating_categories";
            webUrl4 = "https://mainsite-restapi.ele.me/ugc/v1/restaurant/" + shop_id + "/rating_categories";
        }
        String result4 = "";
        try {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Referer", "https://h5.ele.me/shop/");
            headers.put("Origin", "https://h5.ele.me");
            headers.put("Host", "mainsite-restapi.ele.me");
            result4 = ElemeHttpBase.get(webUrl4, "utf-8", headers).getResult();
        } catch (Exception e) {
            logger.info("", e);
        }
        ElemeProductTaskJob elemeProductTaskJob = new ElemeProductTaskJob();
        if (result.startsWith("{")) {
            try {
                JSONObject shopInfo = new JSONObject(result);
                if (result.contains("餐厅不存在")) {
                    tDao.update(ElemeUrlTaskJob.class, Chain.make("status", 404), Cnd.where("id", "=", task.getId()));
                    return;
                }
                // 店铺id
                String id = shopInfo.getString("id");
                // 店铺名称
                String name = shopInfo.getString("name");
                if ("SERVICE_REJECTED".equals(name)) {
                    tDao.update(ElemeUrlTaskJob.class, Chain.make("status", 0), Cnd.where("id", "=", task.getId()));
                    logger.info("店铺URL[" + task.getUrl() + "]服务拒绝，重置状态");
                    return;
                }
                if (StringUtils.isEmpty(name)) {
                    tDao.update(ElemeUrlTaskJob.class, Chain.make("status", -2), Cnd.where("id", "=", task.getId()));
                    return;
                }
                elemeProductTaskJob.setShop_name(name);
                // 店铺评分
                String rating = shopInfo.getString("rating");
                // 月销量
                String recent_order_num = shopInfo.getString("recent_order_num");
                // 店铺类型
                JSONArray flavor;
                try {
                    flavor = shopInfo.getJSONArray("flavor");
                } catch (Exception e) {
                    flavor = null;
                }
                try {
                    for (int i = 0; i < flavor.length(); i++) {
                        JSONObject jflavor = flavor.getJSONObject(i);
                        String flavor_name = jflavor.getString("flavor");
                    }
                } catch (Exception e) {

                }
                // 起送价
                String minimum_order_amount = shopInfo.getString("minimum_order_amount");
                // 配送费
                String delivery_fee = shopInfo.getString("delivery_fee");
                // 平均速度
                String order_lead_time = shopInfo.getString("order_lead_time");
                // 商家地址
                // 营业时间
                // 商家公告
            } catch (JSONException e) {
                logger.info("", e);
            }
        } else {
            logger.info("重置饿了么url状态");
            tDao.update(ElemeUrlTaskJob.class, Chain.make("status", 0), Cnd.where("id", "=", task.getId()));
            return;
        }
        elemeProductTaskJob.setShop_id(shop_id);
        elemeProductTaskJob.setCity_name(task.getCity_name());
        elemeProductTaskJob.setKeyword(task.getKeyword());
        elemeProductTaskJob.setRequest_id(task.getRequest_id());
        elemeProductTaskJob.setTask_id(task.getTask_id());
        elemeProductTaskJob.setCreate_time(new Date());
        elemeProductTaskJob.setUrl(task.getUrl());
        elemeProductTaskJob.setNode(task.getNode());
        elemeProductTaskJob.setLevel(0);
        elemeProductTaskJob.setShop_status(0);
        elemeProductTaskJob.setProduct_status(0);
        elemeProductTaskJob.setComment_status(0);
        elemeProductTaskJob.setProject_code(task.getProject_code());
        elemeProductTaskJob.setDown_type(task.getDown_type());
        if (result2.startsWith("{")) {
            try {
                JSONObject shopInfo2 = new JSONObject(result2);
                // 综合评价
                String star_level = shopInfo2.getString("star_level");
                // 与周边比较
                String compare_rating = shopInfo2.getString("compare_rating");
                // 服务评分
                String service_score = shopInfo2.getString("service_score");
                // 菜品评分
                String food_score = shopInfo2.getString("food_score");
            } catch (JSONException e) {
                logger.info("", e);
            }
        }
        if (result3.startsWith("[")) {
            try {
                int total = 0;
                JSONArray foods = new JSONArray(result3);
                for (int i = 0; i < foods.length(); i++) {
                    JSONObject jfood = new JSONObject();
                    // 按类别分
                    jfood = foods.getJSONObject(i);
                    JSONArray jfoods = jfood.getJSONArray("foods");
                    total = total + jfoods.length();
                }
                elemeProductTaskJob.setProduct_number(total);
            } catch (JSONException e) {
                logger.info("", e);
                elemeProductTaskJob.setProduct_number(-1);
            }
        }
        if (result4.startsWith("[")) {
            try {
                JSONArray amounts = new JSONArray(result4);
                JSONObject amount = amounts.getJSONObject(0);
                elemeProductTaskJob.setComment_number(Integer.parseInt(amount.getString("amount")));
            } catch (JSONException e) {
                elemeProductTaskJob.setComment_number(-1);
                logger.info("", e);
            }
        }
        elemeProductTaskJob.setRemark(task.getRemark());
        elemeProductTaskJob.setLatitude(task.getLatitude());
        elemeProductTaskJob.setLongitude(task.getLongitude());
        try {
            tDao.insert(elemeProductTaskJob);
        } catch (Exception e) {
            // logger.error("", e);
        }
        tDao.update(ElemeUrlTaskJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
        logger.info("==========================手机端饿了么URL解析完成，总耗时：" + (System.currentTimeMillis() - time)
                + "============================");
    }

    public static void main(String[] args) {
        ElemeUrlTaskJob elemeUrlTaskJob = new ElemeUrlTaskJob();
        elemeUrlTaskJob.setUrl("https://www.ele.me/shop/379954");
        MobileElemeUrlThread test = new MobileElemeUrlThread(elemeUrlTaskJob);
        test.start();
    }
}
