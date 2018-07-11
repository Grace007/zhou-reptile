package com.reptile.haodaifu.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.reptile.haodaifu.job.HaoDFCategoryJob;
import com.reptile.haodaifu.job.HaoDFPageArticleListJob;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.util.Date;

public class HaoDFPageArticleListThread extends Thread {
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(MyConstant.ZNode);
	private HaoDFCategoryJob task;
	private Logger logger =Logger.getLogger(HaoDFPageArticleListThread.class);
	public  HaoDFPageArticleListThread(Record task) {
		this.task = task.toPojo(HaoDFCategoryJob.class);
	}
	
	@Override
	public void run() {
		try {
			//http://www.haodf.com/jibing/xiaoerganmao.htm
			//http://www.haodf.com/jibing/xiaoerganmao/wz_0_0_1.htm
			mysqlDao.update(HaoDFCategoryJob.class, Chain.make("article_status", 1), Cnd.where("id", "=", task.getId()));
			logger.info(task.getTiny_categoryurl());
			String baseUrl = task.getTiny_categoryurl().replaceAll(".htm", "/wz_0_0_1.htm");
			String html="",pageurl="";
			HaoDFPageArticleListJob haoDFPageArticleListJob = new HaoDFPageArticleListJob();
			haoDFPageArticleListJob.setBig_categoryname(task.getBig_categoryname());
			haoDFPageArticleListJob.setCreate_time(new Date());
			haoDFPageArticleListJob.setMedian_categoryname(task.getMedian_categoryname());
			haoDFPageArticleListJob.setNode(task.getNode());
			haoDFPageArticleListJob.setRequest_id(task.getRequest_id());
			haoDFPageArticleListJob.setSmall_categoryname(task.getSmall_categoryname());
			haoDFPageArticleListJob.setStatus(0);
			haoDFPageArticleListJob.setTask_id(task.getTask_id());
			haoDFPageArticleListJob.setTiny_categoryname(task.getTiny_categoryname());
			haoDFPageArticleListJob.setTiny_categoryurl(task.getTiny_categoryurl());
			
			
			
			try {
				html = HttpBase.get(baseUrl, "utf-8").getResult();
			} catch (Exception e) {
				logger.error("文章任务分页解析失败",e);
			}
			Document doc = Jsoup.parse(html);
			int pageCount=Integer.parseInt(doc.select("a.page_turn_a").select("font.black").text().replaceAll("\\D", ""));
			for (int i = 1; i <= pageCount; i++) {
				pageurl = baseUrl.replaceAll("wz_0_0_1.htm", "wz_0_0_"+i+".htm");
				haoDFPageArticleListJob.setPagecount(pageCount);
				haoDFPageArticleListJob.setPagecurrent(i);
				haoDFPageArticleListJob.setPageurl(pageurl);
				try {
					mysqlDao.insert(haoDFPageArticleListJob);
				} catch (Exception e) {
					logger.error("分页插入失败,页数是"+i);
				}
				
			}
			
			mysqlDao.update(HaoDFCategoryJob.class, Chain.make("article_status", 2), 
					Cnd.where("id", "=", task.getId()));
		} catch (Exception e) {
			mysqlDao.update(HaoDFCategoryJob.class, Chain.make("article_status", 3), 
					Cnd.where("id", "=", task.getId()));
			logger.error("文章任务分页出错",e);
		}
	}

}
