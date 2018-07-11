package com.reptile.ec.mogujie.thread;

import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.mogujie.model.MS_comment_info;
import com.reptile.ec.mogujie.model.job.MogujieCommentTaskModel;
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
 * 
 * 根据产品评论的jsonURL直接解析评论
 */
public class MoGuJieParserCommentThread extends Thread {

	private static Logger logger = Logger.getLogger(MoGuJieParserCommentThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private MogujieCommentTaskModel task;

	public MoGuJieParserCommentThread(MogujieCommentTaskModel task) {
		this.task = task;
	}

	@Override
	public void run() {

		try {

			tDao.update(MogujieCommentTaskModel.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			Dao biDao = BIDataSource.getBiDataSource("mogujie", task.getProject_code(), "get_product_info").getBiDao();

			String jsonUrl = "", html = "";

			jsonUrl = task.getCom_url();
			try {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Host", "shop.mogujie.com");
//				headers.put("Referer", "http://shop.mogujie.com/detail/" + task.getProduct_id());
//				headers.put("Cookie", MyConstant.MOGUJIE_CONFIG.COOKIE);
				html = HttpBase.get(jsonUrl, "utf-8", headers).getResult();
			} catch (Exception e) {
				tDao.update(MogujieCommentTaskModel.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
				logger.info(task.getProduct_id() + "的评论第" + task.getPage_index() + "页，无法连接，将稍后重新连接");
				return;
			}

			JSONObject Json = new JSONObject(html);
			String comment_id = "", username = "", text = "", time = "", commentTime = "", add = "", style = "";
			
			JSONArray jsonArray = new JSONArray(Json.getJSONObject("data").getString("list")) ;

			if (jsonArray.length() == 0) {
				tDao.update(MogujieCommentTaskModel.class, Chain.make("status", 5),
						Cnd.where("id", "=", task.getId()));
				logger.info("蘑菇街此页URL没有评论内容：" + task.getPage_index());
				return;
			} else {
				JSONObject jsonObject = null ;
				for (int j = 0; j < jsonArray.length(); j++) {
					
					text = "" ;
					style = "" ;
					add = "" ;
					comment_id = "" ;
					
					jsonObject = jsonArray.getJSONObject(j);
					// 评论的ID号
					comment_id = jsonObject.getString("id") ;
					// 获得评论者的姓名
					username = jsonObject.getJSONObject("userInfo").getString("uname") ;
					// 获得评价的内容
					text =  jsonObject.getString("content") ;
					
					try {
						// 产品信息：规格等例如：规格:100g / 135g
						style = style + jsonObject.getString("stock").replace("[", "").replace("]", "").replace("\"", "");
					} catch (Exception e) {

					}
					
					try {
						time = jsonObject.getString("formatDate") ;
						commentTime = time.replace("年", "-").replace("月", "-").replace("日", "");
					} catch (Exception e) {
						time = "" ;
						commentTime = "" ;
					}
					try {
						// 获得追加评论
						add = jsonObject.getJSONObject("append").getString("content") ;
					} catch (Exception e) {
						add = "";
					}

					MS_comment_info mogujie = new MS_comment_info();

					mogujie.setAdd_text(add);
					mogujie.setCategory(task.getCategory());
					mogujie.setCom_time(commentTime);
					mogujie.setComment_id(comment_id);
					mogujie.setCreate_date(new Date());
					mogujie.setKeyword(task.getKeyword());
					mogujie.setStyle(style);
					mogujie.setText(text);
					mogujie.setUsername(username);
					mogujie.setProduct_id(task.getProduct_id());
					mogujie.setRequest_id(task.getRequest_id());
					try {
						biDao.fastInsert(mogujie);
					} catch (Exception e) {
						logger.info("================mogujie往BI评论库灌入失败，URL为："+task.getCom_url()+"================");
					}
				}
				tDao.update(MogujieCommentTaskModel.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
				logger.info("完成解析评论列表：" + task.getProduct_id() + "，完成率：" + task.getPage_index() + "/"
						+ task.getPage_total());
			}

		} catch (Exception e) {
			tDao.update(MogujieCommentTaskModel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.info("", e);
		}
	}
}
