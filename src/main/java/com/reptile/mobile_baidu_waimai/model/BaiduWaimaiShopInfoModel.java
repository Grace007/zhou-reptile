package com.reptile.mobile_baidu_waimai.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("baidu_waimai_shop_info${all}")
public class BaiduWaimaiShopInfoModel {
	@Id
	private long id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private Date create_time;
	@Column
	private String unique_id;
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
	private String business_hour;
	@Column
	private String shop_address;
	@Column
	private String average_speed;
	@Column
	private String min_price;
	@Column
	private String distribution_fee;
	@Column
	private String total_score;
	@Column
	private String total_comments;
	@Column
	private String five_star_numbers;
	@Column
	private String four_star_numbers;
	@Column
	private String three_star_numbers;
	@Column
	private String two_star_numbers;
	@Column
	private String one_star_numbers;
	@Column
	private String payment_info;
	@Column
	private String announcement;
	@Column
	private String service_score;
	@Column
	private String dish_score;
	@Column
	private String monthly_sales;
	@Column
	private String phone;
	@Column
	private String shop_logo;
    @Column
    private String Latitude;
    @Column
    private String Longitude;

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

	public String getBusiness_hour() {
		return business_hour;
	}

	public void setBusiness_hour(String business_hour) {
		this.business_hour = business_hour;
	}

	public String getShop_address() {
		return shop_address;
	}

	public void setShop_address(String shop_address) {
		this.shop_address = shop_address;
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

	public String getTotal_comments() {
		return total_comments;
	}

	public void setTotal_comments(String total_comments) {
		this.total_comments = total_comments;
	}

	public String getFive_star_numbers() {
		return five_star_numbers;
	}

	public void setFive_star_numbers(String five_star_numbers) {
		this.five_star_numbers = five_star_numbers;
	}

	public String getFour_star_numbers() {
		return four_star_numbers;
	}

	public void setFour_star_numbers(String four_star_numbers) {
		this.four_star_numbers = four_star_numbers;
	}

	public String getThree_star_numbers() {
		return three_star_numbers;
	}

	public void setThree_star_numbers(String three_star_numbers) {
		this.three_star_numbers = three_star_numbers;
	}

	public String getTwo_star_numbers() {
		return two_star_numbers;
	}

	public void setTwo_star_numbers(String two_star_numbers) {
		this.two_star_numbers = two_star_numbers;
	}

	public String getOne_star_numbers() {
		return one_star_numbers;
	}

	public void setOne_star_numbers(String one_star_numbers) {
		this.one_star_numbers = one_star_numbers;
	}

	public String getPayment_info() {
		return payment_info;
	}

	public void setPayment_info(String payment_info) {
		this.payment_info = payment_info;
	}

	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	public String getService_score() {
		return service_score;
	}

	public String getDish_score() {
		return dish_score;
	}

	public String getMonthly_sales() {
		return monthly_sales;
	}

	public void setService_score(String service_score) {
		this.service_score = service_score;
	}

	public void setDish_score(String dish_score) {
		this.dish_score = dish_score;
	}

	public void setMonthly_sales(String monthly_sales) {
		this.monthly_sales = monthly_sales;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getShop_logo() {
		return shop_logo;
	}

	public void setShop_logo(String shop_logo) {
		this.shop_logo = shop_logo;
	}
}
