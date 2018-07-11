package com.reptile.xiachufang.test;

import com.bds.base.http.HttpBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class XiachufangCookbookInfoTest {

/*	@Test
	public void creatCookbookTable(){
		Dao tDao = MyDao.getDao();
		tDao.create(XiachufangCookbookmodel.class, false);
	}
	@Test
	public void creatCookbookInfoBiTable(){
		Dao tDao = MyDao.getDao();
		tDao.create(XiachufangCookbookInfo.class, false);
	}*/
	
	
	@Test
	public void testRun() {
		
		String url="http://www.xiachufang.com/recipe/269527/";
		String html=null;
		String cookbook=null;
		String category=null;
		String score=null;
		String support_number=null;
		String author_name=null;
		String author_url=null;
		String author_id=null;
		String cookbook_createdate=null;
		String collect_num=null;
		String material="";
		String content=null;
		String img_url="";
		String baseUrl ="http://www.xiachufang.com";
		String [] temp = null;
		Elements img_elements = null;
		Element img_element = null;
		try {
			html = HttpBase.get(url, "utf-8").getResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Document doc = Jsoup.parse(html);
		
		cookbook = doc.select("h1.page-title").text().trim();
		category = doc.select("div.recipe-tags.block.mb50").select("div.recipe-cats").select("a").text().trim();
		score = doc.select("div.stats.clearfix").select("div.score.float-left").select("span.number").text().trim();
		support_number =doc.select("div.stats.clearfix").select("div.cooked.float-left").select("span.number").text().trim();
		author_name=doc.select("div.author").select("a.avatar-link.avatar").select("span[itemprop=author]").text();
		author_url = doc.select("div.author").select("a.avatar-link.avatar").attr("href").trim();
		temp = author_url.split("/");
		author_url = baseUrl.concat(author_url);
		author_id = temp[2];
		cookbook_createdate = doc.select("div.recipe-stats.block.normal-font.gray-font").select("div.time").select("span[itemprop=datePublished]").text().trim();
		collect_num = doc.select("div.recipe-stats.block.normal-font.gray-font").select("div.pv").text().replaceAll("\\D", "");
		
		Elements materials = doc.select("div.ings").select("table").select("tbody").select("tr");
		Element materialElement = null;
		for (int i = 0; i < materials.size(); i++) {
			materialElement = materials.get(i);
			material += materialElement.text()+";";
		}
		
		
		//material = doc.select("div.ings").select("table").text().trim();
		
		
		content = doc.select("div.steps").toString();
		img_elements = doc.select("div.steps").select("img");
		for (int i = 0; i < img_elements.size(); i++) {
			img_element = img_elements.get(i);
			img_url = img_url.concat("###"+img_element.attr("src"));
		}
		System.out.println(cookbook);
		System.out.println(category);
		System.out.println(score);
		System.out.println(support_number);
		System.out.println(author_name);
		System.out.println(author_url);
		System.out.println(author_id);
		System.out.println(cookbook_createdate);
		System.out.println(collect_num);
		System.out.println(material);
		System.out.println(content);
		System.out.println(img_url);
		
		
		
		
	} 
}
