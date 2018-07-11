package com.reptile.jdfresh.common;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;

import java.util.Date;

public class BaseJob {
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
    @ColDefine(type = ColType.INT, width = 20)
    @Column("status")
    private int status = 0;
    /**
     * 创建时间 *
     */
    @ColDefine(type = ColType.DATETIME)
    @Column("create_time")
    private Date create_time;
	@Column
    private String node;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 250)
    private String website;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 250)
    private String down_type;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 250)
    private String project_code;

    public String getDown_type() {
        return down_type;
    }

    public void setDown_type(String down_type) {
        this.down_type = down_type;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
    
    public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}
	

}
