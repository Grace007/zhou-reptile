package com.reptile.mobile_eleme.job;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by lex on 2017/4/20.
 */
@Table("mobile_eleme_category")
public class ElemeCategoryFixed {
    @Id
    private long id;
    @Column
    private String category;
    @Column
    private String sub_category;
    @Column
    private String category_id;

    public long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
