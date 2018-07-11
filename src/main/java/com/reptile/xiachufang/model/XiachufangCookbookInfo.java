package com.reptile.xiachufang.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_xiachufang_cookbbookinfo")
public class XiachufangCookbookInfo {
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
     * 1：category必须存在（三种情况都存在）
     * 2：keyword必须存在，1、3的时候不存在
     * 3：author有关信息必须存在（三种情况都存在）
     */
    @ColDefine(type = ColType.INT, width = 5)
    @Column("entrance")
    private int entrance;
    
    /** keyword**/
    @ColDefine(type = ColType.VARCHAR, width = 100)
    @Column("keyword")
    private String keyword;    
    
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
    
    /** title_url **/
    @ColDefine(type = ColType.VARCHAR , width = 255)
    @Column("title_url")
    private String title_url;

    /** cookbook**/
    @ColDefine(type = ColType.VARCHAR , width = 100)
    @Column("cookbook")
    private String cookbook;
    
    /** category**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("category")
    private String category;
    
    /** 人气（有多少人做过）**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("support_number")
    private String support_number;
    
    /** score**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("score")
    private String score;
    
    /** cookbook_createdate**/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("cookbook_createdate")
    private String cookbook_createdate;
    
    
    /** collect_num **/
    @ColDefine(type = ColType.VARCHAR , width = 50)
    @Column("collect_num")
    private String collect_num;
    
    /** material **/
    @ColDefine(type = ColType.VARCHAR, width = 1000)
    @Column("material")
    private String material;
    
    /** content **/
    @ColDefine(type = ColType.VARCHAR, width = 5000)
    @Column("content")
    private String content;
    
    /** img_url **/
    @ColDefine(type = ColType.VARCHAR, width = 5000)
    @Column("img_url")
    private String img_url;
    
    /** create_date **/
    @ColDefine(type = ColType.DATE)
    @Column("create_date")
    private Date create_date;
    
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

	public int getEntrance() {
		return entrance;
	}

	public void setEntrance(int entrance) {
		this.entrance = entrance;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getCookbook() {
		return cookbook;
	}

	public void setCookbook(String cookbook) {
		this.cookbook = cookbook;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSupport_number() {
		return support_number;
	}

	public void setSupport_number(String support_number) {
		this.support_number = support_number;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getCookbook_createdate() {
		return cookbook_createdate;
	}

	public void setCookbook_createdate(String cookbook_createdate) {
		this.cookbook_createdate = cookbook_createdate;
	}

	public String getCollect_num() {
		return collect_num;
	}

	public void setCollect_num(String collect_num) {
		this.collect_num = collect_num;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
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

	public String getTitle_url() {
		return title_url;
	}

	public void setTitle_url(String title_url) {
		this.title_url = title_url;
	}
    
    
}
