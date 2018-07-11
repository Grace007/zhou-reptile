package com.reptile.haodaifu;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.bds.base.http.HttpBase;

public class SlideVerifyCode {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	WebDriver dr = null;

	@Test
	public void test() throws InterruptedException {
		//System.setProperty("webdriver.firefox.bin","F:\\Program Files\\Mozilla Firefox\\firefox.exe");
		System.setProperty("webdriver.chrome.driver","C:\\chromedriver57.exe");
		WebDriver dr = new ChromeDriver();
		
		// Actions action = new Actions(dr);
		// WebElement source = dr.findElement(By
		// .xpath("//div[@class='dt_child_content']/div[3]"));
		// action.clickAndHold(source).moveByOffset(
		// (int) (Math.random() * 200) + 80, 0);
		String url = "https://login.taobao.com";
		Cookie cookie = new Cookie("thw","cn");
		dr.get(url);
	
		dr.manage().window().maximize(); 
		JavascriptExecutor js = (JavascriptExecutor) dr;
		// dr.findElement(By.cssSelector("#nocaptcha")).;
		// 点击“账号登陆”
		dr.findElement(By.cssSelector(".forget-pwd.J_Quick2Static")).click();
		dr.manage().addCookie(cookie);
		Thread.sleep(1000);
		dr.findElement(By.id("TPL_username_1")).sendKeys("bds_12");
		Thread.sleep(2000);
		js.executeScript("var nocap = document.getElementById(\"nocaptcha\");"
				+ "nocap.setAttribute(\"style\",\"display:block\");");
		try {
			Thread.sleep(3000);
			dr.findElement(By.id("nocaptcha")).click();// sillerVerifyCode为滑块出现时的id
			System.out.println("滑块元素已经存在!!!");
			Actions action = new Actions(dr);
			// 获取滑动滑块的标签元素
			WebElement source = dr.findElement(By
					.xpath("//*[@id=\"nc_1_n1z\"]"));
			// 确保每次拖动的像素不同，故而使用随机数
			action.clickAndHold(source).moveByOffset(298, 0);
			Thread.sleep(2000);
			// 拖动完释放鼠标
			action.moveToElement(source).release();
			// 组织完这些一系列的步骤，然后开始真实执行操作
			Action actions = action.build();
			actions.perform();
		} catch (ElementNotVisibleException e1) {
			System.out.println("滑块元素不存在");
		}
	}

}
