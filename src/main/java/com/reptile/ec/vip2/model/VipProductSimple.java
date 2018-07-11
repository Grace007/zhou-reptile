package com.reptile.ec.vip2.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("vip2_product_simple")
public class VipProductSimple {
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
	private String url;
	@Column
	private String pname;
	@Column
	private String pid;
	@Column
	private String brand_id;
	@Column
	private String current_price;
	@Column
	private String original_price;
	@Column
	private String discount;
	@Column
	private String promotion;
	@Column
	private String remaining_time;
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

	public String getCurrent_price() {
		return current_price;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public String getDiscount() {
		return discount;
	}

	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public String getRemaining_time() {
		return remaining_time;
	}

	public String getUnique_id() {
		return unique_id;
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

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}

	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public void setRemaining_time(String remaining_time) {
		this.remaining_time = remaining_time;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}
}
