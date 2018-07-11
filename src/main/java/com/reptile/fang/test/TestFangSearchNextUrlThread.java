package com.reptile.fang.test;

import com.bds.base.http.HttpBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestFangSearchNextUrlThread {

	public static void main(String[] args) {
		String url="http://search.fang.com/bbs/search.jsp?q=%BA%A3%C1%BF%B4%F3%B6%BC%BB%E1&start=20&num=10&fld=&city=%C9%CF%BA%A3&sort=score&sl=post&show=list&si=0";
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			String html = HttpBase.get(url, "utf-8").getResult();
			//System.out.println(html);
			Document doc = Jsoup.parse(html);
			
			Elements elements =doc.select("div#content").select("div.result");
			String author_url = "";
			//System.out.println(elements.size());
			Element element = null;
			for (int i = 0; i < elements.size(); i++) {
				element = elements.get(i);
				if(element.select("img").isEmpty()){
					//System.out.println(i+".没有图片的标题");
					System.out.println("###################################");
					//截取标题和发帖时间
					String[] onclickText = element.select("div.postTitle").select("a").attr("onclick").split("'");
					String title =onclickText[1].trim();
					
					String unfinishedDatetime = onclickText[onclickText.length-2].trim();
					Date date_posted =df1.parse(unfinishedDatetime);
					//得到titleUrl和简介内容
					String titleUrl = element.select("div.postTitle").select("a").attr("href");
					String message = element.select("div.postSource").select("p").get(0).text();
					//得到作者名字
					String author_name = element.select("div.postSource").select("p").get(1).select("a").get(0).text();
					//得到子论坛连接
					//
					author_url = element.select("div.postSource").select("p").get(1).select("a").get(0).attr("href");
					
					
					String thread_id = titleUrl.substring(titleUrl.lastIndexOf("_")+1,titleUrl.lastIndexOf("."));
					
					String forum_url = element.select("div.postSource").select("p").get(1).select("a").get(1).attr("href");
					System.out.println("title:"+title);
					System.out.println("author_name:"+author_name);
					System.out.println("date_posted:"+date_posted);
					System.out.println("forum_url:"+forum_url);
					System.out.println("titleUrl:"+titleUrl);
					System.out.println("message:"+message);
					System.out.println("author_url:"+author_url);
					System.out.println("thread_id:"+thread_id);
				}
				else{
					//System.out.println(i+".有图片的标题");
					System.out.println("###################################");
					//截取标题和发帖时间
					String[] onclickText = element.select("div.postTitle").select("a").attr("onclick").split("'");
					String title =onclickText[1].trim();
					
					String unfinishedDatetime = onclickText[onclickText.length-2].trim();
					Date date_posted =df1.parse(unfinishedDatetime);
					//得到titleUrl和简介内容
					String titleUrl = element.select("div.postTitle").select("a").attr("href");
					String message = element.select("div.postSource2").select("p").get(0).text();
					//得到作者名字
					String author_name = element.select("div.postSource2").select("p").get(1).select("a").get(0).text();
					//得到子论坛连接
					String forum_url = element.select("div.postSource2").select("p").get(1).select("a").get(1).attr("href");
					//
					String thread_id = titleUrl.substring(titleUrl.lastIndexOf("_")+1,titleUrl.lastIndexOf("."));
					author_url = element.select("div.postSource2").select("p").get(1).select("a").get(0).attr("href");
					System.out.println("title:"+title);
					System.out.println("author_name:"+author_name);
					System.out.println("date_posted:"+date_posted);
					System.out.println("forum_url:"+forum_url);
					System.out.println("titleUrl:"+titleUrl);
					System.out.println("message:"+message);
					System.out.println("author_url:"+author_url);
					System.out.println("thread_id:"+thread_id);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String splitRegex(String url) {
		String [] urlSplit = url.split("");
		return null;
	}
	/*@Test
	public void name() {
		String source = "http://shanghaikangcheng.fang.com/bbs/1210002134~1242/86313947_8631347.htm";
        String find = "[1-9][0-9]{4,}";
        Pattern pattern = Pattern.compile(find);
        Matcher matcher = pattern.matcher(source);
        while(matcher.find()) {
            System.out.println(matcher.group());
        }
	}*/

}
