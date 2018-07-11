package com.reptile.ly.task.ticket.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.ly.model.ticket.LyTicketPriceTagModel;
import com.reptile.ly.model.ticket.LyTicketProductInfoModel;
import com.reptile.ly.model.ticket.job.LyTicketCommentJob;
import com.reptile.ly.model.ticket.job.LyTicketKeywordJob;
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
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 同程门票-根据url(关键字)获取commentJob和获取酒店info,同程票务每种票的信息priceTag
 * @author zhour
 *
 */
public class LyTicketKeywordThread extends Thread {
	/**
	 * 日志变量 *
	 */
	private static Logger logger = Logger.getLogger(LyTicketKeywordThread.class);
	// dao层变量
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(Constant.ZNode);
	// NumysqlDao mysqlDao = new NumysqlDao();

	private LyTicketKeywordJob job;

	public LyTicketKeywordThread(Record task) {
		this.job = task.toPojo(LyTicketKeywordJob.class);
	}

	public void run() {
		try {
			mysqlDao.update(LyTicketKeywordJob.class, Chain.make("status", 1), Cnd.where("id", "=", job.getId()));
			this.parse();
		} catch (Exception e) {
			mysqlDao.update(LyTicketKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("解析ly_ticket异常：关键词：" + job.getKeyword(), e);
		}
	}

	public void parse() {

		LyTicketKeywordJob ltkj = job;
		TaskToBi tobi = BIDataSource.getBiDataSource("ly", job.getProject_code(), job.getDown_type());
		Dao biDao = tobi.getBiDao();
		/** url **/
		String url = ltkj.getUrl();
		WebDriver dr = HttpBase.getFirefoxDriver();

		try {

			dr.get(url);
			// JavascriptExecutor js = (JavascriptExecutor) dr;
			// js.executeScript("scrollTo(0,6000)");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// ***************
			}

			String html = dr.getPageSource();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// *****************
			}

			LyTicketProductInfoModel ltpi = getTicketProduct(html, ltkj, dr);

			// insert profuct-info
			try {
				biDao.insert(ltpi);
				mysqlDao.update(LyTicketKeywordJob.class, Chain.make("status", 2), Cnd.where("id", "=", ltkj.getId()));
				logger.info("|||Url:【" + ltkj.getUrl() + "】的同程票务产品详情获取成功|||");
			} catch (Exception e) {
				mysqlDao.update(LyTicketKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", ltkj.getId()));
				logger.error("LyTicketProductInfoModel插入bi失败,LyTicketKeywordJob-id[" + job.getId() + "] url["
						+ job.getKeyword() + "]", e);
			}

			// insert price-info
			try {
				for (int i = 0; i < ltpi.getTickets().size(); i++) {
					try {
						biDao.insert(ltpi.getTickets().get(i));
						logger.info(i + "/" + ltpi.getTickets().size() + "|||Url:【" + ltkj.getUrl() + "】的同程票价插入成功||||");
					} catch (Exception e) {

					}

				}
				mysqlDao.update(LyTicketKeywordJob.class, Chain.make("ticket_status", 2),
						Cnd.where("id", "=", ltkj.getId()));
				logger.info("|||Url:【" + ltkj.getUrl() + "】的同程票务产品种类详情获取成功|||");
			} catch (Exception e) {
				mysqlDao.update(LyTicketKeywordJob.class, Chain.make("ticket_status", 3),
						Cnd.where("id", "=", ltkj.getId()));
				logger.error("LyTicketPriceTagModel插入bi失败,LyTicketKeywordJob-id[" + job.getId() + "] url["
						+ job.getKeyword() + "]", e);
			}

			// creat CommentJob
			try {
				if (ltpi.getCommentJobs().size() > 0) {
					for (int i = 0; i < ltpi.getCommentJobs().size(); i++) {
						try {
							mysqlDao.insert(ltpi.getCommentJobs().get(i));
							logger.info(i + "/" + ltpi.getCommentJobs().size() + "|||Url:【" + ltkj.getUrl()
									+ "】的同程票务产品评论job插入成功|||");
						} catch (Exception e) {
							logger.error("Url:【" + ltkj.getUrl() + "】的同程票务产品评论job建立失败");
						}
					}
				} else {
					mysqlDao.update(LyTicketKeywordJob.class, Chain.make("comment_status", 3),
							Cnd.where("id", "=", ltkj.getId()));
					logger.error("Url:【" + ltkj.getUrl() + "】的同程票务产品评论job建立失败");
				}
				mysqlDao.update(LyTicketKeywordJob.class, Chain.make("comment_status", 2),
						Cnd.where("id", "=", ltkj.getId()));
			} catch (Exception e) {
				mysqlDao.update(LyTicketKeywordJob.class, Chain.make("comment_status", 3),
						Cnd.where("id", "=", ltkj.getId()));
				logger.error("LyTicketCommentJob建立失败,LyTicketKeywordJob-id[" + job.getId() + "] url[" + job.getKeyword()
						+ "]", e);
			}

		} catch (Exception e) {
			mysqlDao.update(LyTicketKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", ltkj.getId()));
			logger.error("【" + job.getKeyword() + "】的同程票务产品和票价信息获取失败", e);
		} finally {
			dr.close();
			dr.quit();
		}

	}

	/**
	 * 解析票务的相关内容
	 *
	 * @param result
	 * @param job
	 * @return
	 * @author aaron
	 * @param dr
	 */
	public LyTicketProductInfoModel getTicketProduct(String result, LyTicketKeywordJob job, WebDriver dr) {

		logger.info("开始解析ly_ticket_praduct的内容.......");

		/** lv票务产品实例 **/
		LyTicketProductInfoModel ltpi = new LyTicketProductInfoModel();

		try {

			ltpi.setKeyword(job.getKeyword());
			ltpi.setRequest_id(job.getRequest_id());
			ltpi.setTask_id(job.getTask_id());
			ltpi.setUrl(job.getUrl());

			/** 当前时间 **/
			Date date = new Date();
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				ltpi.setCreate_date(dateFormat.parse(dateFormat.format(date)));
			} catch (Exception e) {
				logger.error("url:【" + job.getUrl() + "】创建日期，建立失败");
				ltpi.setCreate_date(date);
			}
			ltpi.setCreate_time(date);

			Elements temps;
			Element temp;
			Document doc = Jsoup.parse(result);

			/** 唯一身份编号 **/
			String sid = doc.select("input#resourceId").val();
			/** 唯一身份名字 **/
			String sname = doc.select("div.ly_info>h1").text();
			/** 产品id **/
			String pid = doc.select("input#commonProId").val();
			/** 产品名字 **/
			String pname = doc.select("div.ly_info>h1").text();

			/** 电话 **/
			String phone = null;

			temps = doc.select("ul.rate_star>li.current");
			/** 星评 **/
			String stars = temps.size() + "";
			/** 星评分 **/
			String rating = doc.select("span.rate_tag").text().replace("分", "");
			/** 昨日评论数 **/
			String commentsYestoday = null;
			try {
				commentsYestoday = doc.select("ul.info_items>li>a").first().text().replace("条点评", "");
			} catch (Exception e) {
				// TODO: handle exception
			}

			/** 月销量 **/
			String sales = doc.select("li.items_right>span>em").text();
			/** 地址 **/
			String address = doc.select("li.scenery_address>b").text();
			/** 价格 **/
			String price = doc.select("div.info_opera>span>b").text();
			/** 推荐理由 **/
			String recommendReason = doc.select("div.info_reason").text();

			/************************ 票价信息 ************************/
			List<LyTicketPriceTagModel> tickets = getTicketPrice(job, date, sid, sname, pid, pname, doc);

			/************************ 特色体验 ************************/
			/** 文字部分 **/
			String featureExperience = doc.select("div#lineInfo").text();
			if (featureExperience.length()>7500) {
				featureExperience=featureExperience.substring(0, 7500);
			}
			/** 图片地址 **/
			String featureImgUrl = null;

			/************************ 预定须知 ************************/
			String bookingNotes = doc.select("div.order_detail").text();

			/************************ 评论得分 ************************/
			/** 满意度 **/
			String satisfaction = doc.select("div.comm-satis>p.satis-num").text();
			/** 综合评分 **/
			String score = doc.select("div.comm-score>p.satis-num").text().replace("分", "");

			temps = doc.select("div.comm-kinds>p");
			/** 服务评分 **/
			String serviceScore = null;
			/** 便捷评分 **/
			String convenientScore = null;
			/** 性价比 **/
			String costPerformance = null;
			try {
				temp = temps.get(0);
				try {
					serviceScore = temp.select("em").get(0).text();
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					convenientScore = temp.select("em").get(1).text();
				} catch (Exception e) {
					// TODO: handle exception
				}

				temp = temps.get(1);
				try {
					costPerformance = temp.select("em").get(1).text();
				} catch (Exception e) {
					// TODO: handle exception
				}

			} catch (Exception e) {
				logger.info("【" + job.getKeyword() + "】的服务评分,便捷评分,性价比获取失败", e);
			}

			/************************* 评论相关 ***********************/
			temps = doc.select("div#J_CometTab>span>label>b");
			/** 全部评论数 **/
			Integer comments = null;
			/** 好评数 **/
			Integer goodComment = null;
			/** 中评数 **/
			Integer middleComment = null;
			/** 差评数 **/
			Integer badComment = null;
			/** 有图评论数 **/
			Integer imgComment = null;
			try {
				comments = Integer.parseInt(CommonUtils.drawNum(temps.get(0).text()));
			} catch (Exception e) {

			}
			try {
				goodComment = Integer.parseInt(CommonUtils.drawNum(temps.get(1).text()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				middleComment = Integer.parseInt(CommonUtils.drawNum(temps.get(2).text()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				badComment = Integer.parseInt(CommonUtils.drawNum(temps.get(3).text()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				imgComment = Integer.parseInt(CommonUtils.drawNum(temps.get(4).text()));
			} catch (Exception e) {
				// TODO: handle exception
			}

//			// 截图
//			try {
//				SeleniumUtils.snapshotTosmall(pid, pname, mysqlDao, dr, Site.tongcheng, SiteType.ticket,
//						MyConstant.LY_CONFIG.COMPRESS_RATE, MyConstant.LY_CONFIG.IMAGE_QUALITY);
//				logger.info("|||||||||||||||||||||Url:【" + job.getUrl() + "】的同程票务截图成功|||||||||||||||||||||");
//			} catch (Exception e) {
//				logger.error("Url:【" + job.getUrl() + "】的同程票务产品截图", e);
//			}

			// 抓取的数据set入bean
			ltpi.setSid(sid);
			ltpi.setSname(sname);
			ltpi.setPid(pid);
			ltpi.setPname(pname);
			ltpi.setPhone(phone);
			ltpi.setStars(stars);
			ltpi.setRating(rating);
			ltpi.setComments_yestoday(commentsYestoday);
			ltpi.setSales(sales);
			ltpi.setAddress(address);
			ltpi.setPrice(price);
			ltpi.setRecommend_reason(recommendReason);

			ltpi.setTickets(tickets);

			ltpi.setFeature_experience(featureExperience);
			ltpi.setFeature_img_url(featureImgUrl);
			ltpi.setBooking_notes(bookingNotes);
			ltpi.setSatisfaction(satisfaction);
			ltpi.setScore(score);
			ltpi.setService_score(serviceScore);
			ltpi.setConvenient_score(convenientScore);
			ltpi.setCost_performance(costPerformance);
			ltpi.setComments(comments);
			ltpi.setGood_comment(goodComment);
			ltpi.setMiddle_comment(middleComment);
			ltpi.setBad_comment(badComment);
			ltpi.setImg_comment(imgComment);

			// 建立评论任务
			logger.info("开始建立ly_ticket_comment的job.......");
			try {
				mysqlDao.update(LyTicketKeywordJob.class, Chain.make("comment_status", 1),
						Cnd.where("id", "=", job.getId()));

				List<LyTicketCommentJob> commentJobs = new ArrayList<LyTicketCommentJob>();
				/**
				 * 评论总页码数 *
				 */
				Integer pages = null;

				String webUrl = "https://www.ly.com/wanle/api/WanleProduct/GetCommentInfo?&lineId={0}&pageSize=5&isSingle=1&pageIndex={1}";
				//String webUrl1= "";
				if (comments > 0) {
					pages = (comments - 1) / 5 + 1;
				} else {
					return ltpi;
				}

				for (int i = 1; i <= pages; i++) {

					LyTicketCommentJob ltcj = new LyTicketCommentJob();
					/**
					 * url地址 *
					 */
					String comment_url = MessageFormat.format(webUrl, sid, i + "");
					/**
					 * 当前评论的页码 *
					 */
					Integer page_index = i;
					ltcj.setSid(sid);
					ltcj.setSname(sname);
					ltcj.setPid(pid);
					ltcj.setPname(pname);
					ltcj.setUrl(comment_url);
					ltcj.setWeb_url(job.getUrl());

					ltcj.setPage_index(page_index);
					ltcj.setPages(pages);
					ltcj.setComments(comments);
					ltcj.setPage_name(pname);

					ltcj.setKeyword(job.getKeyword());
					ltcj.setRequest_id(job.getRequest_id());
					ltcj.setTask_id(job.getTask_id());

					/** 当前时间 **/
					Date time = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					ltcj.setCreate_date(dateFormat.format(time));
					ltcj.setCreate_time(time);
					ltcj.setLevel(job.getLevel());
					ltcj.setNode(job.getNode());
					ltcj.setProject_code(job.getProject_code());
					ltcj.setDown_type(job.getDown_type());

					commentJobs.add(ltcj);
				}

				ltpi.setCommentJobs(commentJobs);
			} catch (Exception e) {
				mysqlDao.update(LyTicketKeywordJob.class, Chain.make("comment_status", 3),
						Cnd.where("id", "=", job.getId()));
				logger.error("【" + job.getKeyword() + "】的票务评论job建立失败", e);
			}

		} catch (Exception e) {
			mysqlDao.update(LyTicketKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("【" + job.getKeyword() + "】的票务产品获取失败", e);
		}

		return ltpi;
	}

	/**
	 * 解析票价具体内容
	 *
	 * @param job
	 * @param createTime
	 * @param sid
	 * @param sname
	 * @param pid
	 * @param pname
	 * @param doc
	 * @return
	 * @author aaron
	 */
	public List<LyTicketPriceTagModel> getTicketPrice(LyTicketKeywordJob job, Date createTime, String sid, String sname,
			String pid, String pname, Document doc) {

		List<LyTicketPriceTagModel> tickets = new ArrayList<LyTicketPriceTagModel>();

		try {
			Elements book_trs = doc.select("div.book_module_0>div.book_bd>div.book_tr");
			for (Element book_tr : book_trs) {
				/** 票的类型 **/
				String ticketType = book_tr.select("div.book_label").text();

				Elements book_lis = book_tr.select("div.book_ul>div.book_li");
				for (Element book_li : book_lis) {
					/** 票的名字 **/
					String ticketName = book_li.select("dt.td1").text();

					/** 有效期 **/
					String validity_period = null;
					DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
					DateFormat dateFormat2 = new SimpleDateFormat("yy年MM月dd日");
					DateFormat dateFormat3 = new SimpleDateFormat("MM月dd日");
					DateFormat dateFormat4 = new SimpleDateFormat("yy年MM月dd号");
					DateFormat dateFormat5 = new SimpleDateFormat("yyyy.MM.dd");

					if (ticketName.contains("(")) {
						if (ticketName.contains("有效期至")) {
							try {
								validity_period = CommonUtils.subStringJd("(有效期至", ")", ticketName);
								validity_period = dateFormat1.format(dateFormat2.parse(validity_period));
							} catch (Exception e) {
								try {
									validity_period = dateFormat1.format(dateFormat4.parse(validity_period));
								} catch (Exception e2) {
									// TODO: handle exception
								}
							}
						}

						if (ticketName.contains("有效期到")) {

							try {
								validity_period = CommonUtils.subStringJd("(有效期到", ")", ticketName);
								validity_period = dateFormat1.format(dateFormat2.parse(validity_period));
							} catch (Exception e) {

							}
						}

						if (ticketName.contains("电子换票证有效期到")) {
							try {
								validity_period = CommonUtils.subStringJd("(电子换票证有效期到", ")", ticketName);
								validity_period = dateFormat1.format(dateFormat2.parse(validity_period));
							} catch (Exception e) {

							}
						}

						if (ticketName.contains("有效入园日期")) {
							try {
								validity_period = CommonUtils.subStringJd("(有效入园日期至", ")", ticketName);
								validity_period = dateFormat1.format(dateFormat5.parse(validity_period));
							} catch (Exception e) {

							}
						}

						if (ticketName.contains("有效期为")) {
							try {
								validity_period = CommonUtils.subStringJd("(有效期为", ")", ticketName);
								validity_period = dateFormat1.format(dateFormat2.parse(validity_period));
							} catch (Exception e) {

							}
						}

					} else if (ticketName.contains("（")) {

						if (ticketName.contains("有效期至")) {
							try {
								validity_period = CommonUtils.subStringJd("（有效期至", "）", ticketName);
								validity_period = dateFormat1.format(dateFormat2.parse(validity_period));
							} catch (Exception e) {
								try {
									validity_period = CommonUtils.subStringJd("（有效期至", "）", ticketName);
									validity_period = dateFormat1.format(dateFormat4.parse(validity_period));
								} catch (Exception e2) {
									// TODO: handle exception
								}
							}
						}

						if (ticketName.contains("有效期到")) {
							try {
								validity_period = CommonUtils.subStringJd("（有效期到", "）", ticketName);
								validity_period = dateFormat1.format(dateFormat2.parse(validity_period));
							} catch (Exception e) {

							}
						}

						if (ticketName.contains("电子换票证有效期到")) {
							try {
								validity_period = CommonUtils.subStringJd("（电子换票证有效期到", "）", ticketName);
								validity_period = dateFormat1.format(dateFormat2.parse(validity_period));
							} catch (Exception e) {

							}
						}

						if (ticketName.contains("有效入园日期")) {
							try {
								validity_period = CommonUtils.subStringJd("（有效入园日期至", "）", ticketName);
								validity_period = dateFormat1.format(dateFormat5.parse(validity_period));
							} catch (Exception e) {

							}
						}

						if (ticketName.contains("有效期为")) {
							try {
								validity_period = CommonUtils.subStringJd("（有效期为", "）", ticketName);
								validity_period = dateFormat1.format(dateFormat2.parse(validity_period));
							} catch (Exception e) {

							}
						}

					}

					/** 可定时间 **/
					String canOrderTime = book_li.select("dd.td2").text();
					/** 入园方式 **/
					String inParkStyle = null;
					// String inParkStyle = book_li.select("dd.td_func").text();
					/** 原价 **/
					String OriginalPrice = null;
					try {
						OriginalPrice = CommonUtils.subStringJd("￥", ",", book_li.select("span.mkprice").text() + ",");
					} catch (Exception e) {
						// TODO: handle exception
					}
					/** 同程价 **/
					String price = null;
					try {
						price = book_li.select("span.tcprice").first().ownText();
					} catch (Exception e) {
						// TODO: handle exception
					}

					/** 预定 **/
					String orderBtn = book_li.select("dd.td5").text();

					Elements book_info = book_li.select("ul.book_info>li");
					/** 预定时间 **/
					String reserveTime = null;
					/** 预定说明 **/
					String reserveIntroduction = null;
					try {
						reserveTime = book_info.get(0).text();
					} catch (Exception e) {

					}
					try {
						reserveIntroduction = book_info.get(1).text();
					} catch (Exception e) {
						// TODO: handle exception
					}

					String str = book_li.select("dd.td5>a").attr("href");

					/** 票的id **/
					String ticket_pid = null;
					try {
						ticket_pid = CommonUtils.subStringJd("productId=", "&", str);
					} catch (Exception e) {
						// TODO: handle exception
					}
					/** 预定票的url **/
					String ticket_url = "http://www.ly.com" + str;

					/** 每种票价信息实例 **/
					LyTicketPriceTagModel ltpt = new LyTicketPriceTagModel();

					ltpt.setKeyword(job.getKeyword());
					ltpt.setRequest_id(job.getRequest_id());
					ltpt.setTask_id(job.getTask_id());
					ltpt.setUrl(job.getUrl());
					// ltpt.setSid(sid);
					// ltpt.setSname(sname);
					ltpt.setPid(pid);
					ltpt.setPname(pname);
					ltpt.setValidity_period(validity_period);

					/** 当前时间 **/
					Date date = new Date();
					try {
						DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
						ltpt.setCreate_date(dateFormat.parse(dateFormat.format(date)));
					} catch (Exception e) {
						logger.error("url:【" + job.getUrl() + "】创建日期，建立失败");
						ltpt.setCreate_date(date);
					}
					ltpt.setCreate_time(date);

					// 抓取的数据set入bean
					ltpt.setTicket_type(ticketType);
					ltpt.setTicket_name(ticketName);
					ltpt.setCan_order_time(canOrderTime);
					ltpt.setIn_park_style(inParkStyle);
					ltpt.setOriginal_price(OriginalPrice);
					ltpt.setPrice(price);
					ltpt.setOrder_btn(orderBtn);
					ltpt.setReserve_time(reserveTime);
					ltpt.setReserve_introduction(reserveIntroduction);
					ltpt.setTicket_pid(ticket_pid);
					ltpt.setTicket_url(ticket_url);
					// 添加进票价列表
					tickets.add(ltpt);
				}
			}
		} catch (Exception e) {
			logger.error("【" + job.getKeyword() + "】的海外玩乐票价信息解析获取失败", e);
		}

		return tickets;
	}
}
