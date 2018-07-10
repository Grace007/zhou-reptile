package com.reptile.server;

import com.bds.base.util.CmdUtil;
import com.bds.base.xml.IMyReadConfig;
import com.reptile.util.ConfigJsonParseUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

public class MyConfig implements IMyReadConfig {
    public List<String> readConfig(List<String> springConfig, List<String> propsConfig) {

    	try {
            CmdUtil.taskkill("chromedriver.exe");
            CmdUtil.taskkill("phantomjs.exe");
            CmdUtil.taskkill("firefox.exe");

            CmdUtil.taskkill("PING.exe");
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
//    	try {
//    		PoolConfig config = new PoolConfig();
//            config.setMaxTotal(3);
//            config.setMaxIdle(3);
//            MyConstant.DRIVER_POOL = new ObjPool(config);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
        String path = MyConfig.getCurrentPath();

        // String config_file = path + File.separator + "conf" + File.separator;

        String config_path = System.getProperty("config_path");
        String config_file = "";
        if (config_path != null && !config_path.equals("")) {
            config_file = path + File.separator + config_path + File.separator + "conf" + File.separator;
        } else {
            config_file = path + File.separator + "conf" + File.separator;
        }
        MyConstant.CONFIG_PATH = config_file;

        for (String po : propsConfig) {
            String index = po.substring(0, po.indexOf("."));

            if (index.equals("config")) {
                Properties configPros = new Properties();
                try {
                    configPros.load(new FileInputStream(config_file + "config.properties"));
                    String proxy = configPros.getProperty("proxy");
                    if (proxy != null && !proxy.equals("")) {
                        MyConstant.proxy = Boolean.parseBoolean(configPros.getProperty("proxy"));
                    } else {
                        MyConstant.proxy = false;
                    }
                    String ipproxyServer = configPros.getProperty("ipproxy.server");
                    if (ipproxyServer != null && !ipproxyServer.equals("")) {
                        MyConstant.IPProxyServer = Boolean.parseBoolean(ipproxyServer);
                    } else {
                        MyConstant.IPProxyServer = false;
                    }
                    String ipproxyPort = configPros.getProperty("ipproxy.port");
                    if (ipproxyPort != null && !ipproxyPort.equals("")) {
                        MyConstant.IPProxyProt = Integer.parseInt(configPros.getProperty("ipproxy.port"));
                    } else {
                        MyConstant.IPProxyProt = 8088;
                    }

                    MyConstant.Node = configPros.getProperty("node");

                    MyConstant.ZNode = configPros.getProperty("znode");
                    MyConstant.AdslName = configPros.getProperty("adsl.name");
                    MyConstant.AdslPwd = configPros.getProperty("adsl.pwd");
                    MyConstant.AdslTitle = configPros.getProperty("adsl.title");
                    MyConstant.vps = Boolean.parseBoolean(configPros.getProperty("vps"));
                } catch (Exception ex) {

                }
            } else if (index.equals("info")) {
                Properties infoPros = new Properties();

                try {
                    infoPros.load(new FileInputStream(config_file + "info.properties"));

                    try {
                    	ConfigJsonParseUtils.parseConfigJson(infoPros);
					} catch (Exception e) {
						// TODO: handle exception
					}

                    try {

                        MyConstant.MEITUAN_CONFIG.INFO_THREAD = Integer
                                .valueOf(infoPros.getProperty("meituan.info.thread"));
                        MyConstant.MEITUAN_CONFIG.INFO_LIMIT = Integer
                                .valueOf(infoPros.getProperty("meituan.info.limit"));
                        MyConstant.MEITUAN_CONFIG.COMMENT_THREAD = Integer
                                .valueOf(infoPros.getProperty("meituan.comment.thread"));
                        MyConstant.MEITUAN_CONFIG.COMMENT_LIMIT = Integer
                                .valueOf(infoPros.getProperty("meituan.comment.limit"));
                        MyConstant.MEITUAN_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("meituan.info"));
                        MyConstant.MEITUAN_CONFIG.COMMNET = Boolean
                                .parseBoolean(infoPros.getProperty("meituan.comment"));
                        MyConstant.MEITUAN_CONFIG.WM_LIST = Boolean
                                .parseBoolean(infoPros.getProperty("meituan.waimai.list"));
                        MyConstant.MEITUAN_CONFIG.SHOP_KEYWORD_LIST = Boolean
                                .parseBoolean(infoPros.getProperty("meituan.shop.keyword.list"));
                        MyConstant.MEITUAN_CONFIG.SEARCH_COMBINE_INFO = Boolean
                                .parseBoolean(infoPros.getProperty("meituan.search.combine.info"));
                        MyConstant.MEITUAN_CONFIG.INFO_TIME = infoPros.getProperty("meituan.info_time");
                        MyConstant.MEITUAN_CONFIG.COMMNET_TIME = infoPros.getProperty("meituan.comment_time");
                        MyConstant.MEITUAN_CONFIG.COOKIE = infoPros.getProperty("meituan.cookie");
                        MyConstant.MEITUAN_CONFIG.WM_COOKIE = infoPros.getProperty("meituan.waimai.cookie");
                        MyConstant.MEITUAN_CONFIG.SEARCH_LIMIT = Integer
                                .valueOf(infoPros.getProperty("meituan.search.limit"));
                        MyConstant.MEITUAN_CONFIG.SEARCH_THREAD = Integer
                                .valueOf(infoPros.getProperty("meituan.search.thread"));
                        MyConstant.MEITUAN_CONFIG.SEARCH = Boolean.parseBoolean(infoPros.getProperty("meituan.search"));
                        MyConstant.MEITUAN_CONFIG.SEARCH_TIME = infoPros.getProperty("meituan.search_time");
                        MyConstant.MEITUAN_CONFIG.PAGE_LIMIT = Integer
                                .valueOf(infoPros.getProperty("meituan.page.limit"));
                        try {
                        	MyConstant.MEITUAN_CONFIG.TOKEN = Boolean
                                    .parseBoolean(infoPros.getProperty("meituan.token"));
						} catch (Exception e) {
							// TODO: handle exception
						}

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {

                    	try {
                    		 MyConstant.DIANPING_RESTAURANT_CONFIG.IMG = Boolean
                                     .parseBoolean(infoPros.getProperty("dianping.restaurant.img"));
						} catch (Exception e) {
							// TODO: handle exception
						}
                        MyConstant.DIANPING_CONFIG.THREAD = Integer.valueOf(infoPros.getProperty("dianping.thread"));
                        MyConstant.DIANPING_CONFIG.LIMIT = Integer.valueOf(infoPros.getProperty("dianping.limit"));
                        MyConstant.DIANPING_CONFIG.SNATCH_THREAD = Integer
                                .valueOf(infoPros.getProperty("dianping.snatch_thread"));
                        MyConstant.DIANPING_CONFIG.SNATCH_LIMIT = Integer
                                .valueOf(infoPros.getProperty("dianping.snatch_limit"));
                        MyConstant.DIANPING_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("dianping.info"));
                        MyConstant.DIANPING_CONFIG.INFO_TIME = infoPros.getProperty("dianping.info_time");

                        MyConstant.DIANPING_RESTAURANT_CONFIG.CREATE_JOB = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.create_job"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.SEARCH = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.search"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.SNATCH = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.snatch"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.INFO = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.info"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.COMMENT = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.comment"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.BRANCH = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.branch"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.DISH_PRICE = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.dish_price"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.SEARCH_TIME = infoPros
                                .getProperty("dianping.restaurant.search_time");
                        MyConstant.DIANPING_RESTAURANT_CONFIG.SNATCH_TIME = infoPros
                                .getProperty("dianping.restaurant.snatch_time");
                        MyConstant.DIANPING_RESTAURANT_CONFIG.INFO_TIME = infoPros
                                .getProperty("dianping.restaurant.info_time");
                        MyConstant.DIANPING_RESTAURANT_CONFIG.COMMNET_TIME = infoPros
                                .getProperty("dianping.restaurant.comment_time");
                        MyConstant.DIANPING_RESTAURANT_CONFIG.THREAD = Integer
                                .parseInt(infoPros.getProperty("dianping.restaurant.thread"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.LIMIT = Integer
                                .parseInt(infoPros.getProperty("dianping.restaurant.limit"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.METHOD = Integer
                                .parseInt(infoPros.getProperty("dianping.restaurant.method"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.LIMIT_PAGE = Integer
                                .parseInt(infoPros.getProperty("dianping.restaurant.limit_page"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.PARA1 = infoPros.getProperty("dianping.restaurant.para1");
                        MyConstant.DIANPING_RESTAURANT_CONFIG.PARA2 = Integer
                                .parseInt(infoPros.getProperty("dianping.restaurant.para2"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.SNATCH_LIMIT = Integer
                                .parseInt(infoPros.getProperty("dianping.restaurant.snatchLimit"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.USER = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.user"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.SHOP_SUMMARY_INFO = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.shop_summary_info"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.BRAND = Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.brand"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.USER_LIMIT=Integer.valueOf(infoPros.getProperty("dianping.restaurant.user_limit"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.USER_THREAD=Integer.valueOf(infoPros.getProperty("dianping.restaurant.user_thread"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.USER_COMMNET=Boolean
                                .parseBoolean(infoPros.getProperty("dianping.restaurant.user_comment"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.IMG_LIMIT=Integer.valueOf(infoPros.getProperty("dianping.restaurant.img_limit"));
                        MyConstant.DIANPING_RESTAURANT_CONFIG.IMG_THREAD=Integer.valueOf(infoPros.getProperty("dianping.restaurant.img_thread"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                    	MyConstant.ELEME_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("eleme.info"));
                        MyConstant.ELEME_CONFIG.COMMENT = Boolean.parseBoolean(infoPros.getProperty("eleme.comment"));
                        MyConstant.ELEME_CONFIG.INFO_LIMIT = Integer.parseInt(infoPros.getProperty("eleme.info.limit"));
                        MyConstant.ELEME_CONFIG.INFO_THREAD = Integer.parseInt(infoPros.getProperty("eleme.info.thread"));
                        MyConstant.ELEME_CONFIG.INFO_TIME = infoPros.getProperty("eleme.info_time");
                        MyConstant.ELEME_CONFIG.COMMENT_LIMIT = Integer
                                .parseInt(infoPros.getProperty("eleme.comment.limit"));
                        MyConstant.ELEME_CONFIG.COMMENT_THREAD = Integer
                                .parseInt(infoPros.getProperty("eleme.comment.thread"));
                        MyConstant.ELEME_CONFIG.COMMENT_TIME = infoPros.getProperty("eleme.comment_time");
                        MyConstant.ELEME_CONFIG.PAGE_MAX = Integer.parseInt(infoPros.getProperty("eleme.pagemax"));
                        MyConstant.ELEME_CONFIG.PRODUCT_LIMIT = Integer
                                .parseInt(infoPros.getProperty("eleme.product.limit"));
                        MyConstant.ELEME_CONFIG.PRODUCT_THREAD = Integer
                                .parseInt(infoPros.getProperty("eleme.product.thread"));
                        MyConstant.ELEME_CONFIG.PRODUCT_TIME = infoPros.getProperty("eleme.product_time");
                        try {
                            MyConstant.ELEME_CONFIG.COMBINE = Boolean.parseBoolean(infoPros.getProperty("eleme.combine"));
                            MyConstant.ELEME_CONFIG.GENERATE_COMMENT = Boolean
                                    .parseBoolean(infoPros.getProperty("eleme.generate_comment"));
                        } catch (Exception e) {
                            MyConstant.ELEME_CONFIG.COMBINE = true;
                            MyConstant.ELEME_CONFIG.GENERATE_COMMENT = true;
                        }
					} catch (Exception e) {
						// TODO: handle exception
					}
                    
                    try {
                        MyConstant.MOBILE_ELEME_CONFIG.INFO = Boolean
                                .parseBoolean(infoPros.getProperty("mobile_eleme.info"));
                        MyConstant.MOBILE_ELEME_CONFIG.COMMENT = Boolean
                                .parseBoolean(infoPros.getProperty("mobile_eleme.comment"));
                        MyConstant.MOBILE_ELEME_CONFIG.INFO_LIMIT = Integer
                                .parseInt(infoPros.getProperty("mobile_eleme.info.limit"));
                        MyConstant.MOBILE_ELEME_CONFIG.INFO_THREAD = Integer
                                .parseInt(infoPros.getProperty("mobile_eleme.info.thread"));
                        MyConstant.MOBILE_ELEME_CONFIG.INFO_TIME = infoPros.getProperty("mobile_eleme.info_time");
                        MyConstant.MOBILE_ELEME_CONFIG.COMMENT_LIMIT = Integer
                                .parseInt(infoPros.getProperty("mobile_eleme.comment.limit"));
                        MyConstant.MOBILE_ELEME_CONFIG.COMMENT_THREAD = Integer
                                .parseInt(infoPros.getProperty("mobile_eleme.comment.thread"));
                        MyConstant.MOBILE_ELEME_CONFIG.COMMENT_TIME = infoPros.getProperty("mobile_eleme.comment_time");
                        MyConstant.MOBILE_ELEME_CONFIG.PAGE_MAX = Integer
                                .parseInt(infoPros.getProperty("mobile_eleme.pagemax"));
                        MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_LIMIT = Integer
                                .parseInt(infoPros.getProperty("mobile_eleme.product.limit"));
                        MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_THREAD = Integer
                                .parseInt(infoPros.getProperty("mobile_eleme.product.thread"));
                        MyConstant.MOBILE_ELEME_CONFIG.PRODUCT_TIME = infoPros.getProperty("mobile_eleme.product_time");
                        try {
                            MyConstant.MOBILE_ELEME_CONFIG.COMBINE = Boolean
                                    .parseBoolean(infoPros.getProperty("mobile_eleme.combine"));
                            MyConstant.MOBILE_ELEME_CONFIG.GENERATE_COMMENT = Boolean
                                    .parseBoolean(infoPros.getProperty("mobile_eleme.generate_comment"));
                        } catch (Exception e) {
                            MyConstant.MOBILE_ELEME_CONFIG.COMBINE = true;
                            MyConstant.MOBILE_ELEME_CONFIG.GENERATE_COMMENT = true;
                        }
                        MyConstant.MOBILE_ELEME_CONFIG.SEARCH = Boolean
                                .parseBoolean(infoPros.getProperty("mobile_eleme.search.info"));
                        MyConstant.MOBILE_ELEME_CONFIG.SEARCH_LIMIT = Integer
                                .parseInt(infoPros.getProperty("mobile_eleme.search.limit"));
                        MyConstant.MOBILE_ELEME_CONFIG.SEARCH_THREAD = Integer
                                .parseInt(infoPros.getProperty("mobile_eleme.serach.thread"));
                        MyConstant.MOBILE_ELEME_CONFIG.SEARCH_TIME = infoPros
                                .getProperty("mobile_eleme.search.search_time");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    
                    try {
                    	 MyConstant.BAIDU_WAIMAI_CONFIG.INFO = Boolean
                                 .parseBoolean(infoPros.getProperty("baidu_waimai.info"));
                         MyConstant.BAIDU_WAIMAI_CONFIG.COMMENT = Boolean
                                 .parseBoolean(infoPros.getProperty("baidu_waimai.comment"));
                         MyConstant.BAIDU_WAIMAI_CONFIG.INFO_LIMIT = Integer
                                 .parseInt(infoPros.getProperty("baidu_waimai.info.limit"));
                         MyConstant.BAIDU_WAIMAI_CONFIG.INFO_THREAD = Integer
                                 .parseInt(infoPros.getProperty("baidu_waimai.info.thread"));
                         MyConstant.BAIDU_WAIMAI_CONFIG.INFO_TIME = infoPros.getProperty("baidu_waimai.info_time");
                         MyConstant.BAIDU_WAIMAI_CONFIG.COMMENT_LIMIT = Integer
                                 .parseInt(infoPros.getProperty("baidu_waimai.comment.limit"));
                         MyConstant.BAIDU_WAIMAI_CONFIG.COMMENT_THREAD = Integer
                                 .parseInt(infoPros.getProperty("baidu_waimai.comment.thread"));
                         MyConstant.BAIDU_WAIMAI_CONFIG.COMMENT_TIME = infoPros.getProperty("baidu_waimai.comment_time");
                         MyConstant.BAIDU_WAIMAI_CONFIG.PAGE_MAX = Integer
                                 .parseInt(infoPros.getProperty("baidu_waimai.pagemax"));
                         try {
                             MyConstant.BAIDU_WAIMAI_CONFIG.COMBINE = Boolean
                                     .parseBoolean(infoPros.getProperty("baidu_waimai.combine"));
                             MyConstant.BAIDU_WAIMAI_CONFIG.GENERATE_COMMENT = Boolean
                                     .parseBoolean(infoPros.getProperty("baidu_waimai.generate_comment"));
                         } catch (Exception e) {
                             MyConstant.BAIDU_WAIMAI_CONFIG.COMBINE = true;
                             MyConstant.BAIDU_WAIMAI_CONFIG.GENERATE_COMMENT = true;
                         }
					} catch (Exception e) {
						// TODO: handle exception
					}

                    try {
                        MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO = Boolean
                                .parseBoolean(infoPros.getProperty("mobile_baidu_waimai.info"));
                        MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMMENT = Boolean
                                .parseBoolean(infoPros.getProperty("mobile_baidu_waimai.comment"));
                        MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_LIMIT = Integer
                                .parseInt(infoPros.getProperty("mobile_baidu_waimai.info.limit"));
                        MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_THREAD = Integer
                                .parseInt(infoPros.getProperty("mobile_baidu_waimai.info.thread"));
                        MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.INFO_TIME = infoPros
                                .getProperty("mobile_baidu_waimai.info_time");
                        MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMMENT_LIMIT = Integer
                                .parseInt(infoPros.getProperty("mobile_baidu_waimai.comment.limit"));
                        MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMMENT_THREAD = Integer
                                .parseInt(infoPros.getProperty("mobile_baidu_waimai.comment.thread"));
                        MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMMENT_TIME = infoPros
                                .getProperty("mobile_baidu_waimai.comment_time");
                        MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.PAGE_MAX = Integer
                                .parseInt(infoPros.getProperty("mobile_baidu_waimai.pagemax"));
                        try {
                            MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMBINE = Boolean
                                    .parseBoolean(infoPros.getProperty("mobile_baidu_waimai.combine"));
                            MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.GENERATE_COMMENT = Boolean
                                    .parseBoolean(infoPros.getProperty("mobile_baidu_waimai.generate_comment"));
                        } catch (Exception e) {
                            MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.COMBINE = true;
                            MyConstant.MOBILE_BAIDU_WAIMAI_CONFIG.GENERATE_COMMENT = true;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    try {
                    	try {
							MyConstant.MOBILE_MEITUAN_CONFIG.IP_PORT_DAILY = infoPros.getProperty("mobile.meituan.ip.port.daily");
							if (StringUtils.isEmpty(MyConstant.MOBILE_MEITUAN_CONFIG.IP_PORT_DAILY)) {
								MyConstant.MOBILE_MEITUAN_CONFIG.IP_PORT_DAILY = "211.152.47.68:7117" ;
							}
							MyConstant.MOBILE_MEITUAN_CONFIG.IP_PORT_ALL= infoPros.getProperty("mobile.meituan.ip.port.all");
							if (StringUtils.isEmpty(MyConstant.MOBILE_MEITUAN_CONFIG.IP_PORT_ALL)) {
								MyConstant.MOBILE_MEITUAN_CONFIG.IP_PORT_ALL = "211.152.47.94:7117" ;
							}
						} catch (Exception e) {
							System.out.println("获取mobile.meituan.ip.port失败");
						}
                    	
//                    	此处读取simple和category的最大页数
                    	try {
                    		MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SIMPLE_LIMIT_PAGE=Integer
                                    .valueOf(infoPros.getProperty("mobile.meituan.simple.limit.page"));
                    		
							MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_CATEGORY_LIMIT_PAGE=Integer
	                                .valueOf(infoPros.getProperty("mobile.meituan.category.limit.page"));
                    		
						} catch (Exception e) {
							MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SIMPLE_LIMIT_PAGE=20;
							MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_CATEGORY_LIMIT_PAGE=10;
//							此处向配置文件中写入配置
							try {
								String simple="mobile.meituan.simple.limit.page=20";
								String category="mobile.meituan.category.limit.page=10";
								FileWriter fw=new FileWriter(config_file+"/info.properties",true);
								fw.write("\n"+simple+"\n"+category);
								fw.flush();
								fw.close();
							} catch (Exception e2) {
								System.out.println("往配置文件中写入配置失败");
							}
						}

                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SEARCH_THREAD = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.search.thread"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SEARCH_LIMIT = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.search.limit"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_LIST_THREAD = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.waimai.list.thread"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_LIST_LIMIT = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.waimai.list.limit"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SHOPKEYWORD_THREAD = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.shop.keyword.thread"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SHOPKEYWORD_LIMIT = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.shop.keyword.limit"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_INFO_THREAD = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.info.thread"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_INFO_LIMIT = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.info.limit"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_COMMENT_THREAD = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.comment.thread"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_COMMENT_LIMIT = Integer
                                .valueOf(infoPros.getProperty("mobile.meituan.comment.limit"));

                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SEARCH = Boolean
                                .parseBoolean(infoPros.getProperty("mobile.meituan_search"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SEARCH_COMBINE_INFO = Boolean
                                .parseBoolean(infoPros.getProperty("mobile.meituan.search.combine.info"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_INFO_COMBINE_COMMENT = Boolean
                                .parseBoolean(infoPros.getProperty("mobile.meituan.info.combine.comment"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_WM_LIST = Boolean
                                .parseBoolean(infoPros.getProperty("mobile.meituan.waimai.list"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SHOP_KEYWORD_LIST = Boolean
                                .parseBoolean(infoPros.getProperty("mobile.meituan.shop.keyword.list"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_INFO = Boolean
                                .parseBoolean(infoPros.getProperty("mobile.meituan_info"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_PRODUCT = Boolean
                                .parseBoolean(infoPros.getProperty("mobile.meituan_prodcuct"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_COMMNET = Boolean
                                .parseBoolean(infoPros.getProperty("mobile.meituan_commment"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_MONITOR = Boolean
                                .parseBoolean(infoPros.getProperty("mobile.meituan_monitor"));
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_SEARCH_TIME = infoPros.getProperty("mobile.meituan.search_time");
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_INFO_TIME = infoPros.getProperty("mobile.meituan.info_time");
                        MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_COMMNET_TIME = infoPros.getProperty("mobile.meituan.comment_time");
                        try {
                        	 MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_FIRSTCATEGORY = Boolean
                                     .parseBoolean(infoPros.getProperty("mobile.meituan_firstcategory"));
						} catch (Exception e) {
							// TODO: handle exception
						}
                        
                        try {
                        	MyConstant.MOBILE_MEITUAN_CONFIG.CHROMEDRIVER_PATH = String
                                    .valueOf(infoPros.getProperty("mobile.chromedriver.path"));
						} catch (Exception e) {
							// TODO: handle exception
						}
                        try {
                        	MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_RESET_VBS = String
                            .valueOf(infoPros.getProperty("mobile.reset.vbs.path"));
                        	System.out.println(MyConstant.MOBILE_MEITUAN_CONFIG.MOBILE_RESET_VBS);
						} catch (Exception e) {
							// TODO: handle exception
						}
                        

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        MyConstant.NODES_DIVIDE_CONFIG.SWITCH = Boolean.parseBoolean(infoPros.getProperty("node_divide.switch"));
                        MyConstant.NODES_DIVIDE_CONFIG.LIMIT = Integer.parseInt(infoPros.getProperty("node_divide.limit"));
                        MyConstant.NODES_DIVIDE_CONFIG.THREAD = Integer.parseInt(infoPros.getProperty("node_divide.thread"));
                        MyConstant.NODES_DIVIDE_CONFIG.KEYWORD_TIME = infoPros.getProperty("node_divide.kt");
                        MyConstant.NODES_DIVIDE_CONFIG.URL_TIME = infoPros.getProperty("node_divide.ut");
                        MyConstant.NODES_DIVIDE_CONFIG.PRODUCT_TIME = infoPros.getProperty("node_divide.pt");
                        MyConstant.NODES_DIVIDE_CONFIG.COMMENT_TIME = infoPros.getProperty("node_divide.ct");
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                    
                    try {
                        MyConstant.DIANPING_RESTAURANT_CREATE_URL.SWITCH = Boolean.parseBoolean(infoPros.getProperty("dianping.url.switch"));
                        MyConstant.DIANPING_RESTAURANT_CREATE_URL.LIMIT = Integer.parseInt(infoPros.getProperty("dianping.url.limit"));
                        MyConstant.DIANPING_RESTAURANT_CREATE_URL.THREAD = Integer.parseInt(infoPros.getProperty("dianping.url.thread"));
                        MyConstant.DIANPING_RESTAURANT_CREATE_URL.TIME = infoPros.getProperty("dianping.url.time");
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                    try {
						MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_URL =Boolean.parseBoolean(infoPros.getProperty("jddaojia_url"));
						MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_THREAD = Integer.parseInt(infoPros.getProperty("jddaojia_thread"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_LIMIT = Integer.parseInt(infoPros.getProperty("jddaojia_limit"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_URL_TIME = infoPros.getProperty("jddaojia_url_time");
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE =Boolean.parseBoolean(infoPros.getProperty("jddaojia_store"));
						MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_THREAD = Integer.parseInt(infoPros.getProperty("jddaojia_store_thread"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_LIMIT= Integer.parseInt(infoPros.getProperty("jddaojia_store_limit"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_TIME = infoPros.getProperty("jddaojia_store_time");
                        
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_SIMPLE =Boolean.parseBoolean(infoPros.getProperty("jddaojia_store_simple"));
						MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_SIMPLE_THREAD = Integer.parseInt(infoPros.getProperty("jddaojia_store_simple_thread"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_SIMPLE_LIMIT= Integer.parseInt(infoPros.getProperty("jddaojia_store_simple_limit"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_SIMPLE_TIME = infoPros.getProperty("jddaojia_store_simple_time");
                        
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_PAGE =Boolean.parseBoolean(infoPros.getProperty("jddaojia_store_page"));
						MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_PAGE_THREAD = Integer.parseInt(infoPros.getProperty("jddaojia_store_page_thread"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_PAGE_LIMIT= Integer.parseInt(infoPros.getProperty("jddaojia_store_page_limit"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_STORE_PAGE_TIME = infoPros.getProperty("jddaojia_store_page_time");
                        
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_COMMENT =Boolean.parseBoolean(infoPros.getProperty("jddaojia_comment"));
						MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_COMMENT_THREAD = Integer.parseInt(infoPros.getProperty("jddaojia_comment_thread"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_COMMENT_LIMIT= Integer.parseInt(infoPros.getProperty("jddaojia_comment_limit"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_COMMENT_TIME = infoPros.getProperty("jddaojia_comment_time");
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_PRODUCT =Boolean.parseBoolean(infoPros.getProperty("jddaojia_product"));
						MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_PRODUCT_THREAD = Integer.parseInt(infoPros.getProperty("jddaojia_product_thread"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_PRODUCT_LIMIT= Integer.parseInt(infoPros.getProperty("jddaojia_product_limit"));
                        MyConstant.JDDAOJIA_CONFIG.JDDAOJIA_PRODUCT_TIME = infoPros.getProperty("jddaojia_product_time");
						
					} catch (Exception e) {
						
					}

                    MyConstant.DAOJIA_CONFIG.SEARCH = Boolean.parseBoolean(infoPros.getProperty("daojia.search"));
                    MyConstant.DAOJIA_CONFIG.INFO = Boolean.parseBoolean(infoPros.getProperty("daojia.info"));
                    MyConstant.DAOJIA_CONFIG.INFO_LIMIT = Integer.parseInt(infoPros.getProperty("daojia.info.limit"));
                    MyConstant.DAOJIA_CONFIG.INFO_THREAD = Integer.parseInt(infoPros.getProperty("daojia.info.thread"));
                    MyConstant.DAOJIA_CONFIG.INFO_TIME = infoPros.getProperty("daojia.info_time");

                    MyConstant.NODE_ID = Integer.parseInt(infoPros.getProperty("nodeid"));
                    MyConstant.UPDATE_CONFIG = Boolean.parseBoolean(infoPros.getProperty("update_config"));
                    MyConstant.UPDATE_CONFIG_TIME = infoPros.getProperty("update_config_time");
                } catch (Exception ex) {

                }
            } else if (index.equals("dataSource")) {
                String dataSource_file = config_file + po;
                System.setProperty("CONFIG_FILE", "file:" + dataSource_file);

            } else if (index.equals("log4j")) {
                String log4j_file = config_file + "log4j.properties";
                PropertyConfigurator.configure(log4j_file);

            }
        }

        return springConfig;
    }

    public static String getCurrentPath() {

        String current_path = System.getProperty("user.dir");
        System.out.println(current_path);
        if (StringUtils.isEmpty(current_path)) {
            File directory = new File("");
            try {
                current_path = directory.getCanonicalPath();
            } catch (Exception e) {
            }
            if (current_path.endsWith("bin")) {
                current_path = current_path.substring(0, current_path.length() - 4);
            }
        } else {
            if (current_path.endsWith("bin")) {
                current_path = current_path.substring(0, current_path.length() - 4);
            }
        }

        System.out.println("current path is:" + current_path);

        return current_path;
    }
}
