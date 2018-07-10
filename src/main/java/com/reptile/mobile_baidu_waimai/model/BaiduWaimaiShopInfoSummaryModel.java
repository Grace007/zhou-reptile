package com.reptile.mobile_baidu_waimai.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("baidu_waimai_shop_info_summary${all}")
public class BaiduWaimaiShopInfoSummaryModel {
    @Id
    private long id;
    @Column
    private long request_id;
    @Column
    private long task_id;
    @Column
    private String keyword;
    @Column
    private String city_name;
    @Column
    private String city_id;
    @Column
    private String shop_id;
    @Column
    private String shop_name;
    @Column
    private String url;
    @Column
    private String average_speed;
    @Column
    private String min_price;
    @Column
    private String distribution_fee;
    @Column
    private String total_score;
    @Column
    private String sale;
    @Column
    private String pay;
    @Column
    private String xin;
    @Column
    private String jian;
    @Column
    private String fan;
    @Column
    private String logistics;
    @Column
    private String preorder;
    @Column
    private String te;
    @Column
    private String shop_coupon;
    @Column
    private String mian;
    @Column
    private String zeng;
    @Column
    private String certification;
    @Column
    private String coupon;
    @Column
    private String invoice;
    @Column
    private String express;
    @Column
    private String distance;
    @Column
    private String welfare_info;
    @Column
    private String announcement;
    @Column
    private String delivery_mode;
    @Column
    private Date create_date;
    @Column
    private Date create_time;
    @Column
    private String unique_id;
    @Column
    private String category;
    @Column
    private String category_id;
    @Column
    private String sub_category;
    @Column
    private int rank;
    @Column
    private String office_id;
    @Column
    private String office_name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRequest_id() {
        return request_id;
    }

    public void setRequest_id(long request_id) {
        this.request_id = request_id;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAverage_speed() {
        return average_speed;
    }

    public void setAverage_speed(String average_speed) {
        this.average_speed = average_speed;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getDistribution_fee() {
        return distribution_fee;
    }

    public void setDistribution_fee(String distribution_fee) {
        this.distribution_fee = distribution_fee;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getWelfare_info() {
        return welfare_info;
    }

    public void setWelfare_info(String welfare_info) {
        this.welfare_info = welfare_info;
    }

    public String getDelivery_mode() {
        return delivery_mode;
    }

    public void setDelivery_mode(String delivery_mode) {
        this.delivery_mode = delivery_mode;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getPay() {
        return pay;
    }

    public String getXin() {
        return xin;
    }

    public String getJian() {
        return jian;
    }

    public String getFan() {
        return fan;
    }

    public String getLogistics() {
        return logistics;
    }

    public String getPreorder() {
        return preorder;
    }

    public String getTe() {
        return te;
    }

    public String getShop_coupon() {
        return shop_coupon;
    }

    public String getMian() {
        return mian;
    }

    public String getZeng() {
        return zeng;
    }

    public String getCertification() {
        return certification;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getExpress() {
        return express;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public void setXin(String xin) {
        this.xin = xin;
    }

    public void setJian(String jian) {
        this.jian = jian;
    }

    public void setFan(String fan) {
        this.fan = fan;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    public void setPreorder(String preorder) {
        this.preorder = preorder;
    }

    public void setTe(String te) {
        this.te = te;
    }

    public void setShop_coupon(String shop_coupon) {
        this.shop_coupon = shop_coupon;
    }

    public void setMian(String mian) {
        this.mian = mian;
    }

    public void setZeng(String zeng) {
        this.zeng = zeng;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getCategory() {
        return category;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }
}
