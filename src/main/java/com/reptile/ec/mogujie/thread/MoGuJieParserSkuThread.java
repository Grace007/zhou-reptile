package com.reptile.ec.mogujie.thread;

import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.ec.mogujie.model.MS_sku_info;
import com.reptile.ec.mogujie.model.job.MogujieProductTaskModel;
import com.reptile.ec.mogujie.util.StringUtils;
import com.reptile.server.MyConstant;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 根据每一件商品的URL来抓取现价，原价，库存等信息
 */
public class MoGuJieParserSkuThread extends Thread {

	private static Logger logger = Logger.getLogger(MoGuJieParserSkuThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private MogujieProductTaskModel task;

	public MoGuJieParserSkuThread(MogujieProductTaskModel task) {
		this.task = task;
	}

	@Override
	public void run() {

		try {
			tDao.update(MogujieProductTaskModel.class, Chain.make("sku_status", 1), Cnd.where("id", "=", task.getId()));
			Document doc = null;
			Dao biDao = BIDataSource.getBiDataSource("mogujie", task.getProject_code(), "get_product_info").getBiDao();

			try {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Cookie", MyConstant.MOGUJIE_CONFIG.COOKIE);
				String html = HttpBase.get(task.getUrl(), "utf-8", headers).getResult();

				doc = Jsoup.parse(html, "utf-8");
			} catch (Exception e) {
				tDao.update(MogujieProductTaskModel.class, Chain.make("sku_status", 3),
						Cnd.where("id", "=", task.getId()));
				logger.info(task.getUrl() + "无法连接，将稍后重新连接");
				return;
			}

			String ResouceScript = "";
			String FinalScript = "";
			Elements es = doc.select("script");
			for (int j = 0; j < es.size(); j++) {
				if (es.get(j).toString().contains("attribute")) {
					
					ResouceScript = StringUtils.stringSub(es.get(j).toString(), "attribute", "listBanner").replace("],",
							"");
					FinalScript = StringUtils.stringSub(ResouceScript, "[", "]");
					break;
				}
			}

			MS_sku_info mogujie = new MS_sku_info();

			JSONObject jsonSku = new JSONObject(FinalScript);
			String style = "", size = "", skuId = "", productId = "";
			double marketPrice = 0, price = 0;
			int stock = 0;
			try {
				style = jsonSku.getString("style");
			} catch (Exception e) {
				style = "";
			}
			try {
				size = jsonSku.getString("size");
			} catch (Exception e) {
				size = "";
			}

			// 原价
			marketPrice = StringUtils.toDouble(jsonSku.getString("price").trim());
			// 现价
			price = StringUtils.toDouble(jsonSku.getString("nowprice").trim());
			// 库存
			stock = StringUtils.toInt(jsonSku.getString("stock").trim());
			// xdSkuId
			skuId = jsonSku.getString("xdSkuId");

			// 产品ID
			productId = task.getProduct_id();

			mogujie.setProduct_id(productId);
			mogujie.setCreate_date(new Date());
			mogujie.setCategory(task.getCategory());
			mogujie.setKeyword(task.getKeyword());
			mogujie.setMarket_price(marketPrice / 100);
			mogujie.setPrice(price / 100);
			mogujie.setSize(size);
			mogujie.setSku_id(skuId);
			mogujie.setStock(stock);
			mogujie.setStyle(style);
			mogujie.setRequest_id(task.getRequest_id());

			try {
				biDao.fastInsert(mogujie);
			} catch (Exception e) {
				logger.info("================mogujie往BI库中灌入SKU信息失败，URL为：" + task.getUrl() + "================");
			}
			

			tDao.update(MogujieProductTaskModel.class, Chain.make("sku_status", 2), Cnd.where("id", "=", task.getId()));
			logger.info("完成解析商品 SKU 数据:" + mogujie.getProduct_id());

		} catch (Exception e) {
			tDao.update(MogujieProductTaskModel.class, Chain.make("sku_status", 3), Cnd.where("id", "=", task.getId()));
			logger.info("", e);
		}
	}

}
