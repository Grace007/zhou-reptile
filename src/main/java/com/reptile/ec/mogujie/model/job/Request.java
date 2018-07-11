package com.reptile.ec.mogujie.model.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;


@Table("Request")
public class Request {
    @Id
    @Column
    private long id;
    @Column
    private String request_name;
    @Column
    private String website;

    @Column
    private String request_type;
    @Column
    private String parm_type;


    @Column
    private String project_code;
    @Column
    private int status;

    @Column
    private Date startdate;
    @Column
    private Date enddate;
    @Column
    private String key_list;

    // 创建日期
    @Column
    private Date create_date;
    // 创建时间
    @Column
    private Date create_time;

    @Column
    private String remark;
    @Column
    private String local;
    @Column
    private String type;
    /** 任务总量 */
    @Column
    private int total;

    private String create_time_view;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRequest_name() {
        return request_name;
    }

    public void setRequest_name(String request_name) {
        this.request_name = request_name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getParm_type() {
        return parm_type;
    }

    public void setParm_type(String parm_type) {
        this.parm_type = parm_type;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getKey_list() {
        return key_list;
    }

    public void setKey_list(String key_list) {
        this.key_list = key_list;
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCreate_time_view() {
        return create_time_view;
    }

    public void setCreate_time_view(String create_time_view) {
        this.create_time_view = create_time_view;
    }
}
