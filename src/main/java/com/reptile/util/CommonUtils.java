package com.reptile.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author:lzq
 * @date: 2018/3/6 18:16
 */
public class CommonUtils {

    public static String parseNumber(String s) {
        if(StringUtils.isEmpty(s)) {
            return "";
        } else {
            String cc = ".0123456789";
            String ss = "";

            for(int i = 0; i < s.length(); ++i) {
                String c = s.charAt(i) + "";
                if (cc.contains(c)) {
                    ss = ss + c;
                }
            }

            return ss;
        }
    }

    public static void main(String[] args)throws Exception{
        System.out.println(">>"+parseNumber("服务：8.1"));
    }

}
