package com.reptile.ec.mogujie.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.mogujie.model.job.MogujieProductTaskModel;
import com.reptile.ec.mogujie.thread.MoGuJieParserProductThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;


@Service
public class MoGuJieParserProductTask extends BaseTask<MogujieProductTaskModel> {
	private static Logger logger = Logger.getLogger(MoGuJieParserProductTask.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");

	@Override
	public List<MogujieProductTaskModel> getTaskList() throws Exception {
		List<MogujieProductTaskModel> list = tDao.query(MogujieProductTaskModel.class,
				Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node)
				.limit(1,MyConstant.MOGUJIE_CONFIG.LIMIT));
		return list;
	}

	@Override
	public void doStart() {

		if (MyConstant.MOGUJIE_CONFIG.INFO) {
			try {
				if (QuartzManager.getTrigger("MuGuJieParserProductTask") == null) {
					QuartzManager.addJob("MuGuJieParserProductTask", this, "0/45 * * * * ?");
				} else {
					logger.info("已经存在任务:MuGuJieParserProductTask");
				}
			} catch (Exception e) {
				logger.info("创建MuGuJieParserProductTask任务失败！");
			}
			logger.info("初始化解析PRODUCT任务...");
			tDao.update(MogujieProductTaskModel.class, Chain.make("status", 0), Cnd.where("status", "=", 1)
					.and("node", "=", MyConstant.Node));
		}
	}

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.MOGUJIE_CONFIG.THREAD);
	}

	@Override
	public Thread getThread(MogujieProductTaskModel task) {
		return new MoGuJieParserProductThread(task);
	}
}
