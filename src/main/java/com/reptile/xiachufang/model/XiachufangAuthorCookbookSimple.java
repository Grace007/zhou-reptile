package com.reptile.xiachufang.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_xiachufang_cookbooksimple")
public class XiachufangAuthorCookbookSimple {

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
    
    /** author_id**/
    @ColDefine(type = ColType.VARCHAR, width = 100)
    @Column("author_id")
    private String author_id;    
    
    /** author**/
    @ColDefine(type = ColType.VARCHAR, width = 100)
    @Column("author")
    private String author;   

    
    /** 菜谱名**/
    @ColDefine(type = ColType.VARCHAR , width = 255)
    @Column("title")
    private String title;
    
    
    /** collect_num**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("collect_num")
    private String collect_num;
    
    /** 人气（有多少人做过）**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("support_number")
    private String support_number;
    
    
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

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCollect_num() {
		return collect_num;
	}

	public void setCollect_num(String collect_num) {
		this.collect_num = collect_num;
	}

	public String getSupport_number() {
		return support_number;
	}

	public void setSupport_number(String support_number) {
		this.support_number = support_number;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
	
}
