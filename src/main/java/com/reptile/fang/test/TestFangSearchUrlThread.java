package com.reptile.fang.test;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bds.base.http.HttpBase;

public class TestFangSearchUrlThread {

	public static void main(String[] args) {
		
		String url="http://search.fang.com/bbs/search.jsp?q=%D0%DB%B0%B2&key=&city=%B1%B1%BE%A9";
		int pages = 1;
		try {
			String html = HttpBase.get(url, "utf-8").getResult();
			//System.out.println(html);
			if(StringUtils.isEmpty(html)){
				System.out.println("获得的页面为空############");
			}
			Document doc = Jsoup.parse(html);
			String amount = doc.select("div.rit").select("span.fB").get(0).text();
			//System.out.println(amount);
			Integer amountI = Integer.parseInt(amount);
			
			pages=amountI/10+1;
			System.out.println(pages);
			if(pages > 30) pages = 30;
			System.out.println(pages);
			
			String keyword ="雄安";
			String city ="北京";
			//http://search.fang.com/bbs/search.jsp?q=%D0%DB%B0%B2&start=50&num=10&fld=&city=%B1%B1%BE%A9&sort=score&sl=post&show=list&si=0
			for (int i = 0; i < pages; i++) {
				String next_url = "http://search.fang.com/bbs/search.jsp?q="+java.net.URLEncoder.encode(keyword, "gb2312")+"&start="+i*10+"&num=10&fld=&city="+java.net.URLEncoder.encode(city, "gb2312")+"&sort=score&sl=post&show=list&si=0";
				//String next_url = "http://search.fang.com/bbs/search.jsp?q="+keyword+"&start="+i*10+"&num=10&fld=&city="+city+"&sort=score&sl=post&show=list&si=0";
				System.out.println(next_url);
			}
		} catch (Exception e) {
		   System.out.println(e);
		}
	}

}
