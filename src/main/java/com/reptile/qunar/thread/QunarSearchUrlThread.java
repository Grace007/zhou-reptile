package com.reptile.qunar.thread;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.http.HttpBase;
import com.bds.base.util.SpringContextHolder;
import com.reptile.qunar.model.QunarSimpleInfo;
import com.reptile.qunar.model.job.QunarSearchUrlTaskModel;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QunarSearchUrlThread extends Thread {
	private static Logger logger = Logger.getLogger(QunarSearchUrlThread.class);
	private Dao tDao = SpringContextHolder.getBean("tDao");
	private QunarSearchUrlTaskModel task;
	public QunarSearchUrlThread(QunarSearchUrlTaskModel task) {
		this.task = task;
	}
	@Override
	public void run() {
		try {
			tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 1), Cnd.where("id", "=", task.getId()));
			
			TaskToBi tobi = BIDataSource.getBiDataSource("qunar", task.getProject_code(), task.getDown_type());
			Dao biDao = tobi.getBiDao();
			String url = "http://hotel.qunar.com/";
//			System.setProperty("webdriver.chrome.driver","C:\\chromedriver57.exe");
//	    	WebDriver driver = new ChromeDriver();
//			System.setProperty("webdriver.firefox.bin",
//					"F:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
//			WebDriver driver = HttpBase.getFirefoxDriver();
			WebDriver driver = HttpBase.getFirefoxDriver();
	    	driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	    	driver.manage().window().maximize(); 
	    	//打开网页
	    	try {
	    	driver.get(url);
	        
	        WebElement cityEle = driver.findElement(By.cssSelector("input#toCity"));
	        cityEle.click();
	        cityEle.sendKeys(task.getCity());
	        
	              
	        WebElement keyEle1 = driver.findElement(By.cssSelector("div#sinfoXI5"));
	        keyEle1.click();
	        WebElement keyEle = driver.findElement(By.cssSelector("input#q"));
	        keyEle.sendKeys(task.getKeyword());
	        
	        WebElement searchEle =driver.findElement(By.cssSelector("a.search-button"));
	        WebElement h3Ele =driver.findElement(By.cssSelector("h3"));
	        h3Ele.click();
	       
	        searchEle.click();
			} catch (Exception e) {
				tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 0),
						Cnd.where("id", "=", task.getId()));
				driver.close();
				return ;
			}
	        
	        Thread.sleep(4000);
	        WebElement loginElement  = null;
	        //WebElement errorElement  = null;
	        if (driver.getCurrentUrl().contains("hotel.qunar.com/noresult.jsp")) {
				tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 2),
						Cnd.where("id", "=", task.getId()));
				driver.close();
	        	logger.error("==============qunar任务时，当前城市" +task.getCity()+  " 没有被qunar收录==============");
	        	return ;
			}
	        try {
			loginElement = driver.findElement(By.id("QunarPopBox"));
			tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 4),
					Cnd.where("id", "=", task.getId()));
			driver.close();
        	logger.error("==============qunar任务时失败，request_id为：" + task.getRequest_id() + "==============");
        	return ;
	        } catch (Exception e) {
	        	WebElement tempElement = null;
	        	 
		        WebElement existenceEle = driver.findElement(By.id("queryCorrect"));
		        String display = existenceEle.getAttribute("style");
		        if ( !display.contains("none")) {
		        	tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 2).add("pages", 0).add("total", 0), Cnd.where("id", "=", task.getId()));
		        	System.out.println("==========================本页没有数据===================关键字和城市是："+task.getKeyword()+" "+task.getCity());
		        	driver.close();
		        	return ;
				}
		        
		        tempElement = driver.findElement(By.cssSelector("em#js_hotel_num"));
		        String amount = tempElement.getText();
		        //System.out.println(amount);
		        
		        /*Integer amountI = Integer.parseInt(amount);
				Integer pages = amountI / 30 + 1;*/
		        Integer pages = 1;
		        Integer amountI = Integer.parseInt(amount);
		        if(amountI > 90) amountI = 90;
		        if(amountI % 30 ==0 && amountI != 0) { 
				pages = (amountI) / 30;
		        }
		        else {
		        	pages = (amountI) / 30 + 1 ;
				}
				//System.out.println(pages);
				List<WebElement> elements =null;
				QunarSimpleInfo simpleInfo = new QunarSimpleInfo();
				
				for (int j = 0; j < pages; j++) {
					//System.out.println("第"+ (j + 1)+ "页：####################################################");
					Thread.sleep(2000);
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("scrollTo(0,3000)");
					Thread.sleep(5000);
					elements = driver.findElements(By.cssSelector("div.b_result_commentbox"));
					System.out.println("本页有"+elements.size()+"条数据");
					WebElement element = null;
					for (int i = 0; i < elements.size(); i++) {
						element = elements.get(i);
						String title = element.findElement(By.cssSelector("span.hotel_item a")).getAttribute("title");
						String hotel_url = element.findElement(By.cssSelector("span.hotel_item a")).getAttribute("href");
						String hotel_id = element.getAttribute("data-id");
						simpleInfo.setCity(task.getCity());
						simpleInfo.setTask_id(task.getTask_id());
						simpleInfo.setCreate_time(new Date());
						simpleInfo.setDown_type(task.getDown_type());
						simpleInfo.setHotel_url(hotel_url);
						simpleInfo.setKeyword(task.getKeyword());
						simpleInfo.setProject_code(task.getProject_code());
						simpleInfo.setRequest_id(task.getRequest_id());
						simpleInfo.setTitle(title);
						simpleInfo.setHotel_id(hotel_id);
						
						try {
							biDao.fastInsert(simpleInfo);
						} catch (Exception e1) {
							logger.error("=====" + "qunar往BI库灌入失败，失败的hotel_url为：" + simpleInfo.getHotel_url() + "=====");
						}
						
					}
					Thread.sleep(4000);
					if (j != pages - 1) {
						try {
						WebElement searchNextAEle = driver.findElement(By.cssSelector("li.item.next")).findElement(By.cssSelector("a.num.icon-tag"));
						searchNextAEle.click();
						} catch (Exception e1) {
							driver.navigate().refresh();
							j=1;
							continue;
						}
					}
				}
				
		        Thread.sleep(2000);
				driver.close();
				
				tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 2).add("pages", pages).add("total", amountI), Cnd.where("id", "=", task.getId()));
				logger.info("====="+"qunar任务成功，关键字+城市为："+task.getKeyword()+"  "+task.getCity()+"=====");
		        
			}
	        
	        
		} catch (Exception e) {
			tDao.update(QunarSearchUrlTaskModel.class, Chain.make("status", 3),
					Cnd.where("id", "=", task.getId()));
			logger.error("==============qunar任务时失败，request_id为：" + task.getRequest_id() + "==============",e);
		}
		
		
	}
	
	
}
