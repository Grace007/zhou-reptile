package com.reptile.haodaifu.test;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.bds.base.http.HttpBase;

public class HaoDFInteractionTest {
	
	@Test
	public void testRun() {
		String baseUrl = "http://so.haodf.com/index/search?kw=%E8%82%9D%E7%A1%AC%E5%8C%96&page=1";
		String html="";
		
		String title="",ask="",answer="",doctor="",disease="",talknum="",time="",url="",username="",iid="";
		Elements wenzhens=null;
		Element eleTemp=null,wenzhen=null;
		try {
			html = HttpBase.get(baseUrl, "utf-8").getResult();
		} catch (Exception e) {
			System.out.println("界面解析失败!");
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(html);
		wenzhens=doc.select("div.sc-wenzhen");
		System.out.println(wenzhens.size());
		for (int i = 0; i < wenzhens.size(); i++) {
			wenzhen = wenzhens.get(i);
			title=wenzhen.select("div.sc-w-title").text();
			ask=wenzhen.select("div.sc-w-ask").text();
			doctor=wenzhen.select("div.sc-w-answer").select("span.wc-w-a-doc").text();
			answer=wenzhen.select("div.sc-w-answer").select("span").last().text();
			disease=wenzhen.select("div.sc-w-footer").select("span.sc-w-f-disease").text();
			talknum=wenzhen.select("div.sc-w-footer").select("span.sc-w-f-talk").text();
			time=wenzhen.select("div.sc-w-footer").select("span.sc-w-f-time").text();
			url=wenzhen.select("div.sc-w-title").select("a.sc-w-title-a.a-title").first().attr("href");
			username=subString("/wenda/", "_g_", url);
			iid=subString("_g_", ".htm", url);
			
			System.out.println("####################"+i);
			System.out.println(title);
			System.out.println(ask);
			System.out.println(doctor);
			System.out.println(answer);
			System.out.println(disease);
			System.out.println(talknum);
			System.out.println(time);
			System.out.println(url);
			System.out.println(username);
			System.out.println(iid);
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
