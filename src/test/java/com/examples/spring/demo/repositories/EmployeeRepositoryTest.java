package com.examples.spring.demo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;

@DataJpaTest
@RunWith(SpringRunner.class)
public class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void firstLearningTest() {
		Employee employee = new Employee(null, "test", 1000);
		Employee saved = repository.save(employee);
		Collection<Employee> employees = repository.findAll();
		assertThat(employees).containsExactly(saved);
	}

	@Test
	public void secondLearningTest() {
		// we can add a record with the TestEntityManager
		// and read it back with the Repository
		Employee employee = new Employee(null, "test", 1000);
		Employee saved = entityManager.persistFlushFind(employee);
		Collection<Employee> employees = repository.findAll();
		assertThat(employees).containsExactly(saved);
	}

	@Test
	public void test_findByEmployeeName() {
		Employee saved = entityManager.
			persistFlushFind(new Employee(null, "test", 1000));
		Employee found = repository.findByName("test");
		assertThat(found).isEqualTo(saved);
	}
}