package com.reptile.xiachufang.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.xiachufang.model.XiachufangAuthorCookbookSimple;
import com.reptile.xiachufang.model.job.XiachufangCookbookPagemodel;
import com.reptile.xiachufang.model.job.XiachufangCookbookmodel;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;

public class XiachufangCookbookSimpleThread extends Thread {
	private static Logger logger = Logger.getLogger(XiachufangCookbookSimpleThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private XiachufangCookbookPagemodel task;
	public  XiachufangCookbookSimpleThread(XiachufangCookbookPagemodel task) {
		this.task =task;
	}
	
	
	@Override
	public void run() {
		try {
		
		logger.info("############开始解析每一个分页任务page_url："+task.getPage_url()+"#################");
		tDao.update(XiachufangCookbookPagemodel.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
		TaskToBi tobi = BIDataSource.getBiDataSource("xiachufang",task.getProject_code(), task.getDown_type());
		Dao  biDao = tobi.getBiDao();
		String page_url = task.getPage_url();
		String html=null,title=null,title_url=null,collect_num=null,support_number=null,author=null;
		String baseUrl = "http://www.xiachufang.com" ;
		try {
		    html = HttpBase.get(page_url, "utf-8").getResult();
		} catch (Exception e) {
			return ;
		}
		Document doc = Jsoup.parse(html);
		//http://www.xiachufang.com/recipe/89158/
		Elements elements = doc.select("div.recipes-280-full-width-list").select("li.pure-u");
		Element element =null;
		XiachufangAuthorCookbookSimple cookbookSimple = new XiachufangAuthorCookbookSimple();
		XiachufangCookbookmodel cookbookmodel = new XiachufangCookbookmodel();
		for (int i = 0; i < elements.size(); i++) {
			element = elements.get(i);
			title =element.select("p.name.ellipsis.red-font").text().trim() ;
			title_url = element.select("p.name.ellipsis.red-font").select("a").attr("href").trim() ;
			title_url = baseUrl.concat(title_url);
			String [] temp = element.select("div.stats.ellipsis").text().trim().split(" ");
			support_number = temp[0];
			collect_num = temp[2];
			author = element.select("a.gray-link").text();
			
			cookbookSimple.setAuthor_id(task.getAuthor_id());
			cookbookSimple.setCollect_num(collect_num);
			cookbookSimple.setCreate_date(new Date());
			cookbookSimple.setCreate_time(new Date());
			cookbookSimple.setDown_type(task.getDown_type());
			cookbookSimple.setNode(task.getNode());
			cookbookSimple.setProject_code(task.getProject_code());
			cookbookSimple.setRequest_id(task.getRequest_id());
			cookbookSimple.setSupport_number(support_number);
			cookbookSimple.setTask_id(task.getTask_id());
			cookbookSimple.setTitle(title);
			cookbookSimple.setTitle_url(title_url);
			cookbookSimple.setWebsite(task.getWebsite());
			cookbookSimple.setAuthor(author);
			
			try {
			biDao.fastInsert(cookbookSimple);
			} catch (Exception e) {
				logger.error("###############分页任务的某一产品插入BI表失败，失败title为："+title+"#############"+e);
			}
			//生成info任务
			cookbookmodel.setCreate_date(new Date());
			cookbookmodel.setDown_type(task.getDown_type());
			cookbookmodel.setEntrance(3);
			cookbookmodel.setNode(task.getNode());
			cookbookmodel.setProject_code(task.getProject_code());
			cookbookmodel.setRequest_id(task.getRequest_id());
			cookbookmodel.setStatus(0);
			cookbookmodel.setTask_id(task.getTask_id());
			cookbookmodel.setTitle_url(title_url);
			cookbookmodel.setWebsite(task.getWebsite());
			
			
			try {
				tDao.fastInsert(cookbookmodel);
			} catch (Exception e) {
				logger.error("###############"+"作者分类simple生成info任务失败，失败的title_url为："+title_url+"#############",e);
			}
			
			
			
		}
		
		} catch (Exception e) {
			logger.info("################分页任务解析失败，失败的page_url："+task.getPage_url()+"##################");
			tDao.update(XiachufangCookbookPagemodel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
		}
		
		tDao.update(XiachufangCookbookPagemodel.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
		logger.info("################分页任务解析成功，成功的page_url："+task.getPage_url()+"##################");
	}
	
	
}
