package com.examples.spring.demo.controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.examples.spring.demo.services.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmployeeWebController.class)
public class EmployeeWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmployeeService employeeService;

	@Test
	public void testStatus200() throws Exception {
		mvc.perform(get("/"))
			.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testReturnHomeView() throws Exception {
		assertViewName(mvc.perform(get("/"))
			.andReturn()
			.getModelAndView(), "index");
	}

	@Test
	public void testEmptyEmployeeList() throws Exception {
		assertViewName(mvc.perform(get("/"))
			.andReturn()
			.getModelAndView(), "index");
		verify(employeeService).getAllEmployees();
	}
}