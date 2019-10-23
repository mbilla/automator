package org.automator.core;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.automator.web.wait.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;


public abstract class AbstractBasePage implements IBasePage{
	protected WebDriver driver;
	protected WaitUtil waitUtil;
	protected int timeOutInSeconds;
	protected int pollingMilliSeconds;
	
	public AbstractBasePage(WebDriver driver,int timeOutInSeconds,int pollingMilliSeconds){
		this.driver = driver;
		waitUtil = new WaitUtil(driver, timeOutInSeconds,pollingMilliSeconds);
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, timeOutInSeconds), this);
		this.waitForPageLoad();
	}
	
	public void waitForPageLoad(){
		waitUtil.waitUntilPageLoad();
		waitUtil.waitForCondition(isPageLoaded());
	}
	
	public WaitUtil getWait(){
		return waitUtil;
	}
	
	public boolean waitForElementToDisplay(WebElement element, Function<WebElement,Boolean> toDisplayMethod){
		FluentWait<WebElement> waitForElement = new FluentWait<>(element);
		return  waitForElement.ignoring(NotFoundException.class)
							  .ignoring(WebDriverException.class)
							  .withTimeout(Duration.ofSeconds(timeOutInSeconds))
							  .pollingEvery(Duration.ofMillis(pollingMilliSeconds))
							  .until(toDisplayMethod); 
	}
	
	public boolean waitForElementToClickable(WebElement element, Function<WebElement,Boolean> toClickableMethod){
		FluentWait<WebElement> waitForElement = new FluentWait<>(element);
		return  waitForElement.ignoring(NotFoundException.class)
							  .ignoring(WebDriverException.class)
							  .withTimeout(Duration.ofSeconds(timeOutInSeconds))
							  .pollingEvery(Duration.ofMillis(pollingMilliSeconds))
							  .until(toClickableMethod); 
	}
	
	public boolean waitForElementToEnable(WebElement element, Function<WebElement,Boolean> toBeEnable){
		FluentWait<WebElement> waitForElement = new FluentWait<>(element);
		return  waitForElement.ignoring(NotFoundException.class)
							  .ignoring(WebDriverException.class)
							  .withTimeout(Duration.ofSeconds(timeOutInSeconds))
							  .pollingEvery(Duration.ofMillis(pollingMilliSeconds))
							  .until(toBeEnable); 
	}
	
	public boolean waitForElementAttributeContains(WebElement element, String attribute, String value){
		WebDriverWait webDriverWait=new WebDriverWait(driver, timeOutInSeconds, pollingMilliSeconds);
		return webDriverWait.ignoring(NotFoundException.class)
		.ignoring(WebDriverException.class)
		.until(ExpectedConditions.attributeContains(element, attribute, value));
	}
	
	public boolean waitForElementAttributeContains(By elementLocator, String attribute, String value){
		WebDriverWait webDriverWait=new WebDriverWait(driver, timeOutInSeconds, pollingMilliSeconds);
		return webDriverWait.ignoring(NotFoundException.class)
		.ignoring(WebDriverException.class)
		.until(ExpectedConditions.attributeContains(elementLocator, attribute, value));
	}
	
	public boolean waitForElementToDisplay(By byLocator){
		FluentWait<WebDriver> waitForElement = new FluentWait<>(driver);
		return  waitForElement.ignoring(NotFoundException.class)
							  .ignoring(WebDriverException.class)
							  .withTimeout(Duration.ofSeconds(timeOutInSeconds))
							  .pollingEvery(Duration.ofMillis(pollingMilliSeconds))
							  .until(ExpectedConditions.visibilityOfElementLocated(byLocator)).isDisplayed(); 
	}
	
	public boolean waitForElementToDisplay(WebElement element){
		FluentWait<WebElement> waitForElement = new FluentWait<>(element);
		return  waitForElement.ignoring(NotFoundException.class)
							  .ignoring(WebDriverException.class)
							  .withTimeout(Duration.ofSeconds(timeOutInSeconds))
							  .pollingEvery(Duration.ofMillis(pollingMilliSeconds))
							  .until(x ->  x.isDisplayed()); 
	}
	
	
}
