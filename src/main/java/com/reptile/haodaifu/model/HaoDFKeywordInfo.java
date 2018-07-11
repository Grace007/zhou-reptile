package com.reptile.haodaifu.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
@Table("haodf_keyword_info")
public class HaoDFKeywordInfo {
	@Id
	private long id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private String spokesman;
	@Column
	private String wenda_title;
	@Column
	private String wenda_time;
	@Column
	private String patient_status;
	@Column
	private String wenda_content;
	@Column
	private String wenda_type;
	@Column
	private int ranking;
	@Column
	private int pagenum;
	@Column
	private Date create_time;
	@Column
	private String wenda_url;
	@Column
	private String disease;
	@Column
	private String keyword;
	@Column
	private String did;
	@Column
	private String wenda_id;
	@Column
	private String node;
	@Column
	private String md5;
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
	public String getSpokesman() {
		return spokesman;
	}
	public void setSpokesman(String spokesman) {
		this.spokesman = spokesman;
	}
	public String getWenda_title() {
		return wenda_title;
	}
	public void setWenda_title(String wenda_title) {
		this.wenda_title = wenda_title;
	}
	public String getWenda_time() {
		return wenda_time;
	}
	public void setWenda_time(String wenda_time) {
		this.wenda_time = wenda_time;
	}
	public String getPatient_status() {
		return patient_status;
	}
	public void setPatient_status(String patient_status) {
		this.patient_status = patient_status;
	}
	public String getWenda_content() {
		return wenda_content;
	}
	public void setWenda_content(String wenda_content) {
		this.wenda_content = wenda_content;
	}
	public String getWenda_type() {
		return wenda_type;
	}
	public void setWenda_type(String wenda_type) {
		this.wenda_type = wenda_type;
	}
	
	public int getPagenum() {
		return pagenum;
	}
	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getWenda_url() {
		return wenda_url;
	}
	public void setWenda_url(String wenda_url) {
		this.wenda_url = wenda_url;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getWenda_id() {
		return wenda_id;
	}
	public void setWenda_id(String wenda_id) {
		this.wenda_id = wenda_id;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

}
