package com.examples.spring.demo.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;

@DataJpaTest
@RunWith(SpringRunner.class)
public class EmployeeJpaTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testJpaMapping() {
		Employee saved =
			entityManager.
				persistFlushFind(new Employee(null, "test", 1000));
	}

}