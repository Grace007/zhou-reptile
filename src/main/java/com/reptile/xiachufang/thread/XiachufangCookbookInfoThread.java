package com.reptile.xiachufang.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.xiachufang.model.XiachufangCookbookInfo;
import com.reptile.xiachufang.model.job.XiachufangCommentmodel;
import com.reptile.xiachufang.model.job.XiachufangCookbookmodel;
import com.reptile.xiachufang.model.job.XiachufangSearchAuthorByIdmodel;
import com.reptile.xiachufang.util.EmojiFilterUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;

public class XiachufangCookbookInfoThread extends Thread {
	private static Logger logger = Logger.getLogger(XiachufangCookbookInfoThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private XiachufangCookbookmodel task;
	public  XiachufangCookbookInfoThread(XiachufangCookbookmodel task) {
		this.task =task;
	}
	
	@Override
	public void run() {
		try {
		
		logger.info("############开始解析每一个菜谱详情info任务，任务入口："+task.getEntrance()+"  request_id:"+task.getRequest_id()+"#################");
		tDao.update(XiachufangCookbookmodel.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
		TaskToBi tobi = BIDataSource.getBiDataSource("xiachufang",task.getProject_code(), task.getDown_type());
		Dao  biDao = tobi.getBiDao();
		String url=task.getTitle_url();
		String html=null;
		String title_id = task.getTitle_url().replaceAll("\\D", "");
		String cookbook=null;
		String category=null;
		String score=null;
		String support_number=null;
		String author_name=null;
		String author_url=null;
		String author_id=null;
		String cookbook_createdate=null;
		String collect_num=null;
		String material="";
		String content=null;
		String img_url="";
		String baseUrl ="http://www.xiachufang.com";
		String [] temp = null;
		Elements img_elements = null;
		Element img_element = null;
		XiachufangCookbookInfo cookbookInfo = new XiachufangCookbookInfo();
		
		try {
			html = HttpBase.get(url, "utf-8").getResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Document doc = Jsoup.parse(html);
		
		
		cookbook = doc.select("h1.page-title").text().trim();
		category = doc.select("div.recipe-tags.block.mb50").select("div.recipe-cats").select("a").text().trim();
		try {
			score = doc.select("div.stats.clearfix").select("div.score.float-left").select("span.number").text().trim();
		} catch (Exception e) {
			score =null;
		}
		
		try {
			support_number =doc.select("div.stats.clearfix").select("div.cooked.float-left").select("span.number").text().trim();
		} catch (Exception e) {
			support_number =null;
		}
		
		author_name=doc.select("div.author").select("a.avatar-link.avatar").select("span[itemprop=author]").text();
		author_url = doc.select("div.author").select("a.avatar-link.avatar").attr("href").trim();
		try {
			temp = author_url.split("/");
			author_url = baseUrl.concat(author_url);
			author_id = temp[2];
		} catch (Exception e) {
			author_url=null;
			author_id=null;
		}
		
		cookbook_createdate = doc.select("div.recipe-stats.block.normal-font.gray-font").select("div.time").select("span[itemprop=datePublished]").text().trim();

		try {
			collect_num = doc.select("div.recipe-stats.block.normal-font.gray-font").select("div.pv").text().replaceAll("\\D", "");
		} catch (Exception e) {
			collect_num =null;
		}
		try {
			Elements materials = doc.select("div.ings").select("table").select("tbody").select("tr");
			Element materialElement = null;
			for (int i = 0; i < materials.size(); i++) {
				materialElement = materials.get(i);
				material += materialElement.text()+";";
			}
			material = EmojiFilterUtils.removeImoji(material);
		} catch (Exception e) {
			material =null;
		}
		try {
			content =  doc.select("div.steps").text();
			content = EmojiFilterUtils.removeImoji(content);
		} catch (Exception e) {
			content =null;
		}
		try {
			img_elements = doc.select("div.steps").select("img");
			for (int i = 0; i < img_elements.size(); i++) {
				img_element = img_elements.get(i);
				img_url = img_url.concat("###"+img_element.attr("src"));
			}
		} catch (Exception e) {
			img_url =null;
		}
		
		
		cookbookInfo.setAuthor_id(author_id);
		cookbookInfo.setAuthor_name(author_name);
		cookbookInfo.setAuthor_url(author_url);
		cookbookInfo.setTitle_url(task.getTitle_url());
		cookbookInfo.setCreate_time(new Date());
		cookbookInfo.setCategory(category);
		cookbookInfo.setCollect_num(collect_num);
		cookbookInfo.setContent(content);
		cookbookInfo.setCookbook(cookbook);
		cookbookInfo.setCreate_date(new Date());
		cookbookInfo.setCookbook_createdate(cookbook_createdate);
		cookbookInfo.setDown_type(task.getDown_type());
		cookbookInfo.setEntrance(task.getEntrance());
		cookbookInfo.setImg_url(img_url);
		cookbookInfo.setKeyword(task.getKeyword());
		cookbookInfo.setMaterial(material);
		cookbookInfo.setNode(task.getNode());
		cookbookInfo.setRequest_id(task.getRequest_id());
		cookbookInfo.setScore(score);
		cookbookInfo.setSupport_number(support_number);
		cookbookInfo.setTask_id(task.getTask_id());
		cookbookInfo.setWebsite(task.getWebsite());
		
		try {
			biDao.fastInsert(cookbookInfo);
		} catch (Exception e) {
			logger.error("###########cookbookinfo插入Bi表失败，失败的入口为："+task.getEntrance()+"  title_url:"+task.getTitle_url()+"############",e);
			tDao.update(XiachufangCookbookmodel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			return ;
		}
		
		if(task.getEntrance() != 3){
		System.out.println("##############创建作者info任务开始###############");
		XiachufangSearchAuthorByIdmodel authorByIdmodel = new XiachufangSearchAuthorByIdmodel();
		
		authorByIdmodel.setAuthor_id(author_id);
		authorByIdmodel.setAuthor_name(author_name);
		authorByIdmodel.setAuthor_url(author_url);
		authorByIdmodel.setCreate_date(new Date());
		authorByIdmodel.setCreate_datetime(new Date());
		authorByIdmodel.setDown_type(task.getDown_type());
		authorByIdmodel.setNode(task.getNode());
		authorByIdmodel.setProject_code(task.getProject_code());
		authorByIdmodel.setRequest_id(task.getRequest_id());
		authorByIdmodel.setStatus(0);
		authorByIdmodel.setTask_id(task.getTask_id());
		authorByIdmodel.setWebsite(task.getWebsite());
		authorByIdmodel.setEntrance(task.getEntrance());
		
		try {
			tDao.fastInsert(authorByIdmodel);
		} catch (Exception e) {
			tDao.update(XiachufangSearchAuthorByIdmodel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
		}
		}
		System.out.println("##############自动生成评论任务开始,titlt_id:"+title_id+"##############");
		XiachufangCommentmodel commentmodel = new XiachufangCommentmodel();
		commentmodel.setCreate_date(new Date());
		commentmodel.setDown_type(task.getDown_type());
		commentmodel.setEntrance(task.getEntrance());
		commentmodel.setNode(task.getNode());
		commentmodel.setProject_code(task.getProject_code());
		commentmodel.setRequest_id(task.getRequest_id());
		commentmodel.setStatus(0);
		commentmodel.setTask_id(task.getTask_id());
		commentmodel.setWebsite(task.getWebsite());
		commentmodel.setTitle_url(task.getTitle_url());
		commentmodel.setTitle_id(title_id);
		try {
			tDao.fastInsert(commentmodel);
		} catch (Exception e) {
			logger.error("###########cookbookinfo生成comment任务失败，title_id:"+title_id+"  title_url:"+task.getTitle_url()+"############");
		}
		
		logger.info("###########cookbookinfo插入Bi表成功，成功的入口为："+task.getEntrance()+"  title_url:"+task.getTitle_url()+"############");
		tDao.update(XiachufangCookbookmodel.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));

		
		} catch (Exception e) {
			logger.error("###########cookbookinfo任务，失败的入口为："+task.getEntrance()+"  title_url:"+task.getTitle_url()+"############");
			tDao.update(XiachufangCookbookmodel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
		}
	}
	

}
