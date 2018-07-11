package com.reptile.ec.vip2.thread;

import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.vip2.job.VipCategoryUrlJob;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;

public class VipCategoryUrlThread extends Thread {
    private static Logger logger = Logger.getLogger(VipCategoryUrlThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private VipCategoryUrlJob task;

    public VipCategoryUrlThread(VipCategoryUrlJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        tDao.update(VipCategoryUrlJob.class, Chain.make("update_status", 1), Cnd.where("id", "=", task.getId()));
        logger.info("开始更新唯品会URL[" + task.getUrl() + "]信息...");
        String url = task.getUrl();
        if (StringUtils.isNotEmpty(url)) {
            String result = null;
            try {
                result = HttpBase.get(url.replace("|", "%7C"), "utf-8").getResult();
                Document doc = Jsoup.parse(result);
                String keyword = task.getKeyword();
                logger.info(url.replace("|", "%7C"));
                /*if (StringUtils.isEmpty(keyword)) {
                    keyword = doc.select("#J_selected").select(".oper-sec-cnt").text();
                }*/
                String category = task.getCategory();
                /*if (StringUtils.isEmpty(category)) {
                    category = doc.select(".J_bread_nav").text();
                }*/
                int total_page = 0;
                try {
                    total_page = Integer.parseInt(subString("共", "页",
                            doc.select("#J_pagingCt").select(".total-item-nums").text()));
                } catch (Exception e) {
                    total_page = 0;
                }
                tDao.update(VipCategoryUrlJob.class,
                        Chain.make("keyword", keyword).add("category", category).add("current_page", 1)
                                .add("total_page", total_page).add("update_status", 2).add("status", 0).add("level", 0)
                                .add("amount", null),
                        Cnd.where("id", "=", task.getId()));
                logger.info("更新唯品会URL[" + task.getUrl() + "]信息完成,共[" + total_page + "]页");
            } catch (Exception e) {
                logger.error("解析异常", e);
                tDao.update(VipCategoryUrlJob.class, Chain.make("update_status", 3),
                        Cnd.where("id", "=", task.getId()));
            }
        } else {
            tDao.update(VipCategoryUrlJob.class, Chain.make("update_status", -1), Cnd.where("id", "=", task.getId()));
        }
    }

    public static void main(String[] args) {

    }

    public static String subString(String begin, String end, String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        int index_s = 0;
        if (begin != null) {
            index_s = content.indexOf(begin) + begin.length();
        }
        String _end = content.substring(index_s, content.length());
        int index_e = 0;

        if (end == null) {
            index_e = content.length();
        } else {
            index_e = _end.indexOf(end);
        }
        return _end.substring(0, index_e);
    }
}
