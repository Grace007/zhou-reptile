package com.reptile.xiachufang.model.job;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

@Table("t_xiachufang_comment")
public class XiachufangCommentmodel {
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
     * 0初始待处理，1处理中，2处理成功，3处理失败
     */
    @ColDefine(type = ColType.INT, width = 5)
    @Column("status")
    private Integer status = 0;
    
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
    
    /** 菜谱id**/
    @ColDefine(type = ColType.VARCHAR , width = 100)
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
     * hot_comment页数*
     */
    @ColDefine(type = ColType.INT)
    @Column("hot_pages")
    private int hot_pages;
    
    /**
     * answer_comment页数*
     */
    @ColDefine(type = ColType.INT)
    @Column("answer_pages")
    private int answer_pages;
    
    /**
     * hot_counts*
     */
    @ColDefine(type = ColType.INT)
    @Column("hot_counts")
    private int hot_counts;
    
    /**
     * answer_counts*
     */
    @ColDefine(type = ColType.INT)
    @Column("answer_counts")
    private int answer_counts;
    
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getEntrance() {
		return entrance;
	}

	public void setEntrance(int entrance) {
		this.entrance = entrance;
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

	public int getHot_pages() {
		return hot_pages;
	}

	public void setHot_pages(int hot_pages) {
		this.hot_pages = hot_pages;
	}

	public int getAnswer_pages() {
		return answer_pages;
	}

	public void setAnswer_pages(int answer_pages) {
		this.answer_pages = answer_pages;
	}

	public int getHot_counts() {
		return hot_counts;
	}

	public void setHot_counts(int hot_counts) {
		this.hot_counts = hot_counts;
	}

	public int getAnswer_counts() {
		return answer_counts;
	}

	public void setAnswer_counts(int answer_counts) {
		this.answer_counts = answer_counts;
	}

	public String getTitle_id() {
		return title_id;
	}

	public void setTitle_id(String title_id) {
		this.title_id = title_id;
	}
	
	
	
}
