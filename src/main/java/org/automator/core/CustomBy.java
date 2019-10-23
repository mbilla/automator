package org.automator.core;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class CustomBy{
	String containsText;
	String cssText;
	
	public static By findByCssContainingText(final String cssSelector,final String containingText){
		return new By() {
			@Override
			public List<WebElement> findElements(SearchContext context) {
				JavascriptExecutor jse=(JavascriptExecutor)context;
				return (List<WebElement>) jse.executeScript("var locateWithCss=function(arg1,arg2){"
						+ "return Array.prototype.slice.call(document.querySelectorAll(arg1)).filter(function(x){return x.innerText.includes(arg2)});"
						+ "};"
						+ "return locateWithCss(arguments[0],arguments[1]); ",cssSelector, containingText);
			}
		};
	}
	
	public static By findByCssWithExactText(final String cssSelector,final String containingText){
		return new By() {
			@Override
			public List<WebElement> findElements(SearchContext context) {
				JavascriptExecutor jse=(JavascriptExecutor)context;
				return (List<WebElement>) jse.executeScript("var locateWithCss=function(arg1,arg2){"
						+ "return Array.prototype.slice.call(document.querySelectorAll(arg1)).filter(function(x){return x.innerText===arg2});"
						+ "};"
						+ "return locateWithCss(arguments[0],arguments[1]); ",cssSelector, containingText);
			}
		};
	}

}
