package com.reptile.mobile_eleme.test;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class TestMobileSendCode {
	
	public static void main(String[] args) {
		
		try {
	        JSONObject jsonObject = new JSONObject();
			jsonObject.put("mobile", "18702171757");
			jsonObject.put("captcha_value", "");
			jsonObject.put("captcha_hash", "");
	        
	        String url = "https://h5.ele.me/restapi/eus/login/mobile_send_code" ;
	        String result = doPost(url, jsonObject);
	        JSONObject jsonObjectResult = new JSONObject(result);
			String validate_token = jsonObjectResult.getString("validate_token");
			System.out.println(validate_token);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		
	}
	
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
			
			org.apache.http.Header[] headersMy = res.getAllHeaders();
			
			for (int i = 0; i < headersMy.length; i++) {
				org.apache.http.Header heddd = headersMy[i];
				if (heddd.getName().contains("Set-Cookie")) {
					if (heddd.getValue().contains("track")) {
						System.out.println(heddd.getName()+":"+heddd.getValue());
						String [] resultHeader = heddd.getValue().split("; ");
						for (int j = 0; j < resultHeader.length; j++) {
							if (resultHeader[j].contains("track_id")) {
								System.out.println(resultHeader[j]);
							}
						}
					}
					
				}
			}
			
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
			
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
