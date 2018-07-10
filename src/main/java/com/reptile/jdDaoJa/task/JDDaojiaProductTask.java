package com.reptile.jdDaoJa.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.jdDaoJa.job.JDDaoJiaProductUrlJob;
import com.reptile.jdDaoJa.thread.JDDaoJiaProductThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.List;
import java.util.concurrent.ExecutorService;

//@Service
public class JDDaojiaProductTask extends BaseTask<JDDaoJiaProductUrlJob> {
	private static Logger logger = Logger.getLogger(JDDaojiaProductTask.class);
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_PRODUCT_THREAD);
	}

	@Override
	public List<JDDaoJiaProductUrlJob> getTaskList() throws Exception {
		long t = System.currentTimeMillis();
		List<JDDaoJiaProductUrlJob> list = tDao.query(JDDaoJiaProductUrlJob.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node)
				.limit(1,MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_PRODUCT_LIMIT));
		logger.info("======JDDaojiaProductTask从任务库取出" + list.size() + "个任务，耗时：" + (System.currentTimeMillis() - t) + "，节点为："
				+ MyConstant.Node + "======");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_PRODUCT) {
			
			logger.info("初始化解析JDDaojiaProductTask任务...");

			tDao.update(JDDaoJiaProductUrlJob.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1)
					.and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("JDDaoJiaProductThread") == null) {
					QuartzManager.addJob("JDDaoJiaProductThread", this, MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_PRODUCT_TIME);
				} else {
					logger.info("已经存在任务:JDDaoJiaProductThread");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}
	@Override
	public Thread getThread(JDDaoJiaProductUrlJob task) {
		// TODO Auto-generated method stub
		return new JDDaoJiaProductThread(task);
	}

}
