package com.reptile.util.WaimaiNodesDivideUtils.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.reptile.util.WaimaiNodesDivideUtils.job.Nodes;
import com.reptile.util.WaimaiNodesDivideUtils.job.QueryStatusRS;
import com.reptile.util.WaimaiNodesDivideUtils.job.Waimai_task_list;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import java.util.Date;
import java.util.List;

public class ShopKeywordDivideThread extends Thread {
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private static Logger logger = Logger.getLogger(ShopKeywordDivideThread.class);
	private Waimai_task_list task;

	public ShopKeywordDivideThread(Waimai_task_list task) {
		this.task = task;
	}

	public void run() {
		tDao.update(Waimai_task_list.class, Chain.make("divide_shopkeyword_or_not", 1),
				Cnd.where("id", "=", task.getId()));
		int cr = getCommentRemaining(task.getWebsite(), task.getTask_id(), task.getRequest_id());
		String cs = getCommentStatus(task.getWebsite(), task.getTask_id(), task.getRequest_id());
		updateTaskList(task.getWebsite(), task.getRequest_id(), task.getTask_id(), task.getDescription(), cr, cs);
	}

	public void updateTaskList(String website, long request_id, long task_id, String description, int comment_remaining,
			String comment_status) {
		long time = System.currentTimeMillis();
		String nodes = "";
		List<Nodes> nodesList = tDao.query(Nodes.class,
				Cnd.where("website", "like", "%" + website + "%").and("description", "like", "%" + description + "%")
				.and("request_id", "=", task.getRequest_id()).and("task_id", "=", task.getTask_id()));
		for (Nodes n : nodesList) {
			nodes += n.getNode() + ",";
		}
		if (StringUtils.isNotEmpty(nodes)) {
			Sql sqlClass = null;
			String sql = "";
			try {
				String[] node_list = nodes.split(",");
				if (comment_remaining > 0 && node_list.length > 1) {
					if ("baidu".equals(task.getWebsite())) {

					} else if ("eleme".equals(task.getWebsite())) {

					}
				} else {

				}
			} catch (Exception e) {
				logger.error(e);
			}
			nodes = nodes.substring(0, nodes.length() - 1);
		}
		tDao.update(Waimai_task_list.class,
				Chain.make("nodes", nodes).add("shopkeyword_remaining", comment_remaining)
						.add("shopkeyword_status", comment_status).add("divide_shopkeyword_or_not", 2)
						.add("update_date", new Date()).add("update_date_time", new Date()),
				Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and("website", "=", website));
		logger.info("shopkeyword任务节点分配完成，总耗时：" + (System.currentTimeMillis() - time));
	}

	public int getCommentRemaining(String website, long task_id, long request_id) {
		long time = System.currentTimeMillis();
		int remaining = 0;
		if ("baidu".equals(website)) {

		} else if ("eleme".equals(website)) {

		}
		return remaining;
	}

	public String getCommentStatus(String website, long task_id, long request_id) {
		long time = System.currentTimeMillis();
		String status = "";
		Sql sqlClass = null;
		String sql = "";
		if ("baidu".equals(website)) {

		} else if ("eleme".equals(website)) {

		} else if ("meituan".equals(website)) {

			sql = "SELECT `status`,COUNT(0) as amount FROM mobile_meituan_waimai_shop_kerword_task WHERE task_id = "
					+ task_id + " AND request_id = " + request_id + "  GROUP BY `status`;";
			sqlClass = Sqls.queryEntity(sql);
			sqlClass.setEntity(tDao.getEntity(QueryStatusRS.class));
			try {
				tDao.execute(sqlClass);
				List<QueryStatusRS> list = sqlClass.getList(QueryStatusRS.class);
				for (QueryStatusRS rs : list) {
					status += rs.getStatus() + "[" + rs.getAmount() + "];";
				}
			} catch (Exception e) {
				logger.error("shopkeyword状态查询异常", e);
			}
			logger.info("查询美团[" + task.getDescription() + "]requestid[" + request_id + "]，taskid[" + task_id
					+ "]，剩余shopkeyword任务状态耗时：" + (System.currentTimeMillis() - time));

		}
		return status;
	}
}
