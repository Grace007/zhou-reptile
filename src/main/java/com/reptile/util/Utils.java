package com.reptile.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Random;

public class Utils {
	
	
	private static Logger logger = Logger.getLogger(Utils.class);
	
	/***
	 * 统一金纬度的长度
	 * 121.38417263718539
	 * @param LatLngStr
	 * @param needLength
	 * @return
	 */
	public static synchronized String addLatLng(String LatLngStr , int needLength) {
		String result = "" ;
		String lat = LatLngStr;
		String lStrings = lat.split("\\.")[1] ;
		String addStr = "" ;
		if (lStrings.length() == needLength) {
			result = LatLngStr ;
		}else {
			int addLength = 10 - lStrings.length();
			for (int i = 0; i < addLength; i++) {
				int random=(int)(Math.random()*10); 
				addStr = addStr  + random ;
			}
			result =  LatLngStr + addStr ;
		}
		return result ;
	}
	
	
	/***
	 * 统一金纬度的长度
	 * 121.38417263718539
	 * @param LatLngStr
	 * @param needLength
	 * @return
	 */
	public static synchronized String minusLatLng(String LatLngStr , int needLength) {
		String result = "" ;
		String lat = LatLngStr;
		String lStrings = lat.split("\\.")[1] ;
		if (lStrings.length() == needLength) {
			result = LatLngStr ;
		}else {
			String addStr = StringUtils.substring(lStrings, 0, needLength);
			result =  lat.split("\\.")[1] + "." + addStr ;
		}
		return result ;
	}
	
