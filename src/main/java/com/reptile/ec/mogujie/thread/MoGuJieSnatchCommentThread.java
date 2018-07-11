package com.reptile.ec.mogujie.thread;

import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.mogujie.model.job.MogujieCommentTaskModel;
import com.reptile.ec.mogujie.model.job.MogujieProductTaskModel;
import com.reptile.ec.mogujie.util.Calculate;
import com.reptile.ec.mogujie.util.StringUtils;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class MoGuJieSnatchCommentThread extends Thread {

	private static Logger logger = Logger.getLogger(MoGuJieSnatchCommentThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private MogujieProductTaskModel task;

	public MoGuJieSnatchCommentThread(MogujieProductTaskModel task) {
		this.task = task;
	}

	@Override
	public void run() {

		try {

			tDao.update(MogujieProductTaskModel.class, Chain.make("comment_status", 1),
					Cnd.where("id", "=", task.getId()));
			String html = "" ;
			Document doc = null ;
			try {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Cookie", MyConstant.MOGUJIE_CONFIG.COOKIE);
				html = HttpBase.get(task.getUrl(), "utf-8", headers).getResult();
				doc = Jsoup.parse(html, "utf-8");
			} catch (Exception e) {
				tDao.update(MogujieProductTaskModel.class, Chain.make("status", 31),
						Cnd.where("id", "=", task.getId()));
				logger.info(task.getUrl() + "无法连接，将稍后重新连接");
				return;
			}
			// 评论数
			int com_num = StringUtils
					.toInt(doc.select("dd.property-extra").select("span.mr10").select("span.num").text());
			// 判断评论总共有多少页
			int pageTotal = Calculate.calculatePage(com_num, 20);

			for (int i = 1; i <= pageTotal; i++) {
//				sort为0，按照时间排序，为1，按照默认排序
				String comUrl = "http://shop.mogujie.com/ajax/pc.rate.ratelist/v1?pageSize=20&sort=0&isNewDetail=1&itemId="
						+ task.getProduct_id()
						+ "&type=1&page="+i;
				// http://shop.mogujie.com/trade/item_detail/ratelist?itemId=16nxy7w&order=&page=3&type=0&emotion=&property=&sort=1
//				http://shop.mogujie.com/ajax/pc.rate.ratelist/v1?pageSize=20&sort=1&isNewDetail=1&itemId=17stkmq&type=1&page=2
				MogujieCommentTaskModel mogujie = new MogujieCommentTaskModel();

				mogujie.setKeyword(task.getKeyword());
				mogujie.setCategory(task.getCategory());
				mogujie.setProduct_id(task.getProduct_id());
				mogujie.setCreate_date(new Date());
				mogujie.setCom_num(task.getCom_num());
				mogujie.setCom_url(comUrl);
				mogujie.setNode(task.getNode());
				mogujie.setPage_index(i);
				mogujie.setProject_code(task.getProject_code());
				mogujie.setPage_total(pageTotal);
				mogujie.setStatus(0);
				mogujie.setWebsite(task.getWebsite());
				mogujie.setRequest_id(task.getRequest_id());
				mogujie.setTask_id(task.getTask_id());
				mogujie.setType(task.getType());

				try {
					tDao.fastInsert(mogujie);
				} catch (Exception e) {
					logger.info("================mogujie生成评论任务失败，URL为：" + mogujie.getCom_url() + "================");
				}
				logger.info("插入一条评论检测数据:" + mogujie.getProduct_id() + "，完成率：" + mogujie.getPage_index() + "/"
						+ mogujie.getPage_total());
			}

			tDao.update(MogujieProductTaskModel.class, Chain.make("comment_status", 2).add("com_num", com_num),
					Cnd.where("id", "=", task.getId()));
			logger.info("完成插入评论检测数据:" + task.getProduct_id());
		} catch (Exception e) {
			tDao.update(MogujieProductTaskModel.class, Chain.make("comment_status", 3),
					Cnd.where("id", "=", task.getId()));
			logger.info("", e);
		}
	}
}
