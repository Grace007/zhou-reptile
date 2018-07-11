package com.reptile.ec.jd.task.thread;

import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.jd.common.CommonUtils;
import com.reptile.ec.jd.job.JdProductJob;
import com.reptile.ec.jd.model.JdProduct;
import org.apache.commons.lang.StringUtils;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JdProductThread extends Thread {

    private static final String GET_PRICE = "http://p.3.cn/prices/get?skuid=J_";
    private static Logger logger = Logger.getLogger(JdProductThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private JdProductJob task;

    public JdProductThread(JdProductJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {

            Dao biDao = BIDataSource.getBiDataSource(task.getWebsite(), task.getProject_code(), "get_product_info")
                    .getBiDao();
            tDao.update(JdProductJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));

            if (task.getPid() != null && task.getPname() != null && task.getComments() != null) {
                Long requestId = task.getRequestId();
                Long taskId = task.getTaskId();
                String key = task.getKeyword();
                String cate = task.getCategory();
                JdProduct jp = new JdProduct(task.getPid(), task.getPname(), key, cate, requestId, taskId, new Date());
                jp.setUrl(task.getUrl());
                jp.setId(UUID.randomUUID().toString());
                try {
                    if (getDetail(jp)) {
                        logger.info("当前任务url失效，页面跳转回首页");
                        tDao.update(JdProductJob.class, Chain.make("status", 4), Cnd.where("id", "=", task.getId()));
                        return;
                    }
                    if (!"-1.00".equals(jp.getPrice())) {
                        if (jp.getPrice() != null) {
                        	/*logger.info("color_size="+jp.getColor_size().length());
                        	logger.info("Parameter="+jp.getParameter().length());
                        	logger.info("getPromotion="+jp.getPromotion().length());
                        	logger.info("Description="+jp.getDescription().length());*/
                        	logger.info(jp.getParameter());
                        	logger.info(jp.getParameter());
                        	logger.info(jp.getDescription());
                        	try {
								String turl="https://c.3.cn/tax?skuId="+task.getPid()+"&callback=jQuery2280638&_=1508211556690";
								Map<String, String> header = new HashMap<String, String>();
				                header.put("Host", "c.3.cn");
				                header.put("Referer", task.getUrl());
				                String _tax = HttpBase
				                .get(turl, "gbk", header)
				                .getResult();
				                _tax=_tax.replace("jQuery2280638(", "");
				                _tax=_tax.replace(")", "");
				                if (StringUtils.isNotEmpty(_tax)) {
			                        JSONObject ta=new JSONObject(_tax);
				                    String tax= ta.getString("tax");
				                    jp.setTax(tax);
				                }
                        	} catch (Exception e) {
							
							}
                        	try {
								String surl="https://c0.3.cn/stock?skuId="+task.getPid()+"&area=1_72_2799_0&venderId=0&cat=9192,9194,12172&buyNum=1&choseSuitSkuIds=&extraParam=%7b%22originid%22:%221%22%7d&ch=1&pduid=15078650525461724889855&pdpin=&callback=jQuery7621529";
								Map<String, String> header = new HashMap<String, String>();
				                header.put("Host", "c0.3.cn");
				                header.put("Referer", task.getUrl());
				                String _stock = HttpBase
				                .get(surl, "gbk", header)
				                .getResult();
				                _stock=_stock.replace("jQuery7621529(", "");
				                _stock=_stock.replace(")", "");
				                if (StringUtils.isNotEmpty(_stock)) {
			                        JSONObject ta=new JSONObject(_stock);
			                        JSONObject array=ta.getJSONObject("stock");
			                        JSONArray eir=array.getJSONArray("eir");
			                        JSONObject st=eir.getJSONObject(0);
				                    String stock=st.getString("showName");
				                    jp.setStock(stock);
				                }
                        	} catch (Exception e) {
                        		
							}
                            biDao.fastInsert(jp);
                            logger.info("|||||||||||||||||||||往BI插入产品成功|||||||||||||||||||||,id:" + task.getId());
                            tDao.update(JdProductJob.class, Chain.make("status", 2),
                                    Cnd.where("id", "=", task.getId()));
                        } else {
                            logger.info("|||||||||||||||||||||未抓到产品价格，重置状态|||||||||||||||||||||,id:" + task.getId());
                            tDao.update(JdProductJob.class, Chain.make("status", -1),
                                    Cnd.where("id", "=", task.getId()));
                        }
                    } else {
                        logger.info("|||||||||||||||||||||产品下架|||||||||||||||||||||,id:" + task.getId());
                        tDao.update(JdProductJob.class, Chain.make("status", -31), Cnd.where("id", "=", task.getId()));
                    }
                } catch (Exception e) {
                    logger.error(e);
                    tDao.update(JdProductJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
                }
                // logger.info("|||||||||||||||||||||完成产品解析|||||||||||||||||||||,id:"
                // + task.getId());
            } else {
                String url = task.getUrl();
                // http://item.jd.com/1657894479.html
                Map<String, String> m_header = new HashMap<String, String>();
                m_header.put("Host", "item.m.jd.com");
                m_header.put("Pragma", "no-cache");
                String result = HttpBase.get(url, "utf-8", m_header).getResult();
                Document document = Jsoup.parse(result);
                if ("京东(JD.COM)-正品低价、品质保障、配送及时、轻松购物！".equals(document.title())) {
                    //判断页面重定向，任务状态置4
                    logger.info("当前任务url失效，页面跳转回首页");
                    tDao.update(JdProductJob.class, Chain.make("status", 4), Cnd.where("id", "=", task.getId()));
                    return;
                }
                String category = null;
                String key = null;
                String pname = null;
                Long comments = 0L;
                String pid = null;
                try {
                    pid = CommonUtils.subStringJd("item.jd.com/", ".html", url);
                } catch (Exception e1) {
                    logger.info("", e1);
                }
                try {
                    category = document.select(".breadcrumb").select("span").get(0).select("a").get(0).text();
                } catch (Exception e) {
                    category = document.select(".breadcrumb").select("strong").text();
                }
                try {
                    key = document.select("#parameter-brand").select("li").attr("title");
                } catch (Exception e) {
                    key = "";
                }
                try {
                    // pname =
                    // document.select(".m-item-inner").select("#itemInfo").select("#name").select("h1").text();
                    // if (pname.isEmpty()) {
                    // pname = document.select(".sku-name").text();
                    // }
                    // if (pname.isEmpty()) {
                    // pname =
                    // document.select("#product-intro").select(".clearfix").select("#name").select("h1")
                    // .text();
                    // }
                    pname = document.select(".detail-items").select("#itemInfo").select("#name").select("h1").text();// 全球购
                    if (StringUtils.isEmpty(pname)) {
                        pname = document.select(".itemInfo-wrap").select(".sku-name").text();// 普通
                    }
                    if (StringUtils.isEmpty(pname)) {
                        pname = document.select("#product-intro").select("#name").select("h1").text();// 闪购
                    }
                    if (StringUtils.isEmpty(pname)) {
                        pname = task.getPname();
                    }
                } catch (Exception e) {
                    pname = task.getPname();
                }
                if (!pid.isEmpty()) {
                    try {
                        String webUrl = "http://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds="
                                + pid;
                        JSONObject jsonObject = new JSONObject(HttpBase.get(webUrl, "utf-8").getResult());
                        JSONArray jsonArray = jsonObject.getJSONArray("CommentsCount");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            comments = Long.parseLong(jsonObject2.getString("CommentCount"));
                        }
                    } catch (Exception e) {
                        comments = 0L;
                    }
                    try {
                        tDao.update(JdProductJob.class,
                                Chain.make("pid", pid).add("pname", pname).add("commentsPage", 1)
                                        .add("comments", comments).add("updated_comments", 0).add("comment_status", 0)
                                        .add("status", 0).add("keyword", key).add("category", category),
                                Cnd.where("id", "=", task.getId()));
                        logger.info("产品id:" + task.getId() + "更新url成功");
                    } catch (Exception e) {
                        tDao.update(JdProductJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
                        logger.info("产品id:" + task.getId() + "更新url失败", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            tDao.update(JdProductJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }
    }

    public boolean getDetail(JdProduct jp) {
        boolean isInvalid = false;
        try {
            String url = jp.getUrl();
            String id = jp.getPid();
            String mobile_price = "-1";
            // try {
            // Map<String, String> m_header = new HashMap<String, String>();
            // m_header.put("Host", "item.m.jd.com");
            // m_header.put("Pragma", "no-cache");
            // String result = HttpBase.get("http://item.m.jd.com/product/" + id
            // + ".html", "utf-8", m_header)
            // .getResult();
            // Document d = Jsoup.parse(result);
            // String m = d.select(".yang-pic-price").text();
            // if (StringUtils.isEmpty(m)) {
            // m = d.select(".p-price").text();
            // }
            // logger.info("############获取手机产品页面耗时"+(System.currentTimeMillis()-t)+"####################,id:"
            // + task.getId());
            // // jp.setM_price(m);
            // } catch (Exception e) {
            // }

            if (StringUtils.isNotEmpty(url)) {
                if (url.startsWith("//")) {
                    url = "http:" + url;
                }

                String content = HttpBase.get(url, "utf8").getResult();
                Document document = Jsoup.parse(content);
                if ("京东(JD.COM)-正品低价、品质保障、配送及时、轻松购物！".equals(document.title())) {
                    //判断页面重定向，任务状态置4
                    isInvalid = true;
                    return isInvalid;
                }
                try {
                    /** 获取type */
                    String type = CommonUtils.subStringJd("pType:", ",", content).trim();
                    if (StringUtils.isNotEmpty(type) && StringUtils.isNumeric(type)) {
                        jp.setType(type);
                    }
                    /** http://p.3.cn/prices/get?skuid=J_${id}&type=${type} */
                    /**
                     * http://p.3.cn/prices/get?type=1&area=5_274_49707&pdtk=&
                     * pduid=1503411505&pdpin=&pdbp=0&skuid=J_1217493&callback=
                     * cnp
                     */
                    Map<String, String> m_header = new HashMap<String, String>();
                    // Host:p.3.cn
                    // :
                    // :http://item.jd.com/10097993708.html
                    m_header.put("Host", "pm.3.cn");
                    m_header.put("Pragma", "no-cache");
                    m_header.put("Referer", url);

                    // String _pirce = HttpBase.get(GET_PRICE + id + "&type=" +
                    // type, "utf8", m_header).getResult();
                    String _pirce = HttpBase
                            .get("http://pm.3.cn/prices/pcpmgets?skuids=" + id + "&origin=2", "utf-8", m_header)
                            .getResult();
                    if (StringUtils.isNotEmpty(_pirce)) {
                        JSONArray js = new JSONArray(_pirce);
                        if (js.length() > 0) {
                            JSONObject j = js.getJSONObject(0);
                            /** JSON解析商品价格 */
                            if (j.has("p")) {
                                jp.setPrice(j.getString("p"));
                                if (j.has("pcp")) {
                                    mobile_price = j.getString("p");
                                }
                            }
                            if (j.has("m")) {
                                jp.setMarket_price(j.getString("m"));
                            }
                            if (j.has("pcp")) {
                                jp.setPrice(j.getString("pcp"));
                            }
                        }
                    }

                } catch (Exception e) {
                }
                try {

                    Map<String, String> header = new HashMap<String, String>();
                    header.put("Host", "club.jd.com");
                    header.put("Referer", "http://item.jd.com/" + id + ".html");
                    long t3 = System.currentTimeMillis();
                    String commentsAndScore = HttpBase
                            .get("http://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds=" + id
                                    + "&callback=&_=" + new Date().getTime(), "utf8")
                            .getResult();
                    logger.info("############获取产品评论页面耗时" + (System.currentTimeMillis() - t3)
                            + "####################,id:" + task.getId());
                    if (StringUtils.isNotEmpty(commentsAndScore)) {
                        JSONObject json = new JSONObject(commentsAndScore);
                        JSONArray cs = json.getJSONArray("CommentsCount");
                        if (cs.length() > 0) {
                            json = cs.getJSONObject(0);
                            // 378564
                            /** 评论人数 */
                            String comments = json.getString("CommentCount");
                            Long coms = Long.valueOf(comments);
                            /**
                             * 更新新的评论数量，评论状态30为初始生成，需要更新精确地评论数
                             */
                            if (!"30".equals(task.getComment_status())) {
                                if (coms > task.getComments()) {
                                    if (!"1".equals(task.getDo_not_reset_comment())) {
                                        // 需要更新的评论数
                                        tDao.update(JdProductJob.class,
                                                Chain.make("comments", coms)
                                                        .add("updated_comments", (coms - task.getComments()))
                                                        .add("comment_status", 4).add("commentsPage", 1),
                                                Cnd.where("id", "=", task.getId()));
                                        logger.info(
                                                "##########重新更新评论,pid:" + task.getPid() + ",评论数:" + coms + "##########");
                                    }
                                } else {
                                    tDao.update(JdProductJob.class,
                                            Chain.make("comments", coms).add("updated_comments", 0L),
                                            Cnd.where("id", "=", task.getId()));
                                    logger.info("##########重新更新评论,pid:" + task.getPid() + ",评论数:" + coms + "##########");
                                }
                            } else {
                                tDao.update(JdProductJob.class,
                                        Chain.make("comments", coms)
                                                .add("updated_comments", coms)
                                                .add("comment_status", 0).add("commentsPage", 1),
                                        Cnd.where("id", "=", task.getId()));
                                logger.info(
                                        "##########更新初始评论,pid:" + task.getPid() + ",评论数:" + coms + "##########");
                            }
                            jp.setComments(coms);
                            jp.setPageTotal((int) ((coms - 1) / 30 + 1));

                            /** 评分 */
                            String score = json.getString("AverageScore");
                            jp.setScore(score);

                            jp.setGoodCount(json.getInt("GoodCount"));
                            jp.setGoodRateShow((int) Math.rint(json.getDouble("GoodRate") * 100));

                            jp.setGeneralRateShow(json.getInt("GeneralCount"));
                            jp.setGeneralCount((int) Math.rint(json.getDouble("GeneralRate") * 100));

                            jp.setPoorCount(json.getInt("PoorCount"));
                            jp.setPoorRateShow((int) Math.rint(json.getDouble("PoorRate") * 100));

                            jp.setShowCount(json.getInt("ShowCount"));

                        }
                    }
                } catch (Exception e) {
                    logger.info("获取评论和评分失败id[" + id + "]", e);
                }
                Document doc = Jsoup.parse(content);
                String color_size = "";
                try {
                    color_size = CommonUtils.subStringJd("colorSize: [", "],", doc.select("script").toString());
                    if (StringUtils.isEmpty(color_size)) {
                        color_size = CommonUtils.subStringJd("colorSize : [", "],", doc.select("script").toString());
                    }
                    if (color_size.length()>3900) {
						color_size = color_size.substring(0, 3900);
					}
                    jp.setColor_size(color_size);
                    logger.info(color_size.length());
                } catch (Exception e2) {
                    logger.error("", e2);
                }
                Element nav = doc.select("div.breadcrumb").first();
                if (nav != null) {
                    /** 导航 */
                    jp.setNavigation(nav.text());
                } else {
                    if (StringUtils.isNotEmpty(doc.select(".detail-bread-crumb").select(".fl").text())) {
                        jp.setNavigation(doc.select(".detail-bread-crumb").select(".fl").text());
                    } else {
                        jp.setNavigation(doc.select(".crumb-wrap").select(".crumb").text());
                    }
                }
                /** 简介 */
                /**
                 * http://simigoods.360buy.com/ThreeCCombineBuying/
                 * ThreeCBroswerBuy.
                 * aspx?callback=Recommend.cbBroswerBuy&wids=${ProductID}
                 */
                // TODO:String info=
                // jp.setInfo(info);
                try {
                    if (content.indexOf("var ColorSize =") > -1) {
                        JSONArray array = new JSONArray(
                                CommonUtils.subStringJd("var ColorSize =", " </script>", content));
                        int size = array.length();
                        JSONObject obj;
                        if (size > 1) {
                            for (int i = 0; i < size; i++) {
                                obj = array.getJSONObject(i);
                                if ((obj.get("SkuId") + "").equals(id)) {
                                    if (obj.has("Color") && obj.get("Color") != null
                                            && !(obj.get("Color") + "").equals("null")) {
                                        /** 颜色 */
                                        jp.setColor(obj.getString("Color"));
                                    }
                                    if (obj.has("Size") && obj.get("Size") != null
                                            && !(obj.get("Size") + "").equals("null")) {
                                        /** 版本/规格/套餐 */
                                        jp.setVersion(obj.getString("Size"));
                                    }
                                    break;
                                }
                            }
                        }
                    } else if (content.indexOf("colorSize: [") > -1) {
                        JSONArray array = new JSONArray(
                                CommonUtils.subStringJd("colorSize:", "],", content) + "]");
                        int size = array.length();
                        JSONObject obj;
                        if (size > 1) {
                            for (int i = 0; i < size; i++) {
                                obj = array.getJSONObject(i);
                                if ((obj.get("SkuId") + "").equals(id)) {
                                    if (obj.has("Color") && obj.get("Color") != null
                                            && !(obj.get("Color") + "").equals("null")) {
                                        /** 颜色 */
                                        jp.setColor(obj.getString("Color"));
                                    }
                                    if (obj.has("Size") && obj.get("Size") != null
                                            && !(obj.get("Size") + "").equals("null")) {
                                        /** 版本/规格/套餐 */
                                        jp.setVersion(obj.getString("Size"));
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotEmpty(color_size)) {// 重写color、version
                    try {
                        JSONArray size_array = new JSONArray("[" + color_size + "]");
                        for (int i = 0; i < size_array.length(); i++) {
                            if (size_array.getJSONObject(i).getString("SkuId").equals(task.getPid())) {
                                jp.setColor(size_array.getJSONObject(i).getString("Color"));
                                jp.setVersion(size_array.getJSONObject(i).getString("Size"));
                            }
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
                /** 商品介绍 */
                String description = doc.select(".p-parameter").text();// 非全球购
                if (StringUtils.isEmpty(description)) {
                    description = doc.select(".p-parameter-list").text();// 全球购
                }
                if (StringUtils.isEmpty(description)) {
                    description = doc.select(".detail-list").text();// 京东闪购
                }
                jp.setDescription(description);
                /** 规格参数 */
                Elements elements = doc.select("#product-detail-2>table>tbody>tr");
                if (elements.size() > 0) {
                    Elements children;
                    StringBuffer parameter = new StringBuffer();
                    for (Element e : elements) {
                        children = e.children();
                        if (children.size() == 2) {
                            parameter.append(
                                    children.get(0).ownText().trim() + ":" + children.get(1).ownText().trim() + ";");
                        }
                    }
                    jp.setParameter(parameter.toString());
                } else {
                    Element temp = doc.getElementById("product-detail-2");
                    if (temp != null) {
                        jp.setParameter(temp.text());
                    }
                }
                /** 包装清单 */
                String packing_list = doc.select("#product-detail-3").text();
                jp.setPacking_list(packing_list);
                /** 售后服务 */
                String service = doc.select("#product-detail-5").text();
                if (StringUtils.isEmpty(service)) {
                    service = doc.select(".service-intr").select(".intr-con").text();
                }
                jp.setService(service);
                jp.setStatus(1);

                //////////////////////////////// 促销信息
                String category = CommonUtils.subStringJd("cat: [", "],", doc.select("script").toString());
                String promotion_url = null;
                if (StringUtils.isNotEmpty(category)) {
                    category = java.net.URLEncoder.encode(category, "utf-8");
                    promotion_url = "https://cd.jd.com/promotion/v2?skuId=" + id + "&area=2_2822_51979_0&cat="
                            + category;
                } else {
                    promotion_url = "https://cd.jd.com/promotion/v2?skuId=" + id
                            + "&area=2_2822_51979_0&cat=1319%2C1523%2C7052";
                }
                String html = null;
                try {
                    html = HttpBase.get(promotion_url, "gb2312").getResult();
                    String promotion = "";
                    String quan = "";
                    String present = "赠品:";
                    String jingdou = "";
                    JSONObject pro = new JSONObject(html);
                    try {
                        JSONArray pros = pro.getJSONObject("prom").getJSONArray("pickOneTag");
                        for (int i = 0; i < pros.length(); i++) {
                            String temp = java.net.URLDecoder.decode(pros.getJSONObject(i).getString("content") + "",
                                    "utf-8");
                            promotion += temp + ";";
                        }
                    } catch (Exception e1) {
                        // logger.error("", e1);
                    }
                    try {
                        JSONArray tags = pro.getJSONObject("prom").getJSONArray("tags");
                        for (int i = 0; i < tags.length(); i++) {
                            if ("10".equals(tags.getJSONObject(i).getString("code"))) {
                                JSONArray gifts = tags.getJSONObject(i).getJSONArray("gifts");
                                for (int j = 0; j < gifts.length(); j++) {
                                    present += gifts.getJSONObject(j).getString("nm") + ";";
                                }
                            }
                            if ("7".equals(tags.getJSONObject(i).getString("code"))) {
                                jingdou = tags.getJSONObject(i).getString("content");
                            }
                        }
                    } catch (Exception e1) {
                        // logger.error("", e1);
                    }
                    try {
                        quan = pro.getJSONObject("quan").getString("title");
                    } catch (Exception e) {
                        // logger.error("", e);
                    }
                    jp.setPromotion(promotion + "/" + quan + "/" + present + "/" + jingdou + "/手机专享价:" + mobile_price);
                } catch (Exception e1) {
                    logger.error("", e1);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return isInvalid;
    }

    public static String getCommentUrl(String pid, int index) {
        if (index < 1) {
            index = 1;
        }
        // return PREFIX_URL + pid + SUFFIX_URL + index + LAST_URL;
        return "http://club.jd.com/review/" + pid + "-0-" + index + "-0.html";
        // return
        // "http://club.jd.com/productpage/p-"+pid+"-s-0-t-3-p-"+index+".html?callback=fetchJSON_comment";
    }

    public static void main(String args[]) throws Exception {
        JdProduct jp = new JdProduct();
        jp.setUrl("http://item.jd.com/10097993708.html");
        jp.setPid("10097993708");
        new JdProductThread(null).getDetail(jp);

    }

}
