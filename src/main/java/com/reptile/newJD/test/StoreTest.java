package com.reptile.newJD.test;

import com.bds.base.http.HttpsBase;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StoreTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		String lng = "116.34977";
		String lat = "39.93078";
//		String lng = "";
//		String lat = "";
		String storeId = "11648794";
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
		// System.out.println(html);

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

		System.out.println("store_name");
		System.out.println(freightAndtime);
		System.out.println(freight_money);
		System.out.println(announcement); // 公告
		System.out.println(store_star);
		System.out.println(comment_num);
		System.out.println(comment_user_name);
		System.out.println(comment_user_star);
		System.out.println(comment_user_content);
		System.out.println(comment_user_label);
		System.out.println(comment_user_datetime);
		System.out.println(store_goods_total);
		System.out.println(monthSale);
		System.out.println(store_follownum);
		System.out.println(store_shophours);
		System.out.println(store_address);
		System.out.println(store_phone);
		System.out.println(store_qualification);
		System.out.println(store_report);
		System.out.println(overtime_payment);
		System.out.println(store_report_reward);
		System.out.println(coupons); // 优惠卷

		// 生成评论任务 分页起来
		String body2 = URLEncoder.encode("{\"storeId\":\"11657777\""
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
		for (int i = 1; i <= commentPageNum; i++) {
			String body_temp = "{\"storeId\":\"11657777\""
					+ ",\"type\":1,\"qual\":0" + ",\"page\":" + i
					+ ",\"pageSize\":10}";
			System.out.println(body_temp);
		}

		// 产品分页
		JSONArray cateArray = allObject.getJSONObject("result").getJSONArray("cateList");
		for (int i = 0; i < cateArray.length(); i++) {
			System.out.println(i+"#######################");
			JSONObject cateObject = cateArray.getJSONObject(i);
			getPageURL(cateObject,storeId,lng,lat);
		}
	}

	// 递归分页
	public String getPageURL(JSONObject cateObject, String store_id,
			String lng, String lat) throws JSONException {

		String category_name = "";
		int product_count = 0;
		String catId = "";
		String page_url = "";
		String father_name = "";
		String father_catId = "";
		int father_count = 0;
		String father_promotLabel = "";

		father_promotLabel = cateObject.getString("promotLabel");
		father_name = cateObject.getString("title");
		father_count = Integer.valueOf(cateObject.getString("productCount"));
		father_catId = cateObject.getString("catId");
		JSONArray childCategoryList = cateObject
				.getJSONArray("childCategoryList");
		if (childCategoryList.length() >= 1) {

			for (int j = 0; j < childCategoryList.length(); j++) {
				JSONObject childCategory = childCategoryList.getJSONObject(j);
				category_name = childCategory.getString("title");
				getPageURL(childCategory, store_id, lng, lat);

			}
		} else {

			category_name = father_name;
			product_count = father_count;
			catId = father_catId;
			int pageSize = 10;
			int pages;
			// 构建URL
			String timeTag = String.valueOf(new Date().getTime()) + "555";
			page_url = "https://daojia.jd.com/client?functionId=productsearch%2Fsearch&body=%7B%22key%22%3A%22%22%2C%22catId%22%3A%22"
					+ catId
					+ "%22%2C%22storeId%22%3A%22"
					+ store_id
					+ "%22%2C%22sortType%22%3A1%2C%22page%22%3A"
					+ "1"
					+ "%2C%22pageSize%22%3A10%2C%22cartUuid%22%3A%22%22%2C%22promotLable%22%3A%22%22%2C%22timeTag%22%3A"
					+ timeTag
					+ "%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng="
					+ lng + "&lat=" + lat;

			if (product_count % pageSize == 0 && product_count != 0) {
				pages = (product_count) / pageSize;
			} else {
				pages = (product_count) / pageSize + 1;
			}

			for (int i = 1; i <= pages; i++) {
				if (i == 1) {

				} else {
					page_url = page_url.replace(
							"page%22%3A" + String.valueOf(i - 1), "page%22%3A"
									+ String.valueOf(i));

				}
				System.out.println(page_url);
				System.out.println("father_promotLabel::" + father_promotLabel);
				// System.out.println(categoryUrl1);
			}

			System.out.println(category_name);
			System.out.println(catId);
			System.out.println(product_count);
			System.out.println(pages);

			// System.out.println(page_url);

		}
		return null;
	}

}
