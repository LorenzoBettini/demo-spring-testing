package com.examples.spring.demo.controllers;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmployeeRestController.class)
public class EmployeeRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmployeeService employeeService;

	@Test
	public void testAllEmployeesEmpty() throws Exception {
		this.mvc.perform(get("/api/employees")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
				// the above checks that the content is an empty JSON list
	}

	@Test
	public void testAllEmployeesNotEmpty() throws Exception {
		when(employeeService.getAllEmployees()).
			thenReturn(asList(
				new Employee(1L, "first", 1000),
				new Employee(2L, "second", 5000)
			));
		this.mvc.perform(get("/api/employees")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("first")))
				.andExpect(jsonPath("$[0].salary", is(1000)))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("second")))
				.andExpect(jsonPath("$[1].salary", is(5000)));
	}

	@Test
	public void testOneEmployeeByIdWithExistingEmployee() throws Exception {
		when(employeeService.getEmployeeById(anyLong())).
			thenReturn(new Employee(1L, "first", 1000));
		this.mvc.perform(get("/api/employees/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("first")))
				.andExpect(jsonPath("$.salary", is(1000)));
	}

	@Test
	public void testOneEmployeeByIdWithNotFoundEmployee() throws Exception {
		when(employeeService.getEmployeeById(anyLong())).
			thenReturn(null);
		this.mvc.perform(get("/api/employees/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
				// the above checks that the content is empty
				// which is different from an empty JSON list
	}
}