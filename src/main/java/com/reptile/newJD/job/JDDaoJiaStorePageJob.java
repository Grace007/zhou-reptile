package com.reptile.newJD.job;

import com.bds.base.dao.MySqlDataSource;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

// 店铺分页表
@Table("new2_jddaojia_store_page")
public class JDDaoJiaStorePageJob {
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
	private String title;
	@Column
	private String url;
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
	private String city_code;

	@Column
	private String adcode;

	@Column
	private String body;

	@Column
	private String page;
	
	@Column
	private String md5;
	
	@Column
	private String category;


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getAdcode() {
		return adcode;
	}

	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public static void main(String[] args) {
		Dao mysqlDao = MySqlDataSource.getMysqlDao("hy_o2o");
		mysqlDao.create(JDDaoJiaStorePageJob.class, false);
	}
}
