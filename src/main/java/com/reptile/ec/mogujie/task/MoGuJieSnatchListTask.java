package com.reptile.ec.mogujie.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.mogujie.model.job.MogujieRequestUrlTaskModel;
import com.reptile.ec.mogujie.thread.MoGuJieSnatchListThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MoGuJieSnatchListTask extends BaseTask<MogujieRequestUrlTaskModel> {

	private static Logger logger = Logger.getLogger(MoGuJieSnatchListTask.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");

	@Override
	public List<MogujieRequestUrlTaskModel> getTaskList() throws Exception {
		List<MogujieRequestUrlTaskModel> list = tDao.query(MogujieRequestUrlTaskModel.class,
				Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node)
				.limit(1, MyConstant.MOGUJIE_CONFIG.SNATCHLIMIT));
		return list;
	}

	@Override
	public void doStart() {

		if (MyConstant.MOGUJIE_CONFIG.SEARCH) {
			try {
				if (QuartzManager.getTrigger("MuGuJieSnatchListTask") == null) {
					QuartzManager.addJob("MuGuJieSnatchListTask", this, "0/30 * * * * ?");
				} else {
					logger.info("已经存在任务:MuGuJieSnatchListTask");
				}
			} catch (Exception e) {
				logger.info("创建MuGuJieSnatchListTask任务失败！");
			}
			logger.info("初始化创建LIST任务...");
			tDao.update(MogujieRequestUrlTaskModel.class, Chain.make("status", 0), Cnd.where("status", "=", 1)
					.and("node", "=", MyConstant.Node));
		}
	}

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.MOGUJIE_CONFIG.THREAD);
	}
	@Override
	public Thread getThread(MogujieRequestUrlTaskModel task) {
		return new MoGuJieSnatchListThread(task);
	}
}
