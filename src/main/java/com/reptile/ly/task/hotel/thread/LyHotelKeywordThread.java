package com.reptile.ly.task.hotel.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.Constant;
import com.reptile.ly.model.hotel.LyHotelCommentTagModel;
import com.reptile.ly.model.hotel.LyHotelProductInfoModel;
import com.reptile.ly.model.hotel.LyHotelRoomTagModel;
import com.reptile.ly.model.hotel.job.LyHotelKeywordJob;
import com.reptile.tianya.common.CommonUtils;
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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 同程酒店-根据关键字获取酒店info和comment
 * @author zhour
 */
public class LyHotelKeywordThread extends Thread {

	private static Logger logger = Logger.getLogger(LyHotelKeywordThread.class);

	// dao层变量
	private Dao mysqlDao = MySqlDataSource.getMysqlDao(Constant.ZNode);

	private LyHotelKeywordJob job;

	public LyHotelKeywordThread(Record task) {
		this.job = task.toPojo(LyHotelKeywordJob.class);
	}

	public void run() {
		try {
			mysqlDao.update(LyHotelKeywordJob.class, Chain.make("status", 1), Cnd.where("id", "=", job.getId()));
			this.parse();
		} catch (Exception e) {
			mysqlDao.update(LyHotelKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("解析ly_hotel异常：关键词：" + job.getKeyword(), e);
		}
	}

	public void parse() {
		LyHotelKeywordJob lhkj = job;

		TaskToBi tobi = BIDataSource.getBiDataSource("ly", job.getProject_code(), job.getDown_type());
		// TaskToBi tobi = BIDataSource.getBiDataSource("test189", "test",
		// "test");
		Dao biDao = tobi.getBiDao();

		String url = lhkj.getUrl();
		WebDriver dr = HttpBase.getFirefoxDriver();
//		System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
//    	WebDriver dr = new ChromeDriver();

		try {
			dr.get(url);
			JavascriptExecutor js = (JavascriptExecutor) dr;
			js.executeScript("scrollTo(0,10000)");
			/** 线程睡眠以便于抓取内容，8000也可 **/
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				// *********************
			}

			String html = dr.getPageSource();

			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				// **********************
			}

			LyHotelProductInfoModel lhpi = getHotelProduct(html, lhkj, dr);

			try {
				biDao.insert(lhpi);
				mysqlDao.update(LyHotelKeywordJob.class, Chain.make("status", 2), Cnd.where("id", "=", lhkj.getId()));
				logger.info("|||Url:【" + lhkj.getUrl() + "】的同程酒店产品详情获取成功|||");
			} catch (Exception e) {
				mysqlDao.update(LyHotelKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", lhkj.getId()));
				logger.info("Url:【" + lhkj.getUrl() + "】的同程酒店产品详情获取失败", e);
			}

			// 插入房间价格
			try {
				for (int i = 0; i < lhpi.getRoomPrices().size(); i++) {
					try {
						biDao.insert(lhpi.getRoomPrices().get(i));
						logger.info(i + "/" + lhpi.getRoomPrices().size() + "|||Url:【" + lhkj.getUrl()
								+ "】的同程酒店产品房间插入成功|||");
					} catch (Exception e) {
						logger.error(
								"Url:【" + lhkj.getUrl() + "】的同程酒店产品房间类型：" + lhpi.getRoomPrices().get(i).getRoom_type()
										+ "房间名：" + lhpi.getRoomPrices().get(i).getPrivilege() + "插入失败");
					}
				}
				mysqlDao.update(LyHotelKeywordJob.class, Chain.make("ticket_status", 2),
						Cnd.where("id", "=", lhkj.getId()));
				logger.info("|||Url:【" + lhkj.getUrl() + "】的同程酒店产品房间详情获取成功|||");
			} catch (Exception e) {
				mysqlDao.update(LyHotelKeywordJob.class, Chain.make("ticket_status", 3),
						Cnd.where("id", "=", lhkj.getId()));
				logger.error("LyHotelPriceTagModel插入bi失败,LyHotelKeywordJob-id[" + job.getId() + "] url[" + lhkj.getUrl()
						+ "]", e);
			}

		} catch (Exception e) {
			mysqlDao.update(LyHotelKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", lhkj.getId()));
			logger.error("【" + job.getKeyword() + "】的同程酒店产品和票价信息获取失败", e);
		} finally {
			dr.close();
		}

	}

	/**
	 * 解析酒店产品的内容
	 * 
	 * @author aaron
	 * @param dr
	 *
	 * @param html
	 * @param lhkj
	 * @return
	 */
	public LyHotelProductInfoModel getHotelProduct(String result, LyHotelKeywordJob job, WebDriver dr) {
		LyHotelProductInfoModel lhpi = new LyHotelProductInfoModel();
		TaskToBi tobi = BIDataSource.getBiDataSource("ly", job.getProject_code(), job.getDown_type());
		Dao biDao = tobi.getBiDao();
		try {

			lhpi.setKeyword(job.getKeyword());
			lhpi.setRequest_id(job.getRequest_id());

			lhpi.setUrl(job.getUrl());
			/** 当前时间 **/
			Date date = new Date();
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				lhpi.setCreate_date(dateFormat.parse(dateFormat.format(date)));
			} catch (Exception e) {
				logger.error("Url:【" + job.getUrl() + "】创建日期，建立失败");
				lhpi.setCreate_date(date);
			}

			lhpi.setCreate_time(date);

			Elements temps;
			Document doc = Jsoup.parse(result);

			/** 产品id **/
			String pid = CommonUtils.drawNum(job.getUrl());
			/** 唯一身份编号 **/
			String sid = pid;

			/** 产品名 **/
			String pname = doc.select("div.title").first().ownText();
			/** 唯一身份名字 **/
			String sname = pname;

//			// 截图
//			try {
//				SeleniumUtils.snapshotTosmall(pid, pname, mysqlDao, dr, Site.tongcheng, SiteType.scenery,
//						MyConstant.LY_CONFIG.COMPRESS_RATE, MyConstant.LY_CONFIG.IMAGE_QUALITY);
//				logger.info("|||Url:【" + job.getUrl() + "】的同程酒店截图成功|||");
//			} catch (Exception e) {
//				logger.error("Url:【" + job.getUrl() + "】的阿里去啊景点产品截图", e);
//			}

			/** 地址 */
			String address = doc.select("div.address").text();
			/** 酒店设施简单说明 **/
			String hotel_facility_simple = null;
			/** 酒店等级 **/
			String level = doc.select("div.title>span").text();

			temps = doc.select("div.hotel-title-right");
			/** 当前真实价格(包含促销价格) */
			String price = null;

			/** 产品实例图片地址 **/
			String product_img_url = "";

			/************************ 酒店房间 **********************/
			List<LyHotelRoomTagModel> roomPrices = getRoomPrice(sid, sname, pid, pname, date, job, doc);

			/************************ 酒店详情 **********************/
			/** 电话 **/
			String phone="";
			try {
				phone = doc.select("span.contract-phone-wrapper").select("span.span2").first().text().replaceAll("\\D", "");
				/** 酒店设施 **/
			} catch (Exception e) {
				phone="";
			}
			//phone = doc.select("span.contract-phone-wrapper").select("span.span2").first().text().replaceAll("\\D", "");
			/** 酒店设施 **/
			String hotel_facilities = doc.select("div.hotel-facility").text();
			/** 酒店简介 **/
			//String hotel_info = doc.select("p.hotel-introduce").text();
			String hotel_info = doc.select("div.hotel-introduce").text();
			/** 酒店政策 **/
			String hotel_policy = null;
			// doc.select("div#portal-block-524808668667").text();
			// temps = doc.select("div.credit-card>span");
			// for (Element element : temps) {
			// hotel_policy += element.attr("class") + " ";
			// }

			/** 周边设施 **/
			String around_facilities = null;

			/************************ 评论得分 ***********************/
			/** 分数 */
			String score = doc.select("div.comments-score>div.total>div.score>p.up>strong").text();
			/** 满意度 **/
			String satisfaction = doc.select("div.comments-score>div.total>div.percent>p.up").text();
			/** 周边环境评分 **/
			String around_facilities_score = doc.select("div.score-list>div.environment>p.up>span").text();
			/** 酒店服务评分 **/
			String service_score = doc.select("div.score-list>div.server>p.up>span").text();
			/** 酒店设施评分 **/
			String facility_score = doc.select("div.score-list>div.installations>p.up>span").text();
			/** 卫生评分 **/
			String health_score = doc.select("div.score-list>div.health>p.up>span").text();

			/************************* 评论数相关 **************************/

			/** 全部评论数 **/
			Integer comments = null;
			/** 好评数 **/
			Integer good_comment = null;
			/** 中评数 **/
			Integer middle_comment = null;
			/** 差评数 **/
			Integer bad_comment = null;
			temps = doc.select("ul.comments-tab-nav");
			try {
				comments = Integer.parseInt(temps.select("span.all").text());
			} catch (Exception e) {

			}
			try {
				good_comment = Integer.parseInt(temps.select("span.good").text());
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				middle_comment = Integer.parseInt(temps.select("span.moderate").text());
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				bad_comment = Integer.parseInt(temps.select("span.negative").text());
			} catch (Exception e) {
				// TODO: handle exception
			}

			// 抓取的数据set入bean
			lhpi.setSid(sid);
			lhpi.setSname(sname);
			lhpi.setPid(pid);
			lhpi.setPname(pname);
			lhpi.setLevel(level);
			lhpi.setHotel_facility_simple(hotel_facility_simple);
			lhpi.setAddress(address);
			lhpi.setPrice(price);
			lhpi.setPhone(phone);
			lhpi.setProduct_img_url(product_img_url);

			lhpi.setRoomPrices(roomPrices);

			lhpi.setHotel_info(hotel_info);
			lhpi.setHotel_policy(hotel_policy);
			lhpi.setHotel_facilities(hotel_facilities);
			lhpi.setAround_facilities(around_facilities);

			lhpi.setScore(score);
			lhpi.setSatisfaction(satisfaction);
			lhpi.setAround_facilities_score(around_facilities_score);
			lhpi.setService_score(service_score);
			lhpi.setFacility_score(facility_score);
			lhpi.setHealth_score(health_score);

			lhpi.setComments(comments);
			lhpi.setGood_comment(good_comment);
			lhpi.setMiddle_comment(middle_comment);
			lhpi.setBad_comment(bad_comment);

			/***************************** 建立评论任务 *************************/
			logger.info("开始建立ly_hotel_comment的job.......");
			try {
				mysqlDao.update(LyHotelKeywordJob.class, Chain.make("comment_status", 1),
						Cnd.where("id", "=", job.getId()));

				WebElement webEle = null;
				int pageIndex = 0;
				do {
					System.out.println("是否含有comments-list:" + doc.select("ul.comments-list li").isEmpty());
					Elements ul = doc.select("ul.comments-list li");

					for (Element li : ul) {
						String nick = "";
						String vip = "";
						String fangType = "";
						String travelType = "";
						String img_path = "";
						Elements dd = li.select("dl.comments-author dd");
						for (Element d : dd) {
							if (d.hasClass("name")) {
								nick = d.text();
							} else {
								vip = d.attr("class");
							}
						}

						String content = li.select("div.comments-details div.comments-body p").text();
						String time = li.select("div.comments-details div.top div.time").text();
						String recommend = li.select("div.comments-details div.top h3.recommend").text();
						String record = li.select("div.comments-details p.record").text();

						Elements types = li.select("div.comments-details div.top div.add-info span");
						Elements images = li.select("div.comments-details div.comments-img img");

						for (Element img : images) {
							img_path = img_path + img.attr("src") + ";";
						}

						if (StringUtils.isNotEmpty(time)) {
							time = time.replaceAll("发表时间：", "");
						}

						Date now = new Date();
						DateFormat c_format = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Calendar rightNow = Calendar.getInstance();
							rightNow.setTime(now);

							if (time.contains("今天")) {
								time = c_format.format(rightNow.getTime());
							} else if (time.contains("昨天")) {
								rightNow.add(Calendar.DAY_OF_YEAR, -1);
								time = c_format.format(rightNow.getTime());
							} else if (time.contains("前天")) {
								rightNow.add(Calendar.DAY_OF_YEAR, -2);
								time = c_format.format(rightNow.getTime());
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

						LyHotelCommentTagModel lhctm = new LyHotelCommentTagModel();
						String comment_id = CommonUtils.md5(lhpi.getPid() + "_" + content + nick + time);
						lhctm.setSid(lhpi.getSid());
						lhctm.setSname(lhpi.getSname());
						lhctm.setPid(lhpi.getPid());
						lhctm.setPname(lhpi.getPname());
						lhctm.setKeyword(lhpi.getKeyword());

						lhctm.setRequest_id(lhpi.getRequest_id());
						lhctm.setComment_id(comment_id);

						lhctm.setPage_index(pageIndex);
						lhctm.setUrl(job.getUrl());
						lhctm.setNid(nick);
						lhctm.setNick(nick);
						lhctm.setGrade(vip);
						lhctm.setContent(content);
						lhctm.setImg_path(img_path);
						lhctm.setComment_date(time);

						lhctm.setAgrees(recommend);
						lhctm.setResponse(record);
						lhctm.setComment_score(lhpi.getScore());

						DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
						lhctm.setCreate_date(dateFormat.parse(dateFormat.format(now)));

						lhctm.setCreate_time(date);

						try {
							biDao.fastInsert(lhctm);
							logger.info("|||同程酒店评论id:" + lhctm.getComment_id() + "评论日期:" + time + ",页码:" + pageIndex
									+ "|||");
						} catch (Exception e) {
							logger.error("插入异常" + e.getMessage());
						}
					}
					webEle = dr.findElement(By.cssSelector("li.next a"));
					webEle.click();
					String html = dr.getPageSource();
					doc = Jsoup.parse(html);
					pageIndex++;
				} while (webEle != null);

			} catch (Exception e) {
				mysqlDao.update(LyHotelKeywordJob.class, Chain.make("comment_status", 3),
						Cnd.where("id", "=", job.getId()));
				logger.error("【" + job.getUrl() + "】的酒店评论job建立失败", e);
			}

		} catch (Exception e) {
			mysqlDao.update(LyHotelKeywordJob.class, Chain.make("status", 3), Cnd.where("id", "=", job.getId()));
			logger.error("【" + job.getKeyword() + "】的同程酒店产品获取失败", e);
		}
		return lhpi;
	}

	/**
	 * 解析同程房间价格及相关信息
	 * 
	 * @author aaron
	 *
	 * @param sid
	 * @param sname
	 * @param pid
	 * @param pname
	 * @param date
	 * @param job
	 * @return
	 */
	private List<LyHotelRoomTagModel> getRoomPrice(String sid, String sname, String pid, String pname, Date date,
			LyHotelKeywordJob job, Document doc) {

		List<LyHotelRoomTagModel> lhrts = new ArrayList<LyHotelRoomTagModel>();

		try {

			Elements policy_items = doc.select("div#room-list>div.policy-items>div.policy-item");

			for (Element policy_item : policy_items) {

				Elements policy_item_info = policy_item.select("div.policy-item-info");
				/** 房间类型 **/
				String room_type = policy_item_info.select("dd>p.name").text();

				/** 房间信息 **/
				String room_note = policy_item_info.select("dd").first().select("span").text();

				Elements policy_info = policy_item.select("div.policy-info-s");

				for (Element policy_info_s : policy_info) {
					try {
						/** 优惠信息 **/
						String privilege = policy_info_s.select("dt.policy-name>span.name").text();
						/** 房间其他提示信息，如：礼/快/代 **/
						String room_subjoin_note = policy_info_s.select("dt.policy-name>span.tags").text();
						/** 代理 **/
						String agent = null;
						try {
							Elements elements = policy_info_s.select("dt.policy-name>span.tags>i");
							for (Element ele : elements) {
								String str = ele.attr("class");
								if (!StringUtils.isEmpty(str) && str.contains("agent")) {
									agent = "代理";
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						/** 床型 **/
						String bed_type = policy_info_s.select("dd.policy-type").text();
						/** 早餐 **/
						String breakfast = policy_info_s.select("dd.policy-break").text();
						/** 宽带 **/
						String broad_band = null;
						/** 政策 **/
						String policy = policy_info_s.select("dd.policy-rules").text();
						/** 价格 **/
						String price = policy_info_s.select("dd.policy-price>span").first().ownText();
						/** 预定状态：可预订\不可订\订完等等 **/
						String reserve_status = policy_info_s.select("dd.policy-pay").text();
						/** 付款方式：预付等等 **/
						String reserve_style = policy_info_s.select("dd.policy-tag").select("div.tips-rules").text();

						/** 房间价格实例 **/
						LyHotelRoomTagModel lhrt = new LyHotelRoomTagModel();

						lhrt.setSid(sid);
						lhrt.setSname(sname);
						lhrt.setPid(pid);
						lhrt.setPname(pname);
						lhrt.setKeyword(job.getKeyword());
						lhrt.setUrl(job.getUrl());
						try {
							DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
							lhrt.setCreate_date(dateFormat.parse(dateFormat.format(date)));
						} catch (Exception e) {
							logger.error("Url:【" + job.getUrl() + "】创建日期，建立失败");
							lhrt.setCreate_date(date);
						}
						lhrt.setCreate_time(date);
						// 部分的数据set入bean
						lhrt.setRoom_type(room_type);
						lhrt.setRoom_note(room_note);

						lhrt.setPrivilege(privilege);
						lhrt.setRoom_subjoin_note(room_subjoin_note);
						lhrt.setBed_type(bed_type);
						lhrt.setBreakfast(breakfast);
						lhrt.setBroad_band(broad_band);
						lhrt.setPolicy(policy);
						lhrt.setPrice(price);
						lhrt.setReserve_status(reserve_status);
						lhrt.setReserve_style(reserve_style);
						lhrt.setAgent(agent);

						lhrt.setRequest_id(job.getRequest_id());
						lhrt.setTask_id(job.getTask_id());

						lhrts.add(lhrt);

					} catch (Exception e) {
						throw e;
					}
				}

			}
		} catch (Exception e) {
			logger.error("【" + job.getKeyword() + "】的同程酒店产品房间价格获取失败", e);
		}

		return lhrts;
	}

	// public static void main(String args[]) throws Exception {
	//
	// // URL url = new URL("http://gny.ly.com/line/t3j1p7627c0.html");
	// // System.out.println(url.getHost());
	//
	// LyHotelKeywordJob task = new LyHotelKeywordJob();
	// task.setKeyword("http://www.ly.com/HotelInfo-7523.html");
	// LyHotelKeywordThread thread = new LyHotelKeywordThread(task);
	// new Thread(thread).start();
	// }
}
