package com.reptile.jdDaoJa.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("jddaojia_product_info")
public class JDDaoJiaProductInfo {
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
	private String freightAndtime;
	@Column
	private String freight_money;
	@Column
	private String announcement;
	@Column
	private String product_id;
	@Column
	private String product_category;
	@Column
	private String product_name;
	@Column
	private String product_num;
	@Column
	private String product_monthSale;
	@Column
	private String product_highOpinion;
	@Column
	private String product_price;
	@Column
	private String product_inStock;
	@Column
	private String product_promotions;
	

	
	
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
	public String getProduct_category() {
		return product_category;
	}
	public void setProduct_category(String product_category) {
		this.product_category = product_category;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_num() {
		return product_num;
	}
	public void setProduct_num(String product_num) {
		this.product_num = product_num;
	}
	public String getProduct_monthSale() {
		return product_monthSale;
	}
	public void setProduct_monthSale(String product_monthSale) {
		this.product_monthSale = product_monthSale;
	}
	public String getProduct_highOpinion() {
		return product_highOpinion;
	}
	public void setProduct_highOpinion(String product_highOpinion) {
		this.product_highOpinion = product_highOpinion;
	}
	public String getProduct_price() {
		return product_price;
	}
	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}
	public String getProduct_inStock() {
		return product_inStock;
	}
	public void setProduct_inStock(String product_inStock) {
		this.product_inStock = product_inStock;
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
	public String getProduct_promotions() {
		return product_promotions;
	}
	public void setProduct_promotions(String product_promotions) {
		this.product_promotions = product_promotions;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	
	
	
	
}
