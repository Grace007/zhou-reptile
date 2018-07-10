package com.reptile.jdDaoJa.test;

import com.bds.base.http.HttpBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FirstUrlTest {
/*	@Test
	public void creatable(){
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/main/java/com/bds/o2o/jdDaoJa/test/dataSource.xml");

        NutDao dao = (NutDao) ctx.getBean("tDao");

        System.out.println("==================================");
        dao.create(JDDaoJiaUrljob.class, false);
        dao.create(JDDaoJiaNextUrljob.class, false);

        System.out.println("+++++++++++++++++++++++++++++++++++");
		
	}*/
	@Test
	public void test1() throws Exception{
		
		
		int pageSize,totalCount,pages;
		//获取位置信息
		String url = "https://daojia.jd.com/client?functionId=address/search&body=%7B%22ref%22:%22indexaddress/LID:NaN%22,%22region%22:%22"+java.net.URLEncoder.encode("北京市", "utf-8")+"%22,%22key%22:%22"+java.net.URLEncoder.encode("凯德", "utf-8")+"%22%7D";
		
		Map<String, String> header = new HashMap<String, String>();
		System.out.println(url);
		header.put("Host", "daojia.jd.com");
		header.put("Accept", "application/json");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
		String html = HttpBase.get(url, "utf-8",header).getResult();
		//String html1 = HttpBase.get(url,"utf-8").getResult();
		//System.out.println(html);
		
		JSONObject allAddressObject = new JSONObject(html);
		
		//System.out.println(allAddressObject);
		JSONArray allAddressArray = allAddressObject.getJSONArray("result");
		//System.out.println(allAddressArray);
		JSONObject firstAddressObject = allAddressArray.getJSONObject(0);
		//System.out.println(firstAddressObject);
		
		String address="",title="",categoryUrl1="",city="",categoryUrl2="",categoryUrl3="",categoryUrl4="",categoryUrl5="";
		double lat,lng;
		
		address=firstAddressObject.getString("address");
		System.out.println("address  =  "+address);
		title=firstAddressObject.getString("title");
		lat = firstAddressObject.getJSONObject("location").getDouble("lat");
		lng = firstAddressObject.getJSONObject("location").getDouble("lng");
		city = firstAddressObject.getString("city");
		
		
		
		//获取位置的详细参数，以方便获取超市便利的JSON参数
		String index_url = "https://daojia.jd.com/client?functionId=indexh5%2FgetIndex&body=%7B%22address%22%3A%22"+java.net.URLEncoder.encode(title,"utf-8")+"%22%2C%22city%22%3A%22"+java.net.URLEncoder.encode(city,"utf-8")+"%22%2C%22longitude%22%3A"+lng+"%2C%22latitude%22%3A"+lat+"%2C%22coordType%22%3A%222%22%2C%22h5From%22%3A%22%22%2C%22isglb%22%3A%22%22%2C%22currentPage%22%3A%22%22%2C%22storeId%22%3A%22%22%2C%22activityId%22%3A%22%22%2C%22isIndex%22%3Afalse%7D&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat+"&city_id=2";
		String index_html = HttpBase.get(index_url, "utf-8",header).getResult();
		JSONObject index_html_json = new JSONObject(index_html);
		JSONArray index_html_array = index_html_json.getJSONObject("result").getJSONArray("data").getJSONObject(1).getJSONArray("data");
		System.out.println("index_html_array:::"+index_html_array);
		
		String channelId1  = index_html_array.getJSONObject(0).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
		String channelId2  = index_html_array.getJSONObject(1).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
		String channelId3  = index_html_array.getJSONObject(2).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
		String channelId4  = index_html_array.getJSONObject(3).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
		String channelId5  = index_html_array.getJSONObject(4).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
		
		//超市便利的url获取
				//#channelPage/channelId:3983/channelName:%E8%B6%85%E5%B8%82%E4%BE%BF%E5%88%A9/res_unit:1/res_type:ball_5/unit_index:2
		String categoryUrl_ref1_1 = "#channelPage/channelId:"+channelId1;
		String categoryUrl_ref1_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(0).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
		String categoryUrl_ref1_3 = "/res_unit:1/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId1+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
				+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
		String categoryUrl_ref1_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
		//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
		String encode_categoryUrl_ref1 = java.net.URLEncoder.encode( categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3,"utf-8");
		categoryUrl1 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
				+encode_categoryUrl_ref1+categoryUrl_ref1_4;
		System.out.println(categoryUrl1);
		String categoryJsonHtml1 = HttpBase.get(categoryUrl1, "utf-8").getResult();
		JSONObject categoryObject1 = new JSONObject(categoryJsonHtml1);
		//System.out.println(categoryObject1);
		
		pageSize = categoryObject1.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
		totalCount = categoryObject1.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
		System.out.println(pageSize);
		System.out.println(totalCount);
		
        if(totalCount % pageSize ==0 && totalCount != 0) { 
		pages = (totalCount) / pageSize;
        }
        else {
        	pages = (totalCount) / pageSize + 1 ;
		}
        System.out.println(pages);
        
        for (int i = 1; i <= pages; i++) {
        	if (i==1) {
				
			}else {
				categoryUrl1=categoryUrl1.replace("currentPage\":"+String.valueOf(i-1), "currentPage\":"+String.valueOf(i));
				
			}
        	//System.out.println(categoryUrl1);
		}
        //新鲜果蔬
        String categoryUrl_ref2_1 = "#channelPage/channelId:"+channelId2;
		String categoryUrl_ref2_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(1).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
		String categoryUrl_ref2_3 = "/res_unit:2/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId2+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
				+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
		String categoryUrl_ref2_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
		//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
		String encode_categoryUrl_ref2 = java.net.URLEncoder.encode( categoryUrl_ref2_1+categoryUrl_ref2_2+categoryUrl_ref2_3,"utf-8");
		categoryUrl2 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
				+encode_categoryUrl_ref2+categoryUrl_ref2_4;
		System.out.println(categoryUrl2);
		String categoryJsonHtml2 = HttpBase.get(categoryUrl2, "utf-8").getResult();
		JSONObject categoryObject2 = new JSONObject(categoryJsonHtml2);
		//System.out.println(categoryObject2);
		
		pageSize = categoryObject2.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
		totalCount = categoryObject2.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
		System.out.println(pageSize);
		System.out.println(totalCount);
		
        if(totalCount % pageSize ==0 && totalCount != 0) { 
		pages = (totalCount) / pageSize;
        }
        else {
        	pages = (totalCount) / pageSize + 1 ;
		}
        System.out.println(pages);
        
        for (int i = 1; i <= pages; i++) {
        	if (i==1) {
				
			}else {
				categoryUrl2=categoryUrl2.replace("currentPage\":"+String.valueOf(i-1), "currentPage\":"+String.valueOf(i));
				
			}
		}
        
        //零食小吃
        String categoryUrl_ref3_1 = "#channelPage/channelId:"+channelId3;
		String categoryUrl_ref3_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(2).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
		String categoryUrl_ref3_3 = "/res_unit:3/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId3+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
				+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
		String categoryUrl_ref3_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
		//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
		String encode_categoryUrl_ref3 = java.net.URLEncoder.encode( categoryUrl_ref3_1+categoryUrl_ref3_2+categoryUrl_ref3_3,"utf-8");
		categoryUrl3 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
				+encode_categoryUrl_ref3+categoryUrl_ref3_4;
		System.out.println(categoryUrl3);
		String categoryJsonHtml3 = HttpBase.get(categoryUrl3, "utf-8").getResult();
		JSONObject categoryObject3 = new JSONObject(categoryJsonHtml3);
		//System.out.println(categoryObject3);
		
		pageSize = categoryObject3.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
		totalCount = categoryObject3.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
		System.out.println(pageSize);
		System.out.println(totalCount);
		
        if(totalCount % pageSize ==0 && totalCount != 0) { 
		pages = (totalCount) / pageSize;
        }
        else {
        	pages = (totalCount) / pageSize + 1 ;
		}
        System.out.println(pages);
        
        for (int i = 1; i <= pages; i++) {
        	if (i==1) {
				
			}else {
				categoryUrl3=categoryUrl3.replace("currentPage\":"+String.valueOf(i-1), "currentPage\":"+String.valueOf(i));
				
			}
		}
        
        
        //鲜花烘培
        String categoryUrl_ref4_1 = "#channelPage/channelId:"+channelId4;
		String categoryUrl_ref4_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(3).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
		String categoryUrl_ref4_3 = "/res_unit:4/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId4+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
				+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
		String categoryUrl_ref4_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
		//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
		String encode_categoryUrl_ref4 = java.net.URLEncoder.encode( categoryUrl_ref4_1+categoryUrl_ref4_2+categoryUrl_ref4_3,"utf-8");
		categoryUrl4 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
				+encode_categoryUrl_ref4+categoryUrl_ref4_4;
		System.out.println(categoryUrl4);
		
		
		String categoryJsonHtml4 = HttpBase.get(categoryUrl4, "utf-8",header).getResult();
		JSONObject categoryObject4 = new JSONObject(categoryJsonHtml4);
		//System.out.println(categoryObject4);
		
		pageSize = categoryObject4.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
		totalCount = categoryObject4.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
		System.out.println(pageSize);
		System.out.println(totalCount);
		
        if(totalCount % pageSize ==0 && totalCount != 0) { 
		pages = (totalCount) / pageSize;
        }
        else {
        	pages = (totalCount) / pageSize + 1 ;
		}
        System.out.println(pages);
        
        for (int i = 1; i <= pages; i++) {
        	if (i==1) {
				
			}else {
				categoryUrl4=categoryUrl4.replace("currentPage%22%3A"+String.valueOf(i-1), "currentPage%22%3A"+String.valueOf(i));
				System.out.println(categoryUrl4);
			}
		}
        
        //医药健康
        String categoryUrl_ref5_1 = "#channelPage/channelId:"+channelId5;
		String categoryUrl_ref5_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(4).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
		String categoryUrl_ref5_3 = "/res_unit:5/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId5+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
				+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
		String categoryUrl_ref5_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
		//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
		String encode_categoryUrl_ref5 = java.net.URLEncoder.encode( categoryUrl_ref5_1+categoryUrl_ref5_2+categoryUrl_ref5_3,"utf-8");
		categoryUrl5 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
				+encode_categoryUrl_ref5+categoryUrl_ref5_4;
		System.out.println(categoryUrl5);
		
		
		String categoryJsonHtml5 = HttpBase.get(categoryUrl5, "utf-8",header).getResult();
		JSONObject categoryObject5 = new JSONObject(categoryJsonHtml5);
		//System.out.println(categoryObject5);
		
		pageSize = categoryObject5.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
		totalCount = categoryObject5.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
		System.out.println(pageSize);
		System.out.println(totalCount);
		
        if(totalCount % pageSize ==0 && totalCount != 0) { 
		pages = (totalCount) / pageSize;
        }
        else {
        	pages = (totalCount) / pageSize + 1 ;
		}
        System.out.println(pages);
        
        for (int i = 1; i <= pages; i++) {
        	if (i==1) {
				
			}else {
				categoryUrl5=categoryUrl5.replace("currentPage%22%3A"+String.valueOf(i-1), "currentPage%22%3A"+String.valueOf(i));
				System.out.println(categoryUrl5);
			}
        	
		}
        

		
		
	}

}
