package com.examples.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;
import com.examples.spring.demo.services.EmployeeService;

/**
 * A possible integration test verifying that the service and repository
 * interact correctly.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(EmployeeService.class)
public class EmployeeServiceRepositoryIT {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Test
	public void testServiceCanInsertIntoRepository() {
		Employee saved = employeeService
			.insertNewEmployee(new Employee(null, "an employee", 1000));
		assertThat(employeeRepository.findById(saved.getId()))
			.isPresent();
	}

	@Test
	public void testServiceCanUpdateRepository() {
		Employee saved = employeeRepository
			.save(new Employee(null, "an employee", 1000));
		Employee modified = employeeService.
			updateEmployeeById(saved.getId(),
				new Employee(saved.getId(), "modified", 2000));
		assertThat(employeeRepository.findById(saved.getId()))
			.contains(modified);
	}
}
