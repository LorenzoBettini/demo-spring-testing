package com.examples.spring.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

@Controller
public class EmployeeWebController {

	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String EMPLOYEE_ATTRIBUTE = "employee";
	private static final String EMPLOYEES_ATTRIBUTE = "employees";

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/")
	public String index(Model model) {
		List<Employee> allEmployees = employeeService.getAllEmployees();
		model.addAttribute(EMPLOYEES_ATTRIBUTE, allEmployees);
		model.addAttribute(MESSAGE_ATTRIBUTE,
			allEmployees.isEmpty() ? "No employee" :  "");
		return "index";
	}

	@GetMapping("/edit/{id}")
	public String editEmployee(@PathVariable long id, Model model) {
		Employee employeeById = employeeService.getEmployeeById(id);
		model.addAttribute(EMPLOYEE_ATTRIBUTE, employeeById);
		model.addAttribute(MESSAGE_ATTRIBUTE,
			employeeById == null ? "No employee found with id: " + id : "");
		return "edit";
	}

	@GetMapping("/new")
	public String newEmployee(Model model) {
		model.addAttribute(EMPLOYEE_ATTRIBUTE, new Employee());
		model.addAttribute(MESSAGE_ATTRIBUTE, "");
		return "edit";
	}

	@PostMapping("/save")
	public String saveEmployee(Employee employee) {
		final Long id = employee.getId();
		if (id == null) {
			employeeService.insertNewEmployee(employee);
		} else {
			employeeService.updateEmployeeById(id, employee);
		}
		return "redirect:/";
	}
}
