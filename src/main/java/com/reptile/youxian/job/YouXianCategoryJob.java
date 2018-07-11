package com.reptile.youxian.job;

import com.bds.base.dao.MySqlDataSource;
import com.reptile.jdfresh.common.BaseJob;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author eli
 * @date 2017/11/17 11:29
 */
@Table("youxian_category_job")
public class YouXianCategoryJob extends BaseJob {
    @Column
    private String city;
    @Column
    private String url;
    @Column
    private String category;
    @Column
    private String area;
    @Column
    private String areaid;
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
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

    public String getCategory() {
        return category;
    }



    public void setCategory(String category) {
        this.category = category;
    }
    public static void main(String[] args) {
        Dao mysqlDao = MySqlDataSource.getMysqlDao("fresh");
        System.out.println("#############");
        mysqlDao.create(YouXianCategoryJob.class,false);
        System.out.println("#############");
    }

}
