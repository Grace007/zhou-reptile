package com.reptile.ec.vip2.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.MD5Util;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.vip2.job.VipProductJob;
import com.reptile.ec.vip2.model.VipProductDetail;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import java.util.Date;

public class VipProductDetailThread extends Thread {
    private static Logger logger = Logger.getLogger(VipProductDetailThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private VipProductJob task;

    public VipProductDetailThread(VipProductJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        tDao.update(VipProductJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        logger.info("开始下载唯品会产品URL[" + task.getUrl() + "]信息...");
        TaskToBi tobi = BIDataSource.getBiDataSource("vip2", task.getProject_code(), task.getDown_type());
        Dao biDao = tobi.getBiDao();
        String url = task.getUrl();
        if (StringUtils.isNotEmpty(url)) {
            String result = null;
            try {
                result = HttpBase.get(url, "utf-8").getResult();
                Document doc = Jsoup.parse(result);
                if (!doc.select(".c-error-title").isEmpty()) {
                	logger.info("唯品会产品URL[" + url + "]解析完成");
                    tDao.update(VipProductJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
                    return ;
				}
                String pname = null;
                //Element element = null;
                //获得商品名
                if (!doc.select(".bt_box_t").select(".bt_title").isEmpty()) {
                pname = doc.select(".bt_box_t").select(".bt_title").text();
				}else if(!doc.select(".pib-title-detail").isEmpty()){
                pname = doc.select(".pib-title-detail").text();
				}
                else if(!doc.select(".pro-title-name").isEmpty()){
					pname = doc.select(".pro-title-name").text().trim();
				}
                if (StringUtils.isEmpty(pname)) {
                    pname = task.getProduct_name();
                }
                
                String pid = null;
                pid = task.getProduct_id();
                //商品描述
                String description = null;
				description = doc.select(".goods-description-title").text();
                String current_price = null;
                //当前价格
                if (!doc.select("div#J-pi-price-box").select("span.pbox-price.showthis").isEmpty()) {
                	current_price = doc.select("div#J-pi-price-box").select("span.pbox-price.showthis").text().replace(" ", "");
				}else if(!doc.select(".bt_infos_r_t").select(".bt_infos_price").isEmpty()){
					current_price = doc.select(".bt_infos_r_t").select(".bt_infos_price").text().replace(" ", "");
				}else if (!doc.select("div.price-name").isEmpty()) {
					current_price = doc.select("div.price-name").text().trim();
				}
                
                if (StringUtils.isEmpty(current_price)) {
                    current_price = task.getCurrent_price();
                }
                //唯品币
                
                
                String vip_coins = null;
                vip_coins = doc.select("div.vip-currency-title-v1").text().replaceAll(" ", "").trim();
                if (StringUtils.isEmpty(vip_coins)) {
                    vip_coins = doc.select(".vip-currency-title-v1").text().replace("购买", "");
                }
                //
                String promotion = null;
                promotion = task.getPromotion();
                //价格相关折扣列表
                String original_price = null;
                String discount = null;
                String saving = null;
                //price-market
                if (doc.select(".bt_price_list").isEmpty()) {
                	if (!doc.select("div.price-market").isEmpty()) {
                		original_price = doc.select("div.price-market").text().replaceAll(" ", "").trim();
					}else if(!doc.select("span.pbox-market").isEmpty()){
						original_price = doc.select("span.pbox-market").text().replaceAll(" ", "").trim();
					}
                	
                	if (!doc.select(".pbox-off-box").isEmpty()) {
                			 discount = doc.select(".pbox-off-box").text().trim();
					}else if (!doc.select("div.price-discount").isEmpty()) {
							discount = doc.select("div.price-discount").text().replaceAll(" ", "");
					}
                	
 	                try {
 	                	saving = "节省 ¥" + (Integer.parseInt(original_price.replace("¥", ""))
 	                            - Integer.parseInt(current_price.replace("¥", "")));
 	                } catch (Exception e) {
 	                    saving = null;
 	                }
                	
				}else {
					try {
	                    original_price = doc.select(".bt_price_list").select("li").get(0).text().replace("  ", "");
	                } catch (Exception e) {
	                    original_price = task.getOriginal_price();
	                }
	               
	                try {
	                    discount = doc.select(".bt_price_list").select("li").get(1).text();
	                } catch (Exception e) {
	                    discount = task.getDiscount();
	                }
	                
	                try {
	                    saving = doc.select(".bt_price_list").select("li").get(2).text();
	                } catch (Exception e) {
	                    saving = "节省 ¥" + (Integer.parseInt(original_price.replace("¥", ""))
	                            - Integer.parseInt(current_price.replace("¥", "")));
	                }
				}
                
                String sales = null;
                
                if (!doc.select(".bt_others").select(".bt_buy_num").isEmpty()) {
                	sales = doc.select(".bt_others").select(".bt_buy_num").text();
                	
				}
                if(StringUtils.isBlank(sales)){
            		sales = doc.select("div.buy-num-box.J_count_box").text();
            	}
                
                String promise = "";
                if (!doc.select(".bt_others").select(".bt_promise").select("p").isEmpty()) {
                	Elements promises = doc.select(".bt_others").select(".bt_promise").select("p");
                    for (Element element : promises) {
                        promise += element.text() + ";";
                    }
				}
                
                String special_performance = null;
                special_performance = subString("'brandName' : '", "',", doc.select("script").toString());
                String category = null;
                category = subString("'category_name' : '", "',", doc.select("script").toString());
                String category_id = null;
                category_id = subString("'category_id' : '", "',", doc.select("script").toString());
                String navigation = null;
                if (!doc.select(".bt_crumbs").isEmpty()) {
                	navigation = doc.select(".bt_crumbs").text();
                    if (StringUtils.isEmpty(navigation)) {
                        navigation = doc.select(".M-class").text();	
                    }
				}else if(!doc.select(".M-class").isEmpty()){
					navigation = doc.select(".M-class").text().trim();
				}else if(!doc.select(".c-breadcrumbs").isEmpty()){
					navigation = doc.select(".c-breadcrumbs").text().trim();
				}
                
                String introduction = null;
                
                if (!doc.select(".bt_layout").isEmpty()) {
                	introduction = doc.select(".bt_layout").text();
				}else if(!doc.select(".dc-info").isEmpty()){
					introduction = doc.select(".dc-info").text();
				}else if (!doc.select(".g-pro-param").isEmpty()) {
					introduction = doc.select(".g-pro-param").text();
				}
                
                
                
                String remaining_time = null;
                remaining_time = task.getRemaining_time();
                
                String delivery_fee = null;
                delivery_fee = doc.select("#J_freight_frame").text();
                
                String special_price = null;
                /*
                String sp_url = "http://pmsgw.vip.com/1.0.2/jsonGetSpecialPrice?jsonRequest=%7B%22salesNo%22:%22"
                        + task.getBrand_id() + "%22,%22mobilePlatform%22:%22PC%22,%22merchandiseNo%22:"
                        + task.getProduct_id() + ",%22skuPrices%22:%5B%7B%22vipshopPrice%22:"
                        + current_price.replace("¥", "") + "%7D%5D%7D";
                String sp_result = null;
                try {
                    sp_result = HttpBase.get(sp_url, "utf-8").getResult();
                    JSONObject sp_data = new JSONObject(sp_result).getJSONObject("data");
                    if (sp_data != null) {
                        if (sp_data.has("skuSpecialPrices")) {
                            JSONArray sp = sp_data.getJSONArray("skuSpecialPrices");
                            special_price = "¥" + sp.getJSONObject(0).getString("specialPrice") + " "
                                    + sp.getJSONObject(0).getString("tips");
                        }
                    }
                } catch (Exception e1) {
                    logger.error("", e1);
                }
                */
                String unique_id = null;
                unique_id = MD5Util.getMD5(task.getRequest_id()+task.getTask_id() + url);

                VipProductDetail detail = new VipProductDetail();
                Date date = new Date();
                detail.setRequest_id(task.getRequest_id());
                detail.setTask_id(task.getTask_id());
                detail.setCreate_date(date);
                detail.setCreate_time(date);
                detail.setKeyword(task.getKeyword());
                detail.setCategory(category);
                detail.setCategory_id(category_id);
                detail.setBrand_name(special_performance);
                detail.setBrand_id(task.getBrand_id());
                detail.setUrl(url);
                detail.setPname(pname);
                detail.setPid(pid);
                detail.setDescription(description);
                detail.setCurrent_price(current_price);
                detail.setVip_coins(vip_coins);
                detail.setPromotion(promotion);
                detail.setOriginal_price(original_price);
                detail.setDiscount(discount);
                detail.setSaving(saving);
                detail.setSales(sales);
                detail.setPromise(promise);
                detail.setSpecial_performance(special_performance);
                detail.setNavigation(navigation);
                detail.setIntroduction(introduction);
                detail.setRemaining_time(remaining_time);
                detail.setDelivery_fee(delivery_fee);
                detail.setSpecial_price(special_price);
                detail.setUnique_id(unique_id);
                try {
                    biDao.insert(detail);
                } catch (Exception e) {

                }
                logger.info("唯品会产品URL[" + url + "]解析完成");
                tDao.update(VipProductJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            } catch (Exception e) {
                logger.error("VipProductDetailThread解析异常", e);
                tDao.update(VipProductJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            }
        } else {
            tDao.update(VipProductJob.class, Chain.make("status", -1), Cnd.where("id", "=", task.getId()));
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public static String subString(String begin, String end, String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        int index_s = 0;
        if (begin != null) {
            index_s = content.indexOf(begin) + begin.length();
        }
        String _end = content.substring(index_s, index_s+500);
        int index_e = 0;

        if (end == null) {
            index_e = _end.length();
        } else {
            index_e = _end.indexOf(end);
        }
        return _end.substring(0, index_e);
    }

}
