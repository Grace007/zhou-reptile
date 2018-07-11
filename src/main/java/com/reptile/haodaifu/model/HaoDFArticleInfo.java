package com.reptile.haodaifu.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
@Table("haodf_article_info")
public class HaoDFArticleInfo {
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
	private String article_title;
	@Column
	private String article_time;
	@Column
	private String dname;
	@Column
	private String did;
	@Column
	private String article_id;
	@Column
	private String dposition;
	@Column
	private String dhospital;
	@Column
	private String ddepartment;
	@Column
	private String article_commentnum;
	@Column
	private String article_url;
	@Column
	private String article_type;
	@Column
	private String article_readnum;
	@Column
	private String username;
	@Column
	private String article_category;
	@Column
	private String publisher;
	@Column
	private String article_content;
	@Column
	private String article_img;
	@Column
	private String article_duration;
	
	
	
	
	@Column
	private Date create_time;
	@Column
	private String node;
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
	public String getTiny_categoryname() {
		return tiny_categoryname;
	}
	public void setTiny_categoryname(String tiny_categoryname) {
		this.tiny_categoryname = tiny_categoryname;
	}
	public String getArticle_title() {
		return article_title;
	}
	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}
	public String getArticle_time() {
		return article_time;
	}
	public void setArticle_time(String article_time) {
		this.article_time = article_time;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getArticle_id() {
		return article_id;
	}
	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}
	public String getDposition() {
		return dposition;
	}
	public void setDposition(String dposition) {
		this.dposition = dposition;
	}
	public String getDhospital() {
		return dhospital;
	}
	public void setDhospital(String dhospital) {
		this.dhospital = dhospital;
	}
	public String getDdepartment() {
		return ddepartment;
	}
	public void setDdepartment(String ddepartment) {
		this.ddepartment = ddepartment;
	}
	public String getArticle_commentnum() {
		return article_commentnum;
	}
	public void setArticle_commentnum(String article_commentnum) {
		this.article_commentnum = article_commentnum;
	}
	public String getArticle_url() {
		return article_url;
	}
	public void setArticle_url(String article_url) {
		this.article_url = article_url;
	}
	public String getArticle_type() {
		return article_type;
	}
	public void setArticle_type(String article_type) {
		this.article_type = article_type;
	}
	public String getArticle_readnum() {
		return article_readnum;
	}
	public void setArticle_readnum(String article_readnum) {
		this.article_readnum = article_readnum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getArticle_category() {
		return article_category;
	}
	public void setArticle_category(String article_category) {
		this.article_category = article_category;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getArticle_content() {
		return article_content;
	}
	public void setArticle_content(String article_content) {
		this.article_content = article_content;
	}
	public String getArticle_img() {
		return article_img;
	}
	public void setArticle_img(String article_img) {
		this.article_img = article_img;
	}
	
	public String getArticle_duration() {
		return article_duration;
	}
	public void setArticle_duration(String article_duration) {
		this.article_duration = article_duration;
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

}
