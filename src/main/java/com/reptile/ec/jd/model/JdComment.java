package com.reptile.ec.jd.model;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("jd_comment")
public class JdComment implements java.io.Serializable {

	private static final long serialVersionUID = 1885199811792760755L;

	/** 评论ID */
	@Id(auto = true)
	private int rid;
	/** 商品ID */
	@Column("ProductId")
	private String productId;
	/** 商品名称 */
	@Column("ProductName")
	private String productName;
	/** 关键字 */
	@Column("[Key]")
	private String key;
	/** 分类 */
	@Column("Category")
	private String category;
	/** 需求ID */
	@Column("RequestId")
	private Long requestId;
	/** 任务ID */
	@Column("TaskId")
	private Long taskId;
	/** 创建时间 */
	@Column("CreateTime")
	private Date createTime;
	/** 评论者ID */
	@Column("Nid")
	private String nid;
	@Column("Nickname")
	private String nickname;
	@Column("Color")
	private String Color;
	@Column("Version")
	private String Version;
	/** 评论者等级 */
	@Column("UserLevelName")
	private String userLevelName;
	/** 评论者地区 */
	@Column("UserProvince")
	private String userProvince;
	/** 购买日期 */
	@Column("ReferenceTime")
	private String referenceTime;
	/** 是否加精 */
	@Column("[Top]")
	private int top = 0;
	/** 评论内容 */
	@Column("Content")
	private String content;
	/** 评分 */
	@Column("Score")
	private String score;
	/** 评论时间/日期 */
	@Column("CreationTime")
	private String creationTime;
	/** 优点 */
	@Column("Pros")
	private String pros;
	/** 不足 */
	@Column("Cons")
	private String cons;
	/** 评论标题 */
	@Column("Topic")
	private String topic;
	/** 用户晒单（照片） */
	private String[] shiningImages;
	/** （此评价对我）有用/赞/顶 */
	@Column("UsefulVoteCount")
	private String usefulVoteCount;
	/** （此评价对我）没用/踩 */
	@Column("UselessVoteCount")
	private String uselessVoteCount;
	/** （评论）回复 */
	@Column("ReplyCount")
	private String replyCount;
	/** 灌入BI的状态0/初始化,1/导入BI中,2/导入BI成功,3/导入BI失败 */
	private int status = 1;
	/** 下载时的页码 */
	private int indexPage;
	// id
	@Column("id")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public JdComment() {
	}

	public JdComment(String id, String productId, String productName, String key, String category, Long requestId,
			Long taskId, Date createTime) {
		super();
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.key = key;
		this.category = category;
		this.requestId = requestId;
		this.taskId = taskId;
		this.createTime = createTime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getUserLevelName() {
		return userLevelName;
	}

	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}

	public String getUserProvince() {
		return userProvince;
	}

	public void setUserProvince(String userProvince) {
		this.userProvince = userProvince;
	}

	public String getReferenceTime() {
		return referenceTime;
	}

	public void setReferenceTime(String referenceTime) {
		this.referenceTime = referenceTime;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getPros() {
		return pros;
	}

	public void setPros(String pros) {
		this.pros = pros;
	}

	public String getCons() {
		return cons;
	}

	public void setCons(String cons) {
		this.cons = cons;
	}

	public String[] getShiningImages() {
		return shiningImages;
	}

	public void setShiningImages(String[] shiningImages) {
		this.shiningImages = shiningImages;
	}

	public String getUsefulVoteCount() {
		return usefulVoteCount;
	}

	public void setUsefulVoteCount(String usefulVoteCount) {
		this.usefulVoteCount = usefulVoteCount;
	}

	public String getUselessVoteCount() {
		return uselessVoteCount;
	}

	public void setUselessVoteCount(String uselessVoteCount) {
		this.uselessVoteCount = uselessVoteCount;
	}

	public String getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(String replyCount) {
		this.replyCount = replyCount;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public int getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(int indexPage) {
		this.indexPage = indexPage;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JdComment other = (JdComment) obj;
		if (rid != 0) {
			if (other.rid != 0)
				return false;
		} else if (rid != other.rid)
			return false;
		return true;
	}

}
