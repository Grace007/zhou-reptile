package com.reptile.xiachufang.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.MD5Util;
import com.bds.base.util.SpringContextHolder;
import com.reptile.xiachufang.model.XiachufangCommentSimple;
import com.reptile.xiachufang.model.job.XiachufangCommentmodel;
import com.reptile.xiachufang.util.EmojiFilterUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Calendar;
import java.util.Date;

public class XiachufangCommentSimpleThread extends Thread {
	private static Logger logger = Logger.getLogger(XiachufangCommentSimpleThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private XiachufangCommentmodel task;
	public  XiachufangCommentSimpleThread(XiachufangCommentmodel task) {
		this.task =task;
	}
	@Override
	public void run() {
		try {
		
		logger.info("###################开始解析产品评论："+task.getTitle_id()+"######################");
		tDao.update(XiachufangCommentmodel.class,Chain.make("status", 1),Cnd.where("id", "=", task.getId()));
		TaskToBi tobi = BIDataSource.getBiDataSource("xiachufang",task.getProject_code(), task.getDown_type());
		Dao  biDao = tobi.getBiDao();
		String  url_base="http://www.xiachufang.com/recipe/";
		String  hot_url_after="/questions/?order=hot&page=";
		String  answer_url_after="/questions/?order=answered&page=";
		String hot_url = null;
		String answer_url = null;
		
		String title_id = task.getTitle_id();
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
		int hot_pages=0;
		int hot_counts = 0;
		int answer_pages=0;
		int answer_counts = 0;
		XiachufangCommentSimple commentSimple = new XiachufangCommentSimple();
		
		//String hot_url = "http://www.xiachufang.com/recipe/101714088/questions/?order=hot&page=1";
		//String answer_url = "http://www.xiachufang.com/recipe/101714088/questions/?order=answered&page=1";
		
		do {
			System.out.println("###############开始解析作者答疑评论################");
			answer_url = url_base.concat(title_id).concat(answer_url_after).concat(String.valueOf(answer_pages+1));
			try {
				html = HttpBase.get(answer_url, "utf-8").getResult();
			} catch (Exception e) {

			}
			Document doc = Jsoup.parse(html);
			answer_nothing  = doc.select("p.recipe-questions-noreply").isEmpty();
			boolean has404 = html.contains("404~~你访问的页面找不到");
			if (!answer_nothing || has404) {
				//元素存在，就没有comment
				System.out.println("##########本页没有作者答疑数据############");
				System.out.println(answer_pages);
				System.out.println(answer_counts);
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
				
				author_answer = EmojiFilterUtils.removeImoji(author_answer);
				comment = EmojiFilterUtils.removeImoji(comment);
				answer_time = dateConvert(answer_time_str);
				question_time = dateConvert(question_time_str);
				
				commentSimple.setAnswer_time(answer_time);
				commentSimple.setAuthor_answer(author_answer);
				commentSimple.setComment(comment);
				commentSimple.setCreate_date(new Date());
				commentSimple.setCreate_time(new Date());
				commentSimple.setDown_type(task.getDown_type());
				commentSimple.setEntrance(task.getEntrance());
				commentSimple.setProject_code(task.getProject_code());
				commentSimple.setQuestion_time(question_time);
				commentSimple.setRequest_id(task.getRequest_id());
				commentSimple.setSupport_number(support_number);
				commentSimple.setWebsite(task.getWebsite());
				commentSimple.setUsername(username);
				commentSimple.setType("作者答疑");
				commentSimple.setTitle_url(task.getTitle_url());
				commentSimple.setTitle_id(task.getTitle_id());
				commentSimple.setTask_id(task.getTask_id());
				commentSimple.setMd5(MD5Util.getMD5(comment+username+author_answer+support_number));
				
				try {
				    biDao.fastInsert(commentSimple);
				} catch (Exception e) {
					logger.error("###############commentSimple插入BI表失败，，失败的类型：作者答疑，  失败商品的url为："+task.getTitle_url()+"#############"+e);

				}
				
				/*System.out.println(username);
				System.out.println(question_time);
				System.out.println(comment);
				System.out.println(support_number);
				System.out.println(answer_time);
				System.out.println(author_answer);
				System.out.println("################");*/
				answer_counts++;
			}
			answer_pages++;
			
			
		} while (true);
		
		XiachufangCommentSimple commentSimple1 = new XiachufangCommentSimple();
		
		do {
			System.out.println("###############开始解析最热评论################");
			hot_url = url_base.concat(title_id).concat(hot_url_after).concat(String.valueOf(hot_pages+1));
			try {
				html = HttpBase.get(hot_url, "utf-8").getResult();
			} catch (Exception e) {

			}
			Document doc = Jsoup.parse(html);
			answer_nothing  = doc.select("p.recipe-questions-noreply").isEmpty();
			boolean has404 = html.contains("404~~你访问的页面找不到");
			if (!answer_nothing || has404 ) {
				//元素存在，就没有comment
				System.out.println("##########本页没有最热评论数据############");
				System.out.println(hot_pages);
				System.out.println(hot_counts);
				break;
			}
			elements = doc.select("div.ias-container").select("li.recipe-quesions-content");
			for (int i = 0; i < elements.size(); i++) {
				element = elements.get(i);
				
				//username = element.select("div.avatar-right-2").select("div.right-top.info").select("a.normal-font").text().trim();
				//question_time_str = element.select("div.avatar-right-2").select("div.right-top.info").select("span.normal-font.gray-font.align-baseline").text().trim();
				//comment = element.select("div.avatar-right-2").select("div.right-bottom").text().trim();
				
				//support_number = element.select("div.avatar-right-2").select("div.right-top.info").select("span.recipe-questions-digg-numbers").select("span.digged-number").text().trim();
				//question_time = dateConvert(question_time_str);
				try {
				
				username = element.select("div.avatar-right-2").select("div.right-top.info").select("a.normal-font").text().trim();
				question_time_str = element.select("div.avatar-right-2").select("div.right-top.info").select("span.normal-font.gray-font.align-baseline").text().trim();
				comment = element.select("div.avatar-right-2").select("div.right-bottom").text().trim();
				support_number = element.select("div.avatar-right-2").select("div.right-top.info").select("span.recipe-questions-digg-numbers").select("span.digged-number").text().trim();
				question_time = dateConvert(question_time_str);
				comment = EmojiFilterUtils.removeImoji(comment);
				
				} catch (Exception e) {
					System.out.println(e);
				}
				
				commentSimple1.setComment(comment);
				commentSimple1.setCreate_date(new Date());
				commentSimple1.setCreate_time(new Date());
				commentSimple1.setDown_type(task.getDown_type());
				commentSimple1.setEntrance(task.getEntrance());
				commentSimple1.setNode(task.getNode());
				commentSimple1.setProject_code(task.getProject_code());
				commentSimple1.setQuestion_time(question_time);
				commentSimple1.setRequest_id(task.getRequest_id());
				commentSimple1.setSupport_number(support_number);
				commentSimple1.setWebsite(task.getWebsite());
				commentSimple1.setUsername(username);
				commentSimple1.setType("最热评论");
				commentSimple1.setTitle_url(task.getTitle_url());
				commentSimple1.setTitle_id(task.getTitle_id());
				commentSimple1.setTask_id(task.getTask_id());
				commentSimple1.setMd5(MD5Util.getMD5(comment+username+question_time+support_number));
				
				try {
				    biDao.fastInsert(commentSimple1);
				} catch (Exception e) {
					logger.error("###############commentSimple插入BI表失败，失败的类型：最热评论，   失败商品的url为："+task.getTitle_url()+"#############"+e);
				}
				
				hot_counts++;
			}
			hot_pages++;
			
			
		} while (true);
		
		logger.info("###############commentSimple插入BI表成功，商品的id为："+task.getTitle_id()+"#############");
		tDao.update(XiachufangCommentmodel.class, Chain.make("status", 2).add("hot_pages", hot_pages).add("answer_pages", answer_pages).add("hot_counts", hot_counts).add("answer_counts", answer_counts), 
				Cnd.where("id", "=", task.getId()));
		
		
		} catch (Exception e) {
			logger.error("###############commentSimple插入BI表失败,失败商品的url为："+task.getTitle_url()+"#############"+e);
			tDao.update(XiachufangCommentmodel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
		}
		
		
		
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
