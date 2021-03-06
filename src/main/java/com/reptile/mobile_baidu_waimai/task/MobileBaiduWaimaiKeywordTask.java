package com.reptile.mobile_baidu_waimai.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiKeywordTaskJob;
import com.reptile.mobile_baidu_waimai.thread.MobileBaiduWaimaiKeywordThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileBaiduWaimaiKeywordTask extends BaseTask<BaiduWaimaiKeywordTaskJob> {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileBaiduWaimaiKeywordTask.class);

    @Override
    public ExecutorService getPool() throws Exception {
        return ThreadPools.getThreadPool(this, MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_THREAD);
    }

    @Override
    public List<BaiduWaimaiKeywordTaskJob> getTaskList() throws Exception {
        List<BaiduWaimaiKeywordTaskJob> list = tDao.query(BaiduWaimaiKeywordTaskJob.class, Cnd.where("status", "=", 0)
                .and("node", "=", MyConstant.Node).limit(1, MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_LIMIT).asc("rand()"));

        return list;
    }

    @Override
    public void doStart() {
        if (MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO) {
        	logger.info("初始化解析MobileBaiduWaimaiKeywordTask任务...");
            tDao.update(BaiduWaimaiKeywordTaskJob.class, Chain.make("status", 0),
                    Cnd.where("status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("MobileBaiduWaimaiKeywordTask") == null) {
                    QuartzManager.addJob("MobileBaiduWaimaiKeywordTask", this,
                            MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_TIME);
                } else {
                    logger.info("已经存在任务:MobileBaiduWaimaiKeywordTask");
                }
            } catch (Exception e) {
                logger.info("创建MobileBaiduWaimaiKeywordTask任务失败！");
            }
        }
    }

    @Override
    public Thread getThread(BaiduWaimaiKeywordTaskJob task) {
        return new MobileBaiduWaimaiKeywordThread(task);
    }

}
