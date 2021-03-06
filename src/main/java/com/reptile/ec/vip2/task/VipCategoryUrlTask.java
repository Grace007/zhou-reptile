package com.reptile.ec.vip2.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.vip2.job.VipCategoryUrlJob;
import com.reptile.ec.vip2.thread.VipCategoryUrlThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class VipCategoryUrlTask extends BaseTask<VipCategoryUrlJob> {

	private Dao tDao = SpringContextHolder.getBean("tDao");
	private static Logger logger = Logger.getLogger(VipCategoryUrlTask.class);

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.VIP2_CONFIG.THREAD);
	}

	@Override
	public List<VipCategoryUrlJob> getTaskList() throws Exception {
		List<VipCategoryUrlJob> list = tDao.query(VipCategoryUrlJob.class, Cnd.where("update_status", "=", 0)
				.and("node", "=", MyConstant.Node).limit(1, MyConstant.VIP2_CONFIG.LIMIT).desc("level"));
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.VIP2_CONFIG.SEARCH) {
			logger.info("初始化解析VipCategoryUrlTask...");
			tDao.update(VipCategoryUrlJob.class, Chain.make("update_status", 0),
					Cnd.where("update_status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("VipCategoryUrlTask") == null) {
					try {
						QuartzManager.addJob("VipCategoryUrlTask", this, MyConstant.VIP2_CONFIG.SEARCH_TIME);
					} catch (ParseException e) {
						logger.info("添加定时任务异常");
					}
				} else {
					logger.info("已经存在:VipCategoryUrlTask");
				}
			} catch (Exception e) {
				logger.info("", e);
			}
		}
	}

	@Override
	public Thread getThread(VipCategoryUrlJob task) {
		return new VipCategoryUrlThread(task);
	}

}
