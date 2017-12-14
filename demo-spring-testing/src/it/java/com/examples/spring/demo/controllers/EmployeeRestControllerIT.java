package com.examples.spring.demo.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

/**
 * Tests the rest controller when running in a real web container, manually
 * calling the {@link EmployeeRepository}.
 * 
 * In tests you can't rely on fixed identifiers: use the ones returned by the
 * repository after saving (automatically generated)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeRestControllerIT {

	@Autowired
	private EmployeeRepository employeeRepository;

	@LocalServerPort
	private int port;

	private String url;

	static final Logger LOGGER = Logger.getLogger(EmployeeRestControllerIT.class);

	@Before
	public void setup() {
		url = "http://localhost:" + port;
		employeeRepository.deleteAll();
		employeeRepository.flush();
		LOGGER.info("employees: " + employeeRepository.findAll());
	}

	@Test
	public void testAllEmployees() throws Exception {
		List<Employee> saved = employeeRepository.save(Arrays.asList(
				new Employee(null, "first", 1000),
				new Employee(null, "second", 5000)
			));
		employeeRepository.flush();
		LOGGER.info("employees: " + employeeRepository.findAll());

		given().
		when().
			get(url + "/api/employees").
		then().
			statusCode(200).
			assertThat().
			body(
				"name[0]", equalTo("first"),
				"salary[0]", equalTo(1000),
				"name[1]", equalTo("second"),
				"salary[1]", equalTo(5000),
				// check that the list of integer ids in JSON response
				// is equal to to the one of saved employees
				"id",
					equalTo(
						saved.stream()
							.map(e -> e.getId().intValue())
							.collect(Collectors.toList()))
			);
	}

	@Test
	public void testFindByIdWithExistingEmployee() throws Exception {
		Employee saved = employeeRepository.save(
			new Employee(null, "first", 1000));
		employeeRepository.flush();
		LOGGER.info("employees: " + employeeRepository.findAll());

		given().
		when().
			get(url + "/api/employees/" + saved.getId()).
		then().
			statusCode(200).
			assertThat().
			body(
				"id", equalTo(saved.getId().intValue()),
				"name", equalTo("first"),
				"salary", equalTo(1000)
			);
	}

	@Test
	public void testFindByIdWithNonExistingEmployee() throws Exception {
		LOGGER.info("employees: " + employeeRepository.findAll());

		given().
		when().
			get(url + "/api/employees/100").
		then().
			statusCode(200).
			contentType(isEmptyOrNullString());
	}
}
