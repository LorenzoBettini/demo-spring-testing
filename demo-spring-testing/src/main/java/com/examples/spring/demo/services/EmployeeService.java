package com.examples.spring.demo.services;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

@Service
public class EmployeeService {

	private EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public Employee getMaxSalariedEmployee() {
		List<Employee> employees = employeeRepository.findAll();
		return employees.stream()
				.max(Comparator.comparing(Employee::getSalary))
				.orElse(null);
	}

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Employee getEmployeeById(long id) {
		return employeeRepository.findOne(id);
	}

	public void deleteAll() {
		employeeRepository.deleteAll();
	}

	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}
}
