package com.reptile.mobile_eleme.test;

import com.bds.base.http.HttpBase;
import com.bds.base.util.CommonUtils;
import com.bds.base.util.MD5Util;
import com.reptile.mobile_eleme.model.ElemeProductInfoModel;
import org.apache.commons.dbcp.BasicDataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.dao.impl.NutDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class TestElemeProduct {

    public static NutDao getDao() {

        long t = System.currentTimeMillis();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ds.setUrl("jdbc:sqlserver://119.90.36.160:1432;instanceName=mssqlserver2016;DatabaseName=test");
        //ds.setUrl("jdbc:mysql://119.90.36.189:3306/buzz_o2o?useUnicode=true&characterEncoding=UTF-8");
        ds.setUsername("bin.e");
        ds.setPassword("hcr@123");
        NutDao dNutDao = new NutDao(ds);
        System.out.println("连接数据库耗时：" + (System.currentTimeMillis() - t) + "毫秒");
        return dNutDao;
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        NutDao dNutDao = getDao();
        try {
            // 请求产品信息
            String webUrl3 = null;
            String shop_id = "895411";
            if (shop_id != null) {
                // webUrl3 =
                // "https://www.ele.me/restapi/shopping/v1/menu?restaurant_id="
                // + shop_id;
                // webUrl3 =
                // "https://mainsite-restapi.ele.me/shopping/v1/menu?restaurant_id="
                // + shop_id;
                webUrl3 = "https://mainsite-restapi.ele.me/shopping/v2/menu?restaurant_id=" + shop_id;
            }
            String result3 = "";
            try {
                result3 = HttpBase.get(webUrl3, "utf-8").getResult();
            } catch (Exception e) {
                //logger.info("", e);
            }
            if (result3.startsWith("[")) {
                try {
                    JSONArray foods = new JSONArray(result3);
                    List<ElemeProductInfoModel> list = new ArrayList<ElemeProductInfoModel>();
                    for (int i = 0; i < foods.length(); i++) {
                        JSONObject jfood = new JSONObject();
                        // 按类别分
                        jfood = foods.getJSONObject(i);
                        String type = jfood.getString("name");
                        String type_description = jfood.getString("description");
                        JSONArray jfoods = jfood.getJSONArray("foods");
                        long t = System.currentTimeMillis();
                        for (int j = 0; j < jfoods.length(); j++) {
                            JSONObject jsonObject_food = new JSONObject();
                            // 按产品分
                            jsonObject_food = jfoods.getJSONObject(j);
                            String product_description = jsonObject_food.getString("description");
                            String satisfy_rate = jsonObject_food.getString("satisfy_rate");
                            //elemeProductInfoModel.setCity_name(task.getCity_name());
                            //elemeProductInfoModel.setKeyword(task.getKeyword());
                            JSONArray specfoods = new JSONArray();
                            specfoods = jsonObject_food.getJSONArray("specfoods");
                            long product_time = System.currentTimeMillis();
                            for (int k = 0; k < specfoods.length(); k++) {
                                ElemeProductInfoModel elemeProductInfoModel = new ElemeProductInfoModel();
                                elemeProductInfoModel.setRequest_id(777);
                                elemeProductInfoModel.setTask_id(777);
                                elemeProductInfoModel.setCreate_time(new Date());
                                elemeProductInfoModel.setShop_id(shop_id);
                                // 品类
                                elemeProductInfoModel.setType(type);
                                elemeProductInfoModel.setType_description(type_description);
                                String activities = "";
                                if (jsonObject_food.has("activities")) {
                                    try {
                                        JSONObject activity_limitation_json = jsonObject_food.getJSONObject("activity");
                                        elemeProductInfoModel
                                                .setActivity_limitation(activity_limitation_json.getString("image_text"));
                                    } catch (Exception e) {

                                    }
                                }
                                JSONObject specfood = new JSONObject();
                                specfood = specfoods.getJSONObject(k);
                                elemeProductInfoModel.setProduct_description(product_description);
                                // 店铺名
                                //elemeProductInfoModel.setShop_name(task.getShop_name());
                                // 菜id
                                String food_id = null;
                                food_id = specfood.getString("food_id");
                                elemeProductInfoModel.setFood_id(food_id);
                                // 菜名
                                String product_name = null;
                                product_name = specfood.getString("name");
                                elemeProductInfoModel.setProduct_name(product_name);
                                // 评分
                                String recent_rating = null;
                                recent_rating = specfood.getString("recent_rating");
                                elemeProductInfoModel.setScore(recent_rating);
                                // 月销量
                                String recent_popularity = null;
                                recent_popularity = jsonObject_food.getString("tips");
                                elemeProductInfoModel
                                        .setMonthly_sales(CommonUtils.subString("月售", "份", recent_popularity));
                                // 满意度
                                elemeProductInfoModel.setSatisfy_rate(satisfy_rate);
                                // 价格
                                String price = null;
                                price = specfood.getString("price");
                                elemeProductInfoModel.setPrice(price);
                                // 原价
                                String original_price = null;
                                original_price = specfood.getString("original_price");
                                elemeProductInfoModel.setOriginal_price(original_price);
                                // 品类(规格)数量,2016-12-14添加
                                long Specification_Amount = 0L;
                                Specification_Amount = specfoods.length();
                                elemeProductInfoModel.setSpecification_Amount(Specification_Amount);
                                // 品类名称,2017-02-13添加
                                String specification = "";
                                if (specfood.has("specs")) {
                                    try {
                                        JSONArray spec_names = specfood.getJSONArray("specs");
                                        if (spec_names.length() > 0) {
                                            elemeProductInfoModel
                                                    .setSpecification(spec_names.getJSONObject(0).getString("value"));
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                                elemeProductInfoModel.setUnique_id(MD5Util.getMD5(
                                        shop_id + type + food_id + 777 + 777));
                                //System.out.print(elemeProductInfoModel.getUnique_id() + "\n");
                                if (noDuplicated(elemeProductInfoModel, list)) {
                                    list.add(elemeProductInfoModel);
                                }
                                //System.out.print(food_id + "\n");
//                                try {
//                                    TableName.set(null);
//                                    dNutDao.insert(elemeProductInfoModel);
//                                } catch (Exception e) {
//                                    // logger.error("", e);
//                                } finally {
//                                    TableName.clear();
//                                }
                            }
//                            System.out.print("插入一条数据耗时:" +
//                                    (System.currentTimeMillis() - product_time));

                        }
                        // logger.info("插入所有数据耗时:" + (System.currentTimeMillis()
                        // - t));
                    }
                    System.out.println("main thread");
                    //List<ProductInsertThread> thread_list = new ArrayList<ProductInsertThread>();
                    CountDownLatch cdl = new CountDownLatch(1);
                    for (int i = 0; i < 1; i++) {
                        ProductInsertThread t1 = new ProductInsertThread(cdl, "Thread" + i, dNutDao, list);
                        t1.start();
                        //thread_list.add(t1);
                    }
                    cdl.await();
                    System.out.print("==========================产品解析完成，耗时：" + (System.currentTimeMillis() - time)
                            + "============================");
//					tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", 2),
//							Cnd.where("id", "=", task.getId()));
                } catch (JSONException e) {
                    //logger.info("", e);
                }
            }
        } catch (Exception e) {
//			logger.info("", e);
//			tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", 3), Cnd.where("id", "=", task.getId()));
            return;
        }
    }

    public static boolean noDuplicated(ElemeProductInfoModel elemeProductInfoModel, List<ElemeProductInfoModel> list) {
        //System.out.print("当前list大小:" + list.size() + "\n");
        boolean flag = false;
        if (list.size() == 0) {
            flag = true;
        } else {
            for (ElemeProductInfoModel model : list) {
                if (model.getUnique_id().equals(elemeProductInfoModel.getUnique_id())) {
                    //System.out.print(model.getUnique_id() + ";" + elemeProductInfoModel.getUnique_id() + "\n");
                    return flag;
                }
            }
            flag = true;
        }
        return flag;
    }
}
