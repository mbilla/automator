package org.automator.config;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

public class DriverBuilder {
	
	private int implicitTimeout;
	private Browser webBrowser;
	private File browserDriverFile;
	private boolean launchFullScreen;
	private boolean headless;
	private boolean maximize;
	
	private DriverBuilder(){
		
	}
	
	
	public static DriverBuilder getDriverBuilder(){
		return new DriverBuilder();
	}
	
	public DriverBuilder setBrowser(Browser browser){
		this.webBrowser=browser;
		return this;
	}
	
	public DriverBuilder setImplicitWaitTimeout(int timeout){
		this.implicitTimeout = timeout;
		return this;
	}
	
	public DriverBuilder setBrowserDriverFile(File browserDriver) {
		this.browserDriverFile=browserDriver;
		return this;
	}

	public DriverBuilder setFullscreen(boolean isFullscreen) {
		this.launchFullScreen=isFullscreen;
		return this;
	}
	
	public DriverBuilder setMaximizeWindow(boolean toMaximize){
		this.maximize=toMaximize;
		return this;
	}
	
	public DriverBuilder setHeadless(boolean isHeadless){
		this.headless=isHeadless;
		return this;
	}

	public WebDriver build() {	
		DriverManager driverManager=new DriverManager();
		driverManager.setBrowser(this.webBrowser,this.browserDriverFile);
		driverManager.setHeadless(this.headless);
		WebDriver webDriver=driverManager.getDriver();
		if(this.maximize){
			webDriver.manage().window().maximize();
		}
		webDriver.manage().timeouts().implicitlyWait(this.implicitTimeout, TimeUnit.SECONDS);
		return webDriver;
	}


	
}
