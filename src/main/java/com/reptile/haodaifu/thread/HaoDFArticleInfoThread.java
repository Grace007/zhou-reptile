package com.reptile.haodaifu.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.http.HttpsBase;
import com.bds.base.util.MD5Util;
import com.reptile.haodaifu.job.HaoDFArticleSimpleJob;
import com.reptile.haodaifu.model.HaoDFArticleCommentSimple;
import com.reptile.haodaifu.model.HaoDFArticleInfo;
import com.reptile.server.MyConstant;
import com.reptile.util.Re;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HaoDFArticleInfoThread extends Thread {
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(MyConstant.ZNode);
	private HaoDFArticleSimpleJob task;
	private Logger logger =Logger.getLogger(HaoDFArticleInfoThread.class);
	TaskToBi tobi = BIDataSource.getBiDataSource("doctor", "DOCTOR", "get_article_info");
	Dao biDao = tobi.getBiDao();
	public  HaoDFArticleInfoThread(Record task) {
		this.task = task.toPojo(HaoDFArticleSimpleJob.class);
	}
	
	@Override
	public void run() {
		try {
			mysqlDao.update(HaoDFArticleSimpleJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			
			String baseUrl =task.getArticle_url();
			String html="";
			String article_title="",article_time="",dname="",dposition="",dhospital="",ddepartment="",article_contentnum="",article_commentnum="",article_url="",
					article_type="",article_readnum="",temp="",username="",article_id="",did="";
			String article_category="",publisher="",article_content="",article_img="",comment_url="",comment_html="",comment_time="",comment_user="",comment_content="",article_duration="";
			Elements imgElements=null,commentElements=null;
			Element commentElement=null,eleTemp=null;
			
			
			
			HaoDFArticleInfo info = new HaoDFArticleInfo();
			info.setCreate_time(new Date());
			info.setBig_categoryname(task.getBig_categoryname());
			info.setMedian_categoryname(task.getMedian_categoryname());
			info.setTiny_categoryname(task.getTiny_categoryname());
			info.setSmall_categoryname(task.getSmall_categoryname());
			info.setRequest_id(task.getRequest_id());
			info.setTask_id(task.getTask_id());
			info.setNode(task.getNode());
			info.setDhospital(task.getDhospital());
			info.setDid(task.getDid());
			info.setDname(task.getDname());
			info.setUsername(task.getUsername());
			info.setDhospital(task.getDhospital());
			info.setArticle_type(task.getArticle_type());
			info.setArticle_id(task.getArticle_id());
			info.setDposition(task.getDposition());
			info.setDdepartment(task.getDdepartment());
			info.setArticle_commentnum(task.getArticle_commentnum());
			info.setArticle_url(task.getArticle_url());
			
			HaoDFArticleCommentSimple commentSimple = new HaoDFArticleCommentSimple();
			commentSimple.setBig_categoryname(task.getBig_categoryname());
			commentSimple.setCreate_time(new Date());
			commentSimple.setMedian_categoryname(task.getMedian_categoryname());
			commentSimple.setTiny_categoryname(task.getTiny_categoryname());
			commentSimple.setSmall_categoryname(task.getSmall_categoryname());
			commentSimple.setRequest_id(task.getRequest_id());
			commentSimple.setTask_id(task.getTask_id());
			commentSimple.setNode(task.getNode());
			commentSimple.setArticle_id(task.getArticle_id());
			commentSimple.setArticle_url(task.getArticle_url());
			commentSimple.setUsername(task.getUsername());
			
			if (baseUrl.contains("touch")) {
				try {
					html = HttpsBase.get(baseUrl, "utf-8").getResult();
				} catch (Exception e) {
					logger.error("界面解析失败");
				}
				logger.info(html);
				Document doc = Jsoup.parse(html);
				article_title=doc.select("h1.d-title").text();
				article_readnum=doc.select("section.detail").select("span.d-w-number").text().replaceAll("\\D", "");
				if (StringUtils.isEmpty(article_readnum)) {
					article_readnum="0";
				}
				
				article_duration=doc.select("section.detail").select("span.d-w-time").text();
				article_time=doc.select("section.detail").select("span.d-w-date").text();
				article_content=doc.select("p.d-recommend").text()+doc.select("ul.d-voice").text();
				
				
				/*Pattern compile = Pattern.compile("\\d+\\:\\d+");
		        Matcher matcher = compile.matcher(article_duration);
		        matcher.find();
		        article_duration = matcher.group();*/
				article_duration= Re.match("\\d+\\:\\d+", article_duration);
				info.setArticle_title(article_title);
				info.setArticle_readnum(article_readnum);
				info.setArticle_duration(article_duration);
				info.setArticle_time(article_time.replace("发表", ""));
				info.setArticle_content(article_content);
				
				try {
					biDao.insert(info);
				} catch (Exception e) {
					logger.error("语音消息:articleinfo插入失败,");
				}
				
				
			}
			else {
				try {
					html = HttpBase.get(baseUrl, "utf-8").getResult();
				} catch (Exception e) {
					logger.error("界面解析失败");
				}
				
				Document doc = Jsoup.parse(html);
				
				
				article_title=doc.select("h1.fn").text();
				article_category=doc.select("div.bg_w.mb20").select("p.art_detail_cate").text();
				try {
					article_time=doc.select("div.pb20").select("p.pb20.gray2").select("span").first().text();
				} catch (Exception e) {
					article_time="";
				}
				
				publisher=doc.select("div.pb20").select("p.pb20.gray2").select("span").get(1).select("a").first().text();
				
				Map<String, String> params =new HashMap<String, String>();
				params.put("id", task.getArticle_id());
				params.put("type", "article");
				String readapi="";
				try {
					readapi = HttpBase.post("http://www.haodf.com/api/thesis/ajaxgethits", null, params);
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Document docapi = Jsoup.parse(readapi);
				article_readnum =docapi.toString().replaceAll("[^0-9]", "");
				article_content=doc.select("div.pb20.article_detail").text();
				imgElements=doc.select("div.pb20.article_detail").select("img");
				for (int i = 0; i < imgElements.size(); i++) {
					article_img=imgElements.get(i).attr("src")+"   ###";
				}
				//评论
				comment_url="http://www.haodf.com/api/article/ajaxcommentlist?uname="+task.getUsername()+"&articleid="+task.getArticle_id();
				try {
					comment_html = HttpBase.get(comment_url, "utf-8").getResult();
				} catch (Exception e) {
					logger.error("评论界面解析失败");
				}
				Document doc_comment = Jsoup.parse(comment_html);
				commentElements =doc_comment.select("div.oh.zoom");
				for (int i = 0; i < commentElements.size(); i++) {
					commentElement = commentElements.get(i);
					comment_time=commentElement.select("p.gray3.fr").select("span").first().text();
					eleTemp=commentElement.select("p.gray3.fr").first();
					eleTemp.select("span").remove();
					comment_user=eleTemp.text();
					comment_content=commentElement.select("p.pb10").text();
					
					commentSimple.setComment_content(comment_content);
					commentSimple.setComment_time(comment_time);
					commentSimple.setComment_url(comment_url);
					try {
					commentSimple.setComment_user(comment_user.substring(0, comment_user.indexOf("(")));
					commentSimple.setAddress(subString("(来自", "的网友)", comment_user));
					} catch (Exception e) {
					}
					commentSimple.setMd5(MD5Util.getMD5(task.getRequest_id()+task.getUsername()+task.getArticle_id()+comment_content));
					
					try {
						biDao.insert(commentSimple);
					} catch (Exception e) {
						logger.error("commentSimple插入失败");
					}
				}
				
				
				info.setArticle_category(article_category);
				info.setArticle_content(article_content);
				info.setArticle_img(article_img);
				info.setArticle_time(article_time);
				info.setArticle_readnum(article_readnum);
				info.setArticle_title(article_title);
				info.setArticle_time(article_time.replace("发表时间：", ""));
				info.setPublisher(publisher);
				try {
					biDao.insert(info);
				} catch (Exception e) {
					logger.error("articleinfo插入失败");
				}
				
				
				
			}
			
			
			
			
			
			
			mysqlDao.update(HaoDFArticleSimpleJob.class, Chain.make("status", 2), 
					Cnd.where("id", "=", task.getId()));
		} catch (Exception e) {
			mysqlDao.update(HaoDFArticleSimpleJob.class, Chain.make("status", 3), 
					Cnd.where("id", "=", task.getId()));
			logger.error("Articleinfo出错",e);
		}
	}
		public static String subString(String begin, String end, String content) {
			if (StringUtils.isEmpty(content)) {
				return content;
			}
			int index_s = 0;
			if (begin != null) {
				index_s = content.indexOf(begin) + begin.length();
			}
			String _end = content.substring(index_s, content.length());
			int index_e = 0;

			if (end == null) {
				index_e = content.length();
			} else {
				index_e = _end.indexOf(end);
			}
			return _end.substring(0, index_e);
		}

}
