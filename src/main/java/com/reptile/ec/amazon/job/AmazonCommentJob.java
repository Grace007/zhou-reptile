package com.reptile.ec.amazon.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;


@Table("amazon_comment_task_adhoc")
public class AmazonCommentJob {
	@Id(auto=true)
	@Column
	private long id;
	@Column
	private String project_code;
	@Column
	private long request_id;
	@Column
	private long task_id;

	@Column
	private String comment_url;
	@Column
	private String title;
	@Column
	private String asin;
	@Column
	private String category;

	@Column
	private int page_index;
	@Column
	private int status;

	@Column
	private String keyword;
	
	@Column
	private String node;

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

	public String getProject_code() {
		return project_code;
	}

	public void setProject_code(String project_code) {
		this.project_code = project_code;
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

	public String getComment_url() {
		return comment_url;
	}

	public void setComment_url(String comment_url) {
		this.comment_url = comment_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getPage_index() {
		return page_index;
	}

	public void setPage_index(int page_index) {
		this.page_index = page_index;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
