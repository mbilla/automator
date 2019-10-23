package org.automator.config;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
	private static ApplicationProperties appProperties;
	private Properties prop;
	
	private ApplicationProperties(){
		this.prop=new Properties();
	}
	
	public static ApplicationProperties getInstance(){
		if(appProperties == null){
			appProperties=new ApplicationProperties();
		}
		return appProperties;
	}
	
	public Properties getProperties(){
		return this.prop;
	}
	
	public void loadProperties(InputStream is){
		try{
			Properties tempProp=new Properties();
			tempProp.load(is);
			loadProperties(tempProp);
		}catch(Exception ec){
			System.out.println(ec.getMessage());
			//ignore the exception
		}
	}
	
	public void loadProperties(Properties prop){		
		this.prop.putAll(prop);
	}
	
	public String getString(String key){
		return this.prop.getProperty(key);
	}
	
	public String getString(String key, String defaultValue){
		return this.prop.getProperty(key, defaultValue);
	}
	
	public int getInt(String key, int defaultValue){
		try{
			return Integer.parseInt(this.prop.getProperty(key));
		}catch(NumberFormatException nfe){
			return defaultValue;
		}
	}

	public String getEnvironmentUrl() {

		switch(getString(ApplicationPropertiesKeys.ENV).toLowerCase()){
			case "dev":
				return (getString(ApplicationPropertiesKeys.PLATFORM).equalsIgnoreCase("web"))
						? getString(ApplicationPropertiesKeys.ENV_DEV_WEB_URL):
							getString(ApplicationPropertiesKeys.ENV_DEV_BASE_API);
			case "uat":
				return (getString(ApplicationPropertiesKeys.PLATFORM).equalsIgnoreCase("web"))
						? getString(ApplicationPropertiesKeys.ENV_UAT_WEB_URL):
							getString(ApplicationPropertiesKeys.ENV_UAT_BASE_API);
				
			default:
				return (getString(ApplicationPropertiesKeys.PLATFORM).equalsIgnoreCase("web"))
						? getString(ApplicationPropertiesKeys.ENV_QA_WEB_URL):
							getString(ApplicationPropertiesKeys.ENV_QA_BASE_API);
		}
	}
}
