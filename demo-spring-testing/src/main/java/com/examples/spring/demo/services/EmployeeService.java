package com.examples.spring.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.examples.spring.demo.model.Employee;

@Service
public class EmployeeService {

	public List<Employee> getAllEmployees() {
		return new ArrayList<>();
	}

	public Employee getEmployeeById(long i) {
		return null;
	}

	public void saveEmployee(Employee employee) {
		
	}

}
