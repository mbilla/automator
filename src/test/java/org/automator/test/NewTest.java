package org.automator.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.automator.config.ApplicationProperties;
import org.automator.core.AbstractWebBaseTest;
import org.automator.core.CustomBy;
import org.automator.core.DefaultWebBaseTest;
import org.automator.data.parser.ExcelParser;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;


public class NewTest  extends DefaultWebBaseTest{

	@Test
	public void f() throws Exception {
		WebDriver driver=getDriver();
		Assert.assertEquals(driver.findElement(CustomBy.findByCssContainingText(".mbs", "Create an account")).getText(),"Create an account");
		String resourceName=appProp.getString("testdata.file");
		ExcelParser excelParser=new ExcelParser(this.getClass().getClassLoader().getResource(resourceName).getPath(),"Sheet1");
		System.out.println(excelParser.getFullDataSet(0));
		excelParser.addFilter("TC_Name", "TC_LoginTest");
		System.out.println(excelParser.getFilteredDataSetAsList(0));
	}

/*	@Override
	public Properties getConfig() {
		Properties prop = new Properties();
		try {
			prop.load(ClassLoader.getSystemResourceAsStream("config/automator.config"));
			System.out.println(prop.get("browser"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return prop;
	}*/

}
