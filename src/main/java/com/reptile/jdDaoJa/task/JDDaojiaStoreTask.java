package com.reptile.jdDaoJa.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.jdDaoJa.job.JDDaoJiaStoreJob;
import com.reptile.jdDaoJa.thread.JDDaoJiaStoreThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.List;
import java.util.concurrent.ExecutorService;

//@Service
public class JDDaojiaStoreTask extends BaseTask<JDDaoJiaStoreJob> {
	private static Logger logger = Logger.getLogger(JDDaojiaStoreTask.class);
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_THREAD);
	}

	@Override
	public List<JDDaoJiaStoreJob> getTaskList() throws Exception {
		long t = System.currentTimeMillis();
		List<JDDaoJiaStoreJob> list = tDao.query(JDDaoJiaStoreJob.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node)
				.limit(1,MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_LIMIT));
		logger.info("======JDDaojiaStoreTask从任务库取出" + list.size() + "个任务，耗时：" + (System.currentTimeMillis() - t) + "，节点为："
				+ MyConstant.Node + "======");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE) {
			
			logger.info("初始化解析JDDaojiaStoreTask任务...");

			tDao.update(JDDaoJiaStoreJob.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1)
					.and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("JDDaoJiaStoreThread") == null) {
					QuartzManager.addJob("JDDaoJiaStoreThread", this, MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_TIME);
				} else {
					logger.info("已经存在任务:JDDaoJiaStoreThread");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}
	@Override
	public Thread getThread(JDDaoJiaStoreJob task) {
		// TODO Auto-generated method stub
		return new JDDaoJiaStoreThread(task);
	}

}
