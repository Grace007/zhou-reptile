package com.reptile.haodaifu.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.reptile.haodaifu.job.HaoDFCategoryJob;
import com.reptile.haodaifu.job.HaoDFPageDoctorListJob;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.util.Date;

public class HaoDFPageDoctorListThread extends Thread{
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(MyConstant.ZNode);
	private HaoDFCategoryJob task;
	private Logger logger =Logger.getLogger(HaoDFPageDoctorListThread.class);
	public  HaoDFPageDoctorListThread(Record task) {
		this.task = task.toPojo(HaoDFCategoryJob.class);
	}
	@Override
	public void run() {
		try {
			mysqlDao.update(HaoDFCategoryJob.class, Chain.make("doctor_status", 1), Cnd.where("id", "=", task.getId()));
			logger.info(task.getTiny_categoryurl());
			String baseUrl = task.getTiny_categoryurl().replaceAll(".htm", "/daifu_1_all_all_all_all.htm");
			String html="",pageurl="";
			HaoDFPageDoctorListJob haoDFPageDoctorListJob = new HaoDFPageDoctorListJob();
			haoDFPageDoctorListJob.setBig_categoryname(task.getBig_categoryname());
			haoDFPageDoctorListJob.setCreate_time(new Date());
			haoDFPageDoctorListJob.setMedian_categoryname(task.getMedian_categoryname());
			haoDFPageDoctorListJob.setNode(task.getNode());
			haoDFPageDoctorListJob.setRequest_id(task.getRequest_id());
			haoDFPageDoctorListJob.setSmall_categoryname(task.getSmall_categoryname());
			haoDFPageDoctorListJob.setStatus(0);
			haoDFPageDoctorListJob.setTask_id(task.getTask_id());
			haoDFPageDoctorListJob.setTiny_categoryname(task.getTiny_categoryname());
			haoDFPageDoctorListJob.setTiny_categoryurl(task.getTiny_categoryurl());
			
			
			
			try {
				html = HttpBase.get(baseUrl, "utf-8").getResult();
			} catch (Exception e) {
				logger.error("作者任务分页解析失败",e);
			}
			Document doc = Jsoup.parse(html);
			int pageCount=Integer.parseInt(doc.select("a.page_turn_a").select("font.black").text());
			for (int i = 1; i <= pageCount; i++) {
				pageurl = baseUrl.replaceAll("_1_", "_"+i+"_");
				haoDFPageDoctorListJob.setPagecount(pageCount);
				haoDFPageDoctorListJob.setPagecurrent(i);
				haoDFPageDoctorListJob.setPageurl(pageurl);
				try {
					mysqlDao.insert(haoDFPageDoctorListJob);
				} catch (Exception e) {
					logger.error("分页插入失败,页数是"+i);
				}
				
			}
			
			mysqlDao.update(HaoDFCategoryJob.class, Chain.make("doctor_status", 2), 
					Cnd.where("id", "=", task.getId()));
		} catch (Exception e) {
			mysqlDao.update(HaoDFCategoryJob.class, Chain.make("doctor_status", 3), 
					Cnd.where("id", "=", task.getId()));
			logger.error("作者任务分页出错",e);
		}
	}

}
