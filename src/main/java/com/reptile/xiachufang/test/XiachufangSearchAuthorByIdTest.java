package com.reptile.xiachufang.test;

import com.bds.base.http.HttpBase;
import com.reptile.xiachufang.model.XiachufangAuthorCookbookSimple;
import com.reptile.xiachufang.model.XiachufangAuthorInfo;
import com.reptile.xiachufang.model.job.XiachufangCookbookPagemodel;
import com.reptile.xiachufang.model.job.XiachufangSearchAuthorByIdmodel;
import com.reptile.xiachufang.util.util.MyDao;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.nutz.dao.Dao;

public class XiachufangSearchAuthorByIdTest {
	@Test
	public void creatTable(){
		XiachufangSearchAuthorByIdmodel searchAuthorById = new XiachufangSearchAuthorByIdmodel();
		Dao tdao = MyDao.getDao();
		tdao.create(XiachufangSearchAuthorByIdmodel.class, false);
		System.out.println("creatTable");
	}
	@Test
	public void creatBiTable(){
		Dao tdao = MyDao.getDao();
		tdao.create(XiachufangAuthorInfo.class, false);
		System.out.println("creatBiTable");
	}
	@Test
	public void creatCookbookPageTable(){
		Dao tdao = MyDao.getDao();
		tdao.create(XiachufangCookbookPagemodel.class, false);
		System.out.println("creatCookbookPageTable");
	}
	@Test
	public void creatCookbookSimpleTable(){
		Dao tdao = MyDao.getDao();
		tdao.create(XiachufangAuthorCookbookSimple.class, false);
		System.out.println("creatCookbookSimpleTable");
	}
	
	
	
	@Test
	public void testRun(){
		String html = null;
		String author_id = "10162229";
		String baseUrl = "http://www.xiachufang.com";
		String author_url = baseUrl.concat("/cook/").concat(author_id);
		try {
		    html = HttpBase.get(author_url, "utf-8").getResult();
		} catch (Exception e) {
			return ;
		}
		Document doc = Jsoup.parse(html);
		//System.out.println(doc);
		
		String author_name = doc.select("h1.page-title").text().trim();
		String sex = doc.select("div.pure-u-5-8.font12.pr30").select("span.display-inline-block").first().text().trim();
		if(!(sex.contains("男") || sex.contains("女"))) sex = null;
		String join_date = doc.select("div.pure-u-5-8.font12.pr30").select("span.display-inline-block").last().text().replaceAll("[\u4e00-\u9fa5]+","").trim();
		String following_num = doc.select("div.follow-wrap").select("div.pure-u-1-2").get(0).select("a").get(0).text().trim();
		String followed_num = doc.select("div.follow-wrap").select("div.pure-u-1-2").get(1).select("a").get(0).text().trim();
		String cookbook_num = doc.select("ul.plain.pure-g.w100").select("li").get(1).select("a").text().trim().replaceAll("\\D", "");
		if(StringUtils.isEmpty(cookbook_num)) cookbook_num = "0";
		String production_num = doc.select("ul.plain.pure-g.w100").select("li").get(2).select("a").text().trim().replaceAll("\\D", "");
		if (StringUtils.isEmpty(production_num)) production_num ="0";
		String menu_num = doc.select("ul.plain.pure-g.w100").select("li").get(3).select("a").text().trim().replaceAll("\\D", "");
		if (StringUtils.isEmpty(menu_num)) menu_num ="0";
		String collect_num = doc.select("ul.plain.pure-g.w100").select("li").get(4).select("a").text().trim().replaceAll("\\D", "");
		if (StringUtils.isEmpty(collect_num)) collect_num ="0";
		String message_num = doc.select("ul.plain.pure-g.w100").select("li").get(5).select("a").text().trim().replaceAll("\\D", "");
		if (StringUtils.isEmpty(message_num)) message_num ="0";
		
		
		System.out.println(author_name);
		System.out.println(following_num);
		System.out.println(followed_num);
		System.out.println(cookbook_num);
		System.out.println(production_num);
		System.out.println(menu_num);
		System.out.println(collect_num);
		System.out.println(message_num);
		System.out.println(join_date);
		System.out.println(sex);
		//System.out.println(author_url);System.out.println(followed_num);
		
		
	}
}
