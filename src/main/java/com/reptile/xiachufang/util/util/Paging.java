package com.reptile.xiachufang.util.util;

import com.bds.base.http.HttpBase;

/**
 * 实现对网页的 分页 采用二分查找法
 * 这个方法通常在实在没办法的时候用
 * @author zyh
 * @date 2017年5月8日 上午11:36:27
 */
public class Paging {
	/**
	 * 根据url判断 有多少页  这里正则表达式 和 判断条件 可以自己编写
	 * @param url
	 * @param low			页码范围 如 1  ~
	 * @param high			100
	 * @return
	 */
	public static int pageNum(String url, int low, int high) {
		while (low <= high) {
			if(low == high){
				return high;
			}
			int mid = (low + high) / 2;
			if (isGetResponse(url.replaceFirst("page=\\d+", "page=" + (mid+1))) == true) {
				low = mid + 1;
			}else{
				high = mid;
			}
		}
		return -1;
	}

	/**
	 * 获取网页返回值 判断是否存在这一页
	 * 
	 * @param replaceFirst
	 * @return
	 */
	private static boolean isGetResponse(String url) {
		String s = null;
		try {
			s = HttpBase.get(url, "utf-8").getResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(s.length()>485)					//判断页面大小  条件
			return true;
		else
			return false;
	}

	public static void main(String[] args) {
		String url = "http://www.meishij.net/ajax/get_more_pl_n.php?id=1694802&page=1";
//		boolean r = isGetResponse("http://www.meishij.net/ajax/get_more_pl_n.php?id=1694803&page=1");
//		System.out.println(r);
		int num = pageNum(url, 1, 100);
		System.out.println(num);
	}
}
