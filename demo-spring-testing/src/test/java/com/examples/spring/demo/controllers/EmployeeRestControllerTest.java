package com.examples.spring.demo.controllers;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmployeeRestController.class)
public class EmployeeRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	public void testAllEmployees() throws Exception {
		given(employeeRepository.findAll()).
			willReturn(Arrays.asList(
				new Employee(1, "first", 1000),
				new Employee(2, "second", 5000)
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
		verify(employeeRepository, times(1)).findAll();
	}

	@Test
	public void testFindByIdWithExistingEmployee() throws Exception {
		given(employeeRepository.findById(1)).
			willReturn(new Employee(1, "first", 1000));
		this.mvc.perform(get("/api/employees/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("first")))
				.andExpect(jsonPath("$.salary", is(1000)));
		verify(employeeRepository, times(1)).findById(1);
	}

	@Test
	public void testFindByIdWithNotFoundEmployee() throws Exception {
		this.mvc.perform(get("/api/employees/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
			.andExpect(content().string(containsString("")));
		verify(employeeRepository, times(1)).findById(1);
	}

}
