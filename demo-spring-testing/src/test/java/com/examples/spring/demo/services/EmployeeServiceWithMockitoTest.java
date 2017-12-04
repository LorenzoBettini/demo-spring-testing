package com.examples.spring.demo.services;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

/**
 * Completely rely on Mockito for injecting mocked implementations, No
 * Spring dependency injection is used here.
 */
@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceWithMockitoTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
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
