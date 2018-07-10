package com.reptile.jdDaoJa.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("jddaojia_store_info")
public class JDDaoJiaStoreInfo {
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
	private String store_url;
	@Column
	private String freight_start;
	@Column
	private String freightAndtime;
	@Column
	private String freight_money;
	@Column
	private String announcement;
	@Column
	private String store_star;
	@Column
	private String comment_num;
	@Column
	private String comment_user_name;
	@Column
	private String comment_user_star;
	@Column
	private String comment_user_content;
	@Column
	private String comment_user_label;
	@Column
	private String comment_user_datetime;
	@Column
	private String store_goods_total;
	@Column
	private String coupons;

	@Column
	private String monthSale;
	@Column
	private String store_follownum;
	@Column
	private String store_shophours;
	@Column
	private String store_address;
	@Column
	private String store_phone;
	@Column
	private String store_qualification;
	@Column
	private String store_report;
	@Column
	private String overtime_payment;
	@Column
	private String store_report_reward;
	
	
	@Column
	private Date create_time;
	@Column
	private String project_code;
	@Column
	private String down_type;
	@Column
	private String website;
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
	public String getFreightAndtime() {
		return freightAndtime;
	}
	public void setFreightAndtime(String freightAndtime) {
		this.freightAndtime = freightAndtime;
	}
	public String getFreight_money() {
		return freight_money;
	}
	public void setFreight_money(String freight_money) {
		this.freight_money = freight_money;
	}
	public String getAnnouncement() {
		return announcement;
	}
	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
	public String getStore_star() {
		return store_star;
	}
	public void setStore_star(String store_star) {
		this.store_star = store_star;
	}
	public String getComment_num() {
		return comment_num;
	}
	public void setComment_num(String comment_num) {
		this.comment_num = comment_num;
	}
	public String getComment_user_name() {
		return comment_user_name;
	}
	public void setComment_user_name(String comment_user_name) {
		this.comment_user_name = comment_user_name;
	}
	public String getComment_user_star() {
		return comment_user_star;
	}
	public void setComment_user_star(String comment_user_star) {
		this.comment_user_star = comment_user_star;
	}
	public String getComment_user_content() {
		return comment_user_content;
	}
	public void setComment_user_content(String comment_user_content) {
		this.comment_user_content = comment_user_content;
	}
	public String getComment_user_label() {
		return comment_user_label;
	}
	public void setComment_user_label(String comment_user_label) {
		this.comment_user_label = comment_user_label;
	}
	public String getComment_user_datetime() {
		return comment_user_datetime;
	}
	public void setComment_user_datetime(String comment_user_datetime) {
		this.comment_user_datetime = comment_user_datetime;
	}
	public String getStore_goods_total() {
		return store_goods_total;
	}
	public void setStore_goods_total(String store_goods_total) {
		this.store_goods_total = store_goods_total;
	}
	public String getMonthSale() {
		return monthSale;
	}
	public void setMonthSale(String monthSale) {
		this.monthSale = monthSale;
	}
	public String getStore_follownum() {
		return store_follownum;
	}
	public void setStore_follownum(String store_follownum) {
		this.store_follownum = store_follownum;
	}
	public String getStore_shophours() {
		return store_shophours;
	}
	public void setStore_shophours(String store_shophours) {
		this.store_shophours = store_shophours;
	}
	public String getStore_address() {
		return store_address;
	}
	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}
	public String getStore_phone() {
		return store_phone;
	}
	public void setStore_phone(String store_phone) {
		this.store_phone = store_phone;
	}
	public String getStore_qualification() {
		return store_qualification;
	}
	public void setStore_qualification(String store_qualification) {
		this.store_qualification = store_qualification;
	}
	public String getStore_report() {
		return store_report;
	}
	public void setStore_report(String store_report) {
		this.store_report = store_report;
	}
	public String getOvertime_payment() {
		return overtime_payment;
	}
	public void setOvertime_payment(String overtime_payment) {
		this.overtime_payment = overtime_payment;
	}
	public String getStore_report_reward() {
		return store_report_reward;
	}
	public void setStore_report_reward(String store_report_reward) {
		this.store_report_reward = store_report_reward;
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
	public String getCoupons() {
		return coupons;
	}
	public void setCoupons(String coupons) {
		this.coupons = coupons;
	}
	public String getStore_url() {
		return store_url;
	}
	public void setStore_url(String store_url) {
		this.store_url = store_url;
	}
	public String getFreight_start() {
		return freight_start;
	}
	public void setFreight_start(String freight_start) {
		this.freight_start = freight_start;
	}
	
}
