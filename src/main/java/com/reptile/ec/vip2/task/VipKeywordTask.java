package com.reptile.ec.vip2.task;

import com.bds.base.task.BaseTask;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.SpringContextHolder;
import com.bds.base.util.ThreadPools;
import com.reptile.ec.vip2.job.VipKeywordJob;
import com.reptile.ec.vip2.thread.VipKeywordThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class VipKeywordTask extends BaseTask<VipKeywordJob> {

    private Dao tDao = SpringContextHolder.getBean("tDao");
    private static Logger logger = Logger.getLogger(VipKeywordTask.class);

    @Override
    public ExecutorService getPool() throws Exception {
        return ThreadPools.getThreadPool(this, MyConstant.VIP2_CONFIG.THREAD);
    }

    @Override
    public List<VipKeywordJob> getTaskList() throws Exception {
        List<VipKeywordJob> list = tDao.query(VipKeywordJob.class, Cnd.where("status", "=", 0)
                .and("node", "=", MyConstant.Node).limit(1, MyConstant.VIP2_CONFIG.LIMIT).desc("level"));
        return list;
    }

    @Override
    public void doStart() {
        if (MyConstant.VIP2_CONFIG.SEARCH) {
            logger.info("初始化解析VipKeywordTask...");
            tDao.update(VipKeywordJob.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("VipKeywordTask") == null) {
                    try {
                        QuartzManager.addJob("VipKeywordTask", this, MyConstant.VIP2_CONFIG.SEARCH_TIME);
                    } catch (ParseException e) {
                        logger.info("添加定时任务异常");
                    }
                } else {
                    logger.info("已经存在:VipKeywordTask");
                }
            } catch (Exception e) {
                logger.info("", e);
            }
        }
    }

    @Override
    public Thread getThread(VipKeywordJob task) {
        return new VipKeywordThread(task);
    }
}
