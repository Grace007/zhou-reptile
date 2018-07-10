package com.reptile.mobile_eleme.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_eleme.job.EleMeSearchUrlJob;
import com.reptile.mobile_eleme.thread.MobileEleSearchKeywordThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileEleSearchKeywordTask extends BaseTask<EleMeSearchUrlJob> {

    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileEleSearchKeywordTask.class);

    @Override
    public ExecutorService getPool() throws Exception {

        return ThreadPools.getThreadPool(this, MyConstant.MOBILE_ELEME_CONFIG.SEARCH_THREAD);
    }

    @Override
    public List<EleMeSearchUrlJob> getTaskList() throws Exception {
        List<EleMeSearchUrlJob> list = tDao.query(EleMeSearchUrlJob.class, Cnd.where("status", "=", 0)
                .and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_ELEME_CONFIG.SEARCH_LIMIT).asc("rand()"));

        return list;
    }

    @Override
    public void doStart() {
        if (MyConstant.MOBILE_ELEME_CONFIG.SEARCH) {

            logger.info("!!!!!" + "初始化MobileEleSearchKeywordTask" + "!!!!!");
            tDao.update(EleMeSearchUrlJob.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("MobileEleSearchKeywordTask") == null) {
                    QuartzManager.addJob("MobileEleSearchKeywordTask", this,
                            MyConstant.MOBILE_ELEME_CONFIG.SEARCH_TIME);
                } else {
                    logger.info("已经存在任务:MobileEleSearchKeywordTask");
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }

    }

    @Override
    public Thread getThread(EleMeSearchUrlJob task) {

        return new MobileEleSearchKeywordThread(task);
    }

}
