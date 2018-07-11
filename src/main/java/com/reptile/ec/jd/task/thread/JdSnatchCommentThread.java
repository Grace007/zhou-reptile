package com.reptile.ec.jd.task.thread;

import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.CommonUtils;
import com.bds.base.util.MD5Util;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.jd.job.JdProductJob;
import com.reptile.ec.jd.model.JdComment;
import com.reptile.ec.jd.model.JdProduct;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JdSnatchCommentThread extends Thread {

	private static Logger logger = Logger.getLogger(JdSnatchCommentThread.class);
	private static NutDao tDao = SpringContextHolder.getBean("tDao");
	private JdProductJob task;

	public JdSnatchCommentThread(JdProductJob task) {
		this.task = task;
	}

	public void run() {
		try {
			long time = System.currentTimeMillis();
			tDao.update(JdProductJob.class, Chain.make("comment_status", 1), Cnd.where("id", "=", task.getId()));
			Dao biDao = BIDataSource.getBiDataSource(task.getWebsite(), task.getProject_code(), "get_product_info")
					.getBiDao();
			Long requestId = task.getRequestId();
			Long taskId = task.getTaskId();
			String key = task.getKeyword();
			String cate = task.getCategory();

			String type = task.getType();
			String project_code = task.getProject_code();
			String website = task.getWebsite();
			String down_type = task.getDown_type();

			long t = System.currentTimeMillis();
			Long commentTotal = task.getComments();
			int maxPage = (int) ((Long.valueOf(commentTotal) - 1) / 30 + 1);
			if (maxPage > MyConstant.JD_CONFIG.PAGEMAX) {
				maxPage = MyConstant.JD_CONFIG.PAGEMAX;
			}
			logger.info("############获取评论页面耗时" + (System.currentTimeMillis() - t) + "####################,id:"
					+ task.getId());
			DateFormat DATA_FORMAT = new SimpleDateFormat("yyyyMMdd");
			for (int i = 0; i < maxPage; i++) {
				/** 中文推荐评论 */
//				JdCommentNextJob cct = new JdCommentNextJob();
//				Date d = new Date();
//				cct.setCreateTime(d);
//				cct.setCreateDate(DATA_FORMAT.format(d));
//				cct.setKeyword(key);
//				cct.setCategory(cate);
//				cct.setNode(task.getNode());
//				cct.setRequestId(requestId);
//				cct.setTaskId(taskId);
//				cct.setStatus(0);
//				cct.setPage_index(i);
//				cct.setPid(task.getPid());
//				cct.setPname(task.getPname());
//
//				cct.setType(type);
//				cct.setProject_code(project_code);
//				cct.setWebsite(website);
//				cct.setDown_type(down_type);
//
//				// cct.setUrl("http://club.jd.com/productpage/p-"+task.getPid()+"-s-0-t-3-p-"+i+".html?callback=fetchJSON_comment");
//				cct.setUrl("http://club.jd.com/review/" + task.getPid() + "-0-" + (i + 1) + "-0.html");
//				try {
//					tDao.fastInsert(cct);
//				} catch (Exception e) {
//					// logger.info("重复插入评论任务:" + i + ",pid:" + task.getPid());
//				}
//				// logger.info("插入评论任务:" + i + ",pid:" + task.getPid());
//				
				String url = "http://club.jd.com/review/" + task.getPid() + "-0-" + (i + 1) + "-0.html";
				String html = HttpBase.get(url, "utf-8").getResult();
				Document doc = null;
				doc = Jsoup.parse(html);
				Elements commentlist = doc.select("#comments-list").select(".mc").select(".item");
				for (Element comment : commentlist) {
					/** 评论者 */
					String nickname = null;
					try {
						nickname = comment.select(".user>.u-name").text();
					} catch (Exception e) {
						logger.info("获取评论者失败");
					}
					/** 评论者等级 */
					String userLevelName = "";
					try {
						userLevelName = comment.select(".user>.u-level").select("span").get(0).text();
					} catch (Exception e) {
						// logger.info("获取评论者等级失败");
					}
					/** 评论者地区 */
					String userProvince = null;
					try {
						userProvince = comment.select(".user>.u-level>.u-address").text();
					} catch (Exception e) {
						logger.info("获取地区失败");
					}
					/** 购买日期 */
					String referenceTime = null;
					try {
						referenceTime = comment.select(".i-item>.comment-content>.dl-extra").text().split("购买日期： ")[1];
					} catch (Exception e) {
						logger.info("获取购买日期失败");
					}
					/** 评论内容 */
					String content = null;
					try {
						content = comment.select(".i-item>.comment-content").select("dl").get(0).text();
					} catch (Exception e) {
						logger.info("获取评论内容失败");
					}
					/** 评分 */
					String score = null;
					try {
						score = comment.select(".i-item>.o-topic").select("span").get(0).attr("class").split("sa")[1];
					} catch (Exception e) {
						logger.info("获取评分失败");
					}
					/** 评论时间/日期 */
					String creationTime = null;
					try {
						creationTime = comment.select(".i-item>.o-topic>.date-comment").text();
					} catch (Exception e) {
						logger.info("获取评论日期失败");
					}
					/** 优点 */
					String pros;
					/** 不足 */
					String cons;
					/** 用户晒单（照片） */
					String[] shiningImages;
					/** （此评价对我）有用/赞/顶 */
					String usefulVoteCount = null;
					try {
						usefulVoteCount = CommonUtils.subString("(", ")",
								comment.select(".i-item>.btns>.useful").text());
					} catch (Exception e) {
						logger.info("获取有用评论数失败");
					}
					/** （此评价对我）没用/踩 */
					String uselessVoteCount;
					/** （评论）回复 */
					String replyCount = null;
					try {
						replyCount = CommonUtils.subString("(", ")", comment.select(".i-item>.btns>.btn-reply").text());
					} catch (Exception e) {
						logger.info("获取回复数失败");
					}
					String md5 = content + "-" + creationTime + "-" + nickname;
					JdComment jdComment = new JdComment();
					jdComment.setNickname(nickname);
					jdComment.setProductId(task.getPid());
					jdComment.setProductName(task.getPname());
					jdComment.setContent(content);
					jdComment.setScore(score);
					jdComment.setUserLevelName(userLevelName.replace(userProvince, ""));
					jdComment.setUserProvince(userProvince);
					jdComment.setReferenceTime(referenceTime);
					jdComment.setCreationTime(creationTime);
					jdComment.setUsefulVoteCount(usefulVoteCount);
					jdComment.setReplyCount(replyCount);
					jdComment.setCreateTime(new Date());
					jdComment.setKey(task.getKeyword());
					jdComment.setCategory(task.getCategory());
					jdComment.setRequestId(task.getRequestId());
					jdComment.setTaskId(task.getTaskId());
					jdComment.setTop(0);
					jdComment.setId(MD5Util.getMD5(md5)+"123");
					try {
						biDao.fastInsert(jdComment);
					} catch (Exception e) {
						//logger.info("评论已存在");
						logger.info(e);
					}
				}
				logger.info("第"+(i+1)+"页评论抓取完成");
			}
			logger.info("产品评论解析完成,用时："+(System.currentTimeMillis()-time));
			tDao.update(JdProductJob.class, Chain.make("comment_status", 2), Cnd.where("id", "=", task.getId()));
		}catch(Exception e){
			logger.info(e);
			tDao.update(JdProductJob.class, Chain.make("comment_status", 3), Cnd.where("id", "=", task.getId()));
		}
			
	}

	public static String getCommentUrl(String pid, int index) {
		if (index < 1) {
			index = 1;
		}
		// return PREFIX_URL + pid + SUFFIX_URL + index + LAST_URL;
		return "http://club.jd.com/review/" + pid + "-0-" + index + "-0.html";
		// return
		// "http://club.jd.com/productpage/p-"+pid+"-s-0-t-3-p-"+index+".html?callback=fetchJSON_comment";
	}

	public static void main(String args[]) throws Exception {
		JdProduct jp = new JdProduct();
		jp.setUrl("http://item.jd.com/10097993708.html");
		jp.setPid("10097993708");

	}

}
