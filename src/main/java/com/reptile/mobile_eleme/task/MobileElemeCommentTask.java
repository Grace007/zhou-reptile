package com.reptile.mobile_eleme.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_eleme.job.ElemeCommentTaskJob;
import com.reptile.mobile_eleme.thread.MobileElemeCommentThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileElemeCommentTask extends BaseTask<ElemeCommentTaskJob> {
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private static Logger logger = Logger.getLogger(MobileElemeCommentTask.class);

	@Override
	public ExecutorService getPool() throws Exception {
		// TODO Auto-generated method stub
		return ThreadPools.getThreadPool(this, MyConstant.MOBILE_ELEME_CONFIG.COMMENT_THREAD);
	}

	@Override
	public List<ElemeCommentTaskJob> getTaskList() throws Exception {
		List<ElemeCommentTaskJob> list = tDao.query(ElemeCommentTaskJob.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_ELEME_CONFIG.COMMENT_LIMIT));

		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.MOBILE_ELEME_CONFIG.COMMENT) {
			logger.info("!!!!!" + "初始化MobileElemeCommentTask" + "!!!!!");
			tDao.update(ElemeCommentTaskJob.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("MobileElemeCommentTask") == null) {
					QuartzManager.addJob("MobileElemeCommentTask", this, MyConstant.MOBILE_ELEME_CONFIG.COMMENT_TIME);
				} else {
					logger.info("已经存在任务:MobileElemeCommentTask");
				}
			} catch (Exception e) {
				logger.info("创建MobileElemeCommentTask任务失败！");
			}
		}
	}

	@Override
	public Thread getThread(ElemeCommentTaskJob task) {
		// TODO Auto-generated method stub
		return new MobileElemeCommentThread(task);
	}

}
