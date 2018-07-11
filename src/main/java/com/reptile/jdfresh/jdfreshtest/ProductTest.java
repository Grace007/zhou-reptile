package com.reptile.jdfresh.jdfreshtest;

import com.bds.base.http.HttpBase;
import com.reptile.jdfresh.utils.RegexUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author eli
 * @date 2017/11/13 15:43
 */
public class ProductTest {
    @Test
    public void test1(){
        //String url="http://item.jd.com/5475246.html";
        String url="http://item.jd.com/4048946.html";
        String html="",introductionHtml="",deliveryHtml="";
        HashMap<String,String> header=new HashMap<String,String>();
        header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        header.put("cookie","areaId=2; ipLoc-djd=2-2824-51914-0;");
        header.put("","");
        header.put("","");
        header.put("","");
        try {
            html= HttpBase.get(url,"utf-8",header).getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(html);
        String name="",introduction="",directory="",delivery="";
        name=doc.select("div.sku-name").text().trim();
        directory=doc.select("div.crumb-wrap").select("div.crumb.fl").text();
        //http://cd.jd.com/promotion/v2?callback=jQuery9155841&skuId=5475246&area=1_72_4137_0&shopId=0&venderId=0&cat=12218,12221,13562&isCanUseDQ=isCanUseDQ-1&isCanUseJQ=isCanUseJQ-1
        //http://cd.jd.com/promotion/v2?callback=jQuery1167818&skuId=5475246&area=2_2824_51914_0&shopId=0&venderId=0&cat=12218%2C12221%2C13562&isCanUseDQ=isCanUseDQ-0&isCanUseJQ=isCanUseJQ-1&_=1510640668100
        //商品介绍+优惠信息
        //String introductionUrl="http://cd.jd.com/promotion/v2?callback=jQuery1167818&skuId=4048946&area=2_2824_51914_0&shopId=0&venderId=0&cat=12218%2C12221%2C13562&isCanUseDQ=isCanUseDQ-0&isCanUseJQ=isCanUseJQ-1";
        String introductionUrl="http://cd.jd.com/promotion/v2?callback=jQuery1167818&skuId=5475246&area=2_2824_51914_0&shopId=0&venderId=0&cat=12218%2C12221%2C13562&isCanUseDQ=isCanUseDQ-0&isCanUseJQ=isCanUseJQ-1";
        try {
            introductionHtml= HttpBase.get(introductionUrl,"gbk",header).getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        introductionHtml= RegexUtil.match1("\\(\\{.*?\\}\\)",introductionHtml);
        introductionHtml=introductionHtml.substring(1,introductionHtml.length()-1);
        System.out.println("introductionHtml = " + introductionHtml);
        String coupon="",promotions="";
        try {

            introduction=new JSONObject(introductionHtml).getJSONArray("ads").getJSONObject(0).getString("ad");
            JSONArray skuCoupon = new JSONObject(introductionHtml).getJSONArray("skuCoupon");
            JSONArray tags=new JSONObject(introductionHtml).getJSONObject("prom").getJSONArray("tags");
            for (int i=0;i<tags.length();i++){
                promotions+=tags.getJSONObject(i).getString("name")+":"+tags.getJSONObject(i).getString("content")+"###";
            }
            promotions+=new JSONObject(introductionHtml).getJSONObject("quan").getString("title");
            for (int i=0;i<skuCoupon.length();i++){
                coupon+="满"+skuCoupon.getJSONObject(i).getString("quota")+"减"+skuCoupon.getJSONObject(i).getString("discount")+";"+skuCoupon.getJSONObject(i).getString("timeDesc")+"###";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String specifications=doc.select("div#choose-attrs").text();
        specifications=specifications.replaceAll("选择种类","");
        specifications=specifications.replaceAll("已选择","");

        //http://c0.3.cn/stock?skuId=5475246&area=1_72_4137_0&venderId=0&cat=12218,12221,13562&buyNum=2&choseSuitSkuIds=&extraParam={%22originid%22:%221%22}&ch=1&fqsp=0&pduid=1445222243&pdpin=&detailedAdd=null&callback=jQuery1247957
        //http://c0.3.cn/stock?skuId=5475246&area=1_72_4137_0&venderId=0&cat=12218,12221,13562&extraParam={%22originid%22:%221%22}&ch=1&fqsp=0
        //http://c0.3.cn/stock?skuId=5475246&area=2_2824_51914_0&venderId=0&cat=12218,12221,13562&buyNum=2&choseSuitSkuIds=&extraParam={%22originid%22:%221%22}&ch=1&fqsp=0&pduid=1510638337282214550277&pdpin=&detailedAdd=null&callback=jQuery3029692
        //抓取配送状况
        String deliveryUrl ="http://c0.3.cn/stock?skuId=4228092&area=2_2824_51914_0&venderId=0&cat=12218,12221,13562&ch=1&fqsp=0&extraParam=%7B%22originid%22:%221%22%7D";
        String weight="",price="",vip_price="",basic_price="";

        try {
            deliveryHtml= HttpBase.get(deliveryUrl,"gbk").getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject deliveryJson = new JSONObject(deliveryHtml).getJSONObject("stock");
            weight=deliveryJson.getString("weightValue");
            delivery+="配送至 "+deliveryJson.getJSONObject("area").getString("provinceName")+" "+deliveryJson.getJSONObject("area").getString("cityName")+" "+deliveryJson.getJSONObject("area").getString("countyName")+"###";
            delivery+=deliveryJson.getString("stockDesc")+"###";
            delivery+="支持  ";
            JSONArray eirArray = deliveryJson.getJSONArray("eir");
            price = deliveryJson.getJSONObject("jdPrice").getString("p");
            if (deliveryJson.getJSONObject("jdPrice").has("tpp")){
                vip_price=deliveryJson.getJSONObject("jdPrice").getString("tpp");
            }
            if (deliveryJson.getJSONObject("jdPrice").has("op")){
                basic_price=deliveryJson.getJSONObject("jdPrice").getString("op");
            }

            for (int i=0;i<eirArray.length();i++){
                delivery+=eirArray.getJSONObject(i).getString("showName")+"&&";
            }
            JSONArray irArray = deliveryJson.getJSONArray("ir");
            for (int i=0;i<irArray.length();i++){
                delivery+=irArray.getJSONObject(i).getString("showName")+"&&";
            }
            delivery+="###";
            delivery+=deliveryJson.getString("serviceInfo");


            System.out.println("deliveryJson = " + deliveryJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //http://sclub.jd.com/comment/productPageComments.action?callback=fetchJSON_comment98vv18993&productId=5475246&score=0&sortType=5&page=0&pageSize=10&isShadowSku=0&fold=1
        //http://club.jd.com/comment/skuProductPageComments.action?callback=fetchJSON_comment98vv18997&productId=5475246&score=0&sortType=5&page=0&pageSize=10&isShadowSku=0&fold=1
        //评论
        String commentUrl ="http://club.jd.com/comment/skuProductPageComments.action?productId=5475246&score=0&sortType=5&page=1&pageSize=10&isShadowSku=0&fold=1";

        String commentHtml="";
        String goodRateShow="",commentCount="",hotCommentTag="",productCommentSummary="";
        try {
            commentHtml= HttpBase.get(commentUrl,"gbk").getResult();
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }


        System.out.println("deliveryHtml = " + deliveryHtml);
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
        System.out.println("price = " + price);
        System.out.println("vip_price = " + vip_price);
        System.out.println("basic_price = " + basic_price);
    }
}
