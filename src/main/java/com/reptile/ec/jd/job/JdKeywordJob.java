package com.reptile.ec.jd.job;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("jd_keyword_task")
public class JdKeywordJob {
	
	@Id(auto = true)
	private Long id;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("key_url")
	private String keyUrl;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String keyword;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String category;
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private Long requestId;
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private Long taskId;
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private Integer pageTotal;
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private Integer count;
	/** 0初始待处理，1处理中，2处理成功，3处理失败 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private int status = 0;
	@ColDefine(type = ColType.VARCHAR, width = 25)
	@Column
	private String type;
	@ColDefine(type = ColType.VARCHAR, width = 250)
	@Column
	private String project_code;
	@ColDefine(type = ColType.VARCHAR, width = 250)
	@Column
	private String website;
	@ColDefine(type = ColType.VARCHAR, width = 250)
	@Column
	private String down_type;
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column
	private String createDate;
	@Column
	private Date createTime;
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column
	private String node;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public Integer getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(Integer pageTotal) {
		this.pageTotal = pageTotal;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getProject_code() {
		return project_code;
	}

	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDown_type() {
		return down_type;
	}

	public void setDown_type(String down_type) {
		this.down_type = down_type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	public String getKeyUrl() {
		return keyUrl;
	}

	public void setKeyUrl(String keyUrl) {
		this.keyUrl = keyUrl;
	}
	
	
	
}
