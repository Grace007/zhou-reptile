package com.reptile.ly.model.hotel.job;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 迪斯尼-同程票务用户评论的job类
 *
 */
@Table("t_ly_hotel_comment_job")
public class LyHotelCommentJob {

	
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
	@Column("level")
	private String level;
	@Column("node")
	private String node;
	/**
	 * 唯一身份编号 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String sid;
	/**
	 * 唯一身份名字 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String sname;
	/**
	 * 商品id
	 */
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 256)
	private String pid;
	/**
	 * 商品名称
	 */
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 256)
	private String pname;
	/**
	 * url地址 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("url")
	private String url;
	/**
	 * 产品url地址 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("web_url")
	private String web_url;
	/**
	 * 当前评论的页码 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private Integer page_index;
	/**
	 * 评论总页码数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("page")
	private Integer pages;
	/**
	 * 总评论数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("comments")
	private Integer comments;
	/****/
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("page_name")
	private String page_name;

	/**
	 * ********* 构造器 ************
	 */
	public LyHotelCommentJob() {
		super();
	}

	/**
	 * ****** 设定器和访问器 *********
	 */
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
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

	public Integer getPage_index() {
		return page_index;
	}

	public void setPage_index(Integer page_index) {
		this.page_index = page_index;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public String getPage_name() {
		return page_name;
	}

	public void setPage_name(String page_name) {
		this.page_name = page_name;
	}

	public String getWeb_url() {
		return web_url;
	}

	public void setWeb_url(String web_url) {
		this.web_url = web_url;
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

}
