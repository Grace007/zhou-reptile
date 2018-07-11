package com.reptile.youxian.model;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.reptile.jdfresh.common.BaseBI;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * @author eli
 * @date 2017/11/17 11:31
 */
@Table("youxian_product_simple")
@TableIndexes({@Index(name="unique_youxian_product_simple",fields = {"request_id","task_id","area","city","sku","category"},unique = true)})
public class YouXianProductSimple extends BaseBI {
    @Column
    private String city;
    @Column
    private String category;
    @Column
    private String pname;
    @Column
    private String price;
    @Column
    private String current_price;
    @Column
    private String vip_price;
    @Column
    private String product_tags;
    @Column
    private String sku;
    @Column
    private String promote_tag;
    @Column
    private String subtitle;
    @Column
    private String product_url;
    @Column
    private String area;
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getVip_price() {
        return vip_price;
    }

    public void setVip_price(String vip_price) {
        this.vip_price = vip_price;
    }

    public String getProduct_tags() {
        return product_tags;
    }

    public void setProduct_tags(String product_tags) {
        this.product_tags = product_tags;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPromote_tag() {
        return promote_tag;
    }

    public void setPromote_tag(String promote_tag) {
        this.promote_tag = promote_tag;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }
    public static void main(String[] args) {
        TaskToBi tobi = BIDataSource.getBiDataSource("fresh","DC_FRESH","get_sort_info");
        Dao bidao= tobi.getBiDao();
        bidao.create(YouXianProductSimple.class,false);
    }


}
