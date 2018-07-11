package com.reptile.ly.task.ticket.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.ly.model.ticket.LyTicketCommentTagModel;
import com.reptile.ly.model.ticket.job.LyTicketCommentJob;
import com.reptile.tianya.common.CommonUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 同程门票-根据commentjob获取评论simple
 * @author zhour
 *
 */
public class LyTicketCommentThread extends Thread {

	/**
	 * 日志变量 *
	 */
	private static Logger logger = Logger.getLogger(LyTicketCommentThread.class);
	// dao层变量
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(Constant.ZNode);

	private LyTicketCommentJob job;

	public LyTicketCommentThread(Record job) {
		this.job = job.toPojo(LyTicketCommentJob.class);
	}

	public void run() {
		try {
			mysqlDao.update(LyTicketCommentJob.class, Chain.make("status", 1), Cnd.where("id", "=", job.getId()));
			this.parse();
		} catch (Exception e) {
			mysqlDao.update(LyTicketCommentJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("解析ly_ticket_comment异常：job：" + job.getKeyword(), e);
		}
	}
	
	
	public void parse() {

		LyTicketCommentJob ltcj = job;
		TaskToBi tobi = BIDataSource.getBiDataSource("ly", job.getProject_code(), job.getDown_type());
		Dao biDao = tobi.getBiDao();
		Map<String, String> header = new HashMap<String, String>();
		
		String webUrl = "https://www.ly.com/wanle/api/WanleProduct/GetCommentInfo?sort=3&lineId={0}&pageSize=5&isSingle=1&pageIndex={1}&_={2}";

		try {
			// 抓取json的url判断
			String url = ltcj.getUrl();
			
			
			if (url == "" || url == null) {
				url = MessageFormat.format(webUrl, ltcj.getSid(), ltcj.getPage_index(), new Date());
			}

			// 获取json文本
			header.put("Referer", "https://www.ly.com/dujia/wanle/"+ltcj.getSid()+".html");
			String result = HttpBase.get(url, "utf-8",header).getResult();
			System.out.println(result);
			// 解析json
			JSONObject json = new JSONObject(result);

			JSONArray cList = json.getJSONObject("Data").getJSONArray("CommentList");

			List<LyTicketCommentTagModel> commentList = new ArrayList<LyTicketCommentTagModel>();

			for (int i = 0; i < cList.length(); i++) {

				JSONObject com = (JSONObject) cList.get(i);

				/**
				 * 会员id *
				 */
				String nid = com.getString("Id");
				/**
				 * 会员昵称 *
				 */
				String nick = com.getString("Creator");
				/**
				 * 会员等级 *
				 */
				String grade = com.getString("UserLeavel");
				/**
				 * 评论内容 *
				 */
				String content = com.getString("Content");

				/**
				 * 评论图片地址 *
				 */
				String img_path = "";
				try {
					JSONArray img_paths = com.getJSONArray("ImageList");
					for (int j = 0; j < img_paths.length(); j++) {
						img_path = img_path + img_paths.getString(i) + ";";
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				// *************************************
				/**
				 * 旅游类型 *
				 */
				String travel_kind = com.getString("Purpose");
				/**
				 * 评论日期 *
				 */
				String comment_date = com.getString("DPTime");

				/** 当前时间 **/
				Date date = new Date();
				DateFormat c_format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Calendar rightNow = Calendar.getInstance();
					rightNow.setTime(date);

					if (comment_date.contains("今天")) {
						comment_date = c_format.format(rightNow.getTime());
					} else if (comment_date.contains("昨天")) {
						rightNow.add(Calendar.DAY_OF_YEAR, -1);
						comment_date = c_format.format(rightNow.getTime());
					} else if (comment_date.contains("前天")) {
						rightNow.add(Calendar.DAY_OF_YEAR, -2);
						comment_date = c_format.format(rightNow.getTime());
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				/**
				 * 评论来源 *
				 */
				String comment_src = com.getString("FromSite");
				/**
				 * 点赞数 *
				 */
				String agrees = com.getString("PraiseCount");
				/** 评论id **/
				String comment_id = CommonUtils.md5(ltcj.getPid() + nick + nid + content + comment_date);

				LyTicketCommentTagModel ltctm = new LyTicketCommentTagModel();

				ltctm.setSid(ltcj.getSid());
				ltctm.setSname(ltcj.getSname());
				ltctm.setPid(ltcj.getPid());
				ltctm.setPname(ltcj.getPname());
				ltctm.setKeyword(ltcj.getKeyword());
				ltctm.setTask_id(ltcj.getTask_id());
				ltctm.setRequest_id(ltcj.getRequest_id());

				ltctm.setUrl(ltcj.getWeb_url());

				ltctm.setComment_id(comment_id);

				ltctm.setPage_index(ltcj.getPage_index());
				ltctm.setNid(nid);
				ltctm.setNick(nick);
				ltctm.setGrade(grade);
				ltctm.setContent(content);
				ltctm.setImg_path(img_path);
				ltctm.setTravel_kind(travel_kind);
				ltctm.setComment_date(comment_date);
				ltctm.setComment_src(comment_src);
				ltctm.setAgrees(agrees);
				try {
					DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
					ltctm.setCreate_date(dateFormat.parse(dateFormat.format(date)));
				} catch (Exception e) {
					logger.error("comment_id:【" + comment_id + "】创建日期，建立失败");
					ltctm.setCreate_date(date);
				}
				ltctm.setCreate_time(date);

				commentList.add(ltctm);

			}

			try {
				if (commentList.size() > 0) {
					for (LyTicketCommentTagModel comment : commentList) {
						try {
							biDao.insert(comment);

							biDao.update(LyTicketCommentTagModel.class, Chain.make("img_path", comment.getImg_path()), Cnd.where("comment_id", "=", comment.getComment_id()));

							logger.info(commentList.indexOf(comment) + "/" + commentList.size() + "|||同程票务评论id:" + comment.getComment_id() + "插入成功|||");
						} catch (Exception e) {
							logger.error("同程票务评论id:" + comment.getComment_id() + "已经存在");
						}
					}
					mysqlDao.update(LyTicketCommentJob.class, Chain.make("status", 2), Cnd.where("id", "=", ltcj.getId()));
					logger.info("|||Url:【" + ltcj.getWeb_url() + "】的同程票务第" + ltcj.getPage_index() + "页评论解析成功|||");
				} else {
					mysqlDao.update(LyTicketCommentJob.class, Chain.make("status", 3), Cnd.where("id", "=", ltcj.getId()));
					logger.error("【" + job.getKeyword() + "】的同程票务第" + ltcj.getPage_index() + "页评论获取失败");
				}

			} catch (Exception e) {
				mysqlDao.update(LyTicketCommentJob.class, Chain.make("status", 3), Cnd.where("id", "=", ltcj.getId()));
				logger.error("【" + job.getKeyword() + "】的同程票务第" + ltcj.getPage_index() + "页评论插入失败", e);
			}

		} catch (Exception e) {
			mysqlDao.update(LyTicketCommentJob.class, Chain.make("status", 3), Cnd.where("id", "=", ltcj.getId()));
			logger.error("【" + job.getKeyword() + "】的同程票务评论信息获取失败", e);
		}
	}

}
