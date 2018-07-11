package com.reptile.ec.jd.task.thread;

import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.jd.common.CommonUtils;
import com.reptile.ec.jd.job.JdKeywordJob;
import com.reptile.ec.jd.job.JdKeywordNextJob;
import com.reptile.ec.jd.model.JdFirstPage;
import com.reptile.ec.jd.model.JdProduct;
import com.reptile.ec.jd.model.JdProductSimple;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdKeywordThread extends Thread {

    HttpBase c = HttpBase.getInstance();
    /**
     * 搜索商品每页显示条数
     */
    private static final int PAGE_COUNT = 5 * 12;
    /**
     * 商品评论每页显示条数
     */

    private static Logger logger = Logger.getLogger(JdKeywordThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private JdKeywordJob task;

    public JdKeywordThread(JdKeywordJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            Dao biDao = BIDataSource.getBiDataSource(task.getWebsite(), task.getProject_code(), "get_product_info")
                    .getBiDao();
            tDao.update(JdKeywordJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));

            Long requestId = task.getRequestId();
            Long taskId = task.getTaskId();
            String key = task.getKeyword();
            String cate = task.getCategory();
            String type = task.getType();
            String project_code = task.getProject_code();
            String website = task.getWebsite();
            String down_type = task.getDown_type();
            String key_url = task.getKeyUrl();

            JdFirstPage jfp = getFirstPage(key, cate, taskId, requestId, key_url);

            int pageTotal = jfp.getPageTotal();
            int count = jfp.getCount();
            if (pageTotal > 100) {
                pageTotal = 100;
            }
            JdProductSimple jps;
            DateFormat DATA_FORMAT = new SimpleDateFormat("yyyyMMdd");

            if (pageTotal >= 1) {
                for (int i = 0; i < pageTotal; i++) {
                    /** 中文推荐评论 */
                    JdKeywordNextJob cct = new JdKeywordNextJob();
                    Date d = new Date();
                    cct.setCreateTime(d);
                    cct.setCreateDate(DATA_FORMAT.format(d));
                    cct.setKeyword(key);
                    cct.setCategory(cate);
                    cct.setNode(task.getNode());

                    cct.setType(type);
                    cct.setProject_code(project_code);
                    cct.setWebsite(website);
                    cct.setDown_type(down_type);

                    cct.setRequestId(requestId);
                    cct.setTaskId(taskId);
                    cct.setUrl(task.getKeyUrl() + "&page=" + 2 * (i + 1));
                    cct.setStatus(0);
                    cct.setPage_index(i + 1);
                    try {
                        tDao.insert(cct);
                    } catch (Exception e) {
                        tDao.update(cct);
                    }

                }
            }

//            if (pageTotal == 1) {
//                List<JdProduct> list = jfp.getProductList();
//                JdProductJob jt;
//                for (JdProduct p : list) {
//                    String weburl = "";
//                    String html = "";
//                    Long comments = 0L;
//                    weburl = p.getUrl();
//                    comments = p.getComments();
//                    try {
//                        html = HttpBase.get(weburl, "utf-8").getResult();
//                    } catch (Exception e1) {
//                        // html = HttpBase.get(weburl.substring(5),
//                        // "utf-8").getResult();
//                        continue;
//                    }
//                    Document document = Jsoup.parse(html);
//                    // // 店铺id
//                    // String storeid = "";
//                    // try {
//                    // storeid = CommonUtils.subStringJd("shopId:'", "'",
//                    // document.select("script").toString());
//                    // } catch (Exception e2) {
//                    // logger.info("店铺id获取失败", e2);
//                    // }
//                    // // 店铺url
//                    // String storeurl = "";
//                    // try {
//                    // storeurl = "http:"
//                    // + CommonUtils.subStringJd("href: '", "'",
//                    // document.select("script").toString());
//                    // } catch (Exception e2) {
//                    // logger.info("店铺url获取失败", e2);
//                    // }
//                    // // 店铺名
//                    // String storename = "";
//                    // try {
//                    // storename =
//                    // document.select(".shopName").select("strong").select("a").text();
//                    // } catch (Exception e1) {
//                    // logger.info("获取店铺名失败", e1);
//                    // }
//                    // if ("".equals(storename)) {
//                    // try {
//                    // storename =
//                    // document.select(".seller-infor").select("a").attr("title");
//                    // } catch (Exception e1) {
//                    // logger.info("获取店铺名失败", e1);
//                    // }
//                    // }
//                    String venderId = "";
//                    String storeid = "";
//                    String storeurl = "";
//                    String storename = "";
//                    Map<String, String> header = new HashMap<String, String>();
//                    header.put("Host", "search.jd.com");
//                    header.put("Cookie", MyConstant.JD_CONFIG.COOKIE);
//                    try {
//                        venderId = CommonUtils.subStringJd("venderId:", ",", document.select("script").toString());
//                    } catch (Exception e2) {
//                        logger.info("venderId获取失败", e2);
//                    }
//                    if (StringUtils.isNotEmpty(venderId)) {
//                        String result = "";
//                        String s_url = "http://c0.3.cn/stock?skuId=" + p.getPid() + "&area=2_2822_51979_0&venderId="
//                                + venderId + "&cat=1318,12099,9757&extraParam=%7B%7D";
//                        try {
//                            result = HttpBase.get(s_url, "gbk", header).getResult();
//                            JSONObject store_info = new JSONObject(result);
//                            storeid = store_info.getJSONObject("stock").getJSONObject("D").getString("id");
//                            storename = store_info.getJSONObject("stock").getJSONObject("D").getString("vender");
//                            storeurl = "http:" + store_info.getJSONObject("stock").getJSONObject("D").getString("url");
//                        } catch (Exception e) {
//                            // 京东自营店铺
//                            try {
//                                result = HttpBase.get(s_url, "gbk", header).getResult();
//                                JSONObject store_info = new JSONObject(result);
//                                storeid = store_info.getJSONObject("stock").getJSONObject("self_D").getString("id");
//                                storename = store_info.getJSONObject("stock").getJSONObject("self_D")
//                                        .getString("vender");
//                                if (!store_info.getJSONObject("stock").getJSONObject("self_D").getString("url")
//                                        .contains("http")) {
//                                    storeurl = "http:" + store_info.getJSONObject("stock").getJSONObject("self_D")
//                                            .getString("url");
//                                } else {
//                                    storeurl = store_info.getJSONObject("stock").getJSONObject("self_D")
//                                            .getString("url");
//                                }
//                            } catch (Exception e1) {
//                                storename = "京东自营(多种配送)";
//                                logger.error("错误URL[" + s_url + "]", e);
//                            }
//                        }
//                    }
//                    jt = new JdProductJob();
//                    jt.setCategory(cate);
//                    jt.setKeyword(key);
//                    jt.setCreateDate(DATA_FORMAT.format(new Date()));
//                    if (comments > 0) {
//                        jt.setComment_status(30);
//                    } else {
//                        jt.setComment_status(2);
//                    }
//                    jt.setNode(task.getNode());
//                    jt.setRequestId(requestId);
//                    jt.setTaskId(taskId);
//                    jt.setStatus(0);
//                    jt.setPid(p.getPid());
//                    jt.setPname(p.getName());
//                    jt.setUrl(p.getUrl());
//                    jt.setType(type);
//                    jt.setProject_code(project_code);
//                    jt.setWebsite(website);
//                    jt.setDown_type(down_type);
//                    jt.setComments(comments);
//                    try {
//                        tDao.insert(jt);
//                    } catch (Exception e) {
//                    }
//
//                    jps = new JdProductSimple();
//                    jps.set(requestId, taskId, key, cate, storeid, storeurl, storename, p.getPid(), p.getName(),
//                            p.getUrl(), p.getPrice(), p.getComments() + "", DATA_FORMAT.format(new Date()), new Date());
//                    // jps.setStoreId(jp.getStoreId());
//                    // jps.setStoreName(jp.getStoreName());
//                    // jps.setStoreUrl(jp.getStoreUrl());
//                    try {
//                        biDao.fastInsert(jps);
//                    } catch (Exception e) {
//                    }
//                }
//            }

            tDao.update(JdKeywordJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            logger.info("|||||||||||||||||||||完成网页搜索|||||||||||||||||||||,id:" + task.getId());
        } catch (Exception e) {
            logger.error("", e);
            tDao.update(JdKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }
    }

    public List<JdProduct> parse(Document doc, String key, String category, Long taskId, Long requestId) {
        List<JdProduct> productList = new ArrayList<JdProduct>();
        Elements elements = doc.select("#J_goodsList").select(".gl-warp").select(".gl-item");
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
                // p.setPrice_url(price_url);
                String oo = e.select("div.p-price").select("i").first().text();
                p.setPrice(oo);
                // p.setComments(comments);
                // logger.info("第" + (i + 1) + "个价格:" + oo);
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
                    // p.setPrice_url(price_url);
                    String oo = e.select(".gl-i-tab-content").select("div.p-price").get(j).select("i").first().text().replace("¥", "").replace("￥", "");
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
        //
        // // tmp = e.select("div.p-price strong img").first();
        // // price_url = tmp.attributes().get("data-lazyload");
        // // price = e.select("div.p-price strong").text();
        //
        // tmp = e.select("div.p-commit a").first();
        // if (tmp.text().contains("万")) {
        // comments = Long.parseLong(
        // String.valueOf(Float.valueOf(tmp.text().replace("万+", "")) *
        // 10000).replace(".0", ""));
        // } else {
        // comments = Long.parseLong(tmp.text().replace("+", ""));
        // }
        // // try {
        // // /** 评论 */
        // // comments = Long.valueOf(tmp.text());
        // // } catch (Exception ee) {
        // // }
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
        // productList.add(p);
        // }
        // 解析XHR
        // String s = CommonUtils.subStringJd("init(", ");",
        // doc.select("script").toString()).split(",")[6];//省份id
        String xhrurl = task.getKeyUrl().replace("search?", "s_new.php?").replace("#J_crumbsBar", "")
                + "&page=2&scrolling=y&pos=30";
        Document document = null;
        try {
            // java.util.Map<String, String> header = new HashMap<String,
            // String>();
            // header.put("Host", "search.jd.com");
            // header.put("Referer", task.getKeyUrl());
            // header.put("Cookie",
            // "user-key=36d5185f-636e-407f-8bd9-f49097ce29e3;
            // TrackID=1ROo-jqdvulYxJzfKBiM6EY2XMcXlzlFQMCdxotSPr0b57CgjxD6KjSVApzRJb7jHrpr0Y-r-lSvMVle8Eoxt4npIk4p9QeToOuUnuIHoOTl2eccIXh6OBzAE2DMH8s9e;
            // pinId=oms4vzn3CyL0U9mivBax9sEbMikaY3KItX37H5_fCPs;
            // pin=%E4%BF%B3%E5%8F%A5%E4%B9%8B%E7%A5%9E%E7%BD%A9%E7%9D%80%E6%88%91%E5%91%90;
            // unick=%E4%BF%B3%E5%8F%A5%E4%B9%8B%E7%A5%9E%E7%BD%A9%E7%9D%80%E6%88%91%E5%91%90;
            // _tp=oYJ7b3qUBlKG%2BC5N6pf9IWj2VFlBXDigLq5m2KW7GVz16CZ3X1WXMKOBoQxI6673hVpVNcKNxKr5%0D%0Ai6jRP4kbyZfLf0GD4NvQDQwr9XMbEE8%3D;
            // _pst=%E4%BF%B3%E5%8F%A5%E4%B9%8B%E7%A5%9E%E7%BD%A9%E7%9D%80%E6%88%91%E5%91%90;
            // cn=0;
            // unpl=V2_ZzNtbRJeQUZ0CEEHc0ldUWJTE15KAEYdIlxPVnkaWwJnVEJaclRCFXIUR1xnGl0UZwQZX0ZcQRBFCHZXchBYAWcCGllyBBNNIEwHDCRSBUE3XHxcFVUWF3RaTwEoSVoAYwtBDkZUFBYhW0IAKElVVTUFR21yVEMldQl2Un4QWAJhBxdecmdEJUU4Q1Z%2bHVsAVwIiXHIVF0l2CUJUehoRA2IKFlpEU0YWRQl2Vw%3d%3d;
            // __jdv=122270672|baidu-pinzhuan|t_288551095_baidupinzhuan|cpc|0f3d30c8dba7459bb52f2eb5eba8ac7d_0_a83b016b9a0e4a029f49fe8332661fa6;
            // areaId=2;
            // __jda=122270672.748567542.1472119550.1474360521.1474439734.14;
            // __jdb=122270672.7.748567542|14.1474439734; __jdc=122270672;
            // thor=AD1C73C186406D599637C5EA6206AA5E9F9B3A7D880041B36355740B4FD55D46625A99BB735D741FA63545AFFD8AF6316378A07984E86E4A6486F367A45A83077C360C973165C889C22B44AD3491CC85F84DA6317DADF7F5EF142C407FC9D9233F78795891D04C5C72DC74F7422F087DA3483405341D8CC45BDD9170DF35D9861EE411939D2FB199858D4A441320680A;
            // xtest=12698.9790.d9798cdf31c02d86b8b81cc119d94836.b7a782741f667201b54880c925faec4b;
            // mx=0_X; rkv=V0700; ipLoc-djd=2-2822-51979-0.138113475;
            // ipLocation=%u4E0A%u6D77; __jdu=748567542");
            // document = Jsoup.parse(HttpBase.get(xhrurl, "utf-8",
            // header).getResult());
            document = Jsoup.parse(HttpBase.get(xhrurl, "utf-8").getResult());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Elements eles = document.select(".gl-item");
        for (Element ele : eles) {
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
            // p.setPrice_url(price_url);
            String oo = ele.select("div.p-price").select("i").first().text();
            p.setPrice(oo);
            // p.setComments(comments);
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

    public JdFirstPage getFirstPage(String key, String category, Long taskId, Long requestId, String k_url) {
        try {
            String content = c.get(getUrl(k_url), "utf-8").getResult();
            Document doc = Jsoup.parse(content);
            /** 解析商品 */
            List<JdProduct> productList = parse(doc, key, category, taskId, requestId);
            Elements elements;
            /** FirstPage */
            String temp = doc.select("#J_resCount").text();
            int pageTotal;
            int total;
            try {
                if (temp.indexOf("万") >= 0) {
                    try {
                        Double d = Double.valueOf(temp.replace("万", "").replace("+", ""));
                        d = d * 10000;
                        total = d.intValue();
                    } catch (Exception e) {
                        total = 0;
                    }

                } else {
                    total = Integer.valueOf(temp.replace("+", "")) - 1;
                }
                pageTotal = (total / PAGE_COUNT + 1);
            } catch (Exception e) {
                // CommonUtils.toFile("E:\\requestId[" + requestId + "] taskId["
                // + taskId + "] key[" + key + "]category[" + category +
                // "].html","url["+url+"]\r\n_url["+_url+"]\r\n"+ content);
                throw e;
            }
            //修改1
            try {
            	pageTotal =Integer.valueOf( doc.select("span.fp-text").select("li").last().text());
			} catch (Exception e) {
				// TODO: handle exception
			}
            //修改1
            String url;
            String URL = null;
            if (pageTotal > 1) {
                int a = k_url.indexOf("?");
                int b = k_url.lastIndexOf("#");
                if (b == -1) {
                    b = k_url.length();
                }

                URL = k_url.substring(0, b) + "&qrst=1&rt=1&stop=1&click=0&page=";

                System.out.println(URL);

            } else {
                url = k_url + "&page=";
            }

            int count = total;
            logger.info("requestId[" + requestId + "] taskId[" + taskId + "] key[" + key + "]category[" + category
                    + "]总共[" + pageTotal + "]页,总[" + count + "]条数据url[" + URL + "]");
            JdFirstPage firstPage = new JdFirstPage(URL, pageTotal, count);
            /** 每页数据 */
            firstPage.setPageCount(PAGE_COUNT);
            /** 当前页码 */
            firstPage.setIndexPage(1);
            /** 关键字 */
            firstPage.setKey(key);
            /** 分类 */
            firstPage.setCategory(category);
            firstPage.setCreateTime(new Date());
            /** 需求ID */
            firstPage.setRequestId(requestId);
            /** 任务ID */
            firstPage.setTaskId(taskId);
            firstPage.setProductList(productList);

            return firstPage;
        } catch (Exception e) {
            String msg = "获取JdFirstPage失败.key[" + key + "],category[" + category + "],taskId[" + taskId + "],requestId["
                    + requestId + "]";
            logger.error(msg, e);
            throw new RuntimeException(msg + ":\n" + e.getMessage(), e);
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

    public static void main(String args[]) throws Exception {
        JdKeywordJob job = new JdKeywordJob();
        job.setKeyUrl(
                "http://search.jd.com/search?keyword=%E8%87%BB%E8%87%B4&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&cid2=1386#J_crumbsBar");
        job.setKeyword("臻致");
        job.setCategory("洗发护发");
        job.setDown_type("get_product_info");
        job.setType("adhoc");
        job.setProject_code("adhoc");
        job.setWebsite("jd");
        JdKeywordThread jkt = new JdKeywordThread(job);
        jkt.run();
    }

}
