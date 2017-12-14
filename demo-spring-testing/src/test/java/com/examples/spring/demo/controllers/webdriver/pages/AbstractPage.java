package com.examples.spring.demo.controllers.webdriver.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AbstractPage {

	protected WebDriver driver;

	@FindBy(tagName = "body")
	private WebElement body;

	public static int port = 0;

	public AbstractPage(WebDriver driver) {
		setDriver(driver);
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public String getBody() {
		return body.getText();
	}

	static void get(WebDriver driver, String relativeUrl) {
		driver.get("http://localhost"
			+ (port > 0 ? ":" + port : "")
			+ "/" + relativeUrl);
	}
}
