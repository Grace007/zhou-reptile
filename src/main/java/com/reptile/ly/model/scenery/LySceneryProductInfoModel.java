package com.reptile.ly.model.scenery;

import com.reptile.ly.model.scenery.job.LySceneryCommentJob;
import org.nutz.dao.entity.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * 迪斯尼-同程景点产品info类
 *
 */
@Table("ly_scenery_info")
public class LySceneryProductInfoModel {

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

	/********************************** 产品信息 ***********************************/
	/**
	 * 优惠信息及景点推荐 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("privilege")
	private String privilege;
	/**
	 * 景区等级 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("level")
	private String level;
	/**
	 * 开放时间 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	@Column("open_time")
	private String open_time;
	/**
	 * 服务保障 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	@Column("service_assurance")
	private String service_assurance;

	/**
	 * ***************************** 票价相关内容 ********************************
	 */
	private List<LySceneryPriceTagModel> scenery_prices;

	/********************************** 预定须知 **********************************/
	/**
	 * 预定须知 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("booking_notes")
	private String booking_notes;

	/********************************** 景点简介 **********************************/
	/**
	 * 景点简介 *
	 */
	@Column("info")
	private String info;

	/********************************** 评论相关 **********************************/
	/**
	 * 满意度 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column(" satisfaction")
	private String satisfaction;
	/**
	 * 大家觉得 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	@Column("feelings")
	private String feelings;
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

	/********************************** 交通指南 **********************************/
	/**
	 * 交通指南 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("traffic_info")
	private String traffic_info;

	private List<LySceneryCommentJob> commentJob_list;

	/**
	 * ************************** 构造器 ******************************
	 */
	public LySceneryProductInfoModel() {
		super();
	}

	/**
	 * ************************* 设定器和访问器 **************************
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

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getOpen_time() {
		return open_time;
	}

	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}

	public String getService_assurance() {
		return service_assurance;
	}

	public void setService_assurance(String service_assurance) {
		this.service_assurance = service_assurance;
	}

	public List<LySceneryPriceTagModel> getScenery_prices() {
		return scenery_prices;
	}

	public void setScenery_prices(List<LySceneryPriceTagModel> scenery_prices) {
		this.scenery_prices = scenery_prices;
	}

	public String getBooking_notes() {
		return booking_notes;
	}

	public void setBooking_notes(String booking_notes) {
		this.booking_notes = booking_notes;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}

	public String getFeelings() {
		return feelings;
	}

	public void setFeelings(String feelings) {
		this.feelings = feelings;
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

	public Integer getImg_comment() {
		return img_comment;
	}

	public void setImg_comment(Integer img_comment) {
		this.img_comment = img_comment;
	}

	public String getTraffic_info() {
		return traffic_info;
	}

	public void setTraffic_info(String traffic_info) {
		this.traffic_info = traffic_info;
	}

	public List<LySceneryCommentJob> getCommentJob_list() {
		return commentJob_list;
	}

	public void setCommentJob_list(List<LySceneryCommentJob> commentJob_list) {
		this.commentJob_list = commentJob_list;
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
