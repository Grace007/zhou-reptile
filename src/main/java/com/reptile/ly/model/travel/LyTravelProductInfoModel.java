package com.reptile.ly.model.travel;

import com.reptile.ly.model.travel.job.LyTravelCommentJob;
import org.nutz.dao.entity.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * 迪斯尼-同程出境游旅游产品info类
 *
 */
@Table("ly_travel_info")
public class LyTravelProductInfoModel {

	/**
	 * 产品id *
	 */
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
	 * 任务id *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("task_id")
	private Long task_id;
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
	 * 产品实例图片地址 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("product_img_url")
	private String product_img_url;// **************************************

	/******************************* 旅游具体信息 *******************************/
	// /** 客服电话 **/
	// @ColDefine(type = ColType.VARCHAR, width = 50)
	// @Column("phone")
	// private String phone;
	// /** 星评 **/
	// @ColDefine(type = ColType.VARCHAR, width = 50)
	// @Column("stars")
	// private String stars;
	// /** 出游类型 **/
	// @ColDefine(type = ColType.VARCHAR, width = 50)
	// @Column("travel_type")
	// private String travel_type;
	/**
	 * 出港城市 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("start_city")
	private String start_city;
	// /** 最低价格 **/
	// @ColDefine(type = ColType.VARCHAR, width = 50)
	// @Column("lowest_price")
	// private String lowest_price;
	/**
	 * 满意度 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("satisfaction")
	private String satisfaction;
	/**
	 * 购买人数 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("tourists")
	private String tourists;
	/**
	 * 特色 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("feature")
	private String feature;
	// /** 优惠信息简介 **/
	// @ColDefine(type = ColType.VARCHAR, width = 128)
	// @Column("privilege_note")
	// private String privilege_note;
	/**
	 * 体验师推荐 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 4000)
	@Column("recommend_reason")
	private String recommend_reason;
	// /** 优惠信息具体介绍 **/
	// @ColDefine(type = ColType.VARCHAR, width = 256)
	// @Column("privilege")
	// private String privilege;

	/************************************ 产品详情 ***********************************/

	/**
	 * 交通信息 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 3000)
	@Column("traffic_info")
	private String traffic_info;
	/**
	 * 酒店信息 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 3000)
	@Column("hotel_detail")
	private String hotel_detail;

	/************************************ 费用说明 ***********************************/
	/**
	 * 费用包含 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 3000)
	@Column("cost_contain")
	private String cost_contain;
	/**
	 * 费用不包含 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 3000)
	@Column("cost_not_contain")
	private String cost_not_contain;
	/**
	 * 退规说明 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 3000)
	@Column("refund_introduction")
	private String refund_introduction;

	/************************************ 预定须知 ***********************************/
	/**
	 * 预定须知 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 4000)
	@Column("booking_notes")
	private String booking_notes;
	/**
	 * 温馨提示 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 4000)
	@Column("reminder")
	private String reminder;
	/**
	 * 安全须知 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 4000)
	@Column("safety_precautions")
	private String safety_precautions;
	// /** 签约方式 **/
	// @ColDefine(type = ColType.VARCHAR, width = 512)
	// @Column("contract_way")
	// private String contract_way;
	// /** 付款方式 **/
	// @ColDefine(type = ColType.VARCHAR, width = 512)
	// @Column("pay_way")
	// private String pay_way;

	/************************ 评论得分 ************************/
	/**
	 * 交通行程评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("routing_score")
	private String routing_score;
	/**
	 * 住宿餐食评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("meals_score")
	private String meals_score;
	/**
	 * 同程服务评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("customer_service_score")
	private String customer_service_score;
	/**
	 * 导游服务评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column("service_score")
	private String service_score;

	/************************* 评论相关 ***********************/
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
	 * 有图数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("img_comment")
	private Integer img_comment;

	/**
	 * 评论列表 *
	 */
	private List<LyTravelCommentJob> commentJob_list;

	/**
	 * ********* 构造器 ************
	 */
	public LyTravelProductInfoModel() {
		super();
	}

	/**
	 * *** ***设定器和访问器 *********
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

	public String getProduct_img_url() {
		return product_img_url;
	}

	public void setProduct_img_url(String product_img_url) {
		this.product_img_url = product_img_url;
	}

	public String getStart_city() {
		return start_city;
	}

	public void setStart_city(String start_city) {
		this.start_city = start_city;
	}

	public String getTourists() {
		return tourists;
	}

	public void setTourists(String tourists) {
		this.tourists = tourists;
	}

	public String getRecommend_reason() {
		return recommend_reason;
	}

	public void setRecommend_reason(String recommend_reason) {
		this.recommend_reason = recommend_reason;
	}

	public String getCost_contain() {
		return cost_contain;
	}

	public void setCost_contain(String cost_contain) {
		this.cost_contain = cost_contain;
	}

	public String getCost_not_contain() {
		return cost_not_contain;
	}

	public void setCost_not_contain(String cost_not_contain) {
		this.cost_not_contain = cost_not_contain;
	}

	public String getRefund_introduction() {
		return refund_introduction;
	}

	public void setRefund_introduction(String refund_introduction) {
		this.refund_introduction = refund_introduction;
	}

	public String getSafety_precautions() {
		return safety_precautions;
	}

	public void setSafety_precautions(String safety_precautions) {
		this.safety_precautions = safety_precautions;
	}

	public String getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}

	public String getRouting_score() {
		return routing_score;
	}

	public void setRouting_score(String routing_score) {
		this.routing_score = routing_score;
	}

	public String getService_score() {
		return service_score;
	}

	public void setService_score(String service_score) {
		this.service_score = service_score;
	}

	public String getCustomer_service_score() {
		return customer_service_score;
	}

	public void setCustomer_service_score(String customer_service_score) {
		this.customer_service_score = customer_service_score;
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

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getTraffic_info() {
		return traffic_info;
	}

	public void setTraffic_info(String traffic_info) {
		this.traffic_info = traffic_info;
	}

	public String getHotel_detail() {
		return hotel_detail;
	}

	public void setHotel_detail(String hotel_detail) {
		this.hotel_detail = hotel_detail;
	}

	public String getBooking_notes() {
		return booking_notes;
	}

	public void setBooking_notes(String booking_notes) {
		this.booking_notes = booking_notes;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	public String getMeals_score() {
		return meals_score;
	}

	public void setMeals_score(String meals_score) {
		this.meals_score = meals_score;
	}

	public Integer getImg_comment() {
		return img_comment;
	}

	public void setImg_comment(Integer img_comment) {
		this.img_comment = img_comment;
	}

	public List<LyTravelCommentJob> getCommentJob_list() {
		return commentJob_list;
	}

	public void setCommentJob_list(List<LyTravelCommentJob> commentJob_list) {
		this.commentJob_list = commentJob_list;
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

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
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

}
