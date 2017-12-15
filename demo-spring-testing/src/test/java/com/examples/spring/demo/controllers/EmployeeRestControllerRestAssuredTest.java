package com.examples.spring.demo.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

public class EmployeeRestControllerRestAssuredTest {

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
				new Employee(1L, "first", 1000),
				new Employee(2L, "second", 5000)
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
			thenReturn(new Employee(1L, "first", 1000));

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

	@Test
	public void testFindByIdWithNonExistingEmployee() throws Exception {
		given().
		when().
			get("/api/employees/100").
		then().
			statusCode(200).
			contentType(isEmptyOrNullString());

		verify(employeeService, times(1)).getEmployeeById(100);
	}

	@Test
	public void testNewEmployee() throws Exception {
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(new Employee(null, "test", 1000)).
		when().
			post("/api/employees/new").
		then().
			statusCode(200);

		verify(employeeService, times(1)).
			saveEmployee(new Employee(null, "test", 1000));
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		when(employeeService.getEmployeeById(1)).
			thenReturn(new Employee(1L, "first", 100));
		Employee updated = new Employee(1L, "test", 1000);

		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(updated).
		when().
			put("/api/employees/update/1").
		then().
			statusCode(200);

		verify(employeeService, times(1)).
			saveEmployee(updated);
	}

	@Test
	public void testUpdateEmployeeWithFakeId() throws Exception {
		// although we pass an Employee in the body with id 100...
		Employee updated = new Employee(100L, "test", 1000);
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(updated).
		when().
			// the id specified in the URL...
			put("/api/employees/update/1").
		then().
			statusCode(200);

		// has the precedence
		verify(employeeService, times(1)).
			saveEmployee(new Employee(1L, "test", 1000));
	}

	@Test
	public void testDeleteEmployee() throws Exception {
		given().
		when().
			delete("/api/employees/delete/1").
		then().
			statusCode(200);

		verify(employeeService, times(1)).
			delete(1L);
	}
}
