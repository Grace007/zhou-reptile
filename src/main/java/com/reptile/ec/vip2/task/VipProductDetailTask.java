package com.reptile.ec.vip2.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.vip2.job.VipProductJob;
import com.reptile.ec.vip2.thread.VipProductDetailThread;
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
public class VipProductDetailTask extends BaseTask<VipProductJob> {

	private Dao tDao = SpringContextHolder.getBean("tDao");
	private static Logger logger = Logger.getLogger(VipProductDetailTask.class);

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.VIP2_CONFIG.THREAD);
	}

	@Override
	public List<VipProductJob> getTaskList() throws Exception {
		List<VipProductJob> list = tDao.query(VipProductJob.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node).limit(1, MyConstant.VIP2_CONFIG.LIMIT).desc("level"));
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.VIP2_CONFIG.INFO) {
			logger.info("初始化解析VipProductDetailTask...");
			tDao.update(VipProductJob.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("VipProductDetailTask") == null) {
					try {
						QuartzManager.addJob("VipProductDetailTask", this, MyConstant.VIP2_CONFIG.INFO_TIME);
					} catch (ParseException e) {
						logger.info("添加定时任务异常");
					}
				} else {
					logger.info("已经存在:VipProductDetailTask");
				}
			} catch (Exception e) {
				logger.info("", e);
			}
		}
	}

	@Override
	public Thread getThread(VipProductJob task) {
		return new VipProductDetailThread(task);
	}

}
