package com.reptile.mobile_eleme.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_eleme.job.ElemeProductTaskJob;
import com.reptile.mobile_eleme.thread.MobileElemeSnatchCommentThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileElemeSnatchCommentTask extends BaseTask<ElemeProductTaskJob> {
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileElemeSnatchCommentTask.class);

	@Override
	public ExecutorService getPool() throws Exception {
		// TODO Auto-generated method stub
		return ThreadPools.getThreadPool(this, MyConstant.MOBILE_ELEME_CONFIG.INFO_THREAD);
	}

	@Override
	public List<ElemeProductTaskJob> getTaskList() throws Exception {
		List<ElemeProductTaskJob> list = tDao.query(ElemeProductTaskJob.class, Cnd.where("comment_status", "=", 0)
				.and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_ELEME_CONFIG.INFO_LIMIT));

		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.MOBILE_ELEME_CONFIG.INFO) {
			logger.info("初始化解析MobileElemeSnatchCommentTask任务...");
			tDao.update(ElemeProductTaskJob.class, Chain.make("comment_status", 0),
					Cnd.where("comment_status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("MobileElemeSnatchCommentTask") == null) {
					QuartzManager.addJob("MobileElemeSnatchCommentTask", this,
							MyConstant.MOBILE_ELEME_CONFIG.INFO_TIME);
				} else {
					logger.info("已经存在任务:MobileElemeSnatchCommentTask");
				}
			} catch (Exception e) {
				logger.info("创建MobileElemeSnatchCommentTask任务失败！");
			}
		}
	}

	@Override
	public Thread getThread(ElemeProductTaskJob task) {
		// TODO Auto-generated method stub
		return new MobileElemeSnatchCommentThread(task);
	}

}
