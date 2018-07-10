package com.reptile.jdDaoJa.test;

import com.bds.base.http.HttpBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.nutz.dao.impl.NutDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class SimpleTest {
	@Test
	public void creatable() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"src/main/java/com/bds/o2o/jdDaoJa/test/dataSource.xml");

		NutDao dao = (NutDao) ctx.getBean("tDao");

		System.out.println("==================================");
		
//		dao.create(JDDaojiaStoreSimple.class, false);
//		dao.create(JDDaoJiaStoreJob.class, false);
//		dao.create(JDDaoJiaStoreInfo.class, false);
//		dao.create(JDDaoJiaCommentJob.class, false);
//		dao.create(JDDaoJiaCommentInfo.class, false);
//		dao.create(JDDaoJiaProductUrlJob.class, false);
//		dao.create(JDDaoJiaProductInfo.class, false);

		System.out.println("+++++++++++++++++++++++++++++++++++");

	}
	
	@Test
	public void run() throws Exception{
		
		String url = "https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22%23channelPage%2FchannelId%3A3983%2FchannelName%3A%25E8%25B6%2585%25E5%25B8%2582%25E4%25BE%25BF%25E5%2588%25A9%2Fres_unit%3A1%2Fres_type%3Aball_5%2Funit_index%3A2%22%2C%22channelId%22%3A%223983%22%2C%22city%22%3A%22%25E4%25B8%258A%25E6%25B5%25B7%25E5%25B8%2582%22%2C%22longitude%22%3A121.478516%2C%22latitude%22%3A31.270588%2C%22currentPage%22%3A1%2C%22pageSize%22%3A10%2C%22areaCode%22%3A2%2C%22coordType%22%3A%222%22%2C%22platform%22%3A%221%22%7D&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng=121.478516&lat=31.270588";
		//String url ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22%23channelPage%2FchannelId%3A1118%2FchannelName%3A%25E9%25B2%259C%25E8%258A%25B1%25E7%2583%2598%25E7%2584%2599%2Fres_unit%3A4%2Fres_type%3Aball_5%2Funit_index%3A2%22%2C%22channelId%22%3A%221118%22%2C%22city%22%3A%22%25E4%25B8%258A%25E6%25B5%25B7%25E5%25B8%2582%22%2C%22longitude%22%3A121.478516%2C%22latitude%22%3A31.270588%2C%22currentPage%22%3A1%2C%22pageSize%22%3A10%2C%22areaCode%22%3A2%2C%22coordType%22%3A%222%22%2C%22platform%22%3A%221%22%7D&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng=121.478516&lat=31.270588";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "daojia.jd.com");
		header.put("Accept", "application/json");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
		String html = HttpBase.get(url, "utf-8",header).getResult();
		
		String store_id;
		String store_name;
		String freightAndtime1;
		String freightAndtime2;
		String freightAndtime;
		String store_star;
		String monthSale;
		String freight_start;
		String distance;
		String freight_money;
		String sales_promotion;
		String overtime_payment;
		
		
		JSONObject allObject = new JSONObject(html);
		//System.out.println(allObject);
		JSONArray dataArray = allObject.getJSONObject("result").getJSONObject("data").getJSONArray("data");
		System.out.println(dataArray.length());
		for (int i = 0; i < dataArray.length(); i++) {
			JSONObject storeObject = dataArray.getJSONObject(i).getJSONObject("floorCellData");
			store_id = storeObject.getJSONObject("params").getString("storeId");
			store_name = storeObject.getString("name");
			store_star = storeObject.getString("storeStar");
			monthSale = storeObject.getString("monthSale");
			distance = storeObject.getString("freightWords");		
			freight_money = storeObject.getString("freightWords");
			sales_promotion = storeObject.getString("freightWords");
			
			String carrierNo =storeObject.getString("carrierNo");
			if (carrierNo == "9966" || carrierNo.equals("9966")) {
				freightAndtime1 = "达达专送-";
			}else{
				freightAndtime1 = "商家自送-";
			}
			freightAndtime2 = storeObject.getString("deliveryFirst");
			freightAndtime = freightAndtime1+freightAndtime2;
			
			String [] freightWords=storeObject.getString("freightWords").split("，");
			try {
				freight_start = freightWords[0];
				distance = freightWords[1];
				freight_money = freightWords[2];
			} catch (Exception e) {
				freight_start = "";
				distance ="";
				freight_money="";
			}
			String tag = "";
			JSONArray tags = storeObject.getJSONArray("tags");
			for (int j = 0; j < tags.length(); j++) {
				tag = tag+tags.getJSONObject(j).getString("words")+"&&&&";
			}
			sales_promotion =tag;
			
			
			try {
				overtime_payment = storeObject.getJSONArray("tagList").getJSONObject(0).getString("tagName");
			} catch (Exception e) {
				overtime_payment = "无";
			}
			
			System.out.println(store_id);
			System.out.println(store_name);
			System.out.println(store_star);
			System.out.println(monthSale);
			System.out.println(freightAndtime);
			System.out.println(freight_start);
			System.out.println(distance);
			System.out.println(freight_money);
			System.out.println(sales_promotion);
			System.out.println(overtime_payment);
			//https://daojia.jd.com/client?functionId=store%2FstoreDetailV220&body=%7B%22ref%22%3A%22channelPage%2FchannelId%3A3983%2FchannelName%3A%25E8%25B6%2585%25E5%25B8%2582%25E4%25BE%25BF%25E5%2588%25A9%2Fres_unit%3A1%2Fres_type%3Aball_5%2Funit_index%3A2%2FLID%3A8%22%2C%22storeId%22%3A%2211029652%22%2C%22skuId%22%3A%22%22%2C%22activityId%22%3A%22%22%2C%22promotionType%22%3A%22%22%2C%22longitude%22%3A121.47865%2C%22latitude%22%3A31.27078%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng=121.47865&lat=31.27078&city_id=2

			/*String baseurl = storeObject.getJSONObject("params").getString("channelId")
					+"/channelName:"+":%E8%B6%85%E5%B8%82%E4%BE%BF%E5%88%A9"+"/res_unit:1/res_type:ball_5/unit_index:2\",\"storeId\":\""+store_id+"\",\"skuId\":\"\",\"activityId\":\"\",\"promotionType\":\"\",\"longitude\":"
					+"121.47865"+",\"latitude\":"+"31.27078"+"}&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng="+"121.47865"+"&lat="+"31.27078";
			*/
			String baseurl = "https://daojia.jd.com/client?functionId=store%2FstoreDetailV220&body=%7B%22ref%22%3A%22channelPage%2FchannelId%3A"
					+ storeObject.getJSONObject("params").getString("channelId") + "%2FchannelName%3A" + "%25E8%25B6%2585%25E5%25B8%2582%25E4%25BE%25BF%25E5%2588%25A9"+"%2F"
					+"res_unit%3A1%2Fres_type%3Aball_5%2Funit_index%3A2%2FLID%3A8%22%2C%22storeId%22%3A%22"+store_id+"%22%2C%22skuId%22%3A%22%22%2C%22activityId%22%3A%22%22%2C%22promotionType%22%3A%22%22%2C%22longitude%22%3A"
					+"121.47865"+"%2C%22latitude%22%3A"+"31.27078"+"%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng="+"121.47865"+"&lat="+"31.27078";
			System.out.println(baseurl);
			System.out.println("#####################################");
		}
		
		
	}
}
