package com.reptile.haodaifu.job;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("haodf_category_job")
public class HaoDFCategoryJob {
	@Id
	private long id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private String big_categoryname;
	@Column
	private String median_categoryname;
	@Column
	private String small_categoryname;
	@Column
	private String tiny_categoryname;
	@Column
	private String tiny_categoryurl;
	@Column
	private Date create_time;
	@Column
	private String node;
	@Column
	private int doctor_status;
	@Column
	private int article_status;
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
	public String getBig_categoryname() {
		return big_categoryname;
	}
	public void setBig_categoryname(String big_categoryname) {
		this.big_categoryname = big_categoryname;
	}
	public String getMedian_categoryname() {
		return median_categoryname;
	}
	public void setMedian_categoryname(String median_categoryname) {
		this.median_categoryname = median_categoryname;
	}
	public String getSmall_categoryname() {
		return small_categoryname;
	}
	public void setSmall_categoryname(String small_categoryname) {
		this.small_categoryname = small_categoryname;
	}

	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public int getDoctor_status() {
		return doctor_status;
	}
	public void setDoctor_status(int doctor_status) {
		this.doctor_status = doctor_status;
	}
	public int getArticle_status() {
		return article_status;
	}
	public void setArticle_status(int article_status) {
		this.article_status = article_status;
	}
	public String getTiny_categoryname() {
		return tiny_categoryname;
	}
	public void setTiny_categoryname(String tiny_categoryname) {
		this.tiny_categoryname = tiny_categoryname;
	}
	public String getTiny_categoryurl() {
		return tiny_categoryurl;
	}
	public void setTiny_categoryurl(String tiny_categoryurl) {
		this.tiny_categoryurl = tiny_categoryurl;
	}
	
	


}
