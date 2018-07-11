package com.reptile.haodaifu.test;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.bds.base.http.HttpBase;

public class HaoDFDoctorInfoTest {
	@Test
	public void test() throws JSONException{
		//String baseUrl = "http://www.haodf.com/doctor/DE4r0BCkuHzdewUYZNNi3poYKbews.htm";
		//String baseUrl="http://www.haodf.com/doctor/DE4r0eJWGqZNwlG0J35FP0G6uJ8FLja7.htm";
		//String baseUrl="http://www.haodf.com/doctor/DE4r0BCkuHzdeibXzekTMFHdvkQ4C.htm";
		String baseUrl = "http://www.haodf.com/doctor/DE4r0Fy0C9LuZ0wmH6TEgmoRNvXH4DNxe.htm";
		String html="";
		String bp_top="",bp_doctor_about="",bp_doctor_servicestar="",bp_doctor_getvote="",bp_doctor_share="";
		String dname="",dposition="",dhospital="",allrecommendlevel="", twoweekreply="",dskill="",thankletternum="",presentnum="",experience=""
				,durl="",did="",vote="",homepage="",efficacy_satisfaction="",attitude_satisfaction="",patienttotal_help="",patienttwoweek="",clinical_experience=""
				,patienttotal_treatment="",patienttotal_followup="",patient_vote="",patient_comment_total="",patient_comment_experiencenum="";
		Element eleTemp=null;
		String ddepartment="",dvisit_count="",darticle_count="",dopening_time="";
		String temp="";
		try {
			html = HttpBase.get(baseUrl, "utf-8").getResult();
		} catch (Exception e) {
			System.out.println("界面解析失败!");
			e.printStackTrace();
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
		eleTemp.select("span").remove();
		dskill = eleTemp.text();
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
		patienttotal_treatment=doc_bp_doctor_servicestar.select("div.rtdiv.rtdivgao").select("tbody").select("td").first().text();
		patienttotal_followup=doc_bp_doctor_servicestar.select("div.rtdiv.rtdivgao").select("tbody").select("td").get(1).text();
		} catch (Exception e) {
			clinical_experience=null;
			patienttotal_treatment=null;
			patienttotal_followup=null;
		}
		patient_vote=doc_bp_doctor_getvote.select("div.ltdiv").select("table").text();
		patient_comment_total=doc_bp_doctor_share.select("div#toptr_type_all").select("div.lt").select("div.nav").text();
		patient_comment_experiencenum=doc_bp_doctor_share.select("div#toptr_type_all").select("div.lt").select("div.nav2").first().text();
		
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
				
			}
			
		 catch (Exception e) {
			ddepartment="";
		}
		}
		
		
		
		System.out.println(dname.replaceAll("", ""));
		System.out.println(dskill);
		//System.out.println(eleTemp);
		System.out.println(thankletternum.replaceAll("\\D", ""));
		System.out.println(presentnum);
		System.out.println(experience);
		System.out.println(homepage);
		System.out.println(allrecommendlevel);
		System.out.println(efficacy_satisfaction);
		System.out.println(attitude_satisfaction);
		System.out.println(patienttotal_help);
		System.out.println(patienttwoweek);
		System.out.println(clinical_experience);
		System.out.println(patienttotal_treatment);
		System.out.println(patienttotal_followup);
		System.out.println(patient_vote);
		System.out.println(patient_comment_total);
		System.out.println(patient_comment_experiencenum);
		
		
		System.out.println(temp);
		System.out.println(dhospital);
		System.out.println(ddepartment);
		System.out.println(dvisit_count);
		System.out.println(darticle_count);
		System.out.println(dopening_time);
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
