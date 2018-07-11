package com.reptile.jdfresh.common;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;

import java.util.Date;

public class BaseBI {
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
	 * 创建时间 *
	 */
	@Column("create_time")
	@ColDefine(type = ColType.DATETIME)
	private Date create_time;
	/**
	 * 创建时间 *
	 */

	@Column("create_date")
	@ColDefine(type = ColType.DATE)
	private Date create_date;

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

	public Date getCreate_time() {
		return create_time;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
    
}
