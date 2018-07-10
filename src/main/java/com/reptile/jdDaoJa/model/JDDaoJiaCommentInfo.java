package com.reptile.jdDaoJa.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("jddaojia_comment_info")
public class JDDaoJiaCommentInfo {
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
	private String store_star;
	@Column
	private String comment_num;
	
	@Column
	private String comment_tag;
	@Column
	private String comment_hascontent;
	@Column
	private String user_name;
	@Column
	private String user_star;
	@Column
	private String comment_content;
	@Column
	private String content_tag;
	@Column
	private String goods_tag;
	@Column
	private String comment_datetime;

	
	@Column
	private Date create_time;
	@Column
	private String project_code;
	@Column
	private String down_type;
	@Column
	private String website;
	@Column
	private String md5;
	
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
	public String getComment_tag() {
		return comment_tag;
	}
	public void setComment_tag(String comment_tag) {
		this.comment_tag = comment_tag;
	}
	public String getComment_hascontent() {
		return comment_hascontent;
	}
	public void setComment_hascontent(String comment_hascontent) {
		this.comment_hascontent = comment_hascontent;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_star() {
		return user_star;
	}
	public void setUser_star(String user_star) {
		this.user_star = user_star;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getContent_tag() {
		return content_tag;
	}
	public void setContent_tag(String content_tag) {
		this.content_tag = content_tag;
	}
	public String getGoods_tag() {
		return goods_tag;
	}
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}
	public String getComment_datetime() {
		return comment_datetime;
	}
	public void setComment_datetime(String comment_datetime) {
		this.comment_datetime = comment_datetime;
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
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	
}
