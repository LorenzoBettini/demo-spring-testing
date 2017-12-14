package com.examples.spring.demo.controllers.webdriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.controllers.EmployeeWebController;
import com.examples.spring.demo.controllers.webdriver.pages.EditPage;
import com.examples.spring.demo.controllers.webdriver.pages.HomePage;
import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmployeeWebController.class)
public class EmployeeWebControllerWebDriverTest {

	@MockBean
	private EmployeeService employeeService;

	@Autowired
	private WebDriver webDriver;

	@Test
	public void testHomePageWithNoEmployees() throws Exception {
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getBody()).contains("No employee");
	}

	@Test
	public void testHomePageWithEmployees() throws Exception {
		when(employeeService.getAllEmployees())
			.thenReturn(
				Arrays.asList(
					new Employee(1L, "test1", 1000),
					new Employee(2L, "test2", 2000)));
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
		when(employeeService.getEmployeeById(1))
			.thenReturn(
				new Employee(1L, "test1", 1000));
		// this is the expected modifies employee
		Employee expectedSave = new Employee(1L, "new test1", 2000);
		// simulates that the modified employee is in the db
		when(employeeService.getAllEmployees())
			.thenReturn(
				Arrays.asList(expectedSave));

		EditPage page = EditPage.to(webDriver, 1L);
		assertThat(page.getBody())
			.doesNotContain("No employee found with id: 1");

		// submit the form
		HomePage homePage = page.submitForm(HomePage.class, "new test1", 2000);
		// verify that the modified employee is in the table
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary 1 new test1 2000"
		);

		// verify that the modified employee has been saved through the service
		verify(employeeService).saveEmployee(expectedSave);
	}

	@Test
	public void testNewEmployee() throws Exception {
		// this is the expected modifies employee
		// since the service is mocked, the id null is considered valid
		// in the real implementation, the service will delegate to
		// the repository which will create a new record and assign
		// a valid id > 0
		Employee expectedSave = new Employee(null, "new test1", 2000);
		// simulates that the modified employee is in the db
		when(employeeService.getAllEmployees())
			.thenReturn(
				Arrays.asList(expectedSave));

		EditPage page = EditPage.to(webDriver);
		// submit the form
		HomePage homePage = page.submitForm(HomePage.class, "new test1", 2000);
		// verify that the modified employee is in the table
		// in this test the id is null since the service is mocked
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary new test1 2000"
		);

		// verify that the employee is passed to the service
		// for saving; we expect the id to be null
		// since in the view the hidden id was null as well;
		// it will be automatically assigned by the persistence layer
		// which is mocked in this test, thus not implemented
		verify(employeeService).saveEmployee(expectedSave);
	}
}