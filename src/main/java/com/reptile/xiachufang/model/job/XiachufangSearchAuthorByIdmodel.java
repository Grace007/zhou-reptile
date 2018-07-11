package com.reptile.xiachufang.model.job;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_xiachufang_searchauthor")
public class XiachufangSearchAuthorByIdmodel {
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
    
    /** author_id**/
    @ColDefine(type = ColType.VARCHAR, width = 100)
    @Column("author_id")
    private String author_id;    
    
    /** author_name**/
    @ColDefine(type = ColType.VARCHAR, width = 100)
    @Column("author_name")
    private String author_name;    
    
    /** author_url**/
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Column("author_url")
    private String author_url;    
    
    /**
     * 0初始待处理，1处理中，2处理成功，3处理失败
     */
    @ColDefine(type = ColType.INT, width = 5)
    @Column("status")
    private Integer status = 0;
    
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
    private Date create_datetime;
    
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getCreate_datetime() {
		return create_datetime;
	}

	public void setCreate_datetime(Date create_datetime) {
		this.create_datetime = create_datetime;
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

	public int getEntrance() {
		return entrance;
	}

	public void setEntrance(int entrance) {
		this.entrance = entrance;
	}
}
