package com.reptile.ly.task.scenery.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.ly.model.scenery.LySceneryPriceTagModel;
import com.reptile.ly.model.scenery.LySceneryProductInfoModel;
import com.reptile.ly.model.scenery.job.LySceneryCommentJob;
import com.reptile.ly.model.scenery.job.LySceneryKeywordJob;
import com.reptile.tianya.common.CommonUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 同程景点-根据url(关键字)获取commentJob和获取酒店info,同程景点每种票的信息priceTag
 * @author zhour
 *
 */
public class LySceneryKeywordThread extends Thread {
	private static Logger logger = Logger.getLogger(LySceneryKeywordThread.class);

	// dao层变量
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(Constant.ZNode);
	// NumysqlDao mysqlDao = new NumysqlDao();

	private LySceneryKeywordJob job;

	public LySceneryKeywordThread(Record task) {
		this.job = task.toPojo(LySceneryKeywordJob.class);
	}

	public void run() {
		try {
			mysqlDao.update(LySceneryKeywordJob.class, Chain.make("status", 1), Cnd.where("id", "=", job.getId()));
			this.parse();
		} catch (Exception e) {
			mysqlDao.update(LySceneryKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("解析ly_scenery异常：关键词：" + job.getKeyword(), e);
		}
	}

	public void parse() {
		LySceneryKeywordJob lskj = job;
		TaskToBi tobi = BIDataSource.getBiDataSource("ly", job.getProject_code(), job.getDown_type());
		Dao biDao = tobi.getBiDao();

		String url = lskj.getUrl();
		WebDriver dr = HttpBase.getFirefoxDriver();

		try {
			dr.get(url);
			JavascriptExecutor js = (JavascriptExecutor) dr;
			js.executeScript("scrollTo(0,6000)");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
			}

			String html = dr.getPageSource();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e2) {
			}

			LySceneryProductInfoModel lspi = getSceneryProduct(html, lskj, dr);

			try {
				biDao.insert(lspi);
				mysqlDao.update(LySceneryKeywordJob.class, Chain.make("status", 2), Cnd.where("id", "=", lskj.getId()));
				logger.info("|||Url:【" + lskj.getUrl() + "】的同程景点产品详情获取成功|||");
			} catch (Exception e) {
				mysqlDao.update(LySceneryKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", lskj.getId()));
				logger.error("LySceneryProductInfoModel插入bi失败,LySceneryKeywordJob-id[" + job.getId() + "] url["
						+ job.getKeyword() + "]", e);
			}
			// insert ticket price
			try {
				for (int i = 0; i < lspi.getScenery_prices().size(); i++) {
					try {
						biDao.insert(lspi.getScenery_prices().get(i));
						logger.info(i + "/" + lspi.getScenery_prices().size() + "|||Url:【" + lskj.getUrl()
								+ "】的同程景点产品门票详情获取成功|||");
					} catch (Exception e) {

					}
				}
				mysqlDao.update(LySceneryKeywordJob.class, Chain.make("ticket_status", 2),
						Cnd.where("id", "=", lskj.getId()));
				logger.info("|||Url:【" + lskj.getUrl() + "】的同程景点产品门票详情获取成功|||");
			} catch (Exception e) {
				e.printStackTrace();
				mysqlDao.update(LySceneryKeywordJob.class, Chain.make("ticket_status", 3),
						Cnd.where("id", "=", lskj.getId()));
				logger.error("LySceneryPriceTagModel插入bi失败,LySceneryKeywordJob-id[" + job.getId() + "] url["
						+ job.getKeyword() + "]", e);
			}

			// creat commentJob
			try {
				if (lspi.getCommentJob_list().size() > 0) {
					for (int i = 0; i < lspi.getCommentJob_list().size(); i++) {
						try {
							mysqlDao.insert(lspi.getCommentJob_list().get(i));
							logger.info("|||Url:【" + lskj.getUrl() + "】的同程景点产品的第"
									+ lspi.getCommentJob_list().get(i).getPage_index() + "页评论job插入成功|||");
						} catch (Exception e) {
							logger.error("Url:【" + lspi.getUrl() + "】的同程票务产品评论job已存在");
						}
					}
					mysqlDao.update(LySceneryKeywordJob.class, Chain.make("comment_status", 2),
							Cnd.where("id", "=", lskj.getId()));
					logger.info("|||Url:【" + lskj.getUrl() + "】的同程景点产品评论job插入成功|||");
				} else {
					mysqlDao.update(LySceneryKeywordJob.class, Chain.make("comment_status", 3),
							Cnd.where("id", "=", lskj.getId()));
					logger.error("Url:【" + lskj.getUrl() + "】的同程景点产品评论任务job失败");
				}
			} catch (Exception e) {
				mysqlDao.update(LySceneryKeywordJob.class, Chain.make("comment_status", 3),
						Cnd.where("id", "=", lskj.getId()));
				logger.error("LySceneryCommentJob建立失败,LySceneryKeywordJob-id[" + job.getId() + "] url["
						+ job.getKeyword() + "]", e);
			}

		} catch (Exception e) {
			mysqlDao.update(LySceneryKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", lskj.getId()));
			logger.error("【" + job.getKeyword() + "】的同程景点产品和票价信息获取失败", e);
		} finally {
			dr.close();
		}
	}

	/**
	 * 解析景点的相关内容
	 *
	 * @param html
	 * @param lskj
	 * @return
	 * @author aaron
	 * @param dr
	 */
	public LySceneryProductInfoModel getSceneryProduct(String result, LySceneryKeywordJob job, WebDriver dr) {
		/** ly景点产品实例 **/
		LySceneryProductInfoModel lspi = new LySceneryProductInfoModel();

		try {

			lspi.setKeyword(job.getKeyword());
			lspi.setRequest_id(job.getRequest_id());
			lspi.setTask_id(job.getTask_id());
			lspi.setUrl(job.getUrl());

			/** 当前时间 **/
			Date date = new Date();
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				lspi.setCreate_date(dateFormat.parse(dateFormat.format(date)));
			} catch (Exception e) {
				logger.error("url:【" + job.getUrl() + "】创建日期，建立失败");
				lspi.setCreate_date(date);
			}
			lspi.setCreate_time(date);

			Elements temps;
			Document doc = Jsoup.parse(result);

			/** 唯一身份编号 **/
			String sid = doc.select("input#hf_SceneryId").val();
			/** 唯一身份名字 **/
			String sname = doc.select("input#hf_SceneryName").val();
			/** 产品id **/
			String pid = doc.select("input#hf_SceneryId").val();// doc.select("input#commonProId").val();
			// String pid = CommonUtils.subStringJd("pid=", "&", result);
			/** 产品名字 **/
			String pname = doc.select("input#hf_SceneryName").val();
			/** 产品实例图片地址 **/
			String product_img_url = null;// **************************************

			/********************************** 产品信息 ***********************************/
			/** 优惠信息及景点推荐 **/
			String privilege = null;
			temps = doc.select("div.s-name>div.icon-box>span");
			try {
				for (Element element : temps) {
					String str = element.attr("class");
					if (str.contains("icon")) {
						privilege += (str + ",");
					}
				}
			} catch (Exception e) {
				logger.info("【" + job.getKeyword() + "】的优惠信息及景点推荐获取失败", e);
			}

			/** 景区等级 **/
			String level = null;
			/** 当前真实价格(包含促销价格) */
			String price = doc.select("div.s_price>div.s_p_t>span>b").text();
			/** 原价 **/
			String original_price = doc.select("div.s_price>span.s_p_b>s").text().replace("¥", "");
			/** 地址 */
			String address = doc.select("input#hf_Address").val();
			/** 开放时间 **/
			String open_time = doc.select("div.s-tShow").text();
			/** 服务保障 **/
			String service_assurance = doc.select("p.service_w").text();

			/******************************** 票价相关内容 *********************************/
			List<LySceneryPriceTagModel> scenery_prices = getSenceryPrice(job, date, sid, sname, pid, pname, doc);

			/********************************** 预定须知 **********************************/
			/** 预定须知 **/
			String booking_notes = doc.select("div#book_note_con").text();

			/********************************** 景点简介 **********************************/
			/** 景点简介 **/
			String info = doc.select("div#scenery_intro_con").text();

			/********************************** 评论相关 **********************************/
			/** 满意度 **/
			String satisfaction = doc.select("div.hpl_grade>span").text();
			/** 大家觉得 **/
			String feelings = doc.select("ul.impress_ul").text();
			/** 全部评论数 **/
			Integer comments = null;
			/** 好评数 **/
			Integer good_comment = null;
			/** 中评数 **/
			Integer middle_comment = null;
			/** 差评数 **/
			Integer bad_comment = null;
			/** 有图数 **/
			Integer img_comment = null;

			temps = doc.select("div#dpType>div");
			try {
				comments = Integer.parseInt(CommonUtils.subStringJd("(", ")", temps.get(0).text()));
			} catch (Exception e) {

			}
			try {
				good_comment = Integer.parseInt(CommonUtils.subStringJd("(", ")", temps.get(2).text()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				middle_comment = Integer.parseInt(CommonUtils.subStringJd("(", ")", temps.get(3).text()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				bad_comment = Integer.parseInt(CommonUtils.subStringJd("(", ")", temps.get(4).text()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				img_comment = Integer.parseInt(CommonUtils.subStringJd("(", ")", temps.get(1).text()));
			} catch (Exception e) {

			}

			/** 交通指南 **/
			String traffic_info = null;

			// // 截图
			// try {
			// SeleniumUtils.snapshotTosmall(pid, pname, mysqlDao, dr, Site.tongcheng,
			// SiteType.scenery,
			// MyConstant.LY_CONFIG.COMPRESS_RATE, MyConstant.LY_CONFIG.IMAGE_QUALITY);
			// logger.info("|||Url:【" + job.getUrl() + "】的同程景点截图成功|||");
			// } catch (Exception e) {
			// logger.error("Url:【" + job.getUrl() + "】的同程景点产品截图", e);
			// }
			// 抓取的数据set入bean
			lspi.setSid(sid);
			lspi.setSname(sname);
			lspi.setPid(pid);
			lspi.setPname(pname);
			lspi.setProduct_img_url(product_img_url);

			lspi.setPrivilege(privilege);
			lspi.setLevel(level);
			lspi.setPrice(price);
			lspi.setOriginal_price(original_price);
			lspi.setAddress(address);
			lspi.setOpen_time(open_time);
			lspi.setService_assurance(service_assurance);

			lspi.setScenery_prices(scenery_prices);

			lspi.setBooking_notes(booking_notes);

			lspi.setInfo(info);

			lspi.setSatisfaction(satisfaction);
			lspi.setFeelings(feelings);

			try {
				comments = Integer.parseInt(doc.select("div.s_com_more>p").first().ownText().replaceAll("条用户点评", ""));
			} catch (Exception e) {
				// TODO: handle exception
			}

			lspi.setComments(comments);
			lspi.setGood_comment(good_comment);
			lspi.setMiddle_comment(middle_comment);
			lspi.setBad_comment(bad_comment);
			lspi.setImg_comment(img_comment);

			lspi.setTraffic_info(traffic_info);

			// 建立评论任务
			logger.info("开始建立ly_scenery_comment的job.......");
			try {
				mysqlDao.update(LySceneryKeywordJob.class, Chain.make("comment_status", 1),
						Cnd.where("id", "=", job.getId()));

				List<LySceneryCommentJob> commentJob_list = new ArrayList<LySceneryCommentJob>();

				// String webUrl =
				// "http://tctj.ly.com/jrec/wlfrec?cid=173&rid={0}&projectId=3&pageSize=10&labId=1&page={1}";

				String webUrl = "http://www.ly.com/scenery/AjaxHelper/DianPingAjax.aspx?action=GetDianPingList&sid={0}&page={1}&pageSize=10&labId=1&sort=0";

				/**
				 * 评论总页码数 *
				 */
				Integer pages = null;

				if (comments > 0) {
					pages = (comments - 1) / 10 + 1;
				} else {
					return lspi;
				}

				for (int i = 1; i <= pages; i++) {
					LySceneryCommentJob lscj = new LySceneryCommentJob();

					String comment_url = MessageFormat.format(webUrl, sid, i + "");

					/**
					 * 当前评论的页码 *
					 */
					Integer page_index = i;

					lscj.setSid(sid);
					lscj.setSname(sname);
					lscj.setPid(pid);
					lscj.setPname(pname);
					lscj.setUrl(comment_url);
					lscj.setWeb_url(job.getUrl());

					lscj.setPage_index(page_index);
					lscj.setPages(pages);
					lscj.setComments(comments);
					lscj.setPage_name(pname);

					lscj.setKeyword(job.getKeyword());
					lscj.setRequest_id(job.getRequest_id());
					lscj.setTask_id(job.getTask_id());

					/** 当前时间 **/
					Date time = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					lscj.setCreate_date(dateFormat.format(time));
					lscj.setCreate_time(time);
					lscj.setLevel(job.getLevel());
					lscj.setNode(job.getNode());
					lscj.setProject_code(job.getProject_code());
					lscj.setDown_type(job.getDown_type());

					commentJob_list.add(lscj);
				}

				lspi.setCommentJob_list(commentJob_list);
			} catch (Exception e) {
				mysqlDao.update(LySceneryKeywordJob.class, Chain.make("comment_status", 3),
						Cnd.where("id", "=", job.getId()));
				logger.error("【" + job.getKeyword() + "】的景点评论job建立失败", e);
			}

			// System.out.println("=========================================================================");

		} catch (Exception e) {
			mysqlDao.update(LySceneryKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("【" + job.getKeyword() + "】的景点产品获取失败", e);
		}

		return lspi;
	}

	public List<LySceneryPriceTagModel> getSenceryPrice(LySceneryKeywordJob job, Date date, String sid, String sname,
			String pid, String pname, Document doc) {

		List<LySceneryPriceTagModel> lspts = null;

		try {

			lspts = new ArrayList<LySceneryPriceTagModel>();
			// 票价类型的div
			Elements api_item = doc.select("div.api_item>div.api_channel");
			for (Element api_channel : api_item) {

				/** 票的类型 **/
				String ticket_type = api_channel.select("div.api_item_name>b").text();
				try {
					// 每张票的div
					Elements api_item_content = api_channel.select("div.api_item_content>div.api_line");
					for (Element api_line : api_item_content) {
						/************************ 票的具体信息 ***********************/

						/** 票的名称 **/
						String ticket_name = api_line.select("dt.W01").text();
						/** 可定时间 **/
						String order_time = api_line.select("dd.W03").text();
						/** 同程价 **/
						String price = null;
						try {
							price = api_line.select("dd.W04>span").first().ownText();
						} catch (Exception e) {

						}
						/** 原价 **/
						String original_price = api_line.select("dd.W04>s").text();
						/** 优惠信息 **/
						String privilege = api_line.select("dd.W05").text();
						/** 预定 **/
						String order_btn = api_line.select("dd.W06").text();

						String str = api_line.select("dd.W06>a").attr("href");

						/** 票的id **/
						String ticket_pid = null;
						try {
							ticket_pid = CommonUtils.subStringJd("ticketId=", ".", str + ".");
						} catch (Exception e) {

						}
						/** 预定票的url **/
						String ticket_url = "http://www.ly.com" + str;

						// 票的其他说明
						/** 入园方式 **/
						String in_park_style = null;
						/** 预定时间 **/
						String reserve_time = null;
						/** 包含项目 **/
						String contain_project = null;
						/** 预定说明 **/
						String reserve_introduction = null;
						/** 发票说明 **/
						String invoice_introduction = null;
						/** 退改规则 **/
						String back_change_rules = null;

						Elements li = api_line.select("div.remark>ul>li");
						try {
							in_park_style = li.get(0).text();
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							reserve_time = li.get(1).text();
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							contain_project = li.get(2).text();
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							reserve_introduction = li.get(3).text();
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							invoice_introduction = li.get(4).text();
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							back_change_rules = li.get(5).text();
						} catch (Exception e) {
							// TODO: handle exception
						}

						/** 每种票价信息实例 **/
						LySceneryPriceTagModel lspt = new LySceneryPriceTagModel();

						lspt.setKeyword(job.getKeyword());
						lspt.setRequest_id(job.getRequest_id());
						lspt.setTask_id(job.getTask_id());
						lspt.setUrl(job.getUrl());
						lspt.setSid(sid);
						lspt.setSname(sname);
						lspt.setPid(pid);
						lspt.setPname(pname);
						/** 当前时间 **/
						try {
							DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
							lspt.setCreate_date(dateFormat.parse(dateFormat.format(date)));
						} catch (Exception e) {
							logger.error("url:【" + job.getUrl() + "】创建日期，建立失败");
							lspt.setCreate_date(date);
						}
						lspt.setCreate_time(date);

						lspt.setTicket_type(ticket_type);
						lspt.setTicket_name(ticket_name);
						lspt.setOrder_time(order_time);
						lspt.setPrice(price);
						lspt.setOriginal_price(original_price);
						lspt.setPrivilege(privilege);
						lspt.setOrder_btn(order_btn);

						lspt.setIn_park_style(in_park_style);
						lspt.setReserve_time(reserve_time);
						lspt.setContain_project(contain_project);
						lspt.setReserve_introduction(reserve_introduction);
						lspt.setInvoice_introduction(invoice_introduction);
						lspt.setBack_change_rules(back_change_rules);
						lspt.setTicket_pid(ticket_pid);
						lspt.setTicket_url(ticket_url);

						lspts.add(lspt);

					}
				} catch (Exception e) {
					logger.error("【" + job.getKeyword() + "】的景点票价类型的div获取失败", e);
				}

			}

		} catch (Exception e) {
			logger.error("【" + job.getKeyword() + "】的景点票价信息解析获取失败", e);
		}

		return lspts;
	}
}
