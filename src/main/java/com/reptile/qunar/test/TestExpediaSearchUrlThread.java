package com.reptile.qunar.test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class TestExpediaSearchUrlThread {
	@Test
	public void test1() {
		try {

			System.setProperty("webdriver.chrome.driver","C:\\chromedriver57.exe");
			WebDriver driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			// 打开网页
			driver.get("https://www.expedia.cn/");
			// 获得输入框的元素位置
			WebElement element = driver.findElement(By.cssSelector("input#hotel-destination"));
			element.clear();
			// 获取焦点
			element.click();
			// 输入要搜索的城市名
			element.sendKeys("上海");
			// 点击搜索按钮
			driver.findElement(By.id("search-button")).click();
			Thread.sleep(4000);
			// 获得输入框的元素位置
			WebElement webElement = driver.findElement(By.cssSelector("input#inpHotelNameMirror"));
			// 获取焦点
			webElement.clear();
			webElement.click();
			webElement.sendKeys("如家");
			// 点击搜索 //可能有问题
			//driver.findElement(By.className("btn-secondary.btn-sub-action")).click();
			driver.findElement(By.cssSelector("span.submitHotelName")).findElement(By.cssSelector("button.btn-sub-action")) .click();
			Thread.sleep(8000);
			/*Thread.sleep(1000);
			Robot robot = new Robot();
			webElement.click();
			robot.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(8000);*/
			System.out.println("#######################################################");
			// 获取本地该酒店的数目
			String string = driver.findElement(By.className("section-header-main")).getText();
			System.out.println("酒店数目："+string);
			// 把文本中的数字提取出来
			String total = string.replaceAll("\\D", "");
			System.out.println("酒店数目："+total);
			int totalInt = Integer.parseInt(total);
			int pages = 0;
			if(totalInt % 20 ==0 && totalInt != 0) { 
				pages = (totalInt) / 20;
		        }
		        else {
		        	pages = (totalInt) / 20 + 1 ;
				}
			
			// 获取当前页面的rul
			String url = driver.getCurrentUrl();
			//https://www.expedia.cn/Hotel-Search?#destination=%E5%8C%97%E4%BA%AC+(%E5%8F%8A%E5%85%B6%E5%91%A8%E8%BE%B9%E5%9C%B0%E5%8C%BA),+%E4%B8%AD%E5%9B%BD&startDate=2017/04/20&endDate=2017/04/21&adults=2&regionId=178237&inventory=regular&hotelName=%E5%A6%82%E5%AE%B6&sort=recommended&page=1
			//https://www.expedia.cn/Hotel-Search?#destination=%E5%8C%97%E4%BA%AC+(%E5%8F%8A%E5%85%B6%E5%91%A8%E8%BE%B9%E5%9C%B0%E5%8C%BA),+%E4%B8%AD%E5%9B%BD&startDate=2017/04/20&endDate=2017/04/21&adults=2&regionId=178237&inventory=regular&hotelName=%E5%A6%82%E5%AE%B6&sort=recommended&page=2

			for (int i = 0; i < pages; i++) {
				String pageUrl = url +"&page="+(i+1);
				System.out.println(pageUrl);
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
