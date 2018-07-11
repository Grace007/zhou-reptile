package com.reptile.jdfresh.jdfreshtest;

import com.bds.base.dao.MySqlDataSource;
import com.reptile.jdfresh.job.JdFreshProductJob;
import org.junit.Test;
import org.nutz.dao.Dao;

/**
 * @author eli
 * @date 2017/11/15 11:35
 */
public class CreatTable {
    @Test
    public void test1(){
        Dao mysqlDao = MySqlDataSource.getMysqlDao("fresh");
        System.out.println("#############");
        mysqlDao.create(JdFreshProductJob.class,false);
        System.out.println("#############");
    }
}
