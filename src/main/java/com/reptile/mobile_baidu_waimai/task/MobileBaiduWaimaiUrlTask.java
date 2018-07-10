package com.reptile.mobile_baidu_waimai.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiUrlTaskJob;
import com.reptile.mobile_baidu_waimai.thread.MobileBaiduWaimaiUrlThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileBaiduWaimaiUrlTask extends BaseTask<BaiduWaimaiUrlTaskJob> {
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private static Logger logger = Logger.getLogger(MobileBaiduWaimaiUrlTask.class);

	@Override
	public ExecutorService getPool() throws Exception {
		// TODO Auto-generated method stub
		return ThreadPools.getThreadPool(this, MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_THREAD);
	}

	@Override
	public List<BaiduWaimaiUrlTaskJob> getTaskList() throws Exception {
		List<BaiduWaimaiUrlTaskJob> list = tDao.query(BaiduWaimaiUrlTaskJob.class, Cnd.where("status", "=", 0)
				.and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_LIMIT));

		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO) {
			logger.info("初始化解析MobileBaiduWaimaiUrlTask任务...");
			tDao.update(BaiduWaimaiUrlTaskJob.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("MobileBaiduWaimaiUrlTask") == null) {
					QuartzManager.addJob("MobileBaiduWaimaiUrlTask", this,
							MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_TIME);
				} else {
					logger.info("已经存在任务:MobileBaiduWaimaiUrlTask");
				}
			} catch (Exception e) {
				logger.info("创建MobileBaiduWaimaiUrlTask任务失败！");
			}
		}
	}

	@Override
	public Thread getThread(BaiduWaimaiUrlTaskJob task) {
		// TODO Auto-generated method stub
		return new MobileBaiduWaimaiUrlThread(task);
	}

}
