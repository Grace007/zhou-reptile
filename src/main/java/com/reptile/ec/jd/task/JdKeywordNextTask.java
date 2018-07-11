package com.reptile.ec.jd.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.jd.job.JdKeywordNextJob;
import com.reptile.ec.jd.task.thread.JdKeywordNextThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class JdKeywordNextTask extends BaseTask<JdKeywordNextJob> {
	protected Dao mysqlDao = MySqlDataSource.getMysqlDao(Constant.ZNode);
	private static Logger logger = Logger.getLogger(JdKeywordNextTask.class);

	// private Dao tDao = SpringContextHolder.getBean("tDao");
	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.JD_CONFIG.KEYWORD_NEXT_THREAD);
	}

	@Override
	public List<JdKeywordNextJob> getTaskList() throws Exception {
        long t1 = System.currentTimeMillis();
		// String sql = this.getRandSql("jd_keyword_next_task",
		// MyConstant.JD_CONFIG.LIMIT);
		String sql = "SELECT * FROM jd_keyword_next_task AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX(id) FROM jd_keyword_next_task)-(SELECT MIN(id) FROM jd_keyword_next_task))+(SELECT MIN(id) FROM jd_keyword_next_task)) AS id) AS t2 WHERE t1.id >= t2.id and t1.status = 0 and t1.node ='"
				+ MyConstant.Node + "' LIMIT 1, " + MyConstant.JD_CONFIG.KEYWORD_NEXT_LIMIT + ";";
		// this.cri.where().and("t1.status", "=", 0).and("node", "=",
		// MyConstant.Node);
		Sql sqlClass = Sqls.queryEntity(sql);
		// sqlClass.setCondition(cri);
		sqlClass.setEntity(mysqlDao.getEntity(JdKeywordNextJob.class));
		mysqlDao.execute(sqlClass);
		// List<JdKeywordNextJob> list =
		// sqlClass.getList(JdKeywordNextJob.class);
		List<JdKeywordNextJob> list = mysqlDao.query(JdKeywordNextJob.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node).limit(1, MyConstant.JD_CONFIG.KEYWORD_NEXT_LIMIT).asc("rand()"));
        logger.info("查询jd_keyword_next任务耗时：" + (System.currentTimeMillis() -
                        t1));
		// List<JdKeywordNextJob> list = tDao.query(JdKeywordNextJob.class,
		// Cnd.where("status", "=", 0).and("node", "=",
		// MyConstant.Node).limit(1,MyConstant.JD_CONFIG.LIMIT).asc("rand()"));
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.JD_CONFIG.SEARCH) {
			logger.info("初始化解析JdKeywordNextJob任务...");
            long t1 = System.currentTimeMillis();
			mysqlDao.update(JdKeywordNextJob.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            logger.info("更新jd_keyword_next任务状态耗时：" + (System.currentTimeMillis() -
                        t1));
			// tDao.update(JdKeywordNextJob.class, Chain.make("status", 0),
			// Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("JdKeywordNextJob") == null) {
					QuartzManager.addJob("JdKeywordNextJob", this, MyConstant.JD_CONFIG.KEYWORD_NEXT_SEARCH_TIME);
				} else {
					logger.info("已经存在任务:JdKeywordNextJob");
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public Thread getThread(JdKeywordNextJob task) {
		return new JdKeywordNextThread(task);
	}

}
