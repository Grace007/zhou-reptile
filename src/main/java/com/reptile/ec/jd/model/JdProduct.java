package com.reptile.ec.jd.model;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 京东商品
 *
 * @author wingware
 */
// @Table("jd_product")
@Table("jd_product")
public class JdProduct implements java.io.Serializable {
    private static final long serialVersionUID = -7907794350885415454L;
    // rid
    @Id
    int rid;
    /**
     * 商品ID
     */
    @Column
    @ColDefine(type = ColType.INT, width = 20)
    private String id;
    /**
     * 商品id
     */
    @Column("Pid")
    private String pid;
    /**
     * 商品名
     */
    @Column("Name")
    private String name;
    /**
     * 商品简介
     */
    private String info;
    /**
     * 关键字
     */
    @Column("[Key]")
    private String key;
    /**
     * 分类
     */
    @Column("Category")
    private String category;
    /**
     * 需求ID
     */
    @Column("RequestId")
    private Long requestId;
    /**
     * 任务ID
     */
    @Column("TaskId")
    private Long taskId;
    /**
     * 创建时间
     */
    @Column("CreateTime")
    private Date createTime;
    /**
     * 商品url
     */
    @Column("Url")
    private String url;
    /**
     * 价格图片地址
     */
    private String price_url;
    /**
     * 价格
     */
    @Column("Price")
    private String price;
    /**
     * 市场价
     */
    @Column("Market_price")
    private String market_price;
    /**
     * 颜色
     */
    @Column("Color")
    private String color;
    /**
     * 版本/规格/套餐
     */
    @Column("Version")
    private String version;
    /**
     * 商品介绍
     */
    @Column("Description")
    private String description;
    /**
     * 包装清单
     */
    @Column("Packing_list")
    private String packing_list;
    /**
     * 售后保障/售后服务
     */
    @Column("Service")
    private String service;
    /**
     * 规格参数
     */
    @Column("Parameter")
    private String parameter;
    /**
     * 商品评论
     */
    @Column("Comments")
    private Long comments;
    /**
     * 评分
     */
    @Column("Score")
    private String score;
    /**
     * 好评度%
     */
    @Column("GoodRateShow")
    private int goodRateShow;
    /**
     * 中评%
     */
    @Column("GeneralRateShow")
    private int generalRateShow;
    /**
     * 差评%
     */
    @Column("PoorRateShow")
    private int poorRateShow;
    /**
     * 好评数量
     */
    @Column("GoodCount")
    private int goodCount;
    /**
     * 中评数量
     */
    @Column("GeneralCount")
    private int generalCount;
    /**
     * 差评数量
     */
    @Column("PoorCount")
    private int poorCount;
    /**
     * 晒单数量
     */
    @Column("ShowCount")
    private int showCount;
    /**
     * jd查询价格用
     */
    private String type;
    /**
     * 总评论页码
     */
    private int totalCommentPage = 0;
    /**
     * 灌入BI的状态0/初始化,1/导入BI中,2/导入BI成功,3/导入BI失败
     */
    private int status = 0;
    /**
     * 导航条
     */
    @Column("navigation")
    private String navigation;
    /**
     * 促销信息
     */
    @Column("promotion")
    private String promotion;

    public String getColor_size() {
        return color_size;
    }

    public void setColor_size(String color_size) {
        this.color_size = color_size;
    }

    /**
     * sku信息
     */
    @Column("color_size")
    private String color_size;
    private Integer pageTotal;
    @Column("tax")
    private String tax;
    @Column("stock")
    private String stock;
    
    public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    /**
     * 手机版价格
     */
    // @Column("m_price")
    // private String m_price;
    // @Column("storeId")
    private String storeId;
    // @Column("storeName")
    private String storeName;
    private String storeUrl;

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public JdProduct() {
    }

    public JdProduct(String pid, String name, String key, String category, Long requestId, Long taskId,
                     Date createTime) {
        super();
        this.pid = pid;
        this.name = name;
        this.key = key;
        this.category = category;
        this.requestId = requestId;
        this.taskId = taskId;
        this.createTime = createTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    // public String getM_price() {
    // return m_price;
    // }
    //
    // public void setM_price(String m_price) {
    // this.m_price = m_price;
    // }

    // public String getStoreId() {
    // return storeId;
    // }
    //
    // public void setStoreId(String storeId) {
    // this.storeId = storeId;
    // }
    //
    // public String getStoreName() {
    // return storeName;
    // }
    //
    // public void setStoreName(String storeName) {
    // this.storeName = storeName;
    // }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice_url() {
        return price_url;
    }

    public void setPrice_url(String priceUrl) {
        price_url = priceUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String marketPrice) {
        market_price = marketPrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPacking_list() {
        return packing_list;
    }

    public void setPacking_list(String packingList) {
        packing_list = packingList;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getGoodRateShow() {
        return goodRateShow;
    }

    public void setGoodRateShow(int goodRateShow) {
        this.goodRateShow = goodRateShow;
    }

    public int getGeneralRateShow() {
        return generalRateShow;
    }

    public void setGeneralRateShow(int generalRateShow) {
        this.generalRateShow = generalRateShow;
    }

    public int getPoorRateShow() {
        return poorRateShow;
    }

    public void setPoorRateShow(int poorRateShow) {
        this.poorRateShow = poorRateShow;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public int getGeneralCount() {
        return generalCount;
    }

    public void setGeneralCount(int generalCount) {
        this.generalCount = generalCount;
    }

    public int getPoorCount() {
        return poorCount;
    }

    public void setPoorCount(int poorCount) {
        this.poorCount = poorCount;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalCommentPage() {
        return totalCommentPage;
    }

    public void setTotalCommentPage(int totalCommentPage) {
        this.totalCommentPage = totalCommentPage;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

}
