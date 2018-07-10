package com.reptile.jdDaoJa.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.jdDaoJa.job.JDDaoJiaProductUrlJob;
import com.reptile.jdDaoJa.model.JDDaoJiaProductInfo;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.*;

public class JDDaoJiaProductThread extends Thread {
	private static Logger logger = Logger.getLogger(JDDaoJiaProductThread.class);
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private JDDaoJiaProductUrlJob task;

	public JDDaoJiaProductThread(JDDaoJiaProductUrlJob task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {
			tDao.update(JDDaoJiaProductUrlJob.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
			TaskToBi tobi = BIDataSource.getBiDataSource("jddaojia", task.getProject_code(),task.getDown_type());
			Dao biDao = tobi.getBiDao();
			Map<String, String> header = new HashMap<String, String>();
			header.put("Host", "daojia.jd.com");
			header.put("Accept", "application/json");
			header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
			String html = HttpBase.get(task.getPage_url(), "utf-8",header).getResult();
			
			String product_name;
			
			String product_num;
			
			String product_monthSale;
			
			String product_highOpinion;
			
			String product_price;
			
			String product_inStock;
			String product_promotions="";
			String product_id="";
			
			
			JSONArray productArray = new JSONObject(html).getJSONObject("result").getJSONArray("searchResultVOList");
			List<JDDaoJiaProductInfo> list = new ArrayList<JDDaoJiaProductInfo>();
			for (int i = 0; i < productArray.length(); i++) {
				JDDaoJiaProductInfo productInfo = new JDDaoJiaProductInfo();
				product_promotions ="";
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
				
				/*System.out.println(i+"#############");
				System.out.println(product_name);
				System.out.println(product_num);
				System.out.println(product_monthSale);
				System.out.println(product_highOpinion);
				System.out.println(product_price);
				System.out.println(product_inStock);
				System.out.println(product_promotions);*/
				
				
				productInfo.setAddress(task.getAddress());
				productInfo.setAddressKey(task.getAddressKey());
				productInfo.setAnnouncement(task.getAnnouncement());
				productInfo.setCity_name(task.getCity_name());
				productInfo.setCreate_time(new Date());
				productInfo.setDown_type(task.getDown_type());
				productInfo.setFreight_money(task.getFreight_money());
				productInfo.setFreightAndtime(task.getFreightAndtime());
				productInfo.setProduct_category(task.getCategory_name());
				productInfo.setProduct_highOpinion(product_highOpinion);
				productInfo.setWebsite(task.getWebsite());
				productInfo.setTask_id(task.getTask_id());
				productInfo.setStore_name(task.getStore_name());
				productInfo.setStore_id(task.getStore_id());
				productInfo.setRequest_id(task.getRequest_id());
				productInfo.setProject_code(task.getProject_code());
				productInfo.setProduct_promotions(product_promotions);
				productInfo.setProduct_price(product_price);
				productInfo.setProduct_num(product_num);
				productInfo.setProduct_name(product_name);
				productInfo.setProduct_monthSale(product_monthSale);
				productInfo.setProduct_inStock(product_inStock);
				productInfo.setProduct_id(product_id);
				
				list.add(productInfo);
				
				
			}
			try {
				biDao.fastInsert(list);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			tDao.update(JDDaoJiaProductUrlJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("productinfo任务完成");
			
			
			
			
			

		} catch (Exception e) {
			tDao.update(JDDaoJiaProductUrlJob.class, Chain.make("status", 3),Cnd.where("id", "=", task.getId()));
			logger.error("=============当前productinfo任务失败！==========" + task.getCity_name()+ "  " + task.getAddressKey() + "=====", e);
		}
	}
  
	
}


