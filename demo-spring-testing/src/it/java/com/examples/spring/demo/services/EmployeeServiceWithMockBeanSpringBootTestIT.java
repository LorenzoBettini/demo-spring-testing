package com.examples.spring.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;
import com.examples.spring.demo.services.EmployeeService;

/**
 * Uses Spring dependency injection, to inject (in the Spring sense) mocked
 * beans (that can then be stubbed with Mockito); {@link SpringBootTest}
 * loads the complete application context for dependency injection.
 * 
 * Just a demonstration of {@link SpringBootTest}; it doesn't make much sense
 * to use it with mocking.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceWithMockBeanSpringBootTestIT {

	@MockBean
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeService employeeService;

	@Test
	public void test_getMaxSalariedEmployee_withNoEmployees() {
		assertNull(employeeService.getMaxSalariedEmployee());
		verify(employeeRepository).findAll();
	}

	@Test
	public void test_getMaxSalariedEmployee_withEmployees() {
		given(employeeRepository.findAll()).
			willReturn(Arrays.asList(
				new Employee(1L, "first", 1000),
				new Employee(2L, "second", 5000),
				new Employee(3L, "third", 2000)
			));
		assertThat(employeeService.getMaxSalariedEmployee().getName()).
			isEqualTo("second");
		verify(employeeRepository).findAll();
	}
}
