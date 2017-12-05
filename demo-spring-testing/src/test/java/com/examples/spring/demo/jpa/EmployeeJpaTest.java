package com.examples.spring.demo.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
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
		Employee saved = entityManager.persistFlushFind(new Employee(null, "test", 1000));
		assertThat(saved.getName()).isEqualTo("test");
		assertThat(saved.getSalary()).isEqualTo(1000);
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isGreaterThan(0);
		// just to see the generated identifier
		LoggerFactory
			.getLogger(EmployeeJpaTest.class)
			.info("Saved: " + saved.toString());
	}

}
