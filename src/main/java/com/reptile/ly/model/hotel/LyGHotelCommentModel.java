package com.reptile.ly.model.hotel;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 迪斯尼-同程酒店每个用户评论类
 *
 */
@Table("ly_ghotel_comment")
public class LyGHotelCommentModel {

	@Id(auto = true)
	private Long id;

	/**
	 * 评论id *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 32)
	@Column("comment_id")
	private String comment_id;

	/**
	 * 唯一身份名字 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String pname;
	/**
	 * 商品id
	 */
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 64)
	private String pid;

	/**
	 * url地址 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 512)
	@Column("url")
	private String url;

	/************************* 评论的具体内容 ****************************/

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

	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("travel_type")
	private String travel_type;
	/**
	 * 评论内容 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("content")
	private String content;
	/**
	 * 评论图片地址 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 1000)
	@Column("img_path")
	private String img_path;// *************************************
	/**
	 * 评论日期 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 32)
	@Column("comment_date")
	private String comment_date;
	/**
	 * 评论来源 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 32)
	@Column("comment_src")
	private String comment_src;
	/**
	 * 点赞数 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("feel")
	private String feel;

	/**
	 * 回复 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 4000)
	@Column("response")
	private String response;
	@Column("create_date")
	private Date create_date;
	/**
	 * 抓取时间 *
	 */
	@Column("create_time")
	private Date create_time;

	/**
	 * ********* 构造器 ************
	 */
	public LyGHotelCommentModel() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getFeel() {
		return feel;
	}

	public void setFeel(String feel) {
		this.feel = feel;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
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

	public String getTravel_type() {
		return travel_type;
	}

	public void setTravel_type(String travel_type) {
		this.travel_type = travel_type;
	}

}
