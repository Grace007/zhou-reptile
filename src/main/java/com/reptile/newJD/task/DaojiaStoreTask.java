package com.reptile.newJD.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.newJD.job.JDDaojiaStoreSimpleJob;
import com.reptile.newJD.thread.DaojiaStoreThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

// 店铺详情
@Service
public class DaojiaStoreTask extends BaseTask<JDDaojiaStoreSimpleJob>{
	
	private static Logger logger = Logger.getLogger(DaojiaStoreTask.class);
	private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode)
			.getMysqlDao();

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this,
				MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_THREAD);
	}

	@Override
	public List<JDDaojiaStoreSimpleJob> getTaskList() throws Exception {
		long t = System.currentTimeMillis();
		List<JDDaojiaStoreSimpleJob> list = tDao
				.query(JDDaojiaStoreSimpleJob.class,
						Cnd.where("status", "=", 0)
								.and("node", "=", MyConstant.Node)
								.limit(1,
										MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_LIMIT).asc("rand()"));
		logger.info("======DaojiaStoreTask从任务库取出" + list.size() + "个任务，耗时："
				+ (System.currentTimeMillis() - t) + "，节点为：" + MyConstant.Node
				+ "======");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE) {

			logger.info("初始化解析DaojiaStoreTask任务...");

			tDao.update(JDDaojiaStoreSimpleJob.class, Chain.make("status", 0), Cnd
					.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("DaojiaStoreTask") == null) {
					QuartzManager.addJob("DaojiaStoreTask", this,
							MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_TIME);
				} else {
					logger.info("已经存在任务:DaojiaStoreTask");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public Thread getThread(JDDaojiaStoreSimpleJob task) {
		return new DaojiaStoreThread(task);
	}
	
}
