package com.reptile.jdDaoJa.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.jdDaoJa.job.JDDaoJiaCommentJob;
import com.reptile.jdDaoJa.model.JDDaoJiaCommentInfo;
import com.reptile.util.EmojiFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JDDaoJiaCommentThread extends Thread {
	private static Logger logger = Logger.getLogger(JDDaoJiaCommentThread.class);
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private JDDaoJiaCommentJob task;

	public JDDaoJiaCommentThread(JDDaoJiaCommentJob task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {
			tDao.update(JDDaoJiaCommentJob.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
			logger.info("开始抓取commentInfo，页数为"+task.getPage());
			TaskToBi tobi = BIDataSource.getBiDataSource("jddaojia", task.getProject_code(),task.getDown_type());
			Dao biDao = tobi.getBiDao();
			Map<String, String> header = new HashMap<String, String>();
			header.put("Host", "daojia.jd.com");
			header.put("Accept", "application/json");
			header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
			String html = HttpBase.get(task.getPage_url(), "utf-8",header).getResult();
			JSONObject allObject = new JSONObject(html);
			
			String store_star="";
			String comment_num;
			String comment_tag="";
			String comment_hascontent;
			String user_name;
			String user_star;
			String coment_content;
			String content_tag;
			String goods_tag="";
			String comment_datetime;
			JDDaoJiaCommentInfo commentInfo = new JDDaoJiaCommentInfo();
			
			JSONArray evaluateList = allObject.getJSONObject("result").getJSONArray("evaluateList");
			
			comment_num = allObject.getJSONObject("result").getJSONObject("config").getString("totalCount");
			comment_hascontent = "只显示有内容的评价";
			if (StringUtils.equals(String.valueOf(task.getPage()) , "1")) {
				JSONObject sta = allObject.getJSONObject("result").getJSONObject("sta");
				store_star = sta.getString("avg");
				comment_tag="好评("+sta.getString("lc1")+")   " +"中评("+sta.getString("lc2")+")   "+ "差评("+sta.getString("lc3")+")   " ;
				JSONArray bdTag = sta.getJSONArray("bdTag");
				try {
					for (int i = 0; i < bdTag.length(); i++) {
						comment_tag = comment_tag + bdTag.getJSONObject(i).getString("tagName")+"("+bdTag.getJSONObject(i).getString("tagCt") +")   "  ;
					}
				} catch (Exception e) {
					
				}
				JSONArray gdTag = sta.getJSONArray("gdTag");
				try {
					for (int i = 0; i < gdTag.length(); i++) {
						comment_tag = comment_tag + gdTag.getJSONObject(i).getString("tagName")+"("+gdTag.getJSONObject(i).getString("tagCt") +")   "  ;
					}
				} catch (Exception e) {
				}
			}else {
				String url = task.getPage_url().replace("page%22%3A"+String.valueOf(task.getPage()), "page%22%3A1");
				String html1 = HttpBase.get(url, "utf-8",header).getResult();
				JSONObject allObject1 = new JSONObject(html1);
				JSONObject sta = allObject1.getJSONObject("result").getJSONObject("sta");
				store_star = sta.getString("avg");
				comment_tag="好评("+sta.getString("lc1")+")   " +"中评("+sta.getString("lc2")+")   "+ "差评("+sta.getString("lc3")+")   " ;
				JSONArray bdTag = sta.getJSONArray("bdTag");
				try {
					for (int i = 0; i < bdTag.length(); i++) {
						comment_tag = comment_tag + bdTag.getJSONObject(i).getString("tagName")+"("+bdTag.getJSONObject(i).getString("tagCt") +")   "  ;
					}
				} catch (Exception e) {
				}
				JSONArray gdTag = sta.getJSONArray("gdTag");
				try {
					for (int i = 0; i < gdTag.length(); i++) {
						comment_tag = comment_tag + gdTag.getJSONObject(i).getString("tagName")+"("+gdTag.getJSONObject(i).getString("tagCt") +")   "  ;
					}
				} catch (Exception e) {
				}
			}
			
			for (int i = 0; i < evaluateList.length(); i++) {
				JSONObject commentObject = evaluateList.getJSONObject(i);
				user_name =commentObject.getString("nickName");
				coment_content = EmojiFilterUtils.removeImoji(commentObject.getString("content")) ;
				user_star = commentObject.getString("score");
				comment_datetime = commentObject.getString("time");
				if (commentObject.has("venderTags")) {
					content_tag = commentObject.getJSONArray("venderTags").toString();
					
				}else {
					content_tag = "";
				}
				JSONArray prds = commentObject.getJSONArray("prds");
				for (int j = 0; j < prds.length(); j++) {
					goods_tag = goods_tag+prds.getJSONObject(j).getString("skuName")+"	";
				}
				
				commentInfo.setAddress(task.getAddress());
				commentInfo.setAddressKey(task.getAddressKey());
				commentInfo.setCategory(task.getCategory());
				commentInfo.setCity_name(task.getCity_name());
				commentInfo.setComment_content(coment_content);
				commentInfo.setComment_datetime(comment_datetime);
				commentInfo.setComment_hascontent(comment_hascontent);
				commentInfo.setComment_num(comment_num);
				commentInfo.setComment_tag(comment_tag);
				commentInfo.setContent_tag(content_tag);
				commentInfo.setCreate_time(new Date());
				commentInfo.setWebsite(task.getWebsite());
				commentInfo.setUser_star(user_star);
				commentInfo.setUser_name(user_name);
				commentInfo.setTask_id(task.getTask_id());
				commentInfo.setStore_star(store_star);
				commentInfo.setStore_name(task.getStore_name());
				commentInfo.setStore_id(task.getStore_id());
				commentInfo.setRequest_id(task.getRequest_id());
				commentInfo.setProject_code(task.getProject_code());
				commentInfo.setGoods_tag(goods_tag);
				commentInfo.setDown_type(task.getDown_type());
				commentInfo.setMd5(MD5Util.getMD5(user_name+comment_datetime+coment_content));
				try {
					biDao.insert(commentInfo);
					//logger.info("comment任务BI插入成功! 页数是"+task.getPage()+"  序号是"+i);
				} catch (Exception e) {
					//logger.error("数据重复,comment任务BI插入失败! 页数是"+task.getPage()+"  序号是"+i);
				}
				user_star="";
				goods_tag="";
				content_tag="";
				coment_content="";
			}
				
			tDao.update(JDDaoJiaCommentJob.class, Chain.make("status", 2),Cnd.where("id", "=", task.getId()));
			logger.info("任务成功！页数是"+task.getPage());

		} catch (Exception e) {
			tDao.update(JDDaoJiaCommentJob.class, Chain.make("status", 3),Cnd.where("id", "=", task.getId()));
			logger.error("=============当前comment任务失败！==========" + task.getCity_name()+ "  " + task.getAddressKey() + "=====", e);
		}
	}
}


