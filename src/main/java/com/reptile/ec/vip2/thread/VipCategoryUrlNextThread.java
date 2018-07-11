package com.reptile.ec.vip2.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.MD5Util;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.vip2.job.VipCategoryUrlJob;
import com.reptile.ec.vip2.job.VipProductJob;
import com.reptile.ec.vip2.model.VipProductSimple;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import java.util.Date;

public class VipCategoryUrlNextThread extends Thread {
	private static Logger logger = Logger
			.getLogger(VipCategoryUrlNextThread.class);
	private static NutDao tDao = SpringContextHolder.getBean("tDao");
	private VipCategoryUrlJob task;

	public VipCategoryUrlNextThread(VipCategoryUrlJob task) {
		this.task = task;
	}

	@Override
	public void run() {
		tDao.update(VipCategoryUrlJob.class, Chain.make("status", 1),
				Cnd.where("id", "=", task.getId()));
		logger.info("开始下载唯品会URL[" + task.getUrl() + "]信息...");
		TaskToBi tobi = BIDataSource.getBiDataSource("vip2",task.getProject_code(), task.getDown_type());
		Dao biDao = tobi.getBiDao();
		String url = task.getUrl();
		
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
		try {
			
			int current_page = task.getCurrent_page();
			int counter = 0;
			//keyword入口
			if (url.contains("http://category.vip.com/suggest.php?keyword")) {
				for (int i = current_page; i <= task.getTotal_page(); i++) {
					String next_page_url = url + "&page=" + i;
					String result = null;
					try {
						result = HttpBase.get(next_page_url, "utf-8").getResult();
						Document doc = Jsoup.parse(result);
						
						//先遍历非js加载的商品
						try {
							Element J_searchCatList = doc.select("section#J_searchCatList").first();
							Elements eles = J_searchCatList.select("div.goods-list-item");
							Element ele=null;
							counter+=eles.size();
							//Elements eles = doc.select("div.goods-list-item.c-goods.J_surpriseSprice_item.J_pro_items");
							//logger.info(eles.size());
							for (int k = 0; k < eles.size(); k++) {
								ele = eles.get(k);
								pname = ele.select("h4.goods-info.goods-title-info").select("a").attr("title");
								pid = ele.attr("data-id");
								brand_id = ele.attr("data-brand");
								current_price = ele.select("div.goods-info.goods-price-info").select("em.goods-sells-price").text();
								original_price = ele.select("div.goods-info.goods-price-info").select("del.goods-market-price").text();
								discount = ele.select("div.goods-info.goods-price-info").select("span.goods-discount").text();
								remaining_time = ele.select("span.goods-countdown-info-text").text();
								
								//logger.info(discount);
								//logger.info(remaining_time);
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
								unique_id=MD5Util.getMD5(task.getRequest_id()+task.getTask_id()
										+ "http://www.vip.com/detail-" + brand_id + "-"
										+ pid + ".html");
								VipProductSimple simple = new VipProductSimple();
								Date date = new Date();
								simple.setRequest_id(task.getRequest_id());
								simple.setTask_id(task.getTask_id());
								simple.setCreate_date(date);
								simple.setCreate_time(date);
								simple.setKeyword(task.getKeyword());
								simple.setCategory(task.getCategory());
								simple.setUrl("http://www.vip.com/detail-" + brand_id
										+ "-" + pid + ".html");
								simple.setPname(pname);
								simple.setPid(pid);
								simple.setBrand_id(brand_id);
								simple.setCurrent_price(current_price);
								simple.setOriginal_price(original_price);
								simple.setDiscount(discount);
								simple.setPromotion(promotion);
								simple.setRemaining_time(remaining_time);
								simple.setUnique_id(unique_id);
								try {
									biDao.insert(simple);
									logger.info("simple插入BI库成功");
								} catch (Exception e) {
									logger.error("simple插入BI库失败",e);
								}

								
								
								
							}
							
							logger.info("界面非js加载商品解析完毕");
						} catch (Exception e) {
							logger.error("界面非js加载商品解析失败",e);
						}
						
						
						
						
						// 页面js,最大显示100个
						String web_json = null;
						JSONObject data2;
						Elements elements = doc.select("script");
						Element script = elements.get(elements.size() - 1);
						
						// System.out.println(script.toString()+"##########################");
						try {
							web_json = subString("\"products\":", "});",
									script.toString());
							data2 = new JSONObject("{data:" + web_json + "}");
							/*System.out.println(data2.toString()
									+ "##########################");*/
						} catch (Exception e) {
							logger.error("页面json异常", e);
							data2 = new JSONObject("{data:[]}");
						}
						JSONArray p_list = data2.getJSONArray("data");
						logger.info(p_list.length());
						for (int j = 0; j < p_list.length(); j++) {
							pname = null;
							if (p_list.getJSONObject(j).has("product_name")) {
								pname = p_list.getJSONObject(j).getString(
										"product_name");
							}
							pid = null;
							if (p_list.getJSONObject(j).has("product_id")) {
								pid = p_list.getJSONObject(j).getString(
										"product_id");
							}
							brand_id = null;
							if (p_list.getJSONObject(j).has("brand_id")) {
								brand_id = p_list.getJSONObject(j).getString(
										"brand_id");
							}
							current_price = null;
							original_price = null;
							if (p_list.getJSONObject(j).has("price_info")) {
								try {
									JSONObject price_info = p_list.getJSONObject(j)
											.getJSONObject("price_info");
									current_price = "¥"
											+ price_info
													.getString("sell_price_min_tips");
									original_price = "¥"
											+ price_info
													.getString("market_price_of_min_sell_price");
								} catch (Exception e) {
									logger.error("price_info异常", e);
								}
							}
							discount = null;
							if (p_list.getJSONObject(j).has("sku_agio")) {
								discount = p_list.getJSONObject(j).getString(
										"sku_agio");
							}
							remaining_time = null;
							if (p_list.getJSONObject(j).has("show_time_cnt")) {
								remaining_time = p_list.getJSONObject(j).getString(
										"show_time_cnt");
							}
							data = null;
							promotion = null;
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
							unique_id = null;
							unique_id = MD5Util.getMD5(task.getRequest_id()+task.getTask_id()
									+ "http://www.vip.com/detail-" + brand_id + "-"
									+ pid + ".html");

							VipProductSimple simple = new VipProductSimple();
							Date date = new Date();
							simple.setRequest_id(task.getRequest_id());
							simple.setTask_id(task.getTask_id());
							simple.setCreate_date(date);
							simple.setCreate_time(date);
							simple.setKeyword(task.getKeyword());
							simple.setCategory(task.getCategory());
							simple.setUrl("http://www.vip.com/detail-" + brand_id
									+ "-" + pid + ".html");
							simple.setPname(pname);
							simple.setPid(pid);
							simple.setBrand_id(brand_id);
							simple.setCurrent_price(current_price);
							simple.setOriginal_price(original_price);
							simple.setDiscount(discount);
							simple.setPromotion(promotion);
							simple.setRemaining_time(remaining_time);
							simple.setUnique_id(unique_id);
							try {
								biDao.insert(simple);
							} catch (Exception e) {
								// logger.error("当前第[" + i +
								// "]页100个中重复,URL[http://www.vip.com/detail-" +
								// brand_id + "-" + pid
								// + ".html]", e);
							}

							/*VipProductJob job = new VipProductJob();
							job.setRequest_id(task.getRequest_id());
							job.setTask_id(task.getTask_id());
							job.setCreate_date(date);
							job.setKeyword(task.getKeyword());
							job.setCategory(task.getCategory());
							job.setUrl("http://www.vip.com/detail-" + brand_id
									+ "-" + pid + ".html");
							job.setProduct_name(pname);
							job.setProduct_id(pid);
							job.setBrand_id(brand_id);
							job.setCurrent_price(current_price);
							job.setOriginal_price(original_price);
							job.setDiscount(discount);
							job.setPromotion(promotion);
							job.setRemaining_time(remaining_time);
							job.setCategory_url(next_page_url);
							job.setCurrent_page(i);
							job.setTotal_page(task.getTotal_page());
							job.setStatus(4);
							job.setNode(task.getNode());
							job.setLevel(0);
							job.setProject_code(task.getProject_code());
							job.setDown_type(task.getDown_type());
							try {
								tDao.insert(job);
							} catch (Exception e) {
								logger.error("##########唯品会插入异常！！！");
							}*/
						}
						counter += p_list.length();
						logger.error("下载唯品会URL[" + next_page_url + "]第[" + i
								+ "]页完成");
						tDao.update(
								VipCategoryUrlJob.class,
								Chain.make("current_page", i).add("amount",
										(task.getAmount() + counter)),
								Cnd.where("id", "=", task.getId()));
					} catch (Exception e) {
						logger.error("VipCategoryUrlNextThread解析异常", e);
						tDao.update(VipCategoryUrlJob.class,
								Chain.make("status", 3),
								Cnd.where("id", "=", task.getId()));
					}
				}
			}
			//分类入口
			else if (url.contains("http://category.vip.com/search-")) {
				String baseUrl=url.replace("|", "%7C");
				String baseUrlArray [] =baseUrl.split(".html");
				try {
				for (int i = current_page; i <= task.getTotal_page(); i++){
					String next_page_url= baseUrlArray[0].substring(0,baseUrlArray[0].length()-1)+i+".html"+baseUrlArray[1];
					String html = null;
					html = HttpBase.get(next_page_url, "utf-8").getResult();
			        Document doc = Jsoup.parse(html);
			        String web_json = null;
			        JSONObject data2;
			        Elements elements = doc.select("script");
			        //System.out.println(elements);
			        Element script = elements.get(elements.size()-1);
			        JSONArray p_list=null;
			        try {
			        web_json = subString("list = [", "}];", script.toString());
			        web_json="["+web_json+"}]";
			        data2 = new JSONObject("{data:" + web_json + "}");
			        p_list = data2.getJSONArray("data");
			        } catch (Exception e) {
			        	p_list=null;
					}
			        
			        
			        
					//先遍历非js加载的商品
					try {
						Element J_searchCatList = doc.select("section#J_searchCatList").first();
						Elements eles = J_searchCatList.select("div.goods-list-item");
						Element ele=null;
						counter+=eles.size();
						//Elements eles = doc.select("div.goods-list-item.c-goods.J_surpriseSprice_item.J_pro_items");
						for (int k = 0; k < eles.size(); k++) {
							ele = eles.get(k);
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
							unique_id=MD5Util.getMD5(task.getRequest_id()+task.getTask_id()
									+ "http://www.vip.com/detail-" + brand_id + "-"
									+ pid + ".html");
							VipProductSimple simple = new VipProductSimple();
							Date date = new Date();
							simple.setRequest_id(task.getRequest_id());
							simple.setTask_id(task.getTask_id());
							simple.setCreate_date(date);
							simple.setCreate_time(date);
							simple.setKeyword(task.getKeyword());
							simple.setCategory(task.getCategory());
							simple.setUrl("http://www.vip.com/detail-" + brand_id
									+ "-" + pid + ".html");
							simple.setPname(pname);
							simple.setPid(pid);
							simple.setBrand_id(brand_id);
							simple.setCurrent_price(current_price);
							simple.setOriginal_price(original_price);
							simple.setDiscount(discount);
							simple.setPromotion(promotion);
							simple.setRemaining_time(remaining_time);
							simple.setUnique_id(unique_id);
							try {
								biDao.insert(simple);
								logger.info("simple插入BI库成功");
							} catch (Exception e) {
								logger.error("simple插入BI库失败",e);
							}

							VipProductJob job = new VipProductJob();
							job.setRequest_id(task.getRequest_id());
							job.setTask_id(task.getTask_id());
							job.setCreate_date(date);
							job.setKeyword(task.getKeyword());
							job.setCategory(task.getCategory());
							job.setUrl("http://www.vip.com/detail-" + brand_id
									+ "-" + pid + ".html");
							job.setProduct_name(pname);
							job.setProduct_id(pid);
							job.setBrand_id(brand_id);
							job.setCurrent_price(current_price);
							job.setOriginal_price(original_price);
							job.setDiscount(discount);
							job.setPromotion(promotion);
							job.setRemaining_time(remaining_time);
							job.setCategory_url(next_page_url);
							job.setCurrent_page(i);
							job.setTotal_page(task.getTotal_page());
							job.setStatus(0);
							job.setNode(task.getNode());
							job.setLevel(0);
							job.setProject_code(task.getProject_code());
							job.setDown_type(task.getDown_type());
							try {
								tDao.insert(job);
							} catch (Exception e) {
								logger.error("##########唯品会插入异常！！！",e);
							}
							
							
							
							
						}
						
						logger.info("界面非js加载商品加载完毕");
					} catch (Exception e) {
						// TODO: handle exception
					}
					logger.info("开始解析js加载部分");
					for (int j = 0; j < p_list.length(); j++) {
						if (p_list.getJSONObject(j).has("show_title")) {
			                pname = p_list.getJSONObject(j).getString("show_title");
			            }
			        	if (p_list.getJSONObject(j).has("id")) {
			                pid = p_list.getJSONObject(j).getString("id");
			            }
			        	if (p_list.getJSONObject(j).has("brand_id")) {
							brand_id = p_list.getJSONObject(j).getString(
									"brand_id");
						}
			        	if (p_list.getJSONObject(j).has("price_info")) {
							try {
								JSONObject price_info = p_list.getJSONObject(j)
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
			        	if (p_list.getJSONObject(j).has("discount")) {
							discount = p_list.getJSONObject(j).getString(
									"discount");
						}
			        	if (p_list.getJSONObject(j).has("show_time_cnt")) {
							remaining_time = p_list.getJSONObject(j).getString(
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
						unique_id=MD5Util.getMD5(task.getRequest_id()+task.getTask_id()
								+ "http://www.vip.com/detail-" + brand_id + "-"
								+ pid + ".html");
						VipProductSimple simple = new VipProductSimple();
						Date date = new Date();
						simple.setRequest_id(task.getRequest_id());
						simple.setTask_id(task.getTask_id());
						simple.setCreate_date(date);
						simple.setCreate_time(date);
						simple.setKeyword(task.getKeyword());
						simple.setCategory(task.getCategory());
						simple.setUrl("http://www.vip.com/detail-" + brand_id
								+ "-" + pid + ".html");
						simple.setPname(pname);
						simple.setPid(pid);
						simple.setBrand_id(brand_id);
						simple.setCurrent_price(current_price);
						simple.setOriginal_price(original_price);
						simple.setDiscount(discount);
						simple.setPromotion(promotion);
						simple.setRemaining_time(remaining_time);
						simple.setUnique_id(unique_id);
						try {
							biDao.insert(simple);
							logger.info("simple插入BI库成功");
						} catch (Exception e) {
							logger.error("simple插入BI库失败",e);
						}

						VipProductJob job = new VipProductJob();
						job.setRequest_id(task.getRequest_id());
						job.setTask_id(task.getTask_id());
						job.setCreate_date(date);
						job.setKeyword(task.getKeyword());
						job.setCategory(task.getCategory());
						job.setUrl("http://www.vip.com/detail-" + brand_id
								+ "-" + pid + ".html");
						job.setProduct_name(pname);
						job.setProduct_id(pid);
						job.setBrand_id(brand_id);
						job.setCurrent_price(current_price);
						job.setOriginal_price(original_price);
						job.setDiscount(discount);
						job.setPromotion(promotion);
						job.setRemaining_time(remaining_time);
						job.setCategory_url(next_page_url);
						job.setCurrent_page(i);
						job.setTotal_page(task.getTotal_page());
						job.setStatus(0);
						job.setNode(task.getNode());
						job.setLevel(0);
						job.setProject_code(task.getProject_code());
						job.setDown_type(task.getDown_type());
						try {
							tDao.insert(job);
						} catch (Exception e) {
							logger.error("##########唯品会插入异常！！！",e);
						}
					}
					counter += p_list.length();
					logger.info("下载唯品会URL[" + next_page_url + "]第[" + i
							+ "]页完成");
					tDao.update(
							VipCategoryUrlJob.class,
							Chain.make("current_page", i).add("amount",
									(task.getAmount() + counter)),
							Cnd.where("id", "=", task.getId()));
				}
				}catch (Exception e) {
						logger.error("VipCategoryUrlNextThread解析异常", e);
						tDao.update(VipCategoryUrlJob.class,
								Chain.make("status", 3),
								Cnd.where("id", "=", task.getId()));
					}
					
				
			}
			
			
			logger.info("下载唯品会URL[" + url + "]完成,共[" + task.getTotal_page()
					+ "]页,[" + (task.getAmount() + counter) + "]个产品");
			tDao.update(VipCategoryUrlJob.class, Chain.make("status", 2),
					Cnd.where("id", "=", task.getId()));
		} catch (Exception e) {
			logger.error("分割异常", e);
			tDao.update(VipCategoryUrlJob.class, Chain.make("status", 3),
					Cnd.where("id", "=", task.getId()));
		}
	}


	public String getPromotion(JSONObject brands, String brand) {
		String promotion = null;
		if (brands.length() != 0) {
			if (brands.has(brand)) {
				try {
					promotion = brands.getJSONObject(brand).getString("msg");
				} catch (JSONException e) {
					logger.error("", e);
				}
			}
		}
		return promotion;
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
/*
 * if ("search".equals(task.getType())) { //新版本type被弃用，一直为空 String url =
 * task.getUrl().replace("|", "%7C"); try { int current_page =
 * task.getCurrent_page(); String next_page_url_prefix = url.split("1.html")[0];
 * String next_page_url_suffix = url.split("1.html")[1]; int counter = 0; for
 * (int i = current_page; i <= task.getTotal_page(); i++) { String next_page_url
 * = next_page_url_prefix + i + ".html" + next_page_url_suffix; String result =
 * null; try { result = HttpBase.get(next_page_url, "utf-8").getResult();
 * Document doc = Jsoup.parse(result); String brandIds =
 * subString("var _brandIds = [", "];", doc.select("script").toString());
 * JSONObject data = null; if (StringUtils.isNotEmpty(brandIds)) { String
 * result2 = null; try { String brands_request_url =
 * "http://pmsgw.vip.com/1.0.2/jsonActiveTipsIndex?jsonRequest=%7B%22warehouse%22:%22VIP_SH%22,%22platform%22:%221%22,%22salesNos%22:%5B"
 * + brandIds + "%5D,%22labels%22:%7B%22mobilePlatform%22:%22PC%22%7D%7D";
 * result2 = HttpBase.get(brands_request_url, "utf-8").getResult(); data = new
 * JSONObject(result2).getJSONObject("data"); } catch (Exception e) {
 * //logger.error("brands请求异常", e); } } Elements ul =
 * doc.select("#J_searchCatList").select("div.J_surpriseSprice_item"); for
 * (Element element : ul) {// 每个页面最大显示24个 String current_price = null;
 * current_price =
 * element.select(".goods-price-info").select(".goods-sells-price").text();
 * String original_price = null; original_price =
 * element.select(".goods-price-info").select(".goods-market-price").text();
 * String discount = null; discount =
 * element.select(".goods-price-info").select(".goods-discount").text(); String
 * product_name = null; product_name =
 * element.select(".goods-title-info").text(); String product_url = null;
 * product_url = "http:" +
 * element.select(".goods-title-info").select("a").attr("href"); String
 * product_id = null; product_id = element.attr("data-id"); String brand_id =
 * null; brand_id = element.attr("data-brand"); String remaining_time = null;
 * remaining_time = element.select(".goods-countdown-info").text(); String
 * promotion = null; if (StringUtils.isNotEmpty(brand_id) && (data != null)) {
 * promotion = this.getPromotion(data, brand_id); } String unique_id = null;
 * unique_id = MD5Util.getMD5(task.getRequest_id() + product_url);
 * 
 * VipProductSimple simple = new VipProductSimple(); Date date = new Date();
 * simple.setRequest_id(task.getRequest_id());
 * simple.setTask_id(task.getTask_id()); simple.setCreate_date(date);
 * simple.setCreate_time(date); simple.setKeyword(task.getKeyword());
 * simple.setCategory(task.getCategory()); simple.setUrl(product_url);
 * simple.setPname(product_name); simple.setPid(product_id);
 * simple.setBrand_id(brand_id); simple.setCurrent_price(current_price);
 * simple.setOriginal_price(original_price); simple.setDiscount(discount);
 * simple.setPromotion(promotion); simple.setRemaining_time(remaining_time);
 * simple.setUnique_id(unique_id); try { biDao.insert(simple); } catch
 * (Exception e) { //logger.error("当前第[" + i + "]页24个中重复,URL[" + product_url +
 * "]", e); }
 * 
 * VipProductJob job = new VipProductJob();
 * job.setRequest_id(task.getRequest_id()); job.setTask_id(task.getTask_id());
 * job.setCreate_date(date); job.setKeyword(task.getKeyword());
 * job.setCategory(task.getCategory()); job.setUrl(product_url);
 * job.setProduct_name(product_name); job.setProduct_id(product_id);
 * job.setBrand_id(brand_id); job.setCurrent_price(current_price);
 * job.setOriginal_price(original_price); job.setDiscount(discount);
 * job.setPromotion(promotion); job.setRemaining_time(remaining_time);
 * job.setCategory_url(next_page_url); job.setCurrent_page(i);
 * job.setTotal_page(task.getTotal_page()); job.setStatus(0);
 * job.setNode(task.getNode()); job.setLevel(0);
 * job.setProject_code(task.getProject_code());
 * job.setDown_type(task.getDown_type()); try { tDao.insert(job); } catch
 * (Exception e) {
 * 
 * } } // 页面js,最大显示75个 String web_json = null; JSONObject data2; try { web_json
 * = "[" + subString("list = [", "];", doc.select("script").toString()) + "]";
 * data2 = new JSONObject("{data:" + web_json + "}"); } catch (Exception e) {
 * logger.error("页面json异常", e); data2 = new JSONObject("{data:[]}"); } JSONArray
 * p_list = data2.getJSONArray("data"); for (int j = 0; j < p_list.length();
 * j++) { String pname = null; if (p_list.getJSONObject(j).has("show_title")) {
 * pname = p_list.getJSONObject(j).getString("show_title"); } String pid = null;
 * if (p_list.getJSONObject(j).has("id")) { pid =
 * p_list.getJSONObject(j).getString("id"); } String brand_id = null; if
 * (p_list.getJSONObject(j).has("brand_id")) { brand_id =
 * p_list.getJSONObject(j).getString("brand_id"); } String current_price = null;
 * String original_price = null; if (p_list.getJSONObject(j).has("price_info"))
 * { try { JSONObject price_info =
 * p_list.getJSONObject(j).getJSONObject("price_info"); current_price = "¥" +
 * price_info.getString("sell_price_max"); original_price = "¥" +
 * price_info.getString("market_price_max"); } catch (Exception e) {
 * logger.error("price_info异常", e); } } String discount = null; if
 * (p_list.getJSONObject(j).has("discount")) { discount =
 * p_list.getJSONObject(j).getString("discount"); } String remaining_time =
 * null; if (p_list.getJSONObject(j).has("show_time_cnt")) { remaining_time =
 * p_list.getJSONObject(j).getString("show_time_cnt"); } String promotion =
 * null; if (StringUtils.isNotEmpty(brand_id) && (data != null)) { promotion =
 * this.getPromotion(data, brand_id); } String unique_id = null; unique_id =
 * MD5Util.getMD5( task.getRequest_id() + "http://www.vip.com/detail-" +
 * brand_id + "-" + pid + ".html");
 * 
 * VipProductSimple simple = new VipProductSimple(); Date date = new Date();
 * simple.setRequest_id(task.getRequest_id());
 * simple.setTask_id(task.getTask_id()); simple.setCreate_date(date);
 * simple.setCreate_time(date); simple.setKeyword(task.getKeyword());
 * simple.setCategory(task.getCategory());
 * simple.setUrl("http://www.vip.com/detail-" + brand_id + "-" + pid + ".html");
 * simple.setPname(pname); simple.setPid(pid); simple.setBrand_id(brand_id);
 * simple.setCurrent_price(current_price);
 * simple.setOriginal_price(original_price); simple.setDiscount(discount);
 * simple.setPromotion(promotion); simple.setRemaining_time(remaining_time);
 * simple.setUnique_id(unique_id); try { biDao.insert(simple); } catch
 * (Exception e) { // logger.error("当前第[" + i +
 * "]页75个中重复,URL[http://www.vip.com/detail-" + brand_id + "-" + pid // +
 * ".html]", e); }
 * 
 * VipProductJob job = new VipProductJob();
 * job.setRequest_id(task.getRequest_id()); job.setTask_id(task.getTask_id());
 * job.setCreate_date(date); job.setKeyword(task.getKeyword());
 * job.setCategory(task.getCategory()); job.setUrl("http://www.vip.com/detail-"
 * + brand_id + "-" + pid + ".html"); job.setProduct_name(pname);
 * job.setProduct_id(pid); job.setBrand_id(brand_id);
 * job.setCurrent_price(current_price); job.setOriginal_price(original_price);
 * job.setDiscount(discount); job.setPromotion(promotion);
 * job.setRemaining_time(remaining_time); job.setCategory_url(next_page_url);
 * job.setCurrent_page(i); job.setTotal_page(task.getTotal_page());
 * job.setStatus(0); job.setNode(task.getNode()); job.setLevel(0);
 * job.setProject_code(task.getProject_code());
 * job.setDown_type(task.getDown_type()); try { tDao.insert(job); } catch
 * (Exception e) {
 * 
 * } } counter += ul.size() + p_list.length(); logger.error("下载唯品会URL[" +
 * next_page_url + "]第[" + i + "]页完成"); tDao.update(VipCategoryUrlJob.class,
 * Chain.make("current_page", i).add("amount", (task.getAmount() + counter)),
 * Cnd.where("id", "=", task.getId())); } catch (Exception e) {
 * logger.error("VipCategoryUrlNextThread解析异常", e);
 * tDao.update(VipCategoryUrlJob.class, Chain.make("status", 3), Cnd.where("id",
 * "=", task.getId())); } } logger.error("下载唯品会URL[" + url + "]完成,共[" +
 * task.getTotal_page() + "]页,[" + (task.getAmount() + counter) + "]个产品");
 * tDao.update(VipCategoryUrlJob.class, Chain.make("status", 2), Cnd.where("id",
 * "=", task.getId())); } catch (Exception e) { logger.error("分割异常", e);
 * tDao.update(VipCategoryUrlJob.class, Chain.make("status", 3), Cnd.where("id",
 * "=", task.getId())); } } else {
 */