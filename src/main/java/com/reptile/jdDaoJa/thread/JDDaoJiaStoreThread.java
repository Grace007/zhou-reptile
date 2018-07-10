package com.reptile.jdDaoJa.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.jdDaoJa.job.JDDaoJiaCommentJob;
import com.reptile.jdDaoJa.job.JDDaoJiaProductUrlJob;
import com.reptile.jdDaoJa.job.JDDaoJiaStoreJob;
import com.reptile.jdDaoJa.model.JDDaoJiaStoreInfo;
import com.reptile.util.EmojiFilterUtils;
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

public class JDDaoJiaStoreThread extends Thread {
    private static Logger logger = Logger.getLogger(JDDaoJiaStoreThread.class);
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private JDDaoJiaStoreJob task;

    public JDDaoJiaStoreThread(JDDaoJiaStoreJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            tDao.update(JDDaoJiaStoreJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
            logger.info("开始抓取storeinfo，店铺名为" + task.getStore_name());
            TaskToBi tobi = BIDataSource.getBiDataSource("jddaojia", task.getProject_code(), task.getDown_type());
            Dao biDao = tobi.getBiDao();
            Map<String, String> header = new HashMap<String, String>();
            header.put("Host", "daojia.jd.com");
            header.put("Accept", "application/json");
            header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
            String html = HttpBase.get(task.getStore_url(), "utf-8", header).getResult();
            JSONObject allObject = new JSONObject(html);

            String freightAndtime;
            String freight_money;
            String announcement = "";
            String store_star;
            String comment_num;
            String comment_user_name;
            String comment_user_star;
            String comment_user_content;
            String comment_user_label;
            String comment_user_datetime;
            String store_goods_total;
            String monthSale;
            String store_follownum;
            String store_shophours;
            String store_address;
            String store_phone;
            String store_qualification;
            String store_report = "举报商家得红包";
            String overtime_payment;
            String store_report_reward = "最高奖100元";
            JDDaoJiaStoreInfo storeInfoPojo = new JDDaoJiaStoreInfo();

            JSONObject storeInfo = allObject.getJSONObject("result").getJSONObject("storeInfo");

            if (StringUtils.equals("9966", storeInfo.getString("carrierNo"))) {
                freightAndtime = "达达专送-";
            } else {
                freightAndtime = "商家自送-";
            }
            freightAndtime = freightAndtime + storeInfo.getString("deliveryFirst");
            freight_money = storeInfo.getString("freightWords");

            if (storeInfo.isNull("expectArrivedTips")) {
                announcement = "";
            } else {
                JSONArray expectArrivedTips = storeInfo.getJSONArray("expectArrivedTips");
                for (int j = 0; j < expectArrivedTips.length(); j++) {
                    announcement = announcement + expectArrivedTips.getJSONObject(j).getString("msg") + "&&&&";
                }
            }

            //storeInfo.isNull("expectArrivedTips");

            store_star = storeInfo.getString("scoreAvg");
            store_goods_total = storeInfo.getString("inSaleNum");
            monthSale = storeInfo.getString("monthSaleNum");
            store_follownum = storeInfo.getString("followNo");
            store_shophours = storeInfo.getJSONArray("serviceTimes").getJSONObject(0).getString("startTime") + "-"
                    + storeInfo.getJSONArray("serviceTimes").getJSONObject(0).getString("endTime");
            store_address = storeInfo.getString("storeAddress");
            store_phone = storeInfo.getString("storeTel");
            store_qualification = storeInfo.getString("storeCertificateUrl");
            if (allObject.getJSONObject("result").getJSONArray("tagList").toString().contains("超时赔付")) {
                overtime_payment = "超时赔付";
            } else {
                overtime_payment = "无";
            }
            //获取优惠券信息
            String coupons = "";
            if (!allObject.getJSONObject("result").isNull("coupons")) {
				
            JSONArray couponsArray = allObject.getJSONObject("result").getJSONArray("coupons");
            for (int i = 0; i < couponsArray.length(); i++) {
                JSONObject couponsObject = couponsArray.getJSONObject(i);
                coupons = coupons + "优惠卷金额：" + couponsObject.getString("amount") + couponsObject.getString("amountUnit") + ",使用要求："
                        + couponsObject.getString("limitRule") + ",领取要求：" + couponsObject.getString("couponTip") + ",有效期:" +
                        couponsObject.getString("avilableDate") + "	";

            }
            }

            JSONObject storeCommentVO = allObject.getJSONObject("result").getJSONObject("storeCommentVO");
            comment_num = storeCommentVO.getString("totalCount");
            comment_user_name = storeCommentVO.getString("buyerShowName");
            comment_user_star = storeCommentVO.getString("score4");
            comment_user_content = storeCommentVO.getString("score4Content");
            comment_user_label = storeCommentVO.getString("tagInfoItemList");
            comment_user_datetime = storeCommentVO.getString("createTime");

            storeInfoPojo.setAddress(task.getAddress());
            storeInfoPojo.setAddressKey(task.getAddressKey());
            storeInfoPojo.setAnnouncement(announcement);
            storeInfoPojo.setCity_name(task.getCity_name());
            storeInfoPojo.setComment_num(comment_num);
            storeInfoPojo.setComment_user_content(comment_user_content);
            storeInfoPojo.setComment_user_label(comment_user_label);
            storeInfoPojo.setComment_user_datetime(comment_user_datetime);
            storeInfoPojo.setComment_user_name(comment_user_name);
            storeInfoPojo.setComment_user_star(comment_user_star);
            storeInfoPojo.setCreate_time(new Date());
            storeInfoPojo.setDown_type(task.getDown_type());
            storeInfoPojo.setFreight_money(freight_money);
            storeInfoPojo.setWebsite(task.getWebsite());
            storeInfoPojo.setTask_id(task.getTask_id());
            storeInfoPojo.setStore_star(store_star);
            storeInfoPojo.setStore_shophours(store_shophours);
            storeInfoPojo.setStore_report_reward(store_report_reward);
            storeInfoPojo.setStore_report(store_report_reward);
            storeInfoPojo.setStore_qualification(store_qualification);
            storeInfoPojo.setStore_phone(store_phone);
            storeInfoPojo.setStore_name(task.getStore_name());
            storeInfoPojo.setStore_id(task.getStore_id());
            storeInfoPojo.setStore_goods_total(store_goods_total);
            storeInfoPojo.setStore_follownum(store_follownum);
            storeInfoPojo.setStore_address(store_address);
            storeInfoPojo.setRequest_id(task.getRequest_id());
            storeInfoPojo.setProject_code(task.getProject_code());
            storeInfoPojo.setFreightAndtime(freightAndtime);
            storeInfoPojo.setMonthSale(monthSale);
            storeInfoPojo.setOvertime_payment(overtime_payment);
            storeInfoPojo.setCoupons(coupons);
            storeInfoPojo.setStore_url(task.getUrl());
            storeInfoPojo.setFreight_start(task.getFreight_start());

            try {
                biDao.insert(storeInfoPojo);

            } catch (Exception e) {
                //logger.error("数据重复====storeinfo插入BI失败，id=" + task.getId());
            }

            //logger.info("====storeinfo插入BI成功，id=" + task.getId() + "现在生成店铺评论任务");

            String comment_url = "https://daojia.jd.com/client?functionId=comment%2FqueryDetail&body=%7B%22storeId%22%3A%22" + task.getStore_id()
                    + "%22%2C%22type%22%3A%221%22%2C%22qual%22%3A0%2C%22page%22%3A" + "1"
                    + "%2C%22pageSize%22%3A10%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5";

            html = HttpBase.get(comment_url, "utf-8", header).getResult();
            JSONObject allObject1 = new JSONObject(html);
            int pageSize, totalCount, pages;
            pageSize = allObject1.getJSONObject("result").getJSONObject("config").getInt("pageSize");
            totalCount = allObject1.getJSONObject("result").getJSONObject("config").getInt("totalCount");

            if (totalCount % pageSize == 0 && totalCount != 0) {
                pages = (totalCount) / pageSize;
            } else {
                pages = (totalCount) / pageSize + 1;
            }
            JDDaoJiaCommentJob commentJob = new JDDaoJiaCommentJob();

            for (int i = 1; i <= pages; i++) {
                if (i == 1) {

                } else {
                    comment_url = comment_url.replace("page%22%3A" + String.valueOf(i - 1), "page%22%3A" + String.valueOf(i));
                }
                commentJob.setAddress(task.getAddress());
                commentJob.setAddressKey(task.getAddressKey());
                commentJob.setCategory(task.getCategory());
                commentJob.setCity_name(task.getCity_name());
                commentJob.setCreate_time(new Date());
                commentJob.setDown_type(task.getDown_type());
                commentJob.setNode(task.getNode());
                commentJob.setPage(i);
                commentJob.setPage_url(comment_url);
                commentJob.setProject_code(task.getProject_code());
                commentJob.setWebsite(task.getWebsite());
                commentJob.setTotal(totalCount);
                commentJob.setTask_id(task.getTask_id());
                commentJob.setStore_name(task.getStore_name());
                commentJob.setStore_id(task.getStore_id());
                commentJob.setStatus(0);
                commentJob.setRequest_id(task.getRequest_id());

                try {
                    tDao.insert(commentJob);
                    logger.info("======store生成comment任务成功==store_name:" + task.getStore_name());

                } catch (Exception e) {
                   // logger.error("数据重复======store生成comment任务失败==store_name:" + task.getStore_name());
                }


            }

            logger.info("现在生成product任务，store_name=" + task.getStore_name());

            JSONArray cateArray = allObject.getJSONObject("result").getJSONArray("cateList");
            for (int i = 0; i < cateArray.length(); i++) {
                System.out.println(i + "#######################");
                JSONObject cateObject = cateArray.getJSONObject(i);
                getPageURL(cateObject, task, tDao, announcement, freight_money, freightAndtime, "", cateObject.getString("title"));
            }

            tDao.update(JDDaoJiaStoreJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));


        } catch (Exception e) {
            tDao.update(JDDaoJiaStoreJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            logger.error("=============当前info任务失败！==========" + task.getCity_name() + "  " + task.getAddressKey() + "=====",e);
        }
    }

