package com.examples.spring.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.examples.spring.demo.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	Employee findByName(String string);

	List<Employee> findByNameAndSalary(String string, long i);

	List<Employee> findByNameOrSalary(String string, long l);

	@Query("Select e from Employee e where e.salary < :threshold")
	List<Employee> findAllEmployeesWithLowSalary(@Param("threshold") long threshold);

}
