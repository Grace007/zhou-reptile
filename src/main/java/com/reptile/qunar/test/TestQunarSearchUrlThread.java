package com.reptile.qunar.test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;


public class TestQunarSearchUrlThread {
	@Test
	public void test() throws InterruptedException{
		
		System.setProperty("webdriver.chrome.driver","C:\\chromedriver57.exe");
    	WebDriver driver = new ChromeDriver();
    	driver.manage().window().maximize(); 
		
		String url ="http://hotel.qunar.com/city/huainan/q-%E5%B8%8C%E5%B0%94%E9%A1%BF%E8%8A%B1%E5%9B%AD#fromDate=2017-04-26&from=qunarindex&toDate=2017-04-27";
    	String url1 = "http://hotel.qunar.com/city/beijing_city/q-%E6%B1%89%E5%BA%AD#fromDate=2017-04-26&cityurl=beijing_city&from=hotellist%7Csug&toDate=2017-04-27&QHFP=ZSL_A2276774&bs=&bc=%E5%8C%97%E4%BA%AC";
		driver.get(url);
    	
    	WebElement existenceEle = driver.findElement(By.id("queryCorrect"));
    	System.out.println(existenceEle.getAttribute("id"));
    	String status = existenceEle.getAttribute("style");
    	if (!status.contains("none")) {
			System.out.println(status.contains("none"));
			System.out.println(status.contains("none")+"dasddsdas");
		}
    	else {
    		System.out.println(status.contains("none"));
    		
		}
    	//style="display:none;"
    	System.out.println(status);
		
    	
    	
    	
    	
    	Thread.sleep(15000);
    	
    	driver .close();
	}
	
	
	

	public static void main(String[] args) {
		try {
			String keyword = "汉庭";
			String city = "上海";
			String url = "http://hotel.qunar.com/";
			System.setProperty("webdriver.chrome.driver","C:\\chromedriver57.exe");
	    	WebDriver driver = new ChromeDriver();
	    	//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    	driver.manage().window().maximize(); 
	    	
	    	//打开网页
	        driver.get(url);
			
	        WebElement cityEle = driver.findElement(By.cssSelector("input#toCity"));
	        cityEle.click();
	        cityEle.sendKeys(city);
	        
	        WebElement keyEle1 = driver.findElement(By.cssSelector("div#sinfoXI5"));
	        keyEle1.click();
	        WebElement keyEle = driver.findElement(By.cssSelector("input#q"));
	        keyEle.sendKeys(keyword);
	        
	        WebElement searchEle =driver.findElement(By.cssSelector("a.search-button"));
	        WebElement h3Ele =driver.findElement(By.cssSelector("h3"));
	        h3Ele.click();
	        System.out.println(searchEle.getText());
	        searchEle.click();
	        Thread.sleep(4000);
	        
	        WebElement tempElement = null;
	        tempElement = driver.findElement(By.cssSelector("em#js_hotel_num"));
	        String amount = tempElement.getText();
	        System.out.println(amount);
	        Integer pages = 1;
	        Integer amountI = Integer.parseInt(amount);
	        if(amountI % 30 ==0 && amountI != 0) { 
			pages = (amountI) / 30;
	        }
	        else {
	        	pages = (amountI) / 30 + 1 ;
			}
			System.out.println(pages);
			List<WebElement> elements =null;
			
			
			
			for (int j = 0; j < pages; j++) {
				System.out.println("第"+ (j + 1)+ "页：#################################################");
				Thread.sleep(2000);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("scrollTo(0,3000)");
				Thread.sleep(5000);
				elements = driver.findElements(By.cssSelector("div.b_result_commentbox"));
				System.out.println(elements.size());
				WebElement element = null;
				for (int i = 0; i < elements.size(); i++) {
					element = elements.get(i);
					String title = element.findElement(By.cssSelector("span.hotel_item a")).getAttribute("title");
					String hotel_url = element.findElement(By.cssSelector("span.hotel_item a")).getAttribute("href");
					String hotel_id = element.getAttribute("data-id");

					System.out.println(i + 1);
					System.out.println(title);
					System.out.println(hotel_id);
					System.out.println(hotel_url);
				}
				Thread.sleep(5000);
				if (j != pages - 1) {
					
					WebElement searchNextAEle = driver.findElement(By.cssSelector("li.item.next")).findElement(By.cssSelector("a.num.icon-tag"));
					searchNextAEle.click();
				}
				/*WebElement searchNextEle = driver.findElement(By.cssSelector("li.item.next"));//item next 
				String nextEleString = searchNextEle.getAttribute("class");
				System.out.println(nextEleString);
				if (nextEleString != "item next  non_disable") {
					WebElement searchNextAEle =driver.findElement(By.cssSelector("a.num.icon-tag"));
					searchNextAEle.click();
				    Thread.sleep(4000);
				}*/
				
				
				
			}
			
	        Thread.sleep(2000);
			driver.close();

		} catch (Exception e) {
			System.out.println("错误信息：" + e);
			e.printStackTrace();
		}

	
	}
}


//WebElement searchUrl = driver.findElement(By.cssSelector("ul.items"));
/*WebElement searchNextEle = driver.findElement(By.cssSelector("li.item.next"));//item next 
String nextEleString = searchNextEle.getAttribute("class");
System.out.println(nextEleString);
if (nextEleString != "item next  non_disable") {
	WebElement searchNextAEle =driver.findElement(By.cssSelector("a.num.icon-tag"));
	searchNextAEle.click();
    Thread.sleep(4000);
}*/
/* WebElement searchNextFatherDisable =null;
try {
    searchNextFatherDisable = driver.findElement(By.cssSelector("li.ItemEvent.next.non_disable"));
} catch (Exception e) {
	searchNextFatherDisable =null;
}*/

/*WebElement searchNextAEle =driver.findElement(By.cssSelector("a.num.icon-tag"));

if(searchNextFatherDisable == null ){
	searchNextAEle.click();
	Thread.sleep(4000);
}*/
