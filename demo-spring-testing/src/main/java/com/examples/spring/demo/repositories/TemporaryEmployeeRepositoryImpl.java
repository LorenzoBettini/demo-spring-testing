package com.examples.spring.demo.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.examples.spring.demo.model.Employee;

/**
 * Temporary fake implementation of {@link EmployeeRepository} so that
 * the Spring Boot application can be started.
 */
@Repository
public class TemporaryEmployeeRepositoryImpl implements EmployeeRepository {

	@Override
	public List<Employee> findAll() {
		return null;
	}

}
