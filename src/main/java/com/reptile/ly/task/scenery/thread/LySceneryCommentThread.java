package com.reptile.ly.task.scenery.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.ly.model.scenery.LySceneryCommentTagModel;
import com.reptile.ly.model.scenery.job.LySceneryCommentJob;
import com.reptile.tianya.common.CommonUtils;
import org.apache.commons.lang.StringUtils;
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
 * 同程景点-根据commentjob获取评论simple
 * @author zhour
 *
 */
public class LySceneryCommentThread extends Thread {

	/**
	 * 日志变量 *
	 */
	private static Logger logger = Logger.getLogger(LySceneryCommentThread.class);
	// dao层变量
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(Constant.ZNode);

	private LySceneryCommentJob job;

	public LySceneryCommentThread(Record task) {
		this.job = task.toPojo(LySceneryCommentJob.class);
	}

	public void run() {
		try {
			mysqlDao.update(LySceneryCommentJob.class, Chain.make("status", 1), Cnd.where("id", "=", job.getId()));
			this.parse();
		} catch (Exception e) {
			mysqlDao.update(LySceneryCommentJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("解析ly_scenery_comment异常：job：" + job.getKeyword(), e);
		}
	}

	public void parse() {

		LySceneryCommentJob lscj = job;

		TaskToBi tobi = BIDataSource.getBiDataSource("ly", job.getProject_code(), job.getDown_type());
		Dao biDao = tobi.getBiDao();

		String webUrl = "http://www.ly.com/scenery/AjaxHelper/DianPingAjax.aspx?action=GetDianPingList&sid={0}&page={1}&pageSize=10&labId=1&sort=0";

		try {
			// 抓取json的url判断
			String url = lscj.getUrl();

			if (StringUtils.isBlank(lscj.getUrl())) {
				url = MessageFormat.format(webUrl, lscj.getSid(), lscj.getPage_index());
			}

			Map<String, String> headers = new HashMap<String, String>();

			headers.put("Host", "www.ly.com");
			headers.put("Referer", lscj.getWeb_url());

			// 获取json文本
			String result = HttpBase.get(url, "utf-8", headers).getResult();
			// 解析json
			JSONObject json = new JSONObject(result);

			JSONArray list = json.getJSONArray("dpList");

			List<LySceneryCommentTagModel> commentList = new ArrayList<LySceneryCommentTagModel>();

			for (int i = 0; i < list.length(); i++) {

				JSONObject comment = (JSONObject) list.get(i);

				/**
				 * 会员id *
				 */
				String nid = comment.getString("userId");
				/**
				 * 会员昵称 *
				 */
				String nick = comment.getString("dpUserName");
				/**
				 * 会员等级 *
				 */
				String grade = comment.getString("DPUserLevel");
				/**
				 * 评论内容 *
				 */
				String content = comment.getString("dpContent");

				/**
				 * 评论图片地址 *
				 */
				String img_path = "";

				// JSONArray photos = comment.getJSONArray("dpImgUrl");
				//
				// for (int j = 0; j < photos.length(); j++) {
				// img_path = img_path +
				// photos.getJSONObject(j).getString("imgUrl") + ";";
				// }
				// *************************************
				/**
				 * 评论日期 *
				 */
				String comment_date = comment.getString("dpDate");

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				Date com_date = new Date();

				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(com_date);

				if (comment_date.trim().equals("今天")) {
					comment_date = dateFormat.format(rightNow.getTime());
				} else if (comment_date.trim().equals("昨天")) {
					rightNow.add(Calendar.DAY_OF_YEAR, -1);
					comment_date = dateFormat.format(rightNow.getTime());
				} else if (comment_date.trim().equals("前天")) {
					rightNow.add(Calendar.DAY_OF_YEAR, -2);
					comment_date = dateFormat.format(rightNow.getTime());
				}
				/**
				 * 评论来源 *
				 */
				String comment_src = comment.getString("DPSite"); // == 1 ?
																	// "发表于同程旅游网页版"
																	// :
																	// "发表于同程旅游手机客户端";
				/**
				 * 点赞数 *
				 */
				String agrees = comment.getString("zanCount");
				/**
				 * 回复 *
				 */
				String response = "";

				JSONArray csReplyList = comment.getJSONArray("csReplyList");
				if (csReplyList !=null) {
					for (int j = 0; j < csReplyList.length(); j++) {
						response = response + csReplyList.getJSONObject(j).getString("replyContent");
					}
				}
				
				/** 评论id **/
				String comment_id = CommonUtils.md5(lscj.getPid() + "_" + comment.getString("dpId")
						+ comment.getString("homeId") + content + nick + comment_date);

				LySceneryCommentTagModel lsctm = new LySceneryCommentTagModel();

				lsctm.setSid(lscj.getSid());
				lsctm.setSname(lscj.getSname());
				lsctm.setPid(lscj.getPid());
				lsctm.setPname(lscj.getPname());
				lsctm.setKeyword(lscj.getKeyword());
				lsctm.setTask_id(lscj.getTask_id());
				lsctm.setRequest_id(lscj.getRequest_id());
				lsctm.setComment_id(comment_id);

				lsctm.setPage_index(lscj.getPage_index());
				lsctm.setUrl(lscj.getWeb_url());
				lsctm.setNid(nid);
				lsctm.setNick(nick);
				lsctm.setGrade(grade);
				lsctm.setContent(content);
				// lsctm.setImg_path(img_path);
				lsctm.setComment_date(comment_date);
				lsctm.setComment_src(comment_src);
				lsctm.setAgrees(agrees);
				lsctm.setResponse(response);
				lsctm.setImg_path(img_path);

				/** 当前时间 **/
				Date date = new Date();
				try {
					lsctm.setCreate_date(dateFormat.parse(dateFormat.format(date)));
				} catch (Exception e) {
					logger.error("comment_id:【" + comment_id + "】创建日期，建立失败");
					lsctm.setCreate_date(date);
				}
				lsctm.setCreate_time(date);

				commentList.add(lsctm);
			}

			try {
				if (commentList.size() > 0) {
					for (int i = 0; i < commentList.size(); i++) {
						try {
							biDao.insert(commentList.get(i));
							logger.info(i + "/" + commentList.size() + "|||同程景点评论id:"
									+ commentList.get(i).getComment_id() + "插入成功|||");
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("同程景点评论id:" + commentList.get(i).getComment_id() + "已经存在");
						}
					}
					mysqlDao.update(LySceneryCommentJob.class, Chain.make("status", 2),
							Cnd.where("id", "=", lscj.getId()));
					logger.info("|||Url:【" + lscj.getWeb_url() + "】的同程景点第" + lscj.getPage_index() + "页评论解析成功|||");
				} else {
					mysqlDao.update(LySceneryCommentJob.class, Chain.make("status", 3),
							Cnd.where("id", "=", lscj.getId()));
					logger.error("【" + job.getKeyword() + "】的同程景点第" + lscj.getPage_index() + "页评论获取失败");
				}
			} catch (Exception e) {
				mysqlDao.update(LySceneryCommentJob.class, Chain.make("status", 3), Cnd.where("id", "=", lscj.getId()));
				logger.error("【" + job.getKeyword() + "】的同程景点第" + lscj.getPage_index() + "页评论插入失败", e);
				// e.printStackTrace();
			}

		} catch (Exception e) {
			mysqlDao.update(LySceneryCommentJob.class, Chain.make("status", 3), Cnd.where("id", "=", lscj.getId()));
			logger.error("【" + job.getKeyword() + "】的同程景点评论信息获取失败", e);
			// e.printStackTrace();
		}

	}

}
