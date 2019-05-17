package com.examples.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Executes the tests against a running Spring Boot application; if you run this
 * as test from Eclipse, make sure you first manually run the Spring Boot
 * application.
 * 
 * No Spring specific testing mechanism is used here: this is a plain JUnit test.
 */
public class EmployeeWebControllerE2E {

	private static int port =
		Integer.parseInt(System.getProperty("server.port", "8080"));

	private static String baseUrl = "http://localhost:" + port;

	private WebDriver driver;

	@BeforeClass
	public static void setupClass() {
		// setup Chrome Driver
		WebDriverManager.chromedriver().setup();
	}

	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new ChromeDriver();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void testCreateNewEmployee() {
		driver.get(baseUrl);

		// add a user using the "New employee" link
		driver.findElement
			(By.cssSelector("a[href*='/new")).click();

		// fill the form
		driver.findElement(By.name("name")).sendKeys("new employee");
		driver.findElement(By.name("salary")).sendKeys("2000");
		// press Save
		driver.findElement(By.name("btn_submit")).click();

		// we are redirected to the home page
		// the table (on the home page) shows the created employee
		assertThat(driver.findElement(By.id("employee_table")).getText()).
			contains("new employee", "2000");
	}

}