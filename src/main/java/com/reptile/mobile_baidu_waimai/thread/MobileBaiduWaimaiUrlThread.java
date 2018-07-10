package com.reptile.mobile_baidu_waimai.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.CommonUtils;
import com.bds.base.util.Constant;
import com.reptile.mobile_baidu_waimai.BaiduHttpBase;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiProductTaskJob;
import com.reptile.mobile_baidu_waimai.job.BaiduWaimaiUrlTaskJob;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MobileBaiduWaimaiUrlThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(MobileBaiduWaimaiUrlThread.class);
    private BaiduWaimaiUrlTaskJob task;

    public MobileBaiduWaimaiUrlThread(BaiduWaimaiUrlTaskJob task) {
        this.task = task;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        tDao.update(BaiduWaimaiUrlTaskJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        String url = task.getUrl();
        // http://waimai.baidu.com/waimai/shop/1549350372
        // http://waimai.baidu.com/mobile/waimai?qt=shopmenu&shop_id=1779143194&address=%E5%BE%90%E5%AE%B6%E6%B1%87&lat=3636567.97&lng=13519043.65
        String shop_id = null;
        String result = "";
        if (url.contains("shop")) {
            try {
                shop_id = CommonUtils.subString("shop_id=", "&address", url);
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Host", "waimai.baidu.com");
                result = BaiduHttpBase.get(url, "utf-8", headers).getResult();
                if (result.contains("该商家无相应菜单") || result.contains("本店休息中，暂不接受订单")) {
                    logger.info("本店休息中，暂不接受订单");
                    tDao.update(BaiduWaimaiUrlTaskJob.class, Chain.make("status", 404),
                            Cnd.where("id", "=", task.getId()));
                    return;
                }
            } catch (Exception e) {
                tDao.update(BaiduWaimaiUrlTaskJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
                return;
            }
            Document document = null;
            document = Jsoup.parse(result);
            BaiduWaimaiProductTaskJob baiduWaimaiProductTaskJob = new BaiduWaimaiProductTaskJob();
            baiduWaimaiProductTaskJob.setCity_name(task.getCity_name());
            baiduWaimaiProductTaskJob.setKeyword(task.getKeyword());
            baiduWaimaiProductTaskJob.setRequest_id(task.getRequest_id());
            baiduWaimaiProductTaskJob.setTask_id(task.getTask_id());
            baiduWaimaiProductTaskJob.setCreate_time(new Date());
            baiduWaimaiProductTaskJob.setUrl(url);
            baiduWaimaiProductTaskJob.setShop_id(shop_id);
            baiduWaimaiProductTaskJob.setNode(task.getNode());
            baiduWaimaiProductTaskJob.setStatus(0);
            baiduWaimaiProductTaskJob.setProject_code(task.getProject_code());
            baiduWaimaiProductTaskJob.setDown_type(task.getDown_type());
            try {
                String json = CommonUtils.subString("result: ", "});", document.toString());
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.has("shop_name")) {
                    // 店铺名称
                    baiduWaimaiProductTaskJob.setShop_name(jsonObject.getString("shop_name"));
                }
                if (jsonObject.has("comment_num")) {
                    // 评论总数
                    baiduWaimaiProductTaskJob.setComment_number(jsonObject.getString("comment_num"));
                }
            } catch (Exception e1) {
                logger.error("手机端百度外卖截取异常");
            }
            // 评分
            String score = null;
            try {
                score = document.select(".b-info>.rate-con").text();
            } catch (Exception e) {
                score = "-1";
            }
            // 营业时间
            String business_hour = null;
            try {
                business_hour = document.select(".sell-info").text().split("营业时间：")[1];
            } catch (Exception e) {
                business_hour = "-1";
            }
            // 商家地址
            String address = null;
            try {
                address = document.select(".address").select("p").text();
            } catch (Exception e) {
                address = "";
            }
            // 平均送餐时间
            String average_time = null;
            try {
                average_time = document.select(".basicinfo>.b-totime>.b-value").text();
            } catch (Exception e) {
                average_time = "";
            }
            // 起送价
            String min_price = null;
            try {
                min_price = document.select(".basicinfo>.b-price>.b-value").text();
            } catch (Exception e) {
                min_price = "-1";
            }
            // 配送费
            String delivery_fee = null;
            try {
                delivery_fee = document.select(".basicinfo>.b-cost>.b-value").text();
            } catch (Exception e) {
                delivery_fee = "-1";
            }
            // 总分
            String total_score = null;
            try {
                total_score = document.select(".b-info>.overall>.rate-info>.rate-num").text();
            } catch (Exception e) {
                total_score = "-1";
            }
            // 评论总数
            String total_comments = null;
            try {
                total_comments = document.select(".b-info>.overall>.rate-info").select("p").get(1).select("span")
                        .text();
            } catch (Exception e) {
                total_comments = "-1";
            }
            // 5星评论人数
            String five_star_numbers = null;
            try {
                five_star_numbers = document.select(".b-info>.overall>.rate-table").select("tbody").select("tr").get(0)
                        .select("td").get(2).text();
            } catch (Exception e) {
                five_star_numbers = "-1";
            }
            // 4星评论人数
            String four_star_numbers = null;
            try {
                four_star_numbers = document.select(".b-info>.overall>.rate-table").select("tbody").select("tr").get(1)
                        .select("td").get(2).text();
            } catch (Exception e) {
                four_star_numbers = "-1";
            }
            // 3星评论人数
            String three_star_numbers = null;
            try {
                three_star_numbers = document.select(".b-info>.overall>.rate-table").select("tbody").select("tr").get(2)
                        .select("td").get(2).text();
            } catch (Exception e) {
                three_star_numbers = "-1";
            }
            // 2星评论人数
            String two_star_numbers = null;
            try {
                two_star_numbers = document.select(".b-info>.overall>.rate-table").select("tbody").select("tr").get(3)
                        .select("td").get(2).text();
            } catch (Exception e) {
                two_star_numbers = "-1";
            }
            // 1星评论人数
            String one_star_numbers = null;
            try {
                one_star_numbers = document.select(".b-info>.overall>.rate-table").select("tbody").select("tr").get(4)
                        .select("td").get(2).text();
            } catch (Exception e) {
                one_star_numbers = "-1";
            }
            // 支付信息
            String payment_info = "";
            try {
                Elements notices = document.select(".side").select("#premium-notice").select("li");
                for (Element notice : notices) {
                    payment_info = payment_info + notice.select(".reduce-msg").text() + ";";
                }
            } catch (Exception e) {
            }
            // 商家公告
            String announcement = null;
            try {
                announcement = document.select(".side").select("#shop-notice").select(".notice-desc").text();
            } catch (Exception e) {
                announcement = "";
            }
            // 菜品总数
            int total_numbers = 0;
            try {
                Elements menu_lists = document.select(".menu-list>.list-wrap");
                for (Element menu_list : menu_lists) {
                    total_numbers = total_numbers + menu_list.select("ul").select("li").size();
                }
            } catch (Exception e) {
            }
            baiduWaimaiProductTaskJob.setProduct_number(String.valueOf(total_numbers));
            if (total_numbers > 300) {
                baiduWaimaiProductTaskJob.setLevel(9);
            } else {
                baiduWaimaiProductTaskJob.setLevel(0);
            }
            baiduWaimaiProductTaskJob.setRemark(task.getRemark());
            baiduWaimaiProductTaskJob.setLatitude(task.getLatitude());
            baiduWaimaiProductTaskJob.setLongitude(task.getLongitude());
            try {
                tDao.insert(baiduWaimaiProductTaskJob);
            } catch (Exception e) {
                //logger.error("", e);
            }
            tDao.update(BaiduWaimaiUrlTaskJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            logger.info("==========================URL解析完成，总耗时：" + (System.currentTimeMillis() - time)
                    + "============================");
        }
    }
}
