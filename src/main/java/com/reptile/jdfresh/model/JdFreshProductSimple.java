package com.reptile.jdfresh.model;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.reptile.jdfresh.common.BaseBI;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import java.util.Date;

/**
 * @author eli
 * @date 2017/11/13 11:16
 */
@Table("jd_fresh_product_simple")
@TableIndexes({@Index(name="unique1",fields = {"request_id","task_id","province","city","pid"},unique = true)})
public class JdFreshProductSimple extends BaseBI {
    @Column
    private String province;
    @Column
    private String city;
    @Column
    private String pname;
    @Column
    private String category;
    @Column
    private String comment_num;
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
    private String district;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
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
    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment_num() {
        return comment_num;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public static void main(String[] args) {
        TaskToBi tobi = BIDataSource.getBiDataSource("fresh","DC_FRESH","get_sort_info");
        Dao bidao= tobi.getBiDao();
        bidao.create(JdFreshProductSimple.class,false);
    }

}
