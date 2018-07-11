package com.reptile.ly.model.hotel.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * 迪斯尼-同程酒店产品Keyword的job类
 *
 */
@Table("t_ly_ghotel_url_job")
public class LyGHotelUrlJob {

	@Id
	private Long id;
	@Column
	private Long request_id;
	@Column
	private Integer status;

	@Column
	private String url;

	@Column
	private Integer comment_index;
	
	@Column
	private Date create_date;
	@Column
	private String node;
	@Column
	private String project_code;
	@Column
	private String down_type;
	@Column
	private Integer info_status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRequest_id() {
		return request_id;
	}
	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getComment_index() {
		return comment_index;
	}
	public void setComment_index(Integer comment_index) {
		this.comment_index = comment_index;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
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
	public Integer getInfo_status() {
		return info_status;
	}
	public void setInfo_status(Integer info_status) {
		this.info_status = info_status;
	}
	
	

}
