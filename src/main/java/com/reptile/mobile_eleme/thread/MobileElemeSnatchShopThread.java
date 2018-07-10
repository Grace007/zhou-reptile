package com.reptile.mobile_eleme.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.mobile_eleme.ElemeHttpBase;
import com.reptile.mobile_eleme.job.ElemeCookie;
import com.reptile.mobile_eleme.job.ElemeProductTaskJob;
import com.reptile.mobile_eleme.model.ElemeShopInfoModel;
import com.reptile.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.TableName;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * api获取店铺，商家 详情（需要cookie）
 */
public class MobileElemeSnatchShopThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileElemeSnatchShopThread.class);
    private ElemeProductTaskJob task;

    public MobileElemeSnatchShopThread(ElemeProductTaskJob task) {
        this.task = task;
    }

    DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run() {
        //logger.info("进入shop_status任务");
        long time = System.currentTimeMillis();
        TaskToBi tobi = BIDataSource.getBiDataSource("eleme", task.getProject_code(), task.getDown_type());
        if (tobi == null) {
            logger.info("连接异常");
            return;
        }
        Dao biDao = tobi.getBiDao();
        try {
            tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", 1), Cnd.where("id", "=", task.getId()));
        } catch (Exception e2) {
            logger.info("ElemeSnatchShopThread更新状态异常");
            return;
        }

        //随机获取cookie
        Random random = new Random();
        List<ElemeCookie> elemeCookieList = tDao.query(ElemeCookie.class,Cnd.where("status","=",2));
        ElemeCookie elemeCookie=null;
        if (elemeCookieList!=null){
            if (elemeCookieList.size()!=0){
                try {
                    int r = random.nextInt(elemeCookieList.size());
                    elemeCookie = elemeCookieList.get(r);
                }catch (Exception e){

                }
            }
        }
        String cookie = "";
        if (elemeCookie != null){
            cookie = elemeCookie.getCookie();
        }
        String url = task.getUrl();
        // https://www.ele.me/shop/379954
        String shop_id = null;
        if (url.contains("shop")) {
            try {
                shop_id = task.getShop_id();
                if (StringUtils.isEmpty(shop_id)) {
                    tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", -1),
                            Cnd.where("id", "=", task.getId()));
                    return;
                }
            } catch (Exception e) {
                tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", 3),
                        Cnd.where("id", "=", task.getId()));
                return;
            }
        }
        // 请求店铺信息
        String webUrl = null;
        if (shop_id != null) {

//            webUrl = "https://mainsite-restapi.ele.me/shopping/restaurant/" + shop_id
//                    + "?extras%5B%5D=activities&extras%5B%5D=albums&extras%5B%5D=license&extras%5B%5D=identification&terminal=h5";
            webUrl = "https://restapi.ele.me/shopping/restaurant/"+shop_id+"?extras%5B%5D=activities&extras%5B%5D=license&extras%5B%5D=identification&extras%5B%5D=flavors&extras%5B%5D=albums&extras%5B%5D=videos&extras%5B%5D=coupon" ;

        }
        String result = "";
        try {

            Map<String, String> headers = new HashMap<String, String>();
//            headers.put("Referer", "https://h5.ele.me/shop/");
//            headers.put("Origin", "https://h5.ele.me");
//            headers.put("Host", "mainsite-restapi.ele.me");
//            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
//            headers.put("cookie",cookie);

            String androidVersionStr = "" , apiLevelStr = "";
            String [] androidVersion = {"5.0" , "5.1" , "4.4.2" , "4.2.2"} ;

            String [] apiLevel = {"19" , "20" , "21" , "22"} ;

            String [] androidMobileType = {"MI_6_" , "OPPO_R11" , "HUAWEI_MLA-AL10" , "SM-G955F", "SM-G955N",
                    "vivo_X20A" , "vivo_X9i" , "vivo_X20" , "M6_Note", "Mi_Note_2"
            } ;

            String [] androidMobileTypeDetail = {"sagit" , "R11" , "MLA-AL10" , "SM-G955F", "SM-G955N",
                    "PD1709_" , "PD1624_" , "PD1709_" , "meizu_M6Note", "scorpio"
            } ;

            String [] androidMobileTypeSystem = {"NMF26X" , "NMF26X" , "HUAWEIMLA-AL10" , "JLS36C", "JLS36C",
                    "NMF26X" , "NMF26F" , "NMF26X" , "N2G47H", "NRD90M"
            } ;
            /**安卓版本**/
            int androidVersionInt = random.nextInt(androidVersion.length);
            androidVersionStr = androidVersion[androidVersionInt];

            /**安卓API**/
            int androidApiInt = random.nextInt(androidVersion.length);
            apiLevelStr = apiLevel[androidApiInt];

            int randomNumber = random.nextInt(androidMobileType.length);

            String userAgent = "Rajax/1 "+androidMobileType[randomNumber]+"/"+androidMobileTypeDetail[randomNumber]+" "
                    + "Android/"+androidVersionStr+" Display/"+androidMobileTypeDetail[randomNumber]+"-user_"+androidVersionStr+"_"+androidMobileTypeSystem[randomNumber]+"_"+"381180402"+"_release-keys "
                    + "Eleme/7.2 ID/"+ Utils.replaceCharRandomly("90d064f5-9421-3392-8e03-33727238f728")+"; "
                    + "KERNEL_VERSION:"+"3.4.0"+" "
                    + "API_Level:"+apiLevelStr+" "
                    + "Hardware:"+Utils.replaceCharRandomly("88c9215a68f1ad4237d45909792f1249") ;

            headers.put("Content-Type","application/json; charset=utf-8");
            headers.put("User-Agent",userAgent);
            headers.put("X-DeviceInfo",Utils.replaceCharRandomly("aW1laTozNTQ3MzAwMTAwODE1ODAgc2VyaWFsOjFkNWIwMjUxOTYxMDg5ZTAgYW5kcm9pZF9pZDowODllMDFkNWIwMjUxOTYxIGJyYW5kOnNhbXN1bmcgbW9kZWw6U00tRzk1NU4gbmV0d29ya09wZXJhdG9yOjQ2MDA3IG1hY0FkZHJlc3M6MDhfOUVfMDFfRDVfQjBfMjUgbmV0VHlwZTpXSUZJIHNpbVNlcmlhbE51bWJlcjo4OTg2MDA4MTAxMDgxNTgwMTIxMyBzaW1TdGF0ZTo1IGxhdGl0dWRlOjQzLjg1Njg2MzMzMzMzMzMzIGxvbmdpdHVkZToxMjYuNTYyNzU4MzMzMzMzMzIgY2lkOjYyMTQxMzM5IGxhYzo0NTg2MSB3aWZpTGlzdDowMV84MF9jMl8wMF8wMF8wMyBoYXZlQmx1ZXRvb3RoOnRydWUgaGFyZHdhcmVfaWQ6ODhjOTIxNWE2OGYxYWQ0MjM3ZDQ1OTA5NzkyZjEyNDkgZmlyc3Rfb3BlbjoxNTMwMTc5MzA5IGxhc3Rfb3BlbjoxNTMwMTgzNzA3IG5ldF90eXBlOndpZmkgZW5lcmd5X3BlcmNlbnQ6OTAgdHJhY2tfaWQ6IG1lbW9yeTo0Mzc="));
            headers.put("Host","restapi.ele.me");
            headers.put("Connection","Keep-Alive");
            headers.put("Accept-Encoding","gzip");
            headers.put("cookie","eleme__ele_me=b9aa8521b051ee3790189c4880472d3a%3A632dd0715d0c9013087785726a2e1732c5167101; track_id=1530179311%7C79105519cb828ad2e26e1014a3a6e70698a0dde066654309af%7Ce163cf17d3835eca1ffd51c9fc1f15cd");


//            Map<String, String> headers = new HashMap<String, String>();
//            headers.put("Referer", "https://h5.ele.me/shop/");
//            headers.put("Origin", "https://h5.ele.me");
//            headers.put("Host", "mainsite-restapi.ele.me");
//            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
////            headers.put("cookie","ubt_ssid=x8okiiwb97ouasa5fqdkgoobcgvkxevb_2017-10-09; _utrace=6e3186bb4eee67eaa35402de155cdbcf_2017-10-09; perf_ssid=mwekmrt7p1z3go5aqm3ykdshg9oqso8p_2017-10-09; SL_GWPT_Show_Hide_tmp=1; SL_wptGlobTipTmp=1; track_id=1509078980|e98388ee1d98655de074ae69384de6b3912e5a4fbd3af5fb6d|1d2cff0ee26a3dbcff4420585b6471c0; USERID=208391949; SID=mgp1HSUdxSpRt3bb8Abeg5VxMEOJ4V8AmqiA");
//            headers.put("cookie",cookie);
//            result = ElemeHttpBase.get(webUrl, "utf-8", headers).getResult();
            result = ElemeHttpBase.appGet(webUrl, "utf-8", headers).getResult();

            if (result.contains("UNAUTHORIZED_RESTAURANT_ERROR")) {
//                logger.info(task.getShop_id()+"标记为只能登录抓取");
//                tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", 100),
//                        Cnd.where("id", "=", task.getId()));
//                return ;
                //采取h5加cookie的方法
                logger.info(task.getShop_id() + "需要登录抓取,采用cookie方法");
                webUrl = "https://mainsite-restapi.ele.me/shopping/restaurant/" + shop_id
                        + "?extras%5B%5D=activities&extras%5B%5D=albums&extras%5B%5D=license&extras%5B%5D=identification&terminal=h5";
                Map<String, String> headers_h5 = new HashMap<String, String>();
                headers_h5.put("Referer", "https://h5.ele.me/shop/");
                headers_h5.put("Origin", "https://h5.ele.me");
                headers_h5.put("Host", "mainsite-restapi.ele.me");
                headers_h5.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
//            headers.put("cookie","ubt_ssid=x8okiiwb97ouasa5fqdkgoobcgvkxevb_2017-10-09; _utrace=6e3186bb4eee67eaa35402de155cdbcf_2017-10-09; perf_ssid=mwekmrt7p1z3go5aqm3ykdshg9oqso8p_2017-10-09; SL_GWPT_Show_Hide_tmp=1; SL_wptGlobTipTmp=1; track_id=1509078980|e98388ee1d98655de074ae69384de6b3912e5a4fbd3af5fb6d|1d2cff0ee26a3dbcff4420585b6471c0; USERID=208391949; SID=mgp1HSUdxSpRt3bb8Abeg5VxMEOJ4V8AmqiA");
                headers_h5.put("cookie", cookie);
                result = ElemeHttpBase.get(webUrl, "utf-8", headers_h5).getResult();
                if (result.contains("UNAUTHORIZED_RESTAURANT_ERROR") && elemeCookie !=null){
                    logger.info("eleme,cookie失效:USERID="+elemeCookie.getUSERID()+" Update_time="+elemeCookie.getUpdate_time());
                    tDao.update(ElemeCookie.class,Chain.make("status",3),Cnd.where("id","=",elemeCookie.getId()));
                }

            }
        } catch (Exception e) {
            logger.info("", e);
        }
        ElemeShopInfoModel elemeShopInfoModel = new ElemeShopInfoModel();
        if (result.startsWith("{")) {
            if (result.contains("餐厅不存在")) {
                tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", 404),
                        Cnd.where("id", "=", task.getId()));
                return;
            }
            try {
                JSONObject shopInfo = new JSONObject(result);
                // 店铺活动
                String activities = "";
                if (shopInfo.has("activities")) {
                    JSONArray array_activities = shopInfo.getJSONArray("activities");
                    try {
                        for (int j = 0; j < array_activities.length(); j++) {
                            activities += array_activities.getJSONObject(j).getString("description") + ";";
                        }
                    } catch (Exception e) {

                    }
                }
                elemeShopInfoModel.setActivities(activities);
                //店铺图片
                String img_url="";
                try{
                    //ip地址+29位编码+图片格式
                    //md5+图片格式 or uuid+图片格式
                    //image_path:"ef50a17a7cd8d400ff7e6a5006b344e0jpeg"
                    //image_path:"e1727fa41fc2b03b1b8c2d794a5cf139jpeg"
                    //image_path:"01a4bb33a9d280eb9fb2f4cd672c2b8fpng"
                    //image_path:"0a4e8b6d14a9acefe2771cbb6e63cdaajpeg"
                    img_url = shopInfo.getString("image_path");
                    String temp= img_url.substring(0,32);
                    String temp_01 = temp.substring(0,1);
                    String temp_02 = temp.substring(1,3);
                    String temp_03 = img_url.substring(32,img_url.length());
                    String temp_04 = img_url.substring(3,img_url.length());
                    img_url="https://fuss10.elemecdn.com/"+temp_01+"/"+temp_02+"/"+temp_04+"."+temp_03;
                }catch (Exception e){
                    img_url="";
                }
                elemeShopInfoModel.setImg_url(img_url);
                // 配送方式
                String delivery_mode = null;
                if (shopInfo.has("delivery_mode")) {
                    try {
                        delivery_mode = shopInfo.getJSONObject("delivery_mode").getString("text");
                    } catch (Exception e) {

                    }
                }
                elemeShopInfoModel.setDelivery_mode(delivery_mode);
                // 店铺id
                String id = shopInfo.getString("id");
                if (StringUtils.isEmpty(id)) {
                    id = shop_id;
                }
                elemeShopInfoModel.setShop_id(id);
                // 经纬度
                String latitude = "";
                if (StringUtils.isEmpty(task.getLatitude())) {
                    latitude = shopInfo.getString("latitude");
                } else {
                    latitude = task.getLatitude();
                }
                elemeShopInfoModel.setLatitude(latitude);
                String longitude = "";
                if (StringUtils.isEmpty(task.getLongitude())) {
                    longitude = shopInfo.getString("longitude");
                } else {
                    longitude = task.getLongitude();
                }
                elemeShopInfoModel.setLongitude(longitude);
                // 店铺名称
                String name = shopInfo.getString("name");
                if ("SERVICE_REJECTED".equals(name)) {
                    tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", 0),
                            Cnd.where("id", "=", task.getId()));
                    logger.info("店铺[" + task.getShop_name() + "]URL[" + task.getUrl() + "]服务拒绝，重置状态");
                    return;
                }
                if (StringUtils.isEmpty(name)) {
                    name = task.getShop_name();
                }
                elemeShopInfoModel.setShop_name(name);
                // 店铺评分
                String rating = shopInfo.getString("rating");
                elemeShopInfoModel.setShop_score(rating);
                // 月销量
                String recent_order_num = shopInfo.getString("recent_order_num");
                elemeShopInfoModel.setMonthly_sales(recent_order_num);
                // 店铺类型
                JSONArray flavor = shopInfo.getJSONArray("flavor");
                try {
                    for (int i = 0; i < flavor.length(); i++) {
                        JSONObject jflavor = flavor.getJSONObject(i);
                        String flavor_name = jflavor.getString("name");
                        elemeShopInfoModel.setShop_type(flavor_name);
                    }
                } catch (Exception e) {

                }
                // 起送价
                String minimum_order_amount = shopInfo.getString("float_minimum_order_amount");
                elemeShopInfoModel.setMin_price(minimum_order_amount);
                // 配送费
                String delivery_fee = null;
                try {
                    delivery_fee = shopInfo.getJSONObject("piecewise_agent_fee").getString("tips");
                } catch (Exception e1) {
                    delivery_fee = shopInfo.getString("float_delivery_fee");
                }
                elemeShopInfoModel.setDistribution_fee(delivery_fee);
                // 平均速度
                String order_lead_time = shopInfo.getString("order_lead_time");
                elemeShopInfoModel.setAverage_speed(order_lead_time);
                // 商家地址
                String address = shopInfo.getString("address");
                elemeShopInfoModel.setShop_address(address);
                // 营业时间
                String opening_hours = "";
                JSONArray opening = new JSONArray();
                opening = shopInfo.getJSONArray("opening_hours");
                try {
                    for (int i = 0; i < opening.length(); i++) {
                        opening_hours = opening_hours + opening.getString(i) + ";";
                    }
                } catch (Exception e) {

                }
                elemeShopInfoModel.setBusiness_hour(opening_hours);
                // 配送详情
                String delivery_detail = null;
                try {
                    delivery_detail = shopInfo.getJSONObject("piecewise_agent_fee").getString("description");
                    elemeShopInfoModel.setDelivery_detail(delivery_detail);
                } catch (Exception e1) {

                }
                // 商家优惠
                String promotion_info = null;
                promotion_info = shopInfo.getString("promotion_info");
                elemeShopInfoModel.setPromotion_info(promotion_info);
                // 商家公告
                String descriptions = "";
                JSONArray description = new JSONArray();
                description = shopInfo.getJSONArray("supports");
                try {
                    for (int j = 0; j < description.length(); j++) {
                        JSONObject jdescription = description.getJSONObject(j);
                        descriptions = descriptions + jdescription.getString("description") + ";";
                    }
                } catch (Exception e) {

                }
                elemeShopInfoModel.setSupports(descriptions);
                // 距离
                // String distance = null;
                // try {
                // String latitude = shopInfo.getString("latitude");
                // String longitude = shopInfo.getString("longitude");
                // String distance_result = null;
                // distance_result =
                // HttpBase.get("https://mainsite-restapi.ele.me/shopping/restaurant/"
                // + shop_id
                // +
                // "?extras%5B%5D=activities&extras%5B%5D=album&extras%5B%5D=license&extras%5B%5D=identification&extras%5B%5D=statistics&latitude="
                // + latitude + "&longitude=" + longitude, "utf-8").getResult();
                // distance = new
                // JSONObject(distance_result).getString("distance");
                // elemeShopInfoModel.setDistance(distance);
                // } catch (Exception e) {
                //
                // }
                // 手机号
                String phone = null;
                phone = shopInfo.getString("phone");
                elemeShopInfoModel.setPhone(phone);
                JSONObject jidentification = new JSONObject();
                jidentification = shopInfo.getJSONObject("identification");
                if (jidentification != null) {
                    // 检查结果
                    String identificate_result = null;
                    if ("1".equals(jidentification.getString("identificate_result"))) {
                        identificate_result = "良好";
                    } else if ("2".equals(jidentification.getString("identificate_result"))) {
                        identificate_result = "一般";
                    }
                    elemeShopInfoModel.setCheck_result(identificate_result);
                    // 检查日期
                    String identificate_date = null;
                    identificate_date = jidentification.getString("identificate_date");
                    elemeShopInfoModel.setCheck_date(identificate_date);
                }
            } catch (JSONException e) {
                logger.info("", e);
            }
        } else {
            logger.info("重置饿了么店铺状态");
            tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", 0),
                    Cnd.where("id", "=", task.getId()));
            return;
        }
        // 请求评价服务信息
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
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
            
            result2 = ElemeHttpBase.get(webUrl2, "utf-8", headers).getResult();
        } catch (Exception e) {
            logger.info("", e);
        }
        if (result2.startsWith("{")) {
            try {
                JSONObject shopInfo2 = new JSONObject(result2);
                BigDecimal bigDecimal = null;
                // 综合评价
                String star_level = shopInfo2.getString("overall_score");
                if (!"".equals(star_level)) {
                    try {
                        bigDecimal = new BigDecimal(Double.parseDouble(star_level));
                        elemeShopInfoModel.setTotal_score(bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                    } catch (NumberFormatException e) {
                        elemeShopInfoModel.setTotal_score("-1");
                    }
                }
                // 与周边比较
                String compare_rating = shopInfo2.getString("compare_rating");
                if (!"".equals(compare_rating)) {
                    try {
                        bigDecimal = new BigDecimal(Double.parseDouble(compare_rating) * 100);
                        elemeShopInfoModel
                                .setCompare("高于周边商家:" + bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP) + "%");
                    } catch (NumberFormatException e) {
                        elemeShopInfoModel.setCompare("-1");
                    }
                }
                // 服务评分
                String service_score = shopInfo2.getString("service_score");
                try {
                    bigDecimal = new BigDecimal(Double.parseDouble(service_score));
                    elemeShopInfoModel.setService_score(bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                } catch (NumberFormatException e) {
                    elemeShopInfoModel.setService_score("-1");
                }

                // 菜品评分
                String food_score = shopInfo2.getString("food_score");
                try {
                    bigDecimal = new BigDecimal(Double.parseDouble(food_score));
                    elemeShopInfoModel.setProduct_score(bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
                } catch (NumberFormatException e) {
                    elemeShopInfoModel.setProduct_score("-1");
                }
            } catch (JSONException e) {
                logger.info("", e);
            }
        }
        // 请求评价服务信息
        String webUrl3 = null;
        if (shop_id != null) {
            // webUrl2 = "https://www.ele.me/restapi/ugc/v1/restaurants/" +
            // shop_id
            // + "/rating_scores?latitude=31.149635&longitude=121.802566";
            // webUrl2 = "https://mainsite-restapi.ele.me/ugc/v1/restaurants/" +
            // shop_id
            // + "/rating_scores?latitude=31.27059&longitude=121.47852";
            webUrl3 = "https://mainsite-restapi.ele.me/ugc/v2/restaurants/" + shop_id + "/ratings/tags";
        }
        String result3 = "";
        try {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Referer", "https://h5.ele.me/shop/");
            headers.put("Origin", "https://h5.ele.me");
            headers.put("Host", "mainsite-restapi.ele.me");
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
            
            result3 = ElemeHttpBase.get(webUrl3, "utf-8", headers).getResult();
        } catch (Exception e) {
            logger.info("", e);
        }
        if (result3.startsWith("[")) {
            try {
                JSONArray tags = new JSONArray(result3);
                // 标签
                String shop_tags = "";
                for (int i = 0; i < tags.length(); i++) {
                    shop_tags += tags.getJSONObject(i).getString("name") + "("
                            + tags.getJSONObject(i).getString("count") + ");";
                }
                elemeShopInfoModel.setShop_tags(shop_tags);
            } catch (JSONException e) {
                logger.error("", e);
            }
        }
        try {
            elemeShopInfoModel.setUrl(url);
            elemeShopInfoModel.setCity_name(task.getCity_name());
            elemeShopInfoModel.setKeyword(task.getKeyword());
            elemeShopInfoModel.setRequest_id(task.getRequest_id());
            elemeShopInfoModel.setTask_id(task.getTask_id());
            elemeShopInfoModel.setCreate_time(new Date());
            elemeShopInfoModel.setUnique_id(MD5Util.getMD5(task.getRequest_id() + shop_id));
            long product_time = System.currentTimeMillis();
            TableName.set(task.getRemark());
            if (StringUtils.isEmpty(elemeShopInfoModel.getMonthly_sales())) {
                tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", 0),
                        Cnd.where("id", "=", task.getId()));
                logger.info("店铺[" + task.getShop_name() + "]URL[" + task.getUrl() + "]未抓到月销量，重置状态");
                return;
            } else {
                biDao.insert(elemeShopInfoModel);
            }
            // logger.info("插入一条数据耗时:" + (System.currentTimeMillis() -
            // product_time));
        } catch (Exception e1) {
        } finally {
            TableName.clear();
        }
        logger.info("==========================店铺解析完成，耗时：" + (System.currentTimeMillis() - time)
                + "============================");

        tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", 2), Cnd.where("id", "=", task.getId()));
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
