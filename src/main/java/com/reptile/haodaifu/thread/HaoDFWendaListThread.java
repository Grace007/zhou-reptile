package com.reptile.haodaifu.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.reptile.haodaifu.job.HaoDFWendaJob;
import com.reptile.haodaifu.job.HaoDFWendaSimpleJob;
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

public class HaoDFWendaListThread extends Thread {
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(MyConstant.ZNode);
	private HaoDFWendaJob task;
	private Logger logger =Logger.getLogger(HaoDFWendaListThread.class);
	public  HaoDFWendaListThread(Record task) {
		this.task = task.toPojo(HaoDFWendaJob.class);
	}
	
	@Override
	public void run() {
		try {
			//http://www.haodf.com/jibing/xiaoerganmao.htm
			//http://www.haodf.com/jibing/xiaoerganmao/wz_0_0_1.htm
			mysqlDao.update(HaoDFWendaJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			logger.info(task.getUrl());
			String html="",baseUrl="",disease="",datestr="";
			Long dateint=20170808L;
			Elements wendaElements=null;
			Element wendaElement =null;
			String wenda_url="",did="",total="";
			int pagenum=1;
			HaoDFWendaSimpleJob haoDFWendaSimpleJob = new HaoDFWendaSimpleJob();
			haoDFWendaSimpleJob.setCreate_time(new Date());
			haoDFWendaSimpleJob.setNode(task.getNode());
			haoDFWendaSimpleJob.setRequest_id(task.getRequest_id());
			haoDFWendaSimpleJob.setStatus(0);
			haoDFWendaSimpleJob.setTask_id(task.getTask_id());
			haoDFWendaSimpleJob.setTiny_categoryname(task.getTiny_categoryname());
			
			//http://cqx200000.haodf.com/zixun/list.htm
			//http://cqx200000.haodf.com/zixun/list.htm?type=&p=2
			baseUrl=task.getUrl()+"?type=&p=1";
			
			do {
				
			try {
				html = HttpBase.get(baseUrl, "utf-8").getResult();
			} catch (Exception e) {
			}
			Document doc = Jsoup.parse(html);
			did=doc.select("div.space_b_info_page.newsc").select("a").first().attr("href");
			//http://www.haodf.com/doctor/DE4r0Fy0C9LuSQPT0wEnC3viZLC3b3s8R.htm
			did=subString("com/doctor/", ".htm", did);
			wendaElements = doc.select("div.zixun_list").select("tr");//.select("div.inside_box");
			total=doc.select("span.f14.orange1").text();
			logger.info("########当前页数:"+pagenum+"    页大小:"+wendaElements.size());
			for (int i = 1; i < wendaElements.size(); i++) {
				wendaElement=wendaElements.get(i);
				try {
					datestr = wendaElement.select("span.gray3").text().replaceAll("\\D", "");
					dateint = new Long(datestr);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				wenda_url=wendaElement.select("td").get(2).select("a").attr("href");
				disease = wendaElement.select("td").get(3).text();
				haoDFWendaSimpleJob.setWenda_url(wenda_url);
				haoDFWendaSimpleJob.setDisease(disease);
				haoDFWendaSimpleJob.setDid(did);
				haoDFWendaSimpleJob.setPagenum(pagenum);
				haoDFWendaSimpleJob.setTotal(total);
				
				try {
					mysqlDao.insert(haoDFWendaSimpleJob);
				} catch (Exception e) {
					logger.error("wendasimple数组重复");
				}
				
			}
			
			if (wendaElements.size()<26 || dateint <=20160825 || pagenum >= 50 ) {
				break;
			}
			
			pagenum++;
			baseUrl=baseUrl.replace("htm?type=&p="+String.valueOf(pagenum-1), "htm?type=&p="+String.valueOf(pagenum));

			} while (true);
			
			mysqlDao.update(HaoDFWendaJob.class, Chain.make("status", 2).add("total", total).add("pagecount", pagenum), Cnd.where("id", "=", task.getId()));
		} catch (Exception e) {
			mysqlDao.update(HaoDFWendaJob.class, Chain.make("status", 3), 
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