	public static synchronized String getUserAgent() {
		String userAgent = "" ;
		if (Math.random() <= 0.3) {
			userAgent = "Mozilla/5.0 (Linux; Android 7.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043307 Safari/537.36 MicroMessenger/6.5.8.1060 NetType/WIFI Language/zh_CN" ;
		} else if (Math.random() >= 0.3 && Math.random() <= 0.6) {
			userAgent = "Mozilla/5.0 (Linux; Android 7.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043805 Mobile Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN" ;
		}else{
			userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_2 like Mac OS X) "
					+ "AppleWebKit/603.2.4 (KHTML, like Gecko) Mobile/14F89 MicroMessenger/6.5.8 NetType/WIFI Language/zh_CN" ;
		}
		return userAgent ;
	}
	
	/*****
	 * 获取微信-钱包-大众点评-外卖的useragent
	 * @return
	 */
	public static synchronized String getAndroidWeiXinDianping() {
		String userAgent = "" ;
		String netTypeStr = "" ;
		String LanguageStr = "" ;
		String androidVersionStr = "" ;
		String iosVersionStr = "" ;
		String chromeVersionStr = "" ;
		String weixinVersionStr = "" ;

//		String [] netType = { "WIFI" , "2G" , "3G+" , "4G" } ;
		String [] netType = { "WIFI" , "2G" , "4G" } ;
		String [] Language = {"zh-CN","en-US"} ;
		String [] androidVersion = {"7.0" , "8.0" , "7.1.1" ,"6.0", "5.0" , "5.1" , "4.4.2" , "4.2.2"} ;
//		String [] iosVersion = {"10_3_3" , "10_3_2" , "10_3_1" ,"10_3", "10_2_1" , "10_2" , "10_1_1" , 
//				                "10_1" , "10_0_3" , "10_0_2" , "10_0_1" , "9_3_5" , "9_3_4" , "9_3_3" } ;
		String [] iosVersion = {"10_3_3" , "10_3_2" , "10_3_1" ,"10_3", "10_2_1" , "10_2" , "10_1_1" , 
                "10_1" , "10_0_3" , "10_0_2" , "10_0_1" } ;
		String [] chromeVersion = {"57.0.2987.132" , "53.0.2785.49", "57.0.2987.132"} ;
		String [] weixinVersion = {"6.6.1.1220(0x26060135)" , "6.6.3.1260(0x26060339)"};
		
		Random random = new Random();
		/**网络类型**/
		int netTypeInt = random.nextInt(netType.length);
		netTypeStr = netType[netTypeInt] + " ";;
		/**语言**/
		int LanguageInt = random.nextInt(Language.length);
		LanguageStr = Language[LanguageInt]+ " ";;
		/**安卓版本**/
		int androidVersionInt = random.nextInt(androidVersion.length);
		androidVersionStr = androidVersion[androidVersionInt] + " ";
		/**iOS版本**/
		int iosVersionInt = random.nextInt(iosVersion.length);
		iosVersionStr = iosVersion[iosVersionInt] + " "; 
		/**谷歌版本**/
		int chromeVersionInt = random.nextInt(chromeVersion.length);
		chromeVersionStr = chromeVersion[chromeVersionInt] ; 
		/**谷歌版本**/
		int wexinVersionInt = random.nextInt(weixinVersion.length);
		weixinVersionStr = weixinVersion[wexinVersionInt] ; 
		
		int i = random.nextInt(4) ;
		switch (i) {
//		三星
		case 0:
			userAgent = "Mozilla/5.0 (Linux; Android "+ androidVersionStr
					+ "; SM-G9500 Build/NRD90M; wv) "
					+ "AppleWebKit/537.36 (KHTML, like Gecko) "
					+ "Version/4.0 Chrome/"+chromeVersionStr
					+ " MQQBrowser/6.2 "
					+ "TBS/043903 Mobile Safari/537.36 "
					+ "MicroMessenger/"+weixinVersionStr
					+ " "
					+ "NetType/"+ netTypeStr
					+ "Language/"+LanguageStr ;
			break;
//			华为
		case 1:
			userAgent = "Mozilla/5.0 (Linux; Android "+ androidVersionStr
					+ "; DUK-AL20 Build/HUAWEIDUK-AL20; wv) "
					+ "AppleWebKit/537.36 (KHTML, like Gecko) "
					+ "Version/4.0 Chrome/"+chromeVersionStr
					+ " Mobile MQQBrowser/6.2 "
					+ "TBS/043307 Safari/537.36 "
					+ "MicroMessenger/"+weixinVersionStr
					+ " "
					+ "NetType/"+ netTypeStr
					+ "Language/"+LanguageStr ;
			break;
//			苹果
		case 2:
//			10_3_2
			userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS " + iosVersionStr
					+ " like Mac OS X) "
					+ "AppleWebKit/603.2.4 (KHTML, like Gecko) Mobile/14F89 "
					+ "MicroMessenger/"+weixinVersionStr
					+ " "
					+ "NetType/"+ netTypeStr
					+ "Language/"+LanguageStr ;
			break;
//			小米
		case 3:
			userAgent = "Mozilla/5.0 (Linux; Android "+ androidVersionStr
					+ "; MI 6 Build/NMF26X; wv) "
					+ "AppleWebKit/537.36 (KHTML, like Gecko) "
					+ "Version/4.0 Chrome/"+chromeVersionStr
					+ " "
					+ "MQQBrowser/6.2 TBS/043811 Mobile Safari/537.36 "
					+ "MicroMessenger/"+weixinVersionStr
					+ " "
					+ "NetType/"+ netTypeStr
					+ "Language/"+LanguageStr ;
			break;
		}
		
		
//		String[] testUserAgent = {
				
//				"Mozilla/5.0 (Linux; Android 8.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043906 Mobile Safari/537.36 MicroMessenger/6.6.3.1260(0x26060339) NetType/WIFI Language/zh_CN",
//				"Mozilla/5.0 (Linux; Android 8.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043906 Mobile Safari/537.36 MicroMessenger/6.6.3.1260(0x26060339) NetType/4G Language/zh_CN",
//				"Mozilla/5.0 (Linux; Android 8.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043906 Mobile Safari/537.36 MicroMessenger/6.6.3.1260(0x26060339) NetType/2G Language/zh_CN",
				
//				"Mozilla/5.0 (Linux; Android 8.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043906 Mobile Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN",
//				"Mozilla/5.0 (Linux; Android 8.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043906 Mobile Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/4G Language/zh_CN",
//				"Mozilla/5.0 (Linux; Android 8.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043906 Mobile Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/2G Language/zh_CN"
//		} ;
//		int testRandom = random.nextInt(testUserAgent.length);
//		userAgent = testUserAgent[0] ;
		
		return userAgent ;
	}
	public static synchronized String getDianPingAndroidCommentUserAgent() {
		String userAgent = "" ;
		String netTypeStr = "" ;
		String LanguageStr = "" ;
		String androidVersionStr = "" ;
		String iosVersionStr = "" ;
		String chromeVersionStr = "" ;
		String weixinVersionStr = "" ;

//		String [] netType = { "WIFI" , "2G" , "3G+" , "4G" } ;
		String [] netType = { "WIFI" , "2G" , "4G" } ;
		String [] Language = {"zh-CN","en-US"} ;
		String [] androidVersion = {"6.0", "5.0" , "5.1" , "4.4.2" , "4.0.1", "4.3", "4.1.1", "4.2.2", "4.0.2"} ;
//		String [] iosVersion = {"10_3_3" , "10_3_2" , "10_3_1" ,"10_3", "10_2_1" , "10_2" , "10_1_1" , 
//				                "10_1" , "10_0_3" , "10_0_2" , "10_0_1" , "9_3_5" , "9_3_4" , "9_3_3" } ;
		String [] iosVersion = {"10_3_3" , "10_3_2" , "10_3_1" ,"10_3", "10_2_1" , "10_2" , "10_1_1" , 
                "10_1" , "10_0_3" , "10_0_2" , "10_0_1" } ;
		String [] chromeVersion = {"57.0.2987.132" , "53.0.2785.49", "57.0.2987.132"} ;
		String [] weixinVersion = {"6.6.1.1220(0x26060135)" , "6.6.3.1260(0x26060339)"};
		
		Random random = new Random();
		/**网络类型**/
		int netTypeInt = random.nextInt(netType.length);
		netTypeStr = netType[netTypeInt] + " ";;
		/**语言**/
		int LanguageInt = random.nextInt(Language.length);
		LanguageStr = Language[LanguageInt]+ " ";;
		/**安卓版本**/
		int androidVersionInt = random.nextInt(androidVersion.length);
		androidVersionStr = androidVersion[androidVersionInt] + " ";
		/**iOS版本**/
		int iosVersionInt = random.nextInt(iosVersion.length);
		iosVersionStr = iosVersion[iosVersionInt] + " "; 
		/**谷歌版本**/
		int chromeVersionInt = random.nextInt(chromeVersion.length);
		chromeVersionStr = chromeVersion[chromeVersionInt] ; 
		/**谷歌版本**/
		int wexinVersionInt = random.nextInt(weixinVersion.length);
		weixinVersionStr = weixinVersion[wexinVersionInt] ; 
		
		int i = random.nextInt(4) ;
		switch (i) {
//		三星
		case 0:
			userAgent = "Mozilla/5.0 (Linux; Android "+ androidVersionStr
					+ "; SM-G9500 Build/NRD90M; wv) "
					+ "AppleWebKit/537.36 (KHTML, like Gecko) "
					+ "Version/4.0 Chrome/"+chromeVersionStr
					+ " MQQBrowser/6.2 "
					+ "TBS/043903 Mobile Safari/537.36 "
//					+ "MicroMessenger/"+weixinVersionStr
					+ " "
//					+ "NetType/"+ netTypeStr
//					+ "Language/"+LanguageStr 
					+"android/"+androidVersionStr
					+ "dp/com.dianping.v1/9.0.0";
			break;
//			华为
		case 1:
			userAgent = "Mozilla/5.0 (Linux; Android "+ androidVersionStr
					+ "; DUK-AL20 Build/HUAWEIDUK-AL20; wv) "
					+ "AppleWebKit/537.36 (KHTML, like Gecko) "
					+ "Version/4.0 Chrome/"+chromeVersionStr
					+ " Mobile MQQBrowser/6.2 "
					+ "TBS/043307 Safari/537.36 "
//					+ "MicroMessenger/"+weixinVersionStr
					+ " "
//					+ "NetType/"+ netTypeStr
//					+ "Language/"+LanguageStr 
					+"android/"+androidVersionStr
					+ "dp/com.dianping.v1/9.0.0";
			break;
//			苹果
		case 2:
//			10_3_2
			userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS " + iosVersionStr
					+ " like Mac OS X) "
					+ "AppleWebKit/603.2.4 (KHTML, like Gecko) Mobile/14F89 "
//					+ "MicroMessenger/"+weixinVersionStr
					+ " "
//					+ "NetType/"+ netTypeStr
//					+ "Language/"+LanguageStr 
					+"android/"+androidVersionStr
					+ "dp/com.dianping.v1/9.0.0";
			break;
//			小米
		case 3:
			userAgent = "Mozilla/5.0 (Linux; Android "+ androidVersionStr
					+ "; MI 6 Build/NMF26X; wv) "
					+ "AppleWebKit/537.36 (KHTML, like Gecko) "
					+ "Version/4.0 Chrome/"+chromeVersionStr
					+ " "
					+ "MQQBrowser/6.2 TBS/043811 Mobile Safari/537.36 "
//					+ "MicroMessenger/"+weixinVersionStr
					+ " "
//					+ "NetType/"+ netTypeStr
//					+ "Language/"+LanguageStr 
					+"android/"+androidVersionStr
					+ "dp/com.dianping.v1/9.0.0";
			break;
		}
//		int randNumber = random.nextInt(2);
//		if (randNumber==1) {
//			userAgent = "Mozilla/5.0 (Linux; Android 6.0.1; Redmi 4A Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 KNB/1.0.0 android/6.0.1 dp/com.dianping.v1/9.0.0" ;
//
//		}else {
//			userAgent = "Mozilla/5.0 (Linux; Android 5.0.2; MI 2 Build/LRX22G; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 Mobile Safari/537.36 KNB/1.0.0 android/5.0.2 dp/com.dianping.v1/8.1.6" ;
//
//		}
//		Mozilla/5.0 (Linux; Android 6.0.1; Redmi 4A Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 KNB/1.0.0 android/6.0.1 dp/com.dianping.v1/9.0.0

		return userAgent ;
	}
	
	
	public synchronized static String getUnionId() {

		String result = "";
		Random random = new Random();

		int startNumber = 1 + random.nextInt(10);
		if (startNumber == 10) {
			startNumber = startNumber-1 ;
		}
		int endNumber = 1 + random.nextInt(10);
		if (endNumber == 10) {
			endNumber = endNumber - 1 ;
		}
		
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(10) ;
			result = result+number;
		}

		result = startNumber + result + endNumber ;
		return result;

	}
	
	

	
	
	public synchronized static String replaceCharRandomly(String str) {
//		System.out.println("传入的字符串为："+str);
		StringBuffer result = new StringBuffer();
		char[] cs = str.toCharArray();
		Random random = new Random();
		int num = 0 ;
		for (int i = 0; i < cs.length; i++) {

			if (cs[i] >= 48 && cs[i] <= 57) {
				//数字
				//第一位数字不为0
				int ran = random.nextInt(10);
				if (num == 0) {
					if (ran == 0 ) {
						ran += 1 ;
					}
				}
				result.append(ran);
			}else if (cs[i] >= 65 && cs[i] <= 90) {
				//大写字母
				result.append((char)(65 + random.nextInt(26)));
			}else if (cs[i] >= 97 && cs[i] <= 122) {
				//小写字母
				result.append((char)(97 + random.nextInt(26)));
			}else {
				//特殊符号返回本身
				result.append(cs[i]);
			}
		}
//		System.out.println("传出的字符串为："+result.toString());
		return result.toString() ;
	}
	


