package com.reptile.jdDaoJa.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("jddaojia_store_simple")
public class JDDaojiaStoreSimple {
	@Id
	private long id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private String city_name;
	@Column
	private String addressKey;
	@Column
	private String address;
	
	@Column
	private String store_id;
	@Column
	private String store_name;
	@Column
	private String category;
	@Column
	private String freightAndtime;
	@Column
	private String store_star;
	@Column
	private String monthSale;
	@Column
	private String freight_start;
	@Column
	private String distance;
	@Column
	private String freight_money;
	@Column
	private String sales_promotion;
	@Column
	private String overtime_payment;
	@Column 
	private String store_url;
	@Column
	private Date create_time;
	@Column
	private String project_code;
	@Column
	private String down_type;
	@Column
	private String website;
	@Column
	private int ranking;
	
	
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
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getAddressKey() {
		return addressKey;
	}
	public void setAddressKey(String addressKey) {
		this.addressKey = addressKey;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFreightAndtime() {
		return freightAndtime;
	}
	public void setFreightAndtime(String freightAndtime) {
		this.freightAndtime = freightAndtime;
	}
	public String getStore_star() {
		return store_star;
	}
	public void setStore_star(String store_star) {
		this.store_star = store_star;
	}
	public String getMonthSale() {
		return monthSale;
	}
	public void setMonthSale(String monthSale) {
		this.monthSale = monthSale;
	}

	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getFreight_money() {
		return freight_money;
	}
	public void setFreight_money(String freight_money) {
		this.freight_money = freight_money;
	}
	public String getSales_promotion() {
		return sales_promotion;
	}
	public void setSales_promotion(String sales_promotion) {
		this.sales_promotion = sales_promotion;
	}
	public String getOvertime_payment() {
		return overtime_payment;
	}
	public void setOvertime_payment(String overtime_payment) {
		this.overtime_payment = overtime_payment;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getProject_code() {
		return project_code;
	}
	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}
	public String getDown_type() {
		return down_type;
	}
	public void setDown_type(String down_type) {
		this.down_type = down_type;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getFreight_start() {
		return freight_start;
	}
	public void setFreight_start(String freight_start) {
		this.freight_start = freight_start;
	}
	public String getStore_url() {
		return store_url;
	}
	public void setStore_url(String store_url) {
		this.store_url = store_url;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	
	
}
