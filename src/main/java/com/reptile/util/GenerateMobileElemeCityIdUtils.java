package com.reptile.util;

import com.bds.base.http.HttpBase;
import com.reptile.mobile_eleme.job.MobileCityId;
import org.apache.commons.dbcp.BasicDataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.impl.NutDao;

public class GenerateMobileElemeCityIdUtils {

	public static NutDao getDao() {

		long t = System.currentTimeMillis();
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://119.90.36.189:3306/buzz_o2o?useUnicode=true&characterEncoding=UTF-8");
		ds.setUsername("new_root");
		ds.setPassword("@Hyipsos");
		NutDao dNutDao = new NutDao(ds);
		System.out.println("连接数据库耗时：" + (System.currentTimeMillis() - t) + "毫秒");
		return dNutDao;
	}

	public static void generate(NutDao Dao) {
		String url = "https://mainsite-restapi.ele.me/v1/cities?type=group";
		String result = null;
		try {
			result = HttpBase.get(url, "utf-8").getResult();
			JSONObject citys = new JSONObject(result);
			String[] letters = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
					"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
			for (int i = 0; i < letters.length; i++) {
				JSONArray array = citys.getJSONArray(letters[i]);
				if (array == null) {
					continue;
				}
				for (int j = 0; j < array.length(); j++) {
					MobileCityId cityId = new MobileCityId();
					if (array.getJSONObject(j).has("abbr")) {
						cityId.setAbbr(array.getJSONObject(j).getString("abbr"));
					}
					if (array.getJSONObject(j).has("area_code")) {
						cityId.setArea_code(array.getJSONObject(j).getString("area_code"));
					}
					if (array.getJSONObject(j).has("id")) {
						cityId.setCity_id(array.getJSONObject(j).getString("id"));
					}
					if (array.getJSONObject(j).has("latitude")) {
						cityId.setLatitude(array.getJSONObject(j).getString("latitude"));
					}
					if (array.getJSONObject(j).has("longitude")) {
						cityId.setLongitude(array.getJSONObject(j).getString("longitude"));
					}
					if (array.getJSONObject(j).has("name")) {
						cityId.setName(array.getJSONObject(j).getString("name"));
					}
					if (array.getJSONObject(j).has("pinyin")) {
						cityId.setPinyin(array.getJSONObject(j).getString("pinyin"));
					}
					try {
						Dao.insert(cityId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("done!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		generate(getDao());
	}

}
