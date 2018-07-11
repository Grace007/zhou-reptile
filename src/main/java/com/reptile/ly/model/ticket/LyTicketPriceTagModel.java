package com.reptile.ly.model.ticket;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 迪斯尼-同程票务每种票的具体内容类
 *
 */
@Table("ly_ticket_price")
public class LyTicketPriceTagModel {

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
	 * 商品id
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("pid")
	private String pid;
	/**
	 * 商品名称
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("pname")
	private String pname;
	/**
	 * url地址 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("url")
	private String url;

	/************************ 票的具体信息 ***********************/
	/**
	 * 票的类型 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("ticket_type")
	private String ticket_type;
	/**
	 * 票的名称 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("ticket_name")
	private String ticket_name;
	/**
	 * 可定时间 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("can_order_time")
	private String can_order_time;
	/**
	 * 入园方式 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("in_park_style")
	private String in_park_style;
	/**
	 * 同程价 *
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
	 * 预定 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("order_btn")
	private String order_btn;
	/**
	 * 预定时间 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("reserve_time")
	private String reserve_time;
	/**
	 * 预定说明 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("reserve_introduction")
	private String reserve_introduction;
	/** 票的id **/
	@ColDefine(type = ColType.VARCHAR, width = 20)
	@Column("ticket_pid")
	private String ticket_pid;
	/** 预定票的url **/
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("ticket_url")
	private String ticket_url;
	/** 有效期 **/
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("validity_period")
	private String validity_period;

	/**
	 * ********* 构造器 ************
	 */
	public LyTicketPriceTagModel() {
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

	public String getTicket_type() {
		return ticket_type;
	}

	public void setTicket_type(String ticket_type) {
		this.ticket_type = ticket_type;
	}

	public String getTicket_name() {
		return ticket_name;
	}

	public void setTicket_name(String ticket_name) {
		this.ticket_name = ticket_name;
	}

	public String getCan_order_time() {
		return can_order_time;
	}

	public void setCan_order_time(String can_order_time) {
		this.can_order_time = can_order_time;
	}

	public String getIn_park_style() {
		return in_park_style;
	}

	public void setIn_park_style(String in_park_style) {
		this.in_park_style = in_park_style;
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

	public String getOrder_btn() {
		return order_btn;
	}

	public void setOrder_btn(String order_btn) {
		this.order_btn = order_btn;
	}

	public String getReserve_time() {
		return reserve_time;
	}

	public void setReserve_time(String reserve_time) {
		this.reserve_time = reserve_time;
	}

	public String getReserve_introduction() {
		return reserve_introduction;
	}

	public void setReserve_introduction(String reserve_introduction) {
		this.reserve_introduction = reserve_introduction;
	}

	public String getTicket_pid() {
		return ticket_pid;
	}

	public void setTicket_pid(String ticket_pid) {
		this.ticket_pid = ticket_pid;
	}

	public String getTicket_url() {
		return ticket_url;
	}

	public void setTicket_url(String ticket_url) {
		this.ticket_url = ticket_url;
	}

	public String getValidity_period() {
		return validity_period;
	}

	public void setValidity_period(String validity_period) {
		this.validity_period = validity_period;
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
