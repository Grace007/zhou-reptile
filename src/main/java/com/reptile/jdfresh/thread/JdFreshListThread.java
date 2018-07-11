package com.reptile.jdfresh.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpsBase;
import com.bds.base.util.Constant;
import com.reptile.jdfresh.job.CityCode;
import com.reptile.jdfresh.job.JdFreshProductJob;
import com.reptile.jdfresh.job.JdFreshSourceJob;
import com.reptile.jdfresh.model.JdFreshProductSimple;
import com.reptile.jdfresh.utils.RegexUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author eli
 * @date 2017/11/15 10:29
 */
public class JdFreshListThread extends Thread {
    private Logger logger=Logger.getLogger(JdFreshListThread.class);
    private Dao mysqldao = MySqlDataSource.getMysqlDao(Constant.ZNode);
    TaskToBi tobi = BIDataSource.getBiDataSource("fresh","DC_FRESH","get_sort_info");
    Dao bidao = tobi.getBiDao();
    private JdFreshSourceJob task;
    public JdFreshListThread(Record task){
        this.task=task.toPojo(JdFreshSourceJob.class);
    }

    @Override
    public void run() {
        try {
            mysqldao.update(JdFreshSourceJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));

            String areaid="",current_areaid="",province="",city="";
            String url = task.getUrl();
            String categrory ="";
            if (url.contains("12218,12221")){
                categrory ="新鲜水果";
            }else if (url.contains("12218,12222")){
                categrory ="海鲜水产";
            }else if (url.contains("12218,13581")){
                categrory ="精选肉类";
            }else if (url.contains("12218,13591")){
                categrory ="冷饮冻食";
            }else if (url.contains("12218,13553")){
                categrory ="蔬菜蛋品";
            }

            CityCode cityCodetemp=null;
            province =task.getProvince();
            if (StringUtils.equals(province,"北京")||StringUtils.equals(province,"上海") ) {
                cityCodetemp=mysqldao.fetch(CityCode.class, Cnd.where("province","=",task.getProvince()).and("city","=",task.getCity()));
            }else{
                cityCodetemp=mysqldao.fetch(CityCode.class, Cnd.where("city","=",task.getProvince()).and("area","=",task.getCity()));
            }
            areaid=cityCodetemp.getAreaid();
            current_areaid=cityCodetemp.getCurrent_areaid();
            String cookie="areaId="+areaid+"; ipLoc-djd="+current_areaid+";";
            HashMap<String,String> header=new HashMap<String,String>();
            header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            header.put("cookie",cookie);
            JSONArray commentArray=null,priceArray=null,shopArray=null;
            //开始分页部分
            int pagestart=task.getPagestart();
            int pagestop=task.getPagestop();
            if (pagestart==0) pagestart=1;
            url=url+"&page="+pagestart;
            while(true) {
                String html = "", commenthtml = "", pricehtml = "", shophtml = "";
                try {
                    html = HttpsBase.get(url, "utf-8", header).getResult();
                } catch (Exception e) {
                    logger.error("获取html失败!");
                }
                Document doc = Jsoup.parse(html);
                //页数
                String pagetotal = doc.select("div#J_topPage").select("span.fp-text").select("i").last().text();
                if (pagestop != Integer.parseInt(pagetotal)) {
                    pagestop = Integer.parseInt(pagetotal);
                    mysqldao.update(JdFreshSourceJob.class, Chain.make("pagestop", pagestop), Cnd.where("id", "=", task.getId()));
                }
                if (pagestart > pagestop) {
                    break;
                }
                //商品第三级分类
                String mcat3ids = "{" + RegexUtil.subString("var attrList = {", "} };", html).trim() + "}}";
                JSONObject mcat3idsObject = null;
                try {
                    mcat3idsObject = new JSONObject(mcat3ids);
                } catch (JSONException e) {
                    logger.error("第"+pagestart+"页获取第三级地址代码异常..");
                }
                //商品标签
                Elements goodsEle = doc.select("li.gl-item");
                List<JdFreshProductSimple> productSimpleList = new ArrayList<JdFreshProductSimple>();
                String referenceIds = "", priceIds = "";
                for (int i = 0; i < goodsEle.size(); i++) {
                    Element element = goodsEle.get(i);
                    String pname = element.select("div.p-name").text().trim();
                    String pid = element.select("div.j-sku-item").attr("data-sku").toString();
                    String shopid = element.select("div.j-sku-item").attr("jdzy_shop_id");
                    referenceIds += pid + ",";
                    priceIds += "J_" + pid + ",";
                    /*System.out.println("##############" + i);
                    System.out.println("pname = " + pname);
                    System.out.println("pid = " + pid);
                    System.out.println("shopid = " + shopid);*/
                    JdFreshProductSimple productSimple = new JdFreshProductSimple();
                    productSimple.setPname(pname);
                    productSimple.setPid(pid);
                    productSimpleList.add(productSimple);
                }

                //获取评论总数
                String commentUrl = "http://club.jd.com/comment/productCommentSummaries.action?my=pinglun2&referenceIds=";
                referenceIds = referenceIds.substring(0, referenceIds.length() - 1);
                priceIds = priceIds.substring(0, priceIds.length() - 1);
                commentUrl = commentUrl + referenceIds;
                try {
                    commenthtml = HttpsBase.get(commentUrl, "utf-8").getResult();
                } catch (Exception e) {
                    logger.error("commentHtml获取异常",e);
                }
                //System.out.println("commentUrl = " + commentUrl);
                try {
                    commentArray = new JSONObject(commenthtml).getJSONArray("CommentsCount");
                    //System.out.println("commentArray.length() = " + commentArray.length());
                    for (int i = 0; i < commentArray.length(); i++) {

                        if (StringUtils.equals(productSimpleList.get(i).getPid(), commentArray.getJSONObject(i).getString("ProductId"))) {
                            String comment_num = commentArray.getJSONObject(i).getString("CommentCount");

                            productSimpleList.get(i).setComment_num(comment_num);

                        } else {
                            System.out.println("不相等");
                        }


                    }
                } catch (Exception e) {
                    logger.error("commentHtml解析异常",e);
                }

                //获取价格
                String priceUrl = "http://p.3.cn/prices/mgets?callback=jQuery7145990&area=" + current_areaid + "&skuIds=" + priceIds;
                try {
                    pricehtml = HttpsBase.get(priceUrl, "utf-8", header).getResult();
                } catch (Exception e) {
                    logger.error("priceHtml获取异常",e);
                }
                //[.*?]
                pricehtml = RegexUtil.match1("\\[.*?\\]", pricehtml);

                try {
                    priceArray = new JSONArray(pricehtml);
                } catch (JSONException e) {
                    logger.error("priceHtml获取异常",e);
                }
                try {
                for (int i = 0; i < priceArray.length(); i++) {
                        String price = priceArray.getJSONObject(i).getString("p");
                        if (StringUtils.equals(priceArray.getJSONObject(i).getString("id").replaceAll("\\D", ""), productSimpleList.get(i).getPid())) {
                            productSimpleList.get(i).setPrice(price);
                            //System.out.println(i + " price = " + productSimpleList.get(i).getPrice());
                        }
                    }
                } catch (JSONException e) {
                    logger.error("priceHtml解析异常",e);
                }
                //获取店铺类型
                String shopUrl = "http://chat1.jd.com/api/checkChat?my=list&pidList=" + referenceIds + "&callback=jQuery2937584";
                try {
                    shophtml = HttpsBase.get(shopUrl, "utf-8").getResult();
                } catch (Exception e) {
                    logger.error("shopHtml获取异常",e);
                }
                shophtml = RegexUtil.match1("\\[.*?\\]", shophtml);
                //System.out.println("shophtml = " + shophtml);
                try {
                    shopArray = new JSONArray(shophtml);

                for (int i = 0; i < shopArray.length(); i++) {
                        if (StringUtils.equals(shopArray.getJSONObject(i).getString("pid"), productSimpleList.get(i).getPid())) {
                            JSONObject shopJSONObject = shopArray.getJSONObject(i);
                            //System.out.println("shopJSONObject = " + shopJSONObject);
                            if (shopJSONObject.has("shopId")) {
                                productSimpleList.get(i).setShopid(shopJSONObject.getString("shopId"));
                            }
                            if (shopJSONObject.has("seller")) {
                                productSimpleList.get(i).setShop(shopJSONObject.getString("seller"));
                            } else
                                productSimpleList.get(i).setShop("自营");
                            if (!shopJSONObject.has("chatUrl")) {
                                productSimpleList.get(i).setShoptype("京东自营");
                            }
                        }
                }
                }catch (JSONException e) {
                    logger.error("shopHtml解析异常",e);
                }
                List<JdFreshProductJob> productJobs = new ArrayList<JdFreshProductJob>();
                for (int i = 0; i < productSimpleList.size(); i++) {
                    try {
                        productSimpleList.get(i).setCat3(mcat3idsObject.getJSONObject(productSimpleList.get(i).getPid()).getString("mcat3Id"));
                    } catch (JSONException e) {
                        logger.error("组装productSimpleList异常");
                    }
                    String cat=RegexUtil.subString("list.html?cat=","&page",url)+","+mcat3idsObject.getJSONObject(productSimpleList.get(i).getPid()).getString("mcat3Id");


                    productSimpleList.get(i).setUrl("http://item.jd.com/" + productSimpleList.get(i).getPid() + ".html");
                    productSimpleList.get(i).setCategory(categrory);
                    //productSimpleList.get(i).setProvince(task.getProvince());
                    productSimpleList.get(i).setCity(task.getProvince());
                    productSimpleList.get(i).setTask_id(task.getTask_id());
                    productSimpleList.get(i).setRequest_id(task.getRequest_id());
                    productSimpleList.get(i).setCreate_time(new Date());
                    productSimpleList.get(i).setCreate_date(new Date());


                    JdFreshProductJob jdFreshProductJob = new JdFreshProductJob();
                    jdFreshProductJob.setCat3(productSimpleList.get(i).getCat3());
                    jdFreshProductJob.setCategory(productSimpleList.get(i).getCategory());
                    jdFreshProductJob.setCity(task.getCity());
                    jdFreshProductJob.setCreate_date(new Date());
                    jdFreshProductJob.setPid(productSimpleList.get(i).getPid());
                    jdFreshProductJob.setPname(productSimpleList.get(i).getPname());
                    jdFreshProductJob.setProvince(task.getProvince());
                    jdFreshProductJob.setShop(productSimpleList.get(i).getShop());
                    jdFreshProductJob.setPrice(productSimpleList.get(i).getPrice());
                    jdFreshProductJob.setShopid(productSimpleList.get(i).getShopid());
                    jdFreshProductJob.setShoptype(productSimpleList.get(i).getShoptype());
                    jdFreshProductJob.setStatus(0);
                    jdFreshProductJob.setUrl(productSimpleList.get(i).getUrl());
                    jdFreshProductJob.setCreate_time(new Date());
                    jdFreshProductJob.setWebsite(task.getWebsite());
                    jdFreshProductJob.setDown_type(task.getDown_type());
                    jdFreshProductJob.setProject_code(task.getProject_code());
                    jdFreshProductJob.setTask_id(task.getTask_id());
                    jdFreshProductJob.setRequest_id(task.getRequest_id());
                    jdFreshProductJob.setNode(task.getNode());
                    jdFreshProductJob.setCookie(current_areaid);
                    jdFreshProductJob.setCat(cat);
                    try {
                        mysqldao.fastInsert(jdFreshProductJob);
                    } catch (Exception e) {
                        //logger.error("jdFreshProductJob插入失败",e);
                    }
                }
                try {
                    bidao.fastInsert(productSimpleList);
                } catch (Exception e) {
                    //logger.error("数据重复...");
                }
                url = url.replaceAll("&page=" + pagestart, "&page=" + (pagestart + 1));
                mysqldao.update(JdFreshSourceJob.class, Chain.make("pagestart", pagestart), Cnd.where("id", "=", task.getId()));
                pagestart++;
            }

        } catch (Exception e) {
            logger.error("任务出错...",e);
            mysqldao.update(JdFreshSourceJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }
        mysqldao.update(JdFreshSourceJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
        logger.info("任务成功!");
    }
}
