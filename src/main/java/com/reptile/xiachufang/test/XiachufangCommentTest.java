package com.reptile.xiachufang.test;

import com.bds.base.http.HttpBase;
import com.reptile.xiachufang.model.XiachufangCommentSimple;
import com.reptile.xiachufang.model.job.XiachufangCommentmodel;
import com.reptile.xiachufang.util.util.MyDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.nutz.dao.Dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class XiachufangCommentTest {
	@Test
	public void creatCommentmodelTable(){
		Dao tdao = MyDao.getDao();
		tdao.create(XiachufangCommentmodel.class, false);
	}
	@Test
	public void creatCommentSimpleTable(){
		Dao tdao = MyDao.getDao();
		tdao.create(XiachufangCommentSimple.class, false);
	}
	
	
	@Test
	public void testRun(){
		String hot_url = "http://www.xiachufang.com/recipe/100352761/questions/?order=hot&page=1";
		String answer_url = "http://www.xiachufang.com/recipe/100352761/questions/?order=answered&page=1";
		
		
		String type =null;
		String username = null;
		String question_time_str = null;
		Date question_time = null;
		String comment = null;
		String answer_time_str =null;
		Date answer_time = null;
		String support_number = null;
		String author_answer = null;
		String html=null;
		Elements elements=null;
		Element element =null;
		boolean hot_nothing = true;
		boolean answer_nothing = true;
		int m = 1;
		int count = 0;
		
		
		do {

			try {
				html = HttpBase.get(hot_url, "utf-8").getResult();
			} catch (Exception e) {

			}
			Document doc = Jsoup.parse(html);
			hot_nothing  = doc.select("p.recipe-questions-noreply").isEmpty();
			if (!hot_nothing) {
				//元素存在，就没有comment
				System.out.println("##########本页没有数据############");
				System.out.println(m);
				System.out.println(count);
				break;
			}
			elements = doc.select("div.ias-container").select("li.recipe-quesions-content");
			for (int i = 0; i < elements.size(); i++) {
				element = elements.get(i);
				
				username = element.select("div.avatar-right-2").select("div.right-top.info").select("a.normal-font").text().trim();
				question_time_str = element.select("div.avatar-right-2").select("div.right-top.info").select("span.normal-font.gray-font.align-baseline").text().trim();
				comment = element.select("div.avatar-right-2").select("div.right-bottom").text().trim();
				support_number = element.select("div.avatar-right-2").select("div.right-top.info").select("span.recipe-questions-digg-numbers").select("span.digged-number").text().trim();
				question_time = dateConvert(question_time_str);
				
				System.out.println(username);
				System.out.println(question_time);
				System.out.println(comment);
				System.out.println(support_number);
				System.out.println("################");
				count++;
				
			}
			m++;
			hot_url = "http://www.xiachufang.com/recipe/101714088/questions/?order=hot&page="+m;
		} while (true);
		
		m=1;count=0;
		
		
		
		/*do {
			try {
				html = HttpBase.get(answer_url, "utf-8").getResult();
			} catch (Exception e) {

			}
			Document doc = Jsoup.parse(html);
			answer_nothing  = doc.select("p.recipe-questions-noreply").isEmpty();
			if (!answer_nothing) {
				//元素存在，就没有comment
				System.out.println("##########本页没有数据############");
				System.out.println(m);
				System.out.println(count);
				break;
			}
			elements = doc.select("div.ias-container").select("li.recipe-quesions-content");
			for (int i = 0; i < elements.size(); i++) {
				element = elements.get(i);
				
				username = element.select("div.avatar-right-2").select("div.right-top.info").select("a.normal-font").text().trim();
				question_time_str = element.select("div.avatar-right-2").select("div.right-top.info").select("span.normal-font.gray-font.align-baseline").text().trim();
				comment = element.select("div.avatar-right-2").select("div.right-bottom").text().trim();
				support_number = element.select("div.avatar-right-2").select("div.right-top.info").select("span.recipe-questions-digg-numbers").select("span.digged-number").text().trim();
				author_answer = element.select("div.avatar-right-2").get(1).select("div.right-bottom.recipe-questions-answer").text().trim();
				answer_time_str = element.select("div.avatar-right-2").get(1).select("span.normal-font.gray-font.align-baseline").text().trim();
				
				answer_time = dateConvert(answer_time_str);
				question_time = dateConvert(question_time_str);
				
				
				System.out.println(username);
				System.out.println(question_time);
				System.out.println(comment);
				System.out.println(support_number);
				System.out.println(answer_time);
				System.out.println(author_answer);
				System.out.println("################");
				count++;
				
				
				
			}
			m++;
			answer_url = "http://www.xiachufang.com/recipe/101714088/questions/?order=answered&page="+m;
			
		} while (true);*/
		
		
		
		
	}
	
	@Test
	public void dateTest(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = " 24小时前   0 赞";
		Date date = dateConvert(str);
		String dateStr = simpleDateFormat.format(date);
		System.out.println(dateStr);
	}
	
	public Date dateConvert(String before_time) {
		// 5个月前   0 赞
		before_time = before_time.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "").trim();
		//5个月前0赞
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		if (before_time.contains("年")) {
			String [] numStr = before_time.split("年");
			int numberInt = Integer.parseInt("-"+numStr[0]);
			calendar.add(Calendar.YEAR, numberInt);
		}else if(before_time.contains("月")){
			String [] numStr = before_time.split("个");
			int numberInt = Integer.parseInt("-"+numStr[0]);
			calendar.add(Calendar.MONTH, numberInt);
		}else if (before_time.contains("昨天")){
			calendar.add(Calendar.DAY_OF_YEAR, -1);
		}else if (before_time.contains("今天")) {
			calendar.add(Calendar.DAY_OF_YEAR, 0);
		}else if (before_time.contains("天")) {
			String [] numStr = before_time.split("天");
			int numberInt = Integer.parseInt("-"+numStr[0]);
			calendar.add(Calendar.DAY_OF_YEAR, numberInt);
		}else {
			calendar.add(Calendar.DAY_OF_YEAR, 0);
		}
		Date after_time = calendar.getTime();
		
		return after_time;
	}
	
	
	
}
