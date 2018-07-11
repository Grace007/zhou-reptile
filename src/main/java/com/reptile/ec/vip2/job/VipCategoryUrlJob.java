package com.reptile.ec.vip2.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("vip_category_url_job")
public class VipCategoryUrlJob {
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
	private String type;
	@Column
	private int current_page;
	@Column
	private int total_page;
	@Column
	private int amount;
	@Column
	private int update_status;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public int getCurrent_page() {
		return current_page;
	}

	public int getTotal_page() {
		return total_page;
	}

	public int getUpdate_status() {
		return update_status;
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

	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setUpdate_status(int update_status) {
		this.update_status = update_status;
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
