package com.reptile.tianya.task.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.tianya.common.CommonUtils;
import com.reptile.tianya.job.TianyaPostJob;
import com.reptile.tianya.model.TianYaPostInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TianyaPostThread extends Thread {
    private static Logger logger = Logger.getLogger(TianyaPostThread.class);
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private TianyaPostJob task;

    public TianyaPostThread(TianyaPostJob task) {
        this.task = task;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    @Override
    public void run() {
        logger.info("正在抓取关键词[" + task.getKeyword() + "]的URL:" + task.getPost_url() + "...");
        tDao.update(TianyaPostJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        TaskToBi tobi = BIDataSource.getBiDataSource("tianya", task.getProject_code(), task.getDown_type());
        Dao biDao = tobi.getBiDao();
        String url = task.getPost_url();
        if (StringUtils.isNotEmpty(url)) {
            String html;
            String info;
            String type = null;
            String pid = null;
            try {
                info = CommonUtils.subStringJd("http://bbs.tianya.cn/post-", ".shtml", url);
                type = info.split("-")[0];
                pid = info.split("-")[1];
            } catch (Exception e3) {
                logger.error("", e3);
            }
            try {
                html = HttpBase.get(url, "utf-8").getResult();
                Document doc = Jsoup.parse(html);
                if (doc.toString().contains("很抱歉， 您没有权限查看该内容")) {
                    tDao.update(TianyaPostJob.class, Chain.make("status", 4), Cnd.where("id", "=", task.getId()));
                    return;
                }
                if (StringUtils.isNotEmpty(doc.toString())) {
                    // 公共信息
                    String article_url = url;
                    // String engine = url.replace("http://", "");
                    String engine = "bbs.tianya.cn";
                    String forum = "";
                    String title = task.getPost_name();
                    String subtitle = "";
                    String thread_id = doc.select(".atl-menu").attr("js_postid");
                    String source_url = "http://bbs.tianya.cn/";
                    Date date = new Date();
                    int total_page = 0;// 初始页
                    int current_reply_page = 1;// 当前楼层页数
                    try {
                        current_reply_page = task.getReply_page();
                    } catch (Exception e3) {

                    }
                    try {
                        total_page = Integer.parseInt(CommonUtils
                                .subStringJd("goPage(", ");",
                                        doc.select("#post_head").select(".atl-pages").select("form").attr("onsubmit"))
                                .split(",")[3]);
                    } catch (Exception e2) {
                        total_page = 1;
                    }
                    if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(pid)) {
                        int count = 0;
                        for (int i = current_reply_page; i <= total_page; i++) {
                            String next_post_url = "http://bbs.tianya.cn/post-" + type + "-" + pid + "-" + i + ".shtml";
                            html = HttpBase.get(next_post_url, "utf-8").getResult();
                            Document document = Jsoup.parse(html);
                            if (StringUtils.isNotEmpty(document.select(".host-item").toString())) {
                                TianYaPostInfo model = new TianYaPostInfo();
                                // 楼主
                                String author_name = task.getAuthor();
                                String author_url = task.getAuthor_url();
                                String date_posted = task.getPost_time();
                                String post_id = thread_id + "_host" + "_"
                                        + document.select(".host-item").attr("_host");
                                String message = document.select(".host-item").select(".bbs-content").text();
                                String message_id = article_url + thread_id + post_id;
                                model.setArticle_url(article_url);
                                model.setEngine(engine);
                                model.setForum(forum);
                                model.setTitle(title);
                                model.setThread_id(thread_id);
                                model.setAuthor_name(author_name);
                                model.setAuthor_url(author_url);
                                try {
                                    model.setDate_posted(sdf.parse(date_posted));
                                } catch (Exception e1) {

                                }
                                model.setPost_id(post_id);
                                model.setSubtitle(subtitle);
                                model.setMessage(message);
                                model.setMessage_id(message_id);
                                model.setSource_url(source_url);
                                model.setIngestdate(date);
                                model.setRequest_id(task.getRequest_id());
                                model.setTask_id(task.getTask_id());
                                model.setKeyword(task.getKeyword());
                                try {
                                    biDao.insert(model);
                                } catch (Exception e) {

                                }
                            }
                            // 层主
                            Elements replys = document.select(".atl-item");
                            for (Element reply : replys) {
                                // 只跳过第一页的楼主
                                count++;
                                if ((count == 1) && (current_reply_page == 1)) {
                                    continue;
                                }
                                TianYaPostInfo model = new TianYaPostInfo();
                                String author_name = reply.select(".atl-info").select("a").text();
                                String author_url = reply.select(".atl-info").select("a").attr("href");
                                String date_posted;
                                try {
                                    date_posted = reply.select(".atl-info").select("span").get(1).text().replace("时间：",
                                            "");
                                } catch (Exception e) {
                                    date_posted = "";
                                }
                                String post_id = thread_id + "_" + reply.attr("replyid") + "_" + reply.attr("id");
                                String message = reply.select(".bbs-content").text();
                                String message_id = article_url + thread_id + post_id;
                                model.setArticle_url(article_url);
                                model.setEngine(engine);
                                model.setForum(forum);
                                model.setTitle(title);
                                model.setThread_id(thread_id);
                                model.setAuthor_name(author_name);
                                model.setAuthor_url(author_url);
                                try {
                                    model.setDate_posted(sdf.parse(date_posted));
                                } catch (Exception e1) {

                                }
                                model.setPost_id(post_id);
                                model.setSubtitle(subtitle);
                                model.setMessage(message);
                                model.setMessage_id(message_id);
                                model.setSource_url(source_url);
                                model.setIngestdate(date);
                                model.setRequest_id(task.getRequest_id());
                                model.setTask_id(task.getTask_id());
                                model.setKeyword(task.getKeyword());
                                try {
                                    biDao.insert(model);
                                } catch (Exception e) {

                                }
                                tDao.update(TianyaPostJob.class, Chain.make("reply_page", i),
                                        Cnd.where("id", "=", task.getId()));
                            }
                            logger.info("抓取关键词[" + task.getKeyword() + "]的URL:" + next_post_url + "的第" + i + "页完成");
                        }
                    }

                }
            } catch (Exception e) {
                logger.error("", e);
                tDao.update(TianyaPostJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            }
            logger.info("抓取关键词[" + task.getKeyword() + "]的URL:" + task.getPost_url() + "完成");
            tDao.update(TianyaPostJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
        }
    }

    public static void main(String[] args) {
        TianyaPostJob task = new TianyaPostJob();
        task.setPost_url("http://bbs.tianya.cn/post-funinfo-7271434-1.shtml");
        TianyaPostThread test = new TianyaPostThread(task);
        test.start();
    }
}
