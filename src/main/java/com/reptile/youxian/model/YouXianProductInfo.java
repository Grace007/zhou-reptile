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
@Table("youxian_product_info")
@TableIndexes({@Index(name="unique_youxian_product_info",fields = {"request_id","task_id","area","city","sku","category"},unique = true)})
public class YouXianProductInfo extends BaseBI {
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
    private String subtitle;
    @Column
    private String product_url;
    @Column
    private String sku;
    @Column
    private String country;
    @Column
    private String sales_volume;
    @Column
    private String delivery_mode_name;
    @Column
    private String vip_card;
    @Column
    private String product_integarl_text;
    @Column
    private String share_product_text;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(String sales_volume) {
        this.sales_volume = sales_volume;
    }

    public String getDelivery_mode_name() {
        return delivery_mode_name;
    }

    public void setDelivery_mode_name(String delivery_mode_name) {
        this.delivery_mode_name = delivery_mode_name;
    }

    public String getVip_card() {
        return vip_card;
    }

    public void setVip_card(String vip_card) {
        this.vip_card = vip_card;
    }

    public String getProduct_integarl_text() {
        return product_integarl_text;
    }

    public void setProduct_integarl_text(String product_integarl_text) {
        this.product_integarl_text = product_integarl_text;
    }

    public String getShare_product_text() {
        return share_product_text;
    }

    public void setShare_product_text(String share_product_text) {
        this.share_product_text = share_product_text;
    }
    public static void main(String[] args) {
        TaskToBi tobi = BIDataSource.getBiDataSource("fresh","DC_FRESH","get_sort_info");
        Dao bidao= tobi.getBiDao();
        bidao.create(YouXianProductInfo.class,false);
    }
}
