package com.reptile.mobile_eleme.test;


import com.reptile.mobile_eleme.ElemeHttpBase;

import java.util.HashMap;
import java.util.Map;

public class TestProductInfo {
	
	public static void main(String[] args) {
		
		try {
			String webUrl3 = "https://restapi.ele.me/shopping/v2/menu?restaurant_id=" + "2277608";	
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Referer", "https://h5.ele.me/shop/");
            headers.put("Origin", "https://h5.ele.me");
            headers.put("Host", "restapi.ele.me");
            headers.put("X-Shard", "shopid="+"2277608");
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
            String result3 = ElemeHttpBase.get(webUrl3, "utf-8", headers).getResult();
            System.out.println(result3);
            if (result3.length()<5) {
            	webUrl3 = "https://mainsite-restapi.ele.me/shopping/v2/menu?restaurant_id=" + "2277608";
            	result3 = ElemeHttpBase.get(webUrl3, "utf-8", headers).getResult();
			}else if(result3.length()<5){
				 return;
			}
        } catch (Exception e) {
            System.out.println(e);
        }
		
		
	}

}
