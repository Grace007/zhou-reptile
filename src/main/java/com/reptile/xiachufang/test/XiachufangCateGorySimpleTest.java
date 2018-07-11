package com.reptile.xiachufang.test;

import com.bds.base.http.HttpBase;
import com.reptile.xiachufang.model.XiachufangCateGorySimple;
import com.reptile.xiachufang.model.job.XiachufangCateGorymodel;
import com.reptile.xiachufang.util.util.MyDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.nutz.dao.Dao;

public class XiachufangCateGorySimpleTest {
	@Test
	public void testCreatTable() throws Exception{
		XiachufangCateGorySimple cateGorymodel = new XiachufangCateGorySimple();
		Dao tdao = MyDao.getDao();
		tdao.create(XiachufangCateGorySimple.class, false);
	}
	
	@Test
	public void testRun(){
		//Dao tDao = SpringContextHolder.getBean("tDao");
		XiachufangCateGorymodel task;
		String url = "http://www.xiachufang.com/category/1304/time/";
		//String url = "http://www.xiachufang.com/category/1304/time/?page=3";
		String html = null;
		
		do {
		try {
			html = HttpBase.get(url, "utf-8").getResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("div.normal-recipe-list").select("ul.list").select("li");	
		Element element =null,pagerEle=null;
		Boolean temp = false;
		String urlBase = "http://www.xiachufang.com";
		for (int i = 0; i < elements.size(); i++) {
			element = elements.get(i);
			String title_url = urlBase.concat(element.select("a").attr("href")) ;
			String title = element.select("p.name").text().trim();
			String material = element.select("p.ing.ellipsis").text().trim();
			String score =element.select("p.stats").select("span.score.bold.green-font").text().trim() ;
			String support_number=element.select("p.stats").select("span.bold.score").get(1).text().trim();
			String author=element.select("p.author").select("span.gray-font").text().trim();
			
			//http://www.xiachufang.com/recipe/100486395/
			
			/*System.out.println(i);
			System.out.println(title_url);
			System.out.println(title);
			System.out.println(material);
			System.out.println(score);
			System.out.println(support_number);
			System.out.println(author);
			System.out.println("###############################");*/
			
			
			
			
			
		}
		
		temp = doc.select("div.pager").select("a.next").isEmpty();
		System.out.println(temp);
		if (temp) {
			//没有下一页
			break;
		}else {
			//有下一页
			String next_url = urlBase.concat(doc.select("div.pager").select("a.next").attr("href"));
			url = next_url;
		}
		
		} while (true);
	}
	
}
