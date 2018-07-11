package com.reptile.haodaifu.test;

import com.bds.base.http.HttpBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class HaoDFwendaZixunTest {
	@Test
	public void testRun(){
		
		String baseUrl ="http://gaoyinjie.haodf.com/zixun/list.htm";
		//String baseUrl = "http://www.haodf.com/jibing/xiaoerganmao/daifu_1_all_all_all_all.htm";
		String html="",datestr="";
		Long dateint=0L;
		Elements wendaElements=null;
		Element wendaElement =null;
		String patientname="",wenda_url="",wenda_title="",disease="";
		try {
			html = HttpBase.get(baseUrl, "utf-8").getResult();
		} catch (Exception e) {
			System.out.println("界面解析失败!");
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(html);
		wendaElements = doc.select("div.zixun_list").select("tr");//.select("div.inside_box");
		System.out.println(wendaElements.size());
		for (int i = 1; i < wendaElements.size(); i++) {
			wendaElement=wendaElements.get(i);
			datestr = wendaElement.select("span.gray3").text().replaceAll("\\D", "");
			dateint = new Long(datestr);
			patientname=wendaElement.select("td").get(1).text();
			wenda_url=wendaElement.select("td").get(2).select("a").attr("href");
			wenda_title=wendaElement.select("td").get(2).select("a").text();
			disease = wendaElement.select("td").get(3).text();
			
			System.out.println("################i");
			System.out.println(patientname);
			System.out.println(wenda_url);
			System.out.println(wenda_title);
			System.out.println(disease);
			System.out.println(datestr);
			System.out.println(dateint<=20160801);
			
			
		}
		
		
		
		
		
	}

}
