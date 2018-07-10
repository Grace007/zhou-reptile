package com.reptile.util;


import com.reptile.server.MyConstant;

import java.util.Properties;


public class ConfigJsonParseUtils {
    public static void parseConfigJson(Properties infoPros) {
    	
    	try {
    		
			MyConstant.MEITUAN_CONFIG.INFO_THREAD = Integer.valueOf(infoPros.getProperty("meituan.info.thread"));
			MyConstant.MEITUAN_CONFIG.INFO_LIMIT = Integer.valueOf(infoPros.getProperty("meituan.info.limit"));
			MyConstant.MEITUAN_CONFIG.COMMENT_THREAD = Integer.valueOf(infoPros.getProperty("meituan.comment.thread"));
			MyConstant.MEITUAN_CONFIG.COMMENT_LIMIT = Integer.valueOf(infoPros.getProperty("meituan.comment.limit"));
			MyConstant.MEITUAN_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("meituan.info"));
			MyConstant.MEITUAN_CONFIG.COMMNET = Boolean.parseBoolean(infoPros.getProperty("meituan.comment"));
			MyConstant.MEITUAN_CONFIG.WM_LIST = Boolean.parseBoolean(infoPros.getProperty("meituan.waimai.list"));
			MyConstant.MEITUAN_CONFIG.SHOP_KEYWORD_LIST = Boolean.parseBoolean(infoPros.getProperty("meituan.shop.keyword.list"));
			MyConstant.MEITUAN_CONFIG.SEARCH_COMBINE_INFO = Boolean.parseBoolean(infoPros.getProperty("meituan.search.combine.info"));
			MyConstant.MEITUAN_CONFIG.INFO_TIME = infoPros.getProperty("meituan.info_time");
			MyConstant.MEITUAN_CONFIG.COMMNET_TIME = infoPros.getProperty("meituan.comment_time");
			MyConstant.MEITUAN_CONFIG.COOKIE = infoPros.getProperty("meituan.cookie");
			MyConstant.MEITUAN_CONFIG.WM_COOKIE = infoPros.getProperty("meituan.waimai.cookie");
			MyConstant.MEITUAN_CONFIG.SEARCH_LIMIT = Integer.valueOf(infoPros.getProperty("meituan.search.limit"));
			MyConstant.MEITUAN_CONFIG.SEARCH_THREAD = Integer.valueOf(infoPros.getProperty("meituan.search.thread"));
			MyConstant.MEITUAN_CONFIG.SEARCH = Boolean.parseBoolean(infoPros.getProperty("meituan.search"));
			MyConstant.MEITUAN_CONFIG.SEARCH_TIME = infoPros.getProperty("meituan.search_time");
			MyConstant.MEITUAN_CONFIG.PAGE_LIMIT = Integer.valueOf(infoPros.getProperty("meituan.page.limit"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		try {
			MyConstant.DIANPING_CONFIG.THREAD = Integer.valueOf(infoPros.getProperty("dianping.thread"));
			MyConstant.DIANPING_CONFIG.LIMIT = Integer.valueOf(infoPros.getProperty("dianping.limit"));
			MyConstant.DIANPING_CONFIG.SNATCH_THREAD = Integer.valueOf(infoPros.getProperty("dianping.snatch_thread"));
			MyConstant.DIANPING_CONFIG.SNATCH_LIMIT = Integer.valueOf(infoPros.getProperty("dianping.snatch_limit"));
			MyConstant.DIANPING_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("dianping.info"));
			MyConstant.DIANPING_CONFIG.INFO_TIME = infoPros.getProperty("dianping.info_time");

		} catch (Exception e) {
			e.printStackTrace();
		}

			
		try {
			MyConstant.DIANPING_RESTAURANT_CONFIG.CREATE_JOB = Boolean.parseBoolean(infoPros.getProperty("dianping.restaurant.create_job"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.SEARCH = Boolean.parseBoolean(infoPros.getProperty("dianping.restaurant.search"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.SNATCH = Boolean.parseBoolean(infoPros.getProperty("dianping.restaurant.snatch"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("dianping.restaurant.info"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.COMMENT = Boolean.parseBoolean(infoPros.getProperty("dianping.restaurant.comment"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.BRANCH = Boolean.parseBoolean(infoPros.getProperty("dianping.restaurant.branch"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.DISH_PRICE = Boolean.parseBoolean(infoPros.getProperty("dianping.restaurant.dish_price"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.SEARCH_TIME = infoPros.getProperty("dianping.restaurant.search_time");
			MyConstant.DIANPING_RESTAURANT_CONFIG.SNATCH_TIME = infoPros.getProperty("dianping.restaurant.snatch_time");
			MyConstant.DIANPING_RESTAURANT_CONFIG.INFO_TIME = infoPros.getProperty("dianping.restaurant.info_time");
			MyConstant.DIANPING_RESTAURANT_CONFIG.COMMNET_TIME = infoPros.getProperty("dianping.restaurant.comment_time");
			MyConstant.DIANPING_RESTAURANT_CONFIG.THREAD = Integer.parseInt(infoPros.getProperty("dianping.restaurant.thread"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.LIMIT = Integer.parseInt(infoPros.getProperty("dianping.restaurant.limit"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.METHOD = Integer.parseInt(infoPros.getProperty("dianping.restaurant.method"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.LIMIT_PAGE = Integer.parseInt(infoPros.getProperty("dianping.restaurant.limit_page"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.PARA1 = infoPros.getProperty("dianping.restaurant.para1");
			MyConstant.DIANPING_RESTAURANT_CONFIG.PARA2 = Integer.parseInt(infoPros.getProperty("dianping.restaurant.para2"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.SNATCH_LIMIT = Integer.parseInt(infoPros.getProperty("dianping.restaurant.snatchLimit"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.USER = Boolean.parseBoolean(infoPros.getProperty("dianping.restaurant.user"));
			MyConstant.DIANPING_RESTAURANT_CONFIG.SHOP_SUMMARY_INFO = Boolean.parseBoolean(infoPros.getProperty("dianping.restaurant.shop_summary_info"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			MyConstant.ELEME_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("eleme.info"));
			MyConstant.ELEME_CONFIG.COMMENT = Boolean.parseBoolean(infoPros.getProperty("eleme.comment"));
			MyConstant.ELEME_CONFIG.INFO_LIMIT = Integer.parseInt(infoPros.getProperty("eleme.info.limit"));
			MyConstant.ELEME_CONFIG.INFO_THREAD = Integer.parseInt(infoPros.getProperty("eleme.info.thread"));
			MyConstant.ELEME_CONFIG.INFO_TIME = infoPros.getProperty("eleme.info_time");
			MyConstant.ELEME_CONFIG.COMMENT_LIMIT = Integer.parseInt(infoPros.getProperty("eleme.comment.limit"));
			MyConstant.ELEME_CONFIG.COMMENT_THREAD = Integer.parseInt(infoPros.getProperty("eleme.comment.thread"));
			MyConstant.ELEME_CONFIG.COMMENT_TIME = infoPros.getProperty("eleme.comment_time");
			MyConstant.ELEME_CONFIG.PAGE_MAX = Integer.parseInt(infoPros.getProperty("eleme.pagemax"));
			MyConstant.ELEME_CONFIG.PRODUCT_LIMIT = Integer.parseInt(infoPros.getProperty("eleme.product.limit"));
			MyConstant.ELEME_CONFIG.PRODUCT_THREAD = Integer.parseInt(infoPros.getProperty("eleme.product.thread"));
			MyConstant.ELEME_CONFIG.PRODUCT_TIME = infoPros.getProperty("eleme.product_time");
			try {
				MyConstant.ELEME_CONFIG.COMBINE = Boolean.parseBoolean(infoPros.getProperty("eleme.combine"));
			} catch (Exception e) {
				MyConstant.ELEME_CONFIG.COMBINE = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
				MyConstant.BAIDU_WAIMAI_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("baidu_waimai.info"));
				MyConstant.BAIDU_WAIMAI_CONFIG.COMMENT = Boolean.parseBoolean(infoPros.getProperty("baidu_waimai.comment"));
				MyConstant.BAIDU_WAIMAI_CONFIG.INFO_LIMIT = Integer.parseInt(infoPros.getProperty("baidu_waimai.info.limit"));
				MyConstant.BAIDU_WAIMAI_CONFIG.INFO_THREAD = Integer.parseInt(infoPros.getProperty("baidu_waimai.info.thread"));
				MyConstant.BAIDU_WAIMAI_CONFIG.INFO_TIME = infoPros.getProperty("baidu_waimai.info_time");
				MyConstant.BAIDU_WAIMAI_CONFIG.COMMENT_LIMIT = Integer.parseInt(infoPros.getProperty("baidu_waimai.comment.limit"));
				MyConstant.BAIDU_WAIMAI_CONFIG.COMMENT_THREAD = Integer.parseInt(infoPros.getProperty("baidu_waimai.comment.thread"));
				MyConstant.BAIDU_WAIMAI_CONFIG.COMMENT_TIME = infoPros.getProperty("baidu_waimai.comment_time");
				MyConstant.BAIDU_WAIMAI_CONFIG.PAGE_MAX = Integer.parseInt(infoPros.getProperty("baidu_waimai.pagemax"));
				try {
					MyConstant.BAIDU_WAIMAI_CONFIG.COMBINE = Boolean.parseBoolean(infoPros.getProperty("baidu_waimai.combine"));
				} catch (Exception e) {
					MyConstant.BAIDU_WAIMAI_CONFIG.COMBINE = true;
				}

				MyConstant.DAOJIA_CONFIG.SEARCH = Boolean.parseBoolean(infoPros.getProperty("daojia.search"));
				MyConstant.DAOJIA_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("daojia.info"));
				MyConstant.DAOJIA_CONFIG.INFO_LIMIT = Integer.parseInt(infoPros.getProperty("daojia.info.limit"));
				MyConstant.DAOJIA_CONFIG.INFO_THREAD = Integer.parseInt(infoPros.getProperty("daojia.info.thread"));
				MyConstant.DAOJIA_CONFIG.INFO_TIME = infoPros.getProperty("daojia.info_time");
			} catch (Exception e) {
				e.printStackTrace();
			}

		try {
			MyConstant.NODE_ID = Integer.parseInt(infoPros.getProperty("nodeid"));
			MyConstant.UPDATE_CONFIG = Boolean.parseBoolean(infoPros.getProperty("update_config"));
			MyConstant.UPDATE_CONFIG_TIME = infoPros.getProperty("update_config_time");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
       
    }
}
