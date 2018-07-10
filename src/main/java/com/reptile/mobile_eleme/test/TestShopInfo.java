package com.reptile.mobile_eleme.test;


import com.reptile.mobile_eleme.ElemeHttpBase;
import com.reptile.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestShopInfo {
	

	
	public static void main(String[] args) {
		
		try {
//			318008
//			157868531
			 String webUrl = "https://mainsite-restapi.ele.me/shopping/restaurant/" + "318008"
	                    + "?extras%5B%5D=activities&extras%5B%5D=albums&extras%5B%5D=license&extras%5B%5D=identification&terminal=h5";
//				https://restapi.ele.me/shopping/restaurant/318008?extras[]=activities&extras[]=license&extras[]=identification&extras[]=flavors&extras[]=albums&extras[]=videos&extras[]=coupon

			 webUrl = "https://restapi.ele.me/shopping/restaurant/318008?extras%5B%5D=activities&extras%5B%5D=license&extras%5B%5D=identification&extras%5B%5D=flavors&extras%5B%5D=albums&extras%5B%5D=videos&extras%5B%5D=coupon" ;
//			 https://h5.ele.me/shop/#geohash=wtw34ex9jfjc&id=2277608&s_type=0&rank_id=ffc36085e44b4a019c95e13d9d7f63d4
			 
			 String cookie = ""
						+"ubt_ssid=ox6j9p3e5vyb84dg60jn9t6p03ci3f7d_2018-06-27; "
				 		+ "_utrace=a8c29abc925490b1e184c2ba86312c80_2018-06-27; "
				 		+ "perf_ssid=rjvmv5yuenqhz1hx8y1up0vb0c9j0fzr_2018-06-27; "
				 		+ "track_id=1530092712|9c9753d7ca0cf970383dd3d18c1cd6f71158a40a1430c408d8|a776ebb00bfeebda63f94f77bb48e939; "
				 		+ "USERID=656063290; "
			 		+ "SID=ttK1HSUdxSpRt3bb8Abeg3VxMEOJ4V8AmqiA" ;
			 
			 //      https://h5.ele.me/shop/#id=2277608
			 cookie = ""
						+"ubt_ssid=ox6j9p3e5vyb84dg60jn9t6p03ci3f7d_2018-06-27; "
				 		+ "_utrace=a8c29abc925490b1e184c2ba86312c80_2018-06-27; "
				 		+ "perf_ssid=rjvmv5yuenqhz1hx8y1up0vb0c9j0fzr_2018-06-27; "
				 		+ "track_id=1530092712|9c9753d7ca0cf970383dd3d18c1cd6f71158a40a1430c408d8|a776ebb00bfeebda63f94f77bb48e939; "
				 		+ "USERID=656063290; "
				 		+ "SID=JbJKEmmbsVpndo4chRLVp4eE0cvDbAsebpOg" 
				 		;
//			 cookie = ""
//						+"ubt_ssid=ox6j9p3e5vyb84dg60jn9t6p03ci3f7d_2018-06-27; "
//				 		+ "_utrace=a8c29abc925490b1e184c2ba86312c80_2018-06-27; "
//				 		+ "perf_ssid=rjvmv5yuenqhz1hx8y1up0vb0c9j0fzr_2018-06-27; "
//				 		+ "track_id=1530092712|9c9753d7ca0cf970383dd3d18c1cd6f71158a40a1430c408d8|a776ebb00bfeebda63f94f77bb48e939; "
//				 		+ "USERID=656063290; "
//				 		+ "SID=qmXphwXyLJmMZ0nKkeHbaoZGOJ0O22u5RBjA" 
//				 		;
//			 MI6:Rajax/1 MI_6_/sagit Android/4.4.2 Display/sagit-user_4.4.2_NMF26X_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:bf49ed365203407f1b3e16e758b9a9b6
//			 OPPO_R11:Rajax/1 OPPO_R11/R11 Android/4.4.2 Display/R11-user_4.4.2_NMF26X_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:8811cbb00860f5af64394742fb654b04
//			 HUAWEI_MLA-AL10:Rajax/1 HUAWEI_MLA-AL10/MLA-AL10 Android/4.4.2 Display/MLA-AL10-user_4.4.2_HUAWEIMLA-AL10_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:8fc7b5b578e5273748c6af31d95002ca
//			 SM-G955F:Rajax/1 SM-G955F/SM-G955F Android/4.4.2 Display/SM-G955F-user_4.4.2_JLS36C_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:543032f613ea2519a409bc3c0f2b52e4
//			 SM-G955N:Rajax/1 SM-G955N/SM-G955F Android/4.4.2 Display/SM-G955F-user_4.4.2_JLS36C_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:88c9215a68f1ad4237d45909792f1249
//			 vivo_X20A:Rajax/1 vivo_X20A/PD1709_ Android/4.4.2 Display/PD1709-user_4.4.2_NMF26X_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:1c229d7c4c095260a0af03897bd79f85
//			 vivo_X9i:Rajax/1 vivo_X9i/PD1624_ Android/4.4.2 Display/PD1624-user_4.4.2_NMF26F_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:860feb86713dbe855db431652da481bf
//			 vivo_X20:Rajax/1 vivo_X20/PD1709_ Android/4.4.2 Display/PD1709-user_4.4.2_NMF26X_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:e5bd842273a19c3da5351694455ffa11
//			 M6_Note:Rajax/1 M6_Note/meizu_M6Note Android/4.4.2 Display/meizu_M6Note-user_4.4.2_N2G47H_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:f0392bfbdf21ab7873d14e907c81e83a
//			 Mi_Note:Rajax/1 Mi_Note_2/scorpio Android/4.4.2 Display/scorpio-user_4.4.2_NRD90M_381180402_release-keys Eleme/7.2 ID/90d064f5-9421-3392-8e03-33727238f728; KERNEL_VERSION:3.4.0 API_Level:19 Hardware:84d49738555d9079c929d498f42e58e9
			 
			 
			 
			 Map<String, String> headers = new HashMap<String, String>();
//            headers.put("Referer", "https://h5.ele.me/shop/");
//            headers.put("Origin", "https://h5.ele.me");
//            headers.put("Host", "mainsite-restapi.ele.me");
//            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
//            headers.put("cookie",cookie);
            
            String androidVersionStr = "" , apiLevelStr = "";
            Random random = new Random();
            String [] androidVersion = {"5.0" , "5.1" , "4.4.2" , "4.2.2"} ;
            
            String [] apiLevel = {"19" , "20" , "21" , "22"} ;
            
            String [] androidMobileType = {"MI_6_" , "OPPO_R11" , "HUAWEI_MLA-AL10" , "SM-G955F", "SM-G955N",
            							   "vivo_X20A" , "vivo_X9i" , "vivo_X20" , "M6_Note", "Mi_Note_2"
            								} ;
            
            String [] androidMobileTypeDetail = {"sagit" , "R11" , "MLA-AL10" , "SM-G955F", "SM-G955N",
					   						"PD1709_" , "PD1624_" , "PD1709_" , "meizu_M6Note", "scorpio"
											} ;
            
            String [] androidMobileTypeSystem = {"NMF26X" , "NMF26X" , "HUAWEIMLA-AL10" , "JLS36C", "JLS36C",
						"NMF26X" , "NMF26F" , "NMF26X" , "N2G47H", "NRD90M"
					} ;
            /**安卓版本**/
    		int androidVersionInt = random.nextInt(androidVersion.length);
    		androidVersionStr = androidVersion[androidVersionInt];
    		
    		/**安卓API**/
    		int androidApiInt = random.nextInt(androidVersion.length);
    		apiLevelStr = apiLevel[androidApiInt];
    		
    		int randomNumber = random.nextInt(androidMobileType.length);
            
            String userAgent = "Rajax/1 "+androidMobileType[randomNumber]+"/"+androidMobileTypeDetail[randomNumber]+" "
            					+ "Android/"+androidVersionStr+" Display/"+androidMobileTypeDetail[randomNumber]+"-user_"+androidVersionStr+"_"+androidMobileTypeSystem[randomNumber]+"_"+"381180402"+"_release-keys "
            					+ "Eleme/7.2 ID/"+ Utils.replaceCharRandomly("90d064f5-9421-3392-8e03-33727238f728")+"; "
            					+ "KERNEL_VERSION:"+"3.4.0"+" "
            					+ "API_Level:"+apiLevelStr+" "
            					+ "Hardware:"+Utils.replaceCharRandomly("88c9215a68f1ad4237d45909792f1249") ;
            
            headers.put("Content-Type","application/json; charset=utf-8");
            headers.put("User-Agent",userAgent);
            headers.put("X-DeviceInfo",Utils.replaceCharRandomly("aW1laTozNTQ3MzAwMTAwODE1ODAgc2VyaWFsOjFkNWIwMjUxOTYxMDg5ZTAgYW5kcm9pZF9pZDowODllMDFkNWIwMjUxOTYxIGJyYW5kOnNhbXN1bmcgbW9kZWw6U00tRzk1NU4gbmV0d29ya09wZXJhdG9yOjQ2MDA3IG1hY0FkZHJlc3M6MDhfOUVfMDFfRDVfQjBfMjUgbmV0VHlwZTpXSUZJIHNpbVNlcmlhbE51bWJlcjo4OTg2MDA4MTAxMDgxNTgwMTIxMyBzaW1TdGF0ZTo1IGxhdGl0dWRlOjQzLjg1Njg2MzMzMzMzMzMzIGxvbmdpdHVkZToxMjYuNTYyNzU4MzMzMzMzMzIgY2lkOjYyMTQxMzM5IGxhYzo0NTg2MSB3aWZpTGlzdDowMV84MF9jMl8wMF8wMF8wMyBoYXZlQmx1ZXRvb3RoOnRydWUgaGFyZHdhcmVfaWQ6ODhjOTIxNWE2OGYxYWQ0MjM3ZDQ1OTA5NzkyZjEyNDkgZmlyc3Rfb3BlbjoxNTMwMTc5MzA5IGxhc3Rfb3BlbjoxNTMwMTgzNzA3IG5ldF90eXBlOndpZmkgZW5lcmd5X3BlcmNlbnQ6OTAgdHJhY2tfaWQ6IG1lbW9yeTo0Mzc="));
            headers.put("Host","restapi.ele.me");
            headers.put("Connection","Keep-Alive");
            headers.put("Accept-Encoding","gzip");
            headers.put("cookie","eleme__ele_me=b9aa8521b051ee3790189c4880472d3a%3A632dd0715d0c9013087785726a2e1732c5167101; track_id=1530179311%7C79105519cb828ad2e26e1014a3a6e70698a0dde066654309af%7Ce163cf17d3835eca1ffd51c9fc1f15cd");
//            headers.put("cookie",cookie);
            String result = "" ;
            int loopNumber = 1 ;
            for (int i = 0; i < 500; i++) {
                result = ElemeHttpBase.appGet(webUrl, "utf-8", headers).getResult();

                if (result.contains("UNAUTHORIZED_RESTAURANT_ERROR")){
                    System.out.println("共循环"+loopNumber+"次出现："+result);
                    break;
                }
                System.out.println("循环了"+loopNumber+"次:result="+result);
                loopNumber++;
				
			}

        } catch (Exception e) {
           System.out.println(e);
        }
		
		
		
		
	}



}
