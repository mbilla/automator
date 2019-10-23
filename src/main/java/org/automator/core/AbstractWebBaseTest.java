package org.automator.core;

import java.io.File;
import java.util.Properties;

import org.automator.config.ApplicationProperties;
import org.automator.config.ApplicationPropertiesKeys;
import org.automator.config.DriverConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.testng.ISuiteListener;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public abstract class AbstractWebBaseTest extends TestListenerAdapter implements BaseTest {

	protected ThreadLocal<WebDriver> webDrivers=new ThreadLocal<>();
	protected DriverConfiguration driverConfig;
	protected ApplicationProperties appProp;
	private WebDriver driver;
	
	@BeforeSuite(alwaysRun=true)
	public void loadConfiguration(){
		appProp=ApplicationProperties.getInstance();
		appProp.loadProperties(getConfig());
		driverConfig=DriverConfiguration.getDriverConfig(appProp);
		//AbstractWebBaseTest.class.getResourceAsStream("classpath:/webdrivers/chrome")
	}
	
	public ApplicationProperties getApplicationProperties(){
		return appProp;
	}
	
	
	@BeforeMethod
	public void beforeTestMethod(){
		WebDriver driver=driverConfig.createWebDriver();
		driver.get(appProp.getEnvironmentUrl());
		webDrivers.set(driver);
	}
	
	@AfterMethod
	public void afterTestMethod(){
		getDriver().quit();
	}

	public WebDriver getDriver(){
		return webDrivers.get();
	}
	
	public abstract Properties getConfig();
	
}
