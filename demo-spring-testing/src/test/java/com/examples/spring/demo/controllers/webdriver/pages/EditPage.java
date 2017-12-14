package com.examples.spring.demo.controllers.webdriver.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditPage extends AbstractPage {

	private WebElement name;

	private WebElement salary;

	@FindBy(name = "btn_submit")
	private WebElement submit;

	public EditPage(WebDriver driver) {
		super(driver);
	}

	/**
	 * Go to "edit" page with the HTTP GET "new"
	 * 
	 * @param driver
	 * @return
	 */
	public static EditPage to(WebDriver driver) {
		get(driver, "new");
		return PageFactory.initElements(driver, EditPage.class);
	}

	/**
	 * Go to "edit" page with the HTTP GET "edit/" + employeeId
	 * 
	 * @param driver
	 * @param employeeId
	 * @return
	 */
	public static EditPage to(WebDriver driver, Long employeeId) {
		get(driver, "edit/" + employeeId);
		return PageFactory.initElements(driver, EditPage.class);
	}

	public <T> T submitForm(Class<T> resultPage, String name, int salary) {
		this.name.clear();
		this.name.sendKeys(name);
		this.salary.clear();
		this.salary.sendKeys("" + salary);
		this.submit.click();
		return PageFactory.initElements(driver, resultPage);
	}

}
