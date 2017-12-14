package com.examples.spring.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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