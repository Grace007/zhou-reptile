package com.reptile.ly.test;

import com.bds.base.http.HttpBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TicketCommentTest {
	@Test
	public void test1() throws Exception{
		//String url= "https://www.ly.com/wanle/api/WanleProduct/GetCommentInfo?sort=3&callback=jQuery1101018087204661743406_1505805095640&lineId=16671&pageSize=5&isSingle=1&pageIndex=2&_=1505805095643";
		String url = "https://www.ly.com/wanle/api/WanleProduct/GetCommentInfo?&lineId=16671&pageSize=5&isSingle=1&pageIndex=2";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Referer", "https://www.ly.com/dujia/wanle/16671.html");
		String result = HttpBase.get(url, "utf-8", header).getResult();
		System.out.println(result);
		JSONObject json = new JSONObject(result);
		System.out.println(json);
		JSONArray cList = json.getJSONObject("Data").getJSONArray("CommentList");
		System.out.println(cList.length());
		
		//result = StringUtil.match("\\((.*?)\\)$", result);
		//System.out.println(result);
		//JSONObject json = new JSONObject(result);
		//System.out.println(json);
	}

}
