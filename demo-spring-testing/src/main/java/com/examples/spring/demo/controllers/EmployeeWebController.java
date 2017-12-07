package com.examples.spring.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

@Controller
public class EmployeeWebController {

	private EmployeeService employeeService;

	@Autowired
	public EmployeeWebController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/")
	public String index() {
		List<Employee> employees = employeeService.getAllEmployees();
		return "index";
	}
}
