package com.reptile.qunar.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.qunar.model.job.QunarSearchUrlTaskModel;
import com.reptile.qunar.thread.QunarSearchUrlThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class QunarSearchUrlTask extends BaseTask<QunarSearchUrlTaskModel> {
	private static Logger logger = Logger.getLogger(QunarSearchUrlTask.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.QUNAR_CONFIG.SEARCH_THREAD);
	}

	@Override
	public List<QunarSearchUrlTaskModel> getTaskList() throws Exception {
		long t = System.currentTimeMillis();
		List<QunarSearchUrlTaskModel> list = tDao.query(QunarSearchUrlTaskModel.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node)
				.limit(1,MyConstant.QUNAR_CONFIG.SEARCH_LIMIT));
		logger.info("======QunarSearchUrlTask从任务库取出" + list.size() + "个任务，耗时：" + (System.currentTimeMillis() - t) + "，节点为："
				+ MyConstant.Node + "======");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.QUNAR_CONFIG.SEARCH) {
			
			logger.info("初始化解析QunarSearchUrlThread任务...");

			tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1)
					.and("node", "=", MyConstant.Node));
			tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 0),
					Cnd.where("status", "=", 4)
					.and("node", "=", MyConstant.Node));
			tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 0),
					Cnd.where("status", "=", 3)
					.and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("QunarSearchUrlThread") == null) {
					QuartzManager.addJob("QunarSearchUrlThread", this, MyConstant.QUNAR_CONFIG.SEARCH_TIME);
				} else {
					logger.info("已经存在任务:QunarSearchUrlThread");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}
	@Override
	public Thread getThread(QunarSearchUrlTaskModel task) {
		// TODO Auto-generated method stub
		return new QunarSearchUrlThread(task);
	}

}
