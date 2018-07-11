package com.reptile.ec.mogujie.util;

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
