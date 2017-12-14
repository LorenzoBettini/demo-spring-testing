package com.examples.spring.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.controllers.webdriver.pages.AbstractPage;
import com.examples.spring.demo.controllers.webdriver.pages.EditPage;
import com.examples.spring.demo.controllers.webdriver.pages.HomePage;
import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

/**
 * Integration test example for the web controller and the
 * service, moreover the Web server is NOT mocked (a real
 * Tomcat is started on a random port).
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeWebControllerWebDriverRealServerIT {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private WebDriver webDriver;

	@LocalServerPort
	private int port;

	@TestConfiguration
	static class WebDriverConfiguration {
		@Bean
		public WebDriver getWebDriver() {
			return new HtmlUnitDriver();
		}
	}

	@Before
	public void prepare() {
		AbstractPage.port = port;
		employeeService.deleteAll();
	}

	@Test
	public void testHomePageWithNoEmployees() throws Exception {
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getBody()).contains("No employee");
	}

	@Test
	public void testHomePageWithEmployees() throws Exception {
		Employee saved1 = employeeService.saveEmployee(new Employee(1L, "test1", 1000));
		Employee saved2 = employeeService.saveEmployee(new Employee(2L, "test2", 2000));
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary "
			+ saved1.getId()
			+ " test1 1000 "
			+ saved2.getId()
			+ " test2 2000"
		);
	}

	@Test
	public void testEditNonExistentEmployee() throws Exception {
		EditPage page = EditPage.to(webDriver, 2000L);
		assertThat(page.getBody())
			.contains("No employee found with id: 2000");
	}

	@Test
	public void testEditExistentEmployee() throws Exception {
		Employee saved = employeeService.saveEmployee(new Employee(1L, "test1", 1000));
		Long id = saved.getId();

		EditPage page = EditPage.to(webDriver, id);
		assertThat(page.getBody())
			.doesNotContain("No employee found with id: " + id);

		// submit the form
		HomePage homePage = page.submitForm(HomePage.class, "new test1", 2000);

		// verify that the modified employee is in the table
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary " + id + " new test1 2000"
		);
	}

	@Test
	public void testNewEmployee() throws Exception {
		EditPage page = EditPage.to(webDriver);

		// submit the form
		HomePage homePage = page.submitForm(HomePage.class, "new test1", 2000);

		List<Employee> employees = employeeService.getAllEmployees();
		assertThat(employees).hasSize(1);
		// verify that the modified employee is in the table
		// with automatically assigned id
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary " + employees.get(0).getId() + " new test1 2000"
		);
	}
}