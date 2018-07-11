package com.reptile.ec.vip2.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("vip2_product_detail")
public class VipProductDetail {
	@Id(auto = true)
	private int id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private Date create_date;
	@Column
	private Date create_time;
	@Column
	private String keyword;
	@Column
	private String category;
	@Column
	private String category_id;
	/**
	 * 店铺名
	 */
	@Column
	private String brand_name;
	/**
	 * 店铺id
	 */
	@Column
	private String brand_id;
	@Column
	private String url;
	@Column
	private String pname;
	@Column
	private String pid;
	@Column
	private String description;
	@Column
	private String current_price;
	@Column
	private String vip_coins;
	@Column
	private String promotion;
	@Column
	private String original_price;
	@Column
	private String discount;
	@Column
	private String saving;
	@Column
	private String sales;
	@Column
	private String promise;
	@Column
	private String special_performance;
	@Column
	private String score;
	@Column
	private String navigation;
	@Column
	private String introduction;
	@Column
	private String remaining_time;
	@Column
	private String delivery_fee;
	@Column
	private String special_price;
	@Column
	private String unique_id;

	public int getId() {
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

	public String getKeyword() {
		return keyword;
	}

	public String getCategory() {
		return category;
	}

	public String getUrl() {
		return url;
	}

	public String getPname() {
		return pname;
	}

	public String getPid() {
		return pid;
	}

	public String getDescription() {
		return description;
	}

	public String getCurrent_price() {
		return current_price;
	}

	public String getVip_coins() {
		return vip_coins;
	}

	public String getPromotion() {
		return promotion;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public String getDiscount() {
		return discount;
	}

	public String getSaving() {
		return saving;
	}

	public String getSales() {
		return sales;
	}

	public String getSpecial_performance() {
		return special_performance;
	}

	public String getScore() {
		return score;
	}

	public String getNavigation() {
		return navigation;
	}

	public String getIntroduction() {
		return introduction;
	}

	public String getRemaining_time() {
		return remaining_time;
	}

	public void setId(int id) {
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

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}

	public void setVip_coins(String vip_coins) {
		this.vip_coins = vip_coins;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public void setSaving(String saving) {
		this.saving = saving;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getPromise() {
		return promise;
	}

	public void setPromise(String promise) {
		this.promise = promise;
	}

	public void setSpecial_performance(String special_performance) {
		this.special_performance = special_performance;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void setRemaining_time(String remaining_time) {
		this.remaining_time = remaining_time;
	}

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	public String getCategory_id() {
		return category_id;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getDelivery_fee() {
		return delivery_fee;
	}

	public void setDelivery_fee(String delivery_fee) {
		this.delivery_fee = delivery_fee;
	}

	public String getSpecial_price() {
		return special_price;
	}

	public void setSpecial_price(String special_price) {
		this.special_price = special_price;
	}
}
