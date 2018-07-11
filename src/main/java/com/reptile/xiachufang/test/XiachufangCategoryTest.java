package com.reptile.xiachufang.test;

import com.bds.base.http.HttpBase;
import com.reptile.xiachufang.model.job.XiachufangCateGorymodel;
import com.reptile.xiachufang.util.util.MyDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.nutz.dao.Dao;

import java.util.Date;

public class XiachufangCategoryTest {
	
	@Test
	public void testRun() throws Exception {
		try {
		
			
		String urlBase = "http://www.xiachufang.com";
		Dao tDao = MyDao.getDao();
		String down_type="Down_type",node="test1",project_code="project_code",website="xiachufang";
		Long request_id = 20170502L,task_id=1L;
		String url = "http://www.xiachufang.com/category/";
		String html = HttpBase.get(url, "utf-8").getResult();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("div.block-bg").select("div.cates-list-all.clearfix").select("li");
		XiachufangCateGorymodel cateGorymodel = new XiachufangCateGorymodel();
		for(int i= 0 ; i <elements.size() ;i++){
			Element categoryEle = elements.get(i).select("a").get(0);
			String categoryName = categoryEle.text();
			String category_url = categoryEle.attr("href");
			category_url = urlBase.concat(category_url).concat("time/");
			
			cateGorymodel.setCategory(categoryName);
			cateGorymodel.setCategory_url(category_url);
			cateGorymodel.setCreate_date(new Date());
			cateGorymodel.setDown_type(down_type);
			cateGorymodel.setNode(node);
			cateGorymodel.setProject_code(project_code);
			cateGorymodel.setRequest_id(request_id);
			cateGorymodel.setStatus(0);
			cateGorymodel.setTask_id(task_id);
			cateGorymodel.setWebsite(website);
			cateGorymodel.setCreate_datetime(new Date());
			//System.out.println(categoryName);
			//System.out.println(category_url);s
			try {
				tDao.fastInsert(cateGorymodel);
			} catch (Exception e) {
				System.out.println("categroy生成任务失败,失败的分类为："+categoryName);
			}
			
		}
		System.out.println(elements.size());
		
		} catch (Exception e) {
			System.out.println("categroy生成任务!");
		}
	}
	
	
	//block-bg   

}
