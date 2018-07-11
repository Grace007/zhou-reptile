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
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JdCommentThread extends Thread {

	private static Logger logger = Logger.getLogger(JdCommentThread.class);
	private static NutDao tDao = SpringContextHolder.getBean("tDao");
	private JdProductJob task;

	public JdCommentThread(JdProductJob task) {
		this.task = task;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public void run() {
		try {
			long time = System.currentTimeMillis();
			Dao biDao = BIDataSource.getBiDataSource(task.getWebsite(), task.getProject_code(), "get_product_info")
					.getBiDao();
			tDao.update(JdProductJob.class, Chain.make("comment_status", 1), Cnd.where("id", "=", task.getId()));
			// Dao biDao = BIDataSource.getBiDataSource(task.getWebsite(),
			// task.getProject_code(), "get_product_info")
			// .getBiDao();
			int curPage = 1;
			try {
				curPage = this.task.getCommentsPage();
			} catch (Exception e1) {
				curPage = 1;
			}
			Long commentTotal = task.getComments();
			int maxPage = 0;
			if (commentTotal % 30 == 0) {
				maxPage = (int) (Long.valueOf(commentTotal) / 30);
			} else {
				maxPage = (int) (Long.valueOf(commentTotal) / 30 + 1);
			}
			// int maxPage = (int) ((Long.valueOf(commentTotal) - 1) / 10 + 1);
			if (maxPage > MyConstant.JD_CONFIG.PAGEMAX) {
				maxPage = MyConstant.JD_CONFIG.PAGEMAX;
			}

			for (int i = curPage; i <= maxPage; i++) {
				long time1 = System.currentTimeMillis();
				/** 中文推荐评论 */
				String commentUrl = "http://club.jd.com/review/" + task.getPid() + "-0-" + i + "-0.html";// 每页30个评论
				try {
					// this.getCommentHeml(commentUrl, i);
					String html = HttpBase.get(commentUrl, "utf-8").getResult();
					logger.info("解析耗时：" + (System.currentTimeMillis() - time1));
					// Thread.sleep(8000);
					if ((!html.contains("全部评价")) && (!html.contains("全部商品评价"))) {
						// 错误页面，跳出
						tDao.update(JdProductJob.class, Chain.make("comment_status", 0).add("commentsPage", i),
								Cnd.where("id", "=", task.getId()));
						logger.info("产品url:" + commentUrl + ",第" + i + "页没有获取到评论");
						if (MyConstant.JD_CONFIG.CHANGE_IP) {
							cutAdsl(MyConstant.AdslTitle);
							// 再连，分配一个新的IP
							connAdsl(MyConstant.AdslTitle, MyConstant.AdslName, MyConstant.AdslPwd);
						}
						return;
					} else {
						Document doc = null;
						doc = Jsoup.parse(html);
						Elements commentlist = doc.select("#comments-list").select(".mc").select(".item");
						if (commentlist.size() == 0) {
							commentlist = doc.select("#comment").select(".mc").select(".item");
							if (commentlist.size() == 0) {
								continue;
							}
						}
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
							String userProvince = "";
							try {
								userProvince = comment.select(".user>.u-level>.u-address").text();
							} catch (Exception e) {
								logger.info("获取地区失败");
							}
							if ("".equals(userProvince)) {
								try {
									userProvince = comment.select(".user>.u-address").text();
								} catch (Exception e) {
									logger.info("获取地区失败");
								}
							}
							/** 购买日期 */
							// String referenceTime = "";
							// try {
							// referenceTime =
							// comment.select(".i-item>.comment-content>.dl-extra").text()
							// .split("购买日期： ")[1];
							// } catch (Exception e) {
							// logger.info("获取购买日期失败");
							// }
							// if ("".equals(referenceTime)) {
							// try {
							// referenceTime =
							// comment.select(".user>.date-buy").text().split("购买日期：
							// ")[1];
							// } catch (Exception e) {
							// logger.info("获取购买日期失败");
							// }
							// }
							/** 评论内容 */
							String content = "";
							try {
								if (comment.select(".i-item>.comment-content").select("dl").get(0).text()
										.contains("心　　得")) {
									content = comment.select(".i-item>.comment-content").select("dl").get(0).text();
								} else {
									content = comment.select(".i-item>.comment-content").select("dl").get(1).text();
								}
							} catch (Exception e) {
								// logger.info("获取评论内容失败");
							}
							if ("".equals(content)) {
								try {
									content = comment.select(".i-item>.comment-content").text();
								} catch (Exception e) {
									// logger.info("获取评论内容失败");
								}
							}
							/** 评分 */
							String score = null;
							try {
								score = comment.select(".i-item>.o-topic").select("span").get(0).attr("class")
										.split("sa")[1];
							} catch (Exception e) {
								// logger.info("获取评分失败");
							}
							/** 评论时间/日期 */
							String creationTime = null;
							try {
								creationTime = comment.select(".i-item>.o-topic>.date-comment").text();
								try {
									Date date = sdf.parse(creationTime);
									if (date.getTime() > System.currentTimeMillis()) {
										logger.info("评论日期:[" + creationTime + "]超过当前日期:["
												+ sdf.format(new Date(System.currentTimeMillis())) + "],跳过");
										continue;
									}
								} catch (Exception e) {
									logger.error("", e);
								}
							} catch (Exception e) {
								// logger.info("获取评论日期失败");
							}
							/** 优点 */
							String pros;
							/** 不足 */
							String cons;
							/** 用户晒单（照片） */
							String[] shiningImages;
							/** （此评价对我）有用/赞/顶 */
							String usefulVoteCount = "";
							try {
								usefulVoteCount = CommonUtils.subString("(", ")",
										comment.select(".i-item>.btns>.useful").text());
							} catch (Exception e) {
								// logger.info("获取有用评论数失败");
							}
							if ("".equals(usefulVoteCount)) {
								try {
									usefulVoteCount = CommonUtils.subString("(", ")",
											comment.select(".btn-agree").text());
								} catch (Exception e) {
									// logger.info("获取有用评论数失败");
								}
							}
							/** （此评价对我）没用/踩 */
							String uselessVoteCount = null;
							try {
								uselessVoteCount = CommonUtils.subString("(", ")",
										comment.select(".btn-oppose").text());
							} catch (Exception e1) {
								// logger.info("获取没用数失败");
							}
							/** （评论）回复 */
							String replyCount = null;
							try {
								replyCount = CommonUtils.subString("(", ")",
										comment.select(".i-item>.btns>.btn-reply").text());
							} catch (Exception e) {
								// logger.info("获取回复数失败");
							}
							// 颜色
							String color = "";
							try {
								color = comment.select(".dl-extra").text();
							} catch (Exception e1) {

							}
							String md5 = task.getPid() + "-" + content + "-" + creationTime + "-" + nickname + "-"
									+ task.getRequestId();
							JdComment jdComment = new JdComment();
							jdComment.setNickname(nickname);
							jdComment.setProductId(task.getPid());
							jdComment.setProductName(task.getPname());
							jdComment.setContent(content);
							jdComment.setScore(score);
							jdComment.setUserLevelName(userLevelName.replace(userProvince, ""));
							jdComment.setUserProvince(userProvince);
							jdComment.setCreationTime(creationTime);
							jdComment.setUsefulVoteCount(usefulVoteCount);
							jdComment.setUselessVoteCount(uselessVoteCount);
							jdComment.setReplyCount(replyCount);
							jdComment.setCreateTime(new Date());
							jdComment.setColor(color);
							jdComment.setKey(task.getKeyword());
							jdComment.setCategory(task.getCategory());
							jdComment.setRequestId(task.getRequestId());
							jdComment.setTaskId(task.getTaskId());
							jdComment.setTop(0);
							jdComment.setId(MD5Util.getMD5(md5));
							try {
								biDao.fastInsert(jdComment);
							} catch (Exception e) {
								// logger.info("第" + i + "页评论已存在");
							}
						}
					}
					logger.info("###########已經完成," + task.getPid() + "第" + i + "頁下載，耗时："
							+ (System.currentTimeMillis() - time1) + "#############");
				} catch (Exception e) {
					logger.error("************" + commentUrl + "**************");
				}
				tDao.update(JdProductJob.class, Chain.make("commentsPage", i), Cnd.where("id", "=", task.getId()));

				////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// String commentUrl = "http://club.jd.com/productpage/p-" +
				// task.getPid() + "-s-0-t-3-p-" + (i - 1)
				// + ".html?callback=fetchJSON_comment";// 每页10个评论
				// try {
				// Map<String, String> header = new HashMap<String, String>();
				// header.put("Host", "club.jd.com");
				// header.put("Pragma", "no-cache");
				// header.put("Referer", "http://item.jd.com/" + task.getPid() +
				// ".html");
				// String html = HttpBase.get(commentUrl, "utf8",
				// header).getResult();
				//
				// int begin = html.indexOf("(") + 1;
				// int end = html.lastIndexOf(")");
				// JSONObject json = null;
				// json = new
				// JSONObject(com.bds.common.CommonUtils.subStringJd("fetchJSON_comment(",
				// ");", html));
				// List<JdComment> commentList = parseComments(i, json,
				// task.getPid(), task.getPname(),
				// task.getKeyword(), task.getCategory(), task.getTaskId(),
				// task.getRequestId());
				// for (JdComment jc : commentList) {
				// try {
				// biDao.fastInsert(jc);
				// } catch (Exception e) {
				// // e.printStackTrace();
				// }
				// }
				// logger.info("###########已經完成," + task.getPid() + "第" + i +
				// "頁下載#############");
				// tDao.update(JdProductJob.class, Chain.make("commentsPage",
				// i), Cnd.where("id", "=", task.getId()));
				// // double sleep = 10000 * Math.random();
				// // Thread.sleep((long) sleep);
				// } catch (Exception e) {
				// String msg = "获取JdCommentPage失败.pid[" + task.getPid() +
				// "],index[" + i + "],key["
				// + task.getKeyword() + "],url[" + commentUrl + "],taskId[" +
				// task.getTaskId()
				// + "],requestId[" + task.getRequestId() + "]";
				// // logger.error(msg);
				// tDao.update(JdProductJob.class, Chain.make("comment_status",
				// 0).add("commentsPage", i),
				// Cnd.where("id", "=", task.getId()));
				// logger.info("产品id:" + task.getId() + "第" + i + "页没有获取到评论");
				// return;
				// // throw new RuntimeException(msg, e);
				// }
			}

			tDao.update(JdProductJob.class, Chain.make("comment_status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("|||||||||||||||||||||完成评论抓取|||||||||||||||||||||,id:" + task.getId() + ",总耗时："
					+ (System.currentTimeMillis() - time));
		} catch (Exception e) {
			logger.error("", e);
			tDao.update(JdProductJob.class, Chain.make("comment_status", 3), Cnd.where("id", "=", task.getId()));
		}
	}

	private void getCommentHeml(String url, int i) throws Exception {

	}

	private List<JdComment> parseComments(int index, JSONObject json, String pid, String pname, String key,
			String category, Long taskId, Long requestId) throws Exception {
		List<JdComment> commentList = new ArrayList<JdComment>();

		JdComment comment;
		Elements tmps;
		JSONObject e;
		Element tmp;
		String commentId;
		JSONArray comments = json.getJSONArray("comments");
		int size = comments.length();

		/** 评论者ID */
		String nid;
		/** 评论者等级 */
		String userLevelName;
		/** 评论者地区 */
		String userProvince;
		/** 购买日期 */
		String referenceTime;
		/** 评论内容 */
		String content;
		/** 评分 */
		String score;
		/** 评论时间/日期 */
		String creationTime;
		/** 优点 */
		String pros;
		/** 不足 */
		String cons;
		/** 用户晒单（照片） */
		String[] shiningImages;
		/** （此评价对我）有用/赞/顶 */
		String usefulVoteCount;
		/** （此评价对我）没用/踩 */
		String uselessVoteCount;
		/** （评论）回复 */
		String replyCount;
		/** 评论的回复 */
		// List<JdReplie> replies = null;
		// JdReplie jr;
		String tt;
		for (int i = 0; i < size; i++) {
			e = comments.getJSONObject(i);

			commentId = pid + "_" + e.getString("id");
			String md5 = task.getPid() + "-" + e.getString("content") + "-" + e.getString("creationTime") + "-"
					+ e.getString("nickname");
			comment = new JdComment(MD5Util.getMD5(md5), pid, pname, key, category, requestId, taskId,
					new Date());
			comment.setIndexPage(index);
			comment.setNickname(e.getString("nickname"));
			comment.setUserLevelName(e.getString("userLevelName"));
			comment.setUserProvince(e.getString("userProvince"));
			comment.setTop(e.getBoolean("isTop") ? 1 : 0);
			// 优点
			// comment.setPros(pros);
			// 不足
			// comment.setCons(cons);
			// 评论内容(使用心得)
			comment.setContent(e.getString("content"));
			// 颜色
			if (e.has("productColor")) {
				comment.setColor(e.getString("productColor"));
			}
			// 尺码
			if (e.has("productSize")) {
				comment.setVersion(e.getString("productSize"));
			}
			// 晒单
			if (e.has("images")) {
				JSONArray images = e.getJSONArray("images");
				if (images != null && images.length() > 0) {
					shiningImages = new String[images.length()];
					for (int a = 0; a < images.length(); a++) {
						shiningImages[a] = images.getJSONObject(a).getString("imgUrl");
					}
					comment.setShiningImages(shiningImages);
				}
			}
			// 购买日期
			comment.setReferenceTime(e.getString("referenceTime"));

			// （此评价对我）有用/赞/顶
			comment.setUsefulVoteCount(e.getString("usefulVoteCount"));
			// （此评价对我）没用/踩
			uselessVoteCount = "0";
			comment.setUselessVoteCount(e.getString("uselessVoteCount"));
			// （评论）回复
			comment.setReplyCount(e.getString("replyCount"));
			// 评论日期
			comment.setCreationTime(e.getString("creationTime"));
			// 评分
			comment.setScore(e.getString("score"));

			// replies = new ArrayList<JdReplie>();
			// tmps = e.select("div.none");
			// int t_size = tmps.size();
			// for (int a = 0; a < t_size; a++) {
			// tmp = tmps.get(a);
			// if (tmp.attr("class").equals("item-reply none")) {
			// jr = new JdReplie();
			// /** 父评论ID */
			// jr.setCommentId(commentId);
			// /** 回复ID */
			// Element fl = tmp.select("div.fl").first();
			// if(fl != null){
			// jr.setId(pid+"_"+fl.attr("rid"));
			// /** 回复者ID */
			// jr.setUid(fl.attr("ud"));
			// /** 回复序数 */
			// jr.setIndex(tmp.select("strong").text());
			// /** 回复时间/日期 */
			// jr.setCreationTime(tmp.select(
			// "div.reply-meta span.reply-left").text());
			// /** 回复内容 */
			// jr.setContent(tmp.select("div.reply-con span.u-con")
			// .text());
			// }
			// replies.add(jr);
			// }
			// }
			// comment.setReplies(replies);
			commentList.add(comment);
		}
		return commentList;
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

	/**
	 * 执行CMD命令,并返回String字符串
	 */
	public static String executeCmd(String strCmd) throws Exception {
		Process p = Runtime.getRuntime().exec("cmd /c " + strCmd);
		StringBuilder sbCmd = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			sbCmd.append(line + "\n");
		}
		return sbCmd.toString();
	}

	/**
	 * 连接ADSL
	 */
	public static boolean connAdsl(String adslTitle, String adslName, String adslPass) throws Exception {
		System.out.println("正在建立连接.");
		String adslCmd = "rasdial " + adslTitle + " " + adslName + " " + adslPass;
		String tempCmd = executeCmd(adslCmd);
		// 判断是否连接成功
		if (tempCmd.indexOf("已连接") > 0) {
			System.out.println("已成功建立连接.");
			return true;
		} else {
			System.err.println(tempCmd);
			System.err.println("建立连接失败");
			return false;
		}
	}

	/**
	 * 断开ADSL
	 */
	public static boolean cutAdsl(String adslTitle) throws Exception {
		String cutAdsl = "rasdial " + adslTitle + " /disconnect";
		String result = executeCmd(cutAdsl);

		if (result.indexOf("没有连接") != -1) {
			System.err.println(adslTitle + "连接不存在!");
			return false;
		} else {
			System.out.println("连接已断开");
			return true;
		}
	}

	public static void main(String args[]) throws Exception {
		JdProduct jp = new JdProduct();
		jp.setUrl("http://item.jd.com/10097993708.html");
		jp.setPid("10097993708");

	}

}
