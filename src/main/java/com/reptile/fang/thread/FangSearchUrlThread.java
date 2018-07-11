package com.reptile.fang.thread;

import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.fang.model.job.FangSearchNextUrlTaskModel;
import com.reptile.fang.model.job.FangSearchUrlTaskModel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;

public class FangSearchUrlThread extends Thread {
	private static Logger logger = Logger.getLogger(FangSearchUrlThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private FangSearchUrlTaskModel task;

	public FangSearchUrlThread(FangSearchUrlTaskModel task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {
			tDao.update(FangSearchUrlTaskModel.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
			
			int pages = 1 ;
			String url = "http://search.fang.com/bbs/search.jsp?q="+java.net.URLEncoder.encode(task.getKeyword(), "gb2312")+"&key=&city="+java.net.URLEncoder.encode(task.getCity(), "gb2312");
			String html = HttpBase.get(url, "utf-8").getResult();
			
			if (StringUtils.isEmpty(html)) {
				tDao.update(FangSearchUrlTaskModel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
				logger.info("======fang根据关键词和城市搜索获得的页面为空，请更新状态重新进行抓取，失败URL为："+url+"=====");
				return ;
			}
			
			Document doc = Jsoup.parse(html);
			String amount = doc.select("div.rit").select("span.fB").get(0).text();
			Integer total = 0;
			try {
				 total = Integer.parseInt(amount);
			} catch (Exception e) {
				
			}
			
			pages=total/10+1;
			if(pages > 30) pages = 30;
			
			FangSearchNextUrlTaskModel fangNextUrlTaskModel = new FangSearchNextUrlTaskModel();
			
			for (int i = 0; i < pages; i++) {
				String next_url = "http://search.fang.com/bbs/search.jsp?q="+java.net.URLEncoder.encode(task.getKeyword(), "gb2312")+"&start="+i*10+"&num=10&fld=&city="+java.net.URLEncoder.encode(task.getCity(), "gb2312")+"&sort=score&sl=post&show=list&si=0";
				
				fangNextUrlTaskModel.setKeyword(task.getKeyword());
				fangNextUrlTaskModel.setCity(task.getCity());
				fangNextUrlTaskModel.setCreate_date(new Date());
				fangNextUrlTaskModel.setCreate_time(new Date());
				fangNextUrlTaskModel.setDown_type(task.getDown_type());
				fangNextUrlTaskModel.setNode(task.getNode());
				fangNextUrlTaskModel.setPage_index((i+1));
				fangNextUrlTaskModel.setPages(pages);
				fangNextUrlTaskModel.setProject_code(task.getProject_code());
				fangNextUrlTaskModel.setRequest_id(task.getRequest_id());
				fangNextUrlTaskModel.setStatus(0);
				fangNextUrlTaskModel.setTask_id(task.getTask_id());
				fangNextUrlTaskModel.setTotal(total);
				fangNextUrlTaskModel.setUrl(next_url);
				fangNextUrlTaskModel.setWebsite(task.getWebsite());
				
				try {
					tDao.fastInsert(fangNextUrlTaskModel);
				} catch (Exception e) {
					logger.error("====="+"生成分页任务插入MySQL数据库失败，失败的URL为："+fangNextUrlTaskModel.getUrl()+"=====");
				}
			}
			logger.info("====="+"分页任务成功，关键字+城市为："+task.getKeyword()+"  "+task.getCity()+"=====");
			tDao.update(FangSearchUrlTaskModel.class, Chain.make("status", 2).add("pages", pages).add("total", total), Cnd.where("id", "=", task.getId()));
			
		} catch (Exception e) {
			tDao.update(FangSearchNextUrlTaskModel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.info("====="+"分页任务失败,关键字+城市为："+task.getKeyword()+"  "+task.getCity()+"=====",e);
		}
		
	}
}



