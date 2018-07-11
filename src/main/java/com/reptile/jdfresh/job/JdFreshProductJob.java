package com.reptile.jdfresh.job;

import com.bds.base.dao.MySqlDataSource;
import com.reptile.jdfresh.common.BaseJob;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import java.util.Date;

/**
 * @author eli
 * @date 2017/11/15 16:12
 */
@Table("jd_fresh_product_job")
@TableIndexes({@Index(name="unqiue_product_job",fields = {"request_id","task_id","province","city","pid"},unique = true)})
public class JdFreshProductJob extends BaseJob {
    @Column
    private String province;
    @Column
    private String city;
    @Column
    private String pname;
    @Column
    private String category;
    @Column
    private String price;
    @Column
    private String pid;
    @Column
    private String shopid;
    @Column
    private String shop;
    @Column
    private String shoptype;
    @Column
    private String url;
    @Column
    private String cat3;
    @Column
    private Date create_date;
    @Column
    private int status;

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    @Column
    private String cookie;
    @Column
    private String cat;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


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

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getShoptype() {
        return shoptype;
    }

    public void setShoptype(String shoptype) {
        this.shoptype = shoptype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }


    @Override
    public int getStatus() {
        return status;
    }
    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    public static void main(String[] args) {
        Dao mysqlDao = MySqlDataSource.getMysqlDao("fresh");
        System.out.println("#############");
        mysqlDao.create(JdFreshProductJob.class,false);
        System.out.println("#############");
    }
}
