package com.reptile.ec.mogujie.thread;

import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.mogujie.model.job.MogujieListNextTaskModel;
import com.reptile.ec.mogujie.model.job.MogujieProductTaskModel;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;

/**
 * 抓取搜到的每一页加载完成的产品信息 抓取每一个商品的标题,价格，销量，收藏，折扣，描述等信息
 * 
 */
public class MoGuJieSnatchProductThread extends Thread {

	private static Logger logger = Logger.getLogger(MoGuJieSnatchProductThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private MogujieListNextTaskModel task;

	public MoGuJieSnatchProductThread(MogujieListNextTaskModel task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {

			tDao.update(MogujieListNextTaskModel.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			String html = "", url = "", title = "", discount = "", description = "", product_id = "";
			double price = 0;
			int collect = 0, sale_num = 0;

			try {
				html = HttpBase.get(task.getList_next_url(), "utf-8").getResult();
			} catch (Exception e) {
				tDao.update(MogujieListNextTaskModel.class, Chain.make("status", 31),
						Cnd.where("id", "=", task.getId()));
				logger.info(task.getList_next_url() + "无法连接，将稍后重新连接");
				return;
			}
//			html = html.substring(html.indexOf("(") + 1, html.lastIndexOf(")"));
			JSONObject jsonObject = new JSONObject(html);

			JSONArray jsonArray = new JSONArray(jsonObject.getJSONObject("result").getJSONObject("wall").getString("docs"));
			JSONObject jObject = null;
			
			for (int i = 0; i < jsonArray.length(); i++) {

				jObject = jsonArray.getJSONObject(i);
				title = jObject.getString("title");
				price = Double.parseDouble(jObject.getString("price")) ;
				product_id = jObject.getString("tradeItemId") ;
				try {
					collect = Integer.parseInt(jObject.getString("cfav")) ;
				} catch (Exception e) {
					
				}
				
				url = "http://shop.mogujie.com/detail/" + product_id;

				MogujieProductTaskModel mogujie = new MogujieProductTaskModel();

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
				mogujie.setCollect(collect);
				mogujie.setComment_status(0);
//				mogujie.setDescription(description);
//				mogujie.setDiscount(discount);
				mogujie.setPrice(price);
				mogujie.setProduct_id(product_id);
//				mogujie.setSale_num(sale_num);
				mogujie.setTitle(title);
				mogujie.setUrl(url);
				mogujie.setDeal_status(0);
				mogujie.setSku_status(0);
				mogujie.setType(task.getType());

				try {
					tDao.fastInsert(mogujie);
					logger.info("插入一个新的PRODUCT NEXT检测数据:" + mogujie.getKeyword() + "..." + mogujie.getUrl());
				} catch (Exception e) {
					logger.info("================mogujie生成产品任务失败，URL为：" + mogujie.getUrl() + "================");
				}

			}

			tDao.update(MogujieListNextTaskModel.class, Chain.make("status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("完成插入PRODUCT NEXT检测数据:" + task.getKeyword());
		} catch (Exception e) {
			tDao.update(MogujieListNextTaskModel.class, Chain.make("status", 3), Cnd.where("id", "=", task.getId()));
			logger.info("", e);
		}
	}
}
