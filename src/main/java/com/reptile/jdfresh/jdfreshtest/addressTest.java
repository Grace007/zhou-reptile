package com.reptile.jdfresh.jdfreshtest;

import com.bds.base.http.HttpsBase;
import com.reptile.jdfresh.job.CityCode;
import com.reptile.jdfresh.job.JdFreshSourceJob;
import com.reptile.jdfresh.model.JdFreshProductSimple;
import com.reptile.jdfresh.utils.MySqlDao;
import com.reptile.jdfresh.utils.RegexUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author eli
 * @date 2017/11/10 12:17
 */
public class addressTest {
    @Test
    public void test3(){
        Dao mysqldao = MySqlDao.getDao();
        JdFreshSourceJob task= mysqldao.fetch(JdFreshSourceJob.class, Cnd.where("task_id","=","1"));
        String url = task.getUrl();
        String areaid="",current_areaid="",province="",city="";
        CityCode cityCodetemp=null;
        province =task.getProvince();
        if (StringUtils.equals(province,"北京")||StringUtils.equals(province,"上海") ) {
        cityCodetemp=mysqldao.fetch(CityCode.class, Cnd.where("province","=",task.getProvince()));
        }else{
            cityCodetemp=mysqldao.fetch(CityCode.class, Cnd.where("city","=",task.getCity()));
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
            String html="",commenthtml="",pricehtml="",shophtml="";
            try {
                html = HttpsBase.get(url, "utf-8", header).getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Document doc = Jsoup.parse(html);
            //页数
            String pagetotal = doc.select("div#J_topPage").select("span.fp-text").select("i").last().text();
            if (pagestop != Integer.parseInt(pagetotal)){
                pagestop=Integer.parseInt(pagetotal);
                mysqldao.update(JdFreshSourceJob.class, Chain.make("pagestop",pagestop), Cnd.where("id","=",task.getId()));
            }
            if (pagestart > pagestop){
                return;
            }


            //商品第三级分类
            String mcat3ids = "{" + RegexUtil.subString("var attrList = {", "} };", html).trim() + "}}";
            JSONObject mcat3idsObject = null;
            try {
                mcat3idsObject = new JSONObject(mcat3ids);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println("mcat3id = " + mcat3idsObject);
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
                System.out.println("##############" + i);
                System.out.println("pname = " + pname);
                System.out.println("pid = " + pid);
                System.out.println("shopid = " + shopid);
                JdFreshProductSimple productSimple = new JdFreshProductSimple();
                productSimple.setPname(pname);
                productSimple.setPid(pid);
                productSimpleList.add(productSimple);
            }
            //http://club.jd.com/comment/productCommentSummaries.action?my=pinglun2&referenceIds=1599924,1599930,2081679,2114563,2138027,2592301,2728940,2947731,3009387,3042395,3042415,3106249,3181108,3367946,3391324,3509912,3509934,3517364,3517386,3517418,3627958,3628054,3628240,3694914,3699508,3712995,3756271,3976754,4087436,4181126,4208702,4228092,4228106,4228126,4414582,4441407,4459535,4502057,4533173,4601994,4635056,4635058,4635060,4635062,4813565,4846811,4968336,5059844,5174633,5248452,5297626,5297628,5309412,5383292,5475246,5556474,5604630,1373871207,1765484653,1765496422
            //获取评论总数
            String commentUrl = "http://club.jd.com/comment/productCommentSummaries.action?my=pinglun2&referenceIds=";
            referenceIds = referenceIds.substring(0, referenceIds.length() - 1);
            priceIds = priceIds.substring(0, priceIds.length() - 1);
            commentUrl = commentUrl + referenceIds;
            try {
                commenthtml = HttpsBase.get(commentUrl, "utf-8").getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("commentUrl = " + commentUrl);
            //System.out.println("commenthtml = " + commenthtml);
            try {
                commentArray = new JSONObject(commenthtml).getJSONArray("CommentsCount");
                System.out.println("commentArray.length() = " + commentArray.length());
                for (int i = 0; i < commentArray.length(); i++) {
                    //System.out.println("pid = " + productSimpleList.get(i).getPid());
                    //System.out.println("ProductId = " + commentArray.getJSONObject(i).getString("ProductId"));
                    if (StringUtils.equals(productSimpleList.get(i).getPid(), commentArray.getJSONObject(i).getString("ProductId"))) {
                        String comment_num = commentArray.getJSONObject(i).getString("CommentCount");
                        //System.out.println(i+":comment_num = " + comment_num);
                /*JdFreshProductSimple simple =  productSimpleList.get(i);
                simple.setComment_num(comment_num);
                productSimpleList.set(i,simple);*/
                        productSimpleList.get(i).setComment_num(comment_num);
                        //System.out.println(" productSimpleList.get(i).getComment_num() = " +  productSimpleList.get(i).getComment_num());
                    } else {
                        System.out.println("不相等");
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //http://p.3.cn/prices/mgets?callback=jQuery7145990&area=1_72_4137_0&skuIds=J_1599924,J_1599930,J_2081679,J_2114563,J_2138027,J_2592301,J_2728940,J_2947731,J_3009387,J_3042395,J_3042415,J_3048509,J_3106249,J_3367946,J_3391324,J_3509912,J_3509934,J_3517364,J_3517386,J_3517418,J_3627958,J_3628054,J_3628240,J_3694914,J_3699508,J_3712995,J_3756271,J_3976754,J_4087436,J_4181126,J_4208702,J_4228092,J_4228106,J_4228126,J_4414582,J_4441407,J_4459535,J_4502057,J_4533173,J_4601994,J_4635056,J_4635058,J_4635060,J_4635062,J_4813565,J_4846811,J_4968336,J_5059844,J_5141107,J_5174633,J_5297626,J_5297628,J_5309412,J_5383292,J_5475246,J_5556474,J_5604630,J_1373871207,J_1765484653,J_1765496422
            //获取价格
            String priceUrl = "http://p.3.cn/prices/mgets?callback=jQuery7145990&area="+current_areaid+"&skuIds=" + priceIds;
            try {
                pricehtml = HttpsBase.get(priceUrl, "utf-8", header).getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //[.*?]
            pricehtml = RegexUtil.match1("\\[.*?\\]", pricehtml);
            System.out.println("pricehtml = " + pricehtml);

            try {
                priceArray = new JSONArray(pricehtml);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < priceArray.length(); i++) {
                try {
                    String price = priceArray.getJSONObject(i).getString("p");
                    if (StringUtils.equals(priceArray.getJSONObject(i).getString("id").replaceAll("\\D", ""), productSimpleList.get(i).getPid())) {
                        productSimpleList.get(i).setPrice(price);
                        System.out.println(i + " price = " + productSimpleList.get(i).getPrice());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //获取店铺类型
            //http://chat1.jd.com/api/checkChat?my=list&pidList=3367946,3048509,5297626,5297628,4968336,4601994,2114563,2081679,3976754,1373871207,4813565,3628054,3106249,3509934,2592301,5604630,4635058,4228106,4087436,5383292,5141107,4846811,3009387,4502057,1765496422,4533173,5174633,3712995,2138027,1765484653
            String shopUrl = "http://chat1.jd.com/api/checkChat?my=list&pidList=" + referenceIds + "&callback=jQuery2937584";
            try {
                shophtml = HttpsBase.get(shopUrl, "utf-8").getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
            shophtml = RegexUtil.match1("\\[.*?\\]", shophtml);
            System.out.println("shophtml = " + shophtml);
            try {
                shopArray = new JSONArray(shophtml);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < shopArray.length(); i++) {
                try {
                    if (StringUtils.equals(shopArray.getJSONObject(i).getString("pid"), productSimpleList.get(i).getPid())) {
                        JSONObject shopJSONObject = shopArray.getJSONObject(i);
                        System.out.println("shopJSONObject = " + shopJSONObject);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < productSimpleList.size(); i++) {
                try {
                    productSimpleList.get(i).setCat3(mcat3idsObject.getJSONObject(productSimpleList.get(i).getPid()).getString("mcat3Id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                productSimpleList.get(i).setUrl("http://item.jd.com/" + productSimpleList.get(i).getPid() + ".html");
                productSimpleList.get(i).setCategory("水果");
                productSimpleList.get(i).setProvince(task.getProvince());
                productSimpleList.get(i).setCity(task.getCity());
                productSimpleList.get(i).setTask_id(task.getTask_id());
                productSimpleList.get(i).setRequest_id(task.getRequest_id());
                productSimpleList.get(i).setCreate_time(new Date());
                productSimpleList.get(i).setCreate_date(new Date());
                System.out.println("############");
                System.out.println("pid = " + productSimpleList.get(i).getPid());
                System.out.println("price = " + productSimpleList.get(i).getPrice());
                System.out.println("commentnum = " + productSimpleList.get(i).getComment_num());
                System.out.println("category = " + productSimpleList.get(i).getCategory());
                System.out.println("name = " + productSimpleList.get(i).getPname());
                System.out.println("shop = " + productSimpleList.get(i).getShop());
                System.out.println("shopid = " + productSimpleList.get(i).getShopid());
                System.out.println("shoptype = " + productSimpleList.get(i).getShoptype());
                System.out.println("url = " + productSimpleList.get(i).getUrl());
                System.out.println("cat3 = " + productSimpleList.get(i).getCat3());
            }
            try {
                mysqldao.fastInsert(productSimpleList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            url=url.replaceAll("&page="+pagestart,"&page="+(pagestart+1));
            pagestart++;
            mysqldao.update(JdFreshSourceJob.class, Chain.make("pagestart",pagestart), Cnd.where("id","=",task.getId()));



            //System.out.println("shophtml = " + shophtml);
            //System.out.println("priceArray = " + priceArray.length());
            //System.out.println("commentArray = " + commentArray);

            //System.out.println("html = " + html);
            //System.out.println("#######################");
            //System.out.println("goodsEle.size() = " + goodsEle.size());
            //System.out.println("referenceIds = " + referenceIds);
            //System.out.println("是否在北京朝阳:"+html.contains("北京市朝阳"));
            //System.out.println("是否在深圳:"+html.contains("深圳"));


        }
    }





    @Test
    public void test1(){
        Dao mysqldao = MySqlDao.getDao();
        JdFreshSourceJob jdFreshSourceJob= mysqldao.fetch(JdFreshSourceJob.class, Cnd.where("task_id","=","1"));
        //String url = "http://list.jd.com/list.html?cat=12218,12221";
        String url="http://list.jd.com/list.html?cat=12218,13581";
        HashMap<String,String> header=new HashMap<String,String>();
        header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        header.put("cookie","areaId=2; ipLoc-djd=2-2824-51914-0;");
        header.put("","");
        header.put("","");
        header.put("","");
        header.put("","");
        header.put("","");
        header.put("","");
        header.put("","");
        header.put("","");
        header.put("","");
        header.put("","");

        String html="",commenthtml="",pricehtml="",shophtml="";
        JSONArray commentArray=null,priceArray=null,shopArray=null;
        try {
            html = HttpsBase.get(url,"utf-8",header).getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document doc= Jsoup.parse(html);
        //商品第三级分类
        String mcat3ids="{"+RegexUtil.subString("var attrList = {","} };",html).trim()+"}}";
        JSONObject mcat3idsObject=null;
        try {
            mcat3idsObject=new JSONObject(mcat3ids);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        System.out.println("mcat3id = " + mcat3idsObject);
        //商品标签
        Elements goodsEle =doc.select("li.gl-item");
        List<JdFreshProductSimple> productSimpleList = new ArrayList<JdFreshProductSimple>();
        String referenceIds="",priceIds="";
        for (int i=0;i<goodsEle.size();i++){
            Element element=goodsEle.get(i);
            String pname=element.select("div.p-name").text().trim();
            String pid=element.select("div.j-sku-item").attr("data-sku").toString();
            String shopid=element.select("div.j-sku-item").attr("jdzy_shop_id");
            referenceIds+=pid+",";
            priceIds+="J_"+pid+",";
            System.out.println("##############" + i);
            System.out.println("pname = " + pname);
            System.out.println("pid = " + pid);
            System.out.println("shopid = " + shopid);
            JdFreshProductSimple productSimple =new JdFreshProductSimple();
            productSimple.setPname(pname);
            productSimple.setPid(pid);
            productSimpleList.add(productSimple);
        }
        //http://club.jd.com/comment/productCommentSummaries.action?my=pinglun2&referenceIds=1599924,1599930,2081679,2114563,2138027,2592301,2728940,2947731,3009387,3042395,3042415,3106249,3181108,3367946,3391324,3509912,3509934,3517364,3517386,3517418,3627958,3628054,3628240,3694914,3699508,3712995,3756271,3976754,4087436,4181126,4208702,4228092,4228106,4228126,4414582,4441407,4459535,4502057,4533173,4601994,4635056,4635058,4635060,4635062,4813565,4846811,4968336,5059844,5174633,5248452,5297626,5297628,5309412,5383292,5475246,5556474,5604630,1373871207,1765484653,1765496422
        //获取评论总数
        String commentUrl="http://club.jd.com/comment/productCommentSummaries.action?my=pinglun2&referenceIds=";
        referenceIds=referenceIds.substring(0,referenceIds.length()-1);
        priceIds=priceIds.substring(0,priceIds.length()-1);
        commentUrl=commentUrl+referenceIds;
        try {
             commenthtml= HttpsBase.get(commentUrl,"utf-8").getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("commentUrl = " + commentUrl);
        //System.out.println("commenthtml = " + commenthtml);
        try {
            commentArray = new JSONObject(commenthtml).getJSONArray("CommentsCount");
            System.out.println("commentArray.length() = " + commentArray.length());
            for (int i=0;i<commentArray.length();i++){
                //System.out.println("pid = " + productSimpleList.get(i).getPid());
                //System.out.println("ProductId = " + commentArray.getJSONObject(i).getString("ProductId"));
                if (StringUtils.equals(productSimpleList.get(i).getPid(),commentArray.getJSONObject(i).getString("ProductId"))){
                    String comment_num=commentArray.getJSONObject(i).getString("CommentCount");
                    //System.out.println(i+":comment_num = " + comment_num);
                /*JdFreshProductSimple simple =  productSimpleList.get(i);
                simple.setComment_num(comment_num);
                productSimpleList.set(i,simple);*/
                    productSimpleList.get(i).setComment_num(comment_num);
                    //System.out.println(" productSimpleList.get(i).getComment_num() = " +  productSimpleList.get(i).getComment_num());
                }else {
                    System.out.println("不相等");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //http://p.3.cn/prices/mgets?callback=jQuery7145990&area=1_72_4137_0&skuIds=J_1599924,J_1599930,J_2081679,J_2114563,J_2138027,J_2592301,J_2728940,J_2947731,J_3009387,J_3042395,J_3042415,J_3048509,J_3106249,J_3367946,J_3391324,J_3509912,J_3509934,J_3517364,J_3517386,J_3517418,J_3627958,J_3628054,J_3628240,J_3694914,J_3699508,J_3712995,J_3756271,J_3976754,J_4087436,J_4181126,J_4208702,J_4228092,J_4228106,J_4228126,J_4414582,J_4441407,J_4459535,J_4502057,J_4533173,J_4601994,J_4635056,J_4635058,J_4635060,J_4635062,J_4813565,J_4846811,J_4968336,J_5059844,J_5141107,J_5174633,J_5297626,J_5297628,J_5309412,J_5383292,J_5475246,J_5556474,J_5604630,J_1373871207,J_1765484653,J_1765496422
        //获取价格
        String priceUrl="http://p.3.cn/prices/mgets?callback=jQuery7145990&area=1_72_4137_0&skuIds="+priceIds;
        try {
            pricehtml= HttpsBase.get(priceUrl,"utf-8",header).getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //[.*?]
        pricehtml = RegexUtil.match1("\\[.*?\\]",pricehtml);
        System.out.println("pricehtml = " + pricehtml);

        try {
            priceArray =new JSONArray(pricehtml);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i=0;i<priceArray.length();i++){
            try {
                String price=priceArray.getJSONObject(i).getString("p");
                if (StringUtils.equals(priceArray.getJSONObject(i).getString("id").replaceAll("\\D",""),productSimpleList.get(i).getPid())){
                    productSimpleList.get(i).setPrice(price);
                    System.out.println(i+" price = " + productSimpleList.get(i).getPrice());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //获取店铺类型
        //http://chat1.jd.com/api/checkChat?my=list&pidList=3367946,3048509,5297626,5297628,4968336,4601994,2114563,2081679,3976754,1373871207,4813565,3628054,3106249,3509934,2592301,5604630,4635058,4228106,4087436,5383292,5141107,4846811,3009387,4502057,1765496422,4533173,5174633,3712995,2138027,1765484653
        String shopUrl="http://chat1.jd.com/api/checkChat?my=list&pidList="+referenceIds+"&callback=jQuery2937584";
        try {
            shophtml= HttpsBase.get(shopUrl,"utf-8").getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        shophtml = RegexUtil.match1("\\[.*?\\]",shophtml);
        System.out.println("shophtml = " + shophtml);
        try {
            shopArray = new JSONArray(shophtml);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i=0;i<shopArray.length();i++){
            try {
            if (StringUtils.equals(shopArray.getJSONObject(i).getString("pid"),productSimpleList.get(i).getPid())){
                JSONObject shopJSONObject = shopArray.getJSONObject(i);
                System.out.println("shopJSONObject = " + shopJSONObject);
                if (shopJSONObject.has("shopId")){
                    productSimpleList.get(i).setShopid(shopJSONObject.getString("shopId"));
                }
                if (shopJSONObject.has("seller")) {
                    productSimpleList.get(i).setShop(shopJSONObject.getString("seller"));
                }else
                    productSimpleList.get(i).setShop("自营");
                if (!shopJSONObject.has("chatUrl")){
                    productSimpleList.get(i).setShoptype("京东自营");
                }
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i=0;i<productSimpleList.size();i++){
            try {
                productSimpleList.get(i).setCat3(mcat3idsObject.getJSONObject(productSimpleList.get(i).getPid()).getString("mcat3Id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            productSimpleList.get(i).setUrl("http://item.jd.com/"+productSimpleList.get(i).getPid()+".html");

            System.out.println("############");
            System.out.println("pid = " + productSimpleList.get(i).getPid());
            System.out.println("price = " + productSimpleList.get(i).getPrice());
            System.out.println("commentnum = " + productSimpleList.get(i).getComment_num());
            System.out.println("category = " + productSimpleList.get(i).getCategory());
            System.out.println("name = " + productSimpleList.get(i).getPname());
            System.out.println("shop = " + productSimpleList.get(i).getShop());
            System.out.println("shopid = " + productSimpleList.get(i).getShopid());
            System.out.println("shoptype = " + productSimpleList.get(i).getShoptype());
            System.out.println("url = " + productSimpleList.get(i).getUrl());
            System.out.println("cat3 = " + productSimpleList.get(i).getCat3());

        }


        //System.out.println("shophtml = " + shophtml);
        //System.out.println("priceArray = " + priceArray.length());
        //System.out.println("commentArray = " + commentArray);

        //System.out.println("html = " + html);
        //System.out.println("#######################");
        //System.out.println("goodsEle.size() = " + goodsEle.size());
        //System.out.println("referenceIds = " + referenceIds);
        //System.out.println("是否在北京朝阳:"+html.contains("北京市朝阳"));
        //System.out.println("是否在深圳:"+html.contains("深圳"));
    }
    @Test
    public void test2(){
        Dao mysqldao = MySqlDao.getDao();
        System.out.println("#####");
        mysqldao.create(JdFreshProductSimple.class,false);
        mysqldao.create(JdFreshSourceJob.class,false);
        System.out.println("#####");
    }



}
