package com.reptile.ec.mogujie.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.mogujie.model.job.MogujieProductTaskModel;
import com.reptile.ec.mogujie.thread.MoGuJieSnatchCommentThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MoGuJieSnatchCommentTask extends BaseTask<MogujieProductTaskModel>{
    private static Logger logger = Logger.getLogger(MoGuJieSnatchCommentTask.class);
    private Dao tDao = SpringContextHolder.getBean("tDao");
    
    
    @Override
    public List<MogujieProductTaskModel> getTaskList() throws Exception {
    	  List<MogujieProductTaskModel> list = tDao.query(
                  MogujieProductTaskModel.class,
                  Cnd.where("comment_status", "=", 0).and("status","=",2).and("node", "=", MyConstant.Node)
                          .limit(1,MyConstant.MOGUJIE_CONFIG.SNATCHLIMIT));
    	return list;
    }
    
    @Override
    public void doStart() {
    	 if (MyConstant.MOGUJIE_CONFIG.COMMENT) {
         	try {
         		
         		tDao.update(MogujieProductTaskModel.class, Chain.make("comment_status", 0), 
         				Cnd.where("comment_status", "=", 1).and("node", "=", MyConstant.Node));
         		if (QuartzManager.getTrigger("MuGuJieSnatchCommentTask") == null) {
 				QuartzManager.addJob("MuGuJieSnatchCommentTask", this, "0/30 * * * * ?");
 				} else {
 					logger.info("已经存在任务:MuGuJieSnatchCommentTask");
 				}
 			} catch (Exception e) {
 				logger.info("创建MuGuJieSnatchCommentTask任务失败！");
 			}
         	   logger.info("初始化创建COMMENT任务...");
    	 }
    }
	@Override
	public ExecutorService getPool() throws Exception {
		return ThreadPools.getThreadPool(this, MyConstant.MOGUJIE_CONFIG.THREAD);
	}

	@Override
	public Thread getThread(MogujieProductTaskModel task) {
		return new MoGuJieSnatchCommentThread(task);
	}

}
