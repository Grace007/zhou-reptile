package com.reptile.mobile_eleme_breakfast.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.reptile.mobile_eleme_breakfast.httpbase.MobileElemeHttpsBase;
import com.reptile.mobile_eleme_breakfast.job.ElemeBreakfastKeywordJob;
import com.reptile.mobile_eleme_breakfast.job.ElemeBreakfastKeywordNextJob;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

public class ElemeBreakfastKeywordThread extends Thread {
    //private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
    private static Logger logger = Logger.getLogger(ElemeBreakfastKeywordThread.class);
    private ElemeBreakfastKeywordJob task;

    public ElemeBreakfastKeywordThread(ElemeBreakfastKeywordJob task) {
        this.task = task;
    }

    public void run() {
        long total_time = System.currentTimeMillis();
        logger.info("开始抓取饿了么预定早餐[" + task.getCity_name() + "]地址[" + task.getKeyword() + "]下办公楼...");
        tDao.update(ElemeBreakfastKeywordJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        String city_id = task.getCity_id(), name = task.getKeyword();
        if (StringUtils.isEmpty(city_id) || StringUtils.isEmpty(name)) {
            logger.info("抓取饿了么预定早餐ID[" + task.getId() + "]参数不全");
            tDao.update(ElemeBreakfastKeywordJob.class, Chain.make("status", -1), Cnd.where("id", "=", task.getId()));
            return;
        } else {
            String req_url = "https://zaocan.ele.me/api/building/search?cityId=" + city_id + "&name=" + URLEncoder.encode(name);
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Host", "zaocan.ele.me");
            header.put("Referer", "https://zaocan.ele.me/home?force=1");
            String content;
            try {
                content = MobileElemeHttpsBase.get(req_url, "utf-8", header).getResult();
                JSONObject result = new JSONObject(content);
                if ("ERROR".equals(result.getString("status"))) {
                    logger.info("饿了么预定早餐[" + task.getCity_name() + "]地址[" + task.getKeyword() + "]下无办公楼");
                    tDao.update(ElemeBreakfastKeywordJob.class, Chain.make("status", 404), Cnd.where("id", "=", task.getId()));
                    return;
                } else {
                    JSONArray data = result.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        ElemeBreakfastKeywordNextJob job = new ElemeBreakfastKeywordNextJob();
                        job.setRemark(task.getRemark());
                        job.setRequest_id(task.getRequest_id());
                        job.setTask_id(task.getTask_id());
                        job.setCreate_time(new Date());
                        job.setCreate_date(new Date());
                        job.setCity_name(task.getCity_name());
                        job.setCity_id(city_id);
                        job.setKeyword(name);
                        job.setBuilding_name(data.getJSONObject(i).getString("name"));
                        job.setBuilding_id(data.getJSONObject(i).getString("buildingId"));
                        job.setStatus(0);
                        job.setNode(task.getNode());
                        job.setProject_code(task.getProject_code());
                        job.setDown_type(task.getDown_type());
                        try {
                            tDao.insert(job);
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }
                    logger.info("抓取饿了么预定早餐[" + task.getCity_name() + "]地址[" + task.getKeyword() + "]下办公楼完成，总耗时：" + (System.currentTimeMillis() - total_time));
                    tDao.update(ElemeBreakfastKeywordJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
                }
            } catch (Exception e) {
                logger.error("", e);
                tDao.update(ElemeBreakfastKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
            }
        }
    }
}
