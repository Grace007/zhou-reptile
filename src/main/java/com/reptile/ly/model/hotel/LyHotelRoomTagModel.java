package com.reptile.ly.model.hotel;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 迪斯尼-同程酒店每种房间的具体内容类
 *
 */
@Table("ly_hotel_price")
public class LyHotelRoomTagModel {

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

	/************************ 酒店房间的具体信息 ***********************/
	/**
	 * 房间的类型 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("room_type")
	private String room_type;
	/**
	 * 房间信息 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("room_note")
	private String room_note;
	/**
	 * 优惠信息 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	@Column("privilege")
	private String privilege;
	/**
	 * 房间其他提示信息，如：礼/快/代 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 2000)
	@Column("room_subjoin_note")
	private String room_subjoin_note;
	/**
	 * 床型 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("bed_type")
	private String bed_type;
	/**
	 * 早餐 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("breakfast")
	private String breakfast;
	/**
	 * 宽带 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("broad_band")
	private String broad_band;
	/**
	 * 政策 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("policy")
	private String policy;
	/**
	 * 价格 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("price")
	private String price;
	/**
	 * 预定状态：可预订\不可订\订完等等 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("reserve_status")
	private String reserve_status;
	/**
	 * 付款方式：预付等等 *
	 */
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column("reserve_style")
	private String reserve_style;
	/** 代理房型 **/
	@ColDefine(type = ColType.VARCHAR, width = 20)
	@Column("agent")
	private String agent;

	/**
	 * ********* 构造器 ************
	 */
	public LyHotelRoomTagModel() {
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

	public String getRoom_type() {
		return room_type;
	}

	public void setRoom_type(String room_type) {
		this.room_type = room_type;
	}

	public String getRoom_note() {
		return room_note;
	}

	public void setRoom_note(String room_note) {
		this.room_note = room_note;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getRoom_subjoin_note() {
		return room_subjoin_note;
	}

	public void setRoom_subjoin_note(String room_subjoin_note) {
		this.room_subjoin_note = room_subjoin_note;
	}

	public String getBed_type() {
		return bed_type;
	}

	public void setBed_type(String bed_type) {
		this.bed_type = bed_type;
	}

	public String getBreakfast() {
		return breakfast;
	}

	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
	}

	public String getBroad_band() {
		return broad_band;
	}

	public void setBroad_band(String broad_band) {
		this.broad_band = broad_band;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getReserve_status() {
		return reserve_status;
	}

	public void setReserve_status(String reserve_status) {
		this.reserve_status = reserve_status;
	}

	public String getReserve_style() {
		return reserve_style;
	}

	public void setReserve_style(String reserve_style) {
		this.reserve_style = reserve_style;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
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
