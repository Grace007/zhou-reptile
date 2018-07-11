package com.reptile.tianya.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.server.MyConstant;
import com.reptile.tianya.job.TianyaKeywordJob;
import com.reptile.tianya.task.thread.TianyaKeywordThread;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class TianyaKeywordTask extends BaseTask<TianyaKeywordJob> {
    private static Logger logger = Logger.getLogger(TianyaKeywordTask.class);
    //	private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();

    @Override
    public ExecutorService getPool() throws Exception {
        return ThreadPools.getThreadPool(this, MyConstant.TIANYA_CONFIG.THREAD);
    }

    @Override
    public List<TianyaKeywordJob> getTaskList() throws Exception {
        List<TianyaKeywordJob> list = tDao.query(TianyaKeywordJob.class,
                Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node).limit(1, MyConstant.TIANYA_CONFIG.LIMIT));
        return list;
    }

    @Override
    public void doStart() {
        if (MyConstant.TIANYA_CONFIG.SEARCH) {
            logger.info("初始化解析TianyaKeywordTask任务...");

            tDao.update(TianyaKeywordJob.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("TianyaKeywordTask") == null) {
                    QuartzManager.addJob("TianyaKeywordTask", this, MyConstant.TIANYA_CONFIG.SEARCH_TIME);
                } else {
                    logger.info("已经存在任务:TianyaKeywordTask");
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public Thread getThread(TianyaKeywordJob task) {
        return new TianyaKeywordThread(task);
    }

}
