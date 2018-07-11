package com.reptile.ec.vip2.test;

import org.junit.Test;

public class VipTest {
	/*@Test
	public void test1() {
		String url=null;
		try {
			url = "http://category.vip.com/?keyword=" + URLEncoder.encode("益生菌", "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("url构建出错！");
		}
		String html  = null;
		
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "category.vip.com");
        //header.put("DNT", "1");
        header.put("Referer", url);
        //header.put("Cookie", "vip_rip=116.226.24.124; vip_province=103101; vip_province_name=%E4%B8%8A%E6%B5%B7; vip_city_name=%E4%B8%8A%E6%B5%B7%E5%B8%82; vip_city_code=103101101; vip_wh=VIP_SH; tmp_mars_cid=1490757090000_a0b9fcb1bfaece1a8c9c690bcb4dc1e8; user_class=a; VipUINFO=luc%3Aa%7Csuc%3Aa%7Cbct%3Ac_new%7Ckct%3Ac_new%7Chct%3Ac_new%7Cbdts%3A0%7Cbcts%3A0%7Ckfts%3A0%7Cc10%3A0%7Crcabt%3A0%7Cp2%3A0%7Cp3%3A1%7Cp4%3A0%7Cp5%3A0; mars_pid=0; visit_id=8BFB2EC86FF7B3DD9B4AFD9466600452; Hm_lvt_53a0d71dba66835ff1aa907db99144d8=1490757096,1490760161,1490769606; _mj_c=v3.3,last,1490769626942,1490757096749,315360000000|v3.3,cm,1490757097396,1490757096749,315360000000; _jzqco=%7C%7C%7C%7C%7C1.1997375953.1490757096916.1490769605764.1490769627017.1490769605764.1490769627017.0.0.0.20.20; _adwb=41626277; _adwr=41626277%230; _smt_uid=58db25e9.45f13d33; mars_sid=af53313d2faab291088ff3ebb3217954; _mj_si=si2122918912; _adwc=41626277; _adwp=41626277.6594509654.1490757097.1490760161.1490769605.3; Hm_lpvt_53a0d71dba66835ff1aa907db99144d8=1490769627; mars_cid=1490757090000_a0b9fcb1bfaece1a8c9c690bcb4dc1e8");
        String result = null;
		try {
			result = VipBase.get("http://category.vip.com/ajax/getSuggest.php?keyword=" + URLEncoder.encode("益生菌", "utf-8"), "utf-8", header).getResult();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("VipBase解析出错！");
		}
        System.out.println(result);
        JSONArray  results = null;
		try {
			results = new JSONObject(result).getJSONArray("data");
			
		} catch (Exception e) {
			
			System.out.println("JSON出错"+e);
		}
        
		
		System.out.println(results);
	}*/
	@Test
	public void test2(){
		String string = "￥7440.00";
		string = string.replace("￥", "");
		System.out.println(string);
	}
	
}
