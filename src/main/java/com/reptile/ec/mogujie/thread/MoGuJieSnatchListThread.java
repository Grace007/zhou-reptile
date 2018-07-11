package com.reptile.ec.mogujie.thread;

import com.bds.base.util.CommonUtils;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.mogujie.model.job.MogujieListTaskModel;
import com.reptile.ec.mogujie.model.job.MogujieRequestUrlTaskModel;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.openqa.selenium.WebDriver;

import java.util.Date;


public class MoGuJieSnatchListThread extends Thread {

	private static Logger logger = Logger.getLogger(MoGuJieSnatchListThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private MogujieRequestUrlTaskModel task;
	WebDriver dr = null;

	public MoGuJieSnatchListThread(MogujieRequestUrlTaskModel task) {
		this.task = task;
	}

	@Override
	public void run() {

		try {

			tDao.update(MogujieRequestUrlTaskModel.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));

			MogujieListTaskModel mogujie = new MogujieListTaskModel();

			if (task.getUrl().contains("ppath") && task.getUrl().contains("{")) {

				String ppath = task.getUrl().substring(task.getUrl().indexOf("{") + 2,
						task.getUrl().lastIndexOf("}") - 1);
				String[] aStrings = ppath.split("\"%3A\"");

				if (aStrings.length > 2) {

					tDao.update(MogujieRequestUrlTaskModel.class, Chain.make("status", 3),
							Cnd.where("id", "=", task.getId()));
					return;

				}
				mogujie.setPpath("%7B%E2%80%9C" + aStrings[0] + "%E2%80%9C%3A%E2%80%9C" + aStrings[1] + "%E2%80%9C%7D");
			}

			if ((task.getUrl().contains("ppath")) && (!task.getUrl().contains("{"))) {
				
//				String categotry = java.net.URLDecoder.decode(task.getUrl().substring(task.getUrl().indexOf("ppath=")+6),"utf-8") ;
//				String [] bsString = categotry.split(":") ;
//				mogujie.setPpath("%7B%E2%80%9C"+ bsString[0].replace("{", "").replace("\"", "")  
//						+ "%E2%80%9C%3A%E2%80%9C" + bsString[1].replace("}", "").replace("\"", "") + "%E2%80%9C%7D");
				String ppath = CommonUtils.subString("ppath=", "%7D", task.getUrl()) ;
				mogujie.setPpath(ppath + "%7D");

			}

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
			mogujie.setType(task.getType());
			mogujie.setList_url(task.getUrl());

			try {
				tDao.fastInsert(mogujie);
			} catch (Exception e) {
				
			}
			
			tDao.update(MogujieRequestUrlTaskModel.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("插入一个新的LIST检测数据:" + mogujie.getList_url() + "&ppath=" + mogujie.getPpath());
		} catch (Exception e) {
			tDao.update(MogujieRequestUrlTaskModel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.info("", e);
		}
	}
}
