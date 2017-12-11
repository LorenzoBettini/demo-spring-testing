package com.examples.spring.demo.services;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.examples.spring.demo.model.Employee;

/**
 * Temporary implementation just to make some manual checks.
 * During the tests this will be mocked anyway.
 */
@Service
public class EmployeeService {

	private Map<Long, Employee> employees = new LinkedHashMap<>();

	public EmployeeService() {
		employees.put(1L, new Employee(1L, "John Doe", 1000));
	}

	public List<Employee> getAllEmployees() {
		return new LinkedList<>(employees.values());
	}

	public Employee getEmployeeById(long i) {
		return employees.get(i);
	}

	public void saveEmployee(Employee employee) {
		// simulates the automatic assignment of id if null
		// just for experimenting with the web interface
		if (employee.getId() == null) {
			employee.setId(employees.size()+1L);
		}
		employees.put(employee.getId(), employee);
	}

}
