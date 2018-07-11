package com.reptile.xiachufang.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.server.MyConstant;
import com.reptile.xiachufang.model.job.XiachufangCookbookPagemodel;
import com.reptile.xiachufang.thread.XiachufangCookbookSimpleThread;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class XiachufangCookbookSimpleTask extends BaseTask<XiachufangCookbookPagemodel> {
	private static Logger logger = Logger.getLogger(XiachufangCookbookSimpleTask.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this,
				MyConstant.XIACHUFANG_CONFIG.THREAD);
	}

	@Override
	public List<XiachufangCookbookPagemodel> getTaskList() throws Exception {
		long t = System.currentTimeMillis();
		List<XiachufangCookbookPagemodel> list = tDao.query(
				XiachufangCookbookPagemodel.class,
				Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node)
						.limit(1, MyConstant.XIACHUFANG_CONFIG.LIMIT));
		logger.info("======XiachufangCookbookSimpleTask从任务库取出" + list.size()
				+ "个任务，耗时：" + (System.currentTimeMillis() - t) + "，节点为："
				+ MyConstant.Node + "======");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.XIACHUFANG_CONFIG.AUTHOR) {

			logger.info("初始化解析XiachufangCookbookSimpleThread任务...");

			tDao.update(
					XiachufangCookbookPagemodel.class,
					Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=",
							MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("XiachufangCookbookSimpleThread") == null) {
					QuartzManager.addJob("XiachufangCookbookSimpleThread", this,
							MyConstant.XIACHUFANG_CONFIG.SIMPLE_TIME);
				} else {
					logger.info("已经存在任务:XiachufangCookbookSimpleThread");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public Thread getThread(XiachufangCookbookPagemodel task) {
		// TODO Auto-generated method stub
		return new XiachufangCookbookSimpleThread(task);
	}

}
