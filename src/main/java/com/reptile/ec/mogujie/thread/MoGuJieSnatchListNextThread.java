package com.reptile.ec.mogujie.thread;

import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.mogujie.model.job.MogujieListNextTaskModel;
import com.reptile.ec.mogujie.model.job.MogujieListTaskModel;
import com.reptile.ec.mogujie.util.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;

public class MoGuJieSnatchListNextThread extends Thread {
	private static Logger logger = Logger.getLogger(MoGuJieSnatchListNextThread.class);

	private Dao tDao = SpringContextHolder.getBean("tDao");
	private MogujieListTaskModel task;

	public MoGuJieSnatchListNextThread(MogujieListTaskModel task) {
		this.task = task;
	}

	@Override
	public void run() {

		String url = "", html = "", keyword = task.getKeyword();
		String productTotal = "";
		try {

			tDao.update(MogujieListTaskModel.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			try {

				if (org.apache.commons.lang.StringUtils.isEmpty(task.getPpath())) {
					keyword = java.net.URLEncoder.encode(keyword, "utf-8");
					// http://list.mogujie.com/search?_version=1&sort=pop&cKey=pc-search-wall&page=1&q=%25E7%25B2%2589%25E5%25BA%2595%25E6%25B6%25B2&ppath=%257B%25E2%2580%259C2048%25E2%2580%259C%253A%25E2%2580%259C10479%25E2%2580%259C%257D
					url = "http://list.mogujie.com/search?_version=1&cKey=pc-search-wall&page=1" + "&q=" + keyword;
				} else if (task.getList_url().contains("{")) {
					// 蘑菇街对应的Keyword要经过两次encode
					keyword = java.net.URLEncoder.encode(keyword, "utf-8");
					keyword = java.net.URLEncoder.encode(keyword, "utf-8");
					// http://list.mogujie.com/search?_version=1&sort=pop&cKey=pc-search-wall&page=1&q=%25E7%25B2%2589%25E5%25BA%2595%25E6%25B6%25B2&ppath=%257B%25E2%2580%259C2048%25E2%2580%259C%253A%25E2%2580%259C10479%25E2%2580%259C%257D
					url = "http://list.mogujie.com/search?_version=1&cKey=pc-search-wall&page=1" + "&q=" + keyword
							+ "&ppath=" + java.net.URLEncoder.encode(task.getPpath(), "utf-8");
				}else {
					keyword = java.net.URLEncoder.encode(keyword, "utf-8");
					// http://list.mogujie.com/search?_version=1&sort=pop&cKey=pc-search-wall&page=1&q=%25E7%25B2%2589%25E5%25BA%2595%25E6%25B6%25B2&ppath=%257B%25E2%2580%259C2048%25E2%2580%259C%253A%25E2%2580%259C10479%25E2%2580%259C%257D
					url = "http://list.mogujie.com/search?_version=1&cKey=pc-search-wall&page=1" + "&q=" + keyword
							+ "&ppath=" + java.net.URLEncoder.encode(task.getPpath(), "utf-8");
					
				}
				
				html = HttpBase.get(url, "utf-8").getResult();
			} catch (Exception e) {
				tDao.update(MogujieListTaskModel.class, Chain.make("status", 31), Cnd.where("id", "=", task.getId()));
				logger.info("蘑菇街连接网络失败，稍后将重新连接，URL为：" + task.getList_url(),e);
				return;
			}

			int pageTotal = 1;
			JSONObject jsonObject = new JSONObject(html);
			String total = jsonObject.getJSONObject("result").getJSONObject("wall").getJSONObject("pager")
					.getString("total");

			if (Integer.parseInt(total) % 60 == 0) {
				pageTotal = Integer.parseInt(total) / 60;
			} else {
				pageTotal = Integer.parseInt(total) / 60 + 1;
			}

			MogujieListNextTaskModel mogujie = new MogujieListNextTaskModel();

			for (int i = 1; i <= pageTotal; i++) {

				mogujie.setCreate_date(new Date());
				mogujie.setNode(task.getNode());
				mogujie.setRequest_id(task.getRequest_id());
				mogujie.setTask_id(task.getTask_id());
				mogujie.setWebsite(task.getWebsite());
				mogujie.setStatus(0);
				mogujie.setProject_code(task.getProject_code());
				mogujie.setKeyword(task.getKeyword());
				mogujie.setCategory(task.getCategory());
				mogujie.setKeyword(task.getKeyword());
				
				if (org.apache.commons.lang.StringUtils.isEmpty(task.getPpath())) {
					mogujie.setList_next_url("http://list.mogujie.com/search?_version=1&cKey=pc-search-wall&page=" + i
							+ "&q=" + keyword );
				} else {
					mogujie.setList_next_url("http://list.mogujie.com/search?_version=1&cKey=pc-search-wall&page=" + i
							+ "&q=" + keyword + "&ppath=" + java.net.URLEncoder.encode(task.getPpath(), "utf-8"));
				}
				mogujie.setPpath(task.getPpath());
				mogujie.setPage_index(i);
				mogujie.setPage_total(pageTotal);
				mogujie.setProduct_total(StringUtils.toInt(productTotal));
				mogujie.setType(task.getType());

				try {
					tDao.fastInsert(mogujie);
				} catch (Exception e) {
					logger.info("插入一个新的LIST NEXT检测数据:" + mogujie.getList_next_url());
				}

			}

			tDao.update(MogujieListTaskModel.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("完成插入LIST NEXT检测数据:" + task.getKeyword() + "，" + task.getCategory() + "，" + task.getPpath());

		} catch (Exception e) {
			tDao.update(MogujieListTaskModel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.info("", e);
		}
	}
}
