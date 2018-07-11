package com.reptile.ly.model.scenery;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

@Table("ly_scenery_price")
public class LySceneryPriceTagModel {

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
	@Column("order_time")
	private String order_time;
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
	 * 优惠信息 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("privilege")
	private String privilege;
	/**
	 * 预定 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("order_btn")
	private String order_btn;
	/**
	 * 入园方式 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 4000)
	@Column("in_park_style")
	private String in_park_style;
	/**
	 * 预定时间 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 1000)
	@Column("reserve_time")
	private String reserve_time;
	/**
	 * 包含项目 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	@Column("contain_project")
	private String contain_project;
	/**
	 * 预定说明 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 6000)
	@Column("reserve_introduction")
	private String reserve_introduction;
	/**
	 * 发票说明 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	@Column("invoice_introduction")
	private String invoice_introduction;
	/**
	 * 退改规则 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 4000)
	@Column("back_change_rules")
	private String back_change_rules;
	/** 票的id **/
	@ColDefine(type = ColType.VARCHAR, width = 20)
	@Column("ticket_pid")
	private String ticket_pid;
	/** 预定票的url **/
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("ticket_url")
	private String ticket_url;

	/**
	 * ************************** 构造器 ******************************
	 */
	public LySceneryPriceTagModel() {
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

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
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

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getOrder_btn() {
		return order_btn;
	}

	public void setOrder_btn(String order_btn) {
		this.order_btn = order_btn;
	}

	public String getIn_park_style() {
		return in_park_style;
	}

	public void setIn_park_style(String in_park_style) {
		this.in_park_style = in_park_style;
	}

	public String getReserve_time() {
		return reserve_time;
	}

	public void setReserve_time(String reserve_time) {
		this.reserve_time = reserve_time;
	}

	public String getContain_project() {
		return contain_project;
	}

	public void setContain_project(String contain_project) {
		this.contain_project = contain_project;
	}

	public String getReserve_introduction() {
		return reserve_introduction;
	}

	public void setReserve_introduction(String reserve_introduction) {
		this.reserve_introduction = reserve_introduction;
	}

	public String getInvoice_introduction() {
		return invoice_introduction;
	}

	public void setInvoice_introduction(String invoice_introduction) {
		this.invoice_introduction = invoice_introduction;
	}

	public String getBack_change_rules() {
		return back_change_rules;
	}

	public void setBack_change_rules(String back_change_rules) {
		this.back_change_rules = back_change_rules;
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
