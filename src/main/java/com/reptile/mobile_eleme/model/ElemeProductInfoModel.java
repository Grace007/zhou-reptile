package com.reptile.mobile_eleme.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("eleme_product_info${all}")
public class ElemeProductInfoModel {
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
	private String type;
	@Column
	private String type_description;
	@Column
	private String product_name;
	@Column
	private String product_description;
	@Column
	private String score;
	@Column
	private String monthly_sales;
	@Column
	private String price;
	@Column
	private String original_price;
	@Column
	private String satisfy_rate;
	@Column
	private String activity_limitation;
	@Column
	private String food_id;
	@Column
	private String shop_name;
	@Column
	private String city_name;
	@Column
	private String keyword;
	@Column
	private long Specification_Amount;
	@Column
	private String unique_id;
	@Column
	private String specification;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getMonthly_sales() {
		return monthly_sales;
	}

	public void setMonthly_sales(String monthly_sales) {
		this.monthly_sales = monthly_sales;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFood_id() {
		return food_id;
	}

	public void setFood_id(String food_id) {
		this.food_id = food_id;
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

	public long getSpecification_Amount() {
		return Specification_Amount;
	}

	public void setSpecification_Amount(long specification_Amount) {
		Specification_Amount = specification_Amount;
	}

	public String getType_description() {
		return type_description;
	}

	public String getProduct_description() {
		return product_description;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public String getSatisfy_rate() {
		return satisfy_rate;
	}

	public String getActivity_limitation() {
		return activity_limitation;
	}

	public void setType_description(String type_description) {
		this.type_description = type_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public void setSatisfy_rate(String satisfy_rate) {
		this.satisfy_rate = satisfy_rate;
	}

	public void setActivity_limitation(String activity_limitation) {
		this.activity_limitation = activity_limitation;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

}
