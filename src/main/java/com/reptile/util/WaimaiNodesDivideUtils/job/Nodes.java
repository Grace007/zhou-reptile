package com.reptile.util.WaimaiNodesDivideUtils.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;


@Table("waimai_nodes")
public class Nodes {
	
	
    @Id(auto = true)
    private long id;
    @Column
    private String node;
    @Column
    private String website;
    @Column
    private String description;
    @Column
    private long request_id;
    @Column
    private long task_id;
    @Column
    private String city_name;
    @Column
    private Date create_date;
    @Column
    private Date create_time;
    @Column
    private String remark;
    
    @Column
    private String description_simple;
    
  
	public String getDescription_simple() {
		return description_simple;
	}

	public void setDescription_simple(String description_simple) {
		this.description_simple = description_simple;
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

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
