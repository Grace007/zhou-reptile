package com.reptile.haodaifu.test;

import com.bds.base.http.HttpBase;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class HaoDFWendaInfoTest {
	@Test
	public void testRun(){
		//String baseUrl ="http://www.haodf.com/wenda/gusong_g_5450887261.htm";
		String baseUrl = "http://www.haodf.com/wenda/baichujie_g_5426760457.htm";
		String html="";
		Elements wendaElements=null;
		Element wendaElement =null;
		String spokesman="",wenda_url="",wenda_title="",wenda_time="",patient_status="",wenda_content="",wenda_type=""
				;
		int rangking=1,pagenum=1,pagetotal=1;
		
		baseUrl+="?p=1";
		do {
		
		
		try {
			html = HttpBase.get(baseUrl, "utf-8").getResult();
		} catch (Exception e) {
			System.out.println("界面解析失败!");
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(html);
		try {
		pagetotal=new Integer(doc.select("div.page_turn").select("a.page_turn_a").last().text().replaceAll("\\D", ""));
		} catch (Exception e) {
			pagetotal=1;
		}
		System.out.println("pagetotal:"+pagetotal);
		wendaElements = doc.select("div.zzx_yh_stream");
		wenda_title=doc.select("div.clearfix.zzx_yh_h1").text();
		System.out.println(baseUrl);
		System.out.println(wendaElements.size());
		for (int i = 0; i < wendaElements.size(); i++) {
			wendaElement = wendaElements.get(i);
			if (!StringUtils.equals(wendaElement.select("div.yh_r_t_icon").select("img").first().attr("src"), "http://i1.hdfimg.com/doctorzone/images/yi.png") ) {
				spokesman=wendaElement.select("div.stream_yh_left").select("div.yh_l_huan").text();
				wenda_time=wendaElement.select("div.stream_yh_left").select("div.yh_l_times").text();
				patient_status=wendaElement.select("div.stream_yh_left").select("div.yh_l_states").select("span").text();
				wenda_content=wendaElement.select("div.stream_yh_right").select("div.h_s_cons").text();
				if (StringUtils.isEmpty(wenda_content)) {
					 wenda_content=wendaElement.select("div.stream_yh_right").select("div.h_s_info_cons").text();
				}
				wenda_type="病人发言";
			}
			else {
				
				spokesman=wendaElement.select("div.yh_l_doctor").select("span").text();
				wenda_content=wendaElement.select("div.stream_yh_right").select("h3.h_s_cons_title").text();
				wenda_time=wendaElement.select("div.stream_yh_left").select("div.yh_l_times").text();
				
				wenda_type="医生发言";
			}
			
			
			
			
			
			
			System.out.println("################i");
			System.out.println(spokesman);
			System.out.println(wenda_time);
			System.out.println(patient_status);
			System.out.println(wenda_content);
			
			
			spokesman="";wenda_url="";wenda_title="";wenda_time="";patient_status="";wenda_content="";wenda_type="";
		}
		
		if (wendaElements.size()<20 || pagenum <=pagetotal) {
			break;
		}
		pagenum++;
		baseUrl=baseUrl.replace("htm?p="+String.valueOf(pagenum-1), "htm?p="+String.valueOf(pagenum));
		
		} while (true);
		
		
	}

}
