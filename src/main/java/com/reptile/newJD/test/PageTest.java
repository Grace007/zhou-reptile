package com.reptile.newJD.test;

import com.bds.base.http.HttpsBase;
import com.reptile.util.Re;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PageTest {

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
		String catIds = "[{\"categoryId\":\"20322\",\"level\":\"3\"},{\"categoryId\":\"22410\",\"level\":\"3\"},{\"categoryId\":\"20342\",\"level\":\"3\"}]";
		String areaId = "1";
		
		// 店铺分页
		String body = "{\"longitude\":"+lng+",\"latitude\":"+lat+","
				+ "\"type\":2,\"key\":\"\",\"industryTags\":[]"
				+ ",\"catIds\":"+catIds
				+ ",\"sortType\":1"
				+ ",\"page\":1"
				+ ",\"pageSize\":5,\"storeIds\":[],\"promotLabels\":\"\",\"discountRange\":\"\""
				+ ",\"serviceNo\":1514948628704"
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
		String storeCount = Re.match("\"storeCount\":(\\d+)", html);
		int page;
		if(Integer.parseInt(storeCount)%5==0){
			page = Integer.parseInt(storeCount)/5;
		}else{
			page = Integer.parseInt(storeCount)/5 + 1;
		}
//		System.out.println(storeCount);
		System.out.println(page);
		
		for (int i = 1; i <= page; i++) {
			String body2 = "{\"longitude\":"+lng+",\"latitude\":"+lat+","
					+ "\"type\":2,\"key\":\"\",\"industryTags\":[]"
					+ ",\"catIds\":"+catIds
					+ ",\"sortType\":1"
					+ ",\"page\":"+i
					+ ",\"pageSize\":5,\"storeIds\":[],\"promotLabels\":\"\",\"discountRange\":\"\""
					+ ",\"serviceNo\":1514948628704"
					+ ",\"areaId\":"+areaId+"}";
//			String md5 = MD5Util.getMD5(source);
			System.out.println(body2);
			System.out.println(i);
		}
	}

}
