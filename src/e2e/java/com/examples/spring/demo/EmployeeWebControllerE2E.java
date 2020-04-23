package com.examples.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Executes the tests against a running Spring Boot application; if you run this
 * as test from Eclipse, make sure you first manually run the Spring Boot
 * application.
 * 
 * No Spring specific testing mechanism is used here: this is a plain JUnit test.
 */
public class EmployeeWebControllerE2E {

	private static final Logger LOGGER =
		LoggerFactory.getLogger(EmployeeWebControllerE2E.class);

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

	@Test
	public void testEditEmployee() throws JSONException {
		// manually add an employee in the application's db
		String id = postEmployee("employee to edit", 3000);

		// the home page should show it
		driver.get(baseUrl);

		// edit the employee with the "Edit" link
		driver.findElement
			(By.cssSelector("a[href*='/edit/" + id + "']"))
				.click();

		// edit the values with the form
		final WebElement nameField = driver.findElement(By.name("name"));
		nameField.clear();
		nameField.sendKeys("modified employee");
		final WebElement salaryField = driver.findElement(By.name("salary"));
		salaryField.clear();
		salaryField.sendKeys("2000");
		driver.findElement(By.name("btn_submit")).click();

		// we are redirected to the home page
		// the table (on the home page) shows the modified employee
		assertThat(driver.findElement(By.id("employee_table")).getText()).
			contains(id, "modified employee", "2000");
	}

	private String postEmployee(String name, int salary) throws JSONException {
		// create request body
		JSONObject body = new JSONObject();
		body.put("name", name);
		body.put("salary", salary);

		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity =
			new HttpEntity<String>(body.toString(), headers);

		ResponseEntity<String> answer = new RestTemplate()
				.postForEntity(baseUrl + "/api/employees/new",
						entity, String.class);
		LOGGER.debug("answer for POST: " + answer);
		return new JSONObject(answer.getBody()).get("id").toString();
	}
}