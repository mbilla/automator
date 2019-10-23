package org.automator.config;

import java.io.File;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriverService;

public interface IDriverManager {
	public void setBrowser(Browser webBrowser,File browserDriverFile);
	public WebDriver getDriver();
	public Browser getBrowser();
	
	public default String getDriverExeProperty(Browser browser){
		switch(browser){
		case CHROME:
			return ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY;
		case SAFARI:
			return null;
		case IE:
			return InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY;
		case EDGE:
			return EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY;
		default:
			return GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY;
		}
	}
}
