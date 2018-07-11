package com.reptile.ec.mogujie.model.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;


@Table("mogujie_list_next_task")
public class MogujieListNextTaskModel {
    @Id
    @Column
    private long id;
    @Column
    private long request_id;
    @Column
    private long task_id;
    @Column
    private String keyword;
    @Column
    private String category;
    @Column
    private String list_next_url;
    @Column
    private int page_index;
    @Column
    private int page_total;
    @Column
    private String ppath;
    @Column
    private String website;
    @Column
    private String node;
    @Column
    private int status;
    @Column
    private int product_total;
    @Column
    private Date create_date;
    @Column
    private String project_code;
    @Column
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getProduct_total() {
        return product_total;
    }

    public void setProduct_total(int product_total) {
        this.product_total = product_total;
    }

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getList_next_url() {
        return list_next_url;
    }

    public void setList_next_url(String list_next_url) {
        this.list_next_url = list_next_url;
    }

    public int getPage_index() {
        return page_index;
    }

    public void setPage_index(int page_index) {
        this.page_index = page_index;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public String getPpath() {
        return ppath;
    }

    public void setPpath(String ppath) {
        this.ppath = ppath;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
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

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }
}
