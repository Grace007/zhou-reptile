package com.reptile.mobile_eleme.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("mobile_eleme_keyword_job")
public class EleMeSearchUrlJob {

	@Id(auto = true)
	@Column
	private Long id;

	@Column
	private String keyword;

	@Column
	private Long request_id;

	@Column
	private Long task_id;

	@Column
	private int status;

	@Column
	private Date create_date;

	@Column
	private Date create_time;

	@Column
	private String project_code;

	@Column
	private String down_type;

	@Column
	private String node;

	@Column
	private String url;

	@Column
	private String shopKeyword;

	@Column
	private String city_name;

	@Column
	private int offset;
	@Column
	private int current_category;

	@Column
	private String remark;

	@Column
	private String generate_category;
    @Column
    private String office_id;
    @Column
    private String office_name;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getShopKeyword() {
		return shopKeyword;
	}

	public void setShopKeyword(String shopKeyword) {
		this.shopKeyword = shopKeyword;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getCurrent_category() {
		return current_category;
	}

	public void setCurrent_category(int current_category) {
		this.current_category = current_category;
	}

	public String getGenerate_category() {
		return generate_category;
	}

	public void setGenerate_category(String generate_category) {
		this.generate_category = generate_category;
	}

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }
}