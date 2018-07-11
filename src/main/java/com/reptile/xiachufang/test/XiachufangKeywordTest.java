package com.reptile.xiachufang.test;

import com.bds.base.http.HttpBase;
import com.reptile.xiachufang.model.XiachufangKeywordSimple;
import com.reptile.xiachufang.model.job.XiachufangKeywordmodel;
import com.reptile.xiachufang.util.util.MyDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.nutz.dao.Dao;

public class XiachufangKeywordTest {
	@Test
	public void creatTable(){
		XiachufangKeywordmodel keywordmodel = new XiachufangKeywordmodel();
		Dao tdao = MyDao.getDao();
		tdao.create(XiachufangKeywordmodel.class, false);
		tdao.create(XiachufangKeywordSimple.class, false);
		
	}
	@Test
	public void test() throws Exception{
		String url = "http://detail.vip.com/detail-1194386-121665031.html";
		String html = HttpBase.get(url, "utf-8").getResult();
		Document doc = Jsoup.parse(html);
		String string = doc.select(".price-name").text();
		System.out.println(string);
	}
}
