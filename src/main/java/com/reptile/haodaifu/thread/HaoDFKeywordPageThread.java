package com.reptile.haodaifu.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.reptile.haodaifu.job.HaoDFKeywordJob;
import com.reptile.haodaifu.job.HaoDFKeywordSimpleJob;
import com.reptile.haodaifu.model.HaoDFKeywordSimple;
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

public class HaoDFKeywordPageThread extends Thread {
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(MyConstant.ZNode);
	private HaoDFKeywordJob task;
	private Logger logger =Logger.getLogger(HaoDFKeywordPageThread.class);
	TaskToBi tobi = BIDataSource.getBiDataSource("doctor", "DOCTOR", "get_article_info");
	Dao biDao = tobi.getBiDao();
	public  HaoDFKeywordPageThread(Record task) {
		this.task = task.toPojo(HaoDFKeywordJob.class);
	}
	

	@Override
	public void run() {
		try {
			//http://www.haodf.com/jibing/xiaoerganmao.htm
			//http://www.haodf.com/jibing/xiaoerganmao/wz_0_0_1.htm
			mysqlDao.update(HaoDFKeywordJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			int pagenum=1;
			String title="",ask="",answer="",doctor="",disease="",talknum="",time="",url="",username="",iid="";
			Elements wenzhens=null;
			Element eleTemp=null,wenzhen=null;
			String baseUrl = "http://so.haodf.com/index/search?kw="+java.net.URLEncoder.encode(task.getKeyword(), "utf-8")+"&page=1";
			String html="";
			HaoDFKeywordSimpleJob haoDFKeywordSimpleJob = new HaoDFKeywordSimpleJob();
			haoDFKeywordSimpleJob.setCreate_time(new Date());
			haoDFKeywordSimpleJob.setNode(task.getNode());
			haoDFKeywordSimpleJob.setRequest_id(task.getRequest_id());
			haoDFKeywordSimpleJob.setStatus(0);
			haoDFKeywordSimpleJob.setTask_id(task.getTask_id());
			haoDFKeywordSimpleJob.setKeyword(task.getKeyword());
			HaoDFKeywordSimple haoDFKeywordSimple = new HaoDFKeywordSimple();
			haoDFKeywordSimple.setCreate_time(new Date());
			haoDFKeywordSimple.setNode(task.getNode());
			haoDFKeywordSimple.setRequest_id(task.getRequest_id());
			haoDFKeywordSimple.setStatus(0);
			haoDFKeywordSimple.setTask_id(task.getTask_id());
			haoDFKeywordSimple.setKeyword(task.getKeyword());
			
			do {
				
				try {
					html = HttpBase.get(baseUrl, "utf-8").getResult();
				} catch (Exception e) {
					logger.info("界面解析失败!",e);
				}
				Document doc = Jsoup.parse(html);
				wenzhens=doc.select("div.sc-wenzhen");
				logger.info("####"+pagenum+"####"+wenzhens.size());
				for (int i = 0; i < wenzhens.size(); i++) {
					wenzhen = wenzhens.get(i);
					title=wenzhen.select("div.sc-w-title").text();
					try {
						ask=wenzhen.select("div.sc-w-ask").text();
					} catch (Exception e) {
						ask="";
					}
					
					doctor=wenzhen.select("div.sc-w-answer").select("span.wc-w-a-doc").text();
					try {
						answer=wenzhen.select("div.sc-w-answer").select("span").last().text();
					} catch (Exception e) {
						answer="";
					}
					try {
						disease=wenzhen.select("div.sc-w-footer").select("span.sc-w-f-disease").text().replace("疾病:", "");
					} catch (Exception e) {
						disease="";
					}
					try {
						talknum=wenzhen.select("div.sc-w-footer").select("span.sc-w-f-talk").text().replaceAll("\\D", "");
					} catch (Exception e) {
						talknum="";
					}
					time=wenzhen.select("div.sc-w-footer").select("span.sc-w-f-time").text();
					url=wenzhen.select("div.sc-w-title").select("a.sc-w-title-a.a-title").first().attr("href");
					username=subString("/wenda/", "_g_", url);
					iid=subString("_g_", ".htm", url);
					
					haoDFKeywordSimpleJob.setDisease(disease);
					haoDFKeywordSimpleJob.setDoctor(doctor);
					haoDFKeywordSimpleJob.setIid(iid);
					haoDFKeywordSimpleJob.setTalknum(talknum);
					haoDFKeywordSimpleJob.setTime(time);
					haoDFKeywordSimpleJob.setUsername(username);
					haoDFKeywordSimpleJob.setUrl(url);
					haoDFKeywordSimpleJob.setTitle(title);
					try {
						mysqlDao.insert(haoDFKeywordSimpleJob);
					} catch (Exception e) {
					}
					haoDFKeywordSimple.setAnswer(answer);
					haoDFKeywordSimple.setAsk(ask);
					haoDFKeywordSimple.setDisease(disease);
					haoDFKeywordSimple.setDoctor(doctor);
					haoDFKeywordSimple.setIid(iid);
					haoDFKeywordSimple.setTalknum(talknum);
					haoDFKeywordSimple.setTime(time);
					haoDFKeywordSimple.setUsername(username);
					haoDFKeywordSimple.setUrl(url);
					haoDFKeywordSimple.setTitle(title);
					try {
						biDao.insert(haoDFKeywordSimple);
					} catch (Exception e) {
					}
					
					
				}
				
				if (pagenum ==100) {
					break;
				}
				pagenum++;
				baseUrl=baseUrl.replace("&page="+String.valueOf(pagenum-1), "&page="+String.valueOf(pagenum));

				} while (true);
				
				mysqlDao.update(HaoDFKeywordJob.class, Chain.make("status", 2).add("pagecount", pagenum), Cnd.where("id", "=", task.getId()));
			} catch (Exception e) {
				mysqlDao.update(HaoDFKeywordJob.class, Chain.make("status", 3), 
						Cnd.where("id", "=", task.getId()));
				logger.error("wenda任务分页出错",e);
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
