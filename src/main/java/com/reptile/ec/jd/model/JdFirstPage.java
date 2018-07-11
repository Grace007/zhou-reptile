package com.reptile.ec.jd.model;

import java.util.Date;
import java.util.List;
/**
 * 京东首页Page数据
 * @author wingware
 *
 */
public class JdFirstPage{
	private static final long serialVersionUID = 5801552825229443439L;
	private String nextUrl;
	private int pageTotal;
	private int count;
	private String key;
	private String category;
	private int indexPage;
	private int pageCount;
	private List<JdProduct> productList;
	private Date createTime;
	private Long requestId;
	private Long taskId;

	public JdFirstPage(String nextUrl,int pageTotal,int count){
		this.nextUrl = nextUrl;
		this.pageTotal = pageTotal;
		this.count = count;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(int indexPage) {
		this.indexPage = indexPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<JdProduct> getProductList() {
		return productList;
	}

	public void setProductList(List<JdProduct> productList) {
		this.productList = productList;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getNextUrl() {
		return this.nextUrl;
	}
	
	public int getCount() {
		return count;
	}

	public int getPageTotal() {
		return pageTotal;
	}

}
