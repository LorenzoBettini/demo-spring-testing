package com.examples.spring.demo.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

public class EmployeeControllerRestAssuredTest {

	private EmployeeService employeeService;

	@Before
	public void setup() {
		employeeService = mock(EmployeeService.class);
		standaloneSetup(new EmployeeRestController(employeeService));
	}

	@Test
	public void testAllEmployees() throws Exception {
		// use the standard when().thenReturn() of Mockito
		// to avoid conflicts with REST Assured given().when().then
		when(employeeService.getAllEmployees()).
			thenReturn(Arrays.asList(
				new Employee(1, "first", 1000),
				new Employee(2, "second", 5000)
			));

		given().
		when().
			get("/api/employees").
		then().
			statusCode(200).
			assertThat().
			body(
				"id[0]", equalTo(1),
				"name[0]", equalTo("first"),
				"salary[0]", equalTo(1000),
				"id[1]", equalTo(2),
				"name[1]", equalTo("second"),
				"salary[1]", equalTo(5000)
			);

		verify(employeeService, times(1)).getAllEmployees();
	}

	@Test
	public void testFindByIdWithExistingEmployee() throws Exception {
		when(employeeService.getEmployeeById(1)).
			thenReturn(new Employee(1, "first", 1000));

		given().
		when().
			get("/api/employees/1").
		then().
			statusCode(200).
			assertThat().
			body(
				"id", equalTo(1),
				"name", equalTo("first"),
				"salary", equalTo(1000)
			);

		verify(employeeService, times(1)).getEmployeeById(1);
	}

	// other tests similar to EmployeeRestControllerTest

}
