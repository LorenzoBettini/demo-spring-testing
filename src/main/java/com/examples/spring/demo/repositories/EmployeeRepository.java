package com.examples.spring.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.examples.spring.demo.model.Employee;

/**
 * Temporary fake implementation of repository.
 */
@Repository
public class EmployeeRepository {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<Employee> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Optional<Employee> findById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Employee save(Employee employee) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
