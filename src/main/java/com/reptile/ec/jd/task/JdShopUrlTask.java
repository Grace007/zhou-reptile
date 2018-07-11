package com.reptile.ec.jd.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.jd.job.JdShopUrl;
import com.reptile.ec.jd.task.thread.JdShopUrlThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;


@Service
public class JdShopUrlTask extends BaseTask<JdShopUrl> {
    protected Dao mysqlDao = MySqlDataSource.getMysqlDao(Constant.ZNode);
    private static Logger logger = Logger.getLogger(JdShopUrlTask.class);


    public ExecutorService getPool() throws Exception {
        return ThreadPools.getThreadPool(this, MyConstant.JD_CONFIG.THREAD);
    }

    public List<JdShopUrl> getTaskList() throws Exception {
        List<JdShopUrl> list = mysqlDao.query(JdShopUrl.class, Cnd.where("status", "=", 0)
                .and("node", "=", MyConstant.Node).limit(1, MyConstant.JD_CONFIG.LIMIT).asc("rand()"));
        return list;
    }

    public void doStart() {
        if (MyConstant.JD_CONFIG.SEARCH) {
            logger.info("初始化解析JdShopUrlTask任务...");

            mysqlDao.update(JdShopUrl.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("JdShopUrlTask") == null) {
                    QuartzManager.addJob("JdShopUrlTask", this, MyConstant.JD_CONFIG.SEARCH_TIME);
                } else {
                    logger.info("已经存在任务:JdShopUrlTask");
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    public Thread getThread(JdShopUrl task) {
        return new JdShopUrlThread(task);
    }
}
