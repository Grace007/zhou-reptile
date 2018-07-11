package com.reptile.ec.jd.task.thread;

import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.jd.common.CommonUtils;
import com.reptile.ec.jd.job.JdKeywordNextJob;
import com.reptile.ec.jd.job.JdProductJob;
import com.reptile.ec.jd.job.JdShopInfoResult;
import com.reptile.ec.jd.model.JdProduct;
import com.reptile.ec.jd.model.JdProductSimple;
import com.reptile.server.MyConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
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

public class JdKeywordNextThread extends Thread {

    private static Logger logger = Logger.getLogger(JdKeywordNextThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private JdKeywordNextJob task;

    public JdKeywordNextThread(JdKeywordNextJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            long time = System.currentTimeMillis();
            logger.info("正在下载[" + task.getKeyword() + "],URL:" + task.getUrl() + "...");
            Dao biDao = BIDataSource.getBiDataSource(task.getWebsite(), task.getProject_code(), "get_product_info")
                    .getBiDao();
            tDao.update(JdKeywordNextJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));

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
            String content = HttpBase.get(getUrl(url), "utf8", header).getResult();
            // logger.info("解析url耗时：" + (System.currentTimeMillis() - time));
            // String content = HttpBase.get(url, "utf-8").getResult();
            Document doc = Jsoup.parse(content);
            // System.out.println(doc.toString());
            /** 解析商品 */
            List<JdProduct> productList = parse(doc, key, task.getCategory(), taskId, requestId);
            logger.info("解析列表耗时:" + (System.currentTimeMillis() - time));
            String shop_ids = "";
            List<JdShopInfoResult> shops = null;
            for (JdProduct jdProduct : productList) {
                if (StringUtils.isNotEmpty(jdProduct.getStoreId())) {
                    shop_ids += jdProduct.getStoreId() + ",";
                }
            }
            if (StringUtils.isNotEmpty(shop_ids)) {
                shop_ids = shop_ids.substring(0, shop_ids.length() - 1);
                shops = getJdShopInfo(shop_ids);
            } else {
                // TODO: 2017/4/28
            }
            DateFormat DATA_FORMAT = new SimpleDateFormat("yyyyMMdd");
            JdProductJob jt;
            JdProductSimple jps;

            Date d = new Date();
            int count = 0;
            List<JdProductJob> job = new ArrayList<JdProductJob>();
            List<JdProductSimple> model = new ArrayList<JdProductSimple>();
            for (JdProduct jp : productList) {
                long time1 = System.currentTimeMillis();
                count++;
                //String weburl = "";
                //String html = "";
                //weburl = jp.getUrl();
//                try {
//                    // long t0 = System.currentTimeMillis();
//                    html = HttpBase.get(weburl, "utf-8", header).getResult();
//                    // logger.info("每个产品解析店铺耗时:" + (System.currentTimeMillis() -
//                    // t0));
//                    // html = HttpBase.get(weburl, "utf-8").getResult();
//                } catch (Exception e1) {
//                    continue;
//                    // html = HttpBase.get(weburl.substring(5),
//                    // "utf-8").getResult();
//                }
                //Document document = Jsoup.parse(html);
                // // 店铺id
                // String storeid = "";
                // try {
                // storeid = CommonUtils.subStringJd("shopId:'", "'",
                // document.select("script").toString());
                // } catch (Exception e2) {
                // logger.info("店铺id获取失败", e2);
                // }
                // // 店铺url
                // String storeurl = "";
                // try {
                // storeurl = "http:" + CommonUtils.subStringJd("href: '", "'",
                // document.select("script").toString());
                // } catch (Exception e2) {
                // logger.info("店铺url获取失败", e2);
                // }
                // // 店铺名
                // String storename = "";
                // try {
                // storename =
                // document.select(".shopName").select("strong").select("a").text();
                // } catch (Exception e1) {
                // logger.info("获取店铺名失败", e1);
                // }
                // if ("".equals(storename)) {
                // try {
                // storename =
                // document.select(".seller-infor").select("a").attr("title");
                // } catch (Exception e1) {
                // logger.info("获取店铺名失败", e1);
                // }
                // }
                //String venderId = "";
                String storeid = "";
                String storeurl = "";
                String storename = "";
                if (shops != null  &&  shops.size() > 0) {
                    JdShopInfoResult shop = getShopDetails(jp.getStoreId(), shops);
                    if (shop != null) {
                        storeid = shop.getShop_id();
                        storeurl = shop.getShop_url();
                        storename = shop.getShop_name();
                    }
                }
                if (storename==null) {
					storename="京东自营";	
				}
//                try {
//                    venderId = CommonUtils.subStringJd("venderId:", ",", document.select("script").toString());
//                } catch (Exception e2) {
//                    logger.info("venderId获取失败", e2);
//                }
//                if (StringUtils.isNotEmpty(venderId)) {
//                    String result = "";
//                    String s_url = "http://c0.3.cn/stock?skuId=" + jp.getPid() + "&area=2_2822_51979_0&venderId="
//                            + venderId + "&cat=1318,12099,9757&extraParam=%7B%7D";
//                    try {
//                        result = HttpBase.get(s_url, "gbk", header).getResult();
//                        JSONObject store_info = new JSONObject(result);
//                        storeid = store_info.getJSONObject("stock").getJSONObject("D").getString("id");
//                        storename = store_info.getJSONObject("stock").getJSONObject("D").getString("vender");
//                        storeurl = "http:" + store_info.getJSONObject("stock").getJSONObject("D").getString("url");
//                    } catch (Exception e) {
//                        // 京东自营店铺
//                        try {
//                            result = HttpBase.get(s_url, "gbk", header).getResult();
//                            JSONObject store_info = new JSONObject(result);
//                            storeid = store_info.getJSONObject("stock").getJSONObject("self_D").getString("id");
//                            storename = store_info.getJSONObject("stock").getJSONObject("self_D").getString("vender");
//                            if (!store_info.getJSONObject("stock").getJSONObject("self_D").getString("url")
//                                    .contains("http")) {
//                                storeurl = "http:"
//                                        + store_info.getJSONObject("stock").getJSONObject("self_D").getString("url");
//                            } else {
//                                storeurl = store_info.getJSONObject("stock").getJSONObject("self_D").getString("url");
//                            }
//                        } catch (Exception e1) {
//                            storename = "京东自营(多种配送)";
//                            logger.error("错误URL[" + s_url + "]", e);
//                        }
//                    }
//                }
                jt = new JdProductJob();
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
                job.add(jt);
                long t1 = System.currentTimeMillis();
                try {
                    tDao.insert(jt);
                    // logger.info("成功插入第" + count + "条");
                } catch (Exception e) {
                    // logger.error("插入第" + count + "条失败", e);
                }
                logger.info("插入任务表耗时：" + (System.currentTimeMillis() -
                        t1));

                jps = new JdProductSimple();
                jps.set(requestId, taskId, key, cate, storeid, storeurl, storename, jp.getPid(), jp.getName(),
                        jp.getUrl(), jp.getPrice().replaceAll("￥", ""), jp.getComments() + "", DATA_FORMAT.format(d), d);
                model.add(jps);
                // jps.setStoreId(jp.getStoreId());
                // jps.setStoreName(jp.getStoreName());
                // jps.setStoreUrl(jp.getStoreUrl());
                try {
                    // long t1 = System.currentTimeMillis();
                    //biDao.fastInsert(jps);
                    // logger.info("插入BI表耗时：" + (System.currentTimeMillis() -
                    // t1));
                } catch (Exception e) {
                }
                // logger.info("完成京东第" + count + "个产品抓取，耗时：" +
                // (System.currentTimeMillis() - time1));
            }
            //logger.info("添加产品列表耗时:" + (System.currentTimeMillis() - time));
//            long t1 = System.currentTimeMillis();
//            try {
//
//                tDao.fastInsert(job);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            logger.info("插入任务表耗时：" + (System.currentTimeMillis() -
//                    t1));
            long t2 = System.currentTimeMillis();
            try {
                biDao.fastInsert(model);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            logger.info("插入BI表耗时：" + (System.currentTimeMillis() -
                    t2));

            tDao.update(JdKeywordNextJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            logger.info("|||||||||||||||||||||完成网页搜索|||||||||||||||||||||,id:" + task.getId() + "，总耗时："
                    + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("", e);
            tDao.update(JdKeywordNextJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }
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

            // tmp = e.select("div.p-price strong img").first();
            // price_url = tmp.attributes().get("data-lazyload");
            // price = e.select("div.p-price strong").text();

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
                String shop_id = "";
                //shop_id = e.select("div.p-shop").attr("data-shopid");
                try {
                shop_id = e.select("div.p-shop").select("a").attr("href").replaceAll("\\D", "");
				System.out.println(shop_id);
                } catch (Exception e2) {
					// TODO: handle exception
				}
                tmp = e.select("div.p-name a").first();
                /** 商品名 */
                name = tmp.select("em").text();
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
                p.setStoreId(shop_id);
                // p.setPrice_url(price_url);
                String oo = e.select("div.p-price").select("i").first().text().replace("¥", "");
                p.setPrice(oo);
                // p.setComments(comments);
                // logger.info("第" + (i + 1) + "个价格:" + oo);
                productList.add(p);
            } else {
                // 含有套装
                String shop_id = "";
                shop_id = e.select("div.p-shop").attr("data-shopid");
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
                    name = tmp.select("em").text();
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
                    p.setStoreId(shop_id);
                    // p.setPrice_url(price_url);
                    String oo = e.select(".gl-i-tab-content").select("div.p-price").get(j).select("i").first().text().replace("¥", "");
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
                    // p.setComments(comments);
                    // logger.info("第" + (i + 1) + "个价格:" + oo);
                    productList.add(p);
                }
            }
        }
        // for (int i = 0; i < size; i++) {
        // comments = 0L;
        // score = null;
        // e = elements.get(i);
        // attributes = e.attributes();
        //
        // // tmp = e.select("div.p-price strong img").first();
        // // price_url = tmp.attributes().get("data-lazyload");
        // // price = e.select("div.p-price strong").text();
        //
        // if (StringUtils.isEmpty(e.select(".gl-i-tab-trigger").toString())) {
        // // 无套装
        // /** 商品ID */
        // pid = attributes.get("data-sku");
        // if (StringUtils.isEmpty(pid) &&
        // StringUtils.isNotEmpty(attributes.get("bookid"))) {
        // pid = attributes.get("bookid");
        // }
        // if (StringUtils.isEmpty(pid)) {
        // continue;
        // }
        // tmp = e.select("div.p-name a").first();
        // /** 商品名 */
        // name = tmp.text();
        // /** URL */
        // _url = "http:" + tmp.attributes().get("href");
        // /** 价格图片 */
        // tmp = e.select("div.p-commit a").first();
        // try {
        // /** 评论 */
        // if (tmp.text().contains("万")) {
        // comments = Long.parseLong(
        // String.valueOf(Float.valueOf(tmp.text().replace("万+", "")) *
        // 10000).replace(".0", ""));
        // } else {
        // comments = Long.parseLong(tmp.text().replace("+", ""));
        // }
        // } catch (Exception ee) {
        // }
        // try {
        // /** 评分 */
        // score = CommonUtils.subStringJd("star-yellow h", "\"",
        // e.select("span.star span.star-white").first().html());
        // } catch (Exception eee) {
        // }
        // if ("0".equals(score)) {
        // score = "5";
        // }
        // p = new JdProduct(pid, name, key, category, requestId, taskId, new
        // Date());
        // p.setComments(comments);
        // p.setScore(score);
        // p.setUrl(_url);
        // // p.setPrice_url(price_url);
        // String oo = e.select("div.p-price").text().replace("¥", "");
        // p.setPrice(oo);
        // // p.setComments(comments);
        // // logger.info("第" + (i + 1) + "个价格:" + oo);
        // productList.add(p);
        // } else {
        // // 含有套装
        // for (int j = 0; j <
        // doc.select(".gl-i-tab-trigger").select("span").size(); j++) {
        // /** 商品ID */
        // pid =
        // e.select(".gl-i-tab-content").select("div").get(j).select(".p-price").select("strong")
        // .attr("class").replace("J_", "");
        // if (StringUtils.isEmpty(pid) &&
        // StringUtils.isNotEmpty(attributes.get("bookid"))) {
        // pid = attributes.get("bookid");
        // }
        // if (StringUtils.isEmpty(pid)) {
        // continue;
        // }
        // tmp =
        // e.select(".gl-i-tab-content").select("div").get(j).select("div.p-name
        // a").first();
        // /** 商品名 */
        // name = tmp.text();
        // /** URL */
        // _url = "http:" + tmp.attributes().get("href");
        // /** 价格图片 */
        // tmp =
        // e.select(".gl-i-tab-content").select("div").get(j).select("div.p-commit
        // a").first();
        // try {
        // /** 评论 */
        // if (tmp.text().contains("万")) {
        // comments = Long.parseLong(String
        // .valueOf(Float.valueOf(tmp.text().replace("万+", "")) *
        // 10000).replace(".0", ""));
        // } else {
        // comments = Long.parseLong(tmp.text().replace("+", ""));
        // }
        // } catch (Exception ee) {
        // }
        // try {
        // /** 评分 */
        // score = CommonUtils.subStringJd("star-yellow h", "\"",
        // e.select("span.star span.star-white").first().html());
        // } catch (Exception eee) {
        // }
        // if ("0".equals(score)) {
        // score = "5";
        // }
        // p = new JdProduct(pid, name, key, category, requestId, taskId, new
        // Date());
        // p.setComments(comments);
        // p.setScore(score);
        // p.setUrl(_url);
        // // p.setPrice_url(price_url);
        // String oo = e.select("div.p-price").text().replace("¥", "");
        // p.setPrice(oo);
        // // p.setComments(comments);
        // // logger.info("第" + (i + 1) + "个价格:" + oo);
        // productList.add(p);
        // }
        // }
        // }
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
//			header.put("Cookie", MyConstant.JD_CONFIG.COOKIE);
            header.put("Referer", task.getUrl());
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
            String shop_id = "";
            //shop_id = ele.select("div.p-shop").attr("data-shopid");
            try {
            shop_id = ele.select("div.p-shop").select("a").attr("href").replaceAll("\\D", "");
            System.out.println(shop_id);
            } catch (Exception e2) {
				// TODO: handle exception
			}
            tmp = ele.select("div.p-name a").first();
            /** 商品名 */
            name = tmp.select("em").text();
            /** URL */
            _url = "http:" + tmp.attributes().get("href");
            /** 价格图片 */

            // tmp = e.select("div.p-price strong img").first();
            // price_url = tmp.attributes().get("data-lazyload");
            // price = e.select("div.p-price strong").text();

            tmp = ele.select("div.p-commit a").first();
            try {
                /** 评论 */
                comments = Long.valueOf(tmp.text().replace("+", ""));
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
            p.setStoreId(shop_id);
            // p.setPrice_url(price_url);
            String oo = ele.select("div.p-price").select("i").first().text().replace("¥", "");
            p.setPrice(oo);
            // p.setComments(comments);
            // logger.info("第二组第" + j + "个价格:" + oo);
            productList.add(p);
        }
        /** 下载商品详情 */
        // try {
        // new JdProductInfo().getProductInfo(productList);
        // } catch (Exception e1) {
        // /** TODO:Send Email */
        // logger.error("下载京东商品详情失败", e1);
        // }
        return productList;
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

    public List<JdShopInfoResult> getJdShopInfo(String shop_ids) {
        List<JdShopInfoResult> shops = new ArrayList<JdShopInfoResult>();
        String url = "https://search.jd.com/shop_new.php?ids=" + shop_ids;
        try {
            Map<String, String> header = new HashMap<String, String>();
            header.put("Host", "search.jd.com");
            header.put("Referer", task.getUrl());
            String result = HttpBase.get(url, "utf-8", header).getResult();
            JSONArray s = new JSONArray(result);
            for (int i = 0; i < s.length(); i++) {
                JdShopInfoResult r = new JdShopInfoResult();
                r.setShop_id(s.getJSONObject(i).getString("shop_id"));
                r.setShop_name(s.getJSONObject(i).getString("shop_name"));
                r.setShop_url("http://mall.jd.com/index-" + s.getJSONObject(i).getString("shop_id") + ".html");
                shops.add(r);
            }
        } catch (Exception e) {
            logger.error("获取店铺详细信息异常");
        }
        return shops;
    }

    public JdShopInfoResult getShopDetails(String shop_id, List<JdShopInfoResult> shops) {
        JdShopInfoResult detail = new JdShopInfoResult();
        for (JdShopInfoResult shop : shops) {
            if (shop.getShop_id().equals(shop_id)) {
                detail.setShop_id(shop.getShop_id());
                detail.setShop_url(shop.getShop_url());
                detail.setShop_name(shop.getShop_name());
                break;
            }
        }
        return detail;
    }

}
