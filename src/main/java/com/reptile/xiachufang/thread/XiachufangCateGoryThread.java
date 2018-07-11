package com.reptile.xiachufang.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.xiachufang.model.XiachufangCateGorySimple;
import com.reptile.xiachufang.model.job.XiachufangCateGorymodel;
import com.reptile.xiachufang.model.job.XiachufangCookbookmodel;
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

public class XiachufangCateGoryThread extends Thread {
	private static Logger logger = Logger.getLogger(XiachufangCateGoryThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private XiachufangCateGorymodel task;
	public  XiachufangCateGoryThread(XiachufangCateGorymodel task) {
		this.task =task;
	}

	@Override
	public void run() {
		try {
		logger.info("###################开始解析分类："+task.getCategory()+"######################");
		tDao.update(XiachufangCateGorymodel.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
		TaskToBi tobi = BIDataSource.getBiDataSource("xiachufang",task.getProject_code(), task.getDown_type());
		Dao  biDao = tobi.getBiDao();
		String html = null,title_url=null,title=null,material=null,score=null,support_number=null,author=null;
		Boolean temp = false;
		String url =task.getCategory_url();
		String urlBase = "http://www.xiachufang.com";
		int pageindex = 1;
		
		do {
			try {
				html = HttpBase.get(url, "utf-8").getResult();
			} catch (Exception e) {
				e.printStackTrace();
			}
			XiachufangCateGorySimple cateGorySimple = new XiachufangCateGorySimple();
			XiachufangCookbookmodel cookbookmodel = new XiachufangCookbookmodel();
			Document doc = Jsoup.parse(html);
			Elements elements = doc.select("div.normal-recipe-list").select("ul.list").select("li");
			Element element = null;
			System.out.println("本页有"+elements.size()+"条数据");
			
			
			
			if (elements.size()==0) {
				break;
			}
			for (int i = 0; i < elements.size(); i++) {
				element = elements.get(i);
				title_url = urlBase.concat(element.select("a").attr("href"));
				title = element.select("p.name").text().trim();
				title = EmojiFilterUtils.removeImoji(title);
				try {
					material = element.select("p.ing.ellipsis").text().trim();
					material = EmojiFilterUtils.removeImoji(material);
				} catch (Exception e) {
					material = null;
				}
				
				try {
					score = element.select("p.stats").select("span.score.bold.green-font").text().trim();
				} catch (Exception e) {
					score =null;
				}
				
				try {
					support_number = element.select("p.stats").select("span.bold.score").last().text().trim();  //.get(1).text().trim();
				} catch (Exception e) {
					support_number = null;
				}
				author = element.select("p.author").select("span.gray-font").text().trim();
				
				cateGorySimple.setAuthor(author);
				cateGorySimple.setCategory(task.getCategory());
				cateGorySimple.setCreate_date(new Date());
				cateGorySimple.setCreate_time(new Date());
				cateGorySimple.setDown_type(task.getDown_type());
				cateGorySimple.setMaterial(material);
				cateGorySimple.setNode(task.getNode());
				cateGorySimple.setPageindex(pageindex);
				cateGorySimple.setProject_code(task.getProject_code());
				cateGorySimple.setRequest_id(task.getRequest_id());
				cateGorySimple.setScore(score);
				cateGorySimple.setSupport_number(support_number);
				cateGorySimple.setTask_id(task.getTask_id());
				cateGorySimple.setTitle(title);
				cateGorySimple.setTitle_url(title_url);
				cateGorySimple.setWebsite(task.getWebsite());
				try {
					biDao.fastInsert(cateGorySimple);
				} catch (Exception e) {
					logger.error("###############"+"分类simple插入BI表失败，失败分类为："+task.getCategory()+"#############",e);
				}
				
				cookbookmodel.setCreate_date(new Date());
				cookbookmodel.setDown_type(task.getDown_type());
				cookbookmodel.setEntrance(1);
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
					logger.error("###############"+"分类simple生成info任务失败，失败的title_url为："+title_url+"#############",e);
				}
				
				
				
				
			}
			temp = doc.select("div.pager").select("a.next").isEmpty();
			if (temp) {
				// 没有下一页,循环出口
				break;
			} else {
				// 有下一页
				String next_url = urlBase.concat(doc.select("div.pager").select("a.next").attr("href"));
				url = next_url;
				pageindex++;
			}

		} while (true);
		logger.info("#############"+"分类Simple任务成功，分类条目为："+task.getCategory()+"#######################");
		tDao.update(XiachufangCateGorymodel.class, Chain.make("status", 2).add("page", pageindex-1), Cnd.where("id", "=", task.getId()));

		} catch (Exception e) {
			tDao.update(XiachufangCateGorymodel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.error("################"+"分类Simple任务失败,分类条目为："+task.getCategory()+"##################",e);
		}
	}
}






















	