    public void getPageURL(JSONObject cateObject, JDDaoJiaStoreJob task, Dao tDao, String announcement, String freight_money, String freightAndtime, String category_name1, String const_cat) throws JSONException {
        String father_promotLabel = "";
        String category_name = "";
        int product_count = 0;
        String catId = "";
        String page_url = "";
        String father_name = "";
        String father_catId = "";
        int father_count = 0;

        father_name = const_cat;
        father_count = Integer.valueOf(cateObject.getString("productCount"));
        father_catId = cateObject.getString("catId");
        father_promotLabel = cateObject.getString("promotLabel");


        JSONArray childCategoryList = cateObject.getJSONArray("childCategoryList");
        if (childCategoryList.length() >= 1) {

            for (int j = 0; j < childCategoryList.length(); j++) {
                JSONObject childCategory = childCategoryList.getJSONObject(j);
                category_name = childCategory.getString("title");
                category_name = "-" + category_name;
                getPageURL(childCategory, task, tDao, announcement, freight_money, freightAndtime, category_name, const_cat);

            }
        } else {

            category_name = father_name + category_name1;
            product_count = father_count;
            catId = father_catId;
            int pageSize = 10;
            int pages;
            //构建URL
            String timeTag = String.valueOf(new Date().getTime()) + "555";

            if (cateObject.getBoolean("ispromotcat")) {
                page_url = "https://daojia.jd.com/client?functionId=productsearch%2Fsearch&body=%7B%22key%22%3A%22%22%2C%22catId%22%3A%22"
                        + catId + "%22%2C%22storeId%22%3A%22" + task.getStore_id() + "%22%2C%22sortType%22%3A1%2C%22page%22%3A" + "1" + "%2C%22pageSize%22%3A10%2C%22cartUuid%22%3A%22%22%2C%22promotLable%22%3A%22" + father_promotLabel + "%22%2C%22timeTag%22%3A"
                        + timeTag + "%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=" + task.getLng() + "&lat=" + task.getLat();
            } else {
                page_url = "https://daojia.jd.com/client?functionId=productsearch%2Fsearch&body=%7B%22key%22%3A%22%22%2C%22catId%22%3A%22"
                        + catId + "%22%2C%22storeId%22%3A%22" + task.getStore_id() + "%22%2C%22sortType%22%3A1%2C%22page%22%3A" + "1" + "%2C%22pageSize%22%3A10%2C%22cartUuid%22%3A%22%22%2C%22promotLable%22%3A%22%22%2C%22timeTag%22%3A"
                        + timeTag + "%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=" + task.getLng() + "&lat=" + task.getLat();
            }


            if (product_count % pageSize == 0 && product_count != 0) {
                pages = (product_count) / pageSize;
            } else {
                pages = (product_count) / pageSize + 1;
            }
            for (int i = 1; i <= pages; i++) {
                if (i == 1) {
                } else {
                    page_url = page_url.replace("page%22%3A" + String.valueOf(i - 1), "page%22%3A" + String.valueOf(i));
                }
                //System.out.println(page_url);
                //System.out.println(categoryUrl1);
                JDDaoJiaProductUrlJob productUrlJob = new JDDaoJiaProductUrlJob();
                productUrlJob.setAddress(task.getAddress());
                productUrlJob.setAddressKey(task.getAddressKey());
                productUrlJob.setCatId(catId);
                productUrlJob.setCity_name(task.getCity_name());
                productUrlJob.setCreate_time(new Date());
                productUrlJob.setDown_type(task.getDown_type());
                productUrlJob.setLat(task.getLat());
                productUrlJob.setLng(task.getLng());
                productUrlJob.setNode(task.getNode());
                productUrlJob.setWebsite(task.getWebsite());
                productUrlJob.setTask_id(task.getTask_id());
                productUrlJob.setStore_name(task.getStore_name());
                productUrlJob.setStore_id(task.getStore_id());
                productUrlJob.setStatus(0);
                productUrlJob.setRequest_id(task.getRequest_id());
                productUrlJob.setProject_code(task.getProject_code());
                productUrlJob.setProduct_count(product_count);
                productUrlJob.setPage_url(page_url);
                productUrlJob.setPage(i);
                productUrlJob.setFreightAndtime(freightAndtime);
                //productUrlJob.setCategory_name(category_name);
                productUrlJob.setCategory_name(EmojiFilterUtils.removeImoji(category_name));
                productUrlJob.setFreight_money(freight_money);
                productUrlJob.setAnnouncement(announcement);
                try {
                    tDao.insert(productUrlJob);
                    //logger.info("JDDaoJiaProductUrlJob添加到mysql库中成功。页数=" + i);
                } catch (Exception e) {
                   // logger.error("数据重复,JDDaoJiaProductUrlJob添加到mysql库中失败。页数=" + i);
                }

            }
            logger.info("分类分页完成，分类名为：" + category_name);

            //System.out.println(category_name);
            //System.out.println(catId);
            //System.out.println(product_count);
            //System.out.println(pages);
            //System.out.println(page_url);

        }

    }

}


