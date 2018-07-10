package com.reptile.mobile_eleme.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_eleme.job.ElemeUrlTaskJob;
import com.reptile.mobile_eleme.thread.MobileElemeUrlThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileElemeUrlTask extends BaseTask<ElemeUrlTaskJob> {
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private static Logger logger = Logger.getLogger(MobileElemeUrlTask.class);

	@Override
	public ExecutorService getPool() throws Exception {
		// TODO Auto-generated method stub
		return ThreadPools.getThreadPool(this, MyConstant.MOBILE_ELEME_CONFIG.INFO_THREAD);
	}

	@Override
	public List<ElemeUrlTaskJob> getTaskList() throws Exception {
		java.util.Random random = new java.util.Random();
		int C = 10;
		int R = random.nextInt(C);
		List<ElemeUrlTaskJob> list = tDao.query(ElemeUrlTaskJob.class, Cnd.where("status", "=", 0).and("id%"+C, "=",R)
				.and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_ELEME_CONFIG.INFO_LIMIT));
//		List<ElemeUrlTaskJob> list = tDao.query(ElemeUrlTaskJob.class, Cnd.where("status", "=", 0)
//				.and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_ELEME_CONFIG.INFO_LIMIT));

		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.MOBILE_ELEME_CONFIG.INFO) {
			logger.info("初始化解析MobileElemeUrlTask任务...");
			tDao.update(ElemeUrlTaskJob.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("MobileElemeUrlTask") == null) {
					QuartzManager.addJob("MobileElemeUrlTask", this, MyConstant.MOBILE_ELEME_CONFIG.INFO_TIME);
				} else {
					logger.info("已经存在任务:MobileElemeUrlTask");
				}
			} catch (Exception e) {
				logger.info("创建MobileElemeUrlTask任务失败！");
			}
		}
	}

	@Override
	public Thread getThread(ElemeUrlTaskJob task) {
		// TODO Auto-generated method stub
		return new MobileElemeUrlThread(task);
	}

}
