package com.reptile.newJD.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpsBase;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.newJD.job.JDDaoJiaCommentJob;
import com.reptile.newJD.model.JDDaoJiaCommentInfo;
import com.reptile.util.EmojiFilterUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 抓取评论
 * @date 2018年1月5日 下午2:50:09
 */
public class DaoJiaCommentThread extends Thread {
	
	private static Logger log = Logger.getLogger(DaoJiaCommentThread.class);
	private Dao mysqlDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode)
			.getMysqlDao();
	
	JDDaoJiaCommentJob task;

	public DaoJiaCommentThread(JDDaoJiaCommentJob task) {
		this.task = task;
	}
	
	@Override
	public void run() {
		try {
			log.info("========= newDaoJia 开始抓评论  ==============");
			mysqlDao.update(JDDaoJiaCommentJob.class, Chain.make("status", 1),
					Cnd.where("id", "=", task.getId()));

			crawl();

			mysqlDao.update(JDDaoJiaCommentJob.class, Chain.make("status", 2),
					Cnd.where("id", "=", task.getId()));
			log.info("抓评论任务完毕,website:newDaoJia, task_id:" + task.getId());
		} catch (Exception e) {
			mysqlDao.update(JDDaoJiaCommentJob.class, Chain.make("status", 3),
					Cnd.where("id", "=", task.getId()));
			//log.error("抓评论出错", e);
		}
	}
	
	
	
	/**
	 * 具体抓取逻辑
	 * @throws Exception
	 */
	void crawl() throws Exception{
		TaskToBi tobi = BIDataSource.getBiDataSource("jddaojia", task.getProject_code(),task.getDown_type());
		Dao biDao = tobi.getBiDao();
		// 获取店铺评论页面
		String body = URLEncoder.encode(task.getBody() ,"utf-8");
		
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "daojia.jd.com");
		header.put("Connection", "keep-alive");
		header.put("Referer", "https://daojia.jd.com/html/index.html");
		header.put("Accept", "application/json");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");
		
		String url = String.format(
				"https://daojia.jd.com/client?"
				+ "functionId=comment/queryDetail"
				+ "&appVersion=4.9.5"
				+ "&appName=paidaojia"
				+ "&platCode=H5"
				+ "&body=%s"
				, body);
		
		String html = HttpsBase.get(url, "utf-8", header).getResult();
//		System.out.println(html);
		
		JSONObject allObject = new JSONObject(html);
		String comment_tag = task.getComment_tag();
		
		String comment_hascontent = "只显示有内容的评价";
		// 评论列表
		JSONArray evaluateList = allObject.getJSONObject("result").getJSONArray("evaluateList");
		for (int i = 0; i < evaluateList.length(); i++) {
			String content_tag = "";
			String goods_tag = "";
			JSONObject commentObject = evaluateList.getJSONObject(i);
			String user_name =commentObject.getString("nickName");
			String coment_content = EmojiFilterUtils.removeImoji(commentObject.getString("content")) ;
			String user_star = commentObject.getString("score");
			String comment_datetime = commentObject.getString("time");
			if (commentObject.has("venderTags")) {
				content_tag = commentObject.getJSONArray("venderTags").toString();
				
			}else {
				content_tag = "";
			}
			JSONArray prds = commentObject.getJSONArray("prds");
			for (int j = 0; j < prds.length(); j++) {
				goods_tag = goods_tag+prds.getJSONObject(j).getString("skuName")+"	";
			}
			
			JDDaoJiaCommentInfo commentInfo = new JDDaoJiaCommentInfo();
			
			commentInfo.setAddress(task.getAddress());
			commentInfo.setAddressKey(task.getAddressKey());
			commentInfo.setCategory(task.getCategory());
			commentInfo.setCity_name(task.getCity_name());
			commentInfo.setComment_content(coment_content);
			commentInfo.setComment_datetime(comment_datetime);
			commentInfo.setComment_hascontent(comment_hascontent);
			commentInfo.setComment_num(task.getComment_num());
			commentInfo.setComment_tag(comment_tag);
			commentInfo.setContent_tag(content_tag);
			commentInfo.setCreate_time(new Date());
			commentInfo.setWebsite(task.getWebsite());
			commentInfo.setUser_star(user_star);
			commentInfo.setUser_name(user_name);
			commentInfo.setTask_id(task.getTask_id());
			commentInfo.setStore_star(task.getStore_star());
			commentInfo.setStore_name(task.getStore_name());
			commentInfo.setStore_id(task.getStore_id());
			commentInfo.setRequest_id(task.getRequest_id());
			commentInfo.setProject_code(task.getProject_code());
			commentInfo.setGoods_tag(goods_tag);
			commentInfo.setDown_type(task.getDown_type());
			commentInfo.setMd5(MD5Util.getMD5(user_name+comment_datetime+coment_content));
			
			try {

				biDao.insert(commentInfo);
			} catch (Exception e) {
				//log.error("插入错误", e);
			}
			user_star="";
			goods_tag="";
			content_tag="";
			coment_content="";
		}
		
	}

}
