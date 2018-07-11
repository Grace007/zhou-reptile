package com.reptile.tianya.task.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.tianya.common.CommonUtils;
import com.reptile.tianya.job.TianyaKeywordJob;
import com.reptile.tianya.job.TianyaPostJob;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TianyaKeywordThread extends Thread {
    private static Logger logger = Logger.getLogger(TianyaKeywordThread.class);
    //	private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private TianyaKeywordJob task;

    public TianyaKeywordThread(TianyaKeywordJob task) {
        this.task = task;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run() {
        logger.info("正在抓取关键词[" + task.getKeyword() + "]的post...");
        tDao.update(TianyaKeywordJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        String keyword = task.getKeyword();
        try {
            keyword = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e1) {

        }
        if (StringUtils.isNotEmpty(keyword)) {
            String first_page_url = "http://search.tianya.cn/bbs?q=" + keyword + "&pn=1";
            String html;
            try {
                html = HttpBase.get(first_page_url, "utf-8").getResult();
                Document doc = Jsoup.parse(html);
                int total = 0;
                try {
                    total = Integer.parseInt(
                            CommonUtils.subStringJd("共有", " 条内容", doc.select(".long-pages").select("em").text()));
                } catch (Exception e1) {
                    total = 0;
                }
                int total_page = 0;
                total_page = CommonUtils.calculatePage(total, 10, 75);
                String web_url = "http://search.tianya.cn/bbs?q=" + keyword + "&pn=";
                for (int i = 0; i < total_page; i++) {
                    html = HttpBase.get(web_url + (i + 1), "utf-8").getResult();
                    Document document = Jsoup.parse(html);
                    Elements postList = document.select(".searchListOne").select("ul").select("li");
                    int count = 0;
                    for (Element post : postList) {
                        count++;
                        if (count == postList.size()) {
                            break;
                        }
                        TianyaPostJob postJob = new TianyaPostJob();
                        // url
                        String post_url = post.select("div").select("a").attr("href");
                        // post_name
                        String post_name = post.select("div").select("a").text();
                        // source
                        String source = "";
                        try {
                            source = post.select(".source").select("a").get(0).text();
                        } catch (Exception e) {

                        }
                        // author
                        String author = "";
                        try {
                            author = post.select(".source").select("a").get(1).text();
                        } catch (Exception e) {

                        }
                        // author_url
                        String author_url = "";
                        try {
                            author_url = post.select(".source").select("a").get(1).attr("href");
                        } catch (Exception e) {

                        }
                        // time
                        String time = "";
                        try {
                            time = post.select(".source").select("span").get(0).text();
                        } catch (Exception e) {

                        }
                        // reply
                        String reply = "";
                        try {
                            reply = post.select(".source").select("span").get(1).text();
                        } catch (Exception e) {

                        }
                        postJob.setRequest_id(task.getRequest_id());
                        postJob.setTask_id(task.getTask_id());
                        postJob.setKeyword(task.getKeyword());
                        postJob.setPost_name(post_name);
                        postJob.setPost_url(post_url);
                        postJob.setSource(source);
                        postJob.setAuthor(author);
                        postJob.setAuthor_url(author_url);
                        postJob.setPost_time(time);
                        postJob.setReply(reply);
                        postJob.setReply_page(1);
                        postJob.setPage_number((i + 1));
                        postJob.setTotal_page(total_page);
                        postJob.setStatus(0);
                        postJob.setCreate_date(new Date());
                        postJob.setCreate_time(new Date());
                        postJob.setProject_code(task.getProject_code());
                        postJob.setDown_type(task.getDown_type());
                        postJob.setNode(task.getNode());
                        try {
                            if ((sdf.parse(time).after(task.getDate_from()) && (sdf.parse(time).before(task.getDate_to())))) {
                                tDao.insert(postJob);
                            }
                        } catch (Exception e) {

                        }
                    }
                    // logger.info("完成关键词[" + task.getKeyword() + "]的post第" + (i
                    // + 1) + "页抓取");
                }
                logger.info("完成关键词[" + task.getKeyword() + "]的post抓取");
                tDao.update(TianyaKeywordJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            } catch (Exception e) {
                logger.error("", e);
                tDao.update(TianyaKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            }
        }
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String d = "2011-06-02 23:36";
        try {
            System.out.print(sdf.format(sdf.parse(d)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TianyaKeywordJob task = new TianyaKeywordJob();
        task.setKeyword("马蓉");
        TianyaKeywordThread test = new TianyaKeywordThread(task);
        test.start();
    }
}
