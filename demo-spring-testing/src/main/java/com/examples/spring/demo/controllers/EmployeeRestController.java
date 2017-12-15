package com.examples.spring.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

	private EmployeeService employeeService;

	@Autowired
	public EmployeeRestController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/employees")
	public List<Employee> allEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/employees/{id}")
	public Employee oneEmployee(@PathVariable long id) {
		return employeeService.getEmployeeById(id);
	}

	@PostMapping("/employees/new")
	public Employee newEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}

	@PutMapping("/employees/update/{id}")
	public Employee updateEmployee(@PathVariable long id, @RequestBody Employee employee) {
		// make sure the id is set, using the passed parameter
		employee.setId(id);
		return employeeService.saveEmployee(employee);
	}

	@DeleteMapping("/employees/delete/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employeeService.delete(id);
	}
}
