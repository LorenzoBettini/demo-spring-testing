package com.examples.spring.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examples.spring.demo.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByName(String string);

	List<Employee> findByNameAndSalary(String string, long i);

	List<Employee> findByNameOrSalary(String string, long l);

}
