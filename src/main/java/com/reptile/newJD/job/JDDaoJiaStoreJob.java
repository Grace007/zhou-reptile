package com.reptile.newJD.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

// 店铺表
@Table("new2_jddaojia_store_url")
public class JDDaoJiaStoreJob {
	@Id
	private long id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private String store_id;
	@Column
	private String store_name;
	@Column
	private String store_url;
	@Column
	private Date create_time;
	@Column
	private String city_name;
	@Column
	private String addressKey;
	@Column
	private String address;
	@Column
	private String title;
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
	private String lat;
	@Column
	private String lng;
	@Column
	private String category;
	
	@Column
	private String freight_start;
	@Column
	private String url;
	@Column
	private String city_code;
	@Column
	private String adcode;
	
	
	
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getAdcode() {
		return adcode;
	}
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}
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
	public String getStore_url() {
		return store_url;
	}
	public void setStore_url(String store_url) {
		this.store_url = store_url;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFreight_start() {
		return freight_start;
	}
	public void setFreight_start(String freight_start) {
		this.freight_start = freight_start;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
