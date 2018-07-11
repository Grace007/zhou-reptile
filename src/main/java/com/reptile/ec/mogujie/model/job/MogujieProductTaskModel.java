package com.reptile.ec.mogujie.model.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;


@Table("mogujie_product_task")
public class MogujieProductTaskModel {
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
    private String url;
    @Column
    private String title;
    @Column
    private double price;
    @Column
    private String discount;
    @Column
    private int collect;
    @Column
    private int sale_num;
    @Column
    private int com_num;
    @Column
    private String description;
    @Column
    private String product_id;
    @Column
    private String website;
    @Column
    private String node;
    @Column
    private int status;
    @Column
    private int comment_status;
    @Column
    private int deal_status;
    @Column
    private int sku_status;
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

    public int getSku_status() {
        return sku_status;
    }

    public void setSku_status(int sku_status) {
        this.sku_status = sku_status;
    }

    public int getCom_num() {
        return com_num;
    }

    public void setCom_num(int com_num) {
        this.com_num = com_num;
    }

    public int getDeal_status() {
        return deal_status;
    }

    public void setDeal_status(int deal_status) {
        this.deal_status = deal_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public int getComment_status() {
        return comment_status;
    }

    public void setComment_status(int comment_status) {
        this.comment_status = comment_status;
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
