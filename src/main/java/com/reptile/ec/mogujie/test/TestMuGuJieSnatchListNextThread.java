package com.reptile.ec.mogujie.test;

import org.json.JSONObject;

import com.bds.base.http.HttpBase;

public class TestMuGuJieSnatchListNextThread {

	public static void main(String[] args) {
		
		try {
			
			String url = "http://list.mogujie.com/search?callback=jQuery21106679385025593663_1482378105138&_version=1&_mgjuuid=275d5e8d-aa5e-58f9-7476-b6912ed3532b&sort=pop&cpc_offset=&cKey=pc-search-wall&page=1&q=%25E6%25B4%2597%25E5%258F%2591%25E9%259C%25B2&userId=&ppath=%257B%25E2%2580%259C2048%25E2%2580%259C%253A%25E2%2580%259C11399%25E2%2580%259C%257D&maxPrice=&minPrice=&ratio=2%3A3&_=1482378105139" ;
			String html = HttpBase.get(url, "utf-8").getResult();
			System.out.println(html);
			html = html.substring(html.indexOf("(")+1, html.lastIndexOf(")"));
			System.out.println(html);
			JSONObject jsonObject = new JSONObject(html) ;
			String total = jsonObject.getJSONObject("result").getJSONObject("wall").getJSONObject("pager").getString("total") ;
			int pageTotal = 1 ;
			
			if (Integer.parseInt(total) % 60 == 0) {
				pageTotal = Integer.parseInt(total) / 60 ;
			}else {
				pageTotal = Integer.parseInt(total) / 60 + 1 ;
			}
			
			System.out.println(pageTotal);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
