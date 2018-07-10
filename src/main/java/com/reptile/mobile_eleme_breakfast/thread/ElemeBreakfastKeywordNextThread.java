package com.reptile.mobile_eleme_breakfast.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.mobile_eleme_breakfast.httpbase.MobileElemeHttpBase;
import com.reptile.mobile_eleme_breakfast.job.ElemeBreakfastKeywordNextJob;
import com.reptile.mobile_eleme_breakfast.model.ElemeBreakfastProductInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ElemeBreakfastKeywordNextThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(ElemeBreakfastKeywordNextThread.class);
    private ElemeBreakfastKeywordNextJob task;

    public ElemeBreakfastKeywordNextThread(ElemeBreakfastKeywordNextJob task) {
        this.task = task;
    }

    public void run() {
        long total_time = System.currentTimeMillis();
        logger.info("开始抓取饿了么预定早餐办公楼[" + task.getBuilding_name() + "]产品信息...");
        tDao.update(ElemeBreakfastKeywordNextJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        TaskToBi tobi = BIDataSource.getBiDataSource("eleme", task.getProject_code(), task.getDown_type());
        Dao biDao = tobi.getBiDao();
        String building_id = task.getBuilding_id(), device_no = "";
        if (StringUtils.isEmpty(building_id)) {
            logger.info("饿了么building_id为空");
            tDao.update(ElemeBreakfastKeywordNextJob.class, Chain.make("status", -1), Cnd.where("id", "=", task.getId()));
            return;
        } else {
            String req_url = "https://zaocan.ele.me/api/v5/dishNew";
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Accept", "application/json, text/plain, */*");
            header.put("Accept-Encoding", "gzip, deflate, br");
            header.put("Accept-Language", "zh-CN,zh;q=0.8");
            header.put("Content-Type", "application/json;charset=UTF-8");
            header.put("Host", "zaocan.ele.me");
            header.put("Origin", "https://zaocan.ele.me");
            header.put("Referer", "https://zaocan.ele.me/menu/" + building_id);
            header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("buildingId", building_id);
            data.put("deviceNo", device_no);
            JSONObject o = new JSONObject();
            try {
                o.put("deviceNo", "");
                o.put("buildingId", building_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String content;
            try {
                content = MobileElemeHttpBase.post(req_url, "utf-8", header, o.toString()).getResult();
                JSONObject result = new JSONObject(content);
                if ("SUCCESS".equals(result.getString("status"))) {
                    //插入BI
                    List<ElemeBreakfastProductInfo> infos = getList(result.getJSONObject("data"));
                    if (infos.size() > 0) {
                        try {
                            long time = System.currentTimeMillis();
                            biDao.fastInsert(infos);
                            logger.info("插入[" + infos.size() + "]条数据耗时：" + (System.currentTimeMillis() - time));
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }
                    logger.info("抓取饿了么预定早餐办公楼[" + task.getBuilding_name() + "]产品信息完成，总耗时：" + (System.currentTimeMillis() - total_time));
                    tDao.update(ElemeBreakfastKeywordNextJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
                } else {
                    logger.info("饿了么预定早餐办公楼[" + task.getBuilding_name() + "]下无产品信息");
                    tDao.update(ElemeBreakfastKeywordNextJob.class, Chain.make("status", 404), Cnd.where("id", "=", task.getId()));
                    return;
                }
            } catch (Exception e) {
                logger.error("", e);
                tDao.update(ElemeBreakfastKeywordNextJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            }
        }
    }

    public List<ElemeBreakfastProductInfo> getList(JSONObject data) {
        List<ElemeBreakfastProductInfo> infos = new ArrayList<ElemeBreakfastProductInfo>();
        if (data != null) {
            if (data.has("menus")) {
                try {
                    JSONArray menus = data.getJSONArray("menus");
                    if (menus != null) {
                        for (int i = 0; i < menus.length(); i++) {
                            String date = menus.getJSONObject(i).getString("date");
                            String day = menus.getJSONObject(i).getString("day");
                            JSONArray cats = menus.getJSONObject(i).getJSONArray("category");
                            if (menus.getJSONObject(i).has("options")) {
                                JSONArray options = menus.getJSONObject(i).getJSONArray("options");
                                for (int j = 0; j < options.length(); j++) {
                                    ElemeBreakfastProductInfo info = new ElemeBreakfastProductInfo();
                                    info.setRequestId(task.getRequest_id());
                                    info.setTaskId(task.getTask_id());
                                    info.setCreateTime(new Date());
                                    info.setCreateDate(new Date());
                                    info.setCityId(task.getCity_id());
                                    info.setCityName(task.getCity_name());
                                    info.setKeyword(task.getKeyword());
                                    info.setBuildingId(task.getBuilding_id());
                                    info.setBuildingName(task.getBuilding_name());
                                    info.setDate(date);
                                    info.setDay(day);
//                                    if (!"1".equals(options.getJSONObject(j).getString("typeOrder"))) {
//                                        info.setType(options.getJSONObject(j).getString("typeName"));
//                                    } else {
//                                        info.setType("早餐福利");
//                                    }
                                    info.setType(getType(cats, options.getJSONObject(j).getString("id"), options.getJSONObject(j).getString("newCategoryId")));
                                    info.setFoodName(options.getJSONObject(j).getString("name"));
                                    info.setFoodId(options.getJSONObject(j).getString("id"));
                                    info.setShopName(options.getJSONObject(j).getString("restaurantName"));
                                    info.setDescription(options.getJSONObject(j).getString("desc"));
                                    info.setDiscountedPrice(options.getJSONObject(j).getString("discounted"));
                                    info.setOriginalPrice(options.getJSONObject(j).getString("price"));
                                    info.setRating(options.getJSONObject(j).getString("rating"));
                                    info.setSales(options.getJSONObject(j).getString("saleNum"));
                                    info.setRecommendNum(options.getJSONObject(j).getString("recommend"));
                                    info.setStockNum(options.getJSONObject(j).getString("stockNum"));
                                    if (Boolean.parseBoolean(options.getJSONObject(j).getString("isNew"))) {
                                        info.setIsNew("新品");
                                    }
                                    info.setUniqueId(MD5Util.getMD5(task.getRequest_id() + task.getTask_id() + task.getBuilding_id() + options.getJSONObject(j).getString("id")));
                                    infos.add(info);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    logger.error("", e);
                }
            }
        }
        return infos;
    }

    public String getType(JSONArray cats, String food_id, String cat_id) {
        String type = "";
        if (StringUtils.isNotEmpty(food_id) && StringUtils.isNotEmpty(cat_id) && !cats.isEmpty()) {
            for (int i = 0; i < cats.length(); i++) {
                try {
                    JSONArray ids = cats.getJSONObject(i).getJSONArray("dishIds");
                    if (!ids.isEmpty()) {
                        for (int j = 0; j < ids.length(); j++) {
                            if (food_id.equals(ids.getString(j))) {
                                type = cats.getJSONObject(i).getString("name") + "/";
                                break;
                            }
                        }
                    }
                    if (cat_id.equals(cats.getJSONObject(i).getString("id"))) {
                        type += cats.getJSONObject(i).getString("name");
                        break;
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
            }
        }
//        logger.info(type);
        return type;
    }
}
