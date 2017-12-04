package com.examples.spring.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

@RestController
public class EmployeeRestController {

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/api/employees")
	public List<Employee> allEmployees() {
		return employeeRepository.findAll();
	}

	@GetMapping("/api/employees/{id}")
	public Employee oneEmployee(@PathVariable long id) {
		return employeeRepository.findById(id);
	}
}
