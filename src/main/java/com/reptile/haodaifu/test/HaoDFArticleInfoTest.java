package com.reptile.haodaifu.test;

import com.bds.base.http.HttpBase;
import com.bds.base.http.HttpsBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

public class HaoDFArticleInfoTest {
	@Test
	public void testrun(){
		//String baseUrl = "http://www.haodf.com/zhuanjiaguandian/lengxy2013_5417565168.htm";
		String baseUrl = "http://m.haodf.com/touch/zhuanjiaguandian/doctorwq_5431028320.htm";
		String article_title="",article_time="",dname="",dposition="",dhospital="",ddepartment="",article_contentnum="",article_commentnum="",article_url="",
				article_type="",article_readnum="",temp="",username="",article_id="",did="";
		String article_category="",publisher="",article_content="",article_img="",comment_url="",comment_html="",comment_time="",comment_user="",comment_content="",article_duration="";
		Elements imgElements=null,commentElements=null;
		Element commentElement=null,eleTemp=null;
		//String baseUrl="http://www.haodf.com/api/thesis/ajaxgethits";
		if (baseUrl.contains("touch")) {
			String html="";
			try {
				html = HttpsBase.get(baseUrl, "utf-8").getResult();
				
			} catch (Exception e) {
				System.out.println("界面解析失败!");
				e.printStackTrace();
			}
			System.out.println(html + "222");
			Document doc = Jsoup.parse(html);
			article_title=doc.select("h1.d-title").text();
			article_readnum=doc.select("section.detail").select("span.d-w-number").text();
			article_duration=doc.select("section.detail").select("span.d-w-time").text();
			article_time=doc.select("section.detail").select("span.d-w-date").text();
			article_content=doc.select("p.d-recommend").text()+doc.select("ul.d-voice").text();
			
			
			
			
			System.out.println(article_title);
			System.out.println(article_readnum);
			System.out.println(article_duration);
			System.out.println(article_time);
			System.out.println(article_content);
			
			
		}
		else {
			String html="";
			try {
				html = HttpBase.get(baseUrl, "utf-8").getResult();
			} catch (Exception e) {
				System.out.println("界面解析失败!");
				e.printStackTrace();
			}
			/*Map<String, String> params =new HashMap<String, String>();
			params.put("id", "5417565168");
			params.put("type", "article");
			html = HttpBase.post(baseUrl, null, params);
			Document doc = Jsoup.parse(html);
			System.out.println(doc);*/
			Document doc = Jsoup.parse(html);
			
			
			
			article_title=doc.select("h1.fn").text();
			article_category=doc.select("div.bg_w.mb20").select("p.art_detail_cate").text();
			article_time=doc.select("div.pb20").select("p.pb20.gray2").select("span").first().text();
			publisher=doc.select("div.pb20").select("p.pb20.gray2").select("span").get(1).select("a").first().text();
			
			Map<String, String> params =new HashMap<String, String>();
			params.put("id", "5417565168");
			params.put("type", "article");
			String readapi="";
			try {
				readapi = HttpBase.post("http://www.haodf.com/api/thesis/ajaxgethits", null, params);
			} catch (ConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Document docapi = Jsoup.parse(readapi);
			article_readnum =docapi.toString().replaceAll("[^0-9]", "");
			article_content=doc.select("div.pb20.article_detail").text();
			imgElements=doc.select("div.pb20.article_detail").select("img");
			for (int i = 0; i < imgElements.size(); i++) {
				article_img=imgElements.get(i).attr("src")+"   ###";
			}
			//评论
			comment_url="http://www.haodf.com/api/article/ajaxcommentlist?uname=apussy&articleid=4554356932";
			try {
				comment_html = HttpBase.get(comment_url, "utf-8").getResult();
			} catch (Exception e) {
				System.out.println("评论界面解析失败!");
				e.printStackTrace();
			}
			Document doc_comment = Jsoup.parse(comment_html);
			commentElements =doc_comment.select("div.oh.zoom");
			for (int i = 0; i < commentElements.size(); i++) {
				commentElement = commentElements.get(i);
				comment_time=commentElement.select("p.gray3.fr").select("span").first().text();
				eleTemp=commentElement.select("p.gray3.fr").first();
				eleTemp.select("span").remove();
				comment_user=eleTemp.text();
				comment_content=commentElement.select("p.pb10").text();
				System.out.println("#################"+i);
				System.out.println(comment_time);
				System.out.println(comment_user);
				System.out.println(comment_content);
			}
			
			System.out.println(article_title);
			System.out.println(article_category);
			System.out.println(article_time);
			System.out.println(publisher);
			System.out.println(article_readnum);
			System.out.println(article_content);
			System.out.println(article_img);
			
			
			
		}
		
		
	}
}


