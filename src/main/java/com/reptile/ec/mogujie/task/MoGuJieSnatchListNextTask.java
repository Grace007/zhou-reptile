package com.reptile.ec.mogujie.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.mogujie.model.job.MogujieListTaskModel;
import com.reptile.ec.mogujie.thread.MoGuJieSnatchListNextThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MoGuJieSnatchListNextTask extends BaseTask<MogujieListTaskModel>{
    private static Logger logger = Logger.getLogger(MoGuJieSnatchListNextTask.class);
    private Dao tDao = SpringContextHolder.getBean("tDao");

	@Override
	public List<MogujieListTaskModel> getTaskList() throws Exception {
		List<MogujieListTaskModel> list = tDao.query(MogujieListTaskModel.class,
				Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node)
				.limit(1, MyConstant.MOGUJIE_CONFIG.SNATCHLIMIT));
		return list;
	}

	@Override
	public void doStart() {

		if (MyConstant.MOGUJIE_CONFIG.SEARCH) {
			try {
				if (QuartzManager.getTrigger("MuGuJieSnatchListNextTask") == null) {
					QuartzManager.addJob("MuGuJieSnatchListNextTask", this, "0/30 * * * * ?");
				} else {
					logger.info("已经存在任务:MuGuJieSnatchListNextTask");
				}
			} catch (Exception e) {
				logger.info("创建MuGuJieSnatchListNextTask任务失败！");
			}
			tDao.update(MogujieListTaskModel.class, Chain.make("status", 0), Cnd.where("status", "=", 1)
					.and("node", "=", MyConstant.Node));
		}
	}

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.MOGUJIE_CONFIG.THREAD);
	}
	@Override
	public Thread getThread(MogujieListTaskModel task) {
		return new MoGuJieSnatchListNextThread(task);
	}
}
