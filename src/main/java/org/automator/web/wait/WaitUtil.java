package org.automator.web.wait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WaitUtil implements IWaitForAngular,IWaitForJQuery{

    private  WebDriver jsWaitDriver;
    private int timeOutInSeconds;
    private int pollingTimeoutInMillis;
    

    public WaitUtil(WebDriver driver,int timeOutInSeconds, int pollingTimeout){
    	this.jsWaitDriver = driver;
    	this.timeOutInSeconds=timeOutInSeconds;
    	this.pollingTimeoutInMillis = pollingTimeout;
    }

    public WebDriverWait getWait(){
    	return new WebDriverWait(jsWaitDriver,timeOutInSeconds,pollingTimeoutInMillis);
    }
    
    
    //Wait for JQuery Load
    public void waitForJQueryLoad() {
    	WebDriverWait jsWait=new WebDriverWait(jsWaitDriver, timeOutInSeconds,pollingTimeoutInMillis);
    	JavascriptExecutor jsExec=(JavascriptExecutor) jsWaitDriver;
        //Wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver -> ((Boolean) ((JavascriptExecutor) driver)
                .executeScript("return jQuery.active===undefined?jQuery.isReady:jQuery.active===0") == true);

        //Get JQuery is Ready
        boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active===undefined?true:jQuery.active===0");

        //Wait JQuery until it is Ready!
        if(!jqueryReady) {
            System.out.println("JQuery is NOT Ready!");
            //Wait for jQuery to load
            jsWait.until(jQueryLoad);
        } else {
            System.out.println("JQuery is Ready!");
        }
    }

    //Wait for Angular Load
    public void waitForAngularLoad() {
        WebDriverWait wait = new WebDriverWait(jsWaitDriver,timeOutInSeconds,pollingTimeoutInMillis);
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        //Wait for ANGULAR to load
        ExpectedCondition<Boolean> angularLoad = driver -> Boolean.valueOf(((JavascriptExecutor) driver)
                .executeScript(angularReadyScript).toString());

        //Get Angular is Ready
        boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());

        //Wait ANGULAR until it is Ready!
        if(!angularReady) {
            System.out.println("ANGULAR is NOT Ready!");
            //Wait for Angular to load
            wait.until(angularLoad);
        } else {
            System.out.println("ANGULAR is Ready!");
        }
    }

    //Wait Until JS Ready
    public void waitUntilJSReady() {
        WebDriverWait wait = new WebDriverWait(jsWaitDriver,timeOutInSeconds,pollingTimeoutInMillis);
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        //Wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString().equals("complete");

        //Get JS is Ready
        boolean jsReady =  (Boolean) jsExec.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if(!jsReady) {
           System.out.println("JS in NOT Ready!");
            //Wait for Javascript to load
            wait.until(jsLoad);
        } else {
            System.out.println("JS is Ready!");
        }
    }

    //Wait Until JQuery and JS Ready
    public void waitUntilJQueryReady() {
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        //First check that JQuery is defined on the page. If it is, then wait AJAX
        Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
        if (jQueryDefined == true) {
            //Pre Wait for stability (Optional)
            sleep(20);

            //Wait JQuery Load
            waitForJQueryLoad();

            //Wait JS Load
            waitUntilJSReady();

            //Post Wait for stability (Optional)
            sleep(20);
        }  else {
            System.out.println("jQuery is not defined on this site!");
        }
    }

    //Wait Until Angular and JS Ready
    public void waitUntilAngularReady() {
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;
        //First check that ANGULAR is defined on the page. If it is, then wait ANGULAR
        Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
        if (!angularUnDefined) {
            Boolean angularInjectorUnDefined = (Boolean) jsExec.executeScript("return angular.element(document).injector() === undefined");
            if(!angularInjectorUnDefined) {
                //Pre Wait for stability (Optional)
                sleep(20);

                //Wait Angular Load
                waitForAngularLoad();

                //Wait JS Load
                waitUntilJSReady();

                //Post Wait for stability (Optional)
                sleep(20);
            } else {
                System.out.println("Angular injector is not defined on this site!");
            }
        }  else {
            System.out.println("Angular is not defined on this site!");
        }
    }

    //Wait Until JQuery Angular and JS is ready
    public void waitUntilPageLoad() {
        waitUntilJQueryReady();
        waitUntilAngularReady();
    }

    public static void sleep (long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public boolean waitForFunction(WebElement webElement, Function<WebElement, Boolean> waitFunction){
    	FluentWait<WebElement> wait=new FluentWait<>(webElement);
		return wait.withTimeout(Duration.ofSeconds(timeOutInSeconds))
				.pollingEvery(Duration.ofMillis(pollingTimeoutInMillis))
				.ignoring(NotFoundException.class)
				.ignoring(WebDriverException.class)
				.ignoring(StaleElementReferenceException.class)
				.until(waitFunction);
    }
    
	public boolean waitForCondition(ExpectedCondition<Boolean> waitCondition) {
		FluentWait<WebDriver> wait=new FluentWait<>(jsWaitDriver);
		return wait.withTimeout(Duration.ofSeconds(timeOutInSeconds))
				.pollingEvery(Duration.ofMillis(pollingTimeoutInMillis))
				.ignoring(NotFoundException.class)
				.ignoring(WebDriverException.class)
				.ignoring(StaleElementReferenceException.class)
				.until(waitCondition);
	}
}