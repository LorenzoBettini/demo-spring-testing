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
}
