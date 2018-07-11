package com.reptile.ec.jd.job;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("jd_product_task")
public class JdProductJob {

	@Id(auto = true)
	private Long id;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String keyword;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String category;
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private Long requestId;
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private Long taskId;
	@ColDefine(type = ColType.VARCHAR, width = 512)
	@Column
	private String url;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String pid;
	@ColDefine(type = ColType.VARCHAR, width = 256)
	@Column
	private String pname;
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private int commentsPage = 1;
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private Long comments = 0L;
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private Long updated_comments = 0L;
	/** 0初始待处理，1处理中，2处理成功，3处理失败 */
	@ColDefine(type = ColType.INT, width = 20)
	@Column
	private String do_not_reset_comment;
	@ColDefine(type = ColType.INT, width = 10)
	@Column
	private int status = 0;
	@ColDefine(type = ColType.INT, width = 2)
	@Column
	private int comment_status = 0;

	@ColDefine(type = ColType.VARCHAR, width = 250)
	@Column
	private String project_code;
	@ColDefine(type = ColType.VARCHAR, width = 250)
	@Column
	private String website;
	@ColDefine(type = ColType.VARCHAR, width = 250)
	@Column
	private String down_type;

	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Column
	private String createDate;

	@Column
	private String node;
	@ColDefine(type = ColType.VARCHAR, width = 25)
	@Column
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProject_code() {
		return project_code;
	}

	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDown_type() {
		return down_type;
	}

	public void setDown_type(String down_type) {
		this.down_type = down_type;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getComment_status() {
		return comment_status;
	}

	public void setComment_status(int comment_status) {
		this.comment_status = comment_status;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public int getCommentsPage() {
		return commentsPage;
	}

	public void setCommentsPage(int commentsPage) {
		this.commentsPage = commentsPage;
	}

	public Long getComments() {
		return comments;
	}

	public void setComments(Long comments) {
		this.comments = comments;
	}

	public Long getUpdated_comments() {
		return updated_comments;
	}

	public void setUpdated_comments(Long updated_comments) {
		this.updated_comments = updated_comments;
	}

	public String getDo_not_reset_comment() {
		return do_not_reset_comment;
	}

	public void setDo_not_reset_comment(String do_not_reset_comment) {
		this.do_not_reset_comment = do_not_reset_comment;
	}

}
