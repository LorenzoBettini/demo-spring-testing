package com.examples.spring.demo.controllers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;
import com.gargoylesoftware.htmlunit.WebClient;
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
	public void testHomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getTitleText()).isEqualTo("Employees");
	}

	@Test
	public void testHomePageWithNoEmployees() throws Exception {
		when(employeeService.getAllEmployees())
			.thenReturn(emptyList());

		HtmlPage page = this.webClient.getPage("/");

		assertThat(page.getBody()
			.getTextContent()).contains("No employee");
	}

	@Test
	public void test_HomePage_ShouldProvideALinkForCreatingANewEmployee() throws Exception {
		HtmlPage page = this.webClient.getPage("/");

		assertThat(
			page
				.getAnchorByText("New employee")
				.getHrefAttribute()
		).isEqualTo("/new");
	}

	@Test
	public void test_HomePageWithEmployees_ShouldShowThemInATable() throws Exception {
		when(employeeService.getAllEmployees())
			.thenReturn(
				asList(
					new Employee(1L, "test1", 1000),
					new Employee(2L, "test2", 2000)));

		HtmlPage page = this.webClient.getPage("/");

		assertThat(page.getBody().getTextContent())
			.doesNotContain("No employee");
		HtmlTable table = page.getHtmlElementById("employee_table");
		assertThat(removeWindowsCR(table.asText()))
			.isEqualTo(
				"ID	Name	Salary\n" + 
				"1	test1	1000	Edit\n" + 
				"2	test2	2000	Edit"
			);
		page.getAnchorByHref("/edit/1");
		page.getAnchorByHref("/edit/2");
	}

	@Test
	public void testEditNonExistentEmployee() throws Exception {
		when(employeeService.getEmployeeById(1L))
			.thenReturn(null);

		HtmlPage page = this.webClient.getPage("/edit/1");

		assertThat(page.getBody().getTextContent())
			.contains("No employee found with id: 1");
	}

	@Test
	public void testEditExistentEmployee() throws Exception {
		when(employeeService.getEmployeeById(1))
			.thenReturn(new Employee(1L, "original name", 1000));

		HtmlPage page = this.webClient.getPage("/edit/1");

		// Get the form that we are dealing with
		final HtmlForm form = page.getFormByName("employee_form");
		// make sure the fields are filled with the correct values
		// and then change their values
		form.getInputByValue("original name").setValueAttribute("modified name");
		form.getInputByValue("1000").setValueAttribute("2000");

		// Now submit the form by clicking the button and get back the second page.
		form.getButtonByName("btn_submit").click();

		// verify that the modified employee has been updated through the service
		// using the values entered in the form
		verify(employeeService)
			.updateEmployeeById(1L, new Employee(1L, "modified name", 2000));
	}

	@Test
	public void testEditNewEmployee() throws Exception {
		HtmlPage page = this.webClient.getPage("/new");

		// Get the form that we are dealing with
		final HtmlForm form = page.getFormByName("employee_form");
		// retrieve fields by their names and change their values
		form.getInputByName("name").setValueAttribute("new name");
		form.getInputByName("salary").setValueAttribute("1000");

		// Now submit the form by clicking the button and get back the second page.
		form.getButtonByName("btn_submit").click();

		// verify that the employee has been inserted through the service
		// using the values entered in the form (note the id must be null)
		verify(employeeService)
			.insertNewEmployee(new Employee(null, "new name", 1000));
	}

	private String removeWindowsCR(String s) {
		return s.replaceAll("\r", "");
	}
}