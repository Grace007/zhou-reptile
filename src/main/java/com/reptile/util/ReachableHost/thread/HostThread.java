package com.reptile.util.ReachableHost.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.reptile.util.ReachableHost.job.Host;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
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
            
            /***
             * 此处更新jar包的最后修改时间
             */
            try {
                String path = "D:\\Work\\newbase\\o2o\\target\\o2o-0.0.1-SNAPSHOT.jar";
        		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		String [] paths = {
        				"D:\\Work\\newbase\\o2o\\target\\o2o-0.0.1-SNAPSHOT.jar",
        				"C:\\hy_meituan\\o2o-0.0.1-SNAPSHOT.jar",
        				"C:\\java\\hy_meituan\\o2o-0.0.1-SNAPSHOT.jar",
        				"D:\\hy_meituan\\o2o-0.0.1-SNAPSHOT.jar",
        				"D:\\java\\hy_meituan\\o2o-0.0.1-SNAPSHOT.jar",
        				"C:\\java\\baidu_search\\hy_meituan\\o2o-0.0.1-SNAPSHOT.jar",
        				"D:\\java\\baidu_search\\hy_meituan\\o2o-0.0.1-SNAPSHOT.jar"
        		} ;
        		File f = new File(path);
        		for (int i = 0; i < paths.length; i++) {
        			f = new File(paths[i]);
        			if (f.exists()) {
        				long timeStamp = f.lastModified();
        				Date date = new Date(timeStamp);
        				String res = simpleDateFormat.format(date);
        				System.out.println("jar包的最后修改时间："+res);
        				
                        tDao.update(Host.class, Chain.make("jarLastModified", res), Cnd.where("id", "=", task.getId()));

        				break ;
        			}
        		}
			} catch (Exception e) {
//				logger.error(e);
			}

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
