package com.reptile.ec.amazon.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.amazon.job.AmazonUrlJob;
import com.reptile.ec.amazon.task.thread.AmazonUrlSnatchThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutorService;
@Service
public class AmazonUrlSnatchTask extends BaseTask<AmazonUrlJob> {
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private static Logger logger = Logger.getLogger(AmazonUrlSnatchTask.class);
	
	@Override
	public ExecutorService getPool() throws Exception {
		// TODO Auto-generated method stub
		return ThreadPools.getThreadPool(this, MyConstant.AMAZON_CONFIG.LIMIT);
	}

	@Override
	public List<AmazonUrlJob> getTaskList() throws Exception {
		// TODO Auto-generated method stub
		List<AmazonUrlJob> list = tDao.query(AmazonUrlJob.class,
				Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node).limit(1,MyConstant.AMAZON_CONFIG.LIMIT).asc("rand()"));
		return list;
	}

	@Override
	public void doStart() {
		// TODO Auto-generated method stub
		if(MyConstant.AMAZON_CONFIG.SEARCH){
			logger.info("初始化解析AmazonUrlJob...");
			tDao.update(AmazonUrlJob.class,Chain.make("status", 0),Cnd.where("status","=",1).and("node", "=", MyConstant.Node));
			try {
				if (QuartzManager.getTrigger("AmazonUrlSnatchThread") == null) {
					QuartzManager.addJob("AmazonUrlSnatchThread", this, "0/30 * * * * ?");
				} else {
					logger.info("已经存在:AmazonUrlSnatchThread");
				}
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				logger.info("",e);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				logger.info("",e);
			}
		}
	}

	@Override
	public Thread getThread(AmazonUrlJob task) {
		// TODO Auto-generated method stub
		return new AmazonUrlSnatchThread(task);
	}
	
}
