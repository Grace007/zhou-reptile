package com.reptile.ec.amazon.task.thread;

import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.amazon.job.AmazonListJob;
import com.reptile.ec.amazon.job.AmazonUrlJob;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AmazonUrlSnatchThread extends Thread {
	private static Logger logger = Logger.getLogger(AmazonUrlSnatchThread.class);
	private static NutDao tDao = SpringContextHolder.getBean("tDao");
	private AmazonUrlJob task;

	public AmazonUrlSnatchThread(AmazonUrlJob task) {
		this.task = task;
	}

	DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void run() {
		tDao.update(AmazonUrlJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
		logger.info("开始生成>>>>>" + task.getKeyword() + "<<<<<<pages");
		// 生成任务list_job
		Document doc = null;
		String k = task.getKeyword().replaceAll("\n", "");
		String url = "";
		try {
			url = task.getUrl().replaceAll(k, URLEncoder.encode(k, "utf-8"));
		} catch (UnsupportedEncodingException e2) {
			logger.info("转码异常", e2);
		}
		try {
			String html = HttpBase.get(url, "utf-8").getResult();
			doc = Jsoup.parse(html);
		} catch (Exception e) {
			tDao.update(AmazonUrlJob.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
			logger.info(task.getUrl() + "无法连接，将稍后重新连接");
			return;
		}

		int endPage = 1;
		String page_url = "";
		try {
			endPage = Integer.parseInt(doc.getElementsByClass("pagnDisabled").text());
		} catch (Exception e) {
			try {
				endPage = Integer.parseInt(doc.getElementsByClass("pagnLink").last().text());
			} catch (Exception e1) {
				endPage = 1;
			}
		}

		try {
			for (int i = 1; i <= endPage; i++) {
				AmazonListJob listJob = new AmazonListJob();
				page_url = url + "&page=" + i;
				listJob.setStatus(0);
				listJob.setCreate_date(new Date());
				listJob.setUrl(page_url);
				listJob.setKeyword(task.getKeyword());
				listJob.setTask_id(task.getTask_id());
				listJob.setRequest_id(task.getRequest_id());
				listJob.setWebsite(task.getWebsite());
				listJob.setCity_code(task.getCity_code());
				listJob.setProject_code(task.getProject_code());
				listJob.setDown_type(task.getDown_type());
				listJob.setNode(task.getNode());
				try {
					tDao.fastInsert(listJob);
				} catch (Exception e) {
					try {
						tDao.update(listJob);
					} catch (Exception e1) {
						logger.info("", e1);
					}
				}
			}
			tDao.update(AmazonUrlJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("生成>>>>>" + task.getKeyword() + "<<<<<<pages完成");
		} catch (Exception e) {
			tDao.update(AmazonUrlJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.info("生成listjob失败", e);
		}
	}
}
