package com.reptile.util.WaimaiNodesDivideUtils.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("waimai_node_snatch_detail")
public class QueryNodeStatusRS {
	
    @Id(auto = true)
    private long id;
    @Column
    private long request_id;
    @Column
    private long task_id;
    @Column
    private String node;
    @Column
    private String keyword_status;
    @Column
    private String waimailist_status;
    @Column
    private String shopkeyword_status;
    @Column
    private String url_status;
    @Column
    private String product_status;
    @Column
    private String website;
    @Column
    private String description;
    @Column
    private String city_name;
    @Column
    private String remark;
    @Column
    private Date create_date;
    @Column
    private Date create_time;
    @Column
    private Date update_date;
    @Column
    private Date update_time;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getRequest_id() {
		return request_id;
	}
	public void setRequest_id(long request_id) {
		this.request_id = request_id;
	}
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getKeyword_status() {
		return keyword_status;
	}
	public void setKeyword_status(String keyword_status) {
		this.keyword_status = keyword_status;
	}
	public String getWaimailist_status() {
		return waimailist_status;
	}
	public void setWaimailist_status(String waimailist_status) {
		this.waimailist_status = waimailist_status;
	}
	public String getShopkeyword_status() {
		return shopkeyword_status;
	}
	public void setShopkeyword_status(String shopkeyword_status) {
		this.shopkeyword_status = shopkeyword_status;
	}
	public String getUrl_status() {
		return url_status;
	}
	public void setUrl_status(String url_status) {
		this.url_status = url_status;
	}
	public String getProduct_status() {
		return product_status;
	}
	public void setProduct_status(String product_status) {
		this.product_status = product_status;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
    
    
}
