package com.reptile.jdfresh.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author eli
 * @date 2017/11/14 16:49
 */
@Table("jd_fresh_citycode")
public class CityCode {
    @Id
    private int id;
    @Column
    private String province;
    @Column
    private String provinceid;
    @Column
    private String city;
    @Column
    private String cityid;
    @Column
    private String area;
    @Column
    private String areaid;
    @Column
    private String lastid;
    @Column
    private String current_areaid;
    @Column
    private int status;

    public String getCurrent_areaid() {
        return current_areaid;
    }

    public void setCurrent_areaid(String current_areaid) {
        this.current_areaid = current_areaid;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getLastid() {
        return lastid;
    }

    public void setLastid(String lastid) {
        this.lastid = lastid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }




}
