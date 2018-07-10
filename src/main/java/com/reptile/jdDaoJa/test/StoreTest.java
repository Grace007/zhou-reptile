package com.reptile.jdDaoJa.test;

import com.bds.base.http.HttpBase;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StoreTest {
	
	@Test
	public void run() throws Exception{
		//String url = "https://daojia.jd.com/client?functionId=store%2FstoreDetailV220&body=%7B%22storeId%22%3A%2211029652%22%2C%22skuId%22%3A%22%22%2C%22activityId%22%3A%22%22%2C%22promotionType%22%3A%22%22%2C%22longitude%22%3A121.47865%2C%22latitude%22%3A31.27078%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=121.47865&lat=31.27078";
		String url = "https://daojia.jd.com/client?functionId=store%2FstoreDetailV220&body=%7B%22storeId%22%3A%2211002588%22%2C%22skuId%22%3A%22%22%2C%22activityId%22%3A%22%22%2C%22promotionType%22%3A%22%22%2C%22longitude%22%3A121.47865%2C%22latitude%22%3A31.27078%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=121.47865&lat=31.27078&city_id=2";
		//String url ="https://daojia.jd.com/client?functionId=store%2FstoreDetailV220&body=%7B%22storeId%22%3A%2210044809%22%2C%22skuId%22%3A%22%22%2C%22activityId%22%3A%22%22%2C%22promotionType%22%3A%22%22%2C%22longitude%22%3A121.47865%2C%22latitude%22%3A31.27078%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=121.47865&lat=31.27078&city_id=2";
		Map<String, String> header = new HashMap<String, String>();
		System.out.println(url);
		header.put("Host", "daojia.jd.com");
		header.put("Accept", "application/json");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
		String html = HttpBase.get(url, "utf-8",header).getResult();
		JSONObject allObject = new JSONObject(html);
		
		

		String freightAndtime;
		String freight_money;
		String announcement="";
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
		String store_report="举报商家得红包";
		String overtime_payment;
		String store_report_reward="最高奖100元";
		//System.out.println(html);
		JSONObject storeInfo = allObject.getJSONObject("result").getJSONObject("storeInfo");
		
		if (StringUtils.equals("9966", storeInfo.getString("carrierNo"))) {
			freightAndtime = "达达专送-";
		}else {
			freightAndtime = "商家自送-";
		}
		freightAndtime = freightAndtime+storeInfo.getString("deliveryFirst");
		freight_money = storeInfo.getString("freightWords");
		
		
		JSONArray tags = storeInfo.getJSONArray("tags");
		for (int j = 0; j < tags.length(); j++) {
			announcement = announcement+tags.getJSONObject(j).getString("words")+"&&&&";
		}
		store_star = storeInfo.getString("scoreAvg");
		store_goods_total = storeInfo.getString("inSaleNum");
		monthSale = storeInfo.getString("monthSaleNum");
		store_follownum =storeInfo.getString("followNo");
		store_shophours = storeInfo.getJSONArray("serviceTimes").getJSONObject(0).getString("startTime")+"-"
				+storeInfo.getJSONArray("serviceTimes").getJSONObject(0).getString("endTime");
		store_address = storeInfo.getString("storeAddress");
		store_phone = storeInfo.getString("02156636075");
		store_qualification = storeInfo.getString("storeCertificateUrl");
		if (allObject.getJSONObject("result").getJSONArray("tagList").toString().contains("超时赔付")) {
			overtime_payment = "超时赔付";
		}else {
			overtime_payment = "无";
		}
		
		
		
		JSONObject storeCommentVO = allObject.getJSONObject("result").getJSONObject("storeCommentVO");
		comment_num = storeCommentVO.getString("totalCount");
		comment_user_name = storeCommentVO.getString("buyerShowName");
		comment_user_star = storeCommentVO.getString("score4");
		comment_user_content = storeCommentVO.getString("score4Content");
		comment_user_label = storeCommentVO.getString("tagInfoItemList");
		comment_user_datetime = storeCommentVO.getString("createTime");
		
		
		String coupons ="";
		JSONArray couponsArray = allObject.getJSONObject("result").getJSONArray("coupons");
		for (int i = 0; i < couponsArray.length(); i++) {
			JSONObject couponsObject =couponsArray.getJSONObject(i);
			coupons = coupons+"优惠卷金额："+couponsObject.getString("amount")+couponsObject.getString("amountUnit")+ ",使用要求："
					+couponsObject.getString("limitRule")+",领取要求："+couponsObject.getString("couponTip")+",有效期:"+
					couponsObject.getString("avilableDate")+"	";
			
		}
		//得到分类列表，通过分类列表构建对应的url，注意，可能会有子分类
		//url中需要有catId，storeId，page，timeTag，lng，lat
		
		String category_name="";
		int product_count = 0;
		String catId="";
		String page_url="";
		String father_name="";
		String father_catId="";
		int father_count = 0;
		
		
		
		JSONArray cateArray = allObject.getJSONObject("result").getJSONArray("cateList");
		for (int i = 0; i < cateArray.length(); i++) {
			System.out.println(i+"#######################");
			JSONObject cateObject = cateArray.getJSONObject(i);
			getPageURL(cateObject,"10044809","121.47865","31.27078");
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*System.out.println(coupons);
		System.out.println(freightAndtime);
		System.out.println(freight_money);
		System.out.println(announcement);
		System.out.println(store_star);
		System.out.println(comment_num);
		
		System.out.println(comment_user_name);
		System.out.println(comment_user_star);
		System.out.println(comment_user_label);
		System.out.println(comment_user_content);
		System.out.println(comment_user_datetime);
		System.out.println(store_goods_total);
	    System.out.println(monthSale);
		System.out.println(store_follownum);
		System.out.println(store_shophours);
		System.out.println(store_address);
		System.out.println(store_phone);
		System.out.println(store_qualification);
		System.out.println(overtime_payment);
		Boolean boolean1 =storeInfo.isNull("monthSaleNum");
		
		System.out.println("##############################");*/
		/*
		//构建评论url
		//https://daojia.jd.com/client?functionId=comment%2FqueryDetail&body=%7B%22storeId%22%3A%2211029652%22%2C%22type%22%3A%221%22%2C%22qual%22%3A0%2C%22page%22%3A1%2C%22pageSize%22%3A10%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5
		String baseurl="https://daojia.jd.com/client?functionId=comment%2FqueryDetail&body=%7B%22storeId%22%3A%22"+"storeid"
		+"%22%2C%22type%22%3A%221%22%2C%22qual%22%3A0%2C%22page%22%3A"+"page"
				+"%2C%22pageSize%22%3A10%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5";
		
		String url1 = "https://daojia.jd.com/client?functionId=comment%2FqueryDetail&body=%7B%22storeId%22%3A%2211029652%22%2C%22type%22%3A%221%22%2C%22qual%22%3A0%2C%22page%22%3A1%2C%22pageSize%22%3A10%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5";
		Map<String, String> header1 = new HashMap<String, String>();
		//System.out.println(url);
		header.put("Host", "daojia.jd.com");
		header.put("Accept", "application/json");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
		String html1 = HttpBase.get(url1, "utf-8",header1).getResult();
		JSONObject allObject1 = new JSONObject(html1);
		System.out.println(allObject1);
		int pageSize,totalCount,pages;
		pageSize = allObject1.getJSONObject("result").getJSONObject("config").getInt("pageSize");
		totalCount = allObject1.getJSONObject("result").getJSONObject("config").getInt("totalCount");
		
        if(totalCount % pageSize ==0 && totalCount != 0) { 
		pages = (totalCount) / pageSize;
        }
        else {
        	pages = (totalCount) / pageSize + 1 ;
		}
        System.out.println(totalCount);
        System.out.println(pages);
        System.out.println(pageSize);*/
		
	}
	public String getPageURL(JSONObject cateObject,String store_id,String lng,String lat) throws JSONException {
		
		String category_name="";
		int product_count = 0;
		String catId="";
		String page_url="";
		String father_name="";
		String father_catId="";
		int father_count = 0;
		String father_promotLabel = "";
		
		father_promotLabel = cateObject.getString("promotLabel");
		father_name = cateObject.getString("title");
		father_count = Integer.valueOf(cateObject.getString("productCount")) ;
		father_catId = cateObject.getString("catId");
		JSONArray childCategoryList = cateObject.getJSONArray("childCategoryList");
		if (childCategoryList.length()>=1) {
			
			for (int j = 0; j < childCategoryList.length(); j++) {
				JSONObject childCategory = childCategoryList.getJSONObject(j);
				category_name = childCategory.getString("title");
				getPageURL(childCategory,store_id,lng,lat);
				
			}
		}else {
			
			category_name = father_name;
			product_count = father_count;
			catId = father_catId;
			int pageSize =10;
			int pages;
			//构建URL
			String timeTag=String.valueOf(new Date().getTime())+"555";
			page_url = "https://daojia.jd.com/client?functionId=productsearch%2Fsearch&body=%7B%22key%22%3A%22%22%2C%22catId%22%3A%22"
					+catId+"%22%2C%22storeId%22%3A%22"+store_id+"%22%2C%22sortType%22%3A1%2C%22page%22%3A"+"1"+"%2C%22pageSize%22%3A10%2C%22cartUuid%22%3A%22%22%2C%22promotLable%22%3A%22%22%2C%22timeTag%22%3A"
					+timeTag+"%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng="+lng+"&lat="+lat;
			
			if(product_count % pageSize ==0 && product_count != 0) { 
				pages = (product_count) / pageSize;
		        }
		        else {
		        	pages = (product_count) / pageSize + 1 ;
				}
		      
		        
		        for (int i = 1; i <= pages; i++) {
		        	if (i==1) {
						
					}else {
						page_url=page_url.replace("page%22%3A"+String.valueOf(i-1), "page%22%3A"+String.valueOf(i));
						
					}
		        	System.out.println(page_url);
		        	System.out.println("father_promotLabel::"+father_promotLabel);
		        	//System.out.println(categoryUrl1);
				}
			
			System.out.println(category_name);
			System.out.println(catId);
			System.out.println(product_count);
			System.out.println(pages);
			
			//System.out.println(page_url);
			
		}
		
		
		
		
		
		
		
		return null;
		
	}

}
