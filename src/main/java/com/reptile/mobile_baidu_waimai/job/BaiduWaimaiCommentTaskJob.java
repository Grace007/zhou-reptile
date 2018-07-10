package com.reptile.mobile_baidu_waimai.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * Created by lex on 2017/8/1.
 */
@Table("mobile_baidu_waimai_shop_comment_task")
public class BaiduWaimaiCommentTaskJob {
    @Id
    private long id;
    @Column
    private long request_id;
    @Column
    private long task_id;
    @Column
    private Date create_time;
    @Column
    private String shop_id;
    @Column
    private String shop_name;
    @Column
    private String city_name;
    @Column
    private String keyword;
    @Column
    private String node;
    @Column
    private int page_from;
    @Column
    private int page_to;
    @Column
    private int total;
    @Column
    private int status;
    @Column
    private String project_code;
    @Column
    private String down_type;
    @Column
    private String remark;

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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public int getPage_from() {
        return page_from;
    }

    public void setPage_from(int page_from) {
        this.page_from = page_from;
    }

    public int getPage_to() {
        return page_to;
    }

    public void setPage_to(int page_to) {
        this.page_to = page_to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
