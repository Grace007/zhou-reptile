package com.reptile.ly.model.scenery;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 迪斯尼-同程景点用户评论类
 *
 */
@Table("ly_scenery_comment")
public class LySceneryCommentTagModel {

	@Id(auto = true)
	private Long id;
	/**
	 * 关键词 *
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
	 * 抓取日期 *
	 */
	@Column("create_date")
	private Date create_date;
	/**
	 * 抓取时间 *
	 */
	@Column("create_time")
	private Date create_time;

	/**
	 * 评论id *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("comment_id")
	private String comment_id;
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

	/************************* 评论的具体内容 ****************************/
	/**
	 * 会员id *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("nid")
	private String nid;
	/**
	 * 会员昵称 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("nick")
	private String nick;
	/**
	 * 会员等级 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("grade")
	private String grade;
	/**
	 * 评论内容 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("content")
	private String content;
	/**
	 * 评论图片地址 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("img_path")
	private String img_path;// *************************************
	/**
	 * 评论日期 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("comment_date")
	private String comment_date;
	/**
	 * 评论来源 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("comment_src")
	private String comment_src;
	/**
	 * 点赞数 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("agrees")
	private String agrees;
	/**
	 * 页码 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("page_index")
	private Integer page_index;
	/**
	 * 回复 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 4000)
	@Column("response")
	private String response;

	/**
	 * ********* 构造器 ************
	 */
	public LySceneryCommentTagModel() {
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

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getComment_date() {
		return comment_date;
	}

	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}

	public String getComment_src() {
		return comment_src;
	}

	public void setComment_src(String comment_src) {
		this.comment_src = comment_src;
	}

	public String getAgrees() {
		return agrees;
	}

	public void setAgrees(String agrees) {
		this.agrees = agrees;
	}

	public Integer getPage_index() {
		return page_index;
	}

	public void setPage_index(Integer page_index) {
		this.page_index = page_index;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
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

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
