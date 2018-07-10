package com.reptile.jdDaoJa.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.jdDaoJa.job.JDDaoJiaNextUrljob;
import com.reptile.jdDaoJa.job.JDDaoJiaUrljob;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JDDaoJiaSearchUrlThread extends Thread {
	private static Logger logger = Logger.getLogger(JDDaoJiaSearchUrlThread.class);
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private JDDaoJiaUrljob task;

	public JDDaoJiaSearchUrlThread(JDDaoJiaUrljob task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {
			tDao.update(JDDaoJiaUrljob.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
			
			//获取位置信息
			Map<String, String> header = new HashMap<String, String>();
			String url = "https://daojia.jd.com/client?functionId=address/search&body=%7B%22ref%22:%22indexaddress/LID:NaN%22,%22region%22:%22"+java.net.URLEncoder.encode(task.getCity_name(), "utf-8")+"%22,%22key%22:%22"+java.net.URLEncoder.encode(task.getAddressKey(), "utf-8")+"%22%7D";
			//System.out.println(url);
			header.put("Host", "daojia.jd.com");
			header.put("Accept", "application/json");
			header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
			String html = HttpBase.get(url, "utf-8",header).getResult();
			JSONObject allAddressObject = new JSONObject(html);
			
			//System.out.println(allAddressObject);
			JSONArray allAddressArray = allAddressObject.getJSONArray("result");
			//System.out.println(allAddressArray);
			
			JSONObject firstAddressObject;
			
			try {
				firstAddressObject= allAddressArray.getJSONObject(0);
			} catch (Exception e1) {
				tDao.update(JDDaoJiaUrljob.class,Chain.make("status", 3),Cnd.where("id", "=", task.getId()));
		        logger.error("######################对应的城市地点没有商家###################################");
		        return ;
			}
			//System.out.println(firstAddressObject);
			
			String address="",title="",categoryUrl1="",city="",categoryUrl2="",categoryUrl3="",categoryUrl4="",categoryUrl5="";
			String  lat,lng;
			int pageSize,totalCount,pages;
			
			address=firstAddressObject.getString("address");
			title=firstAddressObject.getString("title");
			lat = firstAddressObject.getJSONObject("location").getString("lat");
			lng = firstAddressObject.getJSONObject("location").getString("lng");
			city = firstAddressObject.getString("city");
			
			//获取位置的详细参数，以方便获取超市便利的JSON参数
			String index_url = "https://daojia.jd.com/client?functionId=indexh5%2FgetIndex&body=%7B%22address%22%3A%22"+java.net.URLEncoder.encode(title,"utf-8")+"%22%2C%22city%22%3A%22"+java.net.URLEncoder.encode(city,"utf-8")+"%22%2C%22longitude%22%3A"+lng+"%2C%22latitude%22%3A"+lat+"%2C%22coordType%22%3A%222%22%2C%22h5From%22%3A%22%22%2C%22isglb%22%3A%22%22%2C%22currentPage%22%3A%22%22%2C%22storeId%22%3A%22%22%2C%22activityId%22%3A%22%22%2C%22isIndex%22%3Afalse%7D&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat+"&city_id=2";
			String index_html = HttpBase.get(index_url, "utf-8",header).getResult();
			JSONObject index_html_json = new JSONObject(index_html);
			JSONArray index_html_array = index_html_json.getJSONObject("result").getJSONArray("data").getJSONObject(1).getJSONArray("data");
			//System.out.println("index_html_array:::"+index_html_array);
			
			String channelId1  = index_html_array.getJSONObject(0).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
			String channelId2  = index_html_array.getJSONObject(1).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
			String channelId3  = index_html_array.getJSONObject(2).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
			String channelId4  = index_html_array.getJSONObject(3).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
			String channelId5  = index_html_array.getJSONObject(4).getJSONObject("floorCellData").getJSONObject("params").getString("channelId");
			JDDaoJiaNextUrljob nextUrljob = new JDDaoJiaNextUrljob();
			String title1 = index_html_array.getJSONObject(0).getJSONObject("floorCellData").getString("title");
			String title2 = index_html_array.getJSONObject(1).getJSONObject("floorCellData").getString("title");
			String title3 = index_html_array.getJSONObject(2).getJSONObject("floorCellData").getString("title");
			String title4 = index_html_array.getJSONObject(3).getJSONObject("floorCellData").getString("title");
			String title5 = index_html_array.getJSONObject(4).getJSONObject("floorCellData").getString("title");
			
			//超市便利的url获取
			//#channelPage/channelId:3983/channelName:%E8%B6%85%E5%B8%82%E4%BE%BF%E5%88%A9/res_unit:1/res_type:ball_5/unit_index:2
			try {
				String categoryUrl_ref1_1 = "#channelPage/channelId:"+channelId1;//3983
				String categoryUrl_ref1_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(0).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
				String categoryUrl_ref1_3 = "/res_unit:1/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId1+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
						+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
				String categoryUrl_ref1_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
				//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
				String encode_categoryUrl_ref1 = java.net.URLEncoder.encode( categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3,"utf-8");
				categoryUrl1 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
						+encode_categoryUrl_ref1+categoryUrl_ref1_4;
				//System.out.println(categoryUrl1);
				String categoryJsonHtml1 = HttpBase.get(categoryUrl1, "utf-8").getResult();
				JSONObject categoryObject1 = new JSONObject(categoryJsonHtml1);
				//System.out.println(categoryObject1);
				
				pageSize = categoryObject1.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
				totalCount = categoryObject1.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
				/*System.out.println("超市便利：");
				System.out.println(pageSize);
				System.out.println(totalCount);*/
				
				if(totalCount % pageSize ==0 && totalCount != 0) { 
				pages = (totalCount) / pageSize;
				}
				else {
					pages = (totalCount) / pageSize + 1 ;
				}
				//System.out.println(pages);
				
				for (int i = 1; i <= pages; i++) {
					if (i==1) {
					}else {
						categoryUrl1=categoryUrl1.replace("currentPage%22%3A"+String.valueOf(i-1), "currentPage%22%3A"+String.valueOf(i));
					}
					System.out.println(address);
					nextUrljob.setAddress(address);
					nextUrljob.setAddressKey(task.getAddressKey());
					nextUrljob.setCategory(title1);
					nextUrljob.setCreate_time(new Date());
					nextUrljob.setCity_name(task.getCity_name());
					nextUrljob.setDown_type(task.getDown_type());
					nextUrljob.setLat(lat);
					nextUrljob.setLng(lng);
					nextUrljob.setNode(task.getNode());
					nextUrljob.setProject_code(task.getProject_code());
					nextUrljob.setRequest_id(task.getRequest_id());
					nextUrljob.setWebsite(task.getWebsite());
					nextUrljob.setUrl(categoryUrl1);
					nextUrljob.setTitle(task.getTitle());
					nextUrljob.setStatus(0);
					nextUrljob.setTask_id(task.getTask_id());
					nextUrljob.setPage(i);
					
					try {
						tDao.insert(nextUrljob);
						//logger.info("当前第"+i+"页分页成功！");
					} catch (Exception e) {
						//logger.error("数据重复====="+"分页分支1任务插入mysql失败,城市和分类为："+task.getCity_name()+"  "+title1+"=====");
					}
				}
			} catch (NullPointerException e1) {
				logger.error("此分类没有商家");
			}
	        
	        
	        //新鲜果蔬
	        try {
				String categoryUrl_ref2_1 = "#channelPage/channelId:"+channelId2;//3997
				String categoryUrl_ref2_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(1).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
				String categoryUrl_ref2_3 = "/res_unit:2/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId2+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
						+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
				String categoryUrl_ref2_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
				//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
				String encode_categoryUrl_ref2 = java.net.URLEncoder.encode( categoryUrl_ref2_1+categoryUrl_ref2_2+categoryUrl_ref2_3,"utf-8");
				categoryUrl2 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
						+encode_categoryUrl_ref2+categoryUrl_ref2_4;
				//System.out.println(categoryUrl2);
				String categoryJsonHtml2 = HttpBase.get(categoryUrl2, "utf-8").getResult();
				JSONObject categoryObject2 = new JSONObject(categoryJsonHtml2);
				//System.out.println(categoryObject2);
				
				pageSize = categoryObject2.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
				totalCount = categoryObject2.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
				/*System.out.println(pageSize);
				System.out.println(totalCount);*/
				
				if(totalCount % pageSize ==0 && totalCount != 0) { 
				pages = (totalCount) / pageSize;
				}
				else {
					pages = (totalCount) / pageSize + 1 ;
				}
				//System.out.println(pages);
				
				for (int i = 1; i <= pages; i++) {
					if (i==1) {
					}else {
						categoryUrl2=categoryUrl2.replace("currentPage%22%3A"+String.valueOf(i-1), "currentPage%22%3A"+String.valueOf(i));
					}
					nextUrljob.setAddress(address);
					nextUrljob.setAddressKey(task.getAddressKey());
					nextUrljob.setCategory(title2);
					nextUrljob.setCreate_time(new Date());
					nextUrljob.setCity_name(task.getCity_name());
					nextUrljob.setDown_type(task.getDown_type());
					nextUrljob.setLat(lat);
					nextUrljob.setLng(lng);
					nextUrljob.setNode(task.getNode());
					nextUrljob.setProject_code(task.getProject_code());
					nextUrljob.setRequest_id(task.getRequest_id());
					nextUrljob.setWebsite(task.getWebsite());
					nextUrljob.setUrl(categoryUrl2);
					nextUrljob.setTitle(task.getTitle());
					nextUrljob.setStatus(0);
					nextUrljob.setTask_id(task.getTask_id());
					nextUrljob.setPage(i);
					try {
						tDao.insert(nextUrljob);
						//logger.info("当前第"+i+"页分页成功！");
					} catch (Exception e) {
						//logger.error("数据重复====="+"分页分支2任务插入mysql失败,城市和分类为："+task.getCity_name()+"  "+title2+"=====");
					}
				}
	        } catch (NullPointerException e1) {
				logger.error("此分类没有商家");
			}
	        
	        
	      //零食小吃
	        try {
				String categoryUrl_ref3_1 = "#channelPage/channelId:"+channelId3;
				String categoryUrl_ref3_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(2).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
				String categoryUrl_ref3_3 = "/res_unit:3/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId3+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
						+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
				String categoryUrl_ref3_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
				//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
				String encode_categoryUrl_ref3 = java.net.URLEncoder.encode( categoryUrl_ref3_1+categoryUrl_ref3_2+categoryUrl_ref3_3,"utf-8");
				categoryUrl3 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
						+encode_categoryUrl_ref3+categoryUrl_ref3_4;
				//System.out.println(categoryUrl3);
				String categoryJsonHtml3 = HttpBase.get(categoryUrl3, "utf-8").getResult();
				JSONObject categoryObject3 = new JSONObject(categoryJsonHtml3);
				//System.out.println(categoryObject3);
				
				pageSize = categoryObject3.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
				totalCount = categoryObject3.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
				/*System.out.println(pageSize);
				System.out.println(totalCount);*/
				
				if(totalCount % pageSize ==0 && totalCount != 0) { 
				pages = (totalCount) / pageSize;
				}
				else {
					pages = (totalCount) / pageSize + 1 ;
				}
				//System.out.println(pages);
				
				for (int i = 1; i <= pages; i++) {
					if (i==1) {
						
					}else {
						categoryUrl3=categoryUrl3.replace("currentPage%22%3A"+String.valueOf(i-1), "currentPage%22%3A"+String.valueOf(i));
						
					}
					nextUrljob.setAddress(address);
					nextUrljob.setAddressKey(task.getAddressKey());
					nextUrljob.setCategory(title3);
					nextUrljob.setCreate_time(new Date());
					nextUrljob.setCity_name(task.getCity_name());
					nextUrljob.setDown_type(task.getDown_type());
					nextUrljob.setLat(lat);
					nextUrljob.setLng(lng);
					nextUrljob.setNode(task.getNode());
					nextUrljob.setProject_code(task.getProject_code());
					nextUrljob.setRequest_id(task.getRequest_id());
					nextUrljob.setWebsite(task.getWebsite());
					nextUrljob.setUrl(categoryUrl3);
					nextUrljob.setTitle(task.getTitle());
					nextUrljob.setStatus(0);
					nextUrljob.setTask_id(task.getTask_id());
					nextUrljob.setPage(i);
					try {
						tDao.insert(nextUrljob);
						//logger.info("当前第"+i+"页分页成功！");
					} catch (Exception e) {
						//logger.error("数据重复====="+"分页分支3任务插入mysql失败,城市和分类为："+task.getCity_name()+"  "+title3+"=====");
					}
				}
	        } catch (NullPointerException e1) {
				logger.error("此分类没有商家");
			}
	        
	        
	        //鲜花烘培
	        try {
				String categoryUrl_ref4_1 = "#channelPage/channelId:"+channelId4;
				String categoryUrl_ref4_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(3).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
				String categoryUrl_ref4_3 = "/res_unit:4/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId4+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
						+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
				String categoryUrl_ref4_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
				//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
				String encode_categoryUrl_ref4 = java.net.URLEncoder.encode( categoryUrl_ref4_1+categoryUrl_ref4_2+categoryUrl_ref4_3,"utf-8");
				categoryUrl4 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
						+encode_categoryUrl_ref4+categoryUrl_ref4_4;
				//System.out.println(categoryUrl4);
				
				
				String categoryJsonHtml4 = HttpBase.get(categoryUrl4, "utf-8",header).getResult();
				JSONObject categoryObject4 = new JSONObject(categoryJsonHtml4);
				//System.out.println(categoryObject4);
				
				pageSize = categoryObject4.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
				totalCount = categoryObject4.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
				/*System.out.println(pageSize);
				System.out.println(totalCount);*/
				
				if(totalCount % pageSize ==0 && totalCount != 0) { 
				pages = (totalCount) / pageSize;
				}
				else {
					pages = (totalCount) / pageSize + 1 ;
				}
				//System.out.println(pages);
				
				for (int i = 1; i <= pages; i++) {
					if (i==1) {
						
					}else {
						categoryUrl4=categoryUrl4.replace("currentPage%22%3A"+String.valueOf(i-1), "currentPage%22%3A"+String.valueOf(i));
						
					}
					nextUrljob.setAddress(address);
					nextUrljob.setAddressKey(task.getAddressKey());
					nextUrljob.setCategory(title4);
					nextUrljob.setCreate_time(new Date());
					nextUrljob.setCity_name(task.getCity_name());
					nextUrljob.setDown_type(task.getDown_type());
					nextUrljob.setLat(lat);
					nextUrljob.setLng(lng);
					nextUrljob.setNode(task.getNode());
					nextUrljob.setProject_code(task.getProject_code());
					nextUrljob.setRequest_id(task.getRequest_id());
					nextUrljob.setWebsite(task.getWebsite());
					nextUrljob.setUrl(categoryUrl4);
					nextUrljob.setTitle(task.getTitle());
					nextUrljob.setStatus(0);
					nextUrljob.setTask_id(task.getTask_id());
					nextUrljob.setPage(i);
					try {
						tDao.insert(nextUrljob);
						//logger.info("当前第"+i+"页分页成功！");
					} catch (Exception e) {
						//logger.error("数据重复====="+"分页分支4任务插入mysql失败,城市和分类为："+task.getCity_name()+"  "+title4+"=====");
					}
				}
	        } catch (NullPointerException e1) {
				logger.error("此分类没有商家");
			}
	        
	        
	        //医药健康
	        try {
				String categoryUrl_ref5_1 = "#channelPage/channelId:"+channelId5;
				String categoryUrl_ref5_2 = "/channelName:"+java.net.URLEncoder.encode(index_html_array.getJSONObject(4).getJSONObject("floorCellData").getString("title"), "utf-8");  ;
				String categoryUrl_ref5_3 = "/res_unit:5/res_type:ball_5/unit_index:2\",\"channelId\":\""+channelId5+"\",\"city\":\""+java.net.URLEncoder.encode(city,"utf-8")+"\",\"longitude\":"+lng
						+",\"latitude\":"+lat+",\"currentPage\":1,\"pageSize\":10,\"areaCode\":2,\"coordType\":\"2\",\"platform\":\"1\"}";
				String categoryUrl_ref5_4 = "&platCode=H5&appVersion=4.3.0&appName=paidaojia&lng="+lng+"&lat="+lat;
				//System.out.println(categoryUrl_ref1_1+categoryUrl_ref1_2+categoryUrl_ref1_3);
				String encode_categoryUrl_ref5 = java.net.URLEncoder.encode( categoryUrl_ref5_1+categoryUrl_ref5_2+categoryUrl_ref5_3,"utf-8");
				categoryUrl5 ="https://daojia.jd.com/client?functionId=zone%2FrecommendStoreList&body=%7B%22ref%22%3A%22"
						+encode_categoryUrl_ref5+categoryUrl_ref5_4;
				//System.out.println(categoryUrl5);
				
				
				String categoryJsonHtml5 = HttpBase.get(categoryUrl5, "utf-8",header).getResult();
				JSONObject categoryObject5 = new JSONObject(categoryJsonHtml5);
				//System.out.println(categoryObject5);
				
				pageSize = categoryObject5.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("pageSize");
				totalCount = categoryObject5.getJSONObject("result").getJSONObject("config").getJSONObject("storeConfig").getInt("totalCount");
				/*System.out.println(pageSize);
				System.out.println(totalCount);*/
				
				if(totalCount % pageSize ==0 && totalCount != 0) { 
				pages = (totalCount) / pageSize;
				}
				else {
					pages = (totalCount) / pageSize + 1 ;
				}
				//System.out.println(pages);
				
				for (int i = 1; i <= pages; i++) {
					if (i==1) {
						
					}else {
						categoryUrl5=categoryUrl5.replace("currentPage%22%3A"+String.valueOf(i-1), "currentPage%22%3A"+String.valueOf(i));
						
					}
					nextUrljob.setAddress(address);
					nextUrljob.setAddressKey(task.getAddressKey());
					nextUrljob.setCategory(title5);
					nextUrljob.setCreate_time(new Date());
					nextUrljob.setCity_name(task.getCity_name());
					nextUrljob.setDown_type(task.getDown_type());
					nextUrljob.setLat(lat);
					nextUrljob.setLng(lng);
					nextUrljob.setNode(task.getNode());
					nextUrljob.setProject_code(task.getProject_code());
					nextUrljob.setRequest_id(task.getRequest_id());
					nextUrljob.setWebsite(task.getWebsite());
					nextUrljob.setUrl(categoryUrl5);
					nextUrljob.setTitle(task.getTitle());
					nextUrljob.setStatus(0);
					nextUrljob.setTask_id(task.getTask_id());
					nextUrljob.setPage(i);
					try {
						tDao.insert(nextUrljob);
						//logger.info("当前第"+i+"页分页成功！");
					} catch (Exception e) {
						//logger.error("数据重复====="+"分页分支5任务插入mysql失败,城市和分类为："+task.getCity_name()+"  "+title5+"=====");
					}
					
				}
	        } catch (NullPointerException e1) {
				logger.error("此分类没有商家");
			}
	        
	        
	        tDao.update(JDDaoJiaUrljob.class,Chain.make("status", 2).add("address", address).add("lat", lat).add("lng", lng),Cnd.where("id", "=", task.getId()));
	        logger.info("######################任务成功###################################");
	        
	        
	        
	        
	        
			
		} catch (Exception e) {
			tDao.update(JDDaoJiaUrljob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.error("====="+"分页任务失败,关键字+城市为："+task.getCity_name()+"  "+task.getAddressKey()+"=====",e);
		}
		
	}
}



