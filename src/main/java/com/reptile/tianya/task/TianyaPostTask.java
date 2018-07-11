package com.reptile.tianya.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.server.MyConstant;
import com.reptile.tianya.job.TianyaPostJob;
import com.reptile.tianya.task.thread.TianyaPostThread;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class TianyaPostTask extends BaseTask<TianyaPostJob> {
    private static Logger logger = Logger.getLogger(TianyaPostTask.class);
    //	private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();

    @Override
    public ExecutorService getPool() throws Exception {
        return ThreadPools.getThreadPool(this, MyConstant.TIANYA_CONFIG.THREAD);
    }

    @Override
    public List<TianyaPostJob> getTaskList() throws Exception {
        List<TianyaPostJob> list = tDao.query(TianyaPostJob.class,
                Cnd.where("status", "=", 0).and("node", "=", MyConstant.Node).limit(1, MyConstant.TIANYA_CONFIG.LIMIT));
        return list;
    }

    @Override
    public void doStart() {
        if (MyConstant.TIANYA_CONFIG.INFO) {
            logger.info("初始化解析TianyaPostTask任务...");

            tDao.update(TianyaPostJob.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("TianyaPostTask") == null) {
                    QuartzManager.addJob("TianyaPostTask", this, MyConstant.TIANYA_CONFIG.INFO_TIME);
                } else {
                    logger.info("已经存在任务:TianyaPostTask");
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public Thread getThread(TianyaPostJob task) {
        return new TianyaPostThread(task);
    }

}
