package com.reptile.haodaifu.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.MD5Util;
import com.reptile.haodaifu.job.HaoDFWendaSimpleJob;
import com.reptile.haodaifu.model.HaoDFWendaInfo;
import com.reptile.server.MyConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;

import java.util.Date;
public class HaoDFWendaInfoThread extends Thread {
	//31异常，问答页面的地址URL解析出错

	private Dao mysqlDao = MySqlDataSource.getMysqlDao(MyConstant.ZNode);
	private HaoDFWendaSimpleJob task;
	private Logger logger =Logger.getLogger(HaoDFWendaInfoThread.class);
	TaskToBi tobi = BIDataSource.getBiDataSource("doctor", "DOCTOR", "get_article_info");
	Dao biDao = tobi.getBiDao();
	public  HaoDFWendaInfoThread(Record task) {
		this.task = task.toPojo(HaoDFWendaSimpleJob.class);
	}
	
	@Override
	public void run() {
		try {
			mysqlDao.update(HaoDFWendaSimpleJob.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			Elements wendaElements=null;
			Element wendaElement =null;
			String spokesman="",wenda_title="",wenda_time="",patient_status="",wenda_content="",wenda_type="";
			String baseUrl =task.getWenda_url();
			String html="";
			int ranking=1,pagenum=1,pagetotal=1;
			
			HaoDFWendaInfo info = new HaoDFWendaInfo();
			info.setCreate_time(new Date());
			info.setDid(task.getDid());
			info.setRequest_id(task.getRequest_id());
			info.setTask_id(task.getTask_id());
			info.setNode(task.getNode());
			info.setPagenum(task.getPagenum());
			info.setWenda_url(task.getWenda_url());
			info.setWenda_id(subString("_g_", ".htm", task.getWenda_url()));
			info.setDisease(task.getDisease());
			info.setTiny_categoryname(task.getTiny_categoryname());
			
			//baseUrl+="?p=1";
			//url规则修改
			//baseUrl+=baseUrl.replace(".htm","_p_1.htm");
			
			try {
				html = HttpBase.get(baseUrl, "utf-8").getResult();
			} catch (Exception e) {
				System.out.println("界面解析失败!");
				mysqlDao.update(HaoDFWendaSimpleJob.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
				e.printStackTrace();
				return;
			}
			Document doc1 = Jsoup.parse(html);
			try {
				pagetotal=new Integer(doc1.select("div.page_turn").select("a.page_turn_a").last().text().replaceAll("\\D", ""));
			} catch (Exception e) {
				pagetotal=1;
			}
			System.out.println("总页数："+pagetotal);
			
		
			do {
			
			
			try {
				html = HttpBase.get(baseUrl, "utf-8").getResult();
				System.out.println("翻页地址："+baseUrl);
			} catch (Exception e) {
				System.out.println("界面解析失败!");
				mysqlDao.update(HaoDFWendaSimpleJob.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
				e.printStackTrace();
				return;
			}
			Document doc = Jsoup.parse(html);
			wendaElements = doc.select("div.zzx_yh_stream");
			wenda_title=doc.select("div.clearfix.zzx_yh_h1").text();
			info.setWenda_title(wenda_title);
			logger.info("##"+wendaElements.size());
			for (int i = 0; i < wendaElements.size(); i++) {
				wendaElement = wendaElements.get(i);
				if (!StringUtils.equals(wendaElement.select("div.yh_r_t_icon").select("img").first().attr("src"), "http://i1.hdfimg.com/doctorzone/images/yi.png") ) {
					spokesman=wendaElement.select("div.stream_yh_left").select("div.yh_l_huan").text();
					wenda_time=wendaElement.select("div.stream_yh_left").select("div.yh_l_times").text();
					patient_status=wendaElement.select("div.stream_yh_left").select("div.yh_l_states").select("span").text();
					wenda_content=wendaElement.select("div.stream_yh_right").select("div.h_s_cons").text();
					if (StringUtils.isEmpty(wenda_content)) {
						 wenda_content=wendaElement.select("div.stream_yh_right").select("div.h_s_info_cons").text();
					}
					wenda_type="病人发言";
				}
				else{
					
					spokesman=wendaElement.select("div.yh_l_doctor").select("span").text();
					wenda_content=wendaElement.select("div.stream_yh_right").select("h3.h_s_cons_title").text();
					wenda_time=wendaElement.select("div.stream_yh_left").select("div.yh_l_times").text();
					
					wenda_type="医生发言";
				}
				if (StringUtils.isEmpty(spokesman)){
					wenda_type="医生助理";
					spokesman="系统回复";
				}
				
				info.setSpokesman(spokesman);
				info.setPagenum(pagenum);
				info.setRanking(ranking);
				info.setWenda_content(wenda_content);
				info.setWenda_time(wenda_time);
				info.setWenda_type(wenda_type);;
				
				info.setPatient_status(patient_status);
				info.setRanking(ranking);
				info.setMd5(MD5Util.getMD5(task.getRequest_id()+task.getTask_id()+task.getWenda_url()+wenda_content+wenda_type));
				try {
					biDao.insert(info);
				} catch (Exception e) {
					//logger.error("wendainfo插入失败",e);
				}
				ranking++;
				spokesman="";wenda_time="";patient_status="";wenda_content="";wenda_type="";
			}
			
			if (wendaElements.size()<20|| pagenum >=pagetotal|| pagenum>=9 ) {
				break;
			}
			pagenum++;
			//_p_1.htm
			//baseUrl=baseUrl.replace("htm?p="+String.valueOf(pagenum-1), "htm?p="+String.valueOf(pagenum));
			if(2==pagenum)
				baseUrl=baseUrl.replace(".htm","_p_2.htm");
			else
				baseUrl=baseUrl.replace(String.valueOf(pagenum-1)+".htm",String.valueOf(pagenum)+".htm");
			
			} while (true);
			
			
			
			logger.info("HaoDFWendainfo任务完成,pagenum="+pagenum+"   pagetotal="+pagetotal);
			mysqlDao.update(HaoDFWendaSimpleJob.class, Chain.make("status", 2), 
					Cnd.where("id", "=", task.getId()));
		} catch (Exception e) {
			mysqlDao.update(HaoDFWendaSimpleJob.class, Chain.make("status", 3), 
					Cnd.where("id", "=", task.getId()));
			logger.error("HaoDFWendainfo出错",e);
		}
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
