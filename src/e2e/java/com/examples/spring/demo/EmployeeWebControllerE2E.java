package com.examples.spring.demo;

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
	public void testHomePage() {
		driver.get(baseUrl);

		// the "New employee" link is present with href containing /new
		driver.findElement
			(By.cssSelector("a[href*='/new"));
	}

}