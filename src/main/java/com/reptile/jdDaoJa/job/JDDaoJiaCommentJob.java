package com.reptile.jdDaoJa.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("jddaojia_comment_url")
public class JDDaoJiaCommentJob {
	@Id
	private long id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private Date create_time;
	@Column
	private String city_name;
	@Column
	private String addressKey;
	@Column
	private String address;
	@Column
	private String store_name;
	@Column
	private String store_id;
	@Column
	private String page_url;
	@Column
	private String node;
	@Column
	private int status;
	@Column
	private String project_code;
	@Column
	private String down_type;
	@Column
	private String website;
	@Column
	private String category;
	@Column
	private int page;
	@Column
	private int total;
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
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getPage_url() {
		return page_url;
	}
	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
