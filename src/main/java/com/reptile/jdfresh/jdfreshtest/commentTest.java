package com.reptile.jdfresh.jdfreshtest;

import com.bds.base.http.HttpBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author eli
 * @date 2017/11/14 12:14
 */
public class commentTest {
    @Test
    public void test1() {
        String url = "http://club.jd.com/comment/skuProductPageComments.action?productId=5475246&score=0&sortType=5&page=1&pageSize=10&isShadowSku=0&rid=0&fold=1";
        String html = "", introductionHtml = "", deliveryHtml = "";
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        //header.put("cookie","");
        header.put("", "");
        header.put("", "");
        header.put("", "");
        try {
            html = HttpBase.get(url, "utf-8", header).getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String nickname="",userexp="",score="",content="",referencename="",referencetime="";
        Document doc = Jsoup.parse(html);
        try {
            JSONArray commentArray = new JSONObject(html).getJSONArray("comments");
            System.out.println("commentArray = " + commentArray.length());
            for (int i=0;i<commentArray.length();i++){
                JSONObject commentJson= commentArray.getJSONObject(i);
                nickname=commentJson.getString("nickname");
                userexp=commentJson.getString("userExpValue");
                score=commentJson.getString("score");
                content=commentJson.getString("content");
                referencename=commentJson.getString("referenceName");
                referencetime=commentJson.getString("referenceTime");

                System.out.println("############");
                System.out.println("nickname = " +nickname );
                System.out.println("userexp = " +userexp );
                System.out.println("score = " +score );
                System.out.println("content = " +content );
                System.out.println("referencename = " +referencename );
                System.out.println("referencetime = " +referencetime );


            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("html = " + html);



    }
}
