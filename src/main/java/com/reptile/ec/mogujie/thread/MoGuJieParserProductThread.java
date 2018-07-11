package com.reptile.ec.mogujie.thread;

import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.mogujie.model.MS_product_info;
import com.reptile.ec.mogujie.model.job.MogujieProductTaskModel;
import com.reptile.ec.mogujie.util.StringUtils;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 根据每个产品的URL来抓取tag,商品描述，评价数
 * 
 */
public class MoGuJieParserProductThread extends Thread {

	private static Logger logger = Logger.getLogger(MoGuJieParserProductThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private MogujieProductTaskModel task;

	public MoGuJieParserProductThread(MogujieProductTaskModel task) {
		this.task = task;
	}

	@Override
	public void run() {

		try {
			tDao.update(MogujieProductTaskModel.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			Document doc = null;
			String html = "";
			Dao biDao = BIDataSource.getBiDataSource("mogujie", task.getProject_code(), "get_product_info").getBiDao();

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

			// 标签：
			String tag = doc.select("div.tags").select("div.list").text();
			// 评论数
			int com_num = StringUtils
					.toInt(doc.select("dd.property-extra").select("span.mr10").select("span.num").text());
			// 销量
			int sale_num = StringUtils.toInt(doc.select("span.J_SaleNum").text());

			String jsonUrl = "http://shop.mogujie.com/ajax/mgj.pc.detailinfo/v1?_ajax=1&itemId="+task.getProduct_id();
			// http://shop.mogujie.com/detailinfo/16qtyym?_ajax=1
			String content = HttpBase.get(jsonUrl, "utf-8").getResult();
			JSONObject json = new JSONObject(content);
			String description = "";
			try {
				description = json.getJSONObject("data").getJSONObject("detailInfos").getString("desc");
			} catch (Exception e) {
				description = "";
			}
			MS_product_info mogujie = new MS_product_info();

			mogujie.setKeyword(task.getKeyword());
			mogujie.setCom_num(com_num);
			mogujie.setCategory(task.getCategory());
			mogujie.setTitle(task.getTitle());
			mogujie.setSale_num(sale_num);
			mogujie.setUrl(task.getUrl());
			mogujie.setProduct_id(task.getProduct_id());
			mogujie.setCollect(task.getCollect());
			mogujie.setCreate_date(new Date());
			mogujie.setDescription(description);
			mogujie.setDiscount(task.getDiscount());
			mogujie.setTag(tag);
			mogujie.setPrice(task.getPrice());
			mogujie.setRequest_id(task.getRequest_id());

			try {
				biDao.fastInsert(mogujie);
			} catch (Exception e) {
				logger.info("================mogujie往BI库中灌入产品信息失败，URL为：" + task.getUrl() + "================");
			}

			tDao.update(MogujieProductTaskModel.class, Chain.make("status", 2).add("com_num", com_num),
					Cnd.where("id", "=", task.getId()));
			logger.info("完成解析商品 一般 数据:" + mogujie.getUrl());

		} catch (Exception e) {
			tDao.update(MogujieProductTaskModel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.info("", e);
		}
	}
}
