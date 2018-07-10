package com.reptile.mobile_eleme.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("eleme_shop_info${all}")
public class ElemeShopInfoModel {
	@Id
	private long id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private Date create_time;
	@Column
	private String shop_id;
	@Column
	private String shop_name;
	@Column
	private String city_name;
	@Column
	private String keyword;
	@Column
	private String url;
	@Column
	private String shop_score;
	@Column
	private String monthly_sales;
	@Column
	private String shop_type;
	@Column
	private String phone;
	@Column
	private String min_price;
	@Column
	private String distribution_fee;
	@Column
	private String average_speed;
	@Column
	private String total_score;
	@Column
	private String compare;
	@Column
	private String service_score;
	@Column
	private String product_score;
	@Column
	private String shop_address;
	@Column
	private String business_hour;
	@Column
	private String supports;
	@Column
	private String announcement;
	@Column
	private String check_result;
	@Column
	private String check_date;
	@Column
	private String delivery_detail;
	@Column
	private String promotion_info;
	@Column
	private String mobile_promotion_info;
	@Column
	private String activities;
	@Column
	private String delivery_mode;
	@Column
	private String shop_tags;
	@Column
	private String unique_id;
    @Column
    private String Latitude;
    @Column
    private String Longitude;
	@Column
	private String img_url;

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

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getShop_score() {
		return shop_score;
	}

	public void setShop_score(String shop_score) {
		this.shop_score = shop_score;
	}

	public String getMonthly_sales() {
		return monthly_sales;
	}

	public void setMonthly_sales(String monthly_sales) {
		this.monthly_sales = monthly_sales;
	}

	public String getShop_type() {
		return shop_type;
	}

	public void setShop_type(String shop_type) {
		this.shop_type = shop_type;
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

	public String getAverage_speed() {
		return average_speed;
	}

	public void setAverage_speed(String average_speed) {
		this.average_speed = average_speed;
	}

	public String getTotal_score() {
		return total_score;
	}

	public void setTotal_score(String total_score) {
		this.total_score = total_score;
	}

	public String getCompare() {
		return compare;
	}

	public void setCompare(String compare) {
		this.compare = compare;
	}

	public String getService_score() {
		return service_score;
	}

	public void setService_score(String service_score) {
		this.service_score = service_score;
	}

	public String getProduct_score() {
		return product_score;
	}

	public void setProduct_score(String product_score) {
		this.product_score = product_score;
	}

	public String getShop_address() {
		return shop_address;
	}

	public void setShop_address(String shop_address) {
		this.shop_address = shop_address;
	}

	public String getBusiness_hour() {
		return business_hour;
	}

	public void setBusiness_hour(String business_hour) {
		this.business_hour = business_hour;
	}

	public String getCheck_result() {
		return check_result;
	}

	public void setCheck_result(String check_result) {
		this.check_result = check_result;
	}

	public String getCheck_date() {
		return check_date;
	}

	public void setCheck_date(String check_date) {
		this.check_date = check_date;
	}

	public String getDelivery_detail() {
		return delivery_detail;
	}

	public String getPromotion_info() {
		return promotion_info;
	}

	public void setDelivery_detail(String delivery_detail) {
		this.delivery_detail = delivery_detail;
	}

	public void setPromotion_info(String promotion_info) {
		this.promotion_info = promotion_info;
	}

	public String getDelivery_mode() {
		return delivery_mode;
	}

	public void setDelivery_mode(String delivery_mode) {
		this.delivery_mode = delivery_mode;
	}

	public String getActivities() {
		return activities;
	}

	public void setActivities(String activities) {
		this.activities = activities;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSupports() {
		return supports;
	}

	public void setSupports(String supports) {
		this.supports = supports;
	}

	public String getShop_tags() {
		return shop_tags;
	}

	public void setShop_tags(String shop_tags) {
		this.shop_tags = shop_tags;
	}

	public String getAnnouncement() {
		return announcement;
	}

	public String getMobile_promotion_info() {
		return mobile_promotion_info;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	public void setMobile_promotion_info(String mobile_promotion_info) {
		this.mobile_promotion_info = mobile_promotion_info;
	}

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
}
