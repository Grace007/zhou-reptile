package com.reptile.mobile_eleme.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_eleme.job.ElemeCommentDailyTaskJob;
import com.reptile.mobile_eleme.thread.MobileElemeCommentDailyThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileElemeCommentDailyTask extends BaseTask<ElemeCommentDailyTaskJob> {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileElemeCommentDailyTask.class);

    @Override
    public ExecutorService getPool() throws Exception {
        // TODO Auto-generated method stub
        return ThreadPools.getThreadPool(this, MyConstant.MOBILE_ELEME_CONFIG.COMMENT_THREAD);
    }

    @Override
    public List<ElemeCommentDailyTaskJob> getTaskList() throws Exception {
        List<ElemeCommentDailyTaskJob> list = tDao.query(ElemeCommentDailyTaskJob.class, Cnd.where("status", "=", 0)
                .and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_ELEME_CONFIG.COMMENT_LIMIT));

        return list;
    }

    @Override
    public void doStart() {
        if (MyConstant.MOBILE_ELEME_CONFIG.COMMENT) {
            logger.info("!!!!!" + "初始化MobileElemeCommentDailyTask" + "!!!!!");
            tDao.update(ElemeCommentDailyTaskJob.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("MobileElemeCommentDailyTask") == null) {
                    QuartzManager.addJob("MobileElemeCommentDailyTask", this, MyConstant.MOBILE_ELEME_CONFIG.COMMENT_TIME);
                } else {
                    logger.info("已经存在任务:MobileElemeCommentDailyTask");
                }
            } catch (Exception e) {
                logger.info("创建MobileElemeCommentDailyTask任务失败！");
            }
        }
    }

    @Override
    public Thread getThread(ElemeCommentDailyTaskJob task) {
        // TODO Auto-generated method stub
        return new MobileElemeCommentDailyThread(task);
    }

}
