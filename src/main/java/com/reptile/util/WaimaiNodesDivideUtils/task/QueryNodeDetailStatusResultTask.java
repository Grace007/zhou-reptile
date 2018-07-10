package com.reptile.util.WaimaiNodesDivideUtils.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.server.MyConstant;
import com.reptile.util.WaimaiNodesDivideUtils.job.Waimai_task_list;
import com.reptile.util.WaimaiNodesDivideUtils.thread.QueryNodeDetailStatusResultThread;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class QueryNodeDetailStatusResultTask extends BaseTask<Waimai_task_list> {

    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(QueryNodeDetailStatusResultTask.class);

    @Override
    public ExecutorService getPool() throws Exception {
        return ThreadPools.getThreadPool(this, MyConstant.NODES_DIVIDE_CONFIG.THREAD);
    }

    @Override
    public List<Waimai_task_list> getTaskList() throws Exception {
        List<Waimai_task_list> list = tDao.query(Waimai_task_list.class, Cnd.where("divide_node_or_not", "=", 0));
        return list;
    }

    @Override
    public void doStart() {
        if (MyConstant.NODES_DIVIDE_CONFIG.SWITCH) {
            tDao.update(Waimai_task_list.class, Chain.make("divide_node_or_not", 0), Cnd.where("divide_node_or_not", "=", 1));
            try {
                if (QuartzManager.getTrigger("QueryNodeDetailStatusResult") == null) {
                    QuartzManager.addJob("QueryNodeDetailStatusResult", this, MyConstant.NODES_DIVIDE_CONFIG.PRODUCT_TIME);
                } else {
                    logger.info("已经存在任务:QueryNodeDetailStatusResult");
                }
            } catch (Exception e) {
                logger.info("创建QueryNodeDetailStatusResult任务失败！");
            }
        }
    }

    @Override
    public Thread getThread(Waimai_task_list task) {
        return new QueryNodeDetailStatusResultThread(task);
    }


}