//	模型
	
	public void name() {
		String userAgent = "" ;
		
		
		userAgent = "Mozilla/5.0 (Linux; Android 7.0; SM-G9500 Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043903 Mobile Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN" ;
		
		userAgent = "Mozilla/5.0 (Linux; Android 8.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043307 Safari/537.36 MicroMessenger/6.5.8.1060 NetType/WIFI Language/zh_CN" ;

		userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_2 like Mac OS X) AppleWebKit/603.2.4 (KHTML, like Gecko) Mobile/14F89 MicroMessenger/6.5.8 NetType/WIFI Language/zh_CN" ;

		userAgent = "Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043811 Mobile Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN" ;
	
		userAgent = "Mozilla/5.0 (Linux; Android 8.0; DUK-AL20 Build/HUAWEIDUK-AL20; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043906 Mobile Safari/537.36 MicroMessenger/6.6.3.1260(0x26060339) NetType/WIFI Language/zh_CN" ;
	}
	
	public static void testRandom2() throws Exception {     
	    Random random = new Random();     
	    for( int i = 0; i < 8; i ++) {     
	        int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写还是小写     
	        System.out.println((char)(choice + random.nextInt(26)));     
	    }     
	}   
	
	
	/**
	 * 保留数字
	 * @param source
	 * @return
	 */
	public synchronized static String removeImoji(String source) {
	        char[] chars = source.toCharArray();
	        StringBuffer comment = new StringBuffer();
	        for (int i = 0; i < chars.length; i++) {
//	        	System.out.println(chars[i]+"--"+(int)chars[i]);
//	            if ((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 0 && chars[i] <= 127)
//	                    || (chars[i] >= 12289 && chars[i] <= 12305) || (chars[i] >= 8212 && chars[i] <= 8230)
//	                    || (chars[i] >= 65281 && chars[i] <= 65311) || chars[i] == 65374) {
//	                comment.append(chars[i]);
//	            }
	        	if (chars[i]==32) {
					continue;
				}else {
					 comment.append(chars[i]);
				}
	        }
	        return comment.toString();
	    }
	
	
	//中文转Unicode  
    public static String gbEncoding(final String gbString) {   //gbString = "测试"  
        char[] utfBytes = gbString.toCharArray();   //utfBytes = [测, 试]  
        String unicodeBytes = "";     
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {     
            String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串  
              if (hexB.length() <= 2) {     
                  hexB = "00" + hexB;     
             }     
             unicodeBytes = unicodeBytes + "\\u" + hexB;     
        }     
        System.out.println("unicodeBytes is: " + unicodeBytes);     
        return unicodeBytes;     
    }  
    
    
    
  //Unicode转中文  
    public static String decodeUnicode(final String dataStr) {     
       int start = 0;     
       int end = 0;     
       final StringBuffer buffer = new StringBuffer();     
       while (start > -1) {     
           end = dataStr.indexOf("\\u", start + 2);     
           String charStr = "";     
           if (end == -1) {     
               charStr = dataStr.substring(start + 2, dataStr.length());     
           } else {     
               charStr = dataStr.substring(start + 2, end);     
           }     
           char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。     
           buffer.append(new Character(letter).toString());     
           start = end;     
       }     
       return buffer.toString();     
    }  
	
	public static void main(String[] args) {
		
		
		System.out.println(replaceCharRandomly("125-5-dH"));
		System.out.println(minusLatLng("12.228224", 4));

		System.out.println(gbEncoding("测试"));
		String result = gbEncoding("测试");
		result = decodeUnicode(result);
		System.out.println(result);
		
	}
	

}
