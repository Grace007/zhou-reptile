package com.reptile.qunar.test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SupermarketTest {
	@Test
	public void test() throws Exception {
		
	
	System.setProperty("webdriver.chrome.driver","C:\\chromedriver57.exe");
	WebDriver driver = new ChromeDriver();
	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	driver.manage().window().maximize(); 
	
	String url = "http://www.supermarket.com/";
	
	driver.get(url);
	Thread.sleep(8000);
	WebElement keyEle = driver.findElement(By.cssSelector("input#key"));
	keyEle.clear();
	keyEle.click();
	keyEle.sendKeys("小米");
	Thread.sleep(5000);
	
	WebElement searchEle = driver.findElement(By.cssSelector("input.button"));
	searchEle.click();
	
	List<WebElement> elements = driver.findElements(By.cssSelector("li.item-book"));
	WebElement itemElement = elements.get(0);
	WebElement itemAElement = itemElement.findElement(By.cssSelector("a"));
	Thread.sleep(5000);
	
	itemAElement.click();
	
	Thread.sleep(5000);
	
	
	WebElement addcartElement = driver.findElement(By.cssSelector("a.btn-append "));
	addcartElement.click();
	Thread.sleep(5000);
	WebElement lookcartElement = driver.findElement(By.cssSelector("a[href=/cart/cart.html]"));
	lookcartElement.click();
	Thread.sleep(5000);
	
	driver.close();
	
	}
}
