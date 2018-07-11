package com.reptile.ec.mogujie.test;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bds.base.http.HttpBase;

public class TestMuGuJieSnatchProductThread {

	public static void main(String[] args) {

		try {
			String html = "", url = "", title = "", discount = "", description = "", product_id = "";
			double price = 0;
			int collect = 0, sale_num = 0;
			url = "http://list.mogujie.com/search?callback=jQuery21106679385025593663_1482378105138&_version=1&_mgjuuid=275d5e8d-aa5e-58f9-7476-b6912ed3532b&sort=pop&cpc_offset=&cKey=pc-search-wall&page=1&q=%25E6%25B4%2597%25E5%258F%2591%25E9%259C%25B2&userId=&ppath=%257B%25E2%2580%259C2048%25E2%2580%259C%253A%25E2%2580%259C11399%25E2%2580%259C%257D&maxPrice=&minPrice=&ratio=2%3A3&_=1482378105139";
			html = HttpBase.get(url, "utf-8").getResult();
			System.out.println(html);
			html = html.substring(html.indexOf("(") + 1, html.lastIndexOf(")"));
			System.out.println(html);
			JSONObject jsonObject = new JSONObject(html);

			JSONArray jsonArray = new JSONArray(jsonObject.getJSONObject("result").getJSONObject("wall").getString("docs"));
			JSONObject jObject = null;
			
			for (int i = 0; i < jsonArray.length(); i++) {

				jObject = jsonArray.getJSONObject(i);
				title = jObject.getString("title");
				price = Double.parseDouble(jObject.getString("price")) ;
				product_id = jObject.getString("tradeItemId") ;
				try {
					collect = Integer.parseInt(jObject.getString("cfav")) ;
				} catch (Exception e) {
					// TODO: handle exception
				}
				System.out.println(title);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
