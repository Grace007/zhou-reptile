package com.reptile.newJD.test;

import com.bds.base.http.HttpsBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GenarateSortTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		
		// 获取地理位置信息 根据关键词
		String city = "北京市";
		String keyword = "aa";
		String body = URLEncoder.encode("{\"region\":\""+city+"\",\"key\":\""+keyword+"\"}","utf-8");
		
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "daojia.jd.com");
		header.put("Connection", "keep-alive");
		header.put("Referer", "https://daojia.jd.com/html/index.html");
		header.put("Accept", "application/json");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");
		
		String url = String.format(
				"https://daojia.jd.com/client?"
				+ "functionId=address/search"
				+ "&appVersion=4.9.0"
				+ "&appName=paidaojia"
				+ "&platCode=H5"
				+ "&body=%s"
				, body);
		
		String html = HttpsBase.get(url, "utf-8", header).getResult();
		JSONObject resultObject = new JSONObject(html);
		JSONArray object = (JSONArray) resultObject.get("result");
		// 获取第一个参数
		JSONObject one = (JSONObject) object.get(0);
		String cityId = one.getString("cityCode");
		String adcode = one.getString("adcode");
		String title = one.getString("title");
		JSONObject location = (JSONObject) one.get("location");
		String lng = location.getString("lng");
		String lat = location.getString("lat");
		String address = one.getString("address");
		
		System.out.println(one);
		System.out.println(cityId);
		System.out.println(lng+" "+ lat);
		System.out.println(address);
		System.out.println(adcode);
		
		// 获取分类列表
		String body2 = URLEncoder.encode("{\"longitude\":"+lng+","
						+ "\"latitude\":"+lat+","
						+ "\"cityId\":"+cityId+"}","utf-8");
		String url2 = String.format(
				"https://daojia.jd.com/client?"
				+ "functionId=homeSearch/tabCateList"
				+ "&appVersion=4.9.0"
				+ "&appName=paidaojia"
				+ "&platCode=H5"
				+ "&body=%s"
				, body2);
		
		
		Map<String, String> header2 = new HashMap<String, String>();
		header2.put("Host", "daojia.jd.com");
		header2.put("Connection", "keep-alive");
		header2.put("Referer", "https://daojia.jd.com/html/index.html");
		header2.put("Accept", "application/json");
		header2.put("Accept-Encoding", "gzip, deflate, br");
		header2.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
		String html2 = HttpsBase.get(url2, "utf-8", header2).getResult();
		JSONObject resultObject2 = new JSONObject(html2);
		JSONArray jsonArr = resultObject2.getJSONObject("result").getJSONArray("firstTabCate");
	
		
		for (int i = 0; i < jsonArr.length(); i++) {
			 String cartIdss = "";
			 JSONObject level1Json = jsonArr.getJSONObject(i);  // 一级分类
			 System.out.println(level1Json.getString("name"));
			 
			 JSONArray level2Array = level1Json.getJSONArray("groupList"); // 二级分类
			 for (int j = 0; j < level2Array.length(); j++) {
				 JSONArray level3json = level2Array.getJSONObject(j).getJSONArray("tabCateList"); // 三级分类
				 for (int k = 0; k < level3json.length(); k++) {
					String catIds = level3json.getJSONObject(k).getString("catIds");
//					System.out.println(catIds);
						cartIdss += catIds + "|";
				}
			 }
			 cartIdss = cartIdss.replace("\"categoryName\":null,", "");
			 System.out.println(cartIdss);
			 System.out.println("===========================");
			 break;
		}
		
	}

}
