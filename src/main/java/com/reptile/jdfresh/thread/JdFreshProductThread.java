package com.reptile.jdfresh.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.jdfresh.job.JdFreshCommentJob;
import com.reptile.jdfresh.job.JdFreshProductJob;
import com.reptile.jdfresh.model.JdFreshProductInfo;
import com.reptile.jdfresh.utils.RegexUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.util.Date;
import java.util.HashMap;

/**
 * @author eli
 * @date 2017/11/15 17:37
 */
public class JdFreshProductThread extends Thread {
    private Logger logger = Logger.getLogger(JdFreshProductThread.class);
    private Dao mysqldao = MySqlDataSource.getMysqlDao(Constant.ZNode);
    TaskToBi tobi = BIDataSource.getBiDataSource("fresh", "DC_FRESH", "get_sort_info");
    Dao bidao = tobi.getBiDao();
    private JdFreshProductJob task;

    public JdFreshProductThread(Record task) {
        this.task = task.toPojo(JdFreshProductJob.class);
    }

    @Override
    public void run() {
        try {
            mysqldao.update(JdFreshProductJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));


            String url=task.getUrl();
            String html="",introductionHtml="",deliveryHtml="";

            String areaid=task.getCookie().substring(0,task.getCookie().indexOf("-")-1);
            String current_areaid=task.getCookie();
            String current_areaid_url=current_areaid.replaceAll("-","_");
            String cookie="areaId="+areaid+"; ipLoc-djd="+current_areaid+";";
            String cat = task.getCat();

            HashMap<String,String> header=new HashMap<String,String>();
            header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            header.put("cookie",cookie);
            try {
                html= HttpBase.get(url,"utf-8",header).getResult();
            } catch (Exception e) {
                logger.error("Html解析失败",e);
            }
            Document doc = Jsoup.parse(html);
            String name="",introduction="",directory="",delivery="";
            name=doc.select("div.sku-name").text().trim();
            if (StringUtils.isEmpty(name)){
                name=task.getPname();
            }
            directory=doc.select("div.crumb-wrap").select("div.crumb.fl").text();
            //商品介绍+优惠信息
            String introductionUrl="http://cd.jd.com/promotion/v2?callback=jQuery1167818&skuId="+task.getPid()+"&area="+current_areaid_url+"&shopId="+task.getShopid()+"&venderId=0&cat="+ java.net.URLEncoder.encode(task.getCat(),"utf-8")+"&isCanUseDQ=isCanUseDQ-0&isCanUseJQ=isCanUseJQ-1";
            try {
                introductionHtml= HttpBase.get(introductionUrl,"gbk",header).getResult();
            } catch (Exception e) {
                logger.error("introductionHtml获取失败",e);
            }
            introductionHtml= RegexUtil.match1("\\(\\{.*?\\}\\)",introductionHtml);
            introductionHtml=introductionHtml.substring(1,introductionHtml.length()-1);
            //System.out.println("introductionHtml = " + introductionHtml);
            String coupon="",promotions="";
            try {
                try {
                    introduction = new JSONObject(introductionHtml).getJSONArray("ads").getJSONObject(0).getString("ad");
                }catch (Exception e){
                    introduction ="";
                }
                JSONArray skuCoupon = new JSONObject(introductionHtml).getJSONArray("skuCoupon");
                JSONArray tags=new JSONObject(introductionHtml).getJSONObject("prom").getJSONArray("tags");
                for (int i=0;i<tags.length();i++){
                    promotions+=tags.getJSONObject(i).getString("name")+":"+tags.getJSONObject(i).getString("content")+"###";
                }
                if (new JSONObject(introductionHtml).getJSONArray("quan").length()>0) {
                    JSONArray promotionsarray = new JSONObject(introductionHtml).getJSONArray("quan");
                    for (int m=0;m<promotionsarray.length();m++){
                        JSONObject promotionsobject = promotionsarray.getJSONObject(m);
                        promotions += promotionsobject.getString("title");
                    }
                }
                for (int i=0;i<skuCoupon.length();i++){
                    coupon+="满"+skuCoupon.getJSONObject(i).getString("quota")+"减"+skuCoupon.getJSONObject(i).getString("discount")+";"+skuCoupon.getJSONObject(i).getString("timeDesc")+"###";
                }
            } catch (Exception e) {
                logger.error("introductionHtml解析失败",e);
            }
            String specifications=doc.select("div#choose-attrs").text();
            specifications=specifications.replaceAll("选择种类","");
            specifications=specifications.replaceAll("已选择","");

            //http://c0.3.cn/stock?skuId=5475246&area=1_72_4137_0&venderId=0&cat=12218,12221,13562&buyNum=2&choseSuitSkuIds=&extraParam={%22originid%22:%221%22}&ch=1&fqsp=0&pduid=1445222243&pdpin=&detailedAdd=null&callback=jQuery1247957
            //http://c0.3.cn/stock?skuId=5475246&area=1_72_4137_0&venderId=0&cat=12218,12221,13562&extraParam={%22originid%22:%221%22}&ch=1&fqsp=0
            //http://c0.3.cn/stock?skuId=5475246&area=2_2824_51914_0&venderId=0&cat=12218,12221,13562&buyNum=2&choseSuitSkuIds=&extraParam={%22originid%22:%221%22}&ch=1&fqsp=0&pduid=1510638337282214550277&pdpin=&detailedAdd=null&callback=jQuery3029692
            //抓取配送状况
            String deliveryUrl ="http://c0.3.cn/stock?skuId="+task.getPid()+"&area="+current_areaid_url+"&venderId=0&cat="+java.net.URLEncoder.encode(task.getCat(),"utf-8")+"&ch=1&fqsp=0&extraParam=%7B%22originid%22:%221%22%7D";
            String weight="",price="",vip_price="",basic_price="";
            try {
                deliveryHtml= HttpBase.get(deliveryUrl,"gbk").getResult();
                //System.out.println("deliveryHtml = " + deliveryHtml);
                JSONObject deliveryJson = new JSONObject(deliveryHtml).getJSONObject("stock");
                weight=deliveryJson.getString("weightValue");
                delivery+="配送至 "+deliveryJson.getJSONObject("area").getString("provinceName")+" "+deliveryJson.getJSONObject("area").getString("cityName")+" "+deliveryJson.getJSONObject("area").getString("countyName")+"###";
                delivery+=deliveryJson.getString("stockDesc")+"###";
                delivery+="支持  ";
                price = deliveryJson.getJSONObject("jdPrice").getString("p");
                if (deliveryJson.getJSONObject("jdPrice").has("tpp")){
                    vip_price=deliveryJson.getJSONObject("jdPrice").getString("tpp");
                }
                if (deliveryJson.getJSONObject("jdPrice").has("op")){
                    basic_price=deliveryJson.getJSONObject("jdPrice").getString("op");
                }
                if (!deliveryJson.isNull("eir")) {
                    //System.out.println("deliveryJson = " + deliveryJson);
                    JSONArray eirArray = deliveryJson.getJSONArray("eir");
                    for (int i = 0; i < eirArray.length(); i++) {
                        delivery += eirArray.getJSONObject(i).getString("showName") + "&&";
                    }
                }
                if (!deliveryJson.isNull("ir")) {
                    JSONArray irArray = deliveryJson.getJSONArray("ir");
                    for (int i = 0; i < irArray.length(); i++) {
                        delivery += irArray.getJSONObject(i).getString("showName") + "&&";
                    }
                }
                delivery+="###";
                delivery+=deliveryJson.getString("serviceInfo");


                //System.out.println("deliveryJson = " + deliveryJson);
            } catch (JSONException e) {
                logger.error("deliveryHtml解析失败",e);
            }
            //http://sclub.jd.com/comment/productPageComments.action?callback=fetchJSON_comment98vv18993&productId=5475246&score=0&sortType=5&page=0&pageSize=10&isShadowSku=0&fold=1
            //http://club.jd.com/comment/skuProductPageComments.action?callback=fetchJSON_comment98vv18997&productId=5475246&score=0&sortType=5&page=0&pageSize=10&isShadowSku=0&fold=1
            //评论
            String commentUrl ="http://club.jd.com/comment/skuProductPageComments.action?productId="+task.getPid()+"&score=0&sortType=5&page=1&pageSize=10&isShadowSku=0&fold=1";

            String commentHtml="";
            String goodRateShow="",commentCount="",hotCommentTag="",productCommentSummary="";
            try {
                commentHtml= HttpBase.get(commentUrl,"gbk").getResult();
            } catch (Exception e) {
                logger.error("commentHtml获取失败",e);
            }
            try {
                JSONObject commentJson=new JSONObject(commentHtml);
                JSONObject productCommentSummaryJson=commentJson.getJSONObject("productCommentSummary");
                commentCount=productCommentSummaryJson.getString("commentCount");
                goodRateShow=productCommentSummaryJson.getString("goodRateShow");
                JSONArray hottag=commentJson.getJSONArray("hotCommentTagStatistics");
                for (int i=0;i<hottag.length();i++){
                    hotCommentTag+=hottag.getJSONObject(i).getString("name")+"("+hottag.getJSONObject(i).getString("count")+")"+"###";
                }
                productCommentSummary+="商品评价总量:"+productCommentSummaryJson.getString("commentCount")+";"
                        +"晒图:"+commentJson.getString("imageListCount")+";"+"追评:"+productCommentSummaryJson.getString("afterCount")+";好评:"+productCommentSummaryJson.getString("goodCount")+";中评:"+productCommentSummaryJson.getString("generalCount")+";差评:"+productCommentSummaryJson.getString("poorCount")+"###";

            } catch (JSONException e) {
                logger.error("commentHtml解析失败",e);
            }


            /*System.out.println("deliveryHtml = " + deliveryHtml);
            System.out.println("introductionHtml = " + introductionHtml);
            System.out.println("commentHtml = " + commentHtml);
            System.out.println("name = " + name);
            System.out.println("introduction = " + introduction);
            System.out.println("directory = " + directory);
            System.out.println("coupon = " + coupon);
            System.out.println("promotions = " + promotions);
            System.out.println("specifications = " + specifications);
            System.out.println("delivery = " + delivery);
            System.out.println("weight = " + weight);
            System.out.println("goodRateShow = " + goodRateShow);
            System.out.println("hotCommentTag = " + hotCommentTag);
            System.out.println("commentCount = " + commentCount);
            System.out.println("productCommentSummary = " + productCommentSummary);
            System.out.println("price = " + price);*/

            int pagestop=Integer.valueOf(commentCount)/10;
            if (pagestop>99) pagestop=99;

            JdFreshCommentJob jdFreshCommentJob =new JdFreshCommentJob();
            jdFreshCommentJob.setCategory(task.getCategory());
            jdFreshCommentJob.setCity(task.getCity());
            jdFreshCommentJob.setComment_count(commentCount);
            jdFreshCommentJob.setPagestart(0);
            jdFreshCommentJob.setPname(name);
            jdFreshCommentJob.setPagestop(pagestop);
            jdFreshCommentJob.setPid(task.getPid());
            jdFreshCommentJob.setTask_id(task.getTask_id());
            jdFreshCommentJob.setRequest_id(task.getRequest_id());
            jdFreshCommentJob.setCreate_time(new Date());
            jdFreshCommentJob.setDown_type(task.getDown_type());
            jdFreshCommentJob.setNode(task.getNode());
            jdFreshCommentJob.setWebsite(task.getWebsite());
            jdFreshCommentJob.setProject_code(task.getProject_code());
            jdFreshCommentJob.setShoptype(task.getShoptype());
            jdFreshCommentJob.setShopid(task.getShopid());
            jdFreshCommentJob.setProvince(task.getProvince());
            jdFreshCommentJob.setShop(task.getShop());
            jdFreshCommentJob.setPage_limit(25);
            try {
                mysqldao.fastInsert(jdFreshCommentJob);
            } catch (Exception e) {
                //logger.error("jdFreshCommentJob重复..");
            }

            JdFreshProductInfo jdFreshProductInfo = new JdFreshProductInfo();
            jdFreshProductInfo.setCat(task.getCat());
            jdFreshProductInfo.setCategory(task.getCategory());
            jdFreshProductInfo.setCity(task.getProvince());
            jdFreshProductInfo.setCommentCount(commentCount);
            jdFreshProductInfo.setCoupon(coupon);
            jdFreshProductInfo.setDelivery(delivery);
            jdFreshProductInfo.setDirectory(directory);
            jdFreshProductInfo.setGoodRateShow(goodRateShow);
            jdFreshProductInfo.setHotCommentTag(hotCommentTag);
            jdFreshProductInfo.setPid(task.getPid());
            jdFreshProductInfo.setGoodRateShow(goodRateShow);
            jdFreshProductInfo.setTask_id(task.getTask_id());
            jdFreshProductInfo.setRequest_id(task.getRequest_id());
            jdFreshProductInfo.setCreate_time(new Date());
            jdFreshProductInfo.setCreate_date(new Date());
            jdFreshProductInfo.setWeight(weight);
            jdFreshProductInfo.setUrl(task.getUrl());
            jdFreshProductInfo.setVip_price(vip_price);
            jdFreshProductInfo.setBasic_price(basic_price);
            jdFreshProductInfo.setSpecifications(specifications);
            jdFreshProductInfo.setShoptype(task.getShoptype());
            jdFreshProductInfo.setShopid(task.getShopid());
            jdFreshProductInfo.setPromotions(promotions);
            //jdFreshProductInfo.setProvince(task.getProvince());
            jdFreshProductInfo.setProductCommentSummary(productCommentSummary);
            jdFreshProductInfo.setPrice(price);
            jdFreshProductInfo.setPname(name);
            jdFreshProductInfo.setIntroduction(introduction);
            jdFreshProductInfo.setShop(task.getShop());

            try {
                bidao.fastInsert(jdFreshProductInfo);
            } catch (Exception e) {
                logger.info("数据重复");
            }
            mysqldao.update(JdFreshProductJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            logger.info("ProductJob任务成功!");


        } catch (Exception e) {
            logger.error("ProductJob任务出错...",e);
            mysqldao.update(JdFreshProductJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }

    }

}