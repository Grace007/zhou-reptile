package com.reptile.ec.vip2.job;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("vip_category_product_job")
public class VipProductJob {
	@Id(auto = true)
	private int id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private Date create_date;
	@Column
	private String keyword;
	@Column
	private String category;
	@Column
	private String url;
	@Column
	private String product_name;
	@Column
	private String product_id;
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
	private String category_url;
	@Column
	private int current_page;
	@Column
	private int total_page;
	@Column
	private int status;
	@Column
	private String node;
	@Column
	private int level;
	@Column
	private String project_code;
	@Column
	private String down_type;

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

	public String getKeyword() {
		return keyword;
	}

	public String getCategory() {
		return category;
	}

	public String getUrl() {
		return url;
	}

	public String getProduct_name() {
		return product_name;
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

	public void setRemaining_time(String remaining_time) {
		this.remaining_time = remaining_time;
	}

	public String getCategory_url() {
		return category_url;
	}

	public int getCurrent_page() {
		return current_page;
	}

	public int getTotal_page() {
		return total_page;
	}

	public int getStatus() {
		return status;
	}

	public String getNode() {
		return node;
	}

	public int getLevel() {
		return level;
	}

	public String getProject_code() {
		return project_code;
	}

	public String getDown_type() {
		return down_type;
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

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_id() {
		return product_id;
	}

	public String getBrand_id() {
		return brand_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}

	public String getCurrent_price() {
		return current_price;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public void setCategory_url(String category_url) {
		this.category_url = category_url;
	}

	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}

	public void setDown_type(String down_type) {
		this.down_type = down_type;
	}
}
