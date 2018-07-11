package com.reptile.ec.jd.task.thread;

import com.bds.base.http.HttpBase;
import com.bds.base.util.CommonUtils;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.jd.job.JdShopUrl;
import com.reptile.ec.jd.job.JdShopUrlNext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JdShopUrlThread extends Thread {
    private static Logger logger = Logger.getLogger(JdShopUrlThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private JdShopUrl task;

    public JdShopUrlThread(JdShopUrl task) {
        this.task = task;
    }

    public void run() {
        logger.info("开始生成京东shop_url_next任务...");
        tDao.update(JdShopUrl.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        String shop_url = task.getShop_url();
        String result;
        try {
            result = HttpBase.get(shop_url, "utf-8").getResult();
            Document doc = Jsoup.parse(result);
            //先获取必要的url参数
            List<String> p_list = new ArrayList<String>();
            String appId = "", orderBy = "", direction = "", categoryId = "", pagePrototypeId = "", pageInstanceId, moduleInstanceId = null, prototypeId = null, templateId = null, layoutInstanceId = null, shopId, venderId = null;
            String params = "{" + CommonUtils.subString("var params = {", "}", doc.select("script").toString()) + "}";
            JSONObject Jparams = new JSONObject(params);
            if (Jparams.has("appId")) {
                appId = Jparams.getString("appId");
            }
            p_list.add(appId);
            if (Jparams.has("orderBy")) {
                orderBy = Jparams.getString("orderBy");
            }
            p_list.add(orderBy);
            if (Jparams.has("direction")) {
                direction = Jparams.getString("direction");
            }
            p_list.add(direction);
            if (Jparams.has("categoryId")) {
                categoryId = Jparams.getString("categoryId");
            }
            p_list.add(categoryId);
            if (Jparams.has("pagePrototypeId")) {
                pagePrototypeId = Jparams.getString("pagePrototypeId");
            }
            p_list.add(pagePrototypeId);
            pageInstanceId = doc.select("#pageInstance_id").attr("value");
            p_list.add(pageInstanceId);
            shopId = doc.select("#shop_id").attr("value");
            p_list.add(shopId);
            venderId = doc.select("#vender_id").attr("value");
            p_list.add(venderId);
            
            
            /*moduleInstanceId = doc.select(".d-w990").select(".m_render_structure").attr("m_render_layout_instance_id");
            p_list.add(moduleInstanceId);
            prototypeId = doc.select(".d-w990").select(".m_render_structure").attr("m_render_prototype_id");
            p_list.add(prototypeId);
            templateId = doc.select(".d-w990").select(".m_render_structure").attr("m_render_template_id");
            p_list.add(templateId);
            layoutInstanceId = moduleInstanceId;
            p_list.add(layoutInstanceId);*/
            
            
            
            String first_page_url=null;
            String first_page_result;
            String temp = null;
            String moduleInstanceIdTemp,prototypeIdTemp,templateIdTemp,layoutInstanceIdTemp;
            
            
            Elements paramIdsElements = doc.select("div.m_render_structure");
            for (int i = 0; i < paramIdsElements.size(); i++) {
            	
            	Element element = paramIdsElements.get(i);
            	moduleInstanceIdTemp = element.attr("m_render_layout_instance_id");
            	prototypeIdTemp = element.attr("m_render_prototype_id");
            	templateIdTemp = element.attr("m_render_template_id");
            	layoutInstanceIdTemp = moduleInstanceIdTemp;
            	first_page_url = "https://module-jshop.jd.com/module/getModuleHtml.html?appId=" + appId + "&orderBy=" + orderBy + "&pageNo=1&direction=" + direction + "&categoryId=" + categoryId + "&pageSize=24&pagePrototypeId=" + pagePrototypeId + "&pageInstanceId=" + pageInstanceId + "&moduleInstanceId=" + moduleInstanceIdTemp + "&prototypeId=" + prototypeIdTemp + "&templateId=" + templateIdTemp + "&layoutInstanceId=" + layoutInstanceIdTemp + "&origin=0&shopId=" + shopId + "&venderId=" + venderId + "&_=" + System.currentTimeMillis();
            	first_page_result = HttpBase.get(first_page_url, "utf-8").getResult();
            	if (i == 0){
            		temp = first_page_result;
            		moduleInstanceId = moduleInstanceIdTemp;
            		prototypeId = prototypeIdTemp;
            		templateId =templateIdTemp;
            		layoutInstanceId = layoutInstanceIdTemp;
            	}
            	else {
					if (first_page_result.length() >= temp.length()) {
						temp = first_page_result;
						moduleInstanceId = moduleInstanceIdTemp;
	            		prototypeId = prototypeIdTemp;
	            		templateId =templateIdTemp;
	            		layoutInstanceId = layoutInstanceIdTemp;
					}
				}
            	
            	
            	
			}
            
            
            
            
            
            
            
            
            
            
            //判断所有参数非空
            if (!notExistEmpty(p_list)) {
                logger.info("存在参数未获取到");
                tDao.update(JdShopUrl.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
                return;
            }
            //获取店铺id、name、url
            String shop_header_url = "https://mall.jd.com/view/getJshopHeader.html?appId=" + appId + "&_=" + System.currentTimeMillis();
            String shop_header_result;
            shop_header_result = HttpBase.get(shop_header_url, "utf-8").getResult();
            String html = new JSONObject(shop_header_result.substring(1, shop_header_result.trim().length() - 2)).getString("html");
            Document shop_doc = Jsoup.parse(html);
            String store_id = shop_doc.select("#shop_id").attr("value");
            if (StringUtils.isEmpty(store_id)) {
				store_id = shopId;
			}
            String store_url = "http:" + shop_doc.select(".jLogo").select("a").attr("href");
            String store_name = shop_doc.select(".jLogo").select("a").text();
            
            
            
            
            Document page_doc = Jsoup.parse(temp);
            int count = 0, page_total = 1;
            if (StringUtils.isNotEmpty(page_doc.select(".jPage").toString())) {
                //分页
                count = Integer.parseInt(CommonUtils.subString("共", "条记录", page_doc.select(".jPage").select("em").toString()));
                page_total = (count / 24) + 1;
                for (int i = 0; i < page_total; i++) {
                    String next_page_url = "https://module-jshop.jd.com/module/getModuleHtml.html?appId=" + appId + "&orderBy=" + orderBy + "&pageNo=" + (i + 1) + "&direction=" + direction + "&categoryId=" + categoryId + "&pageSize=24&pagePrototypeId=" + pagePrototypeId + "&pageInstanceId=" + pageInstanceId + "&moduleInstanceId=" + moduleInstanceId + "&prototypeId=" + prototypeId + "&templateId=" + templateId + "&layoutInstanceId=" + layoutInstanceId + "&origin=0&shopId=" + shopId + "&venderId=" + venderId + "&_=";
                    JdShopUrlNext next = new JdShopUrlNext();
                    next.setStore_id(store_id);
                    next.setStore_name(store_name);
                    next.setStore_url(store_url);
                    next.setShop_url(next_page_url);
                    next.setKeyword(task.getKeyword());
                    next.setCategory(task.getCategory());
                    next.setRequest_id(task.getRequest_id());
                    next.setTask_id(task.getTask_id());
                    next.setPage_index(i + 1);
                    next.setPage_total(page_total);
                    next.setCount(count);
                    next.setStatus(0);
                    next.setType(task.getType());
                    next.setProject_code(task.getProject_code());
                    next.setWebsite(task.getWebsite());
                    next.setDown_type(task.getDown_type());
                    next.setCreate_date(new Date());
                    next.setCreate_time(new Date());
                    next.setNode(task.getNode());
                    try {
                        tDao.insert(next);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
            logger.info("生成京东shop_url_next任务完成");
            tDao.update(JdShopUrl.class, Chain.make("status", 2).add("page_total", page_total).add("count", count), Cnd.where("id", "=", task.getId()));
        } catch (Exception e) {
            logger.error("", e);
            tDao.update(JdShopUrl.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }
    }

    public boolean notExistEmpty(List<String> l) {
        boolean flag = true;
        for (int i = 0; i < l.size(); i++) {
            if (StringUtils.isEmpty(l.get(i))) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
