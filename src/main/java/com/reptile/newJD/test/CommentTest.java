package com.reptile.newJD.test;

import com.bds.base.http.HttpsBase;
import com.reptile.util.EmojiFilterUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CommentTest {

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
		String storeId = "11657777";
		
		// 获取店铺评论页面
		String body = URLEncoder.encode("{\"storeId\":\"11657777\""
				+ ",\"type\":1,\"qual\":0"
				+ ",\"page\":1"
				+ ",\"pageSize\":10}"
				,"utf-8");
		
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "daojia.jd.com");
		header.put("Connection", "keep-alive");
		header.put("Referer", "https://daojia.jd.com/html/index.html");
		header.put("Accept", "application/json");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");
		
		String url = String.format(
				"https://daojia.jd.com/client?"
				+ "functionId=comment/queryDetail"
				+ "&appVersion=4.9.5"
				+ "&appName=paidaojia"
				+ "&platCode=H5"
				+ "&body=%s"
				, body);
		
		String html = HttpsBase.get(url, "utf-8", header).getResult();
//		System.out.println(html);
		
		JSONObject allObject = new JSONObject(html);
		String store_star = allObject.getJSONObject("result").getJSONObject("sta").getString("avg");
		String comment_num = allObject.getJSONObject("result").getJSONObject("config").getString("totalCount");
		JSONObject sta = allObject.getJSONObject("result").getJSONObject("sta");
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
		String comment_hascontent = "只显示有内容的评价";
		// 评论列表
		JSONArray evaluateList = allObject.getJSONObject("result").getJSONArray("evaluateList");
		for (int i = 0; i < evaluateList.length(); i++) {
			String content_tag = "";
			String goods_tag = "";
			JSONObject commentObject = evaluateList.getJSONObject(i);
			String user_name =commentObject.getString("nickName");
			String coment_content = EmojiFilterUtils.removeImoji(commentObject.getString("content")) ;
			String user_star = commentObject.getString("score");
			String comment_datetime = commentObject.getString("time");
			if (commentObject.has("venderTags")) {
				content_tag = commentObject.getJSONArray("venderTags").toString();
				
			}else {
				content_tag = "";
			}
			JSONArray prds = commentObject.getJSONArray("prds");
			for (int j = 0; j < prds.length(); j++) {
				goods_tag = goods_tag+prds.getJSONObject(j).getString("skuName")+"	";
			}
			
			System.out.println(store_star);
			System.out.println(comment_num);
			System.out.println(comment_tag);
			System.out.println(comment_hascontent);
			
			System.out.println(content_tag);
			System.out.println(goods_tag);
			System.out.println(user_name);
			System.out.println(coment_content);
			System.out.println(user_star);
			System.out.println(comment_datetime);
			
			System.out.println("==============");
		}
		
	}

}
