package com.examples.spring.demo.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

/**
 * Uses Spring dependency injection, to inject (in the Spring sense) mocked
 * beans (that can then be stubbed with Mockito); {@link SpringBootTest} loads
 * the complete application context for dependency injection.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceWithMockBeanSpringBootTest {

	@MockBean
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeService employeeService;

	@Test
	public void test_getAllEmployees() {
		Employee employee1 = new Employee(1L, "first", 1000);
		Employee employee2 = new Employee(2L, "second", 5000);
		when(employeeRepository.findAll()).
			thenReturn(asList(employee1, employee2));
		assertThat(employeeService.getAllEmployees()).
			containsExactly(employee1, employee2);
	}

	@Test
	public void test_getEmployeeById_found() {
		Employee employee = new Employee(1L, "employee", 1000);
		when(employeeRepository.findById(1L))
			.thenReturn(Optional.of(employee));
		assertThat(employeeService.getEmployeeById(1))
			.isSameAs(employee);
	}

	@Test
	public void test_getEmployeeById_notFound() {
		when(employeeRepository.findById(anyLong()))
			.thenReturn(Optional.empty());
		assertThat(employeeService.getEmployeeById(1))
			.isNull();
	}

	@Test
	public void test_insertNewEmployee_setsIdToNull_and_returnsSavedEmployee() {
		Employee toSave = spy(new Employee(99L, "", 0));
		Employee saved = new Employee(1L, "saved", 1000);

		when(employeeRepository.save(any(Employee.class)))
			.thenReturn(saved);

		Employee result = employeeService.insertNewEmployee(toSave);

		assertThat(result).isSameAs(saved);

		InOrder inOrder = inOrder(toSave, employeeRepository);
		inOrder.verify(toSave).setId(null);
		inOrder.verify(employeeRepository).save(toSave);
	}

	@Test
	public void test_updateEmployeeById_setsIdToArgument_and_returnsSavedEmployee() {
		Employee replacement = spy(new Employee(null, "employee", 0));
		Employee replaced = new Employee(1L, "saved", 1000);

		when(employeeRepository.save(any(Employee.class)))
			.thenReturn(replaced);

		Employee result = employeeService.updateEmployeeById(1L, replacement);

		assertThat(result).isSameAs(replaced);

		InOrder inOrder = inOrder(replacement, employeeRepository);
		inOrder.verify(replacement).setId(1L);
		inOrder.verify(employeeRepository).save(replacement);
	}
}
