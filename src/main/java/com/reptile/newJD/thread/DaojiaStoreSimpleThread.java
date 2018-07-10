package com.reptile.newJD.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpsBase;
import com.bds.base.util.Constant;
import com.reptile.newJD.job.JDDaoJiaStorePageJob;
import com.reptile.newJD.job.JDDaojiaStoreSimpleJob;
import com.reptile.newJD.model.JDDaojiaStoreSimple;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * simple 商店信息的抓取
 * 
 * @date 2018年1月4日 下午5:54:23
 */
public class DaojiaStoreSimpleThread extends Thread {
	private static Logger log = Logger.getLogger(DaojiaStoreSimpleThread.class);
	private Dao mysqlDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode)
			.getMysqlDao();

	JDDaoJiaStorePageJob task;

	public DaojiaStoreSimpleThread(JDDaoJiaStorePageJob task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {
			log.info("========= newDaoJia 开始店铺simple的抓取 ==============");
			mysqlDao.update(JDDaoJiaStorePageJob.class,
					Chain.make("status", 1), Cnd.where("id", "=", task.getId()));

			crawl();

			mysqlDao.update(JDDaoJiaStorePageJob.class,
					Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
			log.info("店铺simple任务完毕,website:newDaoJia, task_id:" + task.getId());
		} catch (Exception e) {
			mysqlDao.update(JDDaoJiaStorePageJob.class,
					Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			//log.error("店铺simple出错", e);
		}
	}

	/**
	 * 具体的抓取逻辑
	 * 
	 * @throws Exception
	 */
	private void crawl() throws Exception {
		TaskToBi tobi = BIDataSource.getBiDataSource("jddaojia", task.getProject_code(), task.getDown_type());

		// simple 店铺信息抓取
		String body = task.getBody();
		
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "daojia.jd.com");
		header.put("Connection", "keep-alive");
		header.put("Referer", "https://daojia.jd.com/html/index.html");
		header.put("Accept", "application/json");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");
		
		String url = "https://daojia.jd.com/client";
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("functionId", "homeSearch/searchByStorePostV_230");
		data.put("body", body);
		data.put("appVersion", "4.9.5");
		data.put("appName", "paidaojia");
		data.put("platCode", "H5");
		
		String html = HttpsBase.post(url, "utf-8", header, data).getResult();
		JSONObject json = new JSONObject(html);
		JSONArray jsonArray = json.getJSONObject("result").getJSONArray("storeSkuList");
		for (int i = 0; i < jsonArray.length(); i++) {
			String storeName = jsonArray.getJSONObject(i).getJSONObject("store").getString("storeName");
			String carrierNo = jsonArray.getJSONObject(i).getJSONObject("store").getString("carrierNo");
			String freighttime = jsonArray.getJSONObject(i).getJSONObject("store").getString("deliveryFirst");
			if (carrierNo == "9966" || carrierNo.equals("9966")) {
				carrierNo = "达达专送-";
			}else{
				carrierNo = "商家自送-";
			}
			String freightAndtime = carrierNo + freighttime;
			String grade = jsonArray.getJSONObject(i).getJSONObject("store").getString("grade");
			String monSale = jsonArray.getJSONObject(i).getJSONObject("store").getString("monthSale");
			String tag = "";
			JSONArray tags = jsonArray.getJSONObject(i).getJSONObject("store").getJSONArray("tag");
			for (int j = 0; j < tags.length(); j++) {
				tag = tag+tags.getJSONObject(j).getString("words")+"&&&&";
			}
			String sales_promotion = tag;
			String overtime_payment = "";
			try {
				overtime_payment = jsonArray.getJSONObject(i).getJSONObject("store")
						.getJSONArray("tagList").getJSONObject(0).getString("tagName");
			} catch (Exception e) {
				overtime_payment = "无";
			}
			String storeId = jsonArray.getJSONObject(i).getJSONObject("store").getString("storeId");
			
			JDDaojiaStoreSimple storeSimple = new JDDaojiaStoreSimple();
			JDDaojiaStoreSimpleJob storeSimpleJob = new JDDaojiaStoreSimpleJob();
			
			storeSimple.setAddress(task.getAddress());
			storeSimple.setAddressKey(task.getAddressKey());
			storeSimple.setCategory(task.getCategory());
			storeSimple.setCity_name(task.getCity_name());
			storeSimple.setCreate_time(new Date());
			storeSimple.setDown_type(task.getDown_type());
			storeSimple.setFreightAndtime(freightAndtime);
			storeSimple.setMonthSale(monSale);
			storeSimple.setOvertime_payment(overtime_payment);
			storeSimple.setProject_code(task.getProject_code());
			storeSimple.setRanking((Integer.parseInt(task.getPage())-1)*5+i+1);
			storeSimple.setRequest_id(task.getRequest_id());
			storeSimple.setSales_promotion(sales_promotion);
			storeSimple.setStore_id(storeId);
			storeSimple.setStore_name(storeName);
			storeSimple.setStore_star(grade);
			storeSimple.setStore_url("https://daojia.jd.com/html/index.html#storeHome/storeId:"+storeId);
			storeSimple.setTask_id(task.getTask_id());
			storeSimple.setWebsite(task.getWebsite());
			storeSimple.setLng(task.getLng());
			storeSimple.setLat(task.getLat());
			storeSimple.setNode(task.getNode());
			storeSimple.setStatus(0);
			
			storeSimpleJob.setAddress(task.getAddress());
			storeSimpleJob.setAddressKey(task.getAddressKey());
			storeSimpleJob.setCategory(task.getCategory());
			storeSimpleJob.setCity_name(task.getCity_name());
			storeSimpleJob.setCreate_time(new Date());
			storeSimpleJob.setDown_type(task.getDown_type());
			storeSimpleJob.setFreightAndtime(freightAndtime);
			storeSimpleJob.setMonthSale(monSale);
			storeSimpleJob.setOvertime_payment(overtime_payment);
			storeSimpleJob.setProject_code(task.getProject_code());
			storeSimpleJob.setRanking((Integer.parseInt(task.getPage())-1)*5+i+1);
			storeSimpleJob.setRequest_id(task.getRequest_id());
			storeSimpleJob.setSales_promotion(sales_promotion);
			storeSimpleJob.setStore_id(storeId);
			storeSimpleJob.setStore_name(storeName);
			storeSimpleJob.setStore_star(grade);
			storeSimpleJob.setStore_url("https://daojia.jd.com/html/index.html#storeHome/storeId:"+storeId);
			storeSimpleJob.setTask_id(task.getTask_id());
			storeSimpleJob.setWebsite(task.getWebsite());
			storeSimpleJob.setLng(task.getLng());
			storeSimpleJob.setLat(task.getLat());
			storeSimpleJob.setNode(task.getNode());
			storeSimpleJob.setStatus(0);
			
			
			try {
				mysqlDao.insert(storeSimpleJob);
				tobi.getBiDao().insert(storeSimple);
			} catch (Exception e) {
				//log.error("插入store simple错误",e);
			}
		}
	
	}

}
