package com.reptile.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式匹配方法
 * @author zyh
 * @date 2017年5月2日 下午4:26:15
 */
public class Re {
	/**
	 * 如  s = "login?id=123" Re.match("id=(\\d+)",s) -> 123
	 * @param re 正则表达式
	 * @param s 需要匹配的字符串
	 * @return 返回第一个匹配 括号里 的结果
	 */
	public static String match(String re, String s) {
		try {
			Pattern p = Pattern.compile(re);
			Matcher m = p.matcher(s);
			m.find();
			return m.group(1);
		} catch (Exception e) {
			return "no match";
		}
	}
	
	/**
	 * 如： s = "2014-23-23杀的杀" Re.match2("\\d{4}([-/])\\d{2}\\1\\d{2}", s)) -> 2014-23-23
	 * @param re
	 * @param s
	 * @return 返回第一个匹配 的结果
	 * example:
	 * 匹配日期 	\\d{4}([-/])\\d{2}\\1\\d{2}
	 */
	public static String match2(String re, String s) {
		try {
			Pattern p = Pattern.compile(re);
			Matcher m = p.matcher(s);
			m.find();
			return m.group();
		} catch (Exception e) {
			return "no match";
		}
	}
}
