package com.reptile.ec.amazon.task.thread;

import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.amazon.job.AmazonListJob;
import com.reptile.ec.amazon.job.AmazonProductJob;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AmazonListSnatchThread extends Thread {
	private static Logger logger = Logger.getLogger(AmazonListSnatchThread.class);
	private static NutDao tDao = SpringContextHolder.getBean("tDao");
	private AmazonListJob task;

	public AmazonListSnatchThread(AmazonListJob task) {
		this.task = task;
	}

	DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void run() {
		tDao.update(AmazonListJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
		logger.info("开始生成>>>>>" + task.getKeyword() + "<<<<<<产品任务");
		// 生成product
		Document doc = null;
		try {
			String html = HttpBase.get(task.getUrl(), "utf-8").getResult();
			doc = Jsoup.parse(html);
		} catch (Exception e) {
			tDao.update(AmazonListJob.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
			logger.info(task.getUrl() + "无法连接，将稍后重新连接");
			return;
		}
		String title = "", url = "", asin = "";
		int comment_num = 0;

		doc.select("div#catCorResults").remove();
		Elements eles = doc.select(".s-result-list-parent-container").select("ul").select("li");
		try {
			for (int i = 0; i < eles.size(); i++) {
				try {
					title = eles.get(i).getElementsByClass("a-spacing-mini").get(0).getElementsByTag("h2").text();
				} catch (Exception e) {
					title = "";
				}
				try {
					comment_num = Integer.parseInt(eles.get(i).getElementsByClass("a-spacing-none").last()
							.getElementsByClass("a-size-small").text().replaceAll(",", ""));
				} catch (Exception e) {
					comment_num = 0;
				}

				asin = eles.get(i).attr("data-asin");
				url = "https://www.amazon.cn/dp/" + asin;
				if (StringUtils.isEmpty(asin)) {
					continue;
				}
				AmazonProductJob productJob = new AmazonProductJob();
				productJob.setStatus(0);
				productJob.setCreate_date(new Date());
				productJob.setKeyword(task.getKeyword());
				productJob.setProject_code(task.getProject_code());
				productJob.setDown_type(task.getDown_type());
				productJob.setComment_status(0);
				productJob.setRequest_id(task.getRequest_id());
				productJob.setTask_id(task.getTask_id());
				productJob.setComment_total(comment_num);
				productJob.setPage_url(url);
				productJob.setAsin(asin);
				productJob.setTitle(title);
				productJob.setNode(task.getNode());
				try {
					tDao.fastInsert(productJob);
				} catch (Exception e) {
					try {
						tDao.update(productJob);
						tDao.update(AmazonListJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
					} catch (Exception e1) {
						logger.info("", e1);
						tDao.update(AmazonListJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
					}
				}
			}
			tDao.update(AmazonListJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("生成>>>>>" + task.getKeyword() + "<<<<<<产品任务完成");
		} catch (Exception e) {
			tDao.update(AmazonListJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.info("生成productjob失败", e);
		}
	}
}
