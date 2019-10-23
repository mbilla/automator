package org.automator.config;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriverService;

public class DriverConfiguration {	
	private static DriverConfiguration driverConfig;
	private ApplicationProperties appProperties;
	
	
	private DriverConfiguration(ApplicationProperties appProp){
		this.appProperties=appProp;
	}
	
	public static DriverConfiguration getDriverConfig(ApplicationProperties appProp) {
		if(driverConfig == null){
			driverConfig=new DriverConfiguration(appProp);
		}
		return driverConfig;
	}
	
	public Browser getWebBrowserType(){
		switch(appProperties.getString(ApplicationPropertiesKeys.BROWSER).toLowerCase()){
		case "chrome":
			return Browser.CHROME;
		case "safari":
			return Browser.SAFARI;
		case "ie":
			return Browser.IE;
		case "edge":
			return Browser.EDGE;
		default:
			return Browser.FIREFOX;
		}
	}
	
	public int getExplicitTimeout(){
		return appProperties.getInt(ApplicationPropertiesKeys.BROWSER_EXPLICIT_TIMEOUT, 20);
	}
	
	public int getPollingTimeout(){
		return appProperties.getInt(ApplicationPropertiesKeys.BROWSER_POLLING_TIMEOUT, 500);
	}
	
	public int getImplicitTimeout(){
		return appProperties.getInt(ApplicationPropertiesKeys.BROWSER_TIMEOUT, 10);
	}

	public WebDriver createWebDriver() {		
		return DriverBuilder.getDriverBuilder()
				.setBrowser(getWebBrowserType())
				.setBrowserDriverFile(getBrowserDriverLocation())
				.setImplicitWaitTimeout(getImplicitTimeout())
				.setMaximizeWindow(isMaximize())
				.setHeadless(getHeadlessMode())
				.build();		
		 
	}
	
	public boolean getHeadlessMode(){
		return Boolean.valueOf(appProperties.getString(ApplicationPropertiesKeys.BROWSER_HEADLESS));
	}
	
	public boolean isMaximize(){
		return Boolean.valueOf(appProperties.getString(ApplicationPropertiesKeys.BROWSER_MAXIMIZE));
	}
	
	public boolean isFullscreenMode(){
		return Boolean.valueOf(appProperties.getString(ApplicationPropertiesKeys.BROWSER_FULLSCREEN));
	}


	private File getBrowserDriverLocation() {
		String currentWorkingDirectory=System.getProperty("user.dir");
		String driverLocation="";
		URL driverURL=null;
		switch(getWebBrowserType()){
		case CHROME:
			driverLocation=appProperties.getString(ApplicationPropertiesKeys.CHROME_DRIVER_LOCATION);			
			break;
		case IE:
			driverLocation=appProperties.getString(ApplicationPropertiesKeys.IE_DRIVER_LOCATION);			
			break;
		case EDGE:
			driverLocation=appProperties.getString(ApplicationPropertiesKeys.EDGE_DRIVER_LOCATION);			
			break;
		case SAFARI:
			driverLocation=appProperties.getString(ApplicationPropertiesKeys.SAFARI_DRIVER_LOCATION);			
			break;
		default:
			driverLocation=appProperties.getString(ApplicationPropertiesKeys.FIREFOX_DRIVER_LOCATION);			
		}
		try{
			driverURL=ClassLoader.getSystemResource(driverLocation);
			if(driverURL == null){
				StringBuilder driverLocationBuilder=new StringBuilder();
				driverLocationBuilder.append(currentWorkingDirectory).append(File.separator).append(driverLocation);					
				driverURL=new URL(driverLocationBuilder.toString());
				File driverFile=new File(driverURL.getPath());
				if(!driverFile.exists()){
					System.out.println("Driver file is not located. Kindly check the file: "+driverFile.getPath());
					return null;
				}
			}
			return new File(driverURL.getPath());
		}catch(Exception ec){
			ec.printStackTrace();
			return null;
		}
	}

}
