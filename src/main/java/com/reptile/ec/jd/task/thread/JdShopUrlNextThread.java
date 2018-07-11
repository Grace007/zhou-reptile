package com.reptile.ec.jd.task.thread;

import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.CommonUtils;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.jd.job.JdProductJob;
import com.reptile.ec.jd.job.JdShopUrlNext;
import com.reptile.ec.jd.model.JdProductSimple;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class JdShopUrlNextThread extends Thread {
    private static Logger logger = Logger.getLogger(JdShopUrlNextThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private JdShopUrlNext task;

    public JdShopUrlNextThread(JdShopUrlNext task) {
        this.task = task;
    }

    DateFormat DATA_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public void run() {
        logger.info("开始抓取京东shop_url_next产品simple...");
        tDao.update(JdShopUrlNext.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        Dao biDao = BIDataSource.getBiDataSource(task.getWebsite(), task.getProject_code(), "get_product_info")
                .getBiDao();
        String shop_url = task.getShop_url() + System.currentTimeMillis() + "&callback=jshop_module_render_callback";//不加callback返回的不是json
        String result;
        try {
            result = HttpBase.get(shop_url, "utf-8").getResult().replace("jshop_module_render_callback(", "").trim();
            String html = new JSONObject(result.substring(0, result.length() - 1)).getString("moduleText");
            Document doc = Jsoup.parse(html);
            Elements items = doc.select("div.mc").select("div.jItem");
            
            
            logger.info("################################"+items.size());
            if ((items == null) || (items.size() == 0)) {
                logger.info("该页没有产品simple");
                tDao.update(JdShopUrlNext.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
                return;
            }
            String skuids = "";//所有pid，不需要删除最后一个,
            for (Element item : items) {
                String pid = "J_" + item.select(".jGoodsInfo").select(".jPrice").select(".jdNum").attr("jdprice");
                skuids += pid + ",";
            }
            //获取pid对应价格
            String price_url = "https://p.3.cn/prices/mgets?skuids=" + skuids + "&_=" + System.currentTimeMillis();
            String price_result;
            price_result = HttpBase.get(price_url, "utf-8").getResult();
            JSONArray pids = new JSONArray(price_result);
            for (Element item : items) {
                String pid = item.select(".jGoodsInfo").select(".jPrice").select(".jdNum").attr("jdprice");
                String price = getPriceByPid(pid, pids);
                String pname = item.select(".jGoodsInfo").select(".jDesc").attr("title");
                if (StringUtils.isEmpty(pname)) {
					pname = item.select(".jGoodsInfo").select(".jDesc").text();
				}
                String url = "http:" + item.select(".jGoodsInfo").select(".jDesc").select("a").attr("href");
                
                
                logger.info(item.select(".jGoodsInfo").select(".jExtra").select("a").text());
                String comments = CommonUtils.subString("已有", "人评价", item.select(".jGoodsInfo").select(".jExtra").select("a").text());
                logger.info(comments);
                Long commentsNum = 0L;
                if (StringUtils.isEmpty(comments)) {
                	commentsNum = 0L;
				}
                else {
					commentsNum = Long.parseLong(comments);
				}
                
                
                JdProductSimple model = new JdProductSimple();
                model.setRequestId(task.getRequest_id());
                model.setTaskId(task.getTask_id());
                model.setKeyword(task.getKeyword());
                model.setCategory(task.getCategory());
                model.setPid(pid);
                model.setPname(pname);
                model.setUrl(url);
                model.setPirce(price);
                model.setComments(comments.toString());
                model.setCreateDate(DATA_FORMAT.format(new Date()));
                model.setCreateTime(new Date());
                model.setStoreId(task.getStore_id());
                model.setStoreName(task.getStore_name());
                model.setStoreUrl(task.getStore_url());
                try {
                    biDao.insert(model);
                } catch (Exception e) {
                    logger.error("", e);
                }

                JdProductJob job = new JdProductJob();
                job.setKeyword(task.getKeyword());
                job.setCategory(task.getCategory());
                job.setRequestId(task.getRequest_id());
                job.setTaskId(task.getTask_id());
                job.setUrl(url);
                job.setPid(pid);
                job.setPname(pname);
                job.setCommentsPage(1);
                job.setComments(commentsNum);
                job.setUpdated_comments(0L);
                job.setStatus(0);
                job.setComment_status(0);
                job.setProject_code(task.getProject_code());
                job.setDown_type(task.getDown_type());
                job.setCreateDate(DATA_FORMAT.format(new Date()));
                job.setWebsite(task.getWebsite());
                job.setNode(task.getNode());
                job.setType(task.getType());
                try {
                    tDao.insert(job);
                } catch (Exception e) {
                    logger.error("##################"+pid, e);
                }
            }
            logger.info("抓取京东shop_url_next产品simple完成");
            tDao.update(JdShopUrlNext.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
        } catch (Exception e) {
            logger.error("抓取京东shop_url_next产品simple失败！！！", e);
            tDao.update(JdShopUrlNext.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }
    }

    public String getPriceByPid(String pid, JSONArray pids) {
        String price = "";
        try {
            for (int i = 0; i < pids.length(); i++) {
                JSONObject o = pids.getJSONObject(i);
                if (o.getString("id").equals("J_" + pid)) {
                    price = o.getString("p");
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return price;
    }
}
