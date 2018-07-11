package com.reptile.ly.model.ticket;

import com.reptile.ly.model.ticket.job.LyTicketCommentJob;
import org.nutz.dao.entity.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 迪斯尼-同程出境游票务产品info类
 *
 */
@Table("ly_ticket_info")
public class LyTicketProductInfoModel {

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

	/*********************************** 票务具体信息 **********************************/
	/**
	 * 客服电话 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("phone")
	private String phone;
	/**
	 * 星评 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("stars")
	private String stars;
	/**
	 * 评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("rating")
	private String rating;
	/**
	 * 昨天的评论数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("comments_yestoday")
	private String comments_yestoday;
	/**
	 * 月销量 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("sales")
	private String sales;
	/**
	 * 推荐理由 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	@Column("recommend_reason")
	private String recommend_reason;

	/**
	 * ********************* 票价信息 ***********************
	 */

	private List<LyTicketPriceTagModel> tickets = new ArrayList<LyTicketPriceTagModel>();

	private List<LyTicketCommentJob> commentJobs = new ArrayList<LyTicketCommentJob>();

	/************************ 特色体验 ************************/
	/**
	 * 文字部分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 8000)
	@Column("feature_experience")
	private String feature_experience;
	/**
	 * 特色图片地址 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("feature_img_url")
	private String feature_img_url;// ****************************************

	/************************ 预定须知 ************************/
	/**
	 * 预定须知 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("booking_notes")
	private String booking_notes;

	/************************ 评论得分 ************************/
	/**
	 * 满意度 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("satisfaction")
	private String satisfaction;
	/**
	 * 服务评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("service_score")
	private String service_score;
	/**
	 * 便捷评分 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("convenient_score")
	private String convenient_score;
	/**
	 * 性价比 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("cost_performance")
	private String cost_performance;

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
	 * 有图评论数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("img_comment")
	private Integer img_comment;

	/**
	 * ********* 构造器 ************
	 */
	public LyTicketProductInfoModel() {
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

	public String getProduct_img_url() {
		return product_img_url;
	}

	public void setProduct_img_url(String product_img_url) {
		this.product_img_url = product_img_url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getComments_yestoday() {
		return comments_yestoday;
	}

	public void setComments_yestoday(String comments_yestoday) {
		this.comments_yestoday = comments_yestoday;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getRecommend_reason() {
		return recommend_reason;
	}

	public void setRecommend_reason(String recommend_reason) {
		this.recommend_reason = recommend_reason;
	}

	public List<LyTicketPriceTagModel> getTickets() {
		return tickets;
	}

	public void setTickets(List<LyTicketPriceTagModel> tickets) {
		this.tickets = tickets;
	}

	public String getFeature_experience() {
		return feature_experience;
	}

	public void setFeature_experience(String feature_experience) {
		this.feature_experience = feature_experience;
	}

	public String getFeature_img_url() {
		return feature_img_url;
	}

	public void setFeature_img_url(String feature_img_url) {
		this.feature_img_url = feature_img_url;
	}

	public String getBooking_notes() {
		return booking_notes;
	}

	public void setBooking_notes(String booking_notes) {
		this.booking_notes = booking_notes;
	}

	public String getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}

	public String getService_score() {
		return service_score;
	}

	public void setService_score(String service_score) {
		this.service_score = service_score;
	}

	public String getConvenient_score() {
		return convenient_score;
	}

	public void setConvenient_score(String convenient_score) {
		this.convenient_score = convenient_score;
	}

	public String getCost_performance() {
		return cost_performance;
	}

	public void setCost_performance(String cost_performance) {
		this.cost_performance = cost_performance;
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

	public List<LyTicketCommentJob> getCommentJobs() {
		return commentJobs;
	}

	public void setCommentJobs(List<LyTicketCommentJob> commentJobs) {
		this.commentJobs = commentJobs;
	}

}
