package com.reptile.ec.amazon.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("amazon_product")
public class AmazonProductModel {
	@Column
	private String page_id;
	@Column
	private long request_id;
	@Column
	private String url;

	// 商品名称
	@Column
	private String product_name;
	// 品牌
	@Column
	private String product_brand;

	@Column
	private String keyword;
	// 市场价
	@Column
	private double product_market_price;
	// 价格
	@Column
	private double product_price;
	@Column
	private double special_price;
	// 优惠
	@Column
	private String product_save_price;
	// 总评价数
	@Column
	private int comment_total_num;
	// 用户评分
	@Column
	private String product_score;
	// 亚马逊热销商品排名
	@Column
	private int product_rank;
	// 经常一起购买的商品
	@Column
	private String together_by_name;
	@Column
	private String together_by_price;
	@Column
	private int sku_number;
	@Column
	private int create_date;
	@Column
	private String navigation;
	@Id(auto = true)
	@Column
	private int id;

	public String getPage_id() {
		return page_id;
	}

	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}

	public long getRequest_id() {
		return request_id;
	}

	public void setRequest_id(long request_id) {
		this.request_id = request_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_brand() {
		return product_brand;
	}

	public void setProduct_brand(String product_brand) {
		this.product_brand = product_brand;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public double getProduct_market_price() {
		return product_market_price;
	}

	public void setProduct_market_price(double product_market_price) {
		this.product_market_price = product_market_price;
	}

	public double getProduct_price() {
		return product_price;
	}

	public void setProduct_price(double product_price) {
		this.product_price = product_price;
	}

	public double getSpecial_price() {
		return special_price;
	}

	public void setSpecial_price(double special_price) {
		this.special_price = special_price;
	}

	public String getProduct_save_price() {
		return product_save_price;
	}

	public void setProduct_save_price(String product_save_price) {
		this.product_save_price = product_save_price;
	}

	public int getComment_total_num() {
		return comment_total_num;
	}

	public void setComment_total_num(int comment_total_num) {
		this.comment_total_num = comment_total_num;
	}

	public String getProduct_score() {
		return product_score;
	}

	public void setProduct_score(String product_score) {
		this.product_score = product_score;
	}

	public int getProduct_rank() {
		return product_rank;
	}

	public void setProduct_rank(int product_rank) {
		this.product_rank = product_rank;
	}

	public String getTogether_by_name() {
		return together_by_name;
	}

	public void setTogether_by_name(String together_by_name) {
		this.together_by_name = together_by_name;
	}

	public String getTogether_by_price() {
		return together_by_price;
	}

	public void setTogether_by_price(String together_by_price) {
		this.together_by_price = together_by_price;
	}

	public int getCreate_date() {
		return create_date;
	}

	public void setCreate_date(int create_date) {
		this.create_date = create_date;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSku_number() {
		return sku_number;
	}

	public void setSku_number(int sku_number) {
		this.sku_number = sku_number;
	}
}
