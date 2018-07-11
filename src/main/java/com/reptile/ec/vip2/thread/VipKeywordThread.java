package com.reptile.ec.vip2.thread;

import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.vip2.job.VipCategoryUrlJob;
import com.reptile.ec.vip2.job.VipKeywordJob;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VipKeywordThread extends Thread {
    private static Logger logger = Logger.getLogger(VipKeywordThread.class);
    private static NutDao tDao = SpringContextHolder.getBean("tDao");
    private VipKeywordJob task;

    public VipKeywordThread(VipKeywordJob task) {
        this.task = task;
    }

    public void run() {
        tDao.update(VipKeywordJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
        logger.info("开始抓取唯品会keyword[" + task.getKeyword() + "]信息...");
        if (StringUtils.isNotEmpty(task.getKeyword())) {
            String result = null;
            try {
                String url = "http://category.vip.com/?keyword=" + URLEncoder.encode(task.getKeyword(), "utf-8");
                Map<String, String> header = new HashMap<String, String>();
                header.put("Host", "category.vip.com");
                //header.put("DNT", "1");
                header.put("Referer", url);
                //header.put("Cookie", "vip_rip=116.226.24.124; vip_province=103101; vip_province_name=%E4%B8%8A%E6%B5%B7; vip_city_name=%E4%B8%8A%E6%B5%B7%E5%B8%82; vip_city_code=103101101; vip_wh=VIP_SH; tmp_mars_cid=1490757090000_a0b9fcb1bfaece1a8c9c690bcb4dc1e8; user_class=a; VipUINFO=luc%3Aa%7Csuc%3Aa%7Cbct%3Ac_new%7Ckct%3Ac_new%7Chct%3Ac_new%7Cbdts%3A0%7Cbcts%3A0%7Ckfts%3A0%7Cc10%3A0%7Crcabt%3A0%7Cp2%3A0%7Cp3%3A1%7Cp4%3A0%7Cp5%3A0; mars_pid=0; visit_id=8BFB2EC86FF7B3DD9B4AFD9466600452; Hm_lvt_53a0d71dba66835ff1aa907db99144d8=1490757096,1490760161,1490769606; _mj_c=v3.3,last,1490769626942,1490757096749,315360000000|v3.3,cm,1490757097396,1490757096749,315360000000; _jzqco=%7C%7C%7C%7C%7C1.1997375953.1490757096916.1490769605764.1490769627017.1490769605764.1490769627017.0.0.0.20.20; _adwb=41626277; _adwr=41626277%230; _smt_uid=58db25e9.45f13d33; mars_sid=af53313d2faab291088ff3ebb3217954; _mj_si=si2122918912; _adwc=41626277; _adwp=41626277.6594509654.1490757097.1490760161.1490769605.3; Hm_lpvt_53a0d71dba66835ff1aa907db99144d8=1490769627; mars_cid=1490757090000_a0b9fcb1bfaece1a8c9c690bcb4dc1e8");
                result = VipBase.get("http://category.vip.com/ajax/getSuggest.php?keyword=" + URLEncoder.encode(task.getKeyword(), "utf-8"), "utf-8", header).getResult();
                JSONArray items = new JSONObject(result).getJSONArray("data");
                for(int i=0;i<items.length();i++){
                	VipCategoryUrlJob job = new VipCategoryUrlJob();
                	
                	job.setRequest_id(task.getRequest_id());
                    job.setTask_id(task.getTask_id());
                    job.setCreate_date(new Date());
                    job.setKeyword(task.getKeyword());
                    job.setUrl("http:" + items.getJSONObject(i).getString("url"));
                    job.setUpdate_status(0);
                    job.setNode(task.getNode());
                    job.setLevel(task.getLevel());
                    job.setProject_code(task.getProject_code());
                    job.setDown_type(task.getDown_type());
                    try {
                        tDao.insert(job);
                    } catch (Exception e) {
                        logger.error("唯品会VipCategoryUrlJob插入失败##########",e);
                    }
                	
                }
                logger.info("唯品会keyword[" + task.getKeyword() + "]信息生成完毕");
                tDao.update(VipKeywordJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
                
                /*if (results.has("brand") || results.has("category")) {
                    if (results.has("brand")) {
                        String type = results.getJSONObject("brand").getString("group_title");
                        try {
                            JSONArray items = results.getJSONObject("brand").getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                VipCategoryUrlJob job = new VipCategoryUrlJob();
                                job.setRequest_id(task.getRequest_id());
                                job.setTask_id(task.getTask_id());
                                job.setCreate_date(new Date());
                                job.setKeyword(task.getKeyword());
                                job.setCategory(items.getJSONObject(i).getString("title"));
                                job.setUrl("http:" + items.getJSONObject(i).getString("url"));
                                job.setType(type);
                                job.setUpdate_status(0);
                                job.setNode(task.getNode());
                                job.setLevel(task.getLevel());
                                job.setProject_code(task.getProject_code());
                                job.setDown_type(task.getDown_type());
                                try {
                                    tDao.insert(job);
                                } catch (Exception e) {
                                    //e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {

                        }
                    }
                    if (results.has("category")) {
                        String type = results.getJSONObject("category").getString("group_title");
                        try {
                            JSONArray items = results.getJSONObject("category").getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                VipCategoryUrlJob job = new VipCategoryUrlJob();
                                job.setRequest_id(task.getRequest_id());
                                job.setTask_id(task.getTask_id());
                                job.setCreate_date(new Date());
                                job.setKeyword(task.getKeyword());
                                job.setCategory(items.getJSONObject(i).getString("title"));
                                job.setUrl("http:" + items.getJSONObject(i).getString("url"));
                                job.setType(type);
                                job.setUpdate_status(0);
                                job.setNode(task.getNode());
                                job.setLevel(task.getLevel());
                                job.setProject_code(task.getProject_code());
                                job.setDown_type(task.getDown_type());
                                try {
                                    tDao.insert(job);
                                } catch (Exception e) {
                                    //e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {

                        }
                    }

                    logger.info("唯品会keyword[" + task.getKeyword() + "]信息生成完毕");
                    tDao.update(VipKeywordJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
                } else {
                    tDao.update(VipKeywordJob.class, Chain.make("status", -1), Cnd.where("id", "=", task.getId()));
                }*/
            } catch (Exception e) {
                logger.error("解析异常", e);
                tDao.update(VipKeywordJob.class, Chain.make("status", 3),
                        Cnd.where("id", "=", task.getId()));
            }
        } else {
            tDao.update(VipKeywordJob.class, Chain.make("status", -1), Cnd.where("id", "=", task.getId()));
        }
    }

    public static void main(String[] args) {

    }
}
