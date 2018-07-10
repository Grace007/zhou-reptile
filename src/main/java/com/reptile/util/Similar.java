package com.reptile.util;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Similar {
	private int compare(String str, String target) {
		int d[][]; // 矩阵
		int n = str.length();
		int m = target.length();
		int i; // 遍历str的
		int j; // 遍历target的
		char ch1; // str的
		char ch2; // target的
		int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) { // 初始化第一列
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) { // 初始化第一行
			d[0][j] = j;
		}

		for (i = 1; i <= n; i++) { // 遍历str
			ch1 = str.charAt(i - 1);
			// 去匹配target
			for (j = 1; j <= m; j++) {
				ch2 = target.charAt(j - 1);
				if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}
				// 左边+1,上边+1, 左上角+temp取最小
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
			}
		}
		return d[n][m];
	}

	private int min(int one, int two, int three) {
		return (one = one < two ? one : two) < three ? one : three;
	}

	/**
	 * 获取两字符串的相似度
	 */

	private double getSimilarityRatio(String str, String target) {
		return 1 - (double) compare(str, target) / Math.max(str.length(), target.length());
	}

	public JSONArray getRatio(JSONArray array,String sname) throws JSONException{
		if(array == null || array.length() ==0 ){
			return new JSONArray();
		}
		int len = array.length();
		if(len == 1){
			return array;
		}
		Map<Double,JSONObject> map = new HashMap<Double,JSONObject>();
		JSONObject json;
		double[] ff = new double[len];
		for(int i=0;i<len;i++){
			json = array.getJSONObject(i);
			String name = json.getString("name");
			double f = getSimilarityRatio(sname, name);
			ff[i] = f;
			map.put(f, json);
		}
		double max = Arrays.stream(ff).max().getAsDouble();
		JSONArray arr = new JSONArray();
		arr.put(map.get(max));
		return arr;
		
	}
	
	public static void main(String[] args)throws Exception {
		String json = FileUtils.readFileToString(new File("c:\\json.txt"));
		JSONArray array = new JSONArray(json);
		String sname="肯德基现磨咖啡馆";
		Similar le = new Similar();
		System.out.println(le.getRatio(array, sname).getJSONObject(0).getString("name"));
		
	}
}