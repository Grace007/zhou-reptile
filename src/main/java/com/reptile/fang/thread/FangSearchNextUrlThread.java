package com.reptile.fang.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.fang.model.FangPostInfo;
import com.reptile.fang.model.job.FangSearchNextUrlTaskModel;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FangSearchNextUrlThread extends Thread {
	private static Logger logger = Logger.getLogger(FangSearchNextUrlThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private FangSearchNextUrlTaskModel task;
	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public FangSearchNextUrlThread(FangSearchNextUrlTaskModel task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {
			tDao.update(FangSearchNextUrlTaskModel.class, Chain.make("status", 1),
					Cnd.where("id", "=", task.getId()));
			
			TaskToBi tobi = BIDataSource.getBiDataSource("fang", task.getProject_code(), task.getDown_type());
			Dao biDao = tobi.getBiDao();
			String url = task.getUrl();
			String html = HttpBase.get(url, "utf-8").getResult();
			Document doc = Jsoup.parse(html);
			
			String[] onclickText = null;
			String unfinishedTitle="",title="",unfinishedDatetime="",titleUrl="",message="",
			author_name="",forum_url="",thread_id="",author_url="";
			String engine="http://search.fang.com/bbs/";
			Date date_posted=null;
			
			
			Elements elements =doc.select("div#content").select("div.result");
			FangPostInfo fangInfo  = new FangPostInfo();
			Element element = null;
			for (int i = 0; i < elements.size(); i++) {
				element = elements.get(i);
				if(element.select("div.attPic").isEmpty()){
					//System.out.println(i+".没有图片的标题");
					//截取标题和发帖时间
					/*onclickText = element.select("div.postTitle").select("a").attr("onclick").split("'");
					unfinishedTitle =onclickText[1].trim();
					title = unfinishedTitle.substring(1, unfinishedTitle.length()-1);
					
					unfinishedDatetime = onclickText[2].trim();
					date_posted =df1.parse(unfinishedDatetime.substring(1, unfinishedDatetime.length()-3));*/
					onclickText = element.select("div.postTitle").select("a").attr("onclick").split("'");
					title =onclickText[1].trim();
					
					unfinishedDatetime = onclickText[onclickText.length-2].trim();
					date_posted =df1.parse(unfinishedDatetime);
					//得到titleUrl和简介内容
					titleUrl = element.select("div.postTitle").select("a").attr("href");
					message = element.select("div.postSource").select("p").get(0).text();
					//得到作者名字
					author_name = element.select("div.postSource").select("p").get(1).select("a").get(0).text();
					//得到子论坛连接
					forum_url = element.select("div.postSource").select("p").get(1).select("a").get(1).attr("href");
					//
					thread_id = titleUrl.substring(titleUrl.lastIndexOf("_")+1,titleUrl.lastIndexOf("."));
					author_url = element.select("div.postSource").select("p").get(1).select("a").get(0).attr("href");
					fangInfo.setArticle_url(titleUrl);
					fangInfo.setAuthor_name(author_name);
					fangInfo.setEngine(engine);
					fangInfo.setRequest_id(task.getRequest_id());
					fangInfo.setCreate_time(new Date());
					fangInfo.setTitle(title);
					fangInfo.setDate_posted(date_posted);
					fangInfo.setMessage(message);
					fangInfo.setForum_url(forum_url);
					fangInfo.setAuthor_url(author_url);
					fangInfo.setThread_id(thread_id);
					
					try {
						biDao.fastInsert(fangInfo);
					} catch (Exception e) {
						logger.error("=====" + "fang往BI库灌入失败，失败的Article_url为：" + fangInfo.getArticle_url() + "=====");
					}

				}
				else{
					//System.out.println(i+".有图片的标题");
					//截取标题和发帖时间
					/*onclickText = element.select("div.postTitle").select("a").attr("onclick").split(",");
					unfinishedTitle =onclickText[1].trim();
					title = unfinishedTitle.substring(1, unfinishedTitle.length()-1);
					
					unfinishedDatetime = onclickText[2].trim();
					date_posted =df1.parse(unfinishedDatetime.substring(1, unfinishedDatetime.length()-3));*/
					onclickText = element.select("div.postTitle").select("a").attr("onclick").split("'");
					title =onclickText[1].trim();
					
					unfinishedDatetime = onclickText[onclickText.length-2].trim();
					date_posted =df1.parse(unfinishedDatetime);
					//得到titleUrl和简介内容
					titleUrl = element.select("div.postTitle").select("a").attr("href");
					message = element.select("div.postSource2").select("p").get(0).text();
					//得到作者名字
					author_name = element.select("div.postSource2").select("p").get(1).select("a").get(0).text();
					//得到子论坛连接
					forum_url = element.select("div.postSource2").select("p").get(1).select("a").get(1).attr("href");
					author_url = element.select("div.postSource2").select("p").get(1).select("a").get(0).attr("href");
					thread_id = titleUrl.substring(titleUrl.lastIndexOf("_")+1,titleUrl.lastIndexOf("."));
					fangInfo.setArticle_url(titleUrl);
					fangInfo.setAuthor_name(author_name);
					fangInfo.setEngine(engine);
					fangInfo.setRequest_id(task.getRequest_id());
					fangInfo.setCreate_time(new Date());
					fangInfo.setTitle(title);
					fangInfo.setDate_posted(date_posted);
					fangInfo.setMessage(message);
					fangInfo.setForum_url(forum_url);
					fangInfo.setAuthor_url(author_url);
					fangInfo.setThread_id(thread_id);
					try {
						biDao.fastInsert(fangInfo);
					} catch (Exception e) {
						logger.error("=====" + "fang往BI库灌入失败，失败的Article_url为：" + fangInfo.getArticle_url() + "=====");
					}
				}
			
			
			}
			
			tDao.update(FangSearchNextUrlTaskModel.class, Chain.make("status", 2),
					Cnd.where("id", "=", task.getId()));
			logger.info("==============fang解析产品任务时成功，URL为：" + task.getUrl() + "==============");
			
		} catch (Exception e) {
			tDao.update(FangSearchNextUrlTaskModel.class, Chain.make("status", 3),
					Cnd.where("id", "=", task.getId()));
			logger.error("==============fang解析产品任务时失败，URL为：" + task.getUrl() + "==============",e);
		}
		
	}
	
}
