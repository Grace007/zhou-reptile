package com.reptile.mobile_eleme.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("ele_search_shop_info${all}")
public class EleShopInfo {

    @Id(auto = true)
    private long id;
    @Column
    private long request_id;
    @Column
    private long task_id;
    @Column
    private Date create_date;
    @Column
    private Date create_time;
    @Column
    private String restaurant_id;
    @Column
    private String sname;
    @Column
    private String url;
    @Column
    private String average_cost;
    @Column
    private String address;
    @Column
    private String opening_hours;
    @Column
    private String activities;
    @Column
    private String shop_description;
    @Column
    private String score;
    @Column
    private String monthSale;
    @Column
    private String min_price;
    @Column
    private String delivery_detail;
    @Column
    private String shipEfficiency;
    @Column
    private String distance;
    @Column
    private String phone;
    @Column
    private String delivery_fee_detail;
    @Column
    private String promotion_info;
    @Column
    private String payInfo;
    @Column
    private String delivery_mode;
    @Column
    private String keyword;
    @Column
    private String city_name;
    @Column
    private String category;
    @Column
    private String category_id;
    @Column
    private String sub_category;
    @Column
    private String uniqueId;
    @Column
    private int rank;
    @Column
    private String office_id;
    @Column
    private String office_name;

    public long getId() {
        return id;
    }

    public long getRequest_id() {
        return request_id;
    }

    public long getTask_id() {
        return task_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public String getSname() {
        return sname;
    }

    public String getUrl() {
        return url;
    }

    public String getAverage_cost() {
        return average_cost;
    }

    public String getAddress() {
        return address;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public String getActivities() {
        return activities;
    }

    public String getShop_description() {
        return shop_description;
    }

    public String getScore() {
        return score;
    }

    public String getMonthSale() {
        return monthSale;
    }

    public String getMin_price() {
        return min_price;
    }

    public String getDelivery_detail() {
        return delivery_detail;
    }

    public String getShipEfficiency() {
        return shipEfficiency;
    }

    public String getDistance() {
        return distance;
    }

    public String getPhone() {
        return phone;
    }

    public String getDelivery_fee_detail() {
        return delivery_fee_detail;
    }

    public String getPromotion_info() {
        return promotion_info;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public String getDelivery_mode() {
        return delivery_mode;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getCategory() {
        return category;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRequest_id(long request_id) {
        this.request_id = request_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAverage_cost(String average_cost) {
        this.average_cost = average_cost;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public void setShop_description(String shop_description) {
        this.shop_description = shop_description;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setMonthSale(String monthSale) {
        this.monthSale = monthSale;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public void setDelivery_detail(String delivery_detail) {
        this.delivery_detail = delivery_detail;
    }

    public void setShipEfficiency(String shipEfficiency) {
        this.shipEfficiency = shipEfficiency;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDelivery_fee_detail(String delivery_fee_detail) {
        this.delivery_fee_detail = delivery_fee_detail;
    }

    public void setPromotion_info(String promotion_info) {
        this.promotion_info = promotion_info;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public void setDelivery_mode(String delivery_mode) {
        this.delivery_mode = delivery_mode;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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
