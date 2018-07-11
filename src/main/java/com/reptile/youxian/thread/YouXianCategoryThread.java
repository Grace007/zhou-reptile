package com.reptile.youxian.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpsBase;
import com.bds.base.util.Constant;
import com.reptile.youxian.job.YouXianCategoryJob;
import com.reptile.youxian.job.YouXianProductJob;
import com.reptile.youxian.model.YouXianProductSimple;
import com.reptile.youxian.util.YouXianConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eli
 * @date 2017/11/17 12:15
 */
public class YouXianCategoryThread extends Thread{
    private Logger logger=Logger.getLogger(YouXianCategoryThread.class);
    private Dao mysqldao = MySqlDataSource.getMysqlDao(Constant.ZNode);
    TaskToBi tobi = BIDataSource.getBiDataSource("fresh","DC_FRESH","get_sort_info");
    Dao bidao = tobi.getBiDao();
    private YouXianCategoryJob task;
    public YouXianCategoryThread(Record task){
        this.task=task.toPojo(YouXianCategoryJob.class);
    }

    @Override
    public void run() {
        try{
            mysqldao.update(YouXianCategoryJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
            String url =task.getUrl();
            String html="";
            String param="{\"station_code\":\"\",\"address_code\":\""+task.getAreaid()+"\"}";
            Map<String,String> header = new HashMap<String,String >();
            header.put("platform","web");
            header.put("x-region", param);

            try {
                html = HttpsBase.get(url, "utf-8", header).getResult();
            } catch (Exception e) {
                logger.error("获取html失败!可能是IP受限");
            }
            JSONArray products=null;
                products= new JSONObject(html).getJSONArray("products");
                for (int i=0;i<products.length();i++){
                    String pname="",price="",current_price="",vip_price="",product_tags="",sku="",promote_tag="",subtitle="",product_url="";
                    JSONObject product = products.getJSONObject(i);
                    if (!product.has("code")) {
                        pname = product.getString("name");
                        if (!product.isNull("product_tags")) {
                            JSONArray product_tagsArray = product.getJSONArray("product_tags");
                            for (int j = 0; j < product_tagsArray.length(); j++) {
                                product_tags += product_tagsArray.getJSONObject(j).getString("name") + "###";
                            }
                        }
                        sku = product.getString("sku");
                        promote_tag = product.getString("promote_tag");
                        subtitle = product.getString("subtitle");
                        price = product.getString("price");
                        if (product.has("vip_price_pro") &&  !product.isNull("vip_price_pro")) {
                            JSONObject vip_price_pro = product.getJSONObject("vip_price_pro");
                            current_price = vip_price_pro.getJSONObject("price_up").getString("price");
                            vip_price = vip_price_pro.getJSONObject("price_down").getString("price");
                        }
                        product_url = "https://as-vip.missfresh.cn/v3/product/" + sku;


                        /*System.out.println("###############"+i);
                        System.out.println("pname = " + pname);
                        System.out.println("price = " + price);
                        System.out.println("current_price = " + current_price);
                        System.out.println("vip_price = " + vip_price);
                        System.out.println("product_tags = " + product_tags);
                        System.out.println("sku = " + sku);
                        System.out.println("promote_tag = " + promote_tag);
                        System.out.println("subtitle = " + subtitle);
                        System.out.println("product_url = " + product_url);*/

                        YouXianProductJob youXianProductJob = new YouXianProductJob();
                        youXianProductJob.setCategory(task.getCategory());
                        youXianProductJob.setCity(task.getCity());
                        youXianProductJob.setPname(pname);
                        youXianProductJob.setNode(task.getNode());
                        youXianProductJob.setParam(param);
                        youXianProductJob.setUrl(product_url);
                        youXianProductJob.setSku(sku);
                        youXianProductJob.setDown_type(task.getDown_type());
                        youXianProductJob.setProject_code(task.getProject_code());
                        youXianProductJob.setWebsite(task.getWebsite());
                        youXianProductJob.setTask_id(task.getTask_id());
                        youXianProductJob.setRequest_id(task.getRequest_id());
                        youXianProductJob.setCreate_time(new Date());
                        youXianProductJob.setStatus(0);
                        youXianProductJob.setArea(task.getArea());
                        youXianProductJob.setAreaid(task.getAreaid());
                        try {
                            mysqldao.fastInsert(youXianProductJob);
                        } catch (Exception e) {
                            //logger.error("youXianProductJob插入mysql",e);
                        }

                        String product_url1="https://as-vip.missfresh.cn/frontend/#/tab/home?fromSource=baidupinzhuan-H5&sku="+sku;

                        YouXianProductSimple youXianProductSimple = new YouXianProductSimple();
                        youXianProductSimple.setCategory(task.getCategory());
                        youXianProductSimple.setCity(task.getCity());
                        youXianProductSimple.setCurrent_price(getPrice(current_price));
                        youXianProductSimple.setPname(pname);
                        youXianProductSimple.setPrice(getPrice(price));
                        youXianProductSimple.setProduct_tags(product_tags);
                        youXianProductSimple.setProduct_url(product_url1);
                        youXianProductSimple.setSku(sku);
                        youXianProductSimple.setSubtitle(subtitle);
                        youXianProductSimple.setVip_price(getPrice(vip_price));
                        youXianProductSimple.setCreate_date(new Date());
                        youXianProductSimple.setTask_id(task.getTask_id());
                        youXianProductSimple.setRequest_id(task.getRequest_id());
                        youXianProductSimple.setCreate_time(new Date());
                        youXianProductSimple.setPromote_tag(promote_tag);
                        youXianProductSimple.setArea(task.getArea());

                        try {
                            bidao.fastInsert(youXianProductSimple);
                        } catch (Exception e) {
                            //logger.error("youXianProductSimple插入失败...",e);
                        }

                    }

                }

        }catch (Exception e) {
            logger.error("任务出错...",e);
            mysqldao.update(YouXianCategoryJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }
        mysqldao.update(YouXianCategoryJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
        logger.info("任务成功!");
    }

    public String getParam(String city){
        String param ="";
        if (city.contains("上海")){
            param= YouXianConstant.X_REGGION_SHANGHAI;
        }else if (city.contains("北京")){
            param= YouXianConstant.X_REGGION_BEIJING;
        }
        else if (city.contains("广州")){
            param= YouXianConstant.X_REGGION_GUANGZHOU;
        }
        else if (city.contains("深圳")){
            param= YouXianConstant.X_REGGION_SHENZHEN;
        }
        return  param;
    }

    public String getPrice(String price){
        if (!StringUtils.isEmpty(price)) {
            double priceDou = Double.parseDouble(price);
            priceDou = priceDou / 100;
            return String.valueOf(priceDou);
        }
        return "";
    }




}
