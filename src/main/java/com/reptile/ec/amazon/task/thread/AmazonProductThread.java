package com.reptile.ec.amazon.task.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.CommonUtils;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.amazon.job.AmazonProductCookie;
import com.reptile.ec.amazon.job.AmazonProductJob;
import com.reptile.ec.amazon.model.AmazonProductModel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AmazonProductThread extends Thread {
    private static Logger logger = Logger.getLogger(AmazonProductThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private AmazonProductJob task;

    public AmazonProductThread(AmazonProductJob task) {
        this.task = task;
    }

    DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run() {
        tDao.update(AmazonProductJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        logger.info("开始下载>>>>>" + task.getKeyword() + "<<<<<<");
        TaskToBi tobi = BIDataSource.getBiDataSource("amazon", task.getProject_code(), task.getDown_type());
        Dao biDao = tobi.getBiDao();
        String html = "";
        try {
            Map<String, String> header = new HashMap<String, String>();
            String cookie = "";
            try {
                cookie = tDao.fetch(AmazonProductCookie.class).getCookie();
                // header.put("Cookie", cookie);
                // header.put("Host", "www.amazon.cn");
                // header.put("", "");// 带上一个参数就能正常请求，甚至空的都可以
            } catch (Exception e) {
                logger.error("请添加amazonCookie");
                tDao.update(AmazonProductJob.class, Chain.make("status", 32), Cnd.where("id", "=", task.getId()));
                return;
            }
            // html = HttpBase.get(task.getPage_url(), "utf-8").getResult();
            html = HttpBase.get(task.getPage_url(), "utf-8", header).getResult();
            if (html.contains("请输入您在下方看到的字符")) {
                // header.put("", "");
                html = HttpBase.get(task.getPage_url(), "utf-8", header).getResult();
                if (html.contains("请输入您在下方看到的字符")) {
                    header.put("Host", "www.amazon.cn");
                    html = HttpBase.get(task.getPage_url(), "utf-8", header).getResult();
                    if (html.contains("请输入您在下方看到的字符")) {
                        header.put("Cookie", cookie);
                        html = HttpBase.get(task.getPage_url(), "utf-8", header).getResult();
                        if (html.contains("请输入您在下方看到的字符")) {
                            logger.info("amazonCookie已失效，请更换 ");
                            tDao.update(AmazonProductJob.class, Chain.make("status", 33),
                                    Cnd.where("id", "=", task.getId()));
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info("url为空");
        }
        Document doc = null;
        try {
            doc = Jsoup.parse(html);
            if (doc.toString().contains("您在寻找什么商品吗")) {
                tDao.update(AmazonProductJob.class, Chain.make("status", -31), Cnd.where("id", "=", task.getId()));
                logger.info(task.getPage_url() + "下架");
                return;
            }
        } catch (Exception e) {
            tDao.update(AmazonProductJob.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
            logger.info(task.getPage_url() + "无法连接，将稍后重新连接");
            return;
        }
        String title = "", brand = "", to_name = "", to_price = "", youSaveValue = "", salesRank = "0", navigation = "",
                score = "", asin = "";
        double price = -1, m_price = -1, specialPrice = -1;
        int sku_number = 1, comment_num = 0;

        try {
            if (StringUtils.isNotEmpty(doc.select("#variation_style_name").text())) {
                sku_number = doc.select("#variation_style_name").select("ul").select("li").size();
            } else if (StringUtils.isNotEmpty(doc.select("#variation_color_name").text())) {
                sku_number = doc.select("#variation_color_name").select("ul").select("li").size();
            } else if (StringUtils.isNotEmpty(doc.select("#variation_size_name").text())) {
                sku_number = doc.select("#variation_size_name").select("ul").select("li").size();
            } else if (StringUtils.isNotEmpty(doc.select("#variation_scent_name").text())) {
                sku_number = doc.select("#variation_scent_name").select("ul").select("li").size();
            }
            if (sku_number == 0) {
                sku_number = 1;
            }
        } catch (Exception e2) {

        }

        try {
            title = doc.getElementById("productTitle").text();
        } catch (Exception e1) {

        }
        if (StringUtils.isEmpty(title)) {
            title = task.getTitle();
        }
        try {
            brand = doc.select("#brandByline_feature_div").text();
        } catch (Exception e) {

        }
        if (StringUtils.isNotEmpty(doc.select(".a-lineitem").text())) {
            try {
                if (doc.select(".a-lineitem").select("tbody").select("tr").get(0).text().contains("厂商建议零售价")) {
                    try {
                        m_price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(0)
                                .select(".a-span12").text().replace("￥", "").replace(",", ""));
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e1) {

            }
            try {
                if (doc.select(".a-lineitem").select("tbody").select("tr").get(0).text().contains("售价")) {
                    try {
                        price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(0)
                                .select("#priceblock_ourprice").text().replace("￥", "").replace(",", ""));
                    } catch (Exception e) {

                    }
                    if (price == -1) {
                        try {
                            price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(0)
                                    .select("#priceblock_saleprice").text().replace("￥", "").replace(",", ""));
                        } catch (Exception e) {

                        }
                    }
                }
            } catch (Exception e1) {

            }
            try {
                if (doc.select(".a-lineitem").select("tbody").select("tr").get(1).text().contains("售价")) {
                    try {
                        price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(1)
                                .select("#priceblock_ourprice").text().replace("￥", "").replace(",", ""));
                    } catch (Exception e) {

                    }
                    if (price == -1) {
                        try {
                            price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(1)
                                    .select("#priceblock_saleprice").text().replace("￥", "").replace(",", ""));
                        } catch (Exception e) {

                        }
                    }
                }
            } catch (Exception e1) {

            }
            try {
                if (doc.select(".a-lineitem").select("tbody").select("tr").get(0).text().contains("价格")) {
                    try {
                        price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(0)
                                .select(".a-text-strike").text().replace("￥", "").replace(",", ""));
                    } catch (Exception e) {

                    }
                    if (price == -1) {
                        try {
                            price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(0)
                                    .select("#priceblock_ourprice").text().replace("￥", "").replace(",", ""));
                        } catch (Exception e) {

                        }
                    }
                    if ((price == -1)
                            && (doc.select(".a-lineitem").select("tbody").select("tr").get(0).text().contains("-"))) {
                        try {
                            price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(0)
                                    .select("#priceblock_ourprice").text().split("-")[0].replace("￥", "")
                                    .replace(",", "").replace(" ", ""));
                        } catch (Exception e) {

                        }
                    }
                }
            } catch (Exception e1) {

            }
            try {
                if (doc.select(".a-lineitem").select("tbody").select("tr").get(0).text().contains("优惠价")) {
                    try {
                        specialPrice = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(0)
                                .select("#priceblock_saleprice").text().replace("￥", "").replace(",", ""));
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e1) {

            }
            try {
                if (doc.select(".a-lineitem").select("tbody").select("tr").get(1).text().contains("优惠价")) {
                    try {
                        specialPrice = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(1)
                                .select("#priceblock_saleprice").text().replace("￥", "").replace(",", ""));
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e1) {

            }
            try {
                if (doc.select(".a-lineitem").select("tbody").select("tr").get(0).text().contains("秒杀价")) {
                    try {
                        price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(0)
                                .select("#priceblock_dealprice").text().replace("￥", "").replace(",", ""));
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e1) {

            }
            if (price == -1) {
                try {
                    price = Double.parseDouble(doc.select(".a-lineitem").select("tbody").select("tr").get(0)
                            .select(".a-color-price").text().replace("￥", "").replace(",", ""));
                } catch (NumberFormatException e) {

                }
            }
        } else if (StringUtils.isNotEmpty(doc.select(".olp-padding-right").text())) {
            try {
                price = Double.parseDouble(doc.select(".olp-padding-right").select(".a-color-price").text()
                        .replace("￥", "").replace(",", ""));
            } catch (NumberFormatException e) {

            }
        } else if (StringUtils.isNotEmpty(doc.select("#olp_feature_div").text())) {
            try {
                if (doc.select("#olp_feature_div").select(".a-section").select(".olp-padding-right").size() == 2) {
                    try {
                        price = Double.parseDouble(doc.select("#olp_feature_div").select(".a-section").select(".olp-padding-right").get(0).select(".a-color-price").text().replace("￥", ""));
                        double price2 = Double.parseDouble(doc.select("#olp_feature_div").select(".a-section").select(".olp-padding-right").get(1).select(".a-color-price").text().replace("￥", ""));
                        if (price2 > price) {
                            price = price2;
                        }
                    } catch (NumberFormatException e) {
                        //e.printStackTrace();
                    }
                } else if (doc.select("#olp_feature_div").select(".a-section").select(".olp-padding-right").size() == 1) {
                    price = Double.parseDouble(doc.select("#olp_feature_div").select(".a-section").select(".olp-padding-right").select(".a-color-price").text().replace("￥", ""));
                } else {

                }
            } catch (NumberFormatException e) {
                //e.printStackTrace();
            }
        } else {

        }

        // try {
        // m_price = Double.parseDouble(
        // doc.getElementById("price").getElementsByTag("td").get(1).text().substring(1).replaceAll(",",
        // ""));
        // } catch (Exception e) {
        // m_price = -1;
        // }

        try {
            youSaveValue = doc.getElementById("youSaveValue").text();
        } catch (Exception e) {

        }

        if (StringUtils.isNotEmpty(doc.select("#averageCustomerReviews_feature_div").select(".a-declarative").text())) {
            try {
                score = CommonUtils.subString("平均", "星", doc.select("#averageCustomerReviews_feature_div")
                        .select(".a-declarative").select("#acrPopover").attr("title"));
            } catch (Exception e) {

            }
        }

        if (StringUtils.isNotEmpty(
                doc.select("#averageCustomerReviews_feature_div").select("#acrCustomerReviewText").text())) {
            try {
                comment_num = Integer.parseInt(doc.select("#averageCustomerReviews_feature_div")
                        .select("#acrCustomerReviewText").text().replace(" 条商品评论", ""));
            } catch (NumberFormatException e) {

            }
        }

        if (StringUtils.isEmpty(score) && (comment_num == 0)) {
            // 判断海外购的得分和评论数
            if (StringUtils.isNotEmpty(doc.select("#cmrsSummary_feature_div").select(".a-declarative").text())) {
                try {
                    score = CommonUtils.subString("平均", "星",
                            doc.select("#cmrsSummary_feature_div").select(".a-declarative").attr("title"));
                } catch (Exception e) {

                }
            }

            if (StringUtils.isNotEmpty(doc.select("#cmrsSummary_feature_div").select("#cmrs-atf").text())) {
                try {
                    comment_num = Integer.parseInt(
                            doc.select("#cmrsSummary_feature_div").select("#cmrs-atf").text().replace("条亚马逊美国的评论", ""));
                } catch (NumberFormatException e) {

                }
            }
        }

        try {
            to_name = doc.select("#fbt_y_title a").text();
        } catch (Exception e1) {

        }
        String t = "";
        try {
            t = doc.select("#fbt_y_title span").text();
        } catch (Exception e1) {

        }
        try {
            to_price = t.split(" ")[1];
        } catch (Exception e) {
            to_price = "";
        }

        try {
            salesRank = doc.getElementById("SalesRank").text();
            salesRank = salesRank.substring(salesRank.indexOf("第") + 1, salesRank.lastIndexOf("名"));
            salesRank = salesRank.replace(",", "");
        } catch (Exception e) {
            salesRank = "0";
        }

        try {
            navigation = doc.select("#wayfinding-breadcrumbs_feature_div").select(".a-unordered-list").text();
        } catch (Exception e) {

        }

        try {
            asin = doc.getElementById("ASIN").attr("value");
        } catch (Exception e1) {
            asin = "";
        }
        if (StringUtils.isEmpty(asin)) {
            asin = task.getAsin();
        }
        if ((m_price == -1) && (price == -1) && (specialPrice == -1) && (!doc.toString().contains("目前无货"))) {
            // 任务重置条件
            logger.info("amazon产品没抓到价格，任务重置");
            tDao.update(AmazonProductJob.class, Chain.make("status", 0), Cnd.where("id", "=", task.getId()));
            return;
        }
        try {
            DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
            int create_date = Integer.parseInt(DATE_FORMAT.format(new Date()));
            AmazonProductModel productModel = new AmazonProductModel();
            productModel.setComment_total_num(comment_num);
            productModel.setKeyword(task.getKeyword());
            productModel.setNavigation(navigation);
            productModel.setProduct_brand(brand);
            productModel.setProduct_market_price(m_price);
            productModel.setProduct_name(title);
            productModel.setProduct_price(price);
            productModel.setSku_number(sku_number);
            try {
                productModel.setProduct_rank(Integer.parseInt(salesRank));
            } catch (Exception e1) {
                logger.error("rank转换异常", e1);
            }
            productModel.setProduct_save_price(youSaveValue);
            productModel.setProduct_score(score);
            productModel.setRequest_id(task.getRequest_id());
            productModel.setTogether_by_name(to_name);
            productModel.setTogether_by_price(to_price);
            productModel.setUrl(task.getPage_url());
            productModel.setCreate_date(create_date);
            productModel.setPage_id(asin);
            productModel.setSpecial_price(specialPrice);
            try {
                biDao.fastInsert(productModel);

            } catch (Exception e) {
                // logger.info("", e);
                // tDao.update(AmazonProductJob.class, Chain.make("status", 3),
                // Cnd.where("id", "=", task.getId()));
            }
            tDao.update(AmazonProductJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            logger.info("||||||||||||网页:" + task.getPage_url() + ",page_id" + asin + "||||||||||||解析完毕");
        } catch (NumberFormatException e) {
            tDao.update(AmazonProductJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            logger.info("||||||||||||网页:" + task.getPage_url() + ",page_id" + asin + "||||||||||||解析失败");
        }
    }
}
