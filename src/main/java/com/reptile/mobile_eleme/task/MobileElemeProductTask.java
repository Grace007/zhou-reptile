package com.reptile.mobile_eleme.task;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.task.BaseTask;
import com.bds.base.util.Constant;
import com.bds.base.util.QuartzManager;
import com.bds.base.util.ThreadPools;
import com.reptile.mobile_eleme.job.ElemeProductTaskJob;
import com.reptile.mobile_eleme.thread.MobileElemeProductThread;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class MobileElemeProductTask extends BaseTask<ElemeProductTaskJob> {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileElemeProductTask.class);

    @Override
    public ExecutorService getPool() throws Exception {
        // TODO Auto-generated method stub
        return ThreadPools.getThreadPool(this, MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_THREAD);
    }

    @Override
    public List<ElemeProductTaskJob> getTaskList() throws Exception {

        List<ElemeProductTaskJob> list =
                tDao.query(ElemeProductTaskJob.class, Cnd.where("product_status",
                        "=", 0)
                        .and("node", "=", MyConstant.Node).limit(1,
                                MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_LIMIT).asc("level"));

        // 先根据优先级排序后再随机取任务
//		String sql = "SELECT * FROM mobile_eleme_product_task WHERE id >= ((SELECT MAX(id) FROM mobile_eleme_product_task) - (SELECT MIN(id) FROM mobile_eleme_product_task)) * RAND() + (SELECT MIN(id) FROM mobile_eleme_product_task) AND node = '"
//				+ MyConstant.Node + "' AND product_status = 0 ORDER BY `level` LIMIT "
//				+ MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_LIMIT;
//		Sql sqlClass = Sqls.queryEntity(sql);
//		sqlClass.setEntity(mysqlDao.getEntity(ElemeProductTaskJob.class));
//		mysqlDao.execute(sqlClass);
//		List<ElemeProductTaskJob> list = sqlClass.getList(ElemeProductTaskJob.class);
        return list;
    }

    @Override
    public void doStart() {
        if (MyConstant.MOBILE_ELEME_CONFIG.INFO) {
        	logger.info("初始化解析MobileElemeProductTask任务...");
            tDao.update(ElemeProductTaskJob.class, Chain.make("product_status", 0),
                    Cnd.where("product_status", "=", 1).and("node", "=", MyConstant.Node));
            try {
                if (QuartzManager.getTrigger("MobileElemeProductTask") == null) {
                    QuartzManager.addJob("MobileElemeProductTask", this, MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_TIME);
                } else {
                    logger.info("已经存在任务:MobileElemeProductTask");
                }
            } catch (Exception e) {
                logger.info("创建MobileElemeProductTask任务失败！");
            }
        }
    }

    @Override
    public Thread getThread(ElemeProductTaskJob task) {
        // TODO Auto-generated method stub
        return new MobileElemeProductThread(task);
    }

}
