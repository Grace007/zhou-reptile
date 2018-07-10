package com.reptile.util.WaimaiNodesDivideUtils.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;


@Table("waimai_task_list")
public class Waimai_task_list {
    @Id(auto = true)
    private long id;
    @Column
    private String website;
    @Column
    private String description;
    @Column
    private int divide_keyword_or_not;
    @Column
    private int divide_url_or_not;
    @Column
    private int divide_product_or_not;
    @Column
    private int divide_comment_or_not;
    @Column
    private int divide_node_or_not;
    @Column
    private int divide_task_or_not;
    @Column
    private String task_to_task;
    @Column
    private long request_id;
    @Column
    private long task_id;
    @Column
    private String nodes;
    @Column
    private int keyword_remaining;
    @Column
    private int url_remaining;
    @Column
    private int product_remaining;
    @Column
    private int comment_remaining;
    @Column
    private String keyword_status;
    @Column
    private String url_status;
    @Column
    private String product_status;
    @Column
    private String comment_status;
    @Column
    private Date update_date;
    @Column
    private Date update_date_time;
    @Column
    private String description_simple;
    
  
	public String getDescription_simple() {
		return description_simple;
	}

	public void setDescription_simple(String description_simple) {
		this.description_simple = description_simple;
	}

	public int getDivide_node_or_not() {
		return divide_node_or_not;
	}

	public void setDivide_node_or_not(int divide_node_or_not) {
		this.divide_node_or_not = divide_node_or_not;
	}

	public int getDivide_task_or_not() {
		return divide_task_or_not;
	}

	public void setDivide_task_or_not(int divide_task_or_not) {
		this.divide_task_or_not = divide_task_or_not;
	}

	public String getTask_to_task() {
		return task_to_task;
	}

	public void setTask_to_task(String task_to_task) {
		this.task_to_task = task_to_task;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Date getUpdate_date_time() {
		return update_date_time;
	}

	public void setUpdate_date_time(Date update_date_time) {
		this.update_date_time = update_date_time;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getDivide_keyword_or_not() {
        return divide_keyword_or_not;
    }

    public void setDivide_keyword_or_not(int divide_keyword_or_not) {
        this.divide_keyword_or_not = divide_keyword_or_not;
    }

    public int getDivide_url_or_not() {
        return divide_url_or_not;
    }

    public void setDivide_url_or_not(int divide_url_or_not) {
        this.divide_url_or_not = divide_url_or_not;
    }

    public int getDivide_product_or_not() {
        return divide_product_or_not;
    }

    public void setDivide_product_or_not(int divide_product_or_not) {
        this.divide_product_or_not = divide_product_or_not;
    }

    public int getDivide_comment_or_not() {
        return divide_comment_or_not;
    }

    public void setDivide_comment_or_not(int divide_comment_or_not) {
        this.divide_comment_or_not = divide_comment_or_not;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public int getKeyword_remaining() {
        return keyword_remaining;
    }

    public void setKeyword_remaining(int keyword_remaining) {
        this.keyword_remaining = keyword_remaining;
    }

    public int getUrl_remaining() {
        return url_remaining;
    }

    public void setUrl_remaining(int url_remaining) {
        this.url_remaining = url_remaining;
    }

    public int getProduct_remaining() {
        return product_remaining;
    }

    public void setProduct_remaining(int product_remaining) {
        this.product_remaining = product_remaining;
    }

    public int getComment_remaining() {
        return comment_remaining;
    }

    public void setComment_remaining(int comment_remaining) {
        this.comment_remaining = comment_remaining;
    }

    public String getKeyword_status() {
        return keyword_status;
    }

    public void setKeyword_status(String keyword_status) {
        this.keyword_status = keyword_status;
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

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }
}
