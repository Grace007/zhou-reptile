package com.reptile.newJD.test;

import com.bds.base.http.HttpsBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SimpleTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		String lng = "116.64319";
		String lat = "39.87345";
		String catIds = "[{\"categoryId\":\"20315\",\"level\":\"3\"},{\"categoryId\":\"20340\",\"level\":\"3\"}]";
		String areaId = "1";
		
		// simple 店铺信息抓取
		String body = "{\"longitude\":"+lng+",\"latitude\":"+lat+","
				+ "\"type\":2,\"key\":\"\",\"industryTags\":[]"
				+ ",\"catIds\":"+catIds
				+ ",\"sortType\":1"
				+ ",\"page\":1"
				+ ",\"pageSize\":5,\"storeIds\":[],\"promotLabels\":\"\",\"discountRange\":\"\""
				+ ",\"serviceNo\":1514965149192"
				+ ",\"areaId\":"+areaId+"}";
		
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "daojia.jd.com");
		header.put("Connection", "keep-alive");
		header.put("Referer", "https://daojia.jd.com/html/index.html");
		header.put("Accept", "application/json");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");
		
		String url = "https://daojia.jd.com/client";
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("functionId", "homeSearch/searchByStorePostV_230");
		data.put("body", body);
		data.put("appVersion", "4.9.5");
		data.put("appName", "paidaojia");
		data.put("platCode", "H5");
		
		String html = HttpsBase.post(url, "utf-8", header, data).getResult();
		JSONObject json = new JSONObject(html);
		JSONArray jsonArray = json.getJSONObject("result").getJSONArray("storeSkuList");
		for (int i = 0; i < jsonArray.length(); i++) {
			String storeName = jsonArray.getJSONObject(i).getJSONObject("store").getString("storeName");
			String carrierNo = jsonArray.getJSONObject(i).getJSONObject("store").getString("carrierNo");
			String freighttime = jsonArray.getJSONObject(i).getJSONObject("store").getString("deliveryFirst");
			if (carrierNo == "9966" || carrierNo.equals("9966")) {
				carrierNo = "达达专送-";
			}else{
				carrierNo = "商家自送-";
			}
			String freightAndtime = carrierNo + freighttime;
			String grade = jsonArray.getJSONObject(i).getJSONObject("store").getString("grade");
			String monSale = jsonArray.getJSONObject(i).getJSONObject("store").getString("monthSale");
			String tag = "";
			JSONArray tags = jsonArray.getJSONObject(i).getJSONObject("store").getJSONArray("tag");
			for (int j = 0; j < tags.length(); j++) {
				tag = tag+tags.getJSONObject(j).getString("words")+"&&&&";
			}
			String sales_promotion = tag;
			String overtime_payment = "";
			try {
				overtime_payment = jsonArray.getJSONObject(i).getJSONObject("store")
						.getJSONArray("tagList").getJSONObject(0).getString("tagName");
			} catch (Exception e) {
				overtime_payment = "无";
			}
			String storeId = jsonArray.getJSONObject(i).getJSONObject("store").getString("storeId");
			
			
			System.out.println(storeName);
			System.out.println(freightAndtime);
			System.out.println(grade);
			System.out.println(monSale);
			System.out.println(sales_promotion);
			System.out.println(overtime_payment);
			System.out.println(storeId);
			System.out.println("==============");
		}
	}

}
