package com.reptile.youxian.host;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

public class HostThread extends Thread {
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(HostThread.class);
    private Host task;

    public HostThread(Host task) {
        this.task = task;
    }

    @Override
    public void run() {
        logger.info("正在ping www.baidu.com...");
        String line, result = "";
        try {
            Process pro = Runtime.getRuntime().exec("ping www.baidu.com");
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    pro.getInputStream(), "gb2312"));
            while ((line = buf.readLine()) != null) {
                result += line + "\n";
                //System.out.println(line);
            }
//            System.out.print(result);
            if (result.contains("100% 丢失")) {
                tDao.update(Host.class, Chain.make("reachable", 0).add("create_date", new Date()).add("create_time", new Date()), Cnd.where("id", "=", task.getId()));
                logger.info("Host[" + task.getHost() + "],port[" + task.getPort() + "]连接失败");
            } else {
                tDao.update(Host.class, Chain.make("reachable", 1).add("retry_count", (task.getRetry_count() + 1)).add("create_date", new Date()).add("create_time", new Date()), Cnd.where("id", "=", task.getId()));
                logger.info("Host[" + task.getHost() + "],port[" + task.getPort() + "]连接成功");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
