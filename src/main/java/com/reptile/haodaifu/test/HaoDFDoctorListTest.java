package com.reptile.haodaifu.test;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.bds.base.http.HttpBase;

public class HaoDFDoctorListTest {
	
	@Test
	public void testRun(){
		String baseUrl ="http://www.haodf.com/jibing/xiaoerganmao/daifu_17_all_all_all_all.htm";
		//String baseUrl = "http://www.haodf.com/jibing/xiaoerganmao/daifu_1_all_all_all_all.htm";
		String html="";
		Elements doctorElements=null;
		Element doctorElement =null;
		String dname="",dposition="",dhospital="",recommendlevel="", twoweek_reply="",dskill=""
				,durl="",did="",vote="",username="";
		try {
			html = HttpBase.get(baseUrl, "utf-8").getResult();
		} catch (Exception e) {
			System.out.println("界面解析失败!");
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(html);
		doctorElements = doc.select("li.hp_doc_box_serviceStar");//.select("div.inside_box");
		System.out.println(doctorElements.size());
		
		for (int i = 0; i < doctorElements.size(); i++) {
			
			doctorElement = doctorElements.get(i).select("div.inside_box").select("div.lh180").first();
			dname = doctorElement.select("p").first().select("a").first().text();
			durl = doctorElement.select("p").first().select("a").first().attr("href");
			dposition = doctorElement.select("p").first().select("span").first().text();
			dhospital = doctorElement.select("p").first().select("a").get(1).text();
			recommendlevel = doctorElement.select("span.patient_recommend").text();
			vote = doctorElement.select("p").get(2).select("span").first().text();
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
			
			System.out.println("#############################"+i);
			System.out.println(dname);
			System.out.println(did);
			System.out.println(dposition);
			System.out.println(dhospital);
			System.out.println(recommendlevel);
			System.out.println(vote);
			System.out.println(twoweek_reply);
			System.out.println(dskill);
			System.out.println(durl);
			System.out.println(username);
			
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
