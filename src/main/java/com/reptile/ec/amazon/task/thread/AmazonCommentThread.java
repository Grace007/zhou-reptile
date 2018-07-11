/**
 *
 */
package com.reptile.ec.amazon.task.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.amazon.job.AmazonCommentJob;
import com.reptile.ec.amazon.model.AmazonCommentModel;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AmazonCommentThread extends Thread {
	private static Logger logger = Logger.getLogger(AmazonCommentThread.class);
	private static NutDao tDao = SpringContextHolder.getBean("tDao");
	private AmazonCommentJob task;

	public AmazonCommentThread(AmazonCommentJob task) {
		this.task = task;
	}

	private DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");

	@Override
	public void run() {
		tDao.update(AmazonCommentJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
		logger.info("开始下载>>>>>" + task.getKeyword() + "<<<<<<");
		TaskToBi tobi = BIDataSource.getBiDataSource("amazon", task.getProject_code(), "get_product_info");
		Dao biDao = tobi.getBiDao();
		Document doc = null;
		try {
			String html = HttpBase.get(task.getComment_url(), "utf-8").getResult();
			doc = Jsoup.parse(html);

		} catch (Exception e) {
			tDao.update(AmazonCommentJob.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
			logger.info(task.getComment_url() + "无法连接，将稍后重新连接");
			return;
		}

		try {
			Elements els = doc.select("#cm_cr-review_list").select("div.review");
			Date time = null;
			String comment_id = "", comment_userful = "", comment_user = "", comment_content = "", comment_star = "",
					comment_topic = "";
			for (int i = 0; i < els.size(); i++) {
				comment_id = els.get(i).attr("id");
				comment_topic = els.get(i).select("a.review-title").text();
				String t = els.get(i).select("span.review-votes").text();
				if (!t.equals("")) {
					try {
						comment_userful = t.split("人")[1].replace("中有", "").trim() + "/" + t.split("人")[0].trim();
					} catch (Exception e) {

					}
				}
				comment_star = els.get(i).select("span.a-icon-alt").text();
				comment_user = els.get(i).select("a.author").text();
				try {
					time = DATA_FORMAT.parse(els.get(i).select("span.review-date").text().replace("于 ", ""));
				} catch (Exception e) {

				}
				comment_content = els.get(i).select("span.review-text").text();

				AmazonCommentModel amazon = new AmazonCommentModel();

				amazon.setComment_useful(comment_userful);

				amazon.setProduct_name(task.getTitle());
				amazon.setRequest_id(task.getRequest_id());
				amazon.setPage_id(task.getAsin());
				amazon.setUrl(task.getComment_url());
				amazon.setComment_id(comment_id);
				amazon.setComment_user(comment_user);
				amazon.setComment_time(time);
				amazon.setComment_topic(comment_topic);
				amazon.setComment_star(comment_star);
				amazon.setComment_content(comment_content);
				amazon.setCreate_date(new Date());
				try {
					try {
						biDao.fastInsert(amazon);
					} catch (Exception e) {
					}
				} catch (Exception e) {
					// logger.error("", e);
				}
			}

			tDao.update(AmazonCommentJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("*****************完成解析评论列表,ASIN:" + task.getAsin() + ",id" + task.getId() + ",第"
					+ task.getPage_index() + "页" + "*****************");
		} catch (Exception e) {
			tDao.update(AmazonCommentJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.error("", e);
		}
	}
}
