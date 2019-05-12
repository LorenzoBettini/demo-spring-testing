package com.examples.spring.demo.controllers;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeRestControllerRestAssuredTest {

	@Mock
	private EmployeeService employeeService;

	@InjectMocks
	private EmployeeRestController employeeRestController;

	@Before
	public void setup() {
		RestAssuredMockMvc
			.standaloneSetup(employeeRestController);
	}

	@Test
	public void testFindByIdWithExistingEmployee() throws Exception {
		// this test is also in EmployeeRestControllerTest
		// this is the version with REST Assured
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
	public void testPostEmployee() throws Exception {
		Employee requestBodyEmployee = new Employee(null, "test", 1000);
		when(employeeService.insertNewEmployee(requestBodyEmployee)).
			thenReturn(new Employee(1L, "test", 1000));

		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(requestBodyEmployee).
		when().
			post("/api/employees/new").
		then().
			statusCode(200).
			body(
				"id", equalTo(1),
				"name", equalTo("test"),
				"salary", equalTo(1000)
			);
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		Employee requestBodyEmployee = new Employee(null, "test", 1000);
		when(employeeService.updateEmployeeById(1L, requestBodyEmployee)).
			thenReturn(new Employee(1L, "test", 1000));

		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(requestBodyEmployee).
		when().
			put("/api/employees/update/1").
		then().
			statusCode(200).
			body(
				"id", equalTo(1),
				"name", equalTo("test"),
				"salary", equalTo(1000)
			);
	}
}
