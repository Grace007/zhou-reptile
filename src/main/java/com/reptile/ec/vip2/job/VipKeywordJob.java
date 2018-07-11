package com.reptile.ec.vip2.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * Created by lex on 2017/3/28.
 */
@Table("vip_keyword_job")
public class VipKeywordJob {
    @Id(auto = true)
    private int id;
    @Column
    private long request_id;
    @Column
    private long task_id;
    @Column
    private Date create_date;
    @Column
    private String keyword;
    @Column
    private int status;
    @Column
    private String node;
    @Column
    private int level;
    @Column
    private String project_code;
    @Column
    private String down_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
}
