package com.reptile.haodaifu.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("haodf_article_simple_job")
public class HaoDFArticleSimpleJob {
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
	private Date create_time;
	@Column
	private String node;
	@Column
	private int status;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	

}
