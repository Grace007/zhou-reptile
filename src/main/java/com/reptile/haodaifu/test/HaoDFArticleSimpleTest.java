package com.reptile.haodaifu.test;

import com.bds.base.http.HttpBase;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class HaoDFArticleSimpleTest {
	@Test
	public void testrun()
	{
		String baseUrl = "http://www.haodf.com/jibing/guanxinbing/wz_0_0_1.htm";
		String html="";
		String article_title="",article_time="",dname="",dposition="",dhospital="",ddepartment="",article_content="",article_commentnum="",article_url="",
				article_type="",article_readnum="",temp="",username="",article_id="",did="";
		Elements articleElements=null;
		Element articleElement=null,eleTemp=null;
		
		try {
			html = HttpBase.get(baseUrl, "utf-8").getResult();
		} catch (Exception e) {
			System.out.println("界面解析失败!");
			e.printStackTrace();
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
			
			article_readnum=articleElement.select("div.dochots").select("a.doc_reader").text();
			if (StringUtils.equals(article_type, "文字文章")) {
				article_url=articleElement.select("div.article_title").select("h2").select("a").attr("href");
			}else if (StringUtils.equals(article_type, "语音文章")) {
				article_url="http://m.haodf.com/touch/zhuanjiaguandian/"+username+"_"+article_id+".htm";
			}
			
			
			System.out.println("################################"+i);
			System.out.println(article_title);
			System.out.println(article_time);
			System.out.println(dname);
			System.out.println(dposition);
			System.out.println(dhospital);
			System.out.println(ddepartment);
			System.out.println(article_content);
			System.out.println(article_commentnum);
			System.out.println(article_type);
			System.out.println(article_readnum);
			System.out.println(article_url);
			System.out.println(username);
			System.out.println(article_id);
			System.out.println(did);
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
