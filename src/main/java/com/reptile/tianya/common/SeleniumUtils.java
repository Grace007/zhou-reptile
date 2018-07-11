package com.reptile.tianya.common;

import com.bds.base.util.FastDFSUtil;
import com.reptile.tianya.modelcommon.Image;
import com.reptile.tianya.modelcommon.Resource;
import com.reptile.tianya.modelcommon.Site;
import com.reptile.tianya.modelcommon.SiteType;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SeleniumUtils {

	protected static Logger logger = Logger.getLogger(SeleniumUtils.class);
	final static Random RANDOM = new Random();

	public static String snapshot(String pid, String pname, NutDao dao, WebDriver dr, Site site, SiteType siteType) {
		return snapshot(pid, pname, dao, dr, site, siteType, null);
	}

	public static synchronized String snapshot(String pid, String pname, final NutDao dao, WebDriver dr, Site site,
			SiteType siteType, String sku) {
		String image_url = null;
		if (StringUtils.isNotEmpty(sku)) {
			sku = sku.replaceAll(" ", "");
			sku = sku.replaceAll("已选中", "");
		}

		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		DateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
		final Image image = new Image();
		image.setSku(sku);
		image.setPid(pid);
		image.setPname(pname);
		image.setCreate_time(new Date());
		image.setCreate_date(format.format(new Date()));
		image.setResource(Resource.screenshot);
		image.setSite(site);
		image.setType(siteType);
		final String path = File.separator + image.getSite() + File.separator + pid;
		final String filename = format2.format(new Date()) + "_" + RANDOM.nextInt(10000) + ".jpg";
		image.setPath(path + File.separator + filename);

		try {
			File f = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
			final File ff = File.createTempFile(site.name(), filename);
			FileUtils.copyFile(f, ff);
			// System.out.println(">>>"+ff.getPath());
			f.delete();
//			new Thread() {
//				public void run() {
					try {
						Thumbnails.of(ff).scale(1f).toFile(ff);
						String[] fs = FastDFSUtil.uploadFiles(ff);
						System.out.println(fs[0] + ":" + fs[1]);
						image_url = "http://119.90.36.153:8080/"+fs[0]+"/"+fs[1];
						image.setDfs_group(fs[0]);
						image.setDfs_path(fs[1]);
						try {
							dao.insert(image);
						} catch (Exception ee) {
						}
						ff.delete();
					} catch (Exception eee) {
						logger.error("FTP保存截图失败", eee);
					}
//				}
//			}.start();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("FTP保存截图失败", e);
		}
		return image_url;
	}

	public static synchronized void snapshot(String pid, String pname, final Dao dao, final File file, Site site,
			SiteType siteType) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		final Image image = new Image();
		image.setPid(pid);
		image.setPname(pname);
		image.setCreate_time(new Date());
		image.setCreate_date(format.format(new Date()));
		image.setResource(Resource.screenshot);
		image.setSite(site);
		image.setType(siteType);
		// final String path = File.separator + image.getSite() + File.separator
		// + pid;
		// final String filename = format2.format(new Date()) + "_" +
		// RANDOM.nextInt(10000) + ".jpg";
		// image.setPath(path + File.separator + filename);
		try {
			dao.insert(image);
		} catch (Exception ee) {
		}
		try {
			new Thread() {
				public void run() {
					try {
						// FtpUtils ftpUtils = new FtpUtils();
						// ftpUtils.upload(path, filename, file);
						String[] fs = FastDFSUtil.uploadFiles(file);
						image.setDfs_group(fs[0]);
						image.setDfs_path(fs[1]);
						try {
							dao.insert(image);
						} catch (Exception ee) {
						}
						file.delete();
						logger.info("FTP保存截图成功");
					} catch (Exception eee) {
						logger.error("FTP保存截图失败", eee);
					}
				}
			}.start();
		} catch (Exception e) {
			logger.error("FTP保存截图失败", e);
		}
	}

	/**
	 * 
	 * @param pid
	 * @param pname
	 * @param dao
	 * @param dr
	 * @param site
	 * @param siteType
	 * @param image_quality
	 * @param compress_rate
	 */
	public static synchronized void snapshotTosmall(String pid, String pname, Dao dao, WebDriver dr, Site site,
			SiteType siteType, int compress_rate, float image_quality) {
		try {
			File f = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
			ImgUtils imgUtils = new ImgUtils();
			File destFile = imgUtils.compressImage(f, compress_rate, image_quality);
			snapshot(pid, pname, dao, destFile, site, siteType);
		} catch (Exception e) {
			logger.error("FTP保存截图失败", e);
		}
	}

	/**
	 * 
	 * @param pid
	 * @param pname
	 * @param dao
	 * @param dr
	 * @param site
	 * @param siteType
	 * @param image_quality
	 * @param compress_rate
	 */
	public static synchronized String snapshotToImgPath(String pid, String pname, Dao dao, WebDriver dr, Site site,
			SiteType siteType, int compress_rate, float image_quality) {
		try {
			File f = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
			ImgUtils imgUtils = new ImgUtils();
			File destFile = imgUtils.compressImage(f, compress_rate, image_quality);
			return snapshotImgPath(pid, pname, dao, destFile, site, siteType);
		} catch (Exception e) {
			logger.error("FTP保存截图失败", e);
		}
		return null;
	}

	public static synchronized String snapshotImgPath(String pid, String pname, final Dao dao, final File file,
			Site site, SiteType siteType) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		final Image image = new Image();
		image.setPid(pid);
		image.setPname(pname);
		image.setCreate_time(new Date());
		image.setCreate_date(format.format(new Date()));
		image.setResource(Resource.screenshot);
		image.setSite(site);
		image.setType(siteType);
		// final String path = File.separator + image.getSite() + File.separator
		// + pid;
		// final String filename = format2.format(new Date()) + "_" +
		// RANDOM.nextInt(10000) + ".jpg";
		// image.setPath(path + File.separator + filename);
		try {
			dao.insert(image);
		} catch (Exception ee) {
		}
		try {
			// FtpUtils ftpUtils = new FtpUtils();
			// ftpUtils.upload(path, filename, file);
			String[] fs = FastDFSUtil.uploadFiles(file);
			image.setDfs_group(fs[0]);
			image.setDfs_path(fs[1]);
			try {
				dao.insert(image);
			} catch (Exception ee) {
			}
			file.delete();
			logger.info("FTP保存截图成功");
		} catch (Exception eee) {
			logger.error("FTP保存截图失败", eee);
		}
		return image.getDfs_group() + "/" + image.getDfs_path();
	}

	public static synchronized String snapshotImgPath(String pid, String pname, final NutDao dao, WebDriver dr,
			Site site, SiteType siteType, int compress_rate, float image_quality, String sku) {
		String pic_url="";
		try {
			File f = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
			ImgUtils imgUtils = new ImgUtils();
			File destFile = imgUtils.compressImage(f, compress_rate, image_quality);
			pic_url= snapshotImgPath(pid, pname, dao, destFile, site, siteType,sku);
		} catch (Exception e) {
			logger.error("FTP保存截图失败", e);
		}
		return pic_url;
	}
	public static synchronized String snapshotImgPath(String pid, String pname, final Dao dao, final File file,
			Site site, SiteType siteType,String sku) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		final Image image = new Image();
		image.setPid(pid);
		image.setPname(pname);
		image.setCreate_time(new Date());
		image.setCreate_date(format.format(new Date()));
		image.setResource(Resource.screenshot);
		image.setSite(site);
		image.setType(siteType);
		image.setSku(sku);
		try {
			dao.insert(image);
		} catch (Exception ee) {
		}
		try {
			// FtpUtils ftpUtils = new FtpUtils();
			// ftpUtils.upload(path, filename, file);
			String[] fs = FastDFSUtil.uploadFiles(file);
			image.setDfs_group(fs[0]);
			image.setDfs_path(fs[1]);
			try {
				dao.insert(image);
			} catch (Exception ee) {
			}
			file.delete();
			logger.info("FTP保存截图成功");
		} catch (Exception eee) {
			logger.error("FTP保存截图失败", eee);
		}
		return image.getDfs_group() + "/" + image.getDfs_path();
	}
}
