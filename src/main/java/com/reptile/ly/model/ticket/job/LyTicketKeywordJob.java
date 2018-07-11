package com.reptile.ly.model.ticket.job;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 迪斯尼-同程海外玩乐票务产品Keyword的job类
 *
 */
@Table("t_ly_ticket_keyword_job")
public class LyTicketKeywordJob {

	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column
	private String node;
	/****/
	@ColDefine(type = ColType.INT, width = 2)
	@Column("level")
	private int level;

	@Id(auto = true)
	private Long id;
	/**
	 * 关键字 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("keyword")
	private String keyword;
	/**
	 * 需求id *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("request_id")
	private Long request_id;
	/**
	 * 任务id *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("task_id")
	private Long task_id;
	/**
	 * 0初始待处理，1处理中，2处理成功，3处理失败
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("status")
	private int status = 0;
	/**
	 * 创建日期 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("create_date")
	private String create_date;
	/**
	 * 创建时间 *
	 */
	@Column("create_time")
	private Date create_time;
	@ColDefine(type = ColType.VARCHAR, width = 64)
	/** 项目代码 **/
	@Column("project_code")
	private String project_code;
	@ColDefine(type = ColType.VARCHAR, width = 64)
	/** 下载类型 **/
	@Column("down_type")
	private String down_type;

	/** url地址 **/
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("url")
	private String url;
	/** 评论任务建立状态:0初始待处理，1处理中，2处理成功，3处理失败 **/
	@ColDefine(type = ColType.INT, width = 20)
	@Column("comment_status")
	private Integer comment_status = 0;
	/** 评论任务建立状态:0初始待处理，1处理中，2处理成功，3处理失败 **/
	@ColDefine(type = ColType.INT, width = 20)
	@Column("ticket_status")
	private Integer ticket_status = 0;

	/*********** 构造器 *************/
	public LyTicketKeywordJob() {
		super();
	}

	/**
	 * ****** 设定器和访问器 *********
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getComment_status() {
		return comment_status;
	}

	public void setComment_status(Integer comment_status) {
		this.comment_status = comment_status;
	}

	public Integer getTicket_status() {
		return ticket_status;
	}

	public void setTicket_status(Integer ticket_status) {
		this.ticket_status = ticket_status;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

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

	public Long getRequest_id() {
		return request_id;
	}

	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
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

}
