package com.examples.spring.demo.controllers.webdriver.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends AbstractPage {

	@FindBy(id = "employee_table")
	private WebElement employeeTable;

	public HomePage(WebDriver driver) {
		super(driver);
	}

	public static HomePage to(WebDriver driver) {
		get(driver, "");
		return PageFactory.initElements(driver, HomePage.class);
	}

	public String getEmployeeTableAsString() {
		return employeeTable.getText();
	}

}
