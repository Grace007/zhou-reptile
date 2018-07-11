package com.reptile.xiachufang.util.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.bds.base.http.HttpBase;

/**
 * Created by guoyu on 2015/7/24.
 */
public class Calculate {

    /**
     * 计算 每页显示数量为定值的情况下的 页数
     */
    public static int calculatePage(int total, int limit) {
        int result;
        if (total <= limit && total > 0) {
            result = 1;
        } else {
            if (total <= 0) {
                result = -1;
            } else {
                int pageTotalTmp = total % limit;
                if (pageTotalTmp == 0) {
                    result = total / limit;
                } else {
                    result = (total - pageTotalTmp) / limit + 1;
                }
            }
        }
        return result;
    }

    /**
     * 二分查找，减少请求次数
     * 适用于网站的最大页数未知的情况，采用类似二分查找的方法，可以用较少的请求，试验出最大页数
     */
    public static int calculateOffset(int max, int low, String sourceUrl) throws JSONException {
        int high = max, mid = (high + low) / 2;
        if (mid % 10 != 0) {
            mid += 5;
        }
        int result = 0;

        if (high > low) {
            String url = sourceUrl + high;
            String html = "";
            try {
            	html = HttpBase.get(url, "utf-8").getResult();
            } catch (Exception e) {
                return -31;
            }
            JSONObject jo = new JSONObject(html);

            url = sourceUrl + mid;
            try {
            	html = HttpBase.get(url, "utf-8").getResult();
            } catch (Exception e) {
                return -31;
            }
            JSONObject jo1 = new JSONObject(html);

            boolean b1 = jo.getJSONObject("paging").getString("next").equals("");
            boolean b2 = jo1.getJSONObject("paging").getString("next").equals("");
            /**
             * 若 大端 中端 都无下一页，则将 中端前移，并作为 新的大端
             * */
            if (b1 && b2) {
                result= calculateOffset(mid - 10, low, sourceUrl);
            }
            /**
             * 若 大端 无下一页，中端 有下一页，则 大端前移，并作为 新的大端，中端 作为 新的小端
             * */
            if (!b2 && b1) {
                result= calculateOffset(high - 10, mid, sourceUrl);
            }
            if (!b1) {
                result = max;
                if ((result % 10) != 0) {
                    result = (result / 10 + 1) * 10;
                }
                //return result;
            }
        }
        return result;

    }

    public static void main(String args[]) {
        System.out.println(calculatePage(56, 25));
        String ii = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
        String[] i = ii.split("");
        double e = Math.floor(Math.random() * i.length);
        String t = "";
        //for (int j=0;j<13;j++) {
        for (int a = 0; e > a; a++)
            t += i[(int) Math.floor(Math.random() * i.length)];
        //}
        System.out.println(t);
    }
}
