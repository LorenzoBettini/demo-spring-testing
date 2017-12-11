package com.examples.spring.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmployeeWebController.class)
public class EmployeeWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockBean
	private EmployeeService employeeService;

	@Test
	public void testHomePageWithNoEmployees() throws Exception {
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getTitleText()).isEqualTo("Employees");
		assertThat(page.getBody()
			.getTextContent()).contains("No employee");
	}

	@Test
	public void testHomePageWithEmployees() throws Exception {
		when(employeeService.getAllEmployees())
			.thenReturn(
				Arrays.asList(
					new Employee(1L, "test1", 1000),
					new Employee(2L, "test2", 2000)));
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getBody().getTextContent())
			.doesNotContain("No employee");
		HtmlTable table = page.getHtmlElementById("employee_table");
		assertThat(table.asText()).isEqualTo(
			"ID	Name	Salary\n" + 
			"1	test1	1000\n" + 
			"2	test2	2000"
		);
	}

	@Test
	public void testEditNonExistentEmployee() throws Exception {
		HtmlPage page = this.webClient.getPage("/edit/1");
		assertThat(page.getBody().getTextContent())
			.contains("No employee found with id: 1");
	}

	@Test
	public void testEditExistentEmployee() throws Exception {
		when(employeeService.getEmployeeById(1))
			.thenReturn(
				new Employee(1L, "test1", 1000));
		HtmlPage page = this.webClient.getPage("/edit/1");
		assertThat(page.getBody().getTextContent())
			.doesNotContain("No employee found with id: 1");

		// Get the form that we are dealing with
		final HtmlForm form = page.getFormByName("employee_form");
		// make sure the fields are filled with the correct values
		// and then change their values
		form.getInputByValue("test1").setValueAttribute("new test1");
		form.getInputByValue("1000").setValueAttribute("2000");

		// this is the expected modifies employee
		Employee expectedSave = new Employee(1L, "new test1", 2000);
		// simulates that the modified employee is in the db
		when(employeeService.getAllEmployees())
			.thenReturn(
				Arrays.asList(expectedSave));

		// Now submit the form by clicking the button and get back the second page.
		final HtmlButton button = form.getButtonByName("btn_submit");
		final HtmlPage page2 = button.click();

		// verify that the modified employee has been saved through the service
		verify(employeeService).saveEmployee(expectedSave);

		// verify that the modified employee is in the table
		HtmlTable table = page2.getHtmlElementById("employee_table");
		assertThat(table.asText()).isEqualTo(
			"ID	Name	Salary\n" + 
			"1	new test1	2000"
		);
	}

}