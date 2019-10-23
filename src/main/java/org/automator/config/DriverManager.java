package org.automator.config;

import java.io.File;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeDriverService.Builder;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverManager implements IDriverManager {
	private Browser browser;
	private boolean headless;
	
	@Override
	public WebDriver getDriver() {
		WebDriver webDriver=null;
		Browser webBrowser=getBrowser();
		switch(webBrowser){
		case CHROME:
			ChromeDriverService driverService=new ChromeDriverService.Builder().usingAnyFreePort().build();
			ChromeOptions chromeOptions=new ChromeOptions();
			chromeOptions.setHeadless(headless);
			webDriver = new ChromeDriver(driverService,chromeOptions);
			break;
		case IE:
			InternetExplorerDriverService ieDriverService =new InternetExplorerDriverService.Builder().usingAnyFreePort().build();
			InternetExplorerOptions ieOptions =new InternetExplorerOptions(DesiredCapabilities.internetExplorer());
			ieOptions.ignoreZoomSettings();
			ieOptions.disableNativeEvents();
			webDriver = new InternetExplorerDriver(ieDriverService, ieOptions);
			break;
		case EDGE:
			EdgeDriverService edgeDriverService = new EdgeDriverService.Builder().usingAnyFreePort().build();
			EdgeOptions edgeOptions=new EdgeOptions();
			webDriver = new EdgeDriver(edgeDriverService, edgeOptions);
			break;
		case SAFARI:
			break;
		case FIREFOX:
			GeckoDriverService firefoxService=new GeckoDriverService.Builder().usingAnyFreePort().build();
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			webDriver = new FirefoxDriver(firefoxService, firefoxOptions);
			break;			
		}
		return webDriver;
	}

	@Override
	public Browser getBrowser() {
		
		return this.browser;
	}
	
	public void setHeadless(boolean headless){
		this.headless=getBrowser().equals(Browser.CHROME)?headless:false;
	}

	@Override
	public void setBrowser(Browser webBrowser,File browserDriverFile) {
		if(browserDriverFile.exists()){
			browserDriverFile.setExecutable(true);
			System.setProperty(getDriverExeProperty(webBrowser), browserDriverFile.getPath());
		}else{
			System.out.println("Web Driver is not available. Kindly check the file: "+browserDriverFile.getPath());
		}		
		this.browser = webBrowser;
	}

}
