package com.reptile.haodaifu.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.reptile.haodaifu.job.HaoDFDoctorSimpleJob;
import com.reptile.haodaifu.job.HaoDFWendaJob;
import com.reptile.haodaifu.model.HaoDFDoctorInfo;
import com.reptile.server.MyConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.util.Date;

public class HaoDFDoctorInfoThread extends Thread {
	
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(MyConstant.ZNode);
	private HaoDFDoctorSimpleJob task;
	private Logger logger =Logger.getLogger(HaoDFDoctorInfoThread.class);
	TaskToBi tobi = BIDataSource.getBiDataSource("doctor", "DOCTOR", "get_article_info");
	Dao biDao = tobi.getBiDao();
	public  HaoDFDoctorInfoThread(Record task) {
		this.task = task.toPojo(HaoDFDoctorSimpleJob.class);
	}
	@Override
	public void run() {
		try {
			mysqlDao.update(HaoDFDoctorSimpleJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			
			String baseUrl =task.getDurl();
			String html="";
			String bp_top="",bp_doctor_about="",bp_doctor_servicestar="",bp_doctor_getvote="",bp_doctor_share="";
			String dname="",dposition="",dhospital="",allrecommendlevel="", twoweekreply="",dskill="",thankletternum="",presentnum="",experience=""
					,durl="",did="",vote="",homepage="",efficacy_satisfaction="",attitude_satisfaction="",patienttotal_help="",patienttwoweek="",clinical_experience=""
					,patienttotal_treatment="",patienttotal_followup="",patient_vote="",patient_comment_total="",patient_comment_experiencenum="";
			Element eleTemp=null;
			HaoDFWendaJob haoDFWendaJob = new HaoDFWendaJob();
			haoDFWendaJob.setCreate_time(new Date());
			haoDFWendaJob.setNode(task.getNode());
			haoDFWendaJob.setRequest_id(task.getRequest_id());
			haoDFWendaJob.setStatus(0);
			haoDFWendaJob.setTask_id(task.getTask_id());
			haoDFWendaJob.setTiny_categoryname(task.getTiny_categoryname());
			
			
			HaoDFDoctorInfo info = new HaoDFDoctorInfo();
			info.setCreate_time(new Date());
			info.setBig_categoryname(task.getBig_categoryname());
			info.setMedian_categoryname(task.getMedian_categoryname());
			info.setTiny_categoryname(task.getTiny_categoryname());
			info.setSmall_categoryname(task.getSmall_categoryname());
			info.setRequest_id(task.getRequest_id());
			info.setTask_id(task.getTask_id());
			info.setNode(task.getNode());
			info.setDhospital(task.getDhospital());
			info.setDid(task.getDid());
			info.setDname(task.getDname());
			info.setDurl(task.getDurl());
			info.setUsername(task.getUsername());
			info.setDhospital(task.getDhospital());
			info.setDposition(task.getDposition());
			
			try {
				html = HttpBase.get(baseUrl, "utf-8").getResult();
			} catch (Exception e) {
				logger.error("界面解析失败!",e);
			}
			Document doc = Jsoup.parse(html);
			
			bp_doctor_about = subString("{\"id\":\"bp_doctor_about\",\"content\":\"", ");</script>", doc.select("script").toString());
			bp_doctor_about="{\"id\":\"bp_doctor_about\",\"content\":\""+bp_doctor_about;
			JSONObject bp_doctor_about_json = new JSONObject(bp_doctor_about);
			Document doc_bp_doctor_about = Jsoup.parse(bp_doctor_about_json.get("content").toString());
			
			bp_doctor_servicestar = subString("{\"id\":\"bp_doctor_servicestar\",\"content\":\"", ");</script>", doc.select("script").toString());
			bp_doctor_servicestar="{\"id\":\"bp_doctor_servicestar\",\"content\":\""+bp_doctor_servicestar;
			JSONObject bp_doctor_servicestar_json = new JSONObject(bp_doctor_servicestar);
			Document doc_bp_doctor_servicestar = Jsoup.parse(bp_doctor_servicestar_json.get("content").toString());
			
			
			bp_doctor_getvote = subString("{\"id\":\"bp_doctor_getvote\",\"content\":\"", ");</script>", doc.select("script").toString());
			bp_doctor_getvote="{\"id\":\"bp_doctor_getvote\",\"content\":\""+bp_doctor_getvote;
			JSONObject bp_doctor_getvote_json = new JSONObject(bp_doctor_getvote);
			Document doc_bp_doctor_getvote = Jsoup.parse(bp_doctor_getvote_json.get("content").toString());
			
			bp_doctor_share = subString("{\"id\":\"bp_doctor_share\",\"content\":\"", ");</script>", doc.select("script").toString());
			bp_doctor_share="{\"id\":\"bp_doctor_share\",\"content\":\""+bp_doctor_share;
			JSONObject bp_doctor_share_json = new JSONObject(bp_doctor_share);
			Document doc_bp_doctor_share = Jsoup.parse(bp_doctor_share_json.get("content").toString());
			//System.out.println(bp_doctor_about_json.toString());
			//System.out.println(bp_doctor_about_json.get("content"));
			
			//System.out.println(doc_bp_doctor_about);
			dname = doc_bp_doctor_about.select("h1").first().select("a").text();
			//dposition = doc_bp_doctor_about.select("div.middletr").select("div.lt").select("table").first();
			eleTemp = doc_bp_doctor_about.select("div#full_DoctorSpecialize").first();
			try {
				eleTemp.select("span").remove();
				dskill = eleTemp.text();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			thankletternum = doc_bp_doctor_about.select("a.halfgxx_bgletter").text();
			presentnum = doc_bp_doctor_about.select("a.halfgxx_bgpresent").text();
			
			eleTemp = doc_bp_doctor_about.select("div#full").first();
			if (eleTemp == null) {
				experience=doc_bp_doctor_about.select("table").first().select("tr").last().select("td").last().text();
			}else {
				eleTemp.select("span").remove();
				experience = eleTemp.text();
			}
			
			
			homepage=doc_bp_doctor_about.select("div.doctor-home-page").select("a.blue").text();
			allrecommendlevel=doc_bp_doctor_about.select("p.r-p-l-score").text();
			efficacy_satisfaction=doc_bp_doctor_about.select("span.r-p-score").first().text();
			attitude_satisfaction=doc_bp_doctor_about.select("span.r-p-score").get(1).text();
			patienttotal_help=doc_bp_doctor_about.select("div.fl.score-part").select("p").first().select("span").get(1).text();
			patienttwoweek =doc_bp_doctor_about.select("div.fl.score-part").select("span").last().text();
			try {
				clinical_experience=doc_bp_doctor_servicestar.select("div.ltdiv").select("table").text();
				patienttotal_treatment=doc_bp_doctor_servicestar.select("div.rtdiv.rtdivgao").select("tbody").select("td").first().text().replaceAll("\\D", "");
				patienttotal_followup=doc_bp_doctor_servicestar.select("div.rtdiv.rtdivgao").select("tbody").select("td").get(1).text().replaceAll("\\D", "");
				} catch (Exception e) {
					clinical_experience=null;
					patienttotal_treatment=null;
					patienttotal_followup=null;
				}
			patient_vote=doc_bp_doctor_getvote.select("div.ltdiv").select("table").text();
			patient_comment_total=doc_bp_doctor_share.select("div#toptr_type_all").select("div.lt").select("div.nav").text();
			patient_comment_experiencenum=doc_bp_doctor_share.select("div#toptr_type_all").select("div.lt").select("div.nav2").first().text();
			
			info.setAttitude_satisfaction(attitude_satisfaction.replaceAll("\\D", "")+"%");
			info.setClinical_experience(clinical_experience);
			
			info.setEfficacy_satisfaction(efficacy_satisfaction.replaceAll("\\D", "")+"%");
			info.setExperience(experience);
			info.setHomepage(homepage);
			info.setPatient_comment_experiencenum(patient_comment_experiencenum.replaceAll("\\D", ""));
			
			info.setThankletternum(thankletternum.replaceAll("\\D", ""));
			info.setPresentnum(presentnum.replaceAll("\\D", ""));
			info.setPatienttwoweek(patienttwoweek.replaceAll("\\D", ""));
			info.setPatienttotal_treatment(patienttotal_treatment);
			info.setPatienttotal_help(patienttotal_help.replaceAll("\\D", ""));
			info.setPatienttotal_followup(patienttotal_followup);
			info.setPatient_vote(patient_vote);
			info.setPatient_comment_total(patient_comment_total.replaceAll("\\D", ""));
			info.setAllrecommendlevel(allrecommendlevel.replaceAll("\\D", ""));
			info.setDskill(dskill);
			//++++++++++
			String ddepartment="",dvisit_count="",darticle_count="",dopening_time="";
			String temp="";
			
			if (!StringUtils.isEmpty(homepage)) {
				try {
					html = HttpBase.get(homepage, "utf-8").getResult();
					Document doc_homepage = Jsoup.parse(html);
					temp=doc_homepage.select("div.clearfix.pt5.pb5").first().select("div.hh").text().trim();
					dhospital=temp.substring(0, temp.indexOf(" "));
					ddepartment=temp.substring(temp.indexOf(" ")+1, temp.length());
					dvisit_count=doc_homepage.select("ul.space_statistics").select("li").get(0).select("span.orange1.pr5").text();
					darticle_count=doc_homepage.select("ul.space_statistics").select("li").get(2).select("span.orange1.pr5").text();
					dopening_time=doc_homepage.select("ul.space_statistics").select("li").last().select("span.orange1.pr5").text();
					
					//http://xuehaihong.haodf.com/
					//http://xuehaihong.haodf.com/
					haoDFWendaJob.setUrl(homepage+"zixun/list.htm");
					try {
							mysqlDao.insert(haoDFWendaJob);
					} catch (Exception e) {
						logger.error("haoDFWendaJob插入失败");
					}
					
					info.setDhospital(dhospital);
					info.setDdepartment(ddepartment);
					info.setDvisit_count(dvisit_count.replaceAll("\\D", ""));
					info.setDarticle_count(darticle_count.replaceAll("\\D", ""));
					info.setDopening_time(dopening_time);
					
				}
				
			 catch (Exception e) {
				ddepartment="";
			}
			}
			try {
				biDao.insert(info);
			} catch (Exception e) {
				logger.error("info插入失败!");
			}
			
			
			
			mysqlDao.update(HaoDFDoctorSimpleJob.class, Chain.make("status", 2), 
					Cnd.where("id", "=", task.getId()));
		} catch (Exception e) {
			mysqlDao.update(HaoDFDoctorSimpleJob.class, Chain.make("status", 3), 
					Cnd.where("id", "=", task.getId()));
			logger.error("doctorinfo出错",e);
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
