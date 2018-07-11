package com.reptile.xiachufang.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.xiachufang.model.XiachufangAuthorInfo;
import com.reptile.xiachufang.model.job.XiachufangCookbookPagemodel;
import com.reptile.xiachufang.model.job.XiachufangSearchAuthorByIdmodel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;

public class XiachufangSearchAuthorThread extends Thread {
	private static Logger logger = Logger.getLogger(XiachufangSearchAuthorThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private XiachufangSearchAuthorByIdmodel task;
	public  XiachufangSearchAuthorThread(XiachufangSearchAuthorByIdmodel task) {
		this.task =task;
	}
	
	@Override
	public void run() {
		
		logger.info("###################开始解析author_id："+task.getAuthor_id()+"######################");
		tDao.update(XiachufangSearchAuthorByIdmodel.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
		TaskToBi tobi = BIDataSource.getBiDataSource("xiachufang",task.getProject_code(), task.getDown_type());
		Dao  biDao = tobi.getBiDao();
		String html = null;
		String author_id = task.getAuthor_id();
		String baseUrl = "http://www.xiachufang.com";
		String author_url = baseUrl.concat("/cook/").concat(author_id);
		try {
		    html = HttpBase.get(author_url, "utf-8").getResult();
		} catch (Exception e) {
			return ;
		}
		Document doc = Jsoup.parse(html);
		
		XiachufangAuthorInfo authorInfo = new XiachufangAuthorInfo();
		
		String author_name = doc.select("h1.page-title").text().trim();
		String sex = doc.select("div.pure-u-5-8.font12.pr30").select("span.display-inline-block").first().text().trim();
		if(!(sex.contains("男") || sex.contains("女"))) sex = null;
		String join_date = doc.select("div.pure-u-5-8.font12.pr30").select("span.display-inline-block").last().text().replaceAll("[\u4e00-\u9fa5]+","").trim();
		String following_num = doc.select("div.follow-wrap").select("div.pure-u-1-2").get(0).select("a").get(0).text().trim();
		String followed_num = doc.select("div.follow-wrap").select("div.pure-u-1-2").get(1).select("a").get(0).text().trim();
		String cookbook_num = doc.select("div.tab-bar.pure-g").select("ul.plain.pure-g").select("li").get(1).select("a").text().trim().replaceAll("\\D", "");
		if(StringUtils.isEmpty(cookbook_num)) cookbook_num = "0";
		String production_num = doc.select("div.tab-bar.pure-g").select("ul.plain.pure-g").select("li").get(2).select("a").text().trim().replaceAll("\\D", "");
		if (StringUtils.isEmpty(production_num)) production_num ="0";
		String menu_num = doc.select("div.tab-bar.pure-g").select("ul.plain.pure-g").select("li").get(3).select("a").text().trim().replaceAll("\\D", "");
		if (StringUtils.isEmpty(menu_num)) menu_num ="0";
		//界面改版 collent_num为0
		//String collect_num = doc.select("div.tab-bar.pure-g").select("ul.plain.pure-g").select("li").get(4).select("a").text().trim().replaceAll("\\D", "");
		String collect_num = "0";
		if (StringUtils.isEmpty(collect_num)) collect_num ="0";
		String message_num = doc.select("div.tab-bar.pure-g").select("ul.plain.pure-g").select("li").get(4).select("a").text().trim().replaceAll("\\D", "");
		if (StringUtils.isEmpty(message_num)) message_num ="0";
		
		authorInfo.setAuthor_id(author_id);
		authorInfo.setAuthor_name(author_name);
		authorInfo.setAuthor_url(author_url);
		authorInfo.setCollect_num(collect_num);
		authorInfo.setCookbook_num(cookbook_num);
		authorInfo.setCreat_date(new Date());
		authorInfo.setCreate_time(new Date());
		authorInfo.setDown_type(task.getDown_type());
		authorInfo.setFollowed_num(followed_num);
		authorInfo.setFollowing_num(following_num);
		authorInfo.setJoin_date(join_date);
		authorInfo.setMenu_num(menu_num);
		authorInfo.setMessage_num(message_num);
		authorInfo.setNode(task.getNode());
		authorInfo.setProduction_num(production_num);
		authorInfo.setRequest_id(task.getRequest_id());
		authorInfo.setSex(sex);
		authorInfo.setTask_id(task.getTask_id());
		authorInfo.setWebsite(task.getWebsite());
		
		
		
		try {
			biDao.fastInsert(authorInfo);
		} catch (Exception e) {
			tDao.update(XiachufangSearchAuthorByIdmodel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.error("###############authorinfo插入BI表失败，失败id为："+task.getAuthor_id()+"#############"+e);
			return ;
		}
		
		tDao.update(XiachufangSearchAuthorByIdmodel.class, Chain.make("author_name", author_name).add("author_url", author_url).add("status", 2), Cnd.where("id", "=", task.getId()));
		logger.info("###################authorinfo插入BI表成功，成功id为："+task.getAuthor_id()+"#############");
		
		if (task.getEntrance() == 3) {
			//自动生成用户菜谱列表的分页任务
			try {
			
			int pages = 1;
			int amount = Integer.parseInt(cookbook_num);
			if (amount == 0) {
				logger.info("作者id:"+task.getAuthor_id()+"没有菜谱列表");
				return ;
			}
			logger.info("作者id:"+task.getAuthor_id()+"有菜谱列表有"+amount+"个");
			if (amount % 15 == 0 && amount != 0) {
				pages = amount / 15 ;
			}else {
				pages = amount / 15 + 1;
			}
			XiachufangCookbookPagemodel pagemodel = new XiachufangCookbookPagemodel();
			String  page_url = null ;
			for (int i = 0; i < pages; i++) {
				
				page_url = "http://www.xiachufang.com/cook/"+task.getAuthor_id()+"/created/?page="+(i+1);
				
				pagemodel.setAuthor(author_name);
				pagemodel.setAuthor_id(author_id);
				pagemodel.setCreate_date(new Date());
				pagemodel.setDown_type(task.getDown_type());
				pagemodel.setNode(task.getNode());
				pagemodel.setPage_url(page_url);
				pagemodel.setPageindex(i+1);
				pagemodel.setProject_code(task.getProject_code());
				pagemodel.setRequest_id(task.getRequest_id());
				pagemodel.setTask_id(task.getTask_id());
				pagemodel.setWebsite(task.getWebsite());
				pagemodel.setStatus(0);
				
				try {
				    tDao.fastInsert(pagemodel);
				} catch (Exception e) {
					logger.error("################生成分页任务失败，失败的page_url:"+page_url+"###############");
				}
				logger.info("################生成分页任务成功，成功的page_url:"+page_url+"###############");
			}
			} catch (Exception e) {
				logger.info("################生成分页任务失败，失败的作者id:"+task.getAuthor_id()+"###############");
				tDao.update(XiachufangSearchAuthorByIdmodel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			}
			tDao.update(XiachufangSearchAuthorByIdmodel.class, Chain.make("author_name", author_name).add("author_url", author_url).add("status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("###################分页任务插入分页表成功，成功id为："+task.getAuthor_id()+"#############");
			
			
		}
		
		
		
	}

}
