package com.reptile.jdDaoJa.test;

import com.bds.base.http.HttpBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ProductTest {
	@Test
	public void run() throws Exception{
		String url = "https://daojia.jd.com/client?functionId=productsearch%2Fsearch&body=%7B%22key%22%3A%22%22%2C%22catId%22%3A%224173227%22%2C%22storeId%22%3A%2211029652%22%2C%22sortType%22%3A1%2C%22page%22%3A1%2C%22pageSize%22%3A10%2C%22cartUuid%22%3A%22%22%2C%22promotLable%22%3A%22%22%2C%22timeTag%22%3A1500548030727555%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=121.47865&lat=31.27078";
		//String url = "https://daojia.jd.com/client?functionId=productsearch%2Fsearch&body=%7B%22key%22%3A%22%22%2C%22catId%22%3A%22%22%2C%22storeId%22%3A%2211029652%22%2C%22sortType%22%3A1%2C%22page%22%3A1%2C%22pageSize%22%3A10%2C%22cartUuid%22%3A%22%22%2C%22promotLable%22%3A%221201:91864%22%2C%22timeTag%22%3A1500548030662555%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=121.47865&lat=31.27078";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "daojia.jd.com");
		header.put("Accept", "application/json");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
		String html = HttpBase.get(url, "utf-8",header).getResult();
		
		
		
		String product_name;
		
		String product_num;
		
		String product_monthSale;
		
		String product_highOpinion;
		
		String product_price;
		
		String product_inStock;
		String product_promotions="";
		String product_id="";
		
		JSONArray productArray = new JSONObject(html).getJSONObject("result").getJSONArray("searchResultVOList");
		for (int i = 0; i < productArray.length(); i++) {
			JSONObject pJsonObject = productArray.getJSONObject(i);
			product_name = pJsonObject.getString("skuName");
			product_num = pJsonObject.getString("stockCount");
			product_monthSale = pJsonObject.getString("monthSales");
			product_highOpinion = pJsonObject.getString("highOpinion");
			product_price = pJsonObject.getString("realTimePrice");
			product_inStock = pJsonObject.getString("saleStatus");
			JSONArray tags = pJsonObject.getJSONArray("tags");
			product_id = pJsonObject.getString("skuId");
			for (int j = 0; j < tags.length(); j++) {
				product_promotions = product_promotions+tags.getJSONObject(j).getString("iconText")+"	";
			}
			
			System.out.println(i+"#############");
			System.out.println(product_name);
			System.out.println(product_num);
			System.out.println(product_monthSale);
			System.out.println(product_highOpinion);
			System.out.println(product_price);
			System.out.println(product_inStock);
			System.out.println(product_promotions);
			System.out.println(product_id);
			product_promotions ="";
		}
		
		
		
		//System.out.println(productArray);
		//System.out.println(html);
		
		
		/*String freightAndtime;
		
		String freight_money;
		
		String announcement;*/
		
		
		
		
		
		
		
		
		
		
	}

}
