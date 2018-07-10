package com.reptile.mobile_baidu_waimai.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("baidu_waimai_comment_info${all}")
public class BaiduWaimaiCommentInfoModel {
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
	private String comment_id;
	@Column
	private String user_name;
	@Column
	private String score;
	@Column
	private String delivery_time;
	@Column
	private String comment_time;
	@Column
	private String recommended_product;
	@Column
	private String comment_content;
	@Column
	private String shop_name;
	@Column
	private String city_name;
	@Column
	private String keyword;
	@Column
	private String comment_labels;
	@Column
	private String ReplyContent;
	@Column
	private String ReplyTime;

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

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getDelivery_time() {
		return delivery_time;
	}

	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

	public String getRecommended_product() {
		return recommended_product;
	}

	public void setRecommended_product(String recommended_product) {
		this.recommended_product = recommended_product;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
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

	public String getComment_labels() {
		return comment_labels;
	}

	public void setComment_labels(String comment_labels) {
		this.comment_labels = comment_labels;
	}

	public String getReplyContent() {
		return ReplyContent;
	}

	public String getReplyTime() {
		return ReplyTime;
	}

	public void setReplyContent(String replyContent) {
		ReplyContent = replyContent;
	}

	public void setReplyTime(String replyTime) {
		ReplyTime = replyTime;
	}
}
