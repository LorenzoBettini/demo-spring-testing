package com.examples.spring.demo.services;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.examples.spring.demo.model.Employee;

/**
 * Temporary implementation just to make some manual tests.
 * During the tests this will be mocked anyway.
 */
@Service
public class EmployeeService {

	private Map<Long, Employee> employees = new LinkedHashMap<>();

	public EmployeeService() {
		employees.put(1L, new Employee(1L, "John Doe", 1000));
		employees.put(2L, new Employee(2L, "John Smith", 2000));
	}

	public List<Employee> getAllEmployees() {
		return new LinkedList<>(employees.values());
	}

	public Employee getEmployeeById(long i) {
		return employees.get(i);
	}

	public Employee insertNewEmployee(Employee employee) {
		// dumb way of generating an automatic ID
		employee.setId(employees.size()+1L);
		employees.put(employee.getId(), employee);
		return employee;
	}

	public Employee updateEmployeeById(long id, Employee replacement) {
		replacement.setId(id);
		employees.put(replacement.getId(), replacement);
		return replacement;
	}

}
