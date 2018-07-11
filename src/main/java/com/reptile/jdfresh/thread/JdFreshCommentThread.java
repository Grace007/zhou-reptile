package com.reptile.jdfresh.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.bds.base.util.MD5Util;
import com.reptile.jdfresh.job.JdFreshCommentJob;
import com.reptile.jdfresh.model.JdFreshComment;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author eli
 * @date 2017/11/16 11:05
 */
public class JdFreshCommentThread extends Thread {
    private Logger logger = Logger.getLogger(JdFreshCommentThread.class);
    private Dao mysqldao = MySqlDataSource.getMysqlDao(Constant.ZNode);
    TaskToBi tobi = BIDataSource.getBiDataSource("fresh", "DC_FRESH", "get_sort_info");
    Dao bidao = tobi.getBiDao();
    private JdFreshCommentJob task;

    public JdFreshCommentThread(Record task) {
        this.task = task.toPojo(JdFreshCommentJob.class);
    }
    @Override
    public void run() {
        try {
            mysqldao.update(JdFreshCommentJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));

            //开始分页部分
            int pagestart=task.getPagestart();
            int pagestop=task.getPagestop();
            //if (pagestart==0) pagestart=1;
            int page_limit = task.getPage_limit();

            while (true){
                if (pagestart > page_limit) {
                    break;
                }
                String url="http://club.jd.com/comment/skuProductPageComments.action?productId="+task.getPid()+"&score=0&sortType=5&page="+pagestart+"&pageSize=10&isShadowSku=0&rid=0&fold=1";
                String html = "";
                HashMap<String, String> header = new HashMap<String, String>();
                header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                //header.put("cookie","");
                header.put("", "");
                try {
                    html = HttpBase.get(url, "utf-8", header).getResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String nickname="",userexp="",score="",content="",referencename="",referencetime="";
                Document doc = Jsoup.parse(html);
                try {
                    int commentcount = new JSONObject(html).getJSONObject("productCommentSummary").getInt("commentCount");
                    int pagetotal = commentcount /10 ;
                    if (pagetotal > 99 ) pagetotal=99;
                    if (pagetotal > pagestop) {
                        pagestop =pagetotal;
                        mysqldao.update(JdFreshCommentJob.class, Chain.make("pagestop",pagestop), Cnd.where("id", "=", task.getId()));
                    }
                } catch (JSONException e) {
                    logger.error("获取commenttotal失败",e);
                }
                logger.info("当前页数:"+pagestart+"  总页数:"+pagestop);
                JSONArray commentArray = new JSONObject(html).getJSONArray("comments");
                System.out.println("commentArray = " + commentArray.length());
                List<JdFreshComment> jdFreshCommentJobList = new ArrayList<JdFreshComment>();
                for (int i=0;i<commentArray.length();i++){
                    JSONObject commentJson= commentArray.getJSONObject(i);
                    nickname=commentJson.getString("nickname");
                    userexp=commentJson.getString("userExpValue");
                    score=commentJson.getString("score");
                    content=commentJson.getString("content");
                    referencename=commentJson.getString("referenceName");
                    referencetime=commentJson.getString("referenceTime");
                   /* System.out.println("############");
                    System.out.println("nickname = " +nickname );
                    System.out.println("userexp = " +userexp );
                    System.out.println("score = " +score );
                    System.out.println("content = " +content );
                    System.out.println("referencename = " +referencename );
                    System.out.println("referencetime = " +referencetime );*/

                    JdFreshComment jdFreshComment = new JdFreshComment();
                    jdFreshComment.setCategory(task.getCategory());
                    jdFreshComment.setCity(task.getProvince());
                    jdFreshComment.setContent(content);
                    jdFreshComment.setNickname(nickname);
                    jdFreshComment.setPid(task.getPid());
                    jdFreshComment.setPname(task.getPname());
                    //jdFreshComment.setProvince(task.getProvince());
                    jdFreshComment.setReferencename(referencename);
                    jdFreshComment.setReferencetime(referencetime);
                    jdFreshComment.setScore(score);
                    jdFreshComment.setShop(task.getShop());
                    jdFreshComment.setShopid(task.getShopid());
                    jdFreshComment.setShoptype(task.getShoptype());
                    jdFreshComment.setUserexp(userexp);
                    jdFreshComment.setTask_id(task.getTask_id());
                    jdFreshComment.setRequest_id(task.getRequest_id());
                    jdFreshComment.setCreate_time(new Date());
                    jdFreshComment.setCreate_date(new Date());
                    jdFreshComment.setUniqueid(MD5Util.getMD5(nickname+task.getPid()+content+referencetime));
                    jdFreshCommentJobList.add(jdFreshComment);


                }
                try {
                    bidao.fastInsert(jdFreshCommentJobList);
                } catch (Exception e) {
                    logger.error("评论重复 ....");
                }

                pagestart++;
                if (pagestart > pagestop) break;
                mysqldao.update(JdFreshCommentJob.class, Chain.make("pagestart",pagestart), Cnd.where("id", "=", task.getId()));



            }

            mysqldao.update(JdFreshCommentJob.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
            logger.info("CommentJob任务成功!");


        } catch (Exception e) {
            logger.error("CommentJob任务出错...",e);
            mysqldao.update(JdFreshCommentJob.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
        }

    }




}
