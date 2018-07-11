package com.reptile.haodaifu.test;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.reptile.haodaifu.job.HaoDFCategoryJob;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HaoDFJiBingListTest {
	
	
	@Test
	public  void testRun(){
		Dao mysqlDao = MySqlDataSource.getMysqlDao("doctor");
		String baseUrl = "http://www.haodf.com/jibing/list.htm";
		String html="",bigCategoryUrl="",bigCategoryName="",htmlBigCategory="",htmlMedianCategory="",medianCategoryUrl="",medianCategoryName=""
				,smallCategoryName="",tinyCateName="",tinyCateUrl="";
		Element element=null,medianElement=null,smallElementName=null,smallElementUrl=null;
		Elements medianCategoryList =null,smallCategoryListName=null,smallCategoryListUrl=null,smallCategoryListUrlList=null;
		Map<String, String> bigCategoryMap = new HashMap<String, String>();
		Map<String, String> medianCategoryMap = new HashMap<String, String>();
		HaoDFCategoryJob haoDFCategoryJob = new HaoDFCategoryJob();
		haoDFCategoryJob.setCreate_time(new Date());
		haoDFCategoryJob.setNode("ZRM-doctor");
		haoDFCategoryJob.setRequest_id(20170824L);
		haoDFCategoryJob.setDoctor_status(0);
		haoDFCategoryJob.setArticle_status(0);
		haoDFCategoryJob.setTask_id(20170824L);
		
		try {
			html = HttpBase.get(baseUrl, "utf-8").getResult();
		} catch (Exception e) {
			System.out.println("界面解析失败!");
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(html);
		Elements bigCategoryList=doc.select("div.kstl");
		System.out.println(bigCategoryList.size());
		
		for (int i = 0; i < bigCategoryList.size(); i++) {
			System.out.println("###############################################"+i);
			element = bigCategoryList.get(i);
			bigCategoryName = element.select("a").first().text();
			bigCategoryUrl =  element.select("a").first().attr("href");
			bigCategoryUrl ="http://www.haodf.com"+bigCategoryUrl;
			bigCategoryMap.put(bigCategoryName, bigCategoryUrl);
			
			try {
				htmlBigCategory = HttpBase.get(bigCategoryUrl, "utf-8").getResult();
			} catch (Exception e) {
				System.out.println(bigCategoryName+"界面解析失败!");
				e.printStackTrace();
			}
			
			Document docBigCategory = Jsoup.parse(htmlBigCategory);
			if (docBigCategory.select("div.ksbd").isEmpty()) {
				medianCategoryMap.put(bigCategoryName, bigCategoryUrl);
				//不用做请求,直接可以获取smallcategory
				smallCategoryListName = docBigCategory.select("div#el_result_content").select("div.ct").select("div.m_title_green");
				smallCategoryListUrl =  docBigCategory.select("div#el_result_content").select("div.ct").select("div.m_ctt_green");
				for (int k = 0; k < smallCategoryListUrl.size(); k++) {
					smallElementName = smallCategoryListName.get(k);
					smallCategoryName = smallElementName.text();
					smallCategoryListUrlList = smallCategoryListUrl.get(k).select("li").select("a");
					for (int m = 0; m < smallCategoryListUrlList.size(); m++) {
						smallElementUrl = smallCategoryListUrlList.get(m);
						tinyCateName=smallElementUrl.text();
						tinyCateUrl = "http://www.haodf.com"+smallElementUrl.attr("href");
						haoDFCategoryJob.setBig_categoryname(bigCategoryName);
						haoDFCategoryJob.setMedian_categoryname(null);
						haoDFCategoryJob.setSmall_categoryname(smallCategoryName);
						haoDFCategoryJob.setTiny_categoryname(tinyCateName);
						haoDFCategoryJob.setTiny_categoryurl(tinyCateUrl);
						try {
							mysqlDao.insert(haoDFCategoryJob);
						} catch (Exception e) {
							System.out.println(e);
						}
						
						System.out.println("tinyCateName="+tinyCateName+"     tinyCateUrl="+tinyCateUrl);
						
					}
				
				
				}
				
				
			}
			else {
				medianCategoryList = docBigCategory.select("div.ksbd").select("a");
				System.out.println("medianCategoryList="+medianCategoryList.size());
				for (int j = 0; j < medianCategoryList.size(); j++) {
					medianElement = medianCategoryList.get(j);
					medianCategoryName = medianElement.select("a").first().text();
					medianCategoryUrl =  medianElement.select("a").first().attr("href");
					medianCategoryUrl ="http://www.haodf.com"+medianCategoryUrl;
					medianCategoryMap.put(medianCategoryName, medianCategoryUrl);
					
					System.out.println("medianCategoryName="+medianCategoryName+"     medianCategoryUrl="+medianCategoryUrl);
					
					//开始请求htmlMedianCategory 最后一次请求
					try {
						htmlMedianCategory = HttpBase.get(medianCategoryUrl, "utf-8").getResult();
					} catch (Exception e) {
						System.out.println(medianCategoryName+"界面解析失败!");
						e.printStackTrace();
					}
					Document docMedianCategory = Jsoup.parse(htmlMedianCategory);
					smallCategoryListName = docMedianCategory.select("div#el_result_content").select("div.ct").select("div.m_title_green");
					smallCategoryListUrl =  docMedianCategory.select("div#el_result_content").select("div.ct").select("div.m_ctt_green");
					System.out.println(smallCategoryListUrl.size());
					for (int k = 0; k < smallCategoryListUrl.size(); k++) {
						smallElementName = smallCategoryListName.get(k);
						smallCategoryName = smallElementName.text();
						smallCategoryListUrlList = smallCategoryListUrl.get(k).select("li").select("a");
						for (int m = 0; m < smallCategoryListUrlList.size(); m++) {
							smallElementUrl = smallCategoryListUrlList.get(m);
							tinyCateName=smallElementUrl.text();
							tinyCateUrl = "http://www.haodf.com"+smallElementUrl.attr("href");
							haoDFCategoryJob.setBig_categoryname(bigCategoryName);
							haoDFCategoryJob.setMedian_categoryname(medianCategoryName);
							haoDFCategoryJob.setSmall_categoryname(smallCategoryName);
							haoDFCategoryJob.setTiny_categoryname(tinyCateName);
							haoDFCategoryJob.setTiny_categoryurl(tinyCateUrl);
							try {
								mysqlDao.insert(haoDFCategoryJob);
							} catch (Exception e) {
								System.out.println(e);
							}
							
							System.out.println("tinyCateName="+tinyCateName+"     tinyCateUrl="+tinyCateUrl);
							
						}
						
						
					}
					
					
				}
			}
			
			
			System.out.println("medianCategoryMap="+medianCategoryMap.size());
			
			System.out.println(bigCategoryName);
			System.out.println(bigCategoryUrl);
		}
		
		/*Set<Entry<String,String>> entrySet = bigCategoryMap.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			System.out.println(next.getKey()+" :"+next.getValue());
			
		}*/
		
		
		
		
	}

}





