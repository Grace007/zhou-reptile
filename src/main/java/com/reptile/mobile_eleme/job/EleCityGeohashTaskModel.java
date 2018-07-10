package com.reptile.mobile_eleme.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;

import java.util.Date;

public class EleCityGeohashTaskModel {

	
	@Id
	private long id;
	
	@Column
	private String abbr;
	
	@Column
	private String abbrId ;
	
	@Column
	private String latitude ;
	
	@Column
	private String longitude ;
	
	@Column
	private String name ;
	
	@Column
	private String pinyin ;
	
	@Column
	private String geohash;
	
	@Column
	private Date create_date;
	
	@Column
	private Date create_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getAbbrId() {
		return abbrId;
	}

	public void setAbbrId(String abbrId) {
		this.abbrId = abbrId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getGeohash() {
		return geohash;
	}

	public void setGeohash(String geohash) {
		this.geohash = geohash;
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
