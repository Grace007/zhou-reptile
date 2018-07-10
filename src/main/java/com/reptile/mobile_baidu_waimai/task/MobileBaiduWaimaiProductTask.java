package com.reptile.mobile_baidu_waimai.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiProductTaskJob;
import com.reptile.mobile_baidu_waimai.thread.MobileBaiduWaimaiProductThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileBaiduWaimaiProductTask extends BaseTask<BaiduWaimaiProductTaskJob> {
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private static Logger logger = Logger.getLogger(MobileBaiduWaimaiProductTask.class);

	@Override
	public ExecutorService getPool() throws Exception {
		// TODO Auto-generated method stub
		return ThreadPools.getThreadPool(this, MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_THREAD);
	}

	@Override
	public List<BaiduWaimaiProductTaskJob> getTaskList() throws Exception {
		List<BaiduWaimaiProductTaskJob> list = tDao.query(BaiduWaimaiProductTaskJob.class,
				Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node)
						.limit(1, MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_LIMIT).asc("level"));

		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO) {
			logger.info("初始化解析MobileBaiduWaimaiProductTask任务...");
			tDao.update(BaiduWaimaiProductTaskJob.class, Chain.make("status", 0),
					Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("MobileBaiduWaimaiProductTask") == null) {
					QuartzManager.addJob("MobileBaiduWaimaiProductTask", this,
							MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_TIME);
				} else {
					logger.info("已经存在任务:MobileBaiduWaimaiProductTask");
				}
			} catch (Exception e) {
				logger.info("创建MobileBaiduWaimaiProductTask任务失败！");
			}
		}
	}

	@Override
	public Thread getThread(BaiduWaimaiProductTaskJob task) {
		// TODO Auto-generated method stub
		return new MobileBaiduWaimaiProductThread(task);
	}

}
