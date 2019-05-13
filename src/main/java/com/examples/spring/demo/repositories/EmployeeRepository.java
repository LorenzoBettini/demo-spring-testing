package com.examples.spring.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examples.spring.demo.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
