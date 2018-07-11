package com.reptile.ec.jd.task.thread;

import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.jd.common.CommonUtils;
import com.reptile.ec.jd.job.JdKeywordNextJob;
import com.reptile.ec.jd.job.JdProductJob;
import com.reptile.ec.jd.model.JdProduct;
import com.reptile.ec.jd.model.JdProductSimple;
import com.reptile.server.MyConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JdKeywordNextThreadNew extends Thread {
    private static Logger logger = Logger.getLogger(JdKeywordNextThreadNew.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private JdKeywordNextJob task;

    public JdKeywordNextThreadNew(JdKeywordNextJob task) {
        this.task = task;
    }

    DateFormat DATA_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public void run() {
        long time = System.currentTimeMillis();
        logger.info("正在下载[" + task.getKeyword() + "],URL:" + task.getUrl() + "...");
        Dao biDao = BIDataSource.getBiDataSource(task.getWebsite(), task.getProject_code(), "get_product_info")
                .getBiDao();
        logger.info("连接BI库耗时：" + (System.currentTimeMillis() - time));
        tDao.update(JdKeywordNextJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        logger.info("更新任务状态耗时：" + (System.currentTimeMillis() - time));
        Long requestId = task.getRequestId();
        Long taskId = task.getTaskId();
        String key = task.getKeyword();
        String cate = task.getCategory();
        String url = task.getUrl();
        int index = task.getPage_index();
        String type = task.getType();
        String project_code = task.getProject_code();
        String website = task.getWebsite();
        String down_type = task.getDown_type();

        Map<String, String> header = new HashMap<String, String>();
        header.put("Host", "search.jd.com");
        header.put("Cookie", MyConstant.JD_CONFIG.COOKIE);
        try {
            long time2 = System.currentTimeMillis();
            String content = HttpBase.get(getUrl(url), "utf8", header).getResult();
            Document doc = Jsoup.parse(content);
            List<JdProduct> productList = parse(doc, key, task.getCategory(), taskId, requestId);
            logger.info("解析列表耗时:" + (System.currentTimeMillis() - time2));
            Date d = new Date();
            for (JdProduct jp : productList) {
                long time1 = System.currentTimeMillis();
                String storeid = null;
                String storeurl = null;
                String storename = null;
                JdProductJob jt = new JdProductJob();
                jt.setCategory(cate);
                jt.setKeyword(key);
                jt.setCreateDate(DATA_FORMAT.format(d));
                if (jp.getComments() > 0) {
                    jt.setComment_status(30);
                } else {
                    jt.setComment_status(2);
                }
                jt.setNode(task.getNode());
                jt.setRequestId(requestId);
                jt.setTaskId(taskId);
                jt.setStatus(0);
                jt.setCommentsPage(1);
                jt.setPid(jp.getPid());
                jt.setPname(jp.getName());
                jt.setUrl(jp.getUrl());
                jt.setComments(jp.getComments());
                jt.setUpdated_comments(0L);
                jt.setType(type);
                jt.setProject_code(project_code);
                jt.setWebsite(website);
                jt.setDown_type(down_type);
                long time3 = System.currentTimeMillis();
                try {
                    tDao.insert(jt);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                logger.info("任务库插入耗时：" + (System.currentTimeMillis() - time3));

                JdProductSimple jps = new JdProductSimple();
                jps.set(requestId, taskId, key, cate, storeid, storeurl, storename, jp.getPid(), jp.getName(),
                        jp.getUrl(), jp.getPrice(), jp.getComments() + "", DATA_FORMAT.format(d), d);
                long time4 = System.currentTimeMillis();
                try {
                    biDao.insert(jps);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                logger.info("BI插入耗时：" + (System.currentTimeMillis() - time4));
                //logger.info("该条数据插入耗时：" + (System.currentTimeMillis() - time1));
            }
            tDao.update(JdKeywordNextJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            logger.info("|||||||||||||||||||||完成网页搜索|||||||||||||||||||||,id:" + task.getId() + "，总耗时："
                    + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("", e);
            tDao.update(JdKeywordNextJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }
    }

    public static void main(String[] args) {

    }

    public static String getUrl(String url) throws Exception {
        String s = CommonUtils.subStringJd("keyword=", "&", url);
        char[] cs = s.toCharArray();
        StringBuffer new_s = new StringBuffer();
        for (char c : cs) {
            if (isChineseChar(c + "")) {
                new_s.append(java.net.URLEncoder.encode(c + "", "utf8"));
            } else {
                new_s.append(c);
            }
        }
        url = url.replaceAll(s, new_s.toString());
        return url;
    }

    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    public List<JdProduct> parse(Document doc, String key, String category, Long taskId, Long requestId) {
        List<JdProduct> productList = new ArrayList<JdProduct>();
        Elements elements = doc.select("#J_goodsList>ul>li");
        int size = elements.size();
        JdProduct p;
        String pid;
        String name;
        String _url;
        Element e;
        Element tmp;
        String price_url;
        String price;
        Long comments = 0L;
        Attributes attributes;
        String score;
        for (int i = 0; i < size; i++) {
            comments = 0L;
            score = null;
            e = elements.get(i);
            attributes = e.attributes();

            if (StringUtils.isEmpty(e.select(".gl-i-tab-trigger").toString())) {
                // 无套装
                /** 商品ID */
                pid = attributes.get("data-sku");
                if (StringUtils.isEmpty(pid) && StringUtils.isNotEmpty(attributes.get("bookid"))) {
                    pid = attributes.get("bookid");
                }
                if (StringUtils.isEmpty(pid)) {
                    continue;
                }
                tmp = e.select("div.p-name a").first();
                /** 商品名 */
                name = tmp.text();
                /** URL */
                _url = "http:" + tmp.attributes().get("href");
                /** 价格图片 */
                tmp = e.select("div.p-commit a").first();
                try {
                    /** 评论 */
                    if (tmp.text().contains("万")) {
                        comments = Long.parseLong(
                                String.valueOf(Float.valueOf(tmp.text().replace("万+", "")) * 10000).replace(".0", ""));
                    } else {
                        comments = Long.parseLong(tmp.text().replace("+", ""));
                    }
                } catch (Exception ee) {
                }
                try {
                    /** 评分 */
                    score = CommonUtils.subStringJd("star-yellow h", "\"",
                            e.select("span.star span.star-white").first().html());
                } catch (Exception eee) {
                }
                if ("0".equals(score)) {
                    score = "5";
                }
                p = new JdProduct(pid, name, key, category, requestId, taskId, new Date());
                p.setComments(comments);
                p.setScore(score);
                p.setUrl(_url);
                String oo = e.select("div.p-price").text().replace("¥", "");
                p.setPrice(oo);
                productList.add(p);
            } else {
                // 含有套装
                for (int j = 0; j < e.select(".gl-i-tab-trigger span").size(); j++) {
                    /** 商品ID */
                    pid = e.select(".gl-i-tab-content").select(".p-price").get(j).select("strong").attr("class")
                            .replace("J_", "");
                    if (StringUtils.isEmpty(pid) && StringUtils.isNotEmpty(attributes.get("bookid"))) {
                        pid = attributes.get("bookid");
                    }
                    if (StringUtils.isEmpty(pid)) {
                        continue;
                    }
                    tmp = e.select(".gl-i-tab-content").select("div.p-name").get(j).select("a").first();
                    /** 商品名 */
                    name = tmp.text();
                    /** URL */
                    _url = "http:" + tmp.attributes().get("href");
                    /** 价格图片 */
                    tmp = e.select(".gl-i-tab-content").select("div.p-commit").get(j).select("a").first();
                    try {
                        /** 评论 */
                        if (tmp.text().contains("万")) {
                            comments = Long.parseLong(String
                                    .valueOf(Float.valueOf(tmp.text().replace("万+", "")) * 10000).replace(".0", ""));
                        } else {
                            comments = Long.parseLong(tmp.text().replace("+", ""));
                        }
                    } catch (Exception ee) {
                    }
                    try {
                        /** 评分 */
                        score = CommonUtils.subStringJd("star-yellow h", "\"",
                                e.select("span.star span.star-white").first().html());
                    } catch (Exception eee) {
                    }
                    if ("0".equals(score)) {
                        score = "5";
                    }
                    p = new JdProduct(pid, name, key, category, requestId, taskId, new Date());
                    p.setComments(comments);
                    p.setScore(score);
                    p.setUrl(_url);
                    // p.setPrice_url(price_url);
                    String oo = e.select(".gl-i-tab-content").select("div.p-price").get(j).text().replace("¥", "");
                    if (StringUtils.isEmpty(oo) && StringUtils.isNotEmpty(pid)) {
                        String p_url = "https://p.3.cn/prices/mgets?type=1&skuIds=J_" + pid;
                        try {
                            String content = HttpBase.get(p_url, "utf-8").getResult();
                            oo = new JSONObject(content.replace("[", "").replace("]", "")).getString("op");
                        } catch (Exception e1) {
                            logger.error("", e1);
                        }
                    }
                    p.setPrice(oo);
                    productList.add(p);
                }
            }
        }

        // 解析XHR
        String s = "";
        try {
            s = CommonUtils.subStringJd("s.init(", ");", doc.select("script").toString()).split(",")[6];
        } catch (Exception e2) {
            logger.error("省份ID获取失败", e2);
        }
        // String xhrurl = task.getUrl().replace("Search?",
        // "s_new.php?").replace("#J_crumbsBar", "") + "&page="
        // + 2 * task.getPage_index() + "&scrolling=y&pos=30&s=" + s;
        String xhrurl = "http://search.jd.com/s_new.php?" + task.getUrl().split("\\?")[1] + "&scrolling=y&s=" + s;
        Document document = null;
        try {
            Map<String, String> header = new HashMap<String, String>();
            header.put("Host", "search.jd.com");
            header.put("Cookie", MyConstant.JD_CONFIG.COOKIE);
            document = Jsoup.parse(HttpBase.get(getUrl(xhrurl), "utf8", header).getResult());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Elements eles = document.select(".gl-item");
        int j = 0;
        for (Element ele : eles) {
            j++;
            comments = 0L;
            score = null;
            attributes = ele.attributes();
            /** 商品ID */
            pid = attributes.get("data-sku");
            if (StringUtils.isEmpty(pid) && StringUtils.isNotEmpty(attributes.get("bookid"))) {
                pid = attributes.get("bookid");
            }
            if (StringUtils.isEmpty(pid)) {
                continue;
            }
            tmp = ele.select("div.p-name a").first();
            /** 商品名 */
            name = tmp.text();
            /** URL */
            _url = "http:" + tmp.attributes().get("href");
            /** 价格图片 */
            tmp = ele.select("div.p-commit a").first();
            try {
                /** 评论 */
                comments = Long.valueOf(tmp.text());
            } catch (Exception ee) {
            }
            try {
                /** 评分 */
                score = CommonUtils.subStringJd("star-yellow h", "\"",
                        ele.select("span.star span.star-white").first().html());
            } catch (Exception eee) {
            }
            if ("0".equals(score)) {
                score = "5";
            }
            p = new JdProduct(pid, name, key, category, requestId, taskId, new Date());
            p.setComments(comments);
            p.setScore(score);
            p.setUrl(_url);
            String oo = ele.select("div.p-price").text().replace("¥", "");
            p.setPrice(oo);
            productList.add(p);
        }
        return productList;
    }
}
