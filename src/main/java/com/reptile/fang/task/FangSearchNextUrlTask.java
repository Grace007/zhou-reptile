package com.reptile.fang.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.fang.model.job.FangSearchNextUrlTaskModel;
import com.reptile.fang.model.job.FangSearchUrlTaskModel;
import com.reptile.fang.thread.FangSearchNextUrlThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
@Service
public class FangSearchNextUrlTask extends BaseTask<FangSearchNextUrlTaskModel> {
	private static Logger logger = Logger.getLogger(FangSearchUrlTask.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	
	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.FANG_CONFIG.SEARCH_THREAD);
	}

	@Override
	public List<FangSearchNextUrlTaskModel> getTaskList() throws Exception {
		long t = System.currentTimeMillis();
		List<FangSearchNextUrlTaskModel> list = tDao.query(FangSearchNextUrlTaskModel.class,
				Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node).limit(1,
						MyConstant.FANG_CONFIG.SEARCH_LIMIT));
		logger.info("======FangSearchNextUrlTask从任务库取出" + list.size() + "个任务，耗时：" + (System.currentTimeMillis() - t) + "，节点为："
				+ MyConstant.Node + "======");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.FANG_CONFIG.SEARCHNEXT) {
			logger.info("初始化解析FangSearchNextUrlTask任务...");

			tDao.update(FangSearchUrlTaskModel.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("FangSearchNextUrlThread") == null) {
					QuartzManager.addJob("FangSearchNextUrlThread", this, MyConstant.FANG_CONFIG.SEARCH_TIME);
				} else {
					logger.info("已经存在任务:FangSearchNextUrlThread");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}

	}

	@Override
	public Thread getThread(FangSearchNextUrlTaskModel task) {
		return new FangSearchNextUrlThread(task);
	}

}
