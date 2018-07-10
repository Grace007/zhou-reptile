package com.reptile.mobile_eleme.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("eleme_comment_detail_info${all}")
public class ElemeCommentDetailInfoModel {
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
	private String order_id;
	@Column
	private String product_name;
	@Column
	private String product_score;
	@Column
	private String product_comment;
	@Column
	private String shop_id;
	@Column
	private String food_id;
	@Column
	private String delivery_time;
	@Column
	private String shop_name;
	@Column
	private String city_name;
	@Column
	private String keyword;

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

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_score() {
		return product_score;
	}

	public void setProduct_score(String product_score) {
		this.product_score = product_score;
	}

	public String getProduct_comment() {
		return product_comment;
	}

	public void setProduct_comment(String product_comment) {
		this.product_comment = product_comment;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getFood_id() {
		return food_id;
	}

	public void setFood_id(String food_id) {
		this.food_id = food_id;
	}

	public String getDelivery_time() {
		return delivery_time;
	}

	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
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
}
