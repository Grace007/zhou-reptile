package com.reptile.xiachufang.model;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

@Table("t_xiachufang_authorinfo")
public class XiachufangAuthorInfo {

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
    
    /** author_id **/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("author_id")
    private String author_id;
    
    /** author_name**/
    @ColDefine(type = ColType.VARCHAR, width = 100)
    @Column("author_name")
    private String author_name;    
    
    /** author_url **/
    @ColDefine(type = ColType.VARCHAR , width = 255)
    @Column("author_url")
    private String author_url;

    /** sex**/
    @ColDefine(type = ColType.VARCHAR , width = 10)
    @Column("sex")
    private String sex;
    
    /** join_date**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("join_date")
    private String join_date;
    
    /** following_num**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("following_num")
    private String following_num;
    
    /** followed_num**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("followed_num")
    private String followed_num;
    
    /** cookbook_num**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("cookbook_num")
    private String cookbook_num;
    
    /** production_num**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("production_num")
    private String production_num;
  
    /**menu_num**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("menu_num")
    private String menu_num;
    
    /** collect_num **/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("collect_num")
    private String collect_num;
    
    /** message_num **/
    @ColDefine(type = ColType.VARCHAR, width = 64)
    @Column("message_num")
    private String message_num;
    
    /** creat_date **/
    @ColDefine(type = ColType.DATE)
    @Column("creat_date")
    private Date creat_date;
    
    /**
     * 创建日期时间 *
     */
    @ColDefine(type = ColType.DATETIME)
    @Column
    private Date create_time;
    
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getJoin_date() {
		return join_date;
	}

	public void setJoin_date(String join_date) {
		this.join_date = join_date;
	}

	public String getFollowing_num() {
		return following_num;
	}

	public void setFollowing_num(String following_num) {
		this.following_num = following_num;
	}

	public String getFollowed_num() {
		return followed_num;
	}

	public void setFollowed_num(String followed_num) {
		this.followed_num = followed_num;
	}

	public String getCookbook_num() {
		return cookbook_num;
	}

	public void setCookbook_num(String cookbook_num) {
		this.cookbook_num = cookbook_num;
	}

	public String getProduction_num() {
		return production_num;
	}

	public void setProduction_num(String production_num) {
		this.production_num = production_num;
	}

	public String getMenu_num() {
		return menu_num;
	}

	public void setMenu_num(String menu_num) {
		this.menu_num = menu_num;
	}

	public String getCollect_num() {
		return collect_num;
	}

	public void setCollect_num(String collect_num) {
		this.collect_num = collect_num;
	}

	public String getMessage_num() {
		return message_num;
	}

	public void setMessage_num(String message_num) {
		this.message_num = message_num;
	}

	public Date getCreat_date() {
		return creat_date;
	}

	public void setCreat_date(Date creat_date) {
		this.creat_date = creat_date;
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

}
