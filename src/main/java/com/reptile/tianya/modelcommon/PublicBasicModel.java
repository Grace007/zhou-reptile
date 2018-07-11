package com.reptile.tianya.modelcommon;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("bbs")
public class PublicBasicModel {
	@Id
	private Long id;
	
	@Column
	private String article_url;
	@Column
	private String engine;
	@Column
	private String forum;
	@Column
	private String title;
	@Column
	private String thread_id;
	@Column
	private String author_name;
	@Column
	private String author_url;
	@Column
	private Date date_posted;
	@Column
	private String post_id;
	@Column
	private String subtitle;
	@Column
	private String message;
	@Column
	private String message_id;
	@Column
	private String source_url;
	@Column
	private Date ingestdate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArticle_url() {
		return article_url;
	}

	public void setArticle_url(String article_url) {
		this.article_url = article_url;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getForum() {
		return forum;
	}

	public void setForum(String forum) {
		this.forum = forum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThread_id() {
		return thread_id;
	}

	public void setThread_id(String thread_id) {
		this.thread_id = thread_id;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	public String getAuthor_url() {
		return author_url;
	}

	public void setAuthor_url(String author_url) {
		this.author_url = author_url;
	}

	public Date getDate_posted() {
		return date_posted;
	}

	public void setDate_posted(Date date_posted) {
		this.date_posted = date_posted;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public String getSource_url() {
		return source_url;
	}

	public void setSource_url(String source_url) {
		this.source_url = source_url;
	}

	public Date getIngestdate() {
		return ingestdate;
	}

	public void setIngestdate(Date ingestdate) {
		this.ingestdate = ingestdate;
	}

}
