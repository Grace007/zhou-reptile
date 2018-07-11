package com.reptile.ec.jd.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.jd.job.JdProductJob;
import com.reptile.ec.jd.task.thread.JdCommentThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class JdSnatchCommentJob extends BaseTask<JdProductJob> {
	protected Dao mysqlDao = MySqlDataSource.getMysqlDao(Constant.ZNode);
	private static Logger logger = Logger.getLogger(JdSnatchCommentJob.class);

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.JD_CONFIG.JD_COMMENT_THREAD);
	}

	@Override
	public List<JdProductJob> getTaskList() throws Exception {
		// logger.info("##################开始获取JD任务列表##################");
		// long t = System.currentTimeMillis();
		List<JdProductJob> list = mysqlDao.query(JdProductJob.class,
				Cnd.where("status", "=", 2).and("comment_status", "=", 0).and("comments", ">", 0)
						.and("node", "=", MyConstant.Node).limit(1, MyConstant.JD_CONFIG.LIMIT));

		// logger.info("JdSnatchCommentTask产品列表总耗时:" +
		// (System.currentTimeMillis() - t) + ",节点:" + MyConstant.Node + ",获取了"
		// + list.size() + "个产品评论");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.JD_CONFIG.COMMENT) {
			logger.info("初始化解析JdSnatchCommentJob任务...");

			mysqlDao.update(JdProductJob.class, Chain.make("comment_status", 0),
					Cnd.where("comment_status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("JdSnatchCommentJob") == null) {
					QuartzManager.addJob("JdSnatchCommentJob", this, MyConstant.JD_CONFIG.COMMNET_TIME);
				} else {
					logger.info("已经存在任务:JdSnatchCommentJob");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public Thread getThread(JdProductJob task) {
		return new JdCommentThread(task);
	}

}
