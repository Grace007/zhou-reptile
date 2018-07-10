package com.reptile.mobile_eleme.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.mobile_eleme.job.*;
import com.reptile.mobile_eleme.model.EleShopInfo;
import com.reptile.server.MyConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.TableName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 查排名 暂时不用
 */
public class MobileEleSearchKeywordThreadRanking extends Thread {

    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private EleMeSearchUrlJobRanking task;
    private static Logger logger = Logger.getLogger(MobileEleSearchKeywordThreadRanking.class);
    private boolean combine = MyConstant.MOBILE_ELEME_CONFIG.COMBINE;

    public MobileEleSearchKeywordThreadRanking(EleMeSearchUrlJobRanking task) {
        this.task = task;
    }

    @Override
    public void run() {

        try {
            long total_time = System.currentTimeMillis();
            logger.info("开始抓取手机端饿了么城市[" + task.getCity_name() + "]地址[" + task.getKeyword() + "]关键词["
                    + task.getShopKeyword() + "]的信息...");
            tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));

            TaskToBi tobi = BIDataSource.getBiDataSource("eleme", task.getProject_code(), task.getDown_type());
            Dao biDao = tobi.getBiDao();

            MobileCityId cityId = tDao.fetch(MobileCityId.class, Cnd.where("name", "=", task.getCity_name()));
            if (cityId == null) {
                logger.info("==========" + "饿了么根据关键词搜索结果为：无此城市的geo编码，城市为：" + task.getCity_name() + "==========");
                tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("status", 4), Cnd.where("id", "=", task.getId()));
                return;
            }

            // https://mainsite-restapi.ele.me/v1/pois?city_id=10&keyword=%E6%96%B0%E4%B8%96%E7%95%8C&type=search
            String city_id = cityId.getCity_id();
            String search_url = "https://mainsite-restapi.ele.me/v1/pois?city_id=" + city_id + "&keyword="
                    + java.net.URLEncoder.encode(task.getKeyword(), "utf-8") + "&type=search";
            String html = null;
            JSONObject jsonObject;
            try {
                html = HttpBase.get(search_url, "utf-8").getResult();
                JSONArray array = new JSONArray(html);
                if (array.length() > 0) {
                    jsonObject = array.getJSONObject(0);
                    String address = null;
                    String geohash = null;
                    double latitude = 0;
                    double longitude = 0;
                    if (jsonObject.has("address")) {
                        address = jsonObject.getString("address");
                    }
                    if (jsonObject.has("geohash")) {
                        geohash = jsonObject.getString("geohash");
                    }
                    if (jsonObject.has("latitude")) {
                        try {
                            latitude = Double.parseDouble(jsonObject.getString("latitude"));
                        } catch (Exception e) {

                        }
                    }
                    if (jsonObject.has("longitude")) {
                        try {
                            longitude = Double.parseDouble(jsonObject.getString("longitude"));
                        } catch (Exception e) {

                        }
                    }
                    if (!judge(Double.parseDouble(cityId.getLatitude()), Double.parseDouble(cityId.getLongitude()),
                            latitude, longitude) && !address.contains(task.getCity_name())) {
                        logger.info("==========" + "饿了么根据关键词搜索结果为：city_name与给得Adress不符，关键词为：" + task.getKeyword()
                                + "==========");
                        tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("status", 5),
                                Cnd.where("id", "=", task.getId()));
                        return;
                    }
                    JSONObject jsonOb = new JSONObject();
                    ElemeUrlTaskJob job = new ElemeUrlTaskJob();
                    int offset = 0;
                    int current_category = 0;
                    int k = 0;
                    try {
                        offset = task.getOffset();
                        current_category = task.getCurrent_category();
                        k = offset / 20;
                    } catch (Exception e1) {

                    }
                    // 生成category数组
                    List<ElemeCategory> list = new ArrayList<ElemeCategory>();
                    if (list.size() == 0) {// 选用固定数量的分类
                        try {
                            List<ElemeCategoryRanking> rankings = tDao.query(ElemeCategoryRanking.class, null);
                            for (int i = 0; i < rankings.size(); i++) {
                                ElemeCategory b = new ElemeCategory();
                                b.setCategory(rankings.get(i).getCategory());
                                b.setCategory_id(rankings.get(i).getCategory_id());
                                b.setSub_category(rankings.get(i).getSub_category());
                                list.add(b);
                            }
                        } catch (Exception e) {
                            logger.error("饿了么分类查询异常");
                        }
                    }
                    int total_shop = 0;
                    if (StringUtils.isNotEmpty(task.getGenerate_category())) {
                        for (int g = current_category; g < list.size(); g++) {
                            int rank = 1 + offset;
                            while (true) {
                                int shop_number = 0;
                                long time = System.currentTimeMillis();
                                if (StringUtils.isEmpty(task.getShopKeyword())) {
                                    String url = "https://mainsite-restapi.ele.me/shopping/restaurants?latitude="
                                            + latitude + "&longitude=" + longitude + "&offset=" + offset
                                            + "&limit=20&extras%5B%5D=activities&restaurant_category_ids%5B%5D="
                                            + list.get(g).getCategory_id();
                                    String result = null;
                                    try {
                                        result = HttpBase.get(url, "utf-8").getResult();
                                        // logger.info("饿了么地址解析耗时:" +
                                        // (System.currentTimeMillis() - time));
                                        JSONArray shopList = new JSONArray(result);
                                        shop_number += shopList.length();
                                        if (shopList.length() == 0) {
                                            logger.info("==========" + "此页无店铺信息，URL为：" + url + "==========");
                                            offset = 0;// 重置
                                            k = 0;
                                            tDao.update(EleMeSearchUrlJobRanking.class,
                                                    Chain.make("offset", offset).add("current_category", g),
                                                    Cnd.where("id", "=", task.getId()));
                                            break;
                                        }
                                        List<EleShopInfo> infoList = new ArrayList<EleShopInfo>();
                                        for (int i = 0; i < shopList.length(); i++) {
                                            jsonOb = shopList.getJSONObject(i);
                                            String activities = "";
                                            String shop_address = null;
                                            String average_cost = null;
                                            String delivery_mode = null;
                                            String description = null;
                                            String distance = null;
                                            String float_minimum_order_amount = null;
                                            String id = null;
                                            String name = null;
                                            String opening_hours = "";
                                            String order_lead_time = null;
                                            String phone = null;
                                            String delivery_fee = null;
                                            String delivery_fee_detail = null;
                                            String promotion_info = null;
                                            String rating = "0";
                                            String recent_order_num = null;
                                            String supports = "";
                                            String s_latitude = "";
                                            String s_longitude = "";
                                            if (jsonOb.has("activities")) {
                                                JSONArray array_activities = jsonOb.getJSONArray("activities");
                                                try {
                                                    for (int j = 0; j < array_activities.length(); j++) {
                                                        activities += array_activities.getJSONObject(j)
                                                                .getString("description") + ";";
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("address")) {
                                                shop_address = jsonOb.getString("address");
                                            }
                                            if (jsonOb.has("average_cost")) {
                                                average_cost = jsonOb.getString("average_cost");
                                            }
                                            if (jsonOb.has("delivery_mode")) {
                                                try {
                                                    delivery_mode = jsonOb.getJSONObject("delivery_mode")
                                                            .getString("text");
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("description")) {
                                                description = jsonOb.getString("description");
                                            }
                                            if (jsonOb.has("distance")) {
                                                distance = jsonOb.getString("distance");
                                            }
                                            if (jsonOb.has("float_minimum_order_amount")) {
                                                float_minimum_order_amount = jsonOb
                                                        .getString("float_minimum_order_amount");
                                            }
                                            if (jsonOb.has("id")) {
                                                id = jsonOb.getString("id");
                                            }
                                            if (jsonOb.has("name")) {
                                                name = jsonOb.getString("name");
                                            }
                                            if (jsonOb.has("opening_hours")) {
                                                JSONArray opening_hours_array = jsonOb.getJSONArray("opening_hours");
                                                try {
                                                    for (int j = 0; j < opening_hours_array.length(); j++) {
                                                        opening_hours += opening_hours_array.getString(j) + ";";
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("order_lead_time")) {
                                                order_lead_time = jsonOb.getString("order_lead_time");
                                            }
                                            if (jsonOb.has("phone")) {
                                                phone = jsonOb.getString("phone");
                                            }
                                            if (jsonOb.has("piecewise_agent_fee")) {
                                                try {
                                                    delivery_fee = jsonOb.getJSONObject("piecewise_agent_fee")
                                                            .getString("tips");
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("piecewise_agent_fee")) {
                                                try {
                                                    delivery_fee_detail = jsonOb.getJSONObject("piecewise_agent_fee")
                                                            .getString("description");
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("promotion_info")) {
                                                promotion_info = jsonOb.getString("promotion_info");
                                            }
                                            if (jsonOb.has("rating")) {
                                                rating = jsonOb.getString("rating");
                                            }
                                            if (jsonOb.has("recent_order_num")) {
                                                recent_order_num = jsonOb.getString("recent_order_num");
                                            }
                                            if (jsonOb.has("supports")) {
                                                JSONArray supports_array = jsonOb.getJSONArray("supports");
                                                try {
                                                    for (int j = 0; j < supports_array.length(); j++) {
                                                        supports += supports_array.getJSONObject(j)
                                                                .getString("description") + ";";
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("longitude")) {
                                                s_longitude = jsonOb.getString("longitude");
                                            }
                                            if (jsonOb.has("latitude")) {
                                                s_latitude = jsonOb.getString("latitude");
                                            }

                                            EleShopInfo shopInfo = new EleShopInfo();
                                            shopInfo.setRequest_id(task.getRequest_id());
                                            shopInfo.setTask_id(task.getTask_id());
                                            shopInfo.setCreate_date(new Date());
                                            shopInfo.setCreate_time(new Date());
                                            shopInfo.setRestaurant_id(id);
                                            shopInfo.setSname(name);
                                            shopInfo.setOpening_hours(opening_hours);
                                            shopInfo.setAverage_cost(average_cost);
                                            shopInfo.setAddress(shop_address);
                                            shopInfo.setActivities(activities);
                                            shopInfo.setShop_description(description);
                                            shopInfo.setUrl("https://h5.ele.me/shop/#geohash=" + geohash + "&id=" + id);
                                            shopInfo.setScore(rating);
                                            shopInfo.setMonthSale(recent_order_num);
                                            shopInfo.setMin_price(float_minimum_order_amount);
                                            shopInfo.setDelivery_detail(delivery_fee);
                                            shopInfo.setShipEfficiency(order_lead_time);
                                            shopInfo.setDistance(distance);
                                            shopInfo.setPhone(phone);
                                            shopInfo.setDelivery_fee_detail(delivery_fee_detail);
                                            shopInfo.setPromotion_info(promotion_info);
                                            shopInfo.setPayInfo(supports);
                                            shopInfo.setDelivery_mode(delivery_mode);
                                            shopInfo.setKeyword(task.getKeyword());
                                            shopInfo.setCity_name(task.getCity_name());
                                            shopInfo.setCategory(list.get(g).getCategory());
                                            shopInfo.setSub_category(list.get(g).getSub_category());
                                            shopInfo.setCategory_id(list.get(g).getCategory_id());
                                            shopInfo.setOffice_id(task.getOffice_id());
                                            shopInfo.setOffice_name(task.getOffice_name());
                                            shopInfo.setRank(rank + i);
                                            shopInfo.setUniqueId(MD5Util
                                                    .getMD5(task.getRequest_id() + id + list.get(g).getCategory_id()));
                                            infoList.add(shopInfo);
//                                            try {
//                                                TableName.set(task.getRemark());
//                                                biDao.fastInsert(shopInfo);
//                                                //logger.info("饿了么分类：" + list.get(g).getCategory() + "，rank：" + (rank + i));
//                                            } catch (Exception e) {
//                                                // logger.error(e);
//                                            } finally {
//                                                TableName.clear();
//                                            }
                                            job.setRequest_id(task.getRequest_id());
                                            job.setTask_id(task.getTask_id());
                                            job.setCreate_time(new Date());
                                            job.setCity_name(task.getCity_name());
                                            job.setKeyword(task.getKeyword());
                                            job.setUrl("https://h5.ele.me/shop/#geohash=" + geohash + "&id=" + id);
                                            job.setShop_id(id);
                                            job.setNode(task.getNode());
                                            job.setStatus(0);
                                            job.setProject_code(task.getProject_code());
                                            job.setDown_type(task.getDown_type());
                                            job.setRemark(task.getRemark());
                                            job.setLatitude(s_latitude);
                                            job.setLongitude(s_longitude);
                                            if (combine) {
                                                try {
                                                    tDao.fastInsert(job);
                                                } catch (Exception e) {
                                                    // logger.error(e);
                                                }
                                            }
                                        }
                                        try {
                                            TableName.set(task.getRemark());
                                            biDao.fastInsert(infoList);
                                            //logger.info("饿了么分类：" + list.get(g).getCategory() + "，rank：" + (rank + i));
                                        } catch (Exception e) {
                                            // logger.error(e);
                                        } finally {
                                            TableName.clear();
                                        }
                                        logger.info("下载饿了么第个" + g + "分类,第" + k + "页完成，共" + list.size() + "个分类，该页共"
                                                + shop_number + "家店铺，耗时:" + (System.currentTimeMillis() - time));
                                        total_shop += shop_number;
                                        k++;
                                        offset = 20 * k;
                                        rank += shopList.length();
                                        //if (k % 5 == 0) {
                                        tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("offset", offset),
                                                Cnd.where("id", "=", task.getId()));
                                        //}
                                    } catch (Exception e) {
//                                        logger.error("");
                                    }
                                } else {
                                    // https://mainsite-restapi.ele.me/v4/restaurants?extras%5B%5D=restaurant_activity&geohash=wx4fcphug4e&keyword=%E8%82%AF%E5%BE%B7%E5%9F%BA&type=search
                                    String url = "https://mainsite-restapi.ele.me/v4/restaurants?extras%5B%5D=restaurant_activity&geohash="
                                            + geohash + "&keyword="
                                            + java.net.URLEncoder.encode(task.getShopKeyword(), "utf-8")
                                            + "&type=search";
                                    String result = null;
                                    try {
                                        result = HttpBase.get(url, "utf-8").getResult();
                                        // logger.info("饿了么地址解析耗时:" +
                                        // (System.currentTimeMillis() - time));
                                        JSONArray shopList = new JSONArray(result);
                                        shop_number += shopList.length();
                                        if (shopList.length() == 0) {
                                            logger.info("==========" + "此页无店铺信息，URL为：" + url + "==========");
                                            break;
                                        }
                                        List<EleShopInfo> infoList = new ArrayList<EleShopInfo>();
                                        for (int i = 0; i < shopList.length(); i++) {
                                            jsonOb = shopList.getJSONObject(i);
                                            String activities = "";
                                            String shop_address = null;
                                            String average_cost = null;
                                            String delivery_mode = null;
                                            String description = null;
                                            String distance = null;
                                            String float_minimum_order_amount = null;
                                            String id = null;
                                            String name = null;
                                            String opening_hours = "";
                                            String order_lead_time = null;
                                            String phone = null;
                                            String delivery_fee = null;
                                            String delivery_fee_detail = null;
                                            String promotion_info = null;
                                            String rating = "0";
                                            String recent_order_num = null;
                                            String supports = "";
                                            String s_latitude = "";
                                            String s_longitude = "";
                                            if (jsonOb.has("restaurant_activity")) {
                                                JSONArray array_activities = jsonOb.getJSONArray("restaurant_activity");
                                                try {
                                                    for (int j = 0; j < array_activities.length(); j++) {
                                                        activities += array_activities.getJSONObject(j)
                                                                .getString("description") + ";";
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("address")) {
                                                shop_address = jsonOb.getString("address");
                                            }
                                            if (jsonOb.has("average_cost")) {
                                                address = jsonOb.getString("average_cost");
                                            }
                                            if (jsonOb.has("delivery_mode")) {
                                                try {
                                                    delivery_mode = jsonOb.getJSONObject("delivery_mode")
                                                            .getString("text");
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("description")) {
                                                description = jsonOb.getString("description");
                                            }
                                            if (jsonOb.has("distance")) {
                                                distance = jsonOb.getString("distance");
                                            }
                                            if (jsonOb.has("delivery_fee")) {
                                                float_minimum_order_amount = jsonOb.getString("delivery_fee");
                                            }
                                            if (jsonOb.has("id")) {
                                                id = jsonOb.getString("id");
                                            }
                                            if (jsonOb.has("name")) {
                                                name = jsonOb.getString("name");
                                            }
                                            if (jsonOb.has("opening_hours")) {
                                                JSONArray opening_hours_array = jsonOb.getJSONArray("opening_hours");
                                                try {
                                                    for (int j = 0; j < opening_hours_array.length(); j++) {
                                                        opening_hours += opening_hours_array.getString(j) + ";";
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("order_lead_time")) {
                                                order_lead_time = jsonOb.getString("order_lead_time");
                                            }
                                            if (jsonOb.has("phone")) {
                                                phone = jsonOb.getString("phone");
                                            }
                                            if (jsonOb.has("piecewise_agent_fee")) {
                                                try {
                                                    delivery_fee = jsonOb.getJSONObject("piecewise_agent_fee")
                                                            .getString("tips");
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("piecewise_agent_fee")) {
                                                try {
                                                    delivery_fee_detail = jsonOb.getJSONObject("piecewise_agent_fee")
                                                            .getString("description");
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("promotion_info")) {
                                                promotion_info = jsonOb.getString("promotion_info");
                                            }
                                            if (jsonOb.has("rating")) {
                                                rating = jsonOb.getString("rating");
                                            }
                                            if (jsonOb.has("recent_order_num")) {
                                                recent_order_num = jsonOb.getString("recent_order_num");
                                            }
                                            if (jsonOb.has("supports")) {
                                                JSONArray supports_array = jsonOb.getJSONArray("supports");
                                                try {
                                                    for (int j = 0; j < supports_array.length(); j++) {
                                                        supports += supports_array.getJSONObject(j)
                                                                .getString("description") + ";";
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (jsonOb.has("latitude")) {
                                                s_latitude = jsonOb.getString("latitude");
                                            }
                                            if (jsonOb.has("longitude")) {
                                                s_longitude = jsonOb.getString("longitude");
                                            }

                                            EleShopInfo shopInfo = new EleShopInfo();
                                            shopInfo.setRequest_id(task.getRequest_id());
                                            shopInfo.setTask_id(task.getTask_id());
                                            shopInfo.setCreate_date(new Date());
                                            shopInfo.setCreate_time(new Date());
                                            shopInfo.setRestaurant_id(id);
                                            shopInfo.setSname(name);
                                            shopInfo.setOpening_hours(opening_hours);
                                            shopInfo.setAverage_cost(average_cost);
                                            shopInfo.setAddress(shop_address);
                                            shopInfo.setActivities(activities);
                                            shopInfo.setShop_description(description);
                                            shopInfo.setUrl("https://h5.ele.me/shop/#geohash=" + geohash + "&id=" + id);
                                            shopInfo.setScore(rating);
                                            shopInfo.setMonthSale(recent_order_num);
                                            shopInfo.setMin_price(float_minimum_order_amount);
                                            shopInfo.setDelivery_detail(delivery_fee);
                                            shopInfo.setShipEfficiency(order_lead_time);
                                            shopInfo.setDistance(distance);
                                            shopInfo.setPhone(phone);
                                            shopInfo.setDelivery_fee_detail(delivery_fee_detail);
                                            shopInfo.setPromotion_info(promotion_info);
                                            shopInfo.setPayInfo(supports);
                                            shopInfo.setDelivery_mode(delivery_mode);
                                            shopInfo.setKeyword(task.getKeyword());
                                            shopInfo.setCity_name(task.getCity_name());
                                            shopInfo.setOffice_id(task.getOffice_id());
                                            shopInfo.setOffice_name(task.getOffice_name());
                                            shopInfo.setUniqueId(MD5Util.getMD5(task.getRequest_id() + id));
                                            infoList.add(shopInfo);
//                                            try {
//                                                TableName.set(task.getRemark());
//                                                biDao.fastInsert(shopInfo);
//                                            } catch (Exception e) {
//
//                                            } finally {
//                                                TableName.clear();
//                                            }
                                            job.setRequest_id(task.getRequest_id());
                                            job.setTask_id(task.getTask_id());
                                            job.setCreate_time(new Date());
                                            job.setCity_name(task.getCity_name());
                                            job.setKeyword(task.getKeyword());
                                            job.setUrl("https://h5.ele.me/shop/#geohash=" + geohash + "&id=" + id);
                                            job.setShop_id(id);
                                            job.setNode(task.getNode());
                                            job.setStatus(0);
                                            job.setProject_code(task.getProject_code());
                                            job.setDown_type(task.getDown_type());
                                            job.setRemark(task.getRemark());
                                            job.setLongitude(s_longitude);
                                            job.setLatitude(s_latitude);
                                            if (combine) {
                                                try {
                                                    tDao.fastInsert(job);
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                        try {
                                            TableName.set(task.getRemark());
                                            biDao.fastInsert(infoList);
                                        } catch (Exception e) {

                                        } finally {
                                            TableName.clear();
                                        }
                                        logger.info("下载饿了么第" + k + "页完成，该页共" + shop_number + "家店铺，耗时:"
                                                + (System.currentTimeMillis() - time));
                                        tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("offset", shop_number),
                                                Cnd.where("id", "=", task.getId()));
                                        total_shop += shop_number;
                                        // 有店铺关键词的话只返回一次，不翻页
                                        break;
                                    } catch (Exception e) {
//                                        logger.error("");
                                    }
                                }
                            }
                            if (StringUtils.isNotEmpty(task.getShopKeyword())) {
                                break;
                            }
                        }
                    } else {
                        int rank = 1 + offset;
                        while (true) {
                            int shop_number = 0;
                            long time = System.currentTimeMillis();
                            if (StringUtils.isEmpty(task.getShopKeyword())) {
                                String url = "https://mainsite-restapi.ele.me/shopping/restaurants?latitude=" + latitude
                                        + "&longitude=" + longitude + "&offset=" + offset
                                        + "&limit=20&extras%5B%5D=activities";
                                String result = null;
                                try {
                                    result = HttpBase.get(url, "utf-8").getResult();
                                    // logger.info("饿了么地址解析耗时:" +
                                    // (System.currentTimeMillis() - time));
                                    JSONArray shopList = new JSONArray(result);
                                    shop_number += shopList.length();
                                    if (shopList.length() == 0) {
                                        logger.info("==========" + "此页无店铺信息，URL为：" + url + "==========");
                                        tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("offset", offset),
                                                Cnd.where("id", "=", task.getId()));
                                        break;
                                    }
                                    List<EleShopInfo> infoList = new ArrayList<EleShopInfo>();
                                    for (int i = 0; i < shopList.length(); i++) {
                                        jsonOb = shopList.getJSONObject(i);
                                        String activities = "";
                                        String shop_address = null;
                                        String average_cost = null;
                                        String delivery_mode = null;
                                        String description = null;
                                        String distance = null;
                                        String float_minimum_order_amount = null;
                                        String id = null;
                                        String name = null;
                                        String opening_hours = "";
                                        String order_lead_time = null;
                                        String phone = null;
                                        String delivery_fee = null;
                                        String delivery_fee_detail = null;
                                        String promotion_info = null;
                                        String rating = "0";
                                        String recent_order_num = null;
                                        String supports = "";
                                        String s_latitude = "";
                                        String s_longitude = "";
                                        if (jsonOb.has("activities")) {
                                            JSONArray array_activities = jsonOb.getJSONArray("activities");
                                            try {
                                                for (int j = 0; j < array_activities.length(); j++) {
                                                    activities += array_activities.getJSONObject(j)
                                                            .getString("description") + ";";
                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("address")) {
                                            shop_address = jsonOb.getString("address");
                                        }
                                        if (jsonOb.has("average_cost")) {
                                            average_cost = jsonOb.getString("average_cost");
                                        }
                                        if (jsonOb.has("delivery_mode")) {
                                            try {
                                                delivery_mode = jsonOb.getJSONObject("delivery_mode").getString("text");
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("description")) {
                                            description = jsonOb.getString("description");
                                        }
                                        if (jsonOb.has("distance")) {
                                            distance = jsonOb.getString("distance");
                                        }
                                        if (jsonOb.has("float_minimum_order_amount")) {
                                            float_minimum_order_amount = jsonOb.getString("float_minimum_order_amount");
                                        }
                                        if (jsonOb.has("id")) {
                                            id = jsonOb.getString("id");
                                        }
                                        if (jsonOb.has("name")) {
                                            name = jsonOb.getString("name");
                                        }
                                        if (jsonOb.has("opening_hours")) {
                                            JSONArray opening_hours_array = jsonOb.getJSONArray("opening_hours");
                                            try {
                                                for (int j = 0; j < opening_hours_array.length(); j++) {
                                                    opening_hours += opening_hours_array.getString(j) + ";";
                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("order_lead_time")) {
                                            order_lead_time = jsonOb.getString("order_lead_time");
                                        }
                                        if (jsonOb.has("phone")) {
                                            phone = jsonOb.getString("phone");
                                        }
                                        if (jsonOb.has("piecewise_agent_fee")) {
                                            try {
                                                delivery_fee = jsonOb.getJSONObject("piecewise_agent_fee")
                                                        .getString("tips");
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("piecewise_agent_fee")) {
                                            try {
                                                delivery_fee_detail = jsonOb.getJSONObject("piecewise_agent_fee")
                                                        .getString("description");
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("promotion_info")) {
                                            promotion_info = jsonOb.getString("promotion_info");
                                        }
                                        if (jsonOb.has("rating")) {
                                            rating = jsonOb.getString("rating");
                                        }
                                        if (jsonOb.has("recent_order_num")) {
                                            recent_order_num = jsonOb.getString("recent_order_num");
                                        }
                                        if (jsonOb.has("supports")) {
                                            JSONArray supports_array = jsonOb.getJSONArray("supports");
                                            try {
                                                for (int j = 0; j < supports_array.length(); j++) {
                                                    supports += supports_array.getJSONObject(j).getString("description")
                                                            + ";";
                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("latitude")) {
                                            s_latitude = jsonOb.getString("latitude");
                                        }
                                        if (jsonOb.has("longitude")) {
                                            s_longitude = jsonOb.getString("longitude");
                                        }

                                        EleShopInfo shopInfo = new EleShopInfo();
                                        shopInfo.setRequest_id(task.getRequest_id());
                                        shopInfo.setTask_id(task.getTask_id());
                                        shopInfo.setCreate_date(new Date());
                                        shopInfo.setCreate_time(new Date());
                                        shopInfo.setRestaurant_id(id);
                                        shopInfo.setSname(name);
                                        shopInfo.setOpening_hours(opening_hours);
                                        shopInfo.setAverage_cost(average_cost);
                                        shopInfo.setAddress(shop_address);
                                        shopInfo.setActivities(activities);
                                        shopInfo.setShop_description(description);
                                        shopInfo.setUrl("https://h5.ele.me/shop/#geohash=" + geohash + "&id=" + id);
                                        shopInfo.setScore(rating);
                                        shopInfo.setMonthSale(recent_order_num);
                                        shopInfo.setMin_price(float_minimum_order_amount);
                                        shopInfo.setDelivery_detail(delivery_fee);
                                        shopInfo.setShipEfficiency(order_lead_time);
                                        shopInfo.setDistance(distance);
                                        shopInfo.setPhone(phone);
                                        shopInfo.setDelivery_fee_detail(delivery_fee_detail);
                                        shopInfo.setPromotion_info(promotion_info);
                                        shopInfo.setPayInfo(supports);
                                        shopInfo.setDelivery_mode(delivery_mode);
                                        shopInfo.setKeyword(task.getKeyword());
                                        shopInfo.setCity_name(task.getCity_name());
                                        shopInfo.setRank(rank + i);
                                        shopInfo.setOffice_id(task.getOffice_id());
                                        shopInfo.setOffice_name(task.getOffice_name());
                                        shopInfo.setUniqueId(MD5Util.getMD5(task.getRequest_id() + id));
                                        infoList.add(shopInfo);
//                                        try {
//                                            TableName.set(task.getRemark());
//                                            biDao.fastInsert(shopInfo);
//                                        } catch (Exception e) {
//                                            // logger.error(e);
//                                        } finally {
//                                            TableName.clear();
//                                        }
                                        job.setRequest_id(task.getRequest_id());
                                        job.setTask_id(task.getTask_id());
                                        job.setCreate_time(new Date());
                                        job.setCity_name(task.getCity_name());
                                        job.setKeyword(task.getKeyword());
                                        job.setUrl("https://h5.ele.me/shop/#geohash=" + geohash + "&id=" + id);
                                        job.setShop_id(id);
                                        job.setNode(task.getNode());
                                        job.setStatus(0);
                                        job.setProject_code(task.getProject_code());
                                        job.setDown_type(task.getDown_type());
                                        job.setRemark(task.getRemark());
                                        job.setLatitude(s_latitude);
                                        job.setLongitude(s_longitude);
                                        if (combine) {
                                            try {
                                                tDao.fastInsert(job);
                                            } catch (Exception e) {
                                                // logger.error(e);
                                            }
                                        }
                                    }
                                    try {
                                        TableName.set(task.getRemark());
                                        biDao.fastInsert(infoList);
                                    } catch (Exception e) {
                                        // logger.error(e);
                                    } finally {
                                        TableName.clear();
                                    }
                                    logger.info("下载饿了么第" + k + "页完成，该页共" + shop_number + "家店铺，耗时:"
                                            + (System.currentTimeMillis() - time));
                                    total_shop += shop_number;
                                    k++;
                                    offset = 20 * k;
                                    rank += shopList.length();
                                    //if (k % 5 == 0) {
                                    tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("offset", offset),
                                            Cnd.where("id", "=", task.getId()));
                                    //}
                                } catch (Exception e) {
//                                    logger.error("");
                                }
                            } else {
                                // https://mainsite-restapi.ele.me/v4/restaurants?extras%5B%5D=restaurant_activity&geohash=wx4fcphug4e&keyword=%E8%82%AF%E5%BE%B7%E5%9F%BA&type=search
                                String url = "https://mainsite-restapi.ele.me/v4/restaurants?extras%5B%5D=restaurant_activity&geohash="
                                        + geohash + "&keyword="
                                        + java.net.URLEncoder.encode(task.getShopKeyword(), "utf-8") + "&type=search";
                                String result = null;
                                try {
                                    result = HttpBase.get(url, "utf-8").getResult();
                                    // logger.info("饿了么地址解析耗时:" +
                                    // (System.currentTimeMillis() - time));
                                    JSONArray shopList = new JSONArray(result);
                                    shop_number += shopList.length();
                                    if (shopList.length() == 0) {
                                        logger.info("==========" + "此页无店铺信息，URL为：" + url + "==========");
                                        break;
                                    }
                                    List<EleShopInfo> infoList = new ArrayList<EleShopInfo>();
                                    for (int i = 0; i < shopList.length(); i++) {
                                        jsonOb = shopList.getJSONObject(i);
                                        String activities = "";
                                        String shop_address = null;
                                        String average_cost = null;
                                        String delivery_mode = null;
                                        String description = null;
                                        String distance = null;
                                        String float_minimum_order_amount = null;
                                        String id = null;
                                        String name = null;
                                        String opening_hours = "";
                                        String order_lead_time = null;
                                        String phone = null;
                                        String delivery_fee = null;
                                        String delivery_fee_detail = null;
                                        String promotion_info = null;
                                        String rating = "0";
                                        String recent_order_num = null;
                                        String supports = "";
                                        String s_latitude = "";
                                        String s_longitude = "";
                                        if (jsonOb.has("restaurant_activity")) {
                                            JSONArray array_activities = jsonOb.getJSONArray("restaurant_activity");
                                            try {
                                                for (int j = 0; j < array_activities.length(); j++) {
                                                    activities += array_activities.getJSONObject(j)
                                                            .getString("description") + ";";
                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("address")) {
                                            shop_address = jsonOb.getString("address");
                                        }
                                        if (jsonOb.has("average_cost")) {
                                            address = jsonOb.getString("average_cost");
                                        }
                                        if (jsonOb.has("delivery_mode")) {
                                            try {
                                                delivery_mode = jsonOb.getJSONObject("delivery_mode").getString("text");
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("description")) {
                                            description = jsonOb.getString("description");
                                        }
                                        if (jsonOb.has("distance")) {
                                            distance = jsonOb.getString("distance");
                                        }
                                        if (jsonOb.has("delivery_fee")) {
                                            float_minimum_order_amount = jsonOb.getString("delivery_fee");
                                        }
                                        if (jsonOb.has("id")) {
                                            id = jsonOb.getString("id");
                                        }
                                        if (jsonOb.has("name")) {
                                            name = jsonOb.getString("name");
                                        }
                                        if (jsonOb.has("opening_hours")) {
                                            JSONArray opening_hours_array = jsonOb.getJSONArray("opening_hours");
                                            try {
                                                for (int j = 0; j < opening_hours_array.length(); j++) {
                                                    opening_hours += opening_hours_array.getString(j) + ";";
                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("order_lead_time")) {
                                            order_lead_time = jsonOb.getString("order_lead_time");
                                        }
                                        if (jsonOb.has("phone")) {
                                            phone = jsonOb.getString("phone");
                                        }
                                        if (jsonOb.has("piecewise_agent_fee")) {
                                            try {
                                                delivery_fee = jsonOb.getJSONObject("piecewise_agent_fee")
                                                        .getString("tips");
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("piecewise_agent_fee")) {
                                            try {
                                                delivery_fee_detail = jsonOb.getJSONObject("piecewise_agent_fee")
                                                        .getString("description");
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("promotion_info")) {
                                            promotion_info = jsonOb.getString("promotion_info");
                                        }
                                        if (jsonOb.has("rating")) {
                                            rating = jsonOb.getString("rating");
                                        }
                                        if (jsonOb.has("recent_order_num")) {
                                            recent_order_num = jsonOb.getString("recent_order_num");
                                        }
                                        if (jsonOb.has("supports")) {
                                            JSONArray supports_array = jsonOb.getJSONArray("supports");
                                            try {
                                                for (int j = 0; j < supports_array.length(); j++) {
                                                    supports += supports_array.getJSONObject(j).getString("description")
                                                            + ";";
                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                        if (jsonOb.has("latitude")) {
                                            s_latitude = jsonOb.getString("latitude");
                                        }
                                        if (jsonOb.has("longitude")) {
                                            s_longitude = jsonOb.getString("longitude");
                                        }

                                        EleShopInfo shopInfo = new EleShopInfo();
                                        shopInfo.setRequest_id(task.getRequest_id());
                                        shopInfo.setTask_id(task.getTask_id());
                                        shopInfo.setCreate_date(new Date());
                                        shopInfo.setCreate_time(new Date());
                                        shopInfo.setRestaurant_id(id);
                                        shopInfo.setSname(name);
                                        shopInfo.setOpening_hours(opening_hours);
                                        shopInfo.setAverage_cost(average_cost);
                                        shopInfo.setAddress(shop_address);
                                        shopInfo.setActivities(activities);
                                        shopInfo.setShop_description(description);
                                        shopInfo.setUrl("https://h5.ele.me/shop/#geohash=" + geohash + "&id=" + id);
                                        shopInfo.setScore(rating);
                                        shopInfo.setMonthSale(recent_order_num);
                                        shopInfo.setMin_price(float_minimum_order_amount);
                                        shopInfo.setDelivery_detail(delivery_fee);
                                        shopInfo.setShipEfficiency(order_lead_time);
                                        shopInfo.setDistance(distance);
                                        shopInfo.setPhone(phone);
                                        shopInfo.setDelivery_fee_detail(delivery_fee_detail);
                                        shopInfo.setPromotion_info(promotion_info);
                                        shopInfo.setPayInfo(supports);
                                        shopInfo.setDelivery_mode(delivery_mode);
                                        shopInfo.setKeyword(task.getKeyword());
                                        shopInfo.setCity_name(task.getCity_name());
                                        shopInfo.setOffice_id(task.getOffice_id());
                                        shopInfo.setOffice_name(task.getOffice_name());
                                        shopInfo.setUniqueId(MD5Util.getMD5(task.getRequest_id() + id));
                                        infoList.add(shopInfo);
//                                        try {
//                                            TableName.set(task.getRemark());
//                                            biDao.fastInsert(shopInfo);
//                                        } catch (Exception e) {
//
//                                        } finally {
//                                            TableName.clear();
//                                        }
                                        job.setRequest_id(task.getRequest_id());
                                        job.setTask_id(task.getTask_id());
                                        job.setCreate_time(new Date());
                                        job.setCity_name(task.getCity_name());
                                        job.setKeyword(task.getKeyword());
                                        job.setUrl("https://h5.ele.me/shop/#geohash=" + geohash + "&id=" + id);
                                        job.setShop_id(id);
                                        job.setNode(task.getNode());
                                        job.setStatus(0);
                                        job.setProject_code(task.getProject_code());
                                        job.setDown_type(task.getDown_type());
                                        job.setRemark(task.getRemark());
                                        job.setLatitude(s_latitude);
                                        job.setLongitude(s_longitude);
                                        if (combine) {
                                            try {
                                                tDao.fastInsert(job);
                                            } catch (Exception e) {

                                            }
                                        }
                                    }
                                    try {
                                        TableName.set(task.getRemark());
                                        biDao.fastInsert(infoList);
                                    } catch (Exception e) {

                                    } finally {
                                        TableName.clear();
                                    }
                                    logger.info("下载饿了么第" + k + "页完成，该页共" + shop_number + "家店铺，耗时:"
                                            + (System.currentTimeMillis() - time));
                                    tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("offset", shop_number),
                                            Cnd.where("id", "=", task.getId()));
                                    total_shop += shop_number;
                                    // 有店铺关键词的话只返回一次，不翻页
                                    break;
                                } catch (Exception e) {
//                                    logger.error("");
                                }
                            }
                        }
                    }
                    logger.info("==========" + "饿了么根据关键词解析成功，总店铺数量：" + total_shop + "，关键词为：" + task.getKeyword()
                            + "==========，总耗时：" + (System.currentTimeMillis() - total_time));
                    tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
                } else {
                    tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
                }
            } catch (Exception e2) {
//                logger.error("");
                tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            }

        } catch (Exception e) {
            tDao.update(EleMeSearchUrlJobRanking.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            logger.error("==========" + "饿了么根据关键词解析失败，URL为：" + task.getUrl() + "==========");
        }

    }

    public boolean judge(double lat1, double lon1, double lat2, double lon2) {
        boolean flag = false;
        if ((Math.abs(lat2 - lat1) < 1) && (Math.abs(lon2 - lon1) < 1)) {
            flag = true;
        }
        return flag;
    }

    public List<ElemeCategory> getCategories(double lat, double lon) {
        List<ElemeCategory> cates = new ArrayList<ElemeCategory>();
        String cate_url = "https://mainsite-restapi.ele.me/shopping/v2/restaurant/category?latitude=" + lat
                + "&longitude=" + lon;
        String result = null;
        try {
            result = HttpBase.get(cate_url, "utf-8").getResult();
            JSONArray array = new JSONArray(result);
            if (array.length() > 1) {
                for (int i = 1; i < array.length(); i++) {
                    if (array.getJSONObject(i).has("sub_categories")) {
                        JSONArray sub_array = array.getJSONObject(i).getJSONArray("sub_categories");
                        for (int j = 1; j < sub_array.length(); j++) {
                            ElemeCategory e = new ElemeCategory();
                            e.setCategory(array.getJSONObject(i).getString("name"));
                            e.setSub_category(sub_array.getJSONObject(j).getString("name"));
                            e.setCategory_id(sub_array.getJSONObject(j).getString("id"));
                            cates.add(e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("手机端饿了么获取分类异常");
        }
        return cates;
    }

}
