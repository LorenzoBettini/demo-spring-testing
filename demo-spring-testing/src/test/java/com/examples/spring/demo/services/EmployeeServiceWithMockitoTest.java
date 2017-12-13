package com.examples.spring.demo.services;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

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
		Employee maxSalariedEmployee = employeeService.getMaxSalariedEmployee();
		assertNull(maxSalariedEmployee);
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

	@Test
	public void test_getAllEmployees_withEmployees() {
		Employee employee1 = new Employee(1L, "first", 1000);
		Employee employee2 = new Employee(2L, "second", 5000);
		given(employeeRepository.findAll()).
			willReturn(Arrays.asList(
				employee1,
				employee2
			));
		assertThat(employeeService.getAllEmployees()).
			containsExactly(employee1, employee2);
		verify(employeeRepository).findAll();
	}

	@Test
	public void test_getAllEmployees_empty() {
		assertThat(employeeService.getAllEmployees()).
			isEmpty();
		verify(employeeRepository).findAll();
	}

	@Test
	public void test_getEmployeeById_found() {
		Employee employee = new Employee(1L, "employee", 1000);
		given(employeeRepository.findOne(1L))
			.willReturn(employee);
		assertThat(employeeService.getEmployeeById(1))
			.isSameAs(employee);
		verify(employeeRepository).findOne(1L);
	}

	@Test
	public void test_getEmployeeById_notFound() {
		assertThat(employeeService.getEmployeeById(1))
			.isNull();
		verify(employeeRepository).findOne(1L);
	}

	@Test
	public void test_deleteAll() {
		employeeService.deleteAll();
		verify(employeeRepository).deleteAll();
	}
}
