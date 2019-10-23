package org.automator.core;

import java.io.IOException;
import java.util.Properties;

import org.automator.config.ApplicationProperties;
import org.openqa.selenium.WebDriver;

public class DefaultWebBaseTest extends AbstractWebBaseTest {

	@Override
	public Properties getConfig() {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		try {
			prop.load(ClassLoader.getSystemResourceAsStream("config/automator.config"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return prop;
	}

}
