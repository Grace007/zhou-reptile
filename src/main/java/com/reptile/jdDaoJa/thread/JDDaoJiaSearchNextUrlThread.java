package com.reptile.jdDaoJa.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.jdDaoJa.job.JDDaoJiaNextUrljob;
import com.reptile.jdDaoJa.job.JDDaoJiaStoreJob;
import com.reptile.jdDaoJa.model.JDDaojiaStoreSimple;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JDDaoJiaSearchNextUrlThread extends Thread {
	private static Logger logger = Logger.getLogger(JDDaoJiaSearchNextUrlThread.class);
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private JDDaoJiaNextUrljob task;

	public JDDaoJiaSearchNextUrlThread(JDDaoJiaNextUrljob task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {
			tDao.update(JDDaoJiaNextUrljob.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
			logger.info("开始抓取simple");
			TaskToBi tobi = BIDataSource.getBiDataSource("jddaojia", task.getProject_code(),task.getDown_type());
			Dao biDao = tobi.getBiDao();
			
			Map<String, String> header = new HashMap<String, String>();
			header.put("Host", "daojia.jd.com");
			header.put("Accept", "application/json");
			header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
			String html = HttpBase.get(task.getUrl(), "utf-8",header).getResult();
			
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
			int ranking;
			JDDaojiaStoreSimple storeSimple = new JDDaojiaStoreSimple();
			JDDaoJiaStoreJob storeJob = new JDDaoJiaStoreJob();
			
			JSONObject allObject = new JSONObject(html);
			
			//System.out.println(allObject);
			JSONArray dataArray = allObject.getJSONObject("result").getJSONObject("data").getJSONArray("data");
			//System.out.println(dataArray.length());
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
				try {
					ranking=(task.getPage()-1)*10+i+1;
				} catch (Exception e) {
					ranking=0;
				}
				
				
				storeSimple.setAddress(task.getAddress());
				storeSimple.setAddressKey(task.getAddressKey());
				storeSimple.setCategory(task.getCategory());
				storeSimple.setCity_name(task.getCity_name());
				storeSimple.setCreate_time(new Date());
				storeSimple.setDistance(distance);
				storeSimple.setDown_type(task.getDown_type());
				storeSimple.setFreight_money(freight_money);
				storeSimple.setFreight_start(freight_start);
				storeSimple.setFreightAndtime(freightAndtime);
				storeSimple.setMonthSale(monthSale);
				storeSimple.setWebsite(task.getWebsite());
				storeSimple.setTask_id(task.getTask_id());
				storeSimple.setStore_star(store_star);
				storeSimple.setStore_name(store_name);
				storeSimple.setStore_id(store_id);
				storeSimple.setSales_promotion(sales_promotion);
				storeSimple.setRequest_id(task.getRequest_id());
				storeSimple.setProject_code(task.getProject_code());
				storeSimple.setOvertime_payment(overtime_payment);
				storeSimple.setStore_url("https://daojia.jd.com/html/index.html#storeHome/storeId:"+store_id);
				storeSimple.setRanking(ranking);
				try {
					biDao.insert(storeSimple);
				} catch (Exception e) {
					//logger.error("数据重复=============当前单一simple任务插入失败！==========");
				}
				//logger.info("store_id="+store_id+"插入成功!现在进入店铺url获取中。。。");
				
				
				/*String baseurl = "https://daojia.jd.com/client?functionId=store%2FstoreDetailV220&body=%7B%22ref%22%3A%22channelPage%2FchannelId%3A"
						+ storeObject.getJSONObject("params").getString("channelId") + "%2FchannelName%3A" 
						+ java.net.URLEncoder.encode(java.net.URLEncoder.encode(task.getCategory(),"utf-8"),"utf-8")+"%2F"
						+"res_unit%3A1%2Fres_type%3Aball_5%2Funit_index%3A2%2FLID%3A8%22%2C%22storeId%22%3A%22"+store_id+"%22%2C%22skuId%22%3A%22%22%2C%22activityId%22%3A%22%22%2C%22promotionType%22%3A%22%22%2C%22longitude%22%3A"
						+task.getLng()+"%2C%22latitude%22%3A"+task.getLat()+"%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng="+task.getLng()+"&lat="+task.getLat();
				*/
				String store_url = "https://daojia.jd.com/client?functionId=store%2FstoreDetailV220&body=%7B%22storeId%22%3A%22"+store_id
						+"%22%2C%22skuId%22%3A%22%22%2C%22activityId%22%3A%22%22%2C%22promotionType%22%3A%22%22%2C%22longitude%22%3A"
						+task.getLng()+"%2C%22latitude%22%3A"+task.getLat()+"%7D&appVersion=4.3.0&appName=paidaojia&platCode=H5&lng="
						+task.getLng()+"&lat="+task.getLat();
				
				storeJob.setAddress(task.getAddress());
				storeJob.setAddressKey(task.getAddressKey());
				storeJob.setCategory(task.getCategory());
				storeJob.setCity_name(task.getCity_name());
				storeJob.setCreate_time(new Date());
				storeJob.setDown_type(task.getDown_type());
				storeJob.setLat(task.getLat());
				storeJob.setLng(task.getLng());
				storeJob.setNode(task.getNode());
				storeJob.setProject_code(task.getProject_code());
				storeJob.setRequest_id(task.getRequest_id());
				storeJob.setWebsite(task.getWebsite());
				storeJob.setTask_id(task.getTask_id());
				storeJob.setStore_url(store_url);
				storeJob.setStore_name(store_name);
				storeJob.setStore_id(store_id);
				storeJob.setStatus(0);
				storeJob.setUrl("https://daojia.jd.com/html/index.html#storeHome/storeId:"+store_id);
				storeJob.setFreight_start(freight_start);
				
				
				try {
					tDao.insert(storeJob);
				} catch (Exception e) {
					//logger.error("数据重复==store_id"+store_id+"插入mysql失败=====");
				}
				//logger.info("==store_id"+store_id+"插入mysql成功===");

				
			}
			
			tDao.update(JDDaoJiaNextUrljob.class,Chain.make("status", 2),Cnd.where("id", "=", task.getId()));
			logger.info("=============当前simple任务成功！==========task页数"+task.getPage());

		} catch (Exception e) {
			tDao.update(JDDaoJiaNextUrljob.class, Chain.make("status", 3),Cnd.where("id", "=", task.getId()));
			logger.error("=============当前simple任务失败！==========task页数" + task.getCity_name()+ "  " + task.getAddressKey() + "=====", e);
		}

	}
}


