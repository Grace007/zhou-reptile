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
 * @date 2017/11/16 10:44
 */
@Table("jd_fresh_comment")
@TableIndexes({@Index(name="unique_comment_info",fields = {"uniqueid"},unique = true)})
public class JdFreshComment extends BaseBI {
    @Column
    private String province;
    @Column
    private String city;
    @Column
    private String uniqueid;
    @Column
    private String nickname;
    @Column
    private String userexp;
    @Column
    private String score;
    @Column
    private String content;
    @Column
    private String referencename;
    @Column
    private String referencetime;
    @Column
    private String pid;
    @Column
    private String shopid;
    @Column
    private String shop;
    @Column
    private String shoptype;
    @Column
    private String category;
    @Column
    private String pname;
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

    public String getNickname() {
        return nickname;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserexp() {
        return userexp;
    }

    public void setUserexp(String userexp) {
        this.userexp = userexp;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReferencename() {
        return referencename;
    }

    public void setReferencename(String referencename) {
        this.referencename = referencename;
    }

    public String getReferencetime() {
        return referencetime;
    }

    public void setReferencetime(String referencetime) {
        this.referencetime = referencetime;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public static void main(String[] args) {
        TaskToBi tobi = BIDataSource.getBiDataSource("fresh","DC_FRESH","get_sort_info");
        Dao bidao= tobi.getBiDao();
        bidao.create(JdFreshComment.class,false);
    }
}
