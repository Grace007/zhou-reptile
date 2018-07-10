package com.reptile.newJD.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.newJD.job.JDDaoJiaCategoryJob;
import com.reptile.newJD.thread.DaojiaStorePageThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class DaojiaStorePageTask extends BaseTask<JDDaoJiaCategoryJob> {
	private static Logger logger = Logger
			.getLogger(DaojiaStorePageTask.class);
	private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode)
			.getMysqlDao();
	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this,
				MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_PAGE_THREAD);
	}

	@Override
	public List<JDDaoJiaCategoryJob> getTaskList() throws Exception {
		long t = System.currentTimeMillis();
		List<JDDaoJiaCategoryJob> list = tDao.query(JDDaoJiaCategoryJob.class,
				Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node)
						.limit(1, MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_PAGE_LIMIT));
		logger.info("======DaojiaStorePageTask从任务库取出" + list.size()
				+ "个任务，耗时：" + (System.currentTimeMillis() - t) + "，节点为："
				+ MyConstant.Node + "======");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_PAGE) {
			logger.info("初始化解析DaojiaStorePageTask任务...");

			tDao.update(JDDaoJiaCategoryJob.class, Chain.make("status", 0), Cnd
					.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("DaojiaStorePageTask") == null) {
					QuartzManager.addJob("DaojiaStorePageTask", this,
							MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_PAGE_TIME);
				} else {
					logger.info("已经存在任务:DaojiaStorePageTask");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public Thread getThread(JDDaoJiaCategoryJob task) {
		return new DaojiaStorePageThread(task);
	}

}
