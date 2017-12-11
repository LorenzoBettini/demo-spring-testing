package com.examples.spring.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.controllers.webdriver.pages.EditPage;
import com.examples.spring.demo.controllers.webdriver.pages.HomePage;
import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

/**
 * Integration test example for the web controller and the
 * service, the MVC is still mocked.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeWebControllerWebDriverIT {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private WebDriver webDriver;

	@Before
	public void deleteAllEmployees() {
		employeeService.deleteAll();
	}

	@Test
	public void testHomePageWithNoEmployees() throws Exception {
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getBody()).contains("No employee");
	}

	@Test
	public void testHomePageWithEmployees() throws Exception {
		employeeService.saveEmployee(new Employee(1L, "test1", 1000));
		employeeService.saveEmployee(new Employee(2L, "test2", 2000));
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary 1 test1 1000 2 test2 2000"
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
		employeeService.saveEmployee(new Employee(1L, "test1", 1000));

		EditPage page = EditPage.to(webDriver, 1L);
		assertThat(page.getBody())
			.doesNotContain("No employee found with id: 1");

		// submit the form
		HomePage homePage = page.submitForm(HomePage.class, "new test1", 2000);

		// verify that the modified employee is in the table
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary 1 new test1 2000"
		);
	}

	@Test
	public void testNewEmployee() throws Exception {
		EditPage page = EditPage.to(webDriver);

		// submit the form
		HomePage homePage = page.submitForm(HomePage.class, "new test1", 2000);

		// verify that the modified employee is in the table
		// with automatically assigned id
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary 1 new test1 2000"
		);
	}
}