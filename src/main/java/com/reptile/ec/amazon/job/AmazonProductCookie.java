package com.reptile.ec.amazon.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by lex on 2017/2/20.
 */
@Table("amazon_product_cookie")
public class AmazonProductCookie {
    @Id
	private int id;
	@Column
	private String cookie;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
