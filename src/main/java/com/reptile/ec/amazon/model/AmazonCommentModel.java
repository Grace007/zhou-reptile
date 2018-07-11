package com.reptile.ec.amazon.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;


@Table("amazon_comment")
public class AmazonCommentModel {
	@Column
	private String page_id;
	@Column
	private long request_id;
	@Column
	private String product_name;
	@Column
	private String url;
	@Name
	private String comment_id;
	//商品评论
	//private String comment_conent;
	//评论者
	@Column
	private String comment_user;
	//点评时间
	@Column
	private Date comment_time;
	//推荐与否
	@Column
	private String comment_star;
	//评论主题
	@Column
	private String comment_topic;
	//具体评价内容
	@Column
	private String comment_content;
	//评论是否有用
	@Column
	private String comment_useful;
	@Column
	private Date create_date;
	public String getPage_id() {
		return page_id;
	}
	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}
	public long getRequest_id() {
		return request_id;
	}
	public void setRequest_id(long request_id) {
		this.request_id = request_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	public String getComment_user() {
		return comment_user;
	}
	public void setComment_user(String comment_user) {
		this.comment_user = comment_user;
	}
	public Date getComment_time() {
		return comment_time;
	}
	public void setComment_time(Date comment_time) {
		this.comment_time = comment_time;
	}
	public String getComment_star() {
		return comment_star;
	}
	public void setComment_star(String comment_star) {
		this.comment_star = comment_star;
	}
	public String getComment_topic() {
		return comment_topic;
	}
	public void setComment_topic(String comment_topic) {
		this.comment_topic = comment_topic;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getComment_useful() {
		return comment_useful;
	}
	public void setComment_useful(String comment_useful) {
		this.comment_useful = comment_useful;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
}
