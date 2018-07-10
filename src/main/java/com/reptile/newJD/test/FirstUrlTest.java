package com.reptile.newJD.test;

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
		String url = "https://daojia.jd.com/client?functionId=address/search&body=%7B%22ref%22:%22indexaddress/LID:NaN%22,%22region%22:%22"+java.net.URLEncoder.encode("上海市", "utf-8")+"%22,%22key%22:%22"+java.net.URLEncoder.encode("凯德", "utf-8")+"%22%7D";
		
		Map<String, String> header = new HashMap<String, String>();
		System.out.println(url);
		header.put("Host", "daojia.jd.com");
		header.put("Connection", "keep-alive");
		header.put("Referer", "https://daojia.jd.com/html/index.html");
		header.put("Accept", "application/json");
		header.put("Accept-Encoding", "gzip, deflate, br");
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
		
		String address="",title="",city="",adcode="";
		double lat,lng;
		int cityCode=0;
		
		address=firstAddressObject.getString("address");
		//System.out.println("address  =  "+address);
		title=firstAddressObject.getString("title");
		lat = firstAddressObject.getJSONObject("location").getDouble("lat");
		lng = firstAddressObject.getJSONObject("location").getDouble("lng");
		city = firstAddressObject.getString("city");
		cityCode = firstAddressObject.getInt("cityCode");
		adcode = firstAddressObject.getString("adcode");
		
		System.out.println(cityCode);
		System.out.println(adcode);
		
		//https://daojia.jd.com/client&functionId=homeSearch/tabCateList&body={"longitude":121.47797,"latitude":31.27095,"cityId":2}
		//https://daojia.jd.com/client?functionId=homeSearch/tabCateList&body={%22longitude%22:121.47797,%22latitude%22:31.27095,%22cityId%22:2}
		//https://daojia.jd.com/client&functionId=homeSearch/tabCateList&body={%22longitude%22:121.47865,%22latitude%22:31.27078,%22cityId%22:2}
		/*String categoryUrl = "https://daojia.jd.com/client&functionId=homeSearch/tabCateList&body={%22longitude%22:"+lng+",%22latitude%22:"+lat+",%22cityId%22:"+cityCode+"}";
		System.out.println(categoryUrl);
		String categoryHtml = HttpsBase.get(categoryUrl, "utf-8",header).getResult();
		System.out.println(categoryHtml);*/
		
        

		
		
	}

}
