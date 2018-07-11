package com.reptile.ly.task.hotel.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.ly.model.hotel.LyGHotelCommentModel;
import com.reptile.ly.model.hotel.LyGHotelInfoModel;
import com.reptile.ly.model.hotel.job.LyGHotelUrlJob;
import org.apache.commons.lang3.StringUtils;
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
import org.nutz.dao.entity.Record;

import java.util.Date;


/**
 * 同程酒店-根据url获取酒店的info和comment
 * @author zhour
 *
 */
public class LyGHotelUrlThread extends Thread {

	private static Logger logger = Logger.getLogger(LyGHotelUrlThread.class);

	// dao层变量
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(Constant.ZNode);
	// Nu mysqlDao mysqlDao = new Nu mysqlDao();

	private LyGHotelUrlJob job;

	private int totalComment = 0;

	public LyGHotelUrlThread(Record task) {
		this.job = task.toPojo(LyGHotelUrlJob.class);
	}

	public void run() {
		try {
			mysqlDao.update(LyGHotelUrlJob.class, Chain.make("status", 1), Cnd.where("id", "=", job.getId()));
			TaskToBi tobi = BIDataSource.getBiDataSource("ly", job.getProject_code(), job.getDown_type());
			Dao biDao = tobi.getBiDao();
			Document doc = null;
			if (job.getInfo_status().equals(0)) {
				doc = this.getGHotelInfo(tobi);
			}
			String url = job.getUrl();
			try {
				String html = null;
				if (doc == null) {

					html = HttpBase.get(url, "utf-8").getResult();
					doc = Jsoup.parse(html);
					String comment_num = "0";

					Elements commentInfos = doc.select("div.comment_wrap span");
					for (Element info : commentInfos) {
						String title = info.select("em.c-333").text();
						if (StringUtils.isNotEmpty(title)) {
							if (title.equals("全部")) {
								comment_num = info.select("em.kuo label").text();
							}
						}
					}
					this.calPage(comment_num);
				}
				String HotelName = doc.select("span.HotelName").text();
				int pageIndex = 1;
				Integer maxPage = null;
				String pid = url.substring(url.lastIndexOf("-") + 1, url.length());

				if (totalComment > 0) {
					maxPage = (totalComment - 1) / 10 + 1;
				}

				do {
					String comment_url = "https://ghotel.ly.com/HotelDetail/HotelComments?HotelID=" + pid
							+ "&Type=1&pageSize=10&pageIndex=" + pageIndex + "&_=" + new Date().getTime();
					String commentHtml = HttpBase.get(comment_url, "utf-8").getResult();

					if (StringUtils.isNotEmpty(commentHtml)) {
						JSONObject commentJson = new JSONObject(commentHtml);
						JSONArray array = commentJson.getJSONArray("Comments");

						for (int i = 0; i < array.length(); i++) {
							JSONObject json = array.getJSONObject(i);
							String content = json.getString("Comment");
							String level = json.getString("UserLevel");
							String feel = json.getString("CommentGradeDesc");
							String username = json.getString("MemberName");
							String comment_date = json.getString("CommentDate");
							String TravelType = json.getString("TravelType");
							String Comment_from = json.getString("CommentSource");
							String reply = json.getString("TCReply");
							JSONArray imgs = json.getJSONArray("TCcommentImgUrl");
							String imgurl = "";
							if (imgs != null && imgs.length() > 0) {
								for (int j = 0; j < imgs.length(); j++) {
									imgurl = imgurl + imgs.getJSONObject(j).getString("imgUrl") + ";";
								}
							}
							String comment_id = username + "-" + comment_date + "-" + content;
							String comment_uuid = MD5Util.getMD5(comment_id);

							LyGHotelCommentModel comment = new LyGHotelCommentModel();
							comment.setComment_id(comment_uuid);
							comment.setComment_date(comment_date);
							comment.setComment_src(Comment_from);
							comment.setContent(content);
							comment.setCreate_date(new Date());
							comment.setCreate_time(new Date());
							comment.setFeel(feel);
							comment.setGrade(level);
							comment.setImg_path(imgurl);
							comment.setNick(username);
							comment.setPid(pid);
							comment.setPname(HotelName);
							comment.setResponse(reply);
							comment.setTravel_type(TravelType);
							comment.setUrl(url);
							try {
								biDao.fastInsert(comment);
							} catch (Exception e) {
								// logger.error("评论重复"+e.getMessage());
							}
						}
						mysqlDao.update(LyGHotelUrlJob.class, Chain.make("comment_index", pageIndex),
								Cnd.where("id", "=", job.getId()));
						logger.info("###########下载完成" + HotelName + "的第" + pageIndex + "页评论#######");

					}

					pageIndex++;
				} while (pageIndex <= maxPage);

				mysqlDao.update(LyGHotelUrlJob.class, Chain.make("status", 2), Cnd.where("id", "=", job.getId()));
			} catch (Exception e) {
				mysqlDao.update(LyGHotelUrlJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
				logger.error("", e);
			}

		} catch (Exception e) {
			mysqlDao.update(LyGHotelUrlJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("解析ly_hotel异常：关键词：" + job.getUrl(), e);
		}
	}

	private Document getGHotelInfo(TaskToBi tobi) {
		Dao biDao = tobi.getBiDao();

		String url = job.getUrl();
		// dr = HttpBase.get(url, "utf-8")l

		try {
			String html = HttpBase.get(url, "utf-8").getResult();
			// dr.get(url);

			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// String html = dr.getPageSource();
			Document doc = Jsoup.parse(html);

			String HotelName = doc.select("span.HotelName").text();
			Elements zhuans = doc.select("label.ImageIcon.icon_zuan");
			String zuan = null;
			if (zhuans != null && zhuans.size() > 0) {
				zuan = zhuans.size() + "";
			}
			String address = null;
			Elements addrs = doc.select("div.hotel-wrap p");
			if (addrs != null) {
				address = addrs.first().ownText();
			}
			String scroe = doc.select("div.hc-detail  div.dpSourceText span.source").text();
			if (StringUtils.isNotEmpty(scroe)) {
				scroe = scroe.split(" ")[0];
			}

			String comment_num = "0";
			String good_num = "0";
			String middle_num = "0";
			String bad_num = "0";
			Elements commentInfos = doc.select("div.comment_wrap span");
			for (Element info : commentInfos) {
				String title = info.select("em.c-333").text();
				if (StringUtils.isNotEmpty(title)) {
					if (title.equals("全部")) {
						comment_num = info.select("em.kuo label").text();
					} else if (title.equals("好评")) {
						good_num = info.select("em.kuo label").text();
					} else if (title.equals("中评")) {
						middle_num = info.select("em.kuo label").text();
					} else if (title.equals("差评")) {
						bad_num = info.select("em.kuo label").text();
					}
				}
			}

			Integer good_comment = 0;
			Integer middle_comment = 0;
			Integer bad_comment = 0;
			String pid = url.substring(url.lastIndexOf("-") + 1, url.length());
			try {
				good_comment = Integer.parseInt(good_num);
			} catch (Exception e) {

			}

			try {
				middle_comment = Integer.parseInt(middle_num);
			} catch (Exception e) {

			}

			try {
				bad_comment = Integer.parseInt(bad_num);
			} catch (Exception e) {

			}

			this.calPage(comment_num);
			LyGHotelInfoModel info = new LyGHotelInfoModel();
			info.setUrl(url);
			info.setAll_comment(this.totalComment);
			info.setGood_comment(good_comment);
			info.setMiddle_comment(middle_comment);
			info.setBad_comment(bad_comment);
			info.setCreate_date(new Date());
			info.setCreate_time(new Date());
			info.setPname(HotelName);
			info.setScore(scroe);
			info.setZuan(zuan);
			info.setPid(pid);
			info.setAddress(address);
			try {
				biDao.fastInsert(info);
				logger.info("###########下载完成" + HotelName + "的info信息#######");
			} catch (Exception e) {
				logger.error("info重复" + e.getMessage());
			}
			mysqlDao.update(LyGHotelUrlJob.class, Chain.make("info_status", 2), Cnd.where("id", "=", job.getId()));
			return doc;
		} catch (Exception e) {
			mysqlDao.update(LyGHotelUrlJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("", e);
		}
		return null;

	}

	private int calPage(String comment_num) {

		totalComment = 0;

		try {
			totalComment = Integer.parseInt(comment_num);

		} catch (Exception e) {
			logger.error("", e);
		}
		return totalComment;
	}

}
