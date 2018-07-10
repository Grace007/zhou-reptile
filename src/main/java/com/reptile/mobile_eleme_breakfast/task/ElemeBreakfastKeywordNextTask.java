package com.reptile.mobile_eleme_breakfast.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_eleme_breakfast.job.ElemeBreakfastKeywordNextJob;
import com.reptile.mobile_eleme_breakfast.thread.ElemeBreakfastKeywordNextThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;


@Service
public class ElemeBreakfastKeywordNextTask extends BaseTask<ElemeBreakfastKeywordNextJob> {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(ElemeBreakfastKeywordNextTask.class);

    public ExecutorService getPool() throws Exception {
        return ThreadPools.getThreadPool(this, MyConstant.MOBILE_ELEME_CONFIG.INFO_THREAD);
    }

    public List<ElemeBreakfastKeywordNextJob> getTaskList() throws Exception {
        List<ElemeBreakfastKeywordNextJob> list = tDao.query(ElemeBreakfastKeywordNextJob.class, Cnd.where("status", "=", 0)
                .and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_ELEME_CONFIG.INFO_LIMIT));
        return list;
    }

    public void doStart() {
        if (MyConstant.MOBILE_ELEME_CONFIG.INFO) {
            tDao.update(ElemeBreakfastKeywordNextJob.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("ElemeBreakfastKeywordNextTask") == null) {
                    QuartzManager.addJob("ElemeBreakfastKeywordNextTask", this, MyConstant.MOBILE_ELEME_CONFIG.INFO_TIME);
                } else {
                    logger.info("已经存在任务:ElemeBreakfastKeywordNextTask");
                }
            } catch (Exception e) {
                logger.info("创建ElemeBreakfastKeywordNextTask任务失败！");
            }
        }
    }

    public Thread getThread(ElemeBreakfastKeywordNextJob task) {
        return new ElemeBreakfastKeywordNextThread(task);
    }
}
