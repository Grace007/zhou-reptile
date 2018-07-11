package com.reptile.ec.jd.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.jd.job.JdKeywordJob;
import com.reptile.ec.jd.task.thread.JdKeywordThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class JdKeywordTask extends BaseTask<JdKeywordJob> {
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private static Logger logger = Logger.getLogger(JdKeywordTask.class);

	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.JD_CONFIG.THREAD);
	}

	@Override
	public List<JdKeywordJob> getTaskList() throws Exception {

		// String sql = this.getRandSql("jd_keyword_task",
		// MyConstant.JD_CONFIG.LIMIT);
		// String sql = "SELECT * FROM jd_keyword_task AS t1 JOIN (SELECT
		// ROUND(RAND() * ((SELECT MAX(id) FROM jd_keyword_task)-(SELECT MIN(id)
		// FROM jd_keyword_task))+(SELECT MIN(id) FROM jd_keyword_task)) AS id)
		// AS t2 WHERE t1.id >= t2.id and t1.status = 0 and t1.node ='" +
		// MyConstant.Node + "' LIMIT 1, " + MyConstant.JD_CONFIG.LIMIT + ";";
		// this.cri.where().and("t1.status", "=", 0).and("node", "=",
		// MyConstant.Node);
		// Sql sqlClass = Sqls.queryEntity(sql);
		// sqlClass.setCondition(cri);
		// sqlClass.setEntity(tDao.getEntity(JdKeywordJob.class));
		// tDao.execute(sqlClass);
		List<JdKeywordJob> list = tDao.query(JdKeywordJob.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node).limit(1, MyConstant.JD_CONFIG.LIMIT).asc("rand()"));
		// List<JdKeywordJob> list = sqlClass.getList(JdKeywordJob.class);

		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.JD_CONFIG.SEARCH) {
			logger.info("初始化解析JdKeywordThread任务...");

			tDao.update(JdKeywordJob.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("JdKeywordJob") == null) {
					QuartzManager.addJob("JdKeywordJob", this, MyConstant.JD_CONFIG.SEARCH_TIME);
				} else {
					logger.info("已经存在任务:JdKeywordThread");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public Thread getThread(JdKeywordJob task) {
		return new JdKeywordThread(task);
	}

}
