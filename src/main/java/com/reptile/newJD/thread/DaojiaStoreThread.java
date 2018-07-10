package com.reptile.newJD.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpsBase;
import com.bds.base.util.Constant;
import com.reptile.newJD.job.JDDaoJiaCommentJob;
import com.reptile.newJD.job.JDDaoJiaProductUrlJob;
import com.reptile.newJD.job.JDDaojiaStoreSimpleJob;
import com.reptile.newJD.model.JDDaoJiaStoreInfo;
import com.reptile.util.EmojiFilterUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 店铺详情的抓取 包括评论任务的生成 和 产品任务的生成
 * @date 2018年1月5日 下午2:26:13
 */
public class DaojiaStoreThread extends Thread {
	
	private static Logger log = Logger.getLogger(DaojiaStoreThread.class);
	private Dao mysqlDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode)
			.getMysqlDao();
	
	JDDaojiaStoreSimpleJob task;
	
	public DaojiaStoreThread(JDDaojiaStoreSimpleJob task) {
		this.task = task;
	}
	
	@Override
	public void run() {
		try {
			log.info("========= newDaoJia 开始店铺详情的抓取 并分页 ==============");
			mysqlDao.update(JDDaojiaStoreSimpleJob.class,
					Chain.make("status", 1), Cnd.where("id", "=", task.getId()));

			crawl();

			mysqlDao.update(JDDaojiaStoreSimpleJob.class,
					Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
			log.info("店铺详情的抓取 并分页任务完毕,website:newDaoJia, task_id:" + task.getId());
		} catch (Exception e) {
			mysqlDao.update(JDDaojiaStoreSimpleJob.class,
					Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			//log.error("店铺详情的抓取 并分页出错", e);
		}
	}
	
	/**
	 * 具体抓取逻辑
	 * @throws Exception
	 */
	void crawl() throws Exception{
		TaskToBi tobi = BIDataSource.getBiDataSource("jddaojia", task.getProject_code(), task.getDown_type());
		
		String lng = task.getLng();
		String lat = task.getLat();
		String storeId = task.getStore_id();
		// 店铺详情页面
		String body = URLEncoder.encode("{" + "\"storeId\":\"" + storeId + "\""
				+ ",\"skuId\":\"\",\"activityId\":\"\""
				+ ",\"promotionType\":\"\"" + ",\"longitude\":" + lng
				+ ",\"latitude\":" + lat + "}", "utf-8");

		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "daojia.jd.com");
		header.put("Connection", "keep-alive");
		header.put("Referer", "https://daojia.jd.com/html/index.html");
		header.put("Accept", "application/json");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put(
				"User-Agent",
				"Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");

		String url = String.format("https://daojia.jd.com/client?"
				+ "functionId=store/storeDetailV220" + "&appVersion=4.9.5"
				+ "&appName=paidaojia" + "&platCode=H5" + "&body=%s", body);

		String html = HttpsBase.get(url, "utf-8", header).getResult();

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
		// JDDaoJiaStoreInfo storeInfoPojo = new JDDaoJiaStoreInfo();

		JSONObject storeInfo = allObject.getJSONObject("result").getJSONObject(
				"storeInfo");

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
			JSONArray expectArrivedTips = storeInfo
					.getJSONArray("expectArrivedTips");
			for (int j = 0; j < expectArrivedTips.length(); j++) {
				announcement = announcement
						+ expectArrivedTips.getJSONObject(j).getString("msg")
						+ "&&&&";
			}
		}

		// storeInfo.isNull("expectArrivedTips");

		store_star = storeInfo.getString("scoreAvg");
		store_goods_total = storeInfo.getString("inSaleNum");
		monthSale = storeInfo.getString("monthSaleNum");
		store_follownum = storeInfo.getString("followNo");
		store_shophours = storeInfo.getJSONArray("serviceTimes")
				.getJSONObject(0).getString("startTime")
				+ "-"
				+ storeInfo.getJSONArray("serviceTimes").getJSONObject(0)
						.getString("endTime");
		store_address = storeInfo.getString("storeAddress");
		store_phone = storeInfo.getString("storeTel");
		store_qualification = storeInfo.getString("storeCertificateUrl");
		if (allObject.getJSONObject("result").getJSONArray("tagList")
				.toString().contains("超时赔付")) {
			overtime_payment = "超时赔付";
		} else {
			overtime_payment = "无";
		}
		// 获取优惠券信息
		String coupons = "";
		if (!allObject.getJSONObject("result").isNull("coupons")) {

			JSONArray couponsArray = allObject.getJSONObject("result")
					.getJSONArray("coupons");
			for (int i = 0; i < couponsArray.length(); i++) {
				JSONObject couponsObject = couponsArray.getJSONObject(i);
				coupons = coupons + "优惠卷金额："
						+ couponsObject.getString("amount")
						+ couponsObject.getString("amountUnit") + ",使用要求："
						+ couponsObject.getString("limitRule") + ",领取要求："
						+ couponsObject.getString("couponTip") + ",有效期:"
						+ couponsObject.getString("avilableDate") + "	";

			}
		}

		JSONObject storeCommentVO = allObject.getJSONObject("result")
				.getJSONObject("storeCommentVO");
		comment_num = storeCommentVO.getString("totalCount");
		comment_user_name = storeCommentVO.getString("buyerShowName");
		comment_user_star = storeCommentVO.getString("score4");
		comment_user_content = storeCommentVO.getString("score4Content");
		comment_user_label = storeCommentVO.getString("tagInfoItemList");
		comment_user_datetime = storeCommentVO.getString("createTime");
		
		// 插入店铺详情到BI库
		JDDaoJiaStoreInfo storeInfoBi = new JDDaoJiaStoreInfo();
		storeInfoBi.setAddress(task.getAddress());
		storeInfoBi.setAddressKey(task.getAddressKey());
		storeInfoBi.setAnnouncement(announcement);
		storeInfoBi.setCity_name(task.getCity_name());
		storeInfoBi.setComment_num(comment_num);
		storeInfoBi.setComment_user_content(comment_user_content);
		storeInfoBi.setComment_user_datetime(comment_user_datetime);
		storeInfoBi.setComment_user_label(comment_user_label);
		storeInfoBi.setComment_user_name(comment_user_name);
		storeInfoBi.setComment_user_star(comment_user_star);
		storeInfoBi.setCoupons(coupons);
		storeInfoBi.setCreate_time(new Date());
		storeInfoBi.setDown_type(task.getDown_type());
		storeInfoBi.setFreight_money(freight_money);
		storeInfoBi.setFreight_start(task.getFreight_start());
		storeInfoBi.setFreightAndtime(freightAndtime);
		storeInfoBi.setMonthSale(monthSale);
		storeInfoBi.setOvertime_payment(overtime_payment);
		storeInfoBi.setProject_code(task.getProject_code());
		storeInfoBi.setRequest_id(task.getRequest_id());
		storeInfoBi.setStore_address(store_address);
		storeInfoBi.setStore_follownum(store_follownum);
		storeInfoBi.setStore_goods_total(store_goods_total);
		storeInfoBi.setStore_id(task.getStore_id());
		storeInfoBi.setStore_name(task.getStore_name());
		storeInfoBi.setStore_phone(store_phone);
		storeInfoBi.setStore_qualification(store_qualification);
		storeInfoBi.setStore_report(store_report);
		storeInfoBi.setStore_shophours(store_shophours);
		storeInfoBi.setStore_star(store_star);
		storeInfoBi.setStore_url("https://daojia.jd.com/html/index.html#storeHome/storeId:"+storeId);
		storeInfoBi.setTask_id(task.getTask_id());
		storeInfoBi.setWebsite(task.getWebsite());
		storeInfoBi.setStore_report_reward(store_report_reward);
		
		try {
			tobi.getBiDao().insert(storeInfoBi);
			
		} catch (Exception e1) {
			//log.error("插入店铺详情出错", e1);
		}

		log.info("店铺info插入成功");
		
		// ============= 生成评论任务 分页起来 =========================
		String body2 = URLEncoder.encode("{\"storeId\":\""+storeId+"\""
				+ ",\"type\":1,\"qual\":0" + ",\"page\":1"
				+ ",\"pageSize\":10}", "utf-8");

		Map<String, String> header2 = new HashMap<String, String>();
		header.put("Host", "daojia.jd.com");
		header.put("Connection", "keep-alive");
		header.put("Referer", "https://daojia.jd.com/html/index.html");
		header.put("Accept", "application/json");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put(
				"User-Agent",
				"Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");

		String url2 = String.format("https://daojia.jd.com/client?"
				+ "functionId=comment/queryDetail" + "&appVersion=4.9.5"
				+ "&appName=paidaojia" + "&platCode=H5" + "&body=%s", body2);

		String html2 = HttpsBase.get(url2, "utf-8", header2).getResult();
		JSONObject jsonObject = new JSONObject(html2);
		String totalCount = jsonObject.getJSONObject("result")
				.getJSONObject("config").getString("totalCount");
		int commentPageNum = 0;
		// 判断页数
		if (Integer.parseInt(totalCount) % 10 == 0) {
			commentPageNum = Integer.parseInt(totalCount) / 10;
		} else {
			commentPageNum = Integer.parseInt(totalCount) / 10 + 1;
		}
		
		// 评论标签拼接
		JSONObject sta = jsonObject.getJSONObject("result").getJSONObject("sta");
		// 获取第一行标签 (第一行的好评 中评 差评 是不显示的)
		String comment_tag = "好评("+sta.getString("lc1")+")   " +"中评("+sta.getString("lc2")+")   "+ "差评("+sta.getString("lc3")+")   " ;
		// 拼接标签到一起
		JSONArray bdTag = sta.getJSONArray("bdTag");
		try {
			for (int i = 0; i < bdTag.length(); i++) {
				comment_tag = comment_tag + bdTag.getJSONObject(i).getString("tagName")+"("+bdTag.getJSONObject(i).getString("tagCt") +")   "  ;
			}
		} catch (Exception e) {
			
		}
		JSONArray gdTag = sta.getJSONArray("gdTag");
		try {
			for (int i = 0; i < gdTag.length(); i++) {
				comment_tag = comment_tag + gdTag.getJSONObject(i).getString("tagName")+"("+gdTag.getJSONObject(i).getString("tagCt") +")   "  ;
			}
		} catch (Exception e) {
		}
		
		if (commentPageNum >= 200) {
			commentPageNum = 200;
		}
		for (int i = 1; i <= commentPageNum; i++) {
			String body_temp = "{\"storeId\":\""+storeId+"\""
					+ ",\"type\":1,\"qual\":0" + ",\"page\":" + i
					+ ",\"pageSize\":10}";
			
			JDDaoJiaCommentJob commentJob = new JDDaoJiaCommentJob();
			commentJob.setAddress(task.getAddress());
			commentJob.setAddressKey(task.getAddressKey());
			commentJob.setCategory(task.getCategory());
			commentJob.setCity_name(task.getCity_name());
			commentJob.setBody(body_temp);
			commentJob.setComment_num(comment_num);
			commentJob.setComment_tag(comment_tag);
			commentJob.setCreate_time(new Date());
			commentJob.setDown_type(task.getDown_type());
			commentJob.setNode(task.getNode());
			commentJob.setPage(i);
			commentJob.setPage_url(url2);
			commentJob.setProject_code(task.getProject_code());
			commentJob.setRequest_id(task.getRequest_id());
			commentJob.setStatus(0);
			commentJob.setStore_id(storeId);
			commentJob.setStore_name(task.getStore_name());
			commentJob.setStore_star(store_star);
			commentJob.setTask_id(task.getTask_id());
			commentJob.setTotal(commentPageNum);
			commentJob.setWebsite(task.getWebsite());
			
			try {
				mysqlDao.insert(commentJob);
			} catch (Exception e) {
				//log.error("插入评论任务出错", e);
			}
			log.info("comment分页"+i);
		}
		log.info("comment分页任务插入成功");
		
		
		
		

		// ============================= 生成产品分页任务 ===========================
		JSONArray cateArray = allObject.getJSONObject("result").getJSONArray("cateList");
		for (int i = 0; i < cateArray.length(); i++) {
			JSONObject cateObject = cateArray.getJSONObject(i);
			getPageURL(cateObject, task, mysqlDao, announcement, freight_money, freightAndtime, "", cateObject.getString("title"));
		}
		
	}
	
	// 递归分页 (eli.zhou 写的)
	void getPageURL(JSONObject cateObject, JDDaojiaStoreSimpleJob task2, Dao tDao, String announcement, String freight_money, String freightAndtime, String category_name1, String const_cat) throws JSONException {
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
                getPageURL(childCategory, task2, tDao, announcement, freight_money, freightAndtime, category_name, const_cat);

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
                        + catId + "%22%2C%22storeId%22%3A%22" + task2.getStore_id() + "%22%2C%22sortType%22%3A1%2C%22page%22%3A" + "1" + "%2C%22pageSize%22%3A10%2C%22cartUuid%22%3A%22%22%2C%22promotLable%22%3A%22" + father_promotLabel + "%22%2C%22timeTag%22%3A"
                        + timeTag + "%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=" + task2.getLng() + "&lat=" + task2.getLat();
            } else {
                page_url = "https://daojia.jd.com/client?functionId=productsearch%2Fsearch&body=%7B%22key%22%3A%22%22%2C%22catId%22%3A%22"
                        + catId + "%22%2C%22storeId%22%3A%22" + task2.getStore_id() + "%22%2C%22sortType%22%3A1%2C%22page%22%3A" + "1" + "%2C%22pageSize%22%3A10%2C%22cartUuid%22%3A%22%22%2C%22promotLable%22%3A%22%22%2C%22timeTag%22%3A"
                        + timeTag + "%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=" + task2.getLng() + "&lat=" + task2.getLat();
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
                productUrlJob.setAddress(task2.getAddress());
                productUrlJob.setAddressKey(task2.getAddressKey());
                productUrlJob.setCatId(catId);
                productUrlJob.setCity_name(task2.getCity_name());
                productUrlJob.setCreate_time(new Date());
                productUrlJob.setDown_type(task2.getDown_type());
                productUrlJob.setLat(task2.getLat());
                productUrlJob.setLng(task2.getLng());
                productUrlJob.setNode(task2.getNode());
                productUrlJob.setWebsite(task2.getWebsite());
                productUrlJob.setTask_id(task2.getTask_id());
                productUrlJob.setStore_name(task2.getStore_name());
                productUrlJob.setStore_id(task2.getStore_id());
                productUrlJob.setStatus(0);
                productUrlJob.setRequest_id(task2.getRequest_id());
                productUrlJob.setProject_code(task2.getProject_code());
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
                } catch (Exception e) {
                    //log.error(e);
                }

            }
            log.info("分类分页完成，分类名为：" + category_name);

        }

    }

}
