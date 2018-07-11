package com.reptile.ly.model.hotel;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 迪斯尼-同程酒店产品info类
 *
 */
@Table("ly_ghotel_info")
public class LyGHotelInfoModel {

	/**
	 * id *
	 */
	@Id(auto = true)
	private Long id;
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
	@ColDefine(type = ColType.VARCHAR, width = 32)
	@Column
	private String zuan;
	/**
	 * 唯一身份名字 *
	 */

	/************************* 评论相关 **************************/
	/**
	 * 全部评论数 *
	 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column("all_comment")
	private Integer all_comment;
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
	public LyGHotelInfoModel() {
		super();
	}

	public String getZuan() {
		return zuan;
	}

	public void setZuan(String zuan) {
		this.zuan = zuan;
	}

	public Integer getAll_comment() {
		return all_comment;
	}

	public void setAll_comment(Integer all_comment) {
		this.all_comment = all_comment;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
