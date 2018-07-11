package com.reptile.haodaifu.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("haodf_doctor_info")
public class HaoDFDoctorInfo {
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
	private String ddepartment;
	@Column
	private String dvisit_count;
	@Column
	private String darticle_count;
	@Column
	private String dopening_time;
	
	@Column
	private String dhospital;
	@Column
	private String thankletternum;
	@Column
	private String presentnum;
	@Column
	private String experience;
	@Column
	private String homepage;
	@Column
	private String efficacy_satisfaction;
	@Column
	private String attitude_satisfaction;
	@Column
	private String patienttotal_help;
	@Column
	private String patienttwoweek;
	@Column
	private String clinical_experience;
	@Column
	private String patienttotal_treatment;
	@Column
	private String patienttotal_followup;
	@Column
	private String patient_vote;
	@Column
	private String patient_comment_total;
	@Column
	private String patient_comment_experiencenum;
	
	@Column
	private String dskill;
	@Column
	private String allrecommendlevel;
	@Column
	private String durl;
	@Column
	private String did;
	@Column
	private String username;
	
	@Column
	private Date create_time;
	@Column
	private String node;
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

	public String getThankletternum() {
		return thankletternum;
	}
	public void setThankletternum(String thankletternum) {
		this.thankletternum = thankletternum;
	}
	public String getPresentnum() {
		return presentnum;
	}
	public void setPresentnum(String presentnum) {
		this.presentnum = presentnum;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getEfficacy_satisfaction() {
		return efficacy_satisfaction;
	}
	public void setEfficacy_satisfaction(String efficacy_satisfaction) {
		this.efficacy_satisfaction = efficacy_satisfaction;
	}
	public String getAttitude_satisfaction() {
		return attitude_satisfaction;
	}
	public void setAttitude_satisfaction(String attitude_satisfaction) {
		this.attitude_satisfaction = attitude_satisfaction;
	}
	public String getPatienttotal_help() {
		return patienttotal_help;
	}
	public void setPatienttotal_help(String patienttotal_help) {
		this.patienttotal_help = patienttotal_help;
	}
	public String getPatienttwoweek() {
		return patienttwoweek;
	}
	public void setPatienttwoweek(String patienttwoweek) {
		this.patienttwoweek = patienttwoweek;
	}
	public String getClinical_experience() {
		return clinical_experience;
	}
	public void setClinical_experience(String clinical_experience) {
		this.clinical_experience = clinical_experience;
	}
	public String getPatienttotal_treatment() {
		return patienttotal_treatment;
	}
	public void setPatienttotal_treatment(String patienttotal_treatment) {
		this.patienttotal_treatment = patienttotal_treatment;
	}
	public String getPatienttotal_followup() {
		return patienttotal_followup;
	}
	public void setPatienttotal_followup(String patienttotal_followup) {
		this.patienttotal_followup = patienttotal_followup;
	}
	public String getPatient_vote() {
		return patient_vote;
	}
	public void setPatient_vote(String patient_vote) {
		this.patient_vote = patient_vote;
	}
	public String getPatient_comment_total() {
		return patient_comment_total;
	}
	public void setPatient_comment_total(String patient_comment_total) {
		this.patient_comment_total = patient_comment_total;
	}
	public String getPatient_comment_experiencenum() {
		return patient_comment_experiencenum;
	}
	public void setPatient_comment_experiencenum(
			String patient_comment_experiencenum) {
		this.patient_comment_experiencenum = patient_comment_experiencenum;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getAllrecommendlevel() {
		return allrecommendlevel;
	}
	public void setAllrecommendlevel(String allrecommendlevel) {
		this.allrecommendlevel = allrecommendlevel;
	}
	public String getDskill() {
		return dskill;
	}
	public void setDskill(String dskill) {
		this.dskill = dskill;
	}
	public String getDdepartment() {
		return ddepartment;
	}
	public void setDdepartment(String ddepartment) {
		this.ddepartment = ddepartment;
	}
	public String getDvisit_count() {
		return dvisit_count;
	}
	public void setDvisit_count(String dvisit_count) {
		this.dvisit_count = dvisit_count;
	}
	public String getDarticle_count() {
		return darticle_count;
	}
	public void setDarticle_count(String darticle_count) {
		this.darticle_count = darticle_count;
	}
	public String getDopening_time() {
		return dopening_time;
	}
	public void setDopening_time(String dopening_time) {
		this.dopening_time = dopening_time;
	}

}
