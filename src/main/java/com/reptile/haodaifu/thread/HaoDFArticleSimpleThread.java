package com.reptile.haodaifu.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.reptile.haodaifu.job.HaoDFArticleSimpleJob;
import com.reptile.haodaifu.job.HaoDFPageArticleListJob;
import com.reptile.haodaifu.model.HaoDFArticleSimple;
import com.reptile.server.MyConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.util.Date;

public class HaoDFArticleSimpleThread extends Thread {
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(MyConstant.ZNode);
	private HaoDFPageArticleListJob task;
	private Logger logger =Logger.getLogger(HaoDFArticleSimpleThread.class);
	TaskToBi tobi = BIDataSource.getBiDataSource("doctor", "DOCTOR", "get_article_info");
	Dao biDao = tobi.getBiDao();
	public  HaoDFArticleSimpleThread(Record task) {
		this.task = task.toPojo(HaoDFPageArticleListJob.class);
	}
	
	@Override
	public void run() {
		try {
			mysqlDao.update(HaoDFPageArticleListJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			String article_title="",article_time="",dname="",dposition="",dhospital="",ddepartment="",article_content="",article_commentnum="",article_url="",
					article_type="",article_readnum="",temp="",username="",article_id="",did="";
			Elements articleElements=null;
			Element articleElement=null,eleTemp=null;
			String baseUrl =task.getPageurl();
			String html="";
			
			HaoDFArticleSimple simple = new HaoDFArticleSimple();
			simple.setCreate_time(new Date());
			simple.setBig_categoryname(task.getBig_categoryname());
			simple.setMedian_categoryname(task.getMedian_categoryname());
			simple.setTiny_categoryname(task.getTiny_categoryname());
			simple.setSmall_categoryname(task.getSmall_categoryname());
			simple.setRequest_id(task.getRequest_id());
			simple.setTask_id(task.getTask_id());
			simple.setNode(task.getNode());
			
			HaoDFArticleSimpleJob simpleJob = new HaoDFArticleSimpleJob();
			simpleJob.setCreate_time(new Date());
			simpleJob.setBig_categoryname(task.getBig_categoryname());
			simpleJob.setMedian_categoryname(task.getMedian_categoryname());
			simpleJob.setTiny_categoryname(task.getTiny_categoryname());
			simpleJob.setSmall_categoryname(task.getSmall_categoryname());
			simpleJob.setRequest_id(task.getRequest_id());
			simpleJob.setTask_id(task.getTask_id());
			simpleJob.setNode(task.getNode());
			
			try {
				html = HttpBase.get(baseUrl, "utf-8").getResult();
			} catch (Exception e) {
				logger.error("界面解析失败!",e);
			}
			Document doc = Jsoup.parse(html);
			articleElements = doc.select("div.dis_article");
			for (int i = 0; i < articleElements.size(); i++) {
				articleElement = articleElements.get(i);
				did=articleElement.select("div.docmsg-center").select("a").first().attr("href");
				did=subString("/doctor/", ".htm", did);
				article_title = articleElement.select("div.article_title").select("h2").text();
				article_time = articleElement.select("div.article_title").select("span").text();
				dname=articleElement.select("div.docmsg-right").select("a").first().text();
				dposition=articleElement.select("div.docmsg-right").select("span").first().text();
				dhospital=articleElement.select("div.docmsg-right").select("span").get(1).text();
				ddepartment=articleElement.select("div.docmsg-right").select("span").get(2).text();
				eleTemp=articleElement.select("p.con").first();
				eleTemp.select("a").remove();
				article_content = eleTemp.text();
				article_commentnum=articleElement.select("div.dochots").select("a").get(1).text();
				if (articleElement.select("div.dochots").select("a").first().text().contains("收听")) {
					article_type ="语音文章";
				}
				else {
					article_type ="文字文章";
				}
				temp=articleElement.select("div.article_title").select("h2").select("a").attr("href");
				temp=subString("zhuanjiaguandian/", ".htm", temp);
				username=temp.substring(0, temp.indexOf("_"));
				article_id=temp.substring(temp.indexOf("_")+1, temp.length());
				
				article_readnum=articleElement.select("div.dochots").select("a.doc_reader").text().replaceAll("\\D", "");
				if (StringUtils.isEmpty(article_readnum)) {
					article_readnum="0";
				}
				if (StringUtils.equals(article_type, "文字文章")) {
					article_url=articleElement.select("div.article_title").select("h2").select("a").attr("href");
				}else if (StringUtils.equals(article_type, "语音文章")) {
					article_url="http://m.haodf.com/touch/zhuanjiaguandian/"+username+"_"+article_id+".htm";
				}
				
				simple.setArticle_commentnum(article_commentnum.replaceAll("\\D", ""));
				simple.setArticle_content(article_content);
				simple.setArticle_id(article_id);
				simple.setArticle_readnum(article_readnum);
				simple.setArticle_time(article_time.replaceAll("\\D", ""));
				simple.setArticle_title(article_title);
				simple.setArticle_type(article_type);
				simple.setArticle_url(article_url);
				simple.setDdepartment(ddepartment);
				simple.setDhospital(dhospital);
				simple.setDid(did);
				simple.setDname(dname);
				simple.setUsername(username);
				simple.setDposition(dposition);
				try {
					biDao.insert(simple);
				} catch (Exception e2) {
					logger.error("simple插入失败");
				}
				
				simpleJob.setArticle_commentnum(article_commentnum.replaceAll("\\D", ""));
				simpleJob.setArticle_id(article_id);
				simpleJob.setArticle_readnum(article_readnum);
				simpleJob.setArticle_time(article_time.replaceAll("\\D", ""));
				simpleJob.setArticle_title(article_title);
				simpleJob.setArticle_type(article_type);
				simpleJob.setArticle_url(article_url);
				simpleJob.setDdepartment(ddepartment);
				simpleJob.setDhospital(dhospital);
				simpleJob.setDid(did);
				simpleJob.setDname(dname);
				simpleJob.setUsername(username);
				simpleJob.setDposition(dposition);
				try {
					mysqlDao.insert(simpleJob);
				} catch (Exception e) {
					logger.error("simplejob插入失败");
				}
			
			
			
			
			}
			mysqlDao.update(HaoDFPageArticleListJob.class, Chain.make("status", 2), 
					Cnd.where("id", "=", task.getId()));
		} catch (Exception e) {
			mysqlDao.update(HaoDFPageArticleListJob.class, Chain.make("status", 3), 
					Cnd.where("id", "=", task.getId()));
			logger.error("doctorsimple出错",e);
		}
	}
		public static String subString(String begin, String end, String content) {
			if (StringUtils.isEmpty(content)) {
				return content;
			}
			int index_s = 0;
			if (begin != null) {
				index_s = content.indexOf(begin) + begin.length();
			}
			String _end = content.substring(index_s, content.length());
			int index_e = 0;

			if (end == null) {
				index_e = content.length();
			} else {
				index_e = _end.indexOf(end);
			}
			return _end.substring(0, index_e);
		}
		

	

}
