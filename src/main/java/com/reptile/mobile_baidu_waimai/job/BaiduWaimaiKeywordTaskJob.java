package com.reptile.mobile_baidu_waimai.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("mobile_baidu_waimai_keyword_task")
public class BaiduWaimaiKeywordTaskJob {
    @Id
    private long id;
    @Column
    private long request_id;
    @Column
    private long task_id;
    @Column
    private String keyword;
    @Column
    private String city_name;
    @Column
    private String city_id;
    @Column
    private String shop_name;
    @Column
    private long current_page;
    @Column
    private int current_category;
    @Column
    private long total;
    @Column
    private String node;
    @Column
    private int status;
    @Column
    private String project_code;
    @Column
    private String down_type;
    @Column
    private Date create_date;
    @Column
    private Date create_time;
    @Column
    private String remark;
    @Column
    private String generate_category;
    @Column
    private String office_id;
    @Column
    private String office_name;

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

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
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

    public long getCurrent_page() {
        return current_page;
    }

    public long getTotal() {
        return total;
    }

    public void setCurrent_page(long current_page) {
        this.current_page = current_page;
    }

    public void setTotal(long total) {
        this.total = total;
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
