package com.reptile.xiachufang.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.xiachufang.model.XiachufangKeywordSimple;
import com.reptile.xiachufang.model.job.XiachufangCookbookmodel;
import com.reptile.xiachufang.model.job.XiachufangKeywordmodel;
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

public class XiachufangKeywordThread extends Thread {
	private static Logger logger = Logger.getLogger(XiachufangKeywordThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private XiachufangKeywordmodel task;
	public  XiachufangKeywordThread(XiachufangKeywordmodel task) {
		this.task =task;
	}

	@Override
	public void run() {
		try {
		logger.info("###################开始解析关键字simple："+task.getKeyword()+"######################");
		tDao.update(XiachufangKeywordmodel.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
		TaskToBi tobi = BIDataSource.getBiDataSource("xiachufang",task.getProject_code(), task.getDown_type());
		Dao  biDao = tobi.getBiDao();
		String html = null,title_url=null,title=null,material=null,score=null,support_number=null,author=null;
		String keyword = task.getKeyword();
		Boolean temp = false;
		Elements elements = null;
		//http://www.xiachufang.com/search/?keyword=%E5%AE%B6%E4%B9%90&cat=1001&page=5
		String url ="http://www.xiachufang.com/search/?keyword="+java.net.URLEncoder.encode(task.getKeyword(),"utf-8");
		String urlBase = "http://www.xiachufang.com";
		int pageindex = 1;
		
		do {
			
			try {
				html = HttpBase.get(url, "utf-8").getResult();
			} catch (Exception e) {
				e.printStackTrace();
			}
			XiachufangKeywordSimple keywordSimple = new XiachufangKeywordSimple();
			XiachufangCookbookmodel cookbookmodel = new XiachufangCookbookmodel();
			Document doc = Jsoup.parse(html);
			try {
			    elements = doc.select("div.normal-recipe-list").select("ul.list").select("li");
			} catch (Exception e) {
				logger.info("关键字："+task.getKeyword()+"没有搜索结果");
				tDao.update(XiachufangKeywordmodel.class, Chain.make("status", 2).add("page", 0), Cnd.where("id", "=", task.getId()));
			}
			Element element = null;
			System.out.println("本页有"+elements.size()+"条数据");
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
				
				keywordSimple.setAuthor(author);
				keywordSimple.setKeyword(task.getKeyword());
				keywordSimple.setCreate_date(new Date());
				keywordSimple.setCreate_time(new Date());
				keywordSimple.setDown_type(task.getDown_type());
				keywordSimple.setMaterial(material);
				keywordSimple.setNode(task.getNode());
				keywordSimple.setPageindex(pageindex);
				keywordSimple.setProject_code(task.getProject_code());
				keywordSimple.setRequest_id(task.getRequest_id());
				keywordSimple.setScore(score);
				keywordSimple.setSupport_number(support_number);
				keywordSimple.setTask_id(task.getTask_id());
				keywordSimple.setTitle(title);
				keywordSimple.setTitle_url(title_url);
				keywordSimple.setWebsite(task.getWebsite());
				
				try {
					biDao.fastInsert(keywordSimple);
				} catch (Exception e) {
					logger.error("###############"+"关键字simple插入BI表失败，失败关键字为："+task.getKeyword()+"#############"+e);
				}
				
				cookbookmodel.setCreate_date(new Date());
				cookbookmodel.setDown_type(task.getDown_type());
				cookbookmodel.setEntrance(2);
				cookbookmodel.setNode(task.getNode());
				cookbookmodel.setProject_code(task.getProject_code());
				cookbookmodel.setRequest_id(task.getRequest_id());
				cookbookmodel.setStatus(0);
				cookbookmodel.setKeyword(keyword);
				cookbookmodel.setTask_id(task.getTask_id());
				cookbookmodel.setTitle_url(title_url);
				cookbookmodel.setWebsite(task.getWebsite());
				
				try {
					tDao.fastInsert(cookbookmodel);
				} catch (Exception e) {
					logger.error("###############"+"关键字生成info任务失败，失败的关键字"+task.getKeyword()+"，失败的title_url为："+title_url+"#############",e);
				}
				
			}
			temp = doc.select("div.pager").select("a.next").isEmpty();
			//System.out.println(temp);
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
		logger.info("#############"+"关键字Simple任务成功，关键字为："+task.getKeyword()+"#######################");
		tDao.update(XiachufangKeywordmodel.class, Chain.make("status", 2).add("page", pageindex), Cnd.where("id", "=", task.getId()));

		} catch (Exception e) {
			tDao.update(XiachufangKeywordmodel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.error("################"+"关键字Simple任务失败,关键字为："+task.getKeyword()+"##################",e);
		}
	}
}
