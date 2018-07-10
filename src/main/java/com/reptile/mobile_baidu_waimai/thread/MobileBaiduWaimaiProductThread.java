package com.reptile.mobile_baidu_waimai.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.CommonUtils;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.mobile_baidu_waimai.BaiduHttpBase;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiCommentDailyTaskJob;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiCommentTaskJob;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiProductTaskJob;
import com.reptile.mobile_baidu_waimai.model.BaiduWaimaiProductInfoModel;
import com.reptile.mobile_baidu_waimai.model.BaiduWaimaiShopInfoModel;
import com.reptile.server.MyConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.TableName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MobileBaiduWaimaiProductThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileBaiduWaimaiProductThread.class);
    private BaiduWaimaiProductTaskJob task;

    public MobileBaiduWaimaiProductThread(BaiduWaimaiProductTaskJob task) {
        this.task = task;
    }

    DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        TaskToBi tobi = BIDataSource.getBiDataSource("baidu_waimai", task.getProject_code(), task.getDown_type());
        Dao biDao = tobi.getBiDao();
        tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        String url = task.getUrl();

        // http://waimai.baidu.com/waimai/shop/1549350372
        String shop_id = task.getShop_id();
        if (!"".equals(url) && url != null) {
            String result = null;
            try {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Host", "waimai.baidu.com");
                result = BaiduHttpBase.get(url, "utf-8", headers).getResult();
                if (result.contains("该商家无相应菜单") || result.contains("本店休息中，暂不接受订单")) {
                    logger.info("本店休息中，暂不接受订单");
                    tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", 404),
                            Cnd.where("id", "=", task.getId()));
                    return;
                }
            } catch (Exception e) {
                tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", 3),
                        Cnd.where("id", "=", task.getId()));
                return;
            }
            Document document = null;
            document = Jsoup.parse(result);
            BaiduWaimaiShopInfoModel baiduWaimaiShopInfoModel = new BaiduWaimaiShopInfoModel();
            baiduWaimaiShopInfoModel.setCity_name(task.getCity_name());
            baiduWaimaiShopInfoModel.setKeyword(task.getKeyword());
            baiduWaimaiShopInfoModel.setRequest_id(task.getRequest_id());
            baiduWaimaiShopInfoModel.setTask_id(task.getTask_id());
            baiduWaimaiShopInfoModel.setCreate_time(new Date());
            baiduWaimaiShopInfoModel.setShop_id(shop_id);
            baiduWaimaiShopInfoModel.setUrl(url);
            String shop_name = null;
            try {
                String json = CommonUtils.subString("result: ", "});", document.toString());
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.has("shop_name")) {
                    // 店铺名称
                    shop_name = jsonObject.getString("shop_name");
                    baiduWaimaiShopInfoModel.setShop_name(jsonObject.getString("shop_name"));
                }
                if (jsonObject.has("average_score")) {
                    // 评分
                    baiduWaimaiShopInfoModel.setShop_score(jsonObject.getString("average_score"));
                }
                if (jsonObject.has("shop_logo")) {
                    // 评分
                    baiduWaimaiShopInfoModel.setShop_logo(jsonObject.getString("shop_logo"));
                }
                if (jsonObject.has("bussiness_time")) {
                    // 营业时间
                    baiduWaimaiShopInfoModel.setBusiness_hour(jsonObject.getString("bussiness_time"));
                }
                if (jsonObject.has("address")) {
                    // 商家地址
                    baiduWaimaiShopInfoModel.setShop_address(jsonObject.getString("address"));
                }
                if (jsonObject.has("delivery_time")) {
                    // 平均送餐时间
                    baiduWaimaiShopInfoModel.setAverage_speed(jsonObject.getString("delivery_time"));
                }
                if (jsonObject.has("takeout_price")) {
                    // 起送价
                    baiduWaimaiShopInfoModel.setMin_price(jsonObject.getString("takeout_price"));
                }
                if (jsonObject.has("takeout_cost")) {
                    // 配送费
                    baiduWaimaiShopInfoModel.setDistribution_fee(jsonObject.getString("takeout_cost"));
                }
                if (jsonObject.has("average_score")) {
                    // 总分
                    baiduWaimaiShopInfoModel.setTotal_score(jsonObject.getString("average_score"));
                }
                if (jsonObject.has("comment_num")) {
                    // 评论总数
                    baiduWaimaiShopInfoModel.setTotal_comments(jsonObject.getString("comment_num"));
                }
                if (jsonObject.has("score_detail")) {
                    // 评论人数
                    try {
                        baiduWaimaiShopInfoModel
                                .setFive_star_numbers(jsonObject.getJSONObject("score_detail").getString("5"));
                        baiduWaimaiShopInfoModel
                                .setFour_star_numbers(jsonObject.getJSONObject("score_detail").getString("4"));
                        baiduWaimaiShopInfoModel
                                .setThree_star_numbers(jsonObject.getJSONObject("score_detail").getString("3"));
                        baiduWaimaiShopInfoModel
                                .setTwo_star_numbers(jsonObject.getJSONObject("score_detail").getString("2"));
                        baiduWaimaiShopInfoModel
                                .setOne_star_numbers(jsonObject.getJSONObject("score_detail").getString("1"));
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
                String payment_info = "";
                if (jsonObject.has("welfare_info")) {
                    // 支付信息
                    try {
                        JSONArray welfare = jsonObject.getJSONArray("welfare_info");
                        if (welfare.length() > 0) {
                            for (int i = 0; i < welfare.length(); i++) {
                                payment_info += welfare.getJSONObject(i).getString("msg") + ";";
                            }
                        }
                    } catch (Exception e) {

                    }
                }
                if (jsonObject.has("welfare_basic_info")) {
                    // 支付信息
                    try {
                        JSONArray welfare_basic = jsonObject.getJSONArray("welfare_basic_info");
                        if (welfare_basic.length() > 0) {
                            for (int i = 0; i < welfare_basic.length(); i++) {
                                payment_info += welfare_basic.getJSONObject(i).getString("msg") + ";";
                            }
                        }
                    } catch (Exception e) {

                    }
                }
                baiduWaimaiShopInfoModel.setPayment_info(payment_info);
                if (jsonObject.has("shop_announcement")) {
                    // 商家公告
                    baiduWaimaiShopInfoModel.setAnnouncement(jsonObject.getString("shop_announcement"));
                }
                if (jsonObject.has("average_service_score")) {
                    // 服务评分
                    baiduWaimaiShopInfoModel.setService_score(jsonObject.getString("average_service_score"));
                }
                if (jsonObject.has("average_dish_score")) {
                    // 菜品评分
                    baiduWaimaiShopInfoModel.setDish_score(jsonObject.getString("average_dish_score"));
                }
                if (jsonObject.has("saled_month")) {
                    // 月销量
                    baiduWaimaiShopInfoModel.setMonthly_sales(jsonObject.getString("saled_month"));
                }
                if (jsonObject.has("shop_phone")) {
                    // 手机号
                    baiduWaimaiShopInfoModel.setPhone(jsonObject.getString("shop_phone"));
                }
            } catch (Exception e1) {
                logger.error("手机端百度外卖截取异常");
            }
            baiduWaimaiShopInfoModel.setUnique_id(MD5Util.getMD5(url + task.getRequest_id()));
            baiduWaimaiShopInfoModel.setLatitude(task.getLatitude());
            baiduWaimaiShopInfoModel.setLongitude(task.getLongitude());
            try {
                TableName.set(task.getRemark());
                if (StringUtils.isNotEmpty(baiduWaimaiShopInfoModel.getMonthly_sales())) {
                    biDao.insert(baiduWaimaiShopInfoModel);
                } else {
                    logger.info("未抓到月销量，重置状态");
                    tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", 0),
                            Cnd.where("id", "=", task.getId()));
                    return;
                }
            } catch (Exception e) {
            } finally {
                TableName.clear();
            }
            logger.info("==========================店铺解析完成，耗时：" + (System.currentTimeMillis() - time)
                    + "============================");
            // 获取菜品，网页版
            try {
                result = HttpBase.get("https://waimai.baidu.com/waimai/shop/" + shop_id, "utf-8").getResult();
                document = Jsoup.parse(result);
            } catch (Exception e) {
                tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", -3),
                        Cnd.where("id", "=", task.getId()));
                return;
            }
            Elements menu_lists = document.select(".menu-list>.list-wrap");
            List<BaiduWaimaiProductInfoModel> product_list = new ArrayList<BaiduWaimaiProductInfoModel>();
            if (menu_lists.size() > 0) {
                for (Element menu_list : menu_lists) {
                    // 品类
                    String type = null;
                    try {
                        type = menu_list.select(".list-status").text();
                    } catch (Exception e) {
                        type = "";
                    }
                    Elements products = menu_list.select("ul").select("li");
                    for (Element product : products) {
                        long dish_time = System.currentTimeMillis();
                        BaiduWaimaiProductInfoModel baiduWaimaiProductInfoModel = new BaiduWaimaiProductInfoModel();
                        baiduWaimaiProductInfoModel.setCity_name(task.getCity_name());
                        baiduWaimaiProductInfoModel.setKeyword(task.getKeyword());
                        baiduWaimaiProductInfoModel.setRequest_id(task.getRequest_id());
                        baiduWaimaiProductInfoModel.setTask_id(task.getTask_id());
                        baiduWaimaiProductInfoModel.setCreate_time(new Date());
                        baiduWaimaiProductInfoModel.setShop_id(shop_id);
                        if (StringUtils.isNotEmpty(shop_name)) {
                            baiduWaimaiProductInfoModel.setShop_name(shop_name);
                        } else {
                            baiduWaimaiProductInfoModel.setShop_name(task.getShop_name());
                        }
                        baiduWaimaiProductInfoModel.setType(type);
                        // 菜品名称
                        String product_name = null;
                        try {
                            product_name = product.select(".info").select("h3").attr("data-title");
                        } catch (Exception e) {
                            product_name = "";
                        }
                        baiduWaimaiProductInfoModel.setProduct_name(product_name);
                        // 菜品描述
                        String product_description = null;
                        try {
                            product_description = product.select(".info").select("h3").attr("data-content");
                        } catch (Exception e) {
                            product_description = "";
                        }
                        baiduWaimaiProductInfoModel.setProduct_description(product_description);
                        // 菜品ID
                        String product_id = null;
                        // item_1549923431
                        try {
                            product_id = product.attr("data-sid").replace("item_", "");
                        } catch (Exception e1) {
                            product_id = "";
                        }
                        baiduWaimaiProductInfoModel.setProduct_id(product_id);
                        // 推荐人数
                        String recommendation = null;
                        try {
                            recommendation = product.select(".info-desc>.m-hot-value").get(0).select("span").get(0).text();
                        } catch (Exception e) {
                            recommendation = "-1";
                        }
                        baiduWaimaiProductInfoModel.setRecommendation(recommendation);
                        // 销量
                        String sales = null;
                        try {
                            sales = product.select(".info-desc>.m-hot-value").get(0).select("span").get(2).text();
                        } catch (Exception e) {
                            sales = "-1";
                        }
                        baiduWaimaiProductInfoModel.setSales(sales);
                        // 库存
                        String inventory = null;
                        try {
                            inventory = product.select(".info-desc>.m-hot-value").get(1).select("span").text();
                        } catch (Exception e) {
                            inventory = "-1";
                        }
                        baiduWaimaiProductInfoModel.setInventory(inventory);
                        // 现价
                        String current_price = null;
                        try {
                            current_price = product.select(".m-price").select("strong").get(0).text();
                        } catch (Exception e) {
                            current_price = "-1";
                        }
                        try {
                            if ("-1".equals(current_price) || "".equals(current_price)) {
                                current_price = product.select(".m-break").select("strong").get(0).text();
                            }
                        } catch (Exception e1) {
                            current_price = "-1";
                        }
                        baiduWaimaiProductInfoModel.setCurrent_price(current_price);
                        // 原价
                        String original_price = null;
                        try {
                            original_price = product.select(".m-price").select("del").select("strong").text();
                        } catch (Exception e) {
                            original_price = "-1";
                        }
                        try {
                            if ("-1".equals(original_price) || "".equals(original_price)) {
                                original_price = product.select(".m-break").select("del").select("strong").text();
                            }
                        } catch (Exception e1) {
                            original_price = "-1";
                        }
                        baiduWaimaiProductInfoModel.setOriginal_price(original_price);
                        baiduWaimaiProductInfoModel
                                .setUnique_id(MD5Util.getMD5(shop_id + type + product_id + task.getRequest_id()));
                        // 品类(规格)数量,2016-12-14添加
                        long Specification_Amount = 0L;
                        // 根据规格插入多条
                        if (current_price.contains("起")) {
                            if (!"[]".equals(product.select(".have_attr").attr("data-content"))) {
                                try {
                                    JSONObject spec = new JSONObject(
                                            product.select(".have_attr").attr("data-content").toString());
                                    JSONArray specs = spec.getJSONArray("规格");
                                    for (int i = 0; i < specs.length(); i++) {
                                        String s_product_id = null;
                                        try {
                                            s_product_id = specs.getJSONObject(i).getString("dish_attr_id");
                                        } catch (Exception e1) {

                                        }
                                        String s_name = null;
                                        try {
                                            s_name = specs.getJSONObject(i).getString("name");
                                        } catch (Exception e1) {

                                        }
                                        String s_product_price = null;
                                        try {
                                            s_product_price = specs.getJSONObject(i).getString("price");
                                        } catch (Exception e1) {

                                        }
                                        BaiduWaimaiProductInfoModel model = new BaiduWaimaiProductInfoModel();
                                        model.setCity_name(task.getCity_name());
                                        model.setKeyword(task.getKeyword());
                                        model.setRequest_id(task.getRequest_id());
                                        model.setTask_id(task.getTask_id());
                                        model.setCreate_time(new Date());
                                        model.setShop_id(shop_id);
                                        if (StringUtils.isNotEmpty(shop_name)) {
                                            model.setShop_name(shop_name);
                                        } else {
                                            model.setShop_name(task.getShop_name());
                                        }
                                        model.setType(type);
                                        model.setProduct_name(product_name);
                                        model.setProduct_description(product_description);
                                        model.setProduct_id(s_product_id);
                                        model.setRecommendation(recommendation);
                                        model.setSales(sales);
                                        model.setInventory(inventory);
                                        model.setCurrent_price("¥" + s_product_price);
                                        model.setOriginal_price(original_price);
                                        model.setSpecification(s_name);
                                        model.setUnique_id(
                                                MD5Util.getMD5(shop_id + type + s_product_id + task.getRequest_id()));
                                        Specification_Amount = specs.length();
                                        model.setSpecification_Amount(Specification_Amount);
                                        product_list.add(baiduWaimaiProductInfoModel);
//                                        try {
//                                            TableName.set(task.getRemark());
//                                            biDao.insert(model);
//                                        } catch (Exception e) {
//                                        } finally {
//                                            TableName.clear();
//                                        }
                                    }
                                    continue;// 跳出
                                } catch (JSONException e) {
                                    logger.error("", e);
                                }
                            } else {
                                baiduWaimaiProductInfoModel.setCurrent_price(current_price);
                            }
                        } else {
                            baiduWaimaiProductInfoModel.setCurrent_price(current_price);
                        }
                        // 单一规格或者是规格不影响价格
                        baiduWaimaiProductInfoModel.setSpecification_Amount(1L);
                        product_list.add(baiduWaimaiProductInfoModel);
//                        try {
//                            TableName.set(task.getRemark());
//                            biDao.insert(baiduWaimaiProductInfoModel);
//                            // logger.info("==========================产品插入成功，耗时：" +
//                            // (System.currentTimeMillis() - dish_time) +
//                            // "============================");
//                        } catch (Exception e) {
//                            // logger.info("==========================产品解析完成，耗时：" +
//                            // (System.currentTimeMillis() - dish_time)
//                            // + "============================");
//                            // logger.error("", e);
//                        } finally {
//                            TableName.clear();
//                        }
                    }
                }
            } else {
                //String other_url = "http://waimai.baidu.com/mobile/waimai?qt=shopview&shop_id=" + shop_id;
                String other_result = "";
                try {
                    other_result = HttpBase.get(url, "utf-8").getResult();
                    String jsonStr = CommonUtils.subString("shopmenu\")).init(", ");", other_result);
                    JSONObject json = new JSONObject(jsonStr);
                    if (json.has("takeout_menu")) {
                        JSONArray menu = json.getJSONArray("takeout_menu");
                        for (int i = 0; i < menu.length(); i++) {
                            String category_name = menu.getJSONObject(i).getString("catalog");
                            String category_id = menu.getJSONObject(i).getString("category_id");
                            JSONArray data = menu.getJSONObject(i).getJSONArray("data");
                            for (int j = 0; j < data.length(); j++) {
                                String product_name = data.getJSONObject(j).getString("name");
                                String product_description = data.getJSONObject(j).getString("description");
                                String product_id = data.getJSONObject(j).getString("item_id");
                                String origin_price = data.getJSONObject(j).getString("origin_price");
                                String current_price = data.getJSONObject(j).getString("current_price");
                                String saled = data.getJSONObject(j).getString("saled");
                                String left_num = data.getJSONObject(j).getString("left_num");
                                String recommend_num = data.getJSONObject(j).getString("recommend_num");
                                JSONArray specs = data.getJSONObject(j).getJSONArray("dish_attr");
                                if (specs.length() > 0) {
                                    //多规格
                                    for (int k = 0; k < specs.length(); k++) {
                                        String s_product_id = specs.getJSONObject(k).getString("dish_attr_id");
                                        String spec_name = specs.getJSONObject(k).getString("name");
                                        String s_price = specs.getJSONObject(k).getString("price");
                                        String s_store = specs.getJSONObject(k).getString("store_num");

                                        BaiduWaimaiProductInfoModel model = new BaiduWaimaiProductInfoModel();
                                        model.setRequest_id(task.getRequest_id());
                                        model.setTask_id(task.getTask_id());
                                        model.setCreate_time(new Date());
                                        model.setShop_id(shop_id);
                                        model.setType(category_name);
                                        model.setProduct_name(product_name);
                                        model.setProduct_description(product_description);
                                        model.setProduct_id(s_product_id);
                                        model.setRecommendation(recommend_num + "人推荐");
                                        model.setSales("月售" + saled + "份");
                                        model.setInventory(s_store);
                                        model.setCurrent_price("￥" + s_price);
                                        //model.setOriginal_price("￥" + list.getJSONObject(j).getString("origin_price"));
                                        model.setShop_name(task.getShop_name());
                                        model.setCity_name(task.getCity_name());
                                        model.setKeyword(task.getKeyword());
                                        model.setSpecification_Amount(specs.length());
                                        model.setSpecification(spec_name);
                                        model.setUnique_id(MD5Util.getMD5(shop_id + category_name + s_product_id + task.getRequest_id()));
                                        product_list.add(model);
//                                        try {
//                                            TableName.set(task.getRemark());
//                                            biDao.insert(model);
//                                        } catch (Exception e) {
//                                        } finally {
//                                            TableName.clear();
//                                        }
                                    }
                                } else {
                                    //单规格
                                    BaiduWaimaiProductInfoModel model = new BaiduWaimaiProductInfoModel();
                                    model.setRequest_id(task.getRequest_id());
                                    model.setTask_id(task.getTask_id());
                                    model.setCreate_time(new Date());
                                    model.setShop_id(shop_id);
                                    model.setType(category_name);
                                    model.setProduct_name(product_name);
                                    model.setProduct_description(product_description);
                                    model.setProduct_id(product_id);
                                    model.setRecommendation(recommend_num + "人推荐");
                                    model.setSales("月售" + saled + "份");
                                    model.setInventory(left_num);
                                    model.setCurrent_price("￥" + current_price);
                                    model.setOriginal_price("￥" + origin_price);
                                    model.setShop_name(task.getShop_name());
                                    model.setCity_name(task.getCity_name());
                                    model.setKeyword(task.getKeyword());
                                    model.setSpecification_Amount(1);
                                    model.setUnique_id(MD5Util.getMD5(shop_id + category_name + product_id + task.getRequest_id()));
                                    product_list.add(model);
//                                    try {
//                                        TableName.set(task.getRemark());
//                                        biDao.insert(model);
//                                    } catch (Exception e) {
//                                    } finally {
//                                        TableName.clear();
//                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", 3),
                            Cnd.where("id", "=", task.getId()));
                }

            }
            try {
                TableName.set(task.getRemark());
                biDao.fastInsert(product_list);
            } catch (Exception e) {
            } finally {
                TableName.clear();
            }
            logger.info("==========================产品解析完成，耗时：" + (System.currentTimeMillis() - time)
                    + "============================");
            if (MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.GENERATE_COMMENT) {
                // 生成评论URL
                int pages = 0;
                int total = 0;
                try {
                    try {
                        // total = Integer.parseInt(CommonUtils.subString("共",
                        // "条评价", total_comments));
                        total = Integer.parseInt(task.getComment_number());
                    } catch (Exception e1) {
                        total = 0;
                    }
                    if (total > 0) {
                        pages = calculatePage(total, 60, MyConstant.BAIDU_WAIMAI_CONFIG.PAGE_MAX);
                        try {
                            if (StringUtils.isNotEmpty(task.getRemark())) {
                                BaiduWaimaiCommentTaskJob job = new BaiduWaimaiCommentTaskJob();
                                job.setCity_name(task.getCity_name());
                                job.setKeyword(task.getKeyword());
                                job.setRequest_id(task.getRequest_id());
                                job.setTask_id(task.getTask_id());
                                job.setCreate_time(new Date());
                                job.setShop_id(shop_id);
                                if (StringUtils.isNotEmpty(shop_name)) {
                                    job.setShop_name(shop_name);
                                } else {
                                    job.setShop_name(task.getShop_name());
                                }
                                job.setNode(task.getNode());
                                job.setPage_from(1);
                                job.setPage_to(pages);
                                job.setTotal(total);
                                job.setStatus(0);
                                job.setProject_code(task.getProject_code());
                                job.setDown_type(task.getDown_type());
                                job.setRemark("_all");
                                tDao.insert(job);
                            } else {
                                BaiduWaimaiCommentDailyTaskJob job = new BaiduWaimaiCommentDailyTaskJob();
                                job.setCity_name(task.getCity_name());
                                job.setKeyword(task.getKeyword());
                                job.setRequest_id(task.getRequest_id());
                                job.setTask_id(task.getTask_id());
                                job.setCreate_time(new Date());
                                job.setShop_id(shop_id);
                                if (StringUtils.isNotEmpty(shop_name)) {
                                    job.setShop_name(shop_name);
                                } else {
                                    job.setShop_name(task.getShop_name());
                                }
                                job.setNode(task.getNode());
                                job.setPage_from(1);
                                job.setPage_to(pages);
                                job.setTotal(total);
                                job.setStatus(0);
                                job.setProject_code(task.getProject_code());
                                job.setDown_type(task.getDown_type());
                                tDao.insert(job);
                            }
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                        logger.info("==========================评论URL生成完成，耗时：" + (System.currentTimeMillis() - time)
                                + "============================");
                    } else {
                        tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", -1),
                                Cnd.where("id", "=", task.getId()));
                        logger.info("==========================店铺：" + url + "，没有评论============================");
                        return;
                    }
                } catch (NumberFormatException e) {
                    tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", 3),
                            Cnd.where("id", "=", task.getId()));
                    logger.info("", e);
                    return;
                }
            }
            tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
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
