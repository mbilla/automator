package org.automator.core;

import java.util.function.Function;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public interface IBasePage{
		public void waitForPageLoad();
		public ExpectedCondition<Boolean> isPageLoaded();
		public boolean waitForElementToDisplay(WebElement element, Function<WebElement,Boolean> toDisplayMethod);
		public boolean waitForElementToEnable(WebElement element, Function<WebElement,Boolean> toDisplayMethod);
		public boolean waitForElementToClickable(WebElement element, Function<WebElement,Boolean> toDisplayMethod);
		public boolean waitForElementAttributeContains(WebElement element, String attribute, String value);		
}
