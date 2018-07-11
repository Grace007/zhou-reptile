package com.reptile.ec.mogujie.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.mogujie.model.job.MogujieCommentTaskModel;
import com.reptile.ec.mogujie.thread.MoGuJieParserCommentThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MoGuJieParserCommentTask extends BaseTask<MogujieCommentTaskModel> {
	private static Logger logger = Logger.getLogger(MoGuJieParserCommentTask.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");

	@Override
	public List<MogujieCommentTaskModel> getTaskList() throws Exception {
		List<MogujieCommentTaskModel> list = tDao.query(MogujieCommentTaskModel.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node).limit(1, MyConstant.MOGUJIE_CONFIG.LIMIT));
		return list;
	}

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.MOGUJIE_CONFIG.THREAD);
	}

	public void doStart() {

		if (MyConstant.MOGUJIE_CONFIG.COMMENT) {
			try {
				if (QuartzManager.getTrigger("MuGuJieParserCommentTask") == null) {
					QuartzManager.addJob("MuGuJieParserCommentTask", this, "0/45 * * * * ?");
				} else {
					logger.info("已经存在任务:MuGuJieParserCommentTask");
				}
			} catch (Exception e) {
				logger.info("创建MuGuJieParserCommentTask任务失败！");
			}
			logger.info("初始化解析COMMENT任务...");

			tDao.update(MogujieCommentTaskModel.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
		}
	}

	@Override
	public Thread getThread(MogujieCommentTaskModel task) {
		return new MoGuJieParserCommentThread(task);
	}

}
