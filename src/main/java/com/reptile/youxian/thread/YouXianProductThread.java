package com.reptile.youxian.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpsBase;
import com.bds.base.util.Constant;
import com.reptile.youxian.job.YouXianProductJob;
import com.reptile.youxian.model.YouXianProductInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
 * @date 2017/11/17 14:23
 */
public class YouXianProductThread extends Thread {
    private Logger logger=Logger.getLogger(YouXianProductThread.class);
    private Dao mysqldao = MySqlDataSource.getMysqlDao(Constant.ZNode);
    TaskToBi tobi = BIDataSource.getBiDataSource("fresh","DC_FRESH","get_sort_info");
    Dao bidao = tobi.getBiDao();
    private YouXianProductJob task;
    public YouXianProductThread(Record task){
        this.task=task.toPojo(YouXianProductJob.class);
    }

    @Override
    public void run() {
        try{
            mysqldao.update(YouXianProductJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
            String url =task.getUrl();
            String html="";
            String param="{\"station_code\":\"\",\"address_code\":\""+task.getAreaid()+"\"}";
            Map<String,String> header = new HashMap<String,String >();
            header.put("platform","web");
            header.put("x-region", param);
            try {
                html = HttpsBase.get(url, "utf-8", header).getResult();
            } catch (Exception e) {
                logger.error("获取html失败!可能是ip受限或是url错误",e);
            }

                JSONObject product = new JSONObject(html);
                String pname="",price="",current_price="",vip_price="",subtitle="",country="",sales_volume="",delivery_mode_name="",vip_card="",product_integarl_text="",share_product_text="";
                pname = product.getString("name");
                price=product.getString("price");
                if (product.has("vip_price_pro") &&  !product.isNull("vip_price_pro")) {
                    JSONObject vip_price_pro = product.getJSONObject("vip_price_pro");
                    current_price = vip_price_pro.getJSONObject("price_up").getString("price");
                    vip_price = vip_price_pro.getJSONObject("price_down").getString("price");
                }
                subtitle = product.getString("subtitle");
                country = product.getString("country");
                sales_volume = product.getString("sales_volume");
                delivery_mode_name =product.getString("delivery_mode_name");
                if (product.has("vip_card")){
                    vip_card = product.getJSONObject("vip_card").getString("back_cash_text").replaceAll("\\n","");
                }
                if (product.has("product_share_info_v2")){
                    product_integarl_text = product.getJSONObject("product_share_info_v2").getString("product_integarl_text");
                    share_product_text = product.getJSONObject("product_share_info_v2").getString("share_product_text").replaceAll("\\D","");
                }

                /*System.out.println("##############");
                System.out.println("pname = " + pname);
                System.out.println("price = " + price);
                System.out.println("current_price = " + current_price);
                System.out.println("vip_price = " + vip_price);
                System.out.println("subtitle = " + subtitle);
                System.out.println("country = " + country);
                System.out.println("sales_volume = " + sales_volume);
                System.out.println("delivery_mode_name = " + delivery_mode_name);
                System.out.println("vip_card = " + vip_card);
                System.out.println("product_integarl_text = " + product_integarl_text);
                System.out.println("share_product_text = " + share_product_text);*/
                String product_url = "https://as-vip.missfresh.cn/frontend/#/tab/home?fromSource=baidupinzhuan-H5&sku="+task.getSku();
            YouXianProductInfo youXianProductInfo = new YouXianProductInfo();
            youXianProductInfo.setCategory(task.getCategory());
            youXianProductInfo.setCity(task.getCity());
            youXianProductInfo.setCountry(country);
            youXianProductInfo.setCurrent_price(getPrice(current_price));
            youXianProductInfo.setDelivery_mode_name(delivery_mode_name);
            youXianProductInfo.setPname(pname);
            youXianProductInfo.setPrice(getPrice(price));
            youXianProductInfo.setProduct_integarl_text(product_integarl_text);
            youXianProductInfo.setProduct_url(product_url);
            youXianProductInfo.setSales_volume(sales_volume);
            youXianProductInfo.setShare_product_text(share_product_text);
            youXianProductInfo.setSku(task.getSku());
            youXianProductInfo.setSubtitle(subtitle);
            youXianProductInfo.setVip_card(vip_card);
            youXianProductInfo.setTask_id(task.getTask_id());
            youXianProductInfo.setRequest_id(task.getRequest_id());
            youXianProductInfo.setCreate_time(new Date());
            youXianProductInfo.setCreate_date(new Date());
            youXianProductInfo.setVip_price(getPrice(vip_price));
            youXianProductInfo.setArea(task.getArea());
            try {
                bidao.fastInsert(youXianProductInfo);
            } catch (Exception e) {
                //logger.error("youXianProductInfo插入失败...",e);
            }
        }catch (Exception e) {
            logger.error("任务出错...",e);
            mysqldao.update(YouXianProductJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }
        mysqldao.update(YouXianProductJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
        logger.info("任务成功!");
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
