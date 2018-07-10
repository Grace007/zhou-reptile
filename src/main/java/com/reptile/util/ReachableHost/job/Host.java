package com.reptile.util.ReachableHost.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * Created by lex on 2017/5/22.
 */
@Table("host")
public class Host {
    @Id(auto = true)
    private long id;
    @Column
    private String host;
    @Column
    private String port;
    @Column
    private String node;
    @Column
    private String description;
    @Column
    private int retry_count;
    @Column
    private int reachable;
    @Column
    private Date create_date;
    @Column
    private Date create_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRetry_count() {
        return retry_count;
    }

    public void setRetry_count(int retry_count) {
        this.retry_count = retry_count;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public int getReachable() {
        return reachable;
    }

    public void setReachable(int reachable) {
        this.reachable = reachable;
    }
}
