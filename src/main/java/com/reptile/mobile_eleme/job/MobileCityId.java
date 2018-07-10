package com.reptile.mobile_eleme.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("mobile_eleme_city_id")
public class MobileCityId {
	@Id(auto = true)
	private long id;
	@Column
	private String city_id;
	@Column
	private String latitude;
	@Column
	private String longitude;
	@Column
	private String name;
	@Column
	private String pinyin;
	@Column
	private String area_code;
	@Column
	private String abbr;

	public long getId() {
		return id;
	}

	public String getCity_id() {
		return city_id;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getName() {
		return name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public String getArea_code() {
		return area_code;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
}
