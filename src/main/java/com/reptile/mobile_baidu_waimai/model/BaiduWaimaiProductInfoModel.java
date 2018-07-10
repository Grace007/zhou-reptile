package com.reptile.mobile_baidu_waimai.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("baidu_waimai_product_info${all}")
public class BaiduWaimaiProductInfoModel {
	@Id
	private long id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private Date create_time;
	@Column
	private String unique_id;
	@Column
	private String shop_id;
	@Column
	private String type;
	@Column
	private String product_name;
	@Column
	private String product_id;
	@Column
	private String recommendation;
	@Column
	private String sales;
	@Column
	private String inventory;
	@Column
	private String current_price;
	@Column
	private String original_price;
	@Column
	private String shop_name;
	@Column
	private String city_name;
	@Column
	private String keyword;
	@Column
	private String specification;
	@Column
	private long Specification_Amount;
    @Column
    private String product_description;

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
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

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
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

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public long getSpecification_Amount() {
		return Specification_Amount;
	}

	public void setSpecification_Amount(long specification_Amount) {
		Specification_Amount = specification_Amount;
	}
}
