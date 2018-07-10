package com.reptile.mobile_baidu_waimai.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiCommentDailyTaskJob;
import com.reptile.mobile_baidu_waimai.thread.MobileBaiduWaimaiCommentDailyThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileBaiduWaimaiCommentDailyTask extends BaseTask<BaiduWaimaiCommentDailyTaskJob> {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileBaiduWaimaiCommentDailyTask.class);

    @Override
    public ExecutorService getPool() throws Exception {
        // TODO Auto-generated method stub
        return ThreadPools.getThreadPool(this, MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMMENT_THREAD);
    }

    @Override
    public List<BaiduWaimaiCommentDailyTaskJob> getTaskList() throws Exception {
        List<BaiduWaimaiCommentDailyTaskJob> list = tDao.query(BaiduWaimaiCommentDailyTaskJob.class, Cnd.where("status", "=", 0)
                .and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMMENT_LIMIT));

        return list;
    }

    @Override
    public void doStart() {
        if (MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMMENT) {
        	logger.info("初始化解析MobileBaiduWaimaiCommentDailyTask任务...");
            tDao.update(BaiduWaimaiCommentDailyTaskJob.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("MobileBaiduWaimaiCommentDailyTask") == null) {
                    QuartzManager.addJob("MobileBaiduWaimaiCommentDailyTask", this,
                            MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMMENT_TIME);
                } else {
                    logger.info("已经存在任务:MobileBaiduWaimaiCommentDailyTask");
                }
            } catch (Exception e) {
                logger.info("创建MobileBaiduWaimaiCommentDailyTask任务失败！");
            }
        }
    }

    @Override
    public Thread getThread(BaiduWaimaiCommentDailyTaskJob task) {
        // TODO Auto-generated method stub
        return new MobileBaiduWaimaiCommentDailyThread(task);
    }

}
