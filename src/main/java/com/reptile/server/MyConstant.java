package com.reptile.server;

import com.bds.base.util.Constant;
import org.nutz.dao.Dao;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyConstant extends Constant {
    public static int NODE_ID;
    public static boolean UPDATE_CONFIG;
    public static String UPDATE_CONFIG_TIME;
    public static String CONFIG_PATH;

    public static class MEITUAN_CONFIG {
        public static int INFO_THREAD;
        public static int INFO_LIMIT;
        public static int COMMENT_THREAD;
        public static int SEARCH_THREAD;
        public static int COMMENT_LIMIT;
        public static int SEARCH_LIMIT;
        public static int PAGE_LIMIT;
        public static boolean INFO;
        public static boolean COMMNET;
        public static boolean WM_LIST;
        public static boolean SEARCH;
        public static boolean SHOP_KEYWORD_LIST;
        public static boolean SEARCH_COMBINE_INFO;
        public static String INFO_TIME;
        public static String COMMNET_TIME;
        public static String SEARCH_TIME;
        public static String COOKIE;
        public static String WM_COOKIE;
        public static boolean TOKEN;

    }

    public static class DIANPING_CONFIG {
        public static int THREAD;
        public static int LIMIT;
        public static int SNATCH_THREAD;
        public static int SNATCH_LIMIT;
        public static boolean INFO;
        public static String INFO_TIME;

    }

    public static class DIANPING_RESTAURANT_CONFIG {

        public static String DIANPING_RESTAURANT = "dianping";
        public static int THREAD;
        public static int LIMIT;
        public static int USER_THREAD;
        public static int USER_LIMIT;
        public static int IMG_THREAD;
        public static int IMG_LIMIT;
        public static int METHOD;
        public static int LIMIT_PAGE;
        public static boolean CREATE_JOB;
        public static boolean SEARCH;
        public static boolean SNATCH;
        public static boolean INFO;
        public static boolean COMMENT;
        public static boolean BRANCH;
        public static boolean DISH_PRICE;
        public static boolean USER;
        public static boolean SHOP_SUMMARY_INFO;
        public static boolean BRAND;
        public static boolean IMG;
        public static boolean USER_COMMNET;
        public static String SEARCH_TIME;
        public static String SNATCH_TIME;
        public static String INFO_TIME;
        public static String COMMNET_TIME;

        public static String DATE;
        public static int SNATCH_LIMIT;
        public static int PARA2;
        public static String PARA1;
        public static String DIANPING_COOKIE;
        public static String DIANPING_COMMENT_COOKIE;

        public static Map<String, Integer> MAPMAP = new LinkedHashMap<String, Integer>() {
            /**
             *
             */
            private static final long serialVersionUID = -336084742559846922L;

            {
                put("体型：", 1);
                put("恋爱状况：", 2);
                put("生日：", 3);
                put("星座：", 4);
                put("QQ：", 5);
                put("MSN：", 6);
                put("行业职业：", 7);
                put("毕业中学：", 8);
                put("毕业大学：", 9);
                put("个性域名：", 10);
                put("个人主页：", 11);
                put("爱好：", 12);
                put("电影/导演/演员：", 13);
                put("音乐/歌曲/音乐人：", 14);
                put("书/作者：", 15);
                put("菜肴/菜系/餐厅：", 16);
                put("出生地：", 17);
            }
        };
    }

    public static class ELEME_CONFIG {
        public static boolean INFO;
        public static boolean COMMENT;
        public static boolean SEARCH;
        public static boolean COMBINE;

        public static int INFO_LIMIT;
        public static int INFO_THREAD;
        public static int COMMENT_LIMIT;
        public static int COMMENT_THREAD;
        public static int SEARCH_LIMIT;
        public static int SEARCH_THREAD;

        public static String INFO_TIME;
        public static String COMMENT_TIME;
        public static String SEARCH_TIME;
        public static int PAGE_MAX;

        public static int PRODUCT_LIMIT;
        public static int PRODUCT_THREAD;
        public static String PRODUCT_TIME;

        public static boolean GENERATE_COMMENT;
    }

    public static class BAIDU_WAIMAI_CONFIG {
        public static boolean INFO;
        public static boolean COMMENT;
        public static boolean COMBINE;
        public static int INFO_LIMIT;
        public static int INFO_THREAD;
        public static int COMMENT_LIMIT;
        public static int COMMENT_THREAD;
        public static String INFO_TIME;
        public static String COMMENT_TIME;
        public static int PAGE_MAX;

        public static boolean GENERATE_COMMENT;
    }

    public static class DAOJIA_CONFIG {
        public static boolean SEARCH;
        public static boolean INFO;
        public static int INFO_LIMIT;
        public static int INFO_THREAD;
        public static String INFO_TIME;
    }

    public static class MOBILE_ELEME_CONFIG {
        public static boolean INFO;
        public static boolean COMMENT;
        public static boolean SEARCH;
        public static boolean COMBINE;

        public static int INFO_LIMIT;
        public static int INFO_THREAD;
        public static int COMMENT_LIMIT;
        public static int COMMENT_THREAD;
        public static int SEARCH_LIMIT;
        public static int SEARCH_THREAD;

        public static String INFO_TIME;
        public static String COMMENT_TIME;
        public static String SEARCH_TIME;
        public static int PAGE_MAX;

        public static int PRODUCT_LIMIT;
        public static int PRODUCT_THREAD;
        public static String PRODUCT_TIME;

        public static boolean GENERATE_COMMENT;
    }

    public static class MOBILE_MEITUAN_CONFIG {
    	
    	public static int MOBILE_SIMPLE_LIMIT_PAGE;
        public static int MOBILE_CATEGORY_LIMIT_PAGE;

        public static int MOBILE_SEARCH_THREAD;
        public static int MOBILE_LIST_THREAD;
        public static int MOBILE_SHOPKEYWORD_THREAD;
        public static int MOBILE_INFO_THREAD;
        public static int MOBILE_COMMENT_THREAD;

        public static int MOBILE_SEARCH_LIMIT;
        public static int MOBILE_LIST_LIMIT;
        public static int MOBILE_SHOPKEYWORD_LIMIT;
        public static int MOBILE_INFO_LIMIT;
        public static int MOBILE_COMMENT_LIMIT;

        public static boolean MOBILE_INFO;
        public static boolean MOBILE_PRODUCT;
        public static boolean MOBILE_COMMNET;
        public static boolean MOBILE_WM_LIST;
        public static boolean MOBILE_SEARCH;
        public static boolean MOBILE_SHOP_KEYWORD_LIST;
        public static boolean MOBILE_SEARCH_COMBINE_INFO;
        public static boolean MOBILE_INFO_COMBINE_COMMENT;
        public static boolean MOBILE_MONITOR;
        public static boolean MOBILE_SLIDER = false;

        public static String CHROMEDRIVER_PATH;
        public static String MOBILE_INFO_TIME;
        public static String MOBILE_COMMNET_TIME;
        public static String MOBILE_SEARCH_TIME;
        public static String MOBILE_PRODUCT_SHOP_COOKIE;
        public static String MOBILE_RESET_VBS;
        public static String COOKIE;
        public static String IP_PORT_DAILY;
        public static String IP_PORT_ALL;
        

        public static boolean MOBILE_FIRSTCATEGORY;
        
    }

    public static class MOBILE_BAIDU_WAIMAI_CONFIG {
        public static boolean INFO;
        public static boolean COMMENT;
        public static boolean COMBINE;
        public static int INFO_LIMIT;
        public static int INFO_THREAD;
        public static int COMMENT_LIMIT;
        public static int COMMENT_THREAD;
        public static String INFO_TIME;
        public static String COMMENT_TIME;
        public static int PAGE_MAX;

        public static boolean GENERATE_COMMENT;
    }

    public static class NODES_DIVIDE_CONFIG {
        public static boolean SWITCH;
        public static int LIMIT;
        public static int THREAD;
        public static String KEYWORD_TIME;
        public static String URL_TIME;
        public static String PRODUCT_TIME;
        public static String COMMENT_TIME;
    }
    
    public static class DIANPING_RESTAURANT_CREATE_URL {
    	
    	 public static boolean SWITCH;
         public static int LIMIT;
         public static int THREAD;
         public static String TIME;
    }
    
    
    public static class JDDAOJIA_CONFIG {
    	
   	 	public static boolean JDDAOJIA_URL;
        public static int JDDAOJIA_LIMIT;
        public static int JDDAOJIA_THREAD;
        public static String JDDAOJIA_URL_TIME;
        
        public static boolean JDDAOJIA_STORE_PAGE;
        public static int JDDAOJIA_STORE_PAGE_LIMIT;
        public static int JDDAOJIA_STORE_PAGE_THREAD;
        public static String JDDAOJIA_STORE_PAGE_TIME;
        
        public static boolean JDDAOJIA_STORE_SIMPLE;
        public static int JDDAOJIA_STORE_SIMPLE_LIMIT;
        public static int JDDAOJIA_STORE_SIMPLE_THREAD;
        public static String JDDAOJIA_STORE_SIMPLE_TIME;
        
        public static boolean JDDAOJIA_STORE;
        public static int JDDAOJIA_STORE_LIMIT;
        public static int JDDAOJIA_STORE_THREAD;
        public static String JDDAOJIA_STORE_TIME;
        public static boolean JDDAOJIA_COMMENT;
        public static int JDDAOJIA_COMMENT_LIMIT;
        public static int JDDAOJIA_COMMENT_THREAD;
        public static String JDDAOJIA_COMMENT_TIME;
        public static boolean JDDAOJIA_PRODUCT;
        public static int JDDAOJIA_PRODUCT_LIMIT;
        public static int JDDAOJIA_PRODUCT_THREAD;
        public static String JDDAOJIA_PRODUCT_TIME;
   }
    
    public static class DIDI_APP {
        public static int INFO_THREAD;
        public static int INFO_LIMIT;
        
        public static int SEARCH_THREAD;
        public static int SEARCH_LIMIT;
        
        public static int PAGE_LIMIT;
        
        public static boolean INFO;
        public static boolean SEARCH;
        
        public static String INFO_TIME;
        public static String SEARCH_TIME;
        
        public static Dao BIDAO;

    }
}
