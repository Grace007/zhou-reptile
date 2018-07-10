package com.reptile.mobile_baidu_waimai.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.City_code;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiCategory;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiCategoryFixed;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiKeywordTaskJob;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiUrlTaskJob;
import com.reptile.mobile_baidu_waimai.model.BaiduWaimaiShopInfoSummaryModel;
import com.reptile.server.MyConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.TableName;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

public class MobileBaiduWaimaiKeywordThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileBaiduWaimaiKeywordThread.class);
    private boolean combine = MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMBINE;
    private BaiduWaimaiKeywordTaskJob task;

    public MobileBaiduWaimaiKeywordThread(BaiduWaimaiKeywordTaskJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        logger.info("开始抓取城市[" + task.getCity_name() + "][" + task.getKeyword() + "]的[" + task.getShop_name() + "]第["
                + task.getCurrent_page() + "]页信息...");
        TaskToBi tobi = BIDataSource.getBiDataSource("baidu_waimai", task.getProject_code(), task.getDown_type());
        Dao biDao = tobi.getBiDao();
        String city_name = task.getCity_name();
        String city_id="";
        try {
            city_id = tDao.fetch(City_code.class, Cnd.where("website", "=", "baidu").and("city_name", "=", city_name))
                    .getCity_code();
        } catch (Exception e2) {
            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", -31), Cnd.where("id", "=", task.getId()));
            return;
        }
        String keyword = task.getKeyword();
        try {
            keyword = java.net.URLEncoder.encode(keyword, "utf-8");
            // shop_name = java.net.URLEncoder.encode(shop_name, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error("", e1);
            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            return;
        }
        //http://client.waimai.baidu.com/mobileui/lbs/v1/suglist?city_id=289&qt=sug&wd=%E8%99%B9%E5%8F%A3%E8%B6%B3%E7%90%83%E5%9C%BA&request_time=1510197967000&cid=289
        String poisearch = "http://waimai.baidu.com/waimai?qt=poisug&wd=" + keyword + "&cb=suggestion_"
                + System.currentTimeMillis() + "000&cid=" + city_id + "&b=&type=0&newmap=1&ie=utf-8";//怎么突然就封我了呢
        String app_search = "http://client.waimai.baidu.com/mobileui/lbs/v1/suglist?city_id=289&qt=sug&wd=" + keyword + "&request_time=" + System.currentTimeMillis() + "000&cid=" + city_id;
        String result = null;
        String lat = "";
        String lon = "";
//        try {
//            result = HttpBase.get(poisearch, "utf-8").getResult();
//            JSONArray poi = new JSONObject(result).getJSONArray("s");
//            if (poi.length() > 0) {
//                lat = poi.getString(0).split(city_id)[1].replace("$", "").split(",")[0];
//                lon = poi.getString(0).split(city_id)[1].replace("$", "").split(",")[1];
//            } else {
//                tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 5),
//                        Cnd.where("id", "=", task.getId()));
//                logger.info("百度外卖地址[" + task.getKeyword() + "]与城市[" + task.getCity_name() + "]不匹配");
//                return;
//            }
//        } catch (Exception e2) {
//            logger.error("经纬度获取异常", e2);
//            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
//            return;
//        }
        try {
            result = HttpBase.get(app_search, "utf-8").getResult();
//            JSONObject resultJson = new JSONObject(result).getJSONObject("result");
//            if (resultJson.getJSONArray("sug_list").length() != 0) {
//                JSONObject sug1 = new JSONObject(result).getJSONObject("result").getJSONArray("sug_list").getJSONObject(0);
//                lat = sug1.getJSONObject("location").getString("lat");
//                lon = sug1.getJSONObject("location").getString("lng");
//            }else if (resultJson.getJSONArray("sug_list_remote").length() != 0){
//                JSONObject sug1 = new JSONObject(result).getJSONObject("result").getJSONArray("sug_list_remote").getJSONObject(0);
//                lat = sug1.getJSONObject("location").getString("lat");
//                lon = sug1.getJSONObject("location").getString("lng");
//            }
            JSONObject sug1 = new JSONObject(result).getJSONObject("result").getJSONArray("sug_list").getJSONObject(0);
            lat = sug1.getJSONObject("location").getString("lat");
            lon = sug1.getJSONObject("location").getString("lng");
        } catch (Exception e) {
            logger.error("app经纬度获取异常", e);
            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 32), Cnd.where("id", "=", task.getId()));
            return;
        }
        String shop_name = task.getShop_name();
        long current_page = task.getCurrent_page();
        int current_category = task.getCurrent_category();
        if (current_page == 0) {
            current_page = 1;
        }
        if (StringUtils.isNotEmpty(city_name) && StringUtils.isNotEmpty(keyword) && StringUtils.isNotEmpty(shop_name)) {
            // 店铺名不为空的情况
            try {
                shop_name = java.net.URLEncoder.encode(shop_name, "utf-8");
            } catch (UnsupportedEncodingException e1) {
                logger.error("", e1);
                tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 3),
                        Cnd.where("id", "=", task.getId()));
                return;
            }
            int counter = 0;
            while (true) {
                long time = System.currentTimeMillis();
                String list_url = "http://waimai.baidu.com/mobile/waimai?qt=shoplist&address=" + keyword + "&lat=" + lat
                        + "&lng=" + lon + "&page=" + current_page + "&count=20&display=json&wd=" + shop_name;
//                String list_url = "https://waimai.baidu.com/shopui/mobile/searchlist";
                String list_result = null;
                try {
                    Map<String, String> header = new HashMap<String,
                            String>();
                    header.put("Host", "waimai.baidu.com");
                    header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_2 like Mac OS X) AppleWebKit/603.2.4 (KHTML, like Gecko) Mobile/14F89 MicroMessenger/6.5.8 NetType/WIFI Language/zh_CN");
                    Map<String, String> data = new HashMap<String,
                            String>();
                    data.put("from", "miniapp");
                    data.put("client_from", "miniapp");
                    data.put("city_id", city_id);
                    data.put("address", keyword);
                    data.put("lng", lon);
                    data.put("lat", lat);
                    data.put("miniapp_model", "iPhone%206s%20Plus%3CiPhone8%2C2%3E");
                    data.put("miniapp_pixel_ratio", "3");
                    data.put("miniapp_version", "6.5.8");
                    data.put("miniapp_system", "iOS%2010.3.2");
                    data.put("miniapp_platform", "ios");
                    data.put("net_type", "wifi");
                    data.put("wd", shop_name);
                    data.put("count", "20");
                    data.put("page", current_page + "");
//                    list_result = BaiduHttpBase.post(list_url, header, data);
                     list_result = HttpBase.get(list_url, "utf-8",
                     header).getResult();
                    JSONArray shop_list = new JSONObject(list_result).getJSONObject("result").getJSONArray("shop_info");
                    counter += shop_list.length();
                    if (current_page > MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.PAGE_MAX) {// 最大页数限制，防止线程死循环
                        tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 2),
                                Cnd.where("id", "=", task.getId()));
                        break;
                    }
                    if (shop_list.length() == 0) {
                        logger.info("==========" + "此页无店铺信息，URL为：" + list_url + "==========");
                        tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("current_page", current_page),
                                Cnd.where("id", "=", task.getId()));
                        break;
                    } else {
                        List<BaiduWaimaiShopInfoSummaryModel> modelList = new ArrayList<BaiduWaimaiShopInfoSummaryModel>();
                        for (int j = 0; j < shop_list.length(); j++) {
                            String shop_id = "";
                            String name = "";
                            String url = "";
                            String average_speed = "";
                            String min_price = "";
                            String distribution_fee = "";
                            String total_score = "0";
                            String sale = "";
                            String distance = "0";
                            String welfare_info = "";
                            String announcement = "";
                            String delivery_mode = "";
                            String shop_lng = "";
                            String shop_lat = "";
                            if (shop_list.getJSONObject(j).has("shop_id")) {
                                shop_id = shop_list.getJSONObject(j).getString("shop_id");
                            }
                            if (shop_list.getJSONObject(j).has("shop_name")) {
                                name = shop_list.getJSONObject(j).getString("shop_name");
                            }
                            url = "http://waimai.baidu.com/mobile/waimai?qt=shopmenu&shop_id=" + shop_id + "&address="
                                    + keyword + "&lat=" + lat + "&lng=" + lon;
                            if (shop_list.getJSONObject(j).has("delivery_time")) {
                                average_speed = shop_list.getJSONObject(j).getString("delivery_time");
                            }
                            if (shop_list.getJSONObject(j).has("takeout_price")) {
                                min_price = shop_list.getJSONObject(j).getString("takeout_price");
                            }
                            if (shop_list.getJSONObject(j).has("takeout_cost")) {
                                distribution_fee = shop_list.getJSONObject(j).getString("takeout_cost");
                            }
                            if (shop_list.getJSONObject(j).has("average_score")) {
                                total_score = shop_list.getJSONObject(j).getString("average_score");
                            }
                            if (shop_list.getJSONObject(j).has("saled_month")) {
                                sale = shop_list.getJSONObject(j).getString("saled_month");
                            }
                            if (shop_list.getJSONObject(j).has("distance")) {
                                distance = shop_list.getJSONObject(j).getString("distance");
                                try {
                                    BigDecimal bigDecimal = null;
                                    bigDecimal = new BigDecimal(Double.parseDouble(distance));
                                    distance = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                                } catch (NumberFormatException e) {
                                    distance = "-1";
                                }
                            }
                            if (shop_list.getJSONObject(j).has("welfare_act_info")) {
                                try {
                                    JSONArray welfare = shop_list.getJSONObject(j).getJSONArray("welfare_act_info");
                                    if (welfare.length() > 0) {
                                        for (int i = 0; i < welfare.length(); i++) {
                                            welfare_info += welfare.getJSONObject(i).getString("msg") + ";";
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }
                            if (shop_list.getJSONObject(j).has("shop_announcement")) {
                                announcement = shop_list.getJSONObject(j).getString("shop_announcement");
                            }
                            if (shop_list.getJSONObject(j).has("front_logistics_text")) {
                                delivery_mode = shop_list.getJSONObject(j).getString("front_logistics_text");
                            }
                            if (shop_list.getJSONObject(j).has("shop_lng")) {
                                try {
                                    shop_lng = String
                                            .valueOf(Float.parseFloat(shop_list.getJSONObject(j).getString("shop_lng"))
                                                    / 100000);
                                } catch (NumberFormatException e) {

                                } catch (JSONException e) {

                                }
                            }
                            if (shop_list.getJSONObject(j).has("shop_lat")) {
                                try {
                                    shop_lat = String
                                            .valueOf(Float.parseFloat(shop_list.getJSONObject(j).getString("shop_lat"))
                                                    / 100000);
                                } catch (NumberFormatException e) {

                                } catch (JSONException e) {

                                }
                            }

                            BaiduWaimaiShopInfoSummaryModel model = new BaiduWaimaiShopInfoSummaryModel();
                            model.setRequest_id(task.getRequest_id());
                            model.setTask_id(task.getTask_id());
                            model.setKeyword(task.getKeyword());
                            model.setCity_name(task.getCity_name());
                            model.setCity_id(task.getCity_id());
                            model.setShop_id(shop_id);
                            model.setShop_name(name);
                            model.setUrl(url);
                            model.setAverage_speed(average_speed);
                            model.setMin_price(min_price);
                            model.setDistribution_fee(distribution_fee);
                            model.setTotal_score(total_score);
                            model.setSale(sale);
                            model.setDistance(distance);
                            model.setWelfare_info(welfare_info);
                            model.setAnnouncement(announcement);
                            model.setDelivery_mode(delivery_mode);
                            model.setCreate_date(new Date());
                            model.setCreate_time(new Date());
                            model.setOffice_id(task.getOffice_id());
                            model.setOffice_name(task.getOffice_name());
                            model.setUnique_id(MD5Util.getMD5(task.getRequest_id() + shop_id));
                            modelList.add(model);
//                            try {
//                                TableName.set(task.getRemark());
//                                biDao.fastInsert(model);
//                            } catch (Exception e) {
//                                // logger.error(e);
//                            } finally {
//                                TableName.clear();
//                            }

                            BaiduWaimaiUrlTaskJob job = new BaiduWaimaiUrlTaskJob();
                            job.setRequest_id(task.getRequest_id());
                            job.setTask_id(task.getTask_id());
                            job.setCreate_time(new Date());
                            job.setCity_name(task.getCity_name());
                            job.setKeyword(task.getKeyword());
                            job.setUrl(url);
                            job.setShop_id(shop_id);
                            job.setNode(task.getNode());
                            job.setStatus(0);
                            job.setProject_code(task.getProject_code());
                            job.setDown_type(task.getDown_type());
                            job.setRemark(task.getRemark());
                            // job.setLatitude(shop_lat);
                            // job.setLongitude(shop_lng);
                            if (combine) {
                                try {
                                    tDao.insert(job);
                                } catch (Exception e) {

                                }
                            }
                        }
                        try {
                            TableName.set(task.getRemark());
                            biDao.fastInsert(modelList);
                        } catch (Exception e) {
                            // logger.error(e);
                        } finally {
                            TableName.clear();
                        }
                        logger.info(
                                "下载手机端百度外卖simple第" + current_page + "页完成，耗时:" + (System.currentTimeMillis() - time));
                        //if (current_page % 5 == 0) {
                        tDao.update(BaiduWaimaiKeywordTaskJob.class,
                                Chain.make("total", (counter + task.getTotal())).add("current_page", current_page),
                                Cnd.where("id", "=", task.getId()));
                        //}
                        current_page++;
                    }
                } catch (Exception e) {
                    //logger.error("百度外卖simple当前页[" + list_url + "]请求异常", e);
                }
            }
            logger.info("手机端百度外卖simple店铺抓取完成");
            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            // try {
            // shop_name = java.net.URLEncoder.encode(shop_name, "utf-8");
            // } catch (UnsupportedEncodingException e1) {
            // logger.error("", e1);
            // tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status",
            // 3),
            // Cnd.where("id", "=", task.getId()));
            // return;
            // }
            // String position_url =
            // "http://waimai.baidu.com/waimai?qt=poisearch&from=pc&ie=utf-8&c="
            // + city_id + "&wd="
            // + keyword;
            // String position_html = null;
            // try {
            // position_html = HttpBase.get(position_url, "utf-8").getResult();
            // JSONObject position_json = new JSONObject(position_html);
            // String actual_city_id = "";
            // try {
            // actual_city_id =
            // position_json.getJSONObject("result").getString("city_id");
            // if (!city_id.equals(actual_city_id)) {
            // tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status",
            // 5),
            // Cnd.where("id", "=", task.getId()));
            // logger.info("百度外卖地址[" + task.getKeyword() + "]与城市[" +
            // task.getCity_name() + "]不匹配");
            // return;
            // }
            // } catch (Exception e1) {
            // logger.error("", e1);
            // tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status",
            // -5),
            // Cnd.where("id", "=", task.getId()));
            // }
            // JSONArray position_array =
            // position_json.getJSONObject("result").getJSONArray("content");
            // String latitude = "";
            // String longitude = "";
            // for (int i = 0; i < position_array.length(); i++) {
            // latitude = position_array.getJSONObject(0).getString("latitude");
            // longitude =
            // position_array.getJSONObject(0).getString("longitude");
            // break;
            // }
            // String web_url =
            // "http://waimai.baidu.com/waimai?qt=shoplist&lat=" + latitude +
            // "&lng=" + longitude
            // + "&address=" + keyword + "&city_id=" + city_id;
            // String web_html = null;
            // try {
            // web_html = HttpBase.get(web_url, "utf-8").getResult();
            // Document doc = Jsoup.parse(web_html);
            // if
            // (StringUtils.isNotEmpty(doc.select(".filter-search").select("a").attr("href")))
            // {
            // String hash_key;
            // try {
            // hash_key =
            // doc.select(".filter-search").select("a").attr("href").split("/")[3];
            // if (StringUtils.isNotEmpty(hash_key)) {
            // String shop_list_json_first_url =
            // "http://waimai.baidu.com/waimai/shoplist/" + hash_key
            // + "?wd=" + shop_name +
            // "&display=json&promotion=certification&page=1&count=40";
            // try {
            // JSONObject shop_list_json = new JSONObject(
            // HttpBase.get(shop_list_json_first_url, "utf-8").getResult());
            // // 生成shop_list页
            // int total = 0;
            // try {
            // total = Integer
            // .parseInt(shop_list_json.getJSONObject("result").getString("total"));
            // } catch (Exception e) {
            // total = 0;
            // }
            // int total_page = this.calculatePage(total, 40, 999);
            // for (int i = 0; i < total_page; i++) {
            // BaiduWaimaiShopListTaskJob job = new
            // BaiduWaimaiShopListTaskJob();
            // job.setRequest_id(task.getRequest_id());
            // job.setTask_id(task.getTask_id());
            // job.setKeyword(task.getKeyword());
            // job.setCity_name(task.getCity_name());
            // job.setCity_id(city_id);
            // job.setShop_name(task.getShop_name());
            // job.setUrl("http://waimai.baidu.com/waimai/shoplist/" + hash_key
            // + "?wd="
            // + shop_name + "&display=json&promotion=certification&page=" + (i
            // + 1)
            // + "&count=40");
            // job.setShop_number(total);
            // job.setPage_number((i + 1));
            // job.setTotal_page(total_page);
            // job.setNode(task.getNode());
            // job.setStatus(0);
            // job.setProject_code(task.getProject_code());
            // job.setDown_type(task.getDown_type());
            // job.setCreate_date(new Date());
            // job.setCreate_time(new Date());
            // job.setRemark(task.getRemark());
            // try {
            // tDao.insert(job);
            // } catch (Exception e) {
            //
            // }
            // logger.info("生成城市[" + task.getCity_name() + "][" +
            // task.getKeyword() + "]的["
            // + task.getShop_name() + "]的第" + (i + 1) + "页信息完成");
            // }
            // logger.info("抓取城市[" + task.getCity_name() + "][" +
            // task.getKeyword() + "]的["
            // + task.getShop_name() + "]信息完成");
            // tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status",
            // 2),
            // Cnd.where("id", "=", task.getId()));
            // } catch (Exception e) {
            // logger.error("", e);
            // tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status",
            // 3),
            // Cnd.where("id", "=", task.getId()));
            // }
            // }
            // } catch (Exception e) {
            // logger.error("", e);
            // tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status",
            // 3),
            // Cnd.where("id", "=", task.getId()));
            // }
            // } else {
            // tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status",
            // -1),
            // Cnd.where("id", "=", task.getId()));
            // }
            // } catch (Exception e) {
            // logger.error("", e);
            // tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status",
            // 3),
            // Cnd.where("id", "=", task.getId()));
            // }
            // } catch (Exception e) {
            // logger.error("", e);
            // tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status",
            // 3),
            // Cnd.where("id", "=", task.getId()));
            // }
        } else if (StringUtils.isNotEmpty(city_name) && StringUtils.isNotEmpty(keyword)
                && StringUtils.isEmpty(shop_name)) {
            // 店铺名为空的情况
            // 获取category
            List<BaiduWaimaiCategory> list = new ArrayList<BaiduWaimaiCategory>();
            List<BaiduWaimaiCategory> list1 = new ArrayList<BaiduWaimaiCategory>();
            List<BaiduWaimaiCategory> list2 = new ArrayList<BaiduWaimaiCategory>();
            //http://waimai.baidu.com/mobile/waimai?qt=shoplist&address=%E8%99%B9%E5%8F%A3%E8%B6%B3%E7%90%83%E5%9C%BA&lat=3646338.29&lng=13524139.91&categorytype=point&page=1&count=20&display=json
            String cate_url = "http://waimai.baidu.com/mobile/waimai?qt=shoplist&address=" + keyword + "&lat=" + lat
                    + "&lng=" + lon + "&categorytype=point&page=1&count=20&display=json";
            String cate_result = null;
            try {
                cate_result = HttpBase.get(cate_url, "utf-8").getResult();
                JSONObject jsonObject = new JSONObject(cate_result).getJSONObject("result");
                    if (jsonObject.has("classify")) {
                    JSONArray cates = jsonObject.getJSONArray("classify");
                    if (cates.length() > 1) {
                        for (int i = 1; i < cates.length(); i++) {
                            if (cates.getJSONObject(i).has("child")) {// 子分类
                                for (int j = 1; j < cates.getJSONObject(i).getJSONArray("child").length(); j++) {
                                    BaiduWaimaiCategory b = new BaiduWaimaiCategory();
                                    b.setCategory(cates.getJSONObject(i).getString("name"));
                                    b.setSub_category(cates.getJSONObject(i).getJSONArray("child").getJSONObject(j)
                                            .getString("name"));
                                    b.setCategory_id(cates.getJSONObject(i).getJSONArray("child").getJSONObject(j)
                                            .getString("id"));
                                    list1.add(b);
                                }
                            } else {
                                BaiduWaimaiCategory b = new BaiduWaimaiCategory();
                                b.setCategory(cates.getJSONObject(i).getString("name")); //快食简餐
                                b.setCategory_id(cates.getJSONObject(i).getString("id")); //17
                                list1.add(b);
                            }
                        }
                    }
                }
            } catch (Exception e1) {
                logger.error("手机端百度外卖category获取异常", e1);
//                tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", -32),
//                        Cnd.where("id", "=", task.getId()));
//                return;
            }
            if (list1.size() == 0) {// 选用固定数量的分类
                try {
                    List<BaiduWaimaiCategoryFixed> fixed = tDao.query(BaiduWaimaiCategoryFixed.class, null);
                    for (int i = 0; i < fixed.size(); i++) {
                        BaiduWaimaiCategory b = new BaiduWaimaiCategory();
                        b.setCategory(fixed.get(i).getCategory());
                        b.setCategory_id(fixed.get(i).getCategory_id());
                        b.setSub_category(fixed.get(i).getSub_category());
                        list1.add(b);
                    }
                } catch (Exception e) {
                    logger.error("百度外卖分类查询异常", e);
                }
            }


            //http://client.waimai.baidu.com/shopui/na/v1/shoplist?return_type=refresh&address=%E8%99%B9%E5%8F%A3%E8%B6%B3%E7%90%83%E5%9C%BA&cuid=4A36A286-93DB-4281-A3B5-35C1B1817EBF&sv=4.4.1&loc_lng=3646338.29&from=na-iphone&lng=3646338.29&lat=3646338.29&loc_lat=3646338.29&city_id=289
            String baidu_waimai_app_url = "http://client.waimai.baidu.com/shopui/na/v1/shoplist?return_type=refresh&address="
                    + keyword + "&cuid=4A36A286-93DB-4281-A3B5-35C1B1817EBF&sv=4.4.1&loc_lng=" + lon
                    + "&from=na-iphone&lng=" + lon + "&lat=" + lat + "&loc_lat=" + lat + "&city_id=" + city_id;
            String app_result = null;
            try {
                app_result = HttpBase.get(baidu_waimai_app_url, "utf-8").getResult();
                //根据店铺排序 ,综合派寻\销量排序\距离最近等等
                JSONObject jsonObject = new JSONObject(app_result).getJSONObject("result").getJSONObject("shopfilter");
                if (jsonObject.has("classify")) {
                    JSONArray cates = jsonObject.getJSONArray("classify");
                    if (cates.length() > 1) {
                        for (int i = 1; i < cates.length(); i++) {
                            if (cates.getJSONObject(i).has("child")) {// 子分类
                                for (int j = 1; j < cates.getJSONObject(i).getJSONArray("child").length(); j++) {
                                    BaiduWaimaiCategory b = new BaiduWaimaiCategory();
                                    b.setCategory(cates.getJSONObject(i).getString("name"));
                                    b.setSub_category(cates.getJSONObject(i).getJSONArray("child").getJSONObject(j)
                                            .getString("name"));
                                    b.setCategory_id(cates.getJSONObject(i).getJSONArray("child").getJSONObject(j)
                                            .getString("id"));
                                    list2.add(b);
                                }
                            } else {
                                BaiduWaimaiCategory b = new BaiduWaimaiCategory();
                                b.setCategory(cates.getJSONObject(i).getString("name"));
                                b.setCategory_id(cates.getJSONObject(i).getString("id"));
                                list2.add(b);
                            }
                        }
                    }
                }
            } catch (Exception e1) {
                logger.error("手机端百度外卖category获取异常", e1);
//                tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", -34),
//                        Cnd.where("id", "=", task.getId()));
//                return;
            }
            list = judge(list1, list2);
            if (list.size() == 0) {// 避免list为空的情况，至少也要抓一遍
                BaiduWaimaiCategory kong = new BaiduWaimaiCategory();
                kong.setCategory("kong");
                kong.setSub_category("kong");
                kong.setCategory_id("");
                list.add(kong);
            }

            int counter = 0;
            if (StringUtils.isEmpty(task.getGenerate_category())) {
                int rank = 1 + (int) (current_page - 1) * 20;
                while (true) {
                    long time = System.currentTimeMillis();
                    String list_url = "http://waimai.baidu.com/mobile/waimai?qt=shoplist&address=" + keyword + "&lat="
                            + lat + "&lng=" + lon + "&page=" + current_page + "&count=20&display=json";
//                    String list_url = "https://waimai.baidu.com/shopui/mobile/cliententry";
                    String list_result = null;
                    try {
                        Map<String, String> header = new HashMap<String,
                                String>();
                        header.put("Host", "waimai.baidu.com");
//                        Map<String, String> data = new HashMap<String,
//                                String>();
//                        data.put("request_time", System.currentTimeMillis() + "000");
//                        data.put("return_type", "paging");
//                        data.put("count", "20");
//                        data.put("page", current_page + "");
//                        data.put("from", "miniapp");
//                        data.put("client_from", "miniapp");
//                        data.put("city_id", city_id);
//                        data.put("address", keyword);
//                        data.put("lng", lon);
//                        data.put("lat", lat);
//                        data.put("miniapp_model", "iPhone%206s%20Plus%3CiPhone8%2C2%3E");
//                        data.put("miniapp_pixel_ratio", "3");
//                        data.put("miniapp_version", "6.5.8");
//                        data.put("miniapp_system", "iOS%2010.3.2");
//                        data.put("miniapp_platform", "ios");
//                        data.put("net_type", "wifi");
//                        list_result = HttpBase.post(list_url, "utf-8", header, data).getResult();
                         list_result = HttpBase.get(list_url, "utf-8",header).getResult();
                        JSONArray shop_list = new JSONObject(list_result).getJSONObject("result")
                                .getJSONArray("shop_info");

                        counter += shop_list.length();
                        logger.info(shop_list.length());
                        if (current_page > MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.PAGE_MAX) {// 最大页数限制，防止线程死循环
                            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 2),
                                    Cnd.where("id", "=", task.getId()));
                            break;
                        }
                        if (shop_list.length() == 0) {
                            logger.info("==========" + "此页无店铺信息，URL为：" + list_url + "==========");
                            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("current_page", current_page),
                                    Cnd.where("id", "=", task.getId()));
                            break;
                        } else {
                            List<BaiduWaimaiShopInfoSummaryModel> modelList = new ArrayList<BaiduWaimaiShopInfoSummaryModel>();
                            for (int j = 0; j < shop_list.length(); j++) {
                                String shop_id = "";
                                String name = "";
                                String url = "";
                                String average_speed = "";
                                String min_price = "";
                                String distribution_fee = "";
                                String total_score = "0";
                                String sale = "";
                                String distance = "0";
                                String welfare_info = "";
                                String announcement = "";
                                String delivery_mode = "";
                                String shop_lat = "";
                                String shop_lng = "";
                                if (shop_list.getJSONObject(j).has("shop_id")) {
                                    shop_id = shop_list.getJSONObject(j).getString("shop_id");
                                }
                                if (shop_list.getJSONObject(j).has("shop_name")) {
                                    name = shop_list.getJSONObject(j).getString("shop_name");
                                }
                                url = "http://waimai.baidu.com/mobile/waimai?qt=shopmenu&shop_id=" + shop_id
                                        + "&address=" + keyword + "&lat=" + lat + "&lng=" + lon;
                                if (shop_list.getJSONObject(j).has("delivery_time")) {
                                    average_speed = shop_list.getJSONObject(j).getString("delivery_time");
                                }
                                if (shop_list.getJSONObject(j).has("takeout_price")) {
                                    min_price = shop_list.getJSONObject(j).getString("takeout_price");
                                }
                                if (shop_list.getJSONObject(j).has("takeout_cost")) {
                                    distribution_fee = shop_list.getJSONObject(j).getString("takeout_cost");
                                }
                                if (shop_list.getJSONObject(j).has("average_score")) {
                                    total_score = shop_list.getJSONObject(j).getString("average_score");
                                }
                                if (shop_list.getJSONObject(j).has("saled_month")) {
                                    sale = shop_list.getJSONObject(j).getString("saled_month");
                                }
                                if (shop_list.getJSONObject(j).has("distance")) {
                                    distance = shop_list.getJSONObject(j).getString("distance");
                                    try {
                                        BigDecimal bigDecimal = null;
                                        bigDecimal = new BigDecimal(Double.parseDouble(distance));
                                        distance = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                                    } catch (NumberFormatException e) {
                                        distance = "-1";
                                    }
                                }
                                if (shop_list.getJSONObject(j).has("welfare_act_info")) {
                                    try {
                                        JSONArray welfare = shop_list.getJSONObject(j).getJSONArray("welfare_act_info");
                                        if (welfare.length() > 0) {
                                            for (int i = 0; i < welfare.length(); i++) {
                                                welfare_info += welfare.getJSONObject(i).getString("msg") + ";";
                                            }
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                                if (shop_list.getJSONObject(j).has("shop_announcement")) {
                                    announcement = shop_list.getJSONObject(j).getString("shop_announcement");
                                }
                                if (shop_list.getJSONObject(j).has("front_logistics_text")) {
                                    delivery_mode = shop_list.getJSONObject(j).getString("front_logistics_text");
                                }
                                if (shop_list.getJSONObject(j).has("shop_lng")) {
                                    try {
                                        shop_lng = String.valueOf(
                                                Float.parseFloat(shop_list.getJSONObject(j).getString("shop_lng"))
                                                        / 100000);
                                    } catch (NumberFormatException e) {

                                    } catch (JSONException e) {

                                    }
                                }
                                if (shop_list.getJSONObject(j).has("shop_lat")) {
                                    try {
                                        shop_lat = String.valueOf(
                                                Float.parseFloat(shop_list.getJSONObject(j).getString("shop_lat"))
                                                        / 100000);
                                    } catch (NumberFormatException e) {

                                    } catch (JSONException e) {

                                    }
                                }

                                BaiduWaimaiShopInfoSummaryModel model = new BaiduWaimaiShopInfoSummaryModel();
                                model.setRequest_id(task.getRequest_id());
                                model.setTask_id(task.getTask_id());
                                model.setKeyword(task.getKeyword());
                                model.setCity_name(task.getCity_name());
                                model.setCity_id(task.getCity_id());
                                model.setShop_id(shop_id);
                                model.setShop_name(name);
                                model.setUrl(url);
                                model.setAverage_speed(average_speed);
                                model.setMin_price(min_price);
                                model.setDistribution_fee(distribution_fee);
                                model.setTotal_score(total_score);
                                model.setSale(sale);
                                model.setDistance(distance);
                                model.setWelfare_info(welfare_info);
                                model.setAnnouncement(announcement);
                                model.setDelivery_mode(delivery_mode);
                                model.setCreate_date(new Date());
                                model.setCreate_time(new Date());
                                model.setRank(rank + j);
                                model.setOffice_name(task.getOffice_name());
                                model.setOffice_id(task.getOffice_id());
                                model.setUnique_id(MD5Util.getMD5(task.getRequest_id() + shop_id));
                                modelList.add(model);
//                                try {
//                                    TableName.set(task.getRemark());
//                                    biDao.fastInsert(model);
//                                } catch (Exception e) {
//                                    // logger.error(e);
//                                } finally {
//                                    TableName.clear();
//                                }

                                BaiduWaimaiUrlTaskJob job = new BaiduWaimaiUrlTaskJob();
                                job.setRequest_id(task.getRequest_id());
                                job.setTask_id(task.getTask_id());
                                job.setCreate_time(new Date());
                                job.setCity_name(task.getCity_name());
                                job.setKeyword(task.getKeyword());
                                job.setUrl(url);
                                job.setShop_id(shop_id);
                                job.setNode(task.getNode());
                                job.setStatus(0);
                                job.setProject_code(task.getProject_code());
                                job.setDown_type(task.getDown_type());
                                job.setRemark(task.getRemark());
                                // job.setLongitude(shop_lng);
                                // job.setLatitude(shop_lat);
                                if (combine) {
                                    try {
                                        tDao.insert(job);
                                    } catch (Exception e) {

                                    }
                                }
                            }
                            try {
                                TableName.set(task.getRemark());
                                biDao.fastInsert(modelList);
                            } catch (Exception e) {
                                // logger.error(e);
                            } finally {
                                TableName.clear();
                            }
                            logger.info("下载手机端百度外卖simple第" + current_page + "页完成，耗时:"
                                    + (System.currentTimeMillis() - time));
                            //if (current_page % 5 == 0) {
                            tDao.update(BaiduWaimaiKeywordTaskJob.class,
                                    Chain.make("total", (counter + task.getTotal())).add("current_page", current_page),
                                    Cnd.where("id", "=", task.getId()));
                            //}
                            current_page++;
                            rank += shop_list.length();
                        }
                    } catch (Exception e) {
                        //logger.error("百度外卖simple当前页[" + list_url + "]请求异常", e);
                    }
                }
            } else {
                for (int g = current_category; g < list.size(); g++) {
                    while (true) {
                        long time = System.currentTimeMillis();
                        String list_url = "http://waimai.baidu.com/mobile/waimai?qt=shoplist&address=" + keyword
                                + "&lat=" + lat + "&lng=" + lon + "&page=" + current_page
                                + "&count=20&display=json&categorytype=point&taste=" + list.get(g).getCategory_id();
//                        String list_url = "https://waimai.baidu.com/shopui/mobile/cliententry";
                        String list_result = null;
                        try {
                            Map<String, String> header = new HashMap<String,
                                    String>();
                            header.put("Host", "waimai.baidu.com");
                            Map<String, String> data = new HashMap<String,
                                    String>();
                            data.put("request_time", System.currentTimeMillis() + "000");
                            data.put("return_type", "refresh");
                            data.put("count", "20");
                            data.put("page", current_page + "");
                            data.put("from", "miniapp");
                            data.put("client_from", "miniapp");
                            data.put("city_id", city_id);
                            data.put("address", keyword);
                            data.put("lng", lon);
                            data.put("lat", lat);
                            data.put("miniapp_model", "iPhone%206s%20Plus%3CiPhone8%2C2%3E");
                            data.put("miniapp_pixel_ratio", "3");
                            data.put("miniapp_version", "6.5.8");
                            data.put("miniapp_system", "iOS%2010.3.2");
                            data.put("miniapp_platform", "ios");
                            data.put("net_type", "wifi");
                            data.put("taste", list.get(g).getCategory_id());
//                            list_result = HttpBase.post(list_url, "utf-8", header, data).getResult();
                             list_result = HttpBase.get(list_url, "utf-8",
                             header).getResult();
                            JSONArray shop_list = new JSONObject(list_result).getJSONObject("result")
                                    .getJSONArray("shop_info");
                            counter += shop_list.length();
                            if (current_page > MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.PAGE_MAX) {// 最大页数限制，防止线程死循环
                                tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 2),
                                        Cnd.where("id", "=", task.getId()));
                                break;
                            }
                            if (shop_list.length() == 0) {
                                logger.info("==========" + "此页无店铺信息，URL为：" + list_url + "==========");
                                current_page = 0L;// 重置
                                tDao.update(BaiduWaimaiKeywordTaskJob.class,
                                        Chain.make("current_page", current_page).add("current_category", g),
                                        Cnd.where("id", "=", task.getId()));
                                break;
                            } else {
                                List<BaiduWaimaiShopInfoSummaryModel> modelList = new ArrayList<BaiduWaimaiShopInfoSummaryModel>();
                                for (int j = 0; j < shop_list.length(); j++) {
                                    String shop_id = "";
                                    String name = "";
                                    String url = "";
                                    String average_speed = "";
                                    String min_price = "";
                                    String distribution_fee = "";
                                    String total_score = "0";
                                    String sale = "";
                                    String distance = "0";
                                    String welfare_info = "";
                                    String announcement = "";
                                    String delivery_mode = "";
                                    String shop_lat = "";
                                    String shop_lng = "";
                                    if (shop_list.getJSONObject(j).has("shop_id")) {
                                        shop_id = shop_list.getJSONObject(j).getString("shop_id");
                                    }
                                    if (shop_list.getJSONObject(j).has("shop_name")) {
                                        name = shop_list.getJSONObject(j).getString("shop_name");
                                    }
                                    url = "http://waimai.baidu.com/mobile/waimai?qt=shopmenu&shop_id=" + shop_id
                                            + "&address=" + keyword + "&lat=" + lat + "&lng=" + lon;
                                    if (shop_list.getJSONObject(j).has("delivery_time")) {
                                        average_speed = shop_list.getJSONObject(j).getString("delivery_time");
                                    }
                                    if (shop_list.getJSONObject(j).has("takeout_price")) {
                                        min_price = shop_list.getJSONObject(j).getString("takeout_price");
                                    }
                                    if (shop_list.getJSONObject(j).has("takeout_cost")) {
                                        distribution_fee = shop_list.getJSONObject(j).getString("takeout_cost");
                                    }
                                    if (shop_list.getJSONObject(j).has("average_score")) {
                                        total_score = shop_list.getJSONObject(j).getString("average_score");
                                    }
                                    if (shop_list.getJSONObject(j).has("saled_month")) {
                                        sale = shop_list.getJSONObject(j).getString("saled_month");
                                    }
                                    if (shop_list.getJSONObject(j).has("distance")) {
                                        distance = shop_list.getJSONObject(j).getString("distance");
                                        try {
                                            BigDecimal bigDecimal = null;
                                            bigDecimal = new BigDecimal(Double.parseDouble(distance));
                                            distance = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                                        } catch (NumberFormatException e) {
                                            distance = "-1";
                                        }
                                    }
                                    if (shop_list.getJSONObject(j).has("welfare_act_info")) {
                                        try {
                                            JSONArray welfare = shop_list.getJSONObject(j).getJSONArray("welfare_act_info");
                                            if (welfare.length() > 0) {
                                                for (int i = 0; i < welfare.length(); i++) {
                                                    welfare_info += welfare.getJSONObject(i).getString("msg") + ";";
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                    if (shop_list.getJSONObject(j).has("shop_announcement")) {
                                        announcement = shop_list.getJSONObject(j).getString("shop_announcement");
                                    }
                                    if (shop_list.getJSONObject(j).has("front_logistics_text")) {
                                        delivery_mode = shop_list.getJSONObject(j).getString("front_logistics_text");
                                    }
                                    if (shop_list.getJSONObject(j).has("shop_lng")) {
                                        try {
                                            shop_lng = String.valueOf(
                                                    Float.parseFloat(shop_list.getJSONObject(j).getString("shop_lng"))
                                                            / 100000);
                                        } catch (NumberFormatException e) {

                                        } catch (JSONException e) {

                                        }
                                    }
                                    if (shop_list.getJSONObject(j).has("shop_lat")) {
                                        try {
                                            shop_lat = String.valueOf(
                                                    Float.parseFloat(shop_list.getJSONObject(j).getString("shop_lat"))
                                                            / 100000);
                                        } catch (NumberFormatException e) {

                                        } catch (JSONException e) {

                                        }
                                    }

                                    BaiduWaimaiShopInfoSummaryModel model = new BaiduWaimaiShopInfoSummaryModel();
                                    model.setRequest_id(task.getRequest_id());
                                    model.setTask_id(task.getTask_id());
                                    model.setKeyword(task.getKeyword());
                                    model.setCity_name(task.getCity_name());
                                    model.setCity_id(task.getCity_id());
                                    model.setShop_id(shop_id);
                                    model.setShop_name(name);
                                    model.setUrl(url);
                                    model.setAverage_speed(average_speed);
                                    model.setMin_price(min_price);
                                    model.setDistribution_fee(distribution_fee);
                                    model.setTotal_score(total_score);
                                    model.setSale(sale);
                                    model.setDistance(distance);
                                    model.setWelfare_info(welfare_info);
                                    model.setAnnouncement(announcement);
                                    model.setDelivery_mode(delivery_mode);
                                    model.setCreate_date(new Date());
                                    model.setCreate_time(new Date());
                                    model.setCategory(list.get(g).getCategory());
                                    model.setSub_category(list.get(g).getSub_category());
                                    model.setCategory_id(list.get(g).getCategory_id());
                                    model.setOffice_id(task.getOffice_id());
                                    model.setOffice_name(task.getOffice_name());
                                    model.setUnique_id(MD5Util
                                            .getMD5(task.getRequest_id() + shop_id + list.get(g).getCategory_id()));
                                    modelList.add(model);
//                                    try {
//                                        TableName.set(task.getRemark());
//                                        biDao.fastInsert(model);
//                                    } catch (Exception e) {
//                                        // logger.error(e);
//                                    } finally {
//                                        TableName.clear();
//                                    }

                                    BaiduWaimaiUrlTaskJob job = new BaiduWaimaiUrlTaskJob();
                                    job.setRequest_id(task.getRequest_id());
                                    job.setTask_id(task.getTask_id());
                                    job.setCreate_time(new Date());
                                    job.setCity_name(task.getCity_name());
                                    job.setKeyword(task.getKeyword());
                                    job.setUrl(url);
                                    job.setShop_id(shop_id);
                                    job.setNode(task.getNode());
                                    job.setStatus(0);
                                    job.setProject_code(task.getProject_code());
                                    job.setDown_type(task.getDown_type());
                                    job.setRemark(task.getRemark());
                                    // job.setLatitude(shop_lat);
                                    // job.setLongitude(shop_lng);
                                    if (combine) {
                                        try {
                                            tDao.insert(job);
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                                try {
                                    TableName.set(task.getRemark());
                                    biDao.fastInsert(modelList);
                                } catch (Exception e) {
                                    // logger.error(e);
                                } finally {
                                    TableName.clear();
                                }
                                logger.info("下载手机端百度外卖simple第" + g + "个分类，共" + list.size() + "个分类，第" + current_page
                                        + "页完成，耗时:" + (System.currentTimeMillis() - time));
                                //if (current_page % 5 == 0) {
                                tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain
                                                .make("total", (counter + task.getTotal())).add("current_page", current_page),
                                        Cnd.where("id", "=", task.getId()));
                                //}
                                current_page++;
                            }
                        } catch (Exception e) {
                            //logger.error("百度外卖simple当前页[" + list_url + "]请求异常", e);
                        }
                    }
                }
            }
            logger.info("手机端百度外卖simple店铺抓取完成");
            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
        } else {
            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
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

    public static void main(String[] args) {
        BaiduWaimaiKeywordTaskJob task = new BaiduWaimaiKeywordTaskJob();
        task.setCity_id("289");
        task.setCity_name("上海");
        task.setShop_name("");
        task.setKeyword("虹口足球场");
        MobileBaiduWaimaiKeywordThread test = new MobileBaiduWaimaiKeywordThread(task);
        test.start();
    }
    /**
     * 判断店铺性质分类量 和 店铺排序分类量 ,取最大的
     * @param l1
     * @param l2
     * @return
     */
    public List<BaiduWaimaiCategory> judge(List<BaiduWaimaiCategory> l1, List<BaiduWaimaiCategory> l2) {
        List<BaiduWaimaiCategory> list = new ArrayList<BaiduWaimaiCategory>();
        if (l1.size() >= l2.size()) {
            for (int i = 0; i < l1.size(); i++) {
                BaiduWaimaiCategory b = new BaiduWaimaiCategory();
                b.setCategory(l1.get(i).getCategory());
                b.setCategory_id(l1.get(i).getCategory_id());
                b.setSub_category(l1.get(i).getSub_category());
                list.add(b);
            }
        } else {
            for (int i = 0; i < l2.size(); i++) {
                BaiduWaimaiCategory b = new BaiduWaimaiCategory();
                b.setCategory(l2.get(i).getCategory());
                b.setCategory_id(l2.get(i).getCategory_id());
                b.setSub_category(l2.get(i).getSub_category());
                list.add(b);
            }
        }
        return list;
    }
}
