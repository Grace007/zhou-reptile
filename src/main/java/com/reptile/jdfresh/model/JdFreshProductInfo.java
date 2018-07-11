package com.reptile.jdfresh.model;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.reptile.jdfresh.common.BaseBI;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * @author eli
 * @date 2017/11/15 18:11
 */
@Table("jd_fresh_product_info")
@TableIndexes({@Index(name="unique_product_info",fields = {"request_id","task_id","province","city","pid"},unique = true)})
public class JdFreshProductInfo extends BaseBI {
    @Column
    private String province;
    @Column
    private String city;
    @Column
    private String pname;
    @Column
    private String category;
    @Column
    private String price;
    @Column
    private String vip_price;
    @Column
    private String basic_price;
    @Column
    private String pid;
    @Column
    private String shopid;
    @Column
    private String shop;
    @Column
    private String shoptype;
    @Column
    private String url;
    @Column
    private String cat;
    @Column
    private String directory;
    @Column
    private String coupon;
    @Column
    private String promotions;
    @Column
    private String specifications;
    @Column
    private String weight;
    @Column
    private String delivery;
    @Column
    private String goodRateShow;
    @Column
    private String introduction;
    @Column
    private String commentCount;
    @Column
    private String hotCommentTag;
    @Column
    private String productCommentSummary;
    @Column
    private String district;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getVip_price() {
        return vip_price;
    }

    public void setVip_price(String vip_price) {
        this.vip_price = vip_price;
    }

    public String getBasic_price() {
        return basic_price;
    }

    public void setBasic_price(String basic_price) {
        this.basic_price = basic_price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getShoptype() {
        return shoptype;
    }

    public void setShoptype(String shoptype) {
        this.shoptype = shoptype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getPromotions() {
        return promotions;
    }

    public void setPromotions(String promotions) {
        this.promotions = promotions;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getGoodRateShow() {
        return goodRateShow;
    }

    public void setGoodRateShow(String goodRateShow) {
        this.goodRateShow = goodRateShow;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getHotCommentTag() {
        return hotCommentTag;
    }

    public void setHotCommentTag(String hotCommentTag) {
        this.hotCommentTag = hotCommentTag;
    }

    public String getProductCommentSummary() {
        return productCommentSummary;
    }

    public void setProductCommentSummary(String productCommentSummary) {
        this.productCommentSummary = productCommentSummary;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public static void main(String[] args) {
        TaskToBi tobi = BIDataSource.getBiDataSource("fresh","DC_FRESH","get_sort_info");
        Dao bidao= tobi.getBiDao();
        bidao.create(JdFreshProductInfo.class,false);
    }
}
