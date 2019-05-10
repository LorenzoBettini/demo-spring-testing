package com.examples.spring.demo;

import static org.junit.Assert.*;

import org.junit.Test;

import com.examples.spring.demo.model.Employee;

/**
 * A temporary test just to make sure jacoco.xml is generated and a report can
 * be sent to Coveralls.
 * 
 * This must be removed when we have at least a test executed by surefire.
 */
public class TemporaryTest {

	@Test
	public void test() {
		assertNotNull(new Employee(1L, "an employee", 0));
	}

}
