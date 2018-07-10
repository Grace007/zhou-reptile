package com.reptile.newJD.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpsBase;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.newJD.job.JDDaoJiaCategoryJob;
import com.reptile.newJD.job.JDDaoJiaStorePageJob;
import com.reptile.util.Re;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.*;


/**
 * 店铺分页
 * @date 2018年1月4日 下午4:33:33
 */
public class DaojiaStorePageThread extends Thread {

	private static Logger log = Logger.getLogger(DaojiaStorePageThread.class);
	private Dao mysqlDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode)
			.getMysqlDao();
	
	JDDaoJiaCategoryJob task;
	
	public DaojiaStorePageThread(JDDaoJiaCategoryJob task) {
		this.task = task;
	}
	
	@Override
	public void run() {
		try {
			log.info("========= newDaoJia 开始店铺分页 ==============");
			mysqlDao.update(JDDaoJiaCategoryJob.class, Chain.make("status", 1),
					Cnd.where("id", "=", task.getId()));

			crawl();

			mysqlDao.update(JDDaoJiaCategoryJob.class, Chain.make("status", 2),
					Cnd.where("id", "=", task.getId()));
			log.info("店铺分页任务完毕,website:newDaoJia, task_id:" + task.getId());
		} catch (Exception e) {
			mysqlDao.update(JDDaoJiaCategoryJob.class, Chain.make("status", 3),
					Cnd.where("id", "=", task.getId()));
			//log.error("店铺分页出错", e);
		}
	}


	/**
	 * 具体抓取逻辑
	 * @throws Exception
	 */
	private void crawl() throws Exception {
		String catidss = task.getCatidss();
		String[] split = catidss.split("\\|");

		List<String> list = new ArrayList<String>();
		for (String string : split) {
			list.add(string);
		}
		// 取得前8个
		List<String> list2 =  shuffleToEight(list);

		String categoryIds = list2.toString();

		// 大list遍历成单个的
		// single_catids = [{"level":3,"categoryId":"22360"},{"level":3,"categoryId":"20019"}]
			String lng = task.getLng();
			String lat = task.getLat();
			String areaId = task.getCity_code();

			// 店铺分页
			String body = "{\"longitude\":"+lng+",\"latitude\":"+lat+","
					+ "\"type\":2,\"key\":\"\",\"industryTags\":[]"
					+ ",\"catIds\":"+categoryIds
					+ ",\"sortType\":1"
					+ ",\"page\":1"
					+ ",\"pageSize\":5,\"storeIds\":[],\"promotLabels\":\"\",\"discountRange\":\"\""
					+ ",\"serviceNo\":1514948628704"
					+ ",\"areaId\":"+areaId+"}";

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
			String storeCount = Re.match("\"storeCount\":(\\d+)", html);
			int page;
			if(Integer.parseInt(storeCount)%5==0){
				page = Integer.parseInt(storeCount)/5;
			}else{
				page = Integer.parseInt(storeCount)/5 + 1;
			}

			// 生成分页
			for (int i = 1; i <= page; i++) {
				String body2 = "{\"longitude\":"+lng+",\"latitude\":"+lat+","
						+ "\"type\":2,\"key\":\"\",\"industryTags\":[]"
						+ ",\"catIds\":"+categoryIds
						+ ",\"sortType\":1"
						+ ",\"page\":"+i
						+ ",\"pageSize\":5,\"storeIds\":[],\"promotLabels\":\"\",\"discountRange\":\"\""
						+ ",\"serviceNo\":1514948628704"
						+ ",\"areaId\":"+areaId+"}";
				String md5 = MD5Util.getMD5(task.getRequest_id()+task.getCategory()+body2);


				// 存入数据库
				JDDaoJiaStorePageJob storePagejob = new JDDaoJiaStorePageJob();
				storePagejob.setAdcode(task.getAdcode());
				storePagejob.setAddress(task.getAddress());
				storePagejob.setAddressKey(task.getAddressKey());
				storePagejob.setBody(body2);
				storePagejob.setCity_code(task.getCityCode());
				storePagejob.setCity_name(task.getCity_name());
				storePagejob.setCreate_time(new Date());
				storePagejob.setDown_type(task.getDown_type());
				storePagejob.setLat(lat);
				storePagejob.setLng(lng);
				storePagejob.setNode(task.getNode());
				storePagejob.setPage(String.valueOf(i));
				storePagejob.setProject_code(task.getProject_code());
				storePagejob.setRequest_id(task.getRequest_id());
				storePagejob.setStatus(0);
				storePagejob.setTask_id(task.getTask_id());
				storePagejob.setTitle(task.getTitle());
				storePagejob.setUrl(url);
				storePagejob.setWebsite(task.getWebsite());
				storePagejob.setMd5(md5);
				storePagejob.setCategory(task.getCategory());

				try {
					mysqlDao.insert(storePagejob);
				} catch (Exception e) {
					//log.error("店铺分页插入错误",e);
				}
				
			}

	}
	
	/**
	 * 返回前8个打乱顺序的catids
	 * @return list
	 */
	public List<String> shuffleToEight(List<String> list){
		Collections.shuffle(list);
		List<String> list2 = new ArrayList<String>();

		int limit = list.size();
		if(limit > 8){
			limit = 8;
		}
		for (int i = 0; i < limit; i++) {
			list2.add(list.get(i));
		}
		return list2;
	}

}
