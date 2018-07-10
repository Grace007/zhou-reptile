package com.reptile.jdDaoJa.test;

import com.bds.base.http.HttpBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CommentTest {
	@Test
	public void run() throws Exception{
		//String url = "https://daojia.jd.com/client?functionId=comment%2FqueryDetail&body=%7B%22storeId%22%3A%2211029652%22%2C%22type%22%3A%221%22%2C%22qual%22%3A0%2C%22page%22%3A1%2C%22pageSize%22%3A10%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5";
		String url = "https://daojia.jd.com/client?functionId=comment%2FqueryDetail&body=%7B%22storeId%22%3A%2211029652%22%2C%22type%22%3A%221%22%2C%22qual%22%3A0%2C%22page%22%3A2%2C%22pageSize%22%3A10%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5";
		Map<String, String> header = new HashMap<String, String>();
		System.out.println(url);
		header.put("Host", "daojia.jd.com");
		header.put("Accept", "application/json");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
		String html = HttpBase.get(url, "utf-8",header).getResult();
		JSONObject allObject = new JSONObject(html);
		
		
		String store_star;
		String comment_num;
		String comment_tag="";
		String comment_hascontent;
		String user_name;
		String user_star;
		String coment_content;
		String content_tag;
		String goods_tag="";
		String comment_datetime;
		
		JSONArray evaluateList = allObject.getJSONObject("result").getJSONArray("evaluateList");
		
		comment_num = allObject.getJSONObject("result").getJSONObject("config").getString("totalCount");
		comment_hascontent = "只显示有内容的评价";
		
		JSONObject sta = allObject.getJSONObject("result").getJSONObject("sta");
		store_star = sta.getString("avg");
		comment_tag="好评("+sta.getString("lc1")+")   " +"中评("+sta.getString("lc2")+")   "+ "差评("+sta.getString("lc3")+")   " ;
		JSONArray bdTag = sta.getJSONArray("bdTag");
		for (int i = 0; i < bdTag.length(); i++) {
			comment_tag = comment_tag + bdTag.getJSONObject(i).getString("tagName")+"("+bdTag.getJSONObject(i).getString("tagCt") +")   "  ;
		}
		JSONArray gdTag = sta.getJSONArray("gdTag");
		for (int i = 0; i < gdTag.length(); i++) {
			comment_tag = comment_tag + gdTag.getJSONObject(i).getString("tagName")+"("+gdTag.getJSONObject(i).getString("tagCt") +")   "  ;
		}
		//取每一个评论
		for (int i = 0; i < evaluateList.length(); i++) {
			JSONObject commentObject = evaluateList.getJSONObject(i);
			user_name =commentObject.getString("nickName");
			coment_content = commentObject.getString("content");
			user_star = commentObject.getString("score");
			comment_datetime = commentObject.getString("time");
			if (commentObject.has("venderTags")) {
				content_tag = commentObject.getJSONArray("venderTags").toString();
				
			}else {
				content_tag = "";
			}
			JSONArray prds = commentObject.getJSONArray("prds");
			for (int j = 0; j < prds.length(); j++) {
				goods_tag = goods_tag+prds.getJSONObject(j).getString("skuName")+"	";
			}
			
			
			
			
			System.out.println(i+"：####################");
			System.out.println(user_name);
			System.out.println(coment_content);
			System.out.println(user_star);
			System.out.println(comment_datetime);
			System.out.println(content_tag);
			System.out.println(goods_tag);
			
		}
		
		System.out.println(store_star);
		System.out.println(comment_num);
		System.out.println(comment_tag);
	}

}
