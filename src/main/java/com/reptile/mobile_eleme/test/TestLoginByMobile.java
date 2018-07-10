package com.reptile.mobile_eleme.test;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestLoginByMobile {

	public static void main(String[] args) {

		try {

			Map<String, String> headers = new HashMap<String, String>();

			headers.put("Host", "h5.ele.me");
			headers.put("Connection", "keep-alive");
			headers.put("Content-Length", "133");
			headers.put("Pragma", "no-cache");
			headers.put("Cache-Control", "no-cache");
			headers.put("Origin", "https://h5.ele.me");
			headers.put("X-Shard", "loc=121.473658,31.230378");
			headers.put("User-Agent",
					"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
//			headers.put("content-type", "application/json; charset=utf-8");
			headers.put("Accept", "*/*");
			headers.put("Referer", "https://h5.ele.me/login/");
			headers.put("Accept-Encoding", "gzip, deflate, br");
			headers.put("Accept-Language", "en,zh-CN;q=0.9,zh;q=0.8");

			JSONObject jsonObject = new JSONObject();

			// {"mobile":"15001727590","validate_code":"366505","validate_token":"4b414e5535ce9de9466a1fa4fdfe9a07b00b8103cd497b050cc04ca25ebc8312"}

			jsonObject.put("mobile", "18702171757");
			jsonObject.put("validate_code", "285804");
			jsonObject.put("validate_token", "df8b0988bdbd24fb715808c84701762aa7031436e5ec99df80188431ef88c277");

			String url = "https://h5.ele.me/restapi/eus/login/login_by_mobile";
			System.out.println(doPost(url, jsonObject));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 */

	public static String doPost(String url, JSONObject json) {
		String result = null ;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			StringEntity s = new StringEntity(json.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);

			post.setHeader("Host", "h5.ele.me");
			post.setHeader("Connection", "keep-alive");
//			post.setHeader("Content-Length", "133");
			post.setHeader("Pragma", "no-cache");
			post.setHeader("Cache-Control", "no-cache");
			post.setHeader("Origin", "https://h5.ele.me");
			post.setHeader("X-Shard", "loc=121.473658,31.230378");
			post.setHeader("User-Agent",
					"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
			post.setHeader("content-type", "application/json; charset=utf-8");
			post.setHeader("Accept", "*/*");
			post.setHeader("Referer", "https://h5.ele.me/login/");
			post.setHeader("Accept-Encoding", "deflate, br");
			post.setHeader("Accept-Language", "en,zh-CN;q=0.9,zh;q=0.8");

			HttpResponse res = client.execute(post);
			
			 Header[] headersMy = res.getAllHeaders();
			
			for (int i = 0; i < headersMy.length; i++) {
				Header heddd = headersMy[i];
				if (heddd.getName().contains("Set-Cookie")) {
					if (heddd.getValue().contains("SID")) {
//						System.out.println(heddd.getName()+":"+heddd.getValue());
						String [] resultHeader = heddd.getValue().split("; ");
						for (int j = 0; j < resultHeader.length; j++) {
							if (resultHeader[j].contains("SID")) {
								System.out.println(resultHeader[j]);
								result = resultHeader[j].split("=")[1] ;
							}
						}
					}
				}
			}
			
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
			
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
//				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
