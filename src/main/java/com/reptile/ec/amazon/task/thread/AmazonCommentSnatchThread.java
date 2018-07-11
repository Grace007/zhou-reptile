package com.reptile.ec.amazon.task.thread;

import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.amazon.job.AmazonCommentJob;
import com.reptile.ec.amazon.job.AmazonProductJob;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;

import java.util.Date;

public class AmazonCommentSnatchThread extends Thread {
    private static Logger logger = Logger.getLogger(AmazonCommentSnatchThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private AmazonProductJob task;

    public AmazonCommentSnatchThread(AmazonProductJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        tDao.update(AmazonProductJob.class, Chain.make("comment_status", 1), Cnd.where("id", "=", task.getId()));
        logger.info("开始生成>>>>>" + task.getKeyword() + "评论job<<<<<<");
        int comment_total = task.getComment_total();
        String comment_url = "http://www.amazon.cn/product-reviews/" + task.getAsin();
        Document doc = null;
        try {
            String html = HttpBase.get(comment_url, "utf-8").getResult();
            doc = Jsoup.parse(html);
        } catch (Exception e) {
            tDao.update(AmazonProductJob.class, Chain.make("comment_status", 31), Cnd.where("id", "=", task.getId()));
            logger.info(comment_url + "无法连接，将稍后重新连接");
            return;
        }

        String pageTmp = doc.select("div.a-text-center").select("a").text();
        int page = 0;
        String[] pageArray = pageTmp.split(" ");
        try {
            if (pageTmp.contains("下一页")) {
                page = Integer.parseInt(pageArray[pageArray.length - 2]);
            } else {
                page = Integer.parseInt(pageArray[pageArray.length - 1]);
            }
        } catch (Exception e) {
            page = 0;
        }

        if (page == 0 && (task.getComment_total() != 0)) {
            try {
                page = task.getComment_total() / 10 + 1;
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        for (int i = 1; i <= page; i++) {

            AmazonCommentJob amazon = new AmazonCommentJob();

            amazon.setId(task.getId());
            amazon.setTask_id(task.getTask_id());
            amazon.setRequest_id(task.getRequest_id());
            amazon.setNode(task.getNode());
            amazon.setComment_url(comment_url + "?pageNumber=" + i + "&sortBy=recent");
            amazon.setPage_index(i);
            amazon.setKeyword(task.getKeyword());
            amazon.setCreate_date(new Date());
            amazon.setStatus(0);
            amazon.setAsin(task.getAsin());
            amazon.setTitle(task.getTitle());
            amazon.setProject_code(task.getProject_code());
            amazon.setDown_type(task.getDown_type());
            amazon.setNode(task.getNode());
            try {
                tDao.fastInsert(amazon);
            } catch (Exception e) {
                tDao.update(amazon);
            }
            logger.info("###########完成下载评论列表,ASIN:" + task.getAsin() + ",页码:" + i + "###########");
        }
        tDao.update(AmazonProductJob.class, Chain.make("comment_status", 2), Cnd.where("id", "=", task.getId()));
    }
}
