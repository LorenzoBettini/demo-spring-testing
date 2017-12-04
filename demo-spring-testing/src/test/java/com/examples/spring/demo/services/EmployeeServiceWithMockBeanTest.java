package com.examples.spring.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

/**
 * Uses Spring dependency injection, to inject (in the Spring sense) mocked
 * beans (that can then be stubbed with Mockito); {@link ContextConfiguration}
 * specify the class to use ({@link EmployeeService}) to get the application
 * context for dependency injection.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=EmployeeService.class)
public class EmployeeServiceWithMockBeanTest {

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
				new Employee(1, "first", 1000),
				new Employee(2, "second", 5000),
				new Employee(3, "third", 2000)
			));
		assertThat(employeeService.getMaxSalariedEmployee().getName()).
			isEqualTo("second");
		verify(employeeRepository).findAll();
	}
}
