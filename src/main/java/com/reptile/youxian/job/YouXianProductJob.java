package com.reptile.youxian.job;

import com.bds.base.dao.MySqlDataSource;
import com.reptile.jdfresh.common.BaseJob;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * @author eli
 * @date 2017/11/17 11:30
 */
@Table("youxian_product_job")
@TableIndexes({@Index(name="unqiue_youxian_product_job",fields = {"city","sku","area","request_id","task_id","category"},unique = true)})
public class YouXianProductJob extends BaseJob {
    @Column
    private String city;
    @Column
    private String url;
    @Column
    private String category;
    @Column
    private String pname;
    @Column
    private String sku;
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



    @Column

    private int status;
    @Column
    private String param;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
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

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
    public static void main(String[] args) {
        Dao mysqlDao = MySqlDataSource.getMysqlDao("fresh");
        System.out.println("#############");
        mysqlDao.create(YouXianProductJob.class,false);
        System.out.println("#############");
    }
}
