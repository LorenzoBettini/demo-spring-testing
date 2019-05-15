package com.examples.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

/**
 * Some examples of tests for the web controller when running in a real web
 * container, manually using the {@link EmployeeRepository}.
 * 
 * The web server is started on a random port, which can be retrieved by
 * injecting in the test a {@link LocalServerPort}.
 * 
 * In tests you can't rely on fixed identifiers: use the ones returned by the
 * repository after saving (automatically generated)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeWebControllerIT {

	@Autowired
	private EmployeeRepository employeeRepository;

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private String baseUrl;

	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver();
		// always start with an empty database
		employeeRepository.deleteAll();
		employeeRepository.flush();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void testHomePage() {
		Employee testEmployee =
			employeeRepository.save(new Employee(null, "test employee", 1000));

		driver.get(baseUrl);

		// the table shows the test employee
		assertThat(driver.findElement(By.id("employee_table")).getText()).
			contains("test employee", "1000", "Edit");

		// the "Edit" link is present with href containing /edit/{id}
		driver.findElement
			(By.cssSelector
				("a[href*='/edit/" + testEmployee.getId() + "']"));
	}

	@Test
	public void testEditPageNewEmployee() throws Exception {
		driver.get(baseUrl + "/new");

		driver.findElement(By.name("name")).sendKeys("new employee");
		driver.findElement(By.name("salary")).sendKeys("2000");
		driver.findElement(By.name("btn_submit")).click();

		assertThat(employeeRepository.findByName("new employee").getSalary())
			.isEqualTo(2000L);
	}

	@Test
	public void testEditPageUpdateEmployee() throws Exception {
		Employee testEmployee =
			employeeRepository.save(new Employee(null, "test employee", 1000));

		driver.get(baseUrl + "/edit/" + testEmployee.getId());

		final WebElement nameField = driver.findElement(By.name("name"));
		nameField.clear();
		nameField.sendKeys("modified employee");
		final WebElement salaryField = driver.findElement(By.name("salary"));
		salaryField.clear();
		salaryField.sendKeys("2000");
		driver.findElement(By.name("btn_submit")).click();

		assertThat(employeeRepository.findByName("modified employee").getSalary())
			.isEqualTo(2000L);
	}
}