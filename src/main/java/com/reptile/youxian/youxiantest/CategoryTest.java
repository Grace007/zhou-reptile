package com.reptile.youxian.youxiantest;

import com.bds.base.http.HttpsBase;
import com.reptile.youxian.util.YouXianConstant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eli
 * @date 2017/11/16 16:44
 */
public class CategoryTest {
    @Test
    public void test2(){
        String url = "https://as-vip.missfresh.cn/v3/product/category/hb-frist?env=web&platform=web&version=3.9.0.1";
        String html="";
        Map<String,String> header = new HashMap<String,String >();
        header.put("platform","web");
        header.put("x-region", YouXianConstant.X_REGGION_SHANGHAI);
        try {
            html = HttpsBase.get(url,"utf-8",header).getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("html = " + html);
        JSONArray products=null;
        //String pname="",price="",current_price="",vip_price="",product_tags="",sku="",promote_tag="",subtitle="",product_url="";
        try {
            products= new JSONObject(html).getJSONArray("products");
            for (int i=0;i<products.length();i++){
                String pname="",price="",current_price="",vip_price="",product_tags="",sku="",promote_tag="",subtitle="",product_url="";
                JSONObject product = products.getJSONObject(i);
                if (!product.has("code")) {
                    pname = product.getString("name");
                    if (!product.isNull("product_tags")) {
                        JSONArray product_tagsArray = product.getJSONArray("product_tags");
                        for (int j = 0; j < product_tagsArray.length(); j++) {
                            product_tags += product_tagsArray.getJSONObject(j).getString("name") + "###";
                        }
                    }
                    sku = product.getString("sku");
                    promote_tag = product.getString("promote_tag");
                    subtitle = product.getString("subtitle");
                    price = product.getString("price");
                    if (product.has("vip_price_pro") &&  !product.isNull("vip_price_pro")) {
                        JSONObject vip_price_pro = product.getJSONObject("vip_price_pro");
                        current_price = vip_price_pro.getJSONObject("price_up").getString("price");
                        vip_price = vip_price_pro.getJSONObject("price_down").getString("price");
                    }
                    product_url = "https://as-vip.missfresh.cn/v3/product/" + sku;


                    System.out.println("###############"+i);
                    System.out.println("pname = " + pname);
                    System.out.println("price = " + getPrice(price));
                    System.out.println("current_price = " + getPrice(current_price));
                    System.out.println("vip_price = " + getPrice(vip_price));
                    System.out.println("product_tags = " + product_tags);
                    System.out.println("sku = " + sku);
                    System.out.println("promote_tag = " + promote_tag);
                    System.out.println("subtitle = " + subtitle);
                    System.out.println("product_url = " + product_url);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void test1(){
        //上海hb  北京hb      广州hn 深圳hn
        String fruit="https://as-vip.missfresh.cn/v3/product/category/hn-fruit?env=web&platform=web&version=3.9.0.1";
        String food="https://as-vip.missfresh.cn/v3/product/category/hn-food?env=web&platform=web&version=3.9.0.1";
        String sc="https://as-vip.missfresh.cn/v3/product/category/hn-sc?env=web&platform=web&version=3.9.0.1";
        String seafood="https://as-vip.missfresh.cn/v3/product/category/hn-seafood?env=web&platform=web&version=3.9.0.1";

        //https://as-vip.missfresh.cn/v3/product/category/hd-seafood?env=web&platform=web&version=3.9.0.1
        //{"station_code":"","address_code":"310115"}   platform   web
        //https://as-vip.missfresh.cn/v3/product/category/hd-fruit?device_id=48626c982815690eb76c14f0d4469be3&env=web&platform=web&uuid=48626c982815690eb76c14f0d4469be3&version=3.9.0.1
        String shanghai="{\"station_code\":\"\",\"address_code\":\"310115\"}";
    }

    public String getPrice(String price){
        double priceDou = Double.parseDouble(price);
        priceDou = priceDou/100;
        return String.valueOf(priceDou);
    }

}
