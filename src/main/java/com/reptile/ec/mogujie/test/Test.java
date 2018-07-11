package com.reptile.ec.mogujie.test;

import java.io.UnsupportedEncodingException;

public class Test {

	public static void main(String[] args) {

		String dd = "\"";
		try {
			dd = java.net.URLEncoder.encode(dd, "utf-8");
			System.out.println(dd);
			dd = java.net.URLEncoder.encode(dd, "utf-8");
			System.out.println(dd);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str1 = "我java";
		char [] stringArr = str1.toCharArray() ;
		System.out.println(stringArr.length-1);
		StringBuilder sb = new StringBuilder(str1);// 构造一个StringBuilder对象
		sb.insert(stringArr.length-1, "正在学习");// 在指定的位置1，插入指定的字符串
		str1 = sb.toString();
		System.out.println(str1);
	}
}
