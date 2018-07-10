package com.reptile.mobile_eleme.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_eleme.job.ElemeProductTaskJob;
import com.reptile.mobile_eleme.thread.MobileElemeSnatchShopThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileElemeSnatchShopTask extends BaseTask<ElemeProductTaskJob> {
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private static Logger logger = Logger.getLogger(MobileElemeSnatchShopTask.class);

	@Override
	public ExecutorService getPool() throws Exception {
		// TODO Auto-generated method stub
		return ThreadPools.getThreadPool(this, MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_THREAD);
	}

	@Override
	public List<ElemeProductTaskJob> getTaskList() throws Exception {
		java.util.Random random = new java.util.Random();
		int C = 10;
		int R = random.nextInt(C);
		List<ElemeProductTaskJob> list = tDao.query(ElemeProductTaskJob.class, Cnd.where("shop_status", "=", 0)
				.and("node", "=", MyConstant.Node).and("id%"+C, "=",R).limit(1, 2));
//		List<ElemeProductTaskJob> list = tDao.query(ElemeProductTaskJob.class, Cnd.where("shop_status", "=", 0)
//				.and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_LIMIT));
		logger.info("取出"+list.size()+"个任务");
		return list;
	}

	@Override
	public void doStart() {
		if (MyConstant.MOBILE_ELEME_CONFIG.INFO) {
			logger.info("初始化解析MobileElemeSnatchShopTask任务...");
			tDao.update(ElemeProductTaskJob.class, Chain.make("shop_status", 0),
					Cnd.where("shop_status", "=", 1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("MobileElemeSnatchShopTask") == null) {
					QuartzManager.addJob("MobileElemeSnatchShopTask", this, MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_TIME);
				} else {
					logger.info("已经存在任务:MobileElemeSnatchShopTask");
				}
			} catch (Exception e) {
				logger.info("创建MobileElemeSnatchShopTask任务失败！");
			}
		}
	}

	@Override
	public Thread getThread(ElemeProductTaskJob task) {
		// TODO Auto-generated method stub
		return new MobileElemeSnatchShopThread(task);
	}

}
