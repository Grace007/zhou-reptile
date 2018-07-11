package com.reptile.ec.jd.job;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;


@Table("jd_url_task_copy")
public class JdUrlJob {
	
	@Id(auto = true)
	private Long rid;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String url;
	/** 0,1,2,3 */
	@ColDefine(type = ColType.INT, width = 2)
	@Column
	private Integer status;

	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String keyword;
	
	@ColDefine(type = ColType.VARCHAR, width = 250)
	@Column
	private String project_code;
	@ColDefine(type = ColType.VARCHAR, width = 250)
	@Column
	private String website;
	@ColDefine(type = ColType.VARCHAR, width = 250)
	@Column
	private String down_type;
	
	@ColDefine(type = ColType.DATETIME, width = 20)
	@Column
	private Date createTime;
	
	@ColDefine(type = ColType.VARCHAR, width = 20)
	@Column
	private String createDate;
	@ColDefine(type = ColType.VARCHAR, width = 20)
	@Column
	private String node;
	@ColDefine(type = ColType.VARCHAR, width = 25)
	@Column
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

}
