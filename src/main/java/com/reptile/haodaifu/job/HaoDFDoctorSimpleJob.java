package com.reptile.haodaifu.job;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
@Table("haodf_doctor_simple_job")
public class HaoDFDoctorSimpleJob {
	@Id
	private long id;
	@Column
	private long request_id;
	@Column
	private long task_id;
	@Column
	private String big_categoryname;
	@Column
	private String median_categoryname;
	@Column
	private String small_categoryname;
	@Column
	private String tiny_categoryname;
	@Column
	private String dname;
	@Column
	private String dposition;
	@Column
	private String dhospital;
	@Column
	private String recommendlevel;
	@Column
	private String twoweek_reply;
	@Column
	private String dskill;
	@Column
	private String durl;
	@Column
	private String did;
	@Column
	private String dvote;
	@Column
	private String username;
	@Column
	private Date create_time;
	@Column
	private String node;
	@Column
	private int status;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getRequest_id() {
		return request_id;
	}
	public void setRequest_id(long request_id) {
		this.request_id = request_id;
	}
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public String getBig_categoryname() {
		return big_categoryname;
	}
	public void setBig_categoryname(String big_categoryname) {
		this.big_categoryname = big_categoryname;
	}
	public String getMedian_categoryname() {
		return median_categoryname;
	}
	public void setMedian_categoryname(String median_categoryname) {
		this.median_categoryname = median_categoryname;
	}
	public String getSmall_categoryname() {
		return small_categoryname;
	}
	public void setSmall_categoryname(String small_categoryname) {
		this.small_categoryname = small_categoryname;
	}
	public String getTiny_categoryname() {
		return tiny_categoryname;
	}
	public void setTiny_categoryname(String tiny_categoryname) {
		this.tiny_categoryname = tiny_categoryname;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getDposition() {
		return dposition;
	}
	public void setDposition(String dposition) {
		this.dposition = dposition;
	}
	public String getDhospital() {
		return dhospital;
	}
	public void setDhospital(String dhospital) {
		this.dhospital = dhospital;
	}
	public String getRecommendlevel() {
		return recommendlevel;
	}
	public void setRecommendlevel(String recommendlevel) {
		this.recommendlevel = recommendlevel;
	}
	
	public String getDskill() {
		return dskill;
	}
	public void setDskill(String dskill) {
		this.dskill = dskill;
	}
	public String getDurl() {
		return durl;
	}
	public void setDurl(String durl) {
		this.durl = durl;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getDvote() {
		return dvote;
	}
	public void setDvote(String dvote) {
		this.dvote = dvote;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTwoweek_reply() {
		return twoweek_reply;
	}
	public void setTwoweek_reply(String twoweek_reply) {
		this.twoweek_reply = twoweek_reply;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
