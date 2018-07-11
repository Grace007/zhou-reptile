package com.reptile.jdfresh.job;

import com.bds.base.dao.MySqlDataSource;
import com.reptile.jdfresh.common.BaseJob;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * @author eli
 * @date 2017/11/15 18:52
 */
@Table("jd_fresh_comment_job")
@TableIndexes({@Index(name="unqiue_comment_job",fields = {"pid"},unique = true)})
public class JdFreshCommentJob extends BaseJob {
    @Column
    private String province;
    @Column
    private String city;
    @Column
    private String pid;
    @Column
    private String pname;
    @Column
    private String shopid;
    @Column
    private String shop;
    @Column
    private String shoptype;
    @Column
    private String category;
    @Column
    private int status;
    @Column
    private int pagestart;
    @Column
    private int pagestop;
    @Column
    private String comment_count;
    @Column
    private int page_limit;

    public int getPage_limit() {
        return page_limit;
    }

    public void setPage_limit(int page_limit) {
        this.page_limit = page_limit;
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


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
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

    public static void main(String[] args) {
        Dao mysqlDao = MySqlDataSource.getMysqlDao("fresh");
        System.out.println("#############");
        mysqlDao.create(JdFreshCommentJob.class,false);
        System.out.println("#############");
    }
}
