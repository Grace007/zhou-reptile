package com.reptile.jdfresh.job;

import com.bds.base.dao.MySqlDataSource;
import com.reptile.jdfresh.common.BaseJob;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author eli
 * @date 2017/11/14 15:09
 */
@Table("jd_fresh_source_job")
public class JdFreshSourceJob extends BaseJob {
    @Column
    private String province;
    @Column
    private String city;
    @Column
    private String url;
    @Column
    private int pagestart;
    @Column
    private int pagestop;

    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPagestart() {
        return pagestart;
    }

    public void setPagestart(int pagestart) {
        this.pagestart = pagestart;
    }

    public int getPagestop() {
        return pagestop;
    }

    public void setPagestop(int pagestop) {
        this.pagestop = pagestop;
    }

    public static void main(String[] args) {
        Dao mysqlDao = MySqlDataSource.getMysqlDao("fresh");
        System.out.println("#############");
        mysqlDao.create(JdFreshSourceJob.class,false);
        System.out.println("#############");
    }


}
