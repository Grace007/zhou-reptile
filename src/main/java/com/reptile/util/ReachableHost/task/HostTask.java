package com.reptile.util.ReachableHost.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.server.MyConstant;
import com.reptile.util.ReachableHost.job.Host;
import com.reptile.util.ReachableHost.thread.HostThread;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;


@Service
public class HostTask extends BaseTask<Host> {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(HostTask.class);

    public ExecutorService getPool() throws Exception {
        return ThreadPools.getThreadPool(this, 1);
    }

    public List<Host> getTaskList() throws Exception {
        List<Host> list = tDao.query(Host.class, Cnd.where("node", "=", MyConstant.Node));
        logger.info("host获取任务成功，个数为"+list.size());
        return list;
    }

    public void doStart() {
        logger.info("初始化节点检测,host任务...");
        try {
            if (QuartzManager.getTrigger("HostTask") == null) {
                QuartzManager.addJob("HostTask", this, "0 0/1 * * * ?");
            } else {
                logger.info("已经存在任务:HostTask");
            }
        } catch (Exception e) {
            logger.info("创建HostTask任务失败！");
        }
    }

    public Thread getThread(Host task) {
        return new HostThread(task);
    }
}
