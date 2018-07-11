package com.reptile.ec.mogujie.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class StringUtils {
    /**
     * String转换为int
     */
    public static int toInt(String sourceString) {
        int result;
        try {
            result = Integer.parseInt(sourceString.trim());
        } catch (Exception e) {
            result = -1;
        }
        return result;
    }

    /**
     * String转换为double
     */
    public static double toDouble(String sourceString) {
        double result;
        try {
            result = Double.parseDouble(sourceString.trim());
        } catch (Exception e) {
            result = -1;
        }
        return result;
    }

    /**
     * String转换编码
     */
    public static String encodeWord(String sourceString) {
        String result;

        try {
            result = java.net.URLEncoder.encode(sourceString, "utf-8");
        } catch (Exception e) {
            result = "ERR";
        }

        return result;
    }


    /**
    * MD5转换
    *
    * */
    public static String transMD5(String pwd){
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byTemp = md5.digest(pwd.getBytes());
            StringBuffer pwdResult = new StringBuffer();
            String strTemp="";

            for (int i=0; i<byTemp.length;i++) {
                strTemp=(Integer.toHexString(byTemp[i]&0XFF));
                if (strTemp.length() == 1) {
                    pwdResult.append("0");
                    pwdResult.append(strTemp);
                }
                else {
                    pwdResult.append(strTemp);
                }
            }

            pwd=pwdResult.toString();
            return pwd;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格林尼治时间转换
     */
    public static String gmtToDate(String sourceString, int times) {
        String result;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        switch (times) {
            case 1000:
                result = format.format(Long.parseLong(sourceString + "000"));
                break;
            default:
                result = format.format(Long.parseLong(sourceString));
                break;
        }

        return result;
    }

    /**
     * 截取字符串 0
     */
    public static String stringSub(String sourceString, String startString, String endString) {
        int start = 0, end = 0;
        String result;

        start = sourceString.indexOf(startString);
        end = sourceString.lastIndexOf(endString);
        result = sourceString.substring(start + startString.length(), end);

        return result;
    }

    /**
     * 截取字符串 1
     */
    public static String stringSub(String sourceString, int start, String endString) {
        int end = 0;
        String result;

        end = sourceString.lastIndexOf(endString);
        result = sourceString.substring(start, end);

        return result;
    }

    /**
     * 截取字符串 2
     */
    public static String stringSub(String sourceString, String startString, int end) {
        int start = 0;
        String result;

        start = sourceString.indexOf(startString);
        result = sourceString.substring(start + startString.length(), end);

        return result;
    }

    /**
     * 截取字符串 3
     */
    public static String stringSub(String sourceString, String startString) {
        int start = 0;
        String result;

        start = sourceString.indexOf(startString);
        result = sourceString.substring(start + startString.length());

        return result;
    }

    public static void main(String args[]) {
        /*String a = "201阅读da";
        String b="1412929532000";

        System.out.println(toDouble(stringSub(a, 0, "阅读")));
        System.out.println(gmtToDate(b,1));*/

        String i="0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz",t="";
        String[] tt=i.split("");
        double e;
        e=Math.floor(Math.random()*i.length());
        e=5;
        for(int j=0;j<12;j++) {
            for (int a = 0; e > a; a++)
                t += tt[(int) Math.floor(Math.random() * i.length())];
        }
        System.out.println(t);


    }
}
