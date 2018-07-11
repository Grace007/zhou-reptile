package com.reptile.ec.vip2.test;

import com.bds.base.http.HttpBase;
import com.reptile.ec.vip2.thread.VipBase;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class VipCategoryTest {
	
	
	@Test
	public void test1() throws Exception{
		Map<String, String> header = new HashMap<String, String>();
		//String baseUrl="http://category.vip.com/search-2-0-1.html?q=3|29655||&rp=30071|29658";
		String baseUrl="http://category.vip.com/search-2-0-3.html?q=3|29655||&rp=30071|29658";
		baseUrl=baseUrl.replace("|", "%7C");
		String baseUrlArray [] =baseUrl.split(".html");
		for (int i = 0; i < baseUrlArray.length; i++) {
			System.out.println(baseUrlArray[i]);
		}
		
		String next_page_url= baseUrlArray[0].substring(0,baseUrlArray[0].length()-1)+"2"+".html"+baseUrlArray[1];
		System.out.println(next_page_url);
		
		
		String html = null;
		html = VipBase.get(next_page_url, "utf-8",header).getResult();
        Document doc = Jsoup.parse(html);
		//System.out.println(html);
       
        String web_json = null;
        JSONObject data2;
        Elements elements = doc.select("script");
        //System.out.println(elements);
        Element script = elements.get(elements.size()-1);
        
        web_json = subString("list = [", "}];", script.toString());
        web_json="["+web_json+"}]";
        data2 = new JSONObject("{data:" + web_json + "}");
        JSONArray p_list = data2.getJSONArray("data");
        System.out.println(p_list.length());
        
        String pname = null;
        String pid = null;
        String brand_id=null;
        String current_price = null;
		String original_price = null;
		String discount = null;
		String remaining_time = null;
		JSONObject data = null;
		String promotion = null;
		String unique_id = null;
		
		Element J_searchCatList = doc.select("section#J_searchCatList").first();
		Elements eles = J_searchCatList.select("div.goods-list-item.c-goods.J_surpriseSprice_item.J_pro_items");
		Element ele=null;
		//Elements eles = doc.select("div.goods-list-item.c-goods.J_surpriseSprice_item.J_pro_items");
		for (int i = 0; i < eles.size(); i++) {
			ele = eles.get(i);
			pname = ele.select("h4.goods-info.goods-title-info").select("a").attr("title");
			pid = ele.attr("data-id");
			brand_id = ele.attr("data-brand");
			current_price = ele.select("div.goods-info.goods-price-info").select("em.goods-sells-price").text();
			original_price = ele.select("div.goods-info.goods-price-info").select("del.goods-market-price").text();
			discount = ele.select("div.goods-info.goods-price-info").select("span.goods-discount").text();
			remaining_time = ele.select("span.goods-countdown-info-text").text();
			if (StringUtils.isNotEmpty(brand_id)) {
				String result2 = null;
				try {
					String brands_request_url = "http://pmsgw.vip.com/1.0.2/jsonActiveTipsIndex?jsonRequest=%7B%22warehouse%22:%22VIP_SH%22,%22platform%22:%221%22,%22salesNos%22:%5B"
							+ brand_id
							+ "%5D,%22labels%22:%7B%22mobilePlatform%22:%22PC%22%7D%7D";
					result2 = HttpBase.get(brands_request_url,
							"utf-8").getResult();
					data = new JSONObject(result2)
							.getJSONObject("data");
					promotion = data.getJSONObject(brand_id)
							.getString("msg");
				} catch (Exception e) {
					// logger.error("brands请求异常", e);
				}
			}
			
			
			System.out.println("#################"+i);
			System.out.println(pname);
			System.out.println(pid);
			System.out.println(brand_id);
			System.out.println(current_price);
			System.out.println(original_price);
			System.out.println(discount);
			System.out.println(remaining_time);
			System.out.println(promotion);
		}
		System.out.println(eles.size());
		
		
		
		
		
        for (int i = 0; i < p_list.length(); i++) {
        	
        	if (p_list.getJSONObject(i).has("show_title")) {
                pname = p_list.getJSONObject(i).getString("show_title");
            }
        	if (p_list.getJSONObject(i).has("id")) {
                pid = p_list.getJSONObject(i).getString("id");
            }
        	if (p_list.getJSONObject(i).has("brand_id")) {
				brand_id = p_list.getJSONObject(i).getString(
						"brand_id");
			}
        	if (p_list.getJSONObject(i).has("price_info")) {
				try {
					JSONObject price_info = p_list.getJSONObject(i)
							.getJSONObject("price_info");
					current_price = "¥"
							+ price_info
									.getString("sell_price_min");
					original_price = "¥"
							+ price_info
									.getString("market_price_of_min_sell_price");
				} catch (Exception e) {
				}
			}
        	if (p_list.getJSONObject(i).has("discount")) {
				discount = p_list.getJSONObject(i).getString(
						"discount");
			}
        	if (p_list.getJSONObject(i).has("show_time_cnt")) {
				remaining_time = p_list.getJSONObject(i).getString(
						"show_time_cnt");
			}
			if (StringUtils.isNotEmpty(brand_id)) {
				String result2 = null;
				try {
					String brands_request_url = "http://pmsgw.vip.com/1.0.2/jsonActiveTipsIndex?jsonRequest=%7B%22warehouse%22:%22VIP_SH%22,%22platform%22:%221%22,%22salesNos%22:%5B"
							+ brand_id
							+ "%5D,%22labels%22:%7B%22mobilePlatform%22:%22PC%22%7D%7D";
					result2 = HttpBase.get(brands_request_url,
							"utf-8").getResult();
					data = new JSONObject(result2)
							.getJSONObject("data");
					promotion = data.getJSONObject(brand_id)
							.getString("msg");
				} catch (Exception e) {
					// logger.error("brands请求异常", e);
				}
			}
			
        	
        	
        	
        	System.out.println("####################"+i);
        	System.out.println(pname);
            System.out.println(pid);
            System.out.println(brand_id);
            System.out.println(current_price);
            System.out.println(original_price);
            System.out.println(discount);
            System.out.println(remaining_time);
            System.out.println(promotion);
		}
        
        
        
        
       
        
        
        
        
        //System.out.println(script);
	}
	
	
	public static String subString(String begin, String end, String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        int index_s = 0;
        if (begin != null) {
            index_s = content.indexOf(begin) + begin.length();
        }
        String _end = content.substring(index_s, content.length());
        int index_e = 0;

        if (end == null) {
            index_e = content.length();
        } else {
            index_e = _end.indexOf(end);
        }
        return _end.substring(0, index_e);
    }

}