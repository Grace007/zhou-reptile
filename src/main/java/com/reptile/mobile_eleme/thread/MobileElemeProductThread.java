package com.reptile.mobile_eleme.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.CommonUtils;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.mobile_eleme.ElemeHttpBase;
import com.reptile.mobile_eleme.job.ElemeProductTaskJob;
import com.reptile.mobile_eleme.model.ElemeProductInfoModel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.TableName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取产品信息
 */
public class MobileElemeProductThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileElemeProductThread.class);
    private ElemeProductTaskJob task;

    public MobileElemeProductThread(ElemeProductTaskJob task) {
        this.task = task;
    }

    DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run() {
        logger.info("==========================开始下载手机端饿了么产品信息============================");
        long time = System.currentTimeMillis();
        TaskToBi tobi = BIDataSource.getBiDataSource("eleme", task.getProject_code(), task.getDown_type());
        if (tobi == null) {
            logger.info("连接异常");
            return;
        }
        Dao biDao = tobi.getBiDao();
        try {
            tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", 1), Cnd.where("id", "=", task.getId()));
        } catch (Exception e1) {
            logger.info("ElemeProductThread更新状态异常");
            return;
        }
        String url = task.getUrl();
        // https://www.ele.me/shop/379954
        String shop_id = null;
        if (url.contains("shop")) {
            try {
                shop_id = task.getShop_id();
                if (StringUtils.isEmpty(shop_id)) {
                    tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", -1),
                            Cnd.where("id", "=", task.getId()));
                    return;
                }
            } catch (Exception e) {
                tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", 3),
                        Cnd.where("id", "=", task.getId()));
                return;
            }
        }
        try {
            // 请求产品信息
            String webUrl3 = null;
            if (shop_id != null) {
                // webUrl3 =
                // "https://www.ele.me/restapi/shopping/v1/menu?restaurant_id="
                // + shop_id;
                // webUrl3 =
                // "https://mainsite-restapi.ele.me/shopping/v1/menu?restaurant_id="
                // + shop_id;
                //webUrl3 = "https://mainsite-restapi.ele.me/shopping/v2/menu?restaurant_id=" + shop_id;
                webUrl3 = "https://restapi.ele.me/shopping/v2/menu?restaurant_id=" + shop_id;	
            }
            String result3 = "";
            try {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Referer", "https://h5.ele.me/shop/");
                headers.put("Origin", "https://h5.ele.me");
                headers.put("Host", "restapi.ele.me");
                headers.put("X-Shard", "shopid="+shop_id);
                headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
                result3 = ElemeHttpBase.get(webUrl3, "utf-8", headers).getResult();
                if (result3.length()<5) {
                	webUrl3 = "https://mainsite-restapi.ele.me/shopping/v2/menu?restaurant_id=" + shop_id;
                	result3 = ElemeHttpBase.get(webUrl3, "utf-8", headers).getResult();
				}else if(result3.length()<5){
					 tDao.update(ElemeProductTaskJob.class, Chain.make("status", 5), Cnd.where("id", "=", task.getId()));
					 return;
				}
            } catch (Exception e) {
                logger.info("", e);
            }
            if (result3.startsWith("[")) {
                try {
                    JSONArray foods = new JSONArray(result3);
                    List<ElemeProductInfoModel> product_list = new ArrayList<ElemeProductInfoModel>();
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
                            JSONArray specfoods = new JSONArray();
                            specfoods = jsonObject_food.getJSONArray("specfoods");
                            long product_time = System.currentTimeMillis();
                            for (int k = 0; k < specfoods.length(); k++) {
                                ElemeProductInfoModel elemeProductInfoModel = new ElemeProductInfoModel();
                                elemeProductInfoModel.setCity_name(task.getCity_name());
                                elemeProductInfoModel.setKeyword(task.getKeyword());
                                elemeProductInfoModel.setRequest_id(task.getRequest_id());
                                elemeProductInfoModel.setTask_id(task.getTask_id());
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
                                elemeProductInfoModel.setShop_name(task.getShop_name());
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
                                        shop_id + type + food_id + task.getProject_code() + task.getRequest_id()));
                                product_list.add(elemeProductInfoModel);
//                                try {
//                                    TableName.set(task.getRemark());
//                                    biDao.insert(elemeProductInfoModel);
//                                } catch (Exception e) {
//                                    // logger.error("", e);
//                                } finally {
//                                    TableName.clear();
//                                }
                            }
                            // logger.info("插入一条数据耗时:" +
                            // (System.currentTimeMillis() - product_time));

                        }
                        // logger.info("插入所有数据耗时:" + (System.currentTimeMillis()
                        // - t));
                    }
                    try {
                        TableName.set(task.getRemark());
                        biDao.fastInsert(product_list);
                    } catch (Exception e) {
                         //logger.error("", e);
                    } finally {
                        TableName.clear();
                    }
                    logger.info("==========================产品解析完成，耗时：" + (System.currentTimeMillis() - time)
                            + "============================");
                    tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", 2),
                            Cnd.where("id", "=", task.getId()));
                } catch (JSONException e) {
                    logger.info("", e);
                }
            } else {
                logger.info("重置饿了么产品状态");
                tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", 0),
                        Cnd.where("id", "=", task.getId()));
                return;
            }
        } catch (Exception e) {
            logger.info("", e);
            tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", 3), Cnd.where("id", "=", task.getId()));
            return;
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
