package com.reptile.ec.amazon.job;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;


@Table("amazon_product_task_adhoc")
public class AmazonProductJob {
	@Id(auto=true)
	@Column
	private long id;

	@Column
	private long request_id;
	@Column
	private long task_id;

	@Column
	private String page_url;

	@Column
	private String project_code;
	@Column
	private String title;
	@Column
	private String category;
	@Column
	private int comment_total;
	@Column
	private int status;
	@Column
	private int comment_status;
	@Column
	private String keyword;
	
	@Column
	private String node;
	
	@Column
	private String asin;

	@Column
	private Date create_date;
	
	@Column
	private String down_type;

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

	public String getPage_url() {
		return page_url;
	}

	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}

	public String getProject_code() {
		return project_code;
	}

	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getComment_total() {
		return comment_total;
	}

	public void setComment_total(int comment_total) {
		this.comment_total = comment_total;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getComment_status() {
		return comment_status;
	}

	public void setComment_status(int comment_status) {
		this.comment_status = comment_status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getDown_type() {
		return down_type;
	}

	public void setDown_type(String down_type) {
		this.down_type = down_type;
	}
}
