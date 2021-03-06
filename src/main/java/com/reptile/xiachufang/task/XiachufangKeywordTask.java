package com.reptile.xiachufang.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.server.MyConstant;
import com.reptile.xiachufang.model.job.XiachufangKeywordmodel;
import com.reptile.xiachufang.thread.XiachufangKeywordThread;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class XiachufangKeywordTask extends BaseTask<XiachufangKeywordmodel> {
	private static Logger logger = Logger.getLogger(XiachufangKeywordTask.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this,
				MyConstant.XIACHUFANG_CONFIG.THREAD);
	}

	@Override
	public List<XiachufangKeywordmodel> getTaskList() throws Exception {
		long t = System.currentTimeMillis();
		List<XiachufangKeywordmodel> list = tDao.query(
				XiachufangKeywordmodel.class,
				Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node)
						.limit(1, MyConstant.XIACHUFANG_CONFIG.LIMIT));
		logger.info("======XiachufangKeywordTask从任务库取出" + list.size()
				+ "个任务，耗时：" + (System.currentTimeMillis() - t) + "，节点为："
				+ MyConstant.Node + "======");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.XIACHUFANG_CONFIG.KEYWORD) {

			logger.info("初始化解析XiachufangKeywordThread任务...");

			tDao.update(
					XiachufangKeywordmodel.class,
					Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=",
							MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("XiachufangKeywordThread") == null) {
					QuartzManager.addJob("XiachufangKeywordThread", this,
							MyConstant.XIACHUFANG_CONFIG.SIMPLE_TIME);
				} else {
					logger.info("已经存在任务:XiachufangKeywordThread");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public Thread getThread(XiachufangKeywordmodel task) {
		// TODO Auto-generated method stub
		return new XiachufangKeywordThread(task);
	}

}
