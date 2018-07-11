package com.reptile.ec.jd.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("jd_product_simple")
public class JdProductSimple {
	@Id
	@ColDefine(type = ColType.INT, width = 20)
	private Long id;
	@Column
	@ColDefine(type = ColType.INT, width = 20)
	private Long requestId;
	@Column
	@ColDefine(type = ColType.INT, width = 20)
	private Long taskId;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String keyword;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String category;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String pid;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 256)
	private String pname;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 256)
	private String url;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String pirce;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String comments;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 20)
	private String createDate;
	@Column
	private Date createTime;
	
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String storeId;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 128)
	private String storeName;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 256)
	private String storeUrl;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStoreUrl(){
		return storeUrl;
	}
	
	public void setStoreUrl(String storeUrl){
		this.storeUrl = storeUrl;
	}

	public void set(Long requestId, Long taskId, 
			String keyword,String category, String storeId, String storeUrl, String storeName, String pid, String pname, String url, String pirce,
			String comments, String createDate, Date createTime) {
		this.requestId = requestId;
		this.taskId = taskId;
		this.keyword = keyword;
		this.category = category;
		this.storeId = storeId;
		this.storeUrl = storeUrl;
		this.storeName = storeName;
		this.pid = pid;
		this.pname = pname;
		this.url = url;
		this.pirce = pirce;
		this.comments = comments;
		this.createDate = createDate;
		this.createTime = createTime;
		
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPirce() {
		return pirce;
	}

	public void setPirce(String pirce) {
		this.pirce = pirce;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

}
