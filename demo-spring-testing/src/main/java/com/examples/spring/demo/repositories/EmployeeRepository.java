package com.examples.spring.demo.repositories;

import java.util.List;

import com.examples.spring.demo.model.Employee;

public interface EmployeeRepository {

	List<Employee> findAll();

	Employee findById(long id);

}
