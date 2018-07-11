package com.reptile.ly.model.hotel;

import java.util.Date;
import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 迪斯尼-同程酒店产品info类
 *
 */
@Table("ly_hotel_info")
public class LyHotelProductInfoModel {

	@Id(auto = true)
	private Long id;
	/**
	 * 关键词 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("keyword")
	private String keyword;
	/**
	 * 需求id *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("request_id")
	private Long request_id;

	/**
	 * 抓取日期 *
	 */
	@Column("create_date")
	private Date create_date;
	/**
	 * 抓取时间 *
	 */
	@Column("create_time")
	private Date create_time;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("pid")
	private String pid;
	/**
	 * 产品名 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("pname")
	private String pname;
	/** url **/
	@ColDefine(type = ColType.VARCHAR, width = 512)
	@Column("url")
	private String url;
	/**
	 * 当前真实价格(包含促销价格)
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("price")
	private String price;
	/**
	 * 原价 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("original_price")
	private String original_price;
	/**
	 * 分数
	 */
	@ColDefine(type = ColType.VARCHAR, width = 12)
	@Column
	private String score;
	/**
	 * 地址
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String address;
	/**
	 * 唯一身份编号 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("sid")
	private String sid;
	/**
	 * 唯一身份名字 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("sname")
	private String sname;
	/**
	 * 酒店等级 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("level")
	private String level;
	/**
	 * 酒店设施简单说明 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	@Column("hotel_facility_simple")
	private String hotel_facility_simple;
	/**
	 * 电话 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("phone")
	private String phone;
	/**
	 * 产品实例图片地址 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("product_img_url")
	private String product_img_url;// **************************************

	/**
	 * ********************* 酒店房间 *********************
	 */
	private List<LyHotelRoomTagModel> roomPrices;

	/************************ 酒店详情 **********************/
	/**
	 * 酒店简介 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("hotel_info")
	private String hotel_info;
	/**
	 * 酒店政策 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("hotel_policy")
	private String hotel_policy;
	/**
	 * 酒店设施 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("hotel_facilities")
	private String hotel_facilities;
	/**
	 * 周边设施 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("around_facilities")
	private String around_facilities;

	/************************ 评论得分 ***********************/
	/**
	 * 满意度 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column(" satisfaction")
	private String satisfaction;
	/**
	 * 周边环境评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("around_facilities_score")
	private String around_facilities_score;
	/**
	 * 酒店服务评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("service_score")
	private String service_score;
	/**
	 * 酒店设施评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("facility_score")
	private String facility_score;
	/**
	 * 卫生评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("health_score")
	private String health_score;

	/************************* 评论相关 **************************/
	/**
	 * 全部评论数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("comments")
	private Integer comments;
	/**
	 * 好评数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("good_comment")
	private Integer good_comment;
	/**
	 * 中评数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("middle_comment")
	private Integer middle_comment;
	/**
	 * 差评数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("bad_comment")
	private Integer bad_comment;

	/**
	 * ********* 构造器 ************
	 */
	public LyHotelProductInfoModel() {
		super();
	}

	/**
	 * ****** 设定器和访问器 *********
	 */
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getHotel_facility_simple() {
		return hotel_facility_simple;
	}

	public void setHotel_facility_simple(String hotel_facility_simple) {
		this.hotel_facility_simple = hotel_facility_simple;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProduct_img_url() {
		return product_img_url;
	}

	public void setProduct_img_url(String product_img_url) {
		this.product_img_url = product_img_url;
	}

	public String getHotel_info() {
		return hotel_info;
	}

	public void setHotel_info(String hotel_info) {
		this.hotel_info = hotel_info;
	}

	public String getHotel_policy() {
		return hotel_policy;
	}

	public void setHotel_policy(String hotel_policy) {
		this.hotel_policy = hotel_policy;
	}

	public String getHotel_facilities() {
		return hotel_facilities;
	}

	public void setHotel_facilities(String hotel_facilities) {
		this.hotel_facilities = hotel_facilities;
	}

	public String getAround_facilities() {
		return around_facilities;
	}

	public void setAround_facilities(String around_facilities) {
		this.around_facilities = around_facilities;
	}

	public String getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}

	public String getAround_facilities_score() {
		return around_facilities_score;
	}

	public void setAround_facilities_score(String around_facilities_score) {
		this.around_facilities_score = around_facilities_score;
	}

	public String getService_score() {
		return service_score;
	}

	public void setService_score(String service_score) {
		this.service_score = service_score;
	}

	public String getFacility_score() {
		return facility_score;
	}

	public void setFacility_score(String facility_score) {
		this.facility_score = facility_score;
	}

	public String getHealth_score() {
		return health_score;
	}

	public void setHealth_score(String health_score) {
		this.health_score = health_score;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public Integer getGood_comment() {
		return good_comment;
	}

	public void setGood_comment(Integer good_comment) {
		this.good_comment = good_comment;
	}

	public Integer getMiddle_comment() {
		return middle_comment;
	}

	public void setMiddle_comment(Integer middle_comment) {
		this.middle_comment = middle_comment;
	}

	public Integer getBad_comment() {
		return bad_comment;
	}

	public void setBad_comment(Integer bad_comment) {
		this.bad_comment = bad_comment;
	}

	public List<LyHotelRoomTagModel> getRoomPrices() {
		return roomPrices;
	}

	public void setRoomPrices(List<LyHotelRoomTagModel> roomPrices) {
		this.roomPrices = roomPrices;
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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
