package com.reptile.haodaifu.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.reptile.haodaifu.job.HaoDFDoctorSimpleJob;
import com.reptile.haodaifu.job.HaoDFPageDoctorListJob;
import com.reptile.haodaifu.model.HaoDFDoctorSimple;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HaoDFDoctorSimpleThread extends Thread {
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(MyConstant.ZNode);
	private HaoDFPageDoctorListJob task;
	private Logger logger =Logger.getLogger(HaoDFDoctorSimpleThread.class);
	TaskToBi tobi = BIDataSource.getBiDataSource("doctor", "DOCTOR", "get_article_info");
	Dao biDao = tobi.getBiDao();
	public  HaoDFDoctorSimpleThread(Record task) {
		this.task = task.toPojo(HaoDFPageDoctorListJob.class);
	}
	
	@Override
	public void run() {
		try {
			mysqlDao.update(HaoDFPageDoctorListJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			
			String baseUrl =task.getPageurl();
			String html="";
			Elements doctorElements=null;
			Element doctorElement =null;
			String dname="",dposition="",dhospital="",recommendlevel="", twoweek_reply="",dskill=""
					,durl="",did="",dvote="",votetotal="",username="",vote="";
			HaoDFDoctorSimple simple = new HaoDFDoctorSimple();
			simple.setCreate_time(new Date());
			simple.setBig_categoryname(task.getBig_categoryname());
			simple.setMedian_categoryname(task.getMedian_categoryname());
			simple.setTiny_categoryname(task.getTiny_categoryname());
			simple.setSmall_categoryname(task.getSmall_categoryname());
			simple.setRequest_id(task.getRequest_id());
			simple.setTask_id(task.getTask_id());
			simple.setNode(task.getNode());
			
			HaoDFDoctorSimpleJob simpleJob = new HaoDFDoctorSimpleJob();
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
			doctorElements = doc.select("li.hp_doc_box_serviceStar");//.select("div.inside_box");
			
			for (int i = 0; i < doctorElements.size(); i++) {
				
				doctorElement = doctorElements.get(i).select("div.inside_box").select("div.lh180").first();
				dname = doctorElement.select("p").first().select("a").first().text();
				durl = doctorElement.select("p").first().select("a").first().attr("href");
				dposition = doctorElement.select("p").first().select("span").first().text();
				dhospital = doctorElement.select("p").first().select("a").get(1).text();
				recommendlevel = doctorElement.select("span.patient_recommend").text();
				vote = doctorElement.select("p").get(2).select("span").first().text();
				try {
					dvote=vote.substring(1,vote.indexOf("/")).replaceAll("\\D", "");
					votetotal=vote.substring(vote.indexOf("/")+1, vote.length()).replaceAll("\\D", "");
				} catch (Exception e) {
					dvote="";votetotal="";
				}
				try {
					twoweek_reply = doctorElement.select("p").get(2).select("span").get(1).text();
				} catch (Exception e) {
					twoweek_reply="0";
				}
				
				
				dskill = doctorElement.select("p").get(3).text();
				did=subString("com/doctor/", ".htm", durl);
				try {
					username=doctorElements.get(i).select("a.personweb-sickness-btn").first().attr("href");
					username=username.replaceAll("http://", "").replaceAll(".haodf.com/", "");
				} catch (Exception e) {
					username=null;
				}
				
				Pattern compile = Pattern.compile("\\d+\\.\\d+");
		        Matcher matcher = compile.matcher(recommendlevel);
		        matcher.find();
		        recommendlevel = matcher.group();
		        
				simpleJob.setDhospital(dhospital);
				simpleJob.setDname(dname);
				simpleJob.setDskill(dskill);
				simpleJob.setDurl(durl);
				simpleJob.setDvote(dvote);
				simpleJob.setDid(did);
				simpleJob.setRecommendlevel(recommendlevel);
				simpleJob.setStatus(0);
				simpleJob.setUsername(username);
				simpleJob.setTwoweek_reply(twoweek_reply);
				simpleJob.setDposition(dposition);
				try {
					mysqlDao.insert(simpleJob);
				} catch (Exception e) {
					logger.error("simplejob插入失败");
				}
				
				
				simple.setDhospital(dhospital);
				simple.setDname(dname);
				simple.setDskill(dskill);
				simple.setDurl(durl);
				simple.setDvote(dvote);
				simple.setVotetotal(votetotal);
				simple.setDid(did);
				simple.setRecommendlevel(recommendlevel);
				simple.setUsername(username);
				simple.setTwoweek_reply(twoweek_reply);
				simple.setDposition(dposition);
				
				try {
					biDao.insert(simple);
				} catch (Exception e) {
					logger.error("simple插入失败",e);
				}
				
			}
			
			mysqlDao.update(HaoDFPageDoctorListJob.class, Chain.make("status", 2), 
					Cnd.where("id", "=", task.getId()));
		} catch (Exception e) {
			mysqlDao.update(HaoDFPageDoctorListJob.class, Chain.make("status", 3), 
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
