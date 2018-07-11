package com.reptile.tianya.modelcommon;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_image")
public class Image {

	@Id
	private Long id;
	@ColDefine(type = ColType.VARCHAR, width = 64)
	@Column
	private String pid;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String pname;
	@ColDefine(type = ColType.VARCHAR, width = 512)
	@Column
	private String url;
	/** lvmama,ly,alitrip,qunar,mango,ctrip,tmall,taobao */
	@ColDefine(type = ColType.VARCHAR, width = 32)
	@Column
	private Site site;
	/** ticket,hotel,travel,scenery,dujia */
	@ColDefine(type = ColType.VARCHAR, width = 32)
	@Column
	private SiteType type;
	/**
	 * 截图/图标/详情 screenshot/icon/detail
	 */
	@ColDefine(type = ColType.VARCHAR, width = 64)
	@Column
	private Resource resource;
	/** 图片的原始url地址 */
	@ColDefine(type = ColType.VARCHAR, width = 512)
	@Column
	private String r_url;
	/** 图片文档地址 */
	@ColDefine(type = ColType.VARCHAR, width = 512)
	@Column
	private String path;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 32)
	private String create_date;
	@ColDefine(type = ColType.DATETIME)
	@Column
	private Date create_time;
	@ColDefine(type = ColType.VARCHAR, width = 512)
	@Column
	private String sku;
	@ColDefine(type = ColType.VARCHAR, width = 24)
	@Column
	private String dfs_group;
	@ColDefine(type = ColType.VARCHAR, width = 128)
	@Column
	private String dfs_path;
	
	public String getDfs_group() {
		return dfs_group;
	}

	public void setDfs_group(String dfs_group) {
		this.dfs_group = dfs_group;
	}

	public String getDfs_path() {
		return dfs_path;
	}

	public void setDfs_path(String dfs_path) {
		this.dfs_path = dfs_path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public SiteType getType() {
		return type;
	}

	public void setType(SiteType type) {
		this.type = type;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getR_url() {
		return r_url;
	}

	public void setR_url(String r_url) {
		this.r_url = r_url;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getSku(){
		return this.sku;
	}
	
	public void setSku(String sku){
		this.sku = sku;
	}
	
}
