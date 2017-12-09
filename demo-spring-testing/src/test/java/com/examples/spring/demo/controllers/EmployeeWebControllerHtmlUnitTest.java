package com.examples.spring.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
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
					new Employee(1, "test1", 1000),
					new Employee(2, "test2", 2000)));
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
}