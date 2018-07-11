package com.reptile.youxian.youxiantest;

import com.bds.base.http.HttpsBase;
import com.reptile.youxian.util.YouXianConstant;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eli
 * @date 2017/11/17 10:47
 */
public class ProductTest {
    @Test
    public void test1(){
        //String url = "https://as-vip.missfresh.cn/v3/product/yy-clzHD-0yq-171108";
        String url="https://as-vip.missfresh.cn/v3/product/p-hdalspg-4g";
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
        try {
            JSONObject product = new JSONObject(html);
            String pname="",price="",current_price="",vip_price="",subtitle="",country="",sales_volume="",delivery_mode_name="",vip_card="",product_integarl_text="",share_product_text="";
            pname = product.getString("name");
            price=product.getString("price");
            if (product.has("vip_price_pro") &&  !product.isNull("vip_price_pro")) {
                JSONObject vip_price_pro = product.getJSONObject("vip_price_pro");
                current_price = vip_price_pro.getJSONObject("price_up").getString("price");
                vip_price = vip_price_pro.getJSONObject("price_down").getString("price");
            }
            subtitle = product.getString("subtitle");
            country = product.getString("country");
            sales_volume = product.getString("sales_volume");
            delivery_mode_name =product.getString("delivery_mode_name");
            if (product.has("vip_card")){
                vip_card = product.getJSONObject("vip_card").getString("back_cash_text").replaceAll("\\n","");
            }
            if (product.has("product_share_info_v2")){
                product_integarl_text = product.getJSONObject("product_share_info_v2").getString("product_integarl_text");
                share_product_text = product.getJSONObject("product_share_info_v2").getString("share_product_text").replaceAll("\\D","");
            }

            System.out.println("##############");
            System.out.println("pname = " + pname);
            System.out.println("price = " + price);
            System.out.println("current_price = " + current_price);
            System.out.println("vip_price = " + vip_price);
            System.out.println("subtitle = " + subtitle);
            System.out.println("country = " + country);
            System.out.println("sales_volume = " + sales_volume);
            System.out.println("delivery_mode_name = " + delivery_mode_name);
            System.out.println("vip_card = " + vip_card);
            System.out.println("product_integarl_text = " + product_integarl_text);
            System.out.println("share_product_text = " + share_product_text);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
