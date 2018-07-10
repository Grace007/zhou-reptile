package com.reptile.mobile_eleme_breakfast.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_eleme_breakfast.job.ElemeBreakfastKeywordJob;
import com.reptile.mobile_eleme_breakfast.thread.ElemeBreakfastKeywordThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;


@Service
public class ElemeBreakfastKeywordTask extends BaseTask<ElemeBreakfastKeywordJob> {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(ElemeBreakfastKeywordTask.class);

    public ExecutorService getPool() throws Exception {
        return ThreadPools.getThreadPool(this, MyConstant.MOBILE_ELEME_CONFIG.INFO_THREAD);
    }

    public List<ElemeBreakfastKeywordJob> getTaskList() throws Exception {
        List<ElemeBreakfastKeywordJob> list = tDao.query(ElemeBreakfastKeywordJob.class, Cnd.where("status", "=", 0)
                .and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_ELEME_CONFIG.INFO_LIMIT));
        return list;
    }

    public void doStart() {
        if (MyConstant.MOBILE_ELEME_CONFIG.INFO) {
            tDao.update(ElemeBreakfastKeywordJob.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("ElemeBreakfastKeywordTask") == null) {
                    QuartzManager.addJob("ElemeBreakfastKeywordTask", this, MyConstant.MOBILE_ELEME_CONFIG.INFO_TIME);
                } else {
                    logger.info("已经存在任务:ElemeBreakfastKeywordTask");
                }
            } catch (Exception e) {
                logger.info("创建ElemeBreakfastKeywordTask任务失败！");
            }
        }
    }

    public Thread getThread(ElemeBreakfastKeywordJob task) {
        return new ElemeBreakfastKeywordThread(task);
    }
}
