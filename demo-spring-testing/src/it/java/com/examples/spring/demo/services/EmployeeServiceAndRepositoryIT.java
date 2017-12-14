package com.examples.spring.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceAndRepositoryIT {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeService employeeService;

	@Test
	public void test_getMaxSalariedEmployee_withNoEmployees() {
		employeeRepository.deleteAll();
		assertNull(employeeService.getMaxSalariedEmployee());
	}

	@Test
	public void test_getMaxSalariedEmployee_withEmployees() {
		employeeRepository.save(
			Arrays.asList(
				new Employee(1L, "first", 1000),
				new Employee(2L, "second", 5000),
				new Employee(3L, "third", 2000)
			));
		employeeRepository.flush();
		assertThat(employeeService.getMaxSalariedEmployee().getName()).
			isEqualTo("second");
	}

	// other integration tests not implemented
}
