package com.reptile.xiachufang.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
@Table("t_xiachufang_commentsimple")
public class XiachufangCommentSimple {
	/**
     * id *
     */
    @Id(auto = true)
    private Long id;
    
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
     * info的入口，1为全网分类入口，2为关键字入口，3为作者入口 *
     * 在cookbookinfo表中
     * 1：category必须存在（三种情况都存在）
     * 2：keyword必须存在，1、3的时候不存在
     * 3：author有关信息必须存在（三种情况都存在）
     */
    @ColDefine(type = ColType.INT, width = 5)
    @Column("entrance")
    private int entrance;
    
    /**
     * 本comment的类型，作者回复类型，最热评论类型
     * 在commentsimple表中
     * 1：author_answer，answer_time，必须存在
     * 2：author_answer，answer_time为空
     */
    @ColDefine(type = ColType.VARCHAR, width = 50)
    @Column("type")
    private String  type;
    
    /** 提问用户名username**/
    @ColDefine(type = ColType.VARCHAR , width = 100)
    @Column("username")
    private String username;
    
    /** 提问时间question_time**/
    @ColDefine(type = ColType.DATE)
    @Column("question_time")
    private Date question_time;
    
    /** 评论comment**/
    @ColDefine(type = ColType.VARCHAR , width = 2000)
    @Column("comment")
    private String comment;
    
    /** 作者回复时间answer_time**/
    @ColDefine(type = ColType.DATE)
    @Column("answer_time")
    private Date answer_time;
    
    /** 点赞数： support_number**/
    @ColDefine(type = ColType.VARCHAR , width = 2000)
    @Column("support_number")
    private String support_number;
    
    /** 作者回答 author_answer**/
    @ColDefine(type = ColType.VARCHAR , width = 2000)
    @Column("author_answer")
    private String author_answer;
    
    /** 菜谱title_id**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("title_id")
    private String title_id;
    
    /** 菜谱url**/
    @ColDefine(type = ColType.VARCHAR , width = 255)
    @Column("title_url")
    private String title_url;
    
    /**
     * 创建日期 *
     */
    @ColDefine(type = ColType.DATE)
    @Column
    private Date create_date;
    /**
     * 创建日期时间 *
     */
    @ColDefine(type = ColType.DATETIME)
    @Column
    private Date create_time;
    
    /** 项目代码 **/
    @ColDefine(type = ColType.VARCHAR, width = 64)
    @Column("project_code")
    private String project_code;
    
    /** 下载类型 **/
    @ColDefine(type = ColType.VARCHAR, width = 64)
    @Column("down_type")
    private String down_type;
    
    /** node**/
    @ColDefine(type = ColType.VARCHAR, width = 100)
    @Column
    private String node;
    
    /** website**/
    @ColDefine(type = ColType.VARCHAR, width = 100)
    @Column
    private String website;
    
    @Column
    private String md5;

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

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}

	public int getEntrance() {
		return entrance;
	}

	public void setEntrance(int entrance) {
		this.entrance = entrance;
	}

	public String  getType() {
		return type;
	}

	public void setType(String  type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getQuestion_time() {
		return question_time;
	}

	public void setQuestion_time(Date question_time) {
		this.question_time = question_time;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getAnswer_time() {
		return answer_time;
	}

	public void setAnswer_time(Date answer_time) {
		this.answer_time = answer_time;
	}

	public String getSupport_number() {
		return support_number;
	}

	public void setSupport_number(String support_number) {
		this.support_number = support_number;
	}

	public String getAuthor_answer() {
		return author_answer;
	}

	public void setAuthor_answer(String author_answer) {
		this.author_answer = author_answer;
	}

	public String getTitle_url() {
		return title_url;
	}

	public void setTitle_url(String title_url) {
		this.title_url = title_url;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
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

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getTitle_id() {
		return title_id;
	}

	public void setTitle_id(String title_id) {
		this.title_id = title_id;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
}
