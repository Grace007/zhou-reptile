package com.reptile.fang.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.fang.model.job.FangSearchUrlTaskModel;
import com.reptile.fang.thread.FangSearchUrlThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
@Service
public class FangSearchUrlTask extends BaseTask<FangSearchUrlTaskModel> {
	private static Logger logger = Logger.getLogger(FangSearchUrlTask.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.FANG_CONFIG.SEARCH_THREAD);
	}

	@Override
	public List<FangSearchUrlTaskModel> getTaskList() throws Exception {
		long t = System.currentTimeMillis();
		List<FangSearchUrlTaskModel> list = tDao.query(FangSearchUrlTaskModel.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node)
				.limit(1,MyConstant.FANG_CONFIG.SEARCH_LIMIT));
		logger.info("======FangSearchUrlTask从任务库取出" + list.size() + "个任务，耗时：" + (System.currentTimeMillis() - t) + "，节点为："
				+ MyConstant.Node + "======");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.FANG_CONFIG.SEARCH) {
			
			logger.info("初始化解析FangSearchUrlTask任务...");

			tDao.update(FangSearchUrlTaskModel.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1)
					.and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("FangSearchUrlThread") == null) {
					QuartzManager.addJob("FangSearchUrlThread", this, MyConstant.FANG_CONFIG.SEARCH_TIME);
				} else {
					logger.info("已经存在任务:FangSearchUrlThread");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}
	@Override
	public Thread getThread(FangSearchUrlTaskModel task) {
		// TODO Auto-generated method stub
		return new FangSearchUrlThread(task);
	}

}
