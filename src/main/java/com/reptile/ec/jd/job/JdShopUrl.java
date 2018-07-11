package com.reptile.ec.jd.job;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * Created by lex on 2017/6/21.
 */
@Table("jd_shop_url_task")
public class JdShopUrl {
    @Id(auto = true)
    private Long id;
    @Column
    private String shop_url;
    @Column
    private String keyword;
    @Column
    private String category;
    @Column
    private Long request_id;
    @Column
    private Long task_id;
    @Column
    private Integer page_total;
    @Column
    private Integer count;
    @Column
    private int status;
    @Column
    private String type;
    @Column
    private String project_code;
    @Column
    private String website;
    @Column
    private String down_type;
    @Column
    private Date create_date;
    @Column
    private Date create_time;
    @Column
    private String node;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop_url() {
        return shop_url;
    }

    public void setShop_url(String shop_url) {
        this.shop_url = shop_url;
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

    public Integer getPage_total() {
        return page_total;
    }

    public void setPage_total(Integer page_total) {
        this.page_total = page_total;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDown_type() {
        return down_type;
    }

    public void setDown_type(String down_type) {
        this.down_type = down_type;
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

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
