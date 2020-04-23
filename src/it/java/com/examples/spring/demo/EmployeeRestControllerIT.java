package com.examples.spring.demo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.repositories.EmployeeRepository;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * Some examples of tests for the rest controller when running in a real web
 * container, manually using the {@link EmployeeRepository}.
 * 
 * The web server is started on a random port, which can be retrieved by
 * injecting in the test a {@link LocalServerPort}.
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

	@Before
	public void setup() {
		RestAssured.port = port;
		// always start with an empty database
		employeeRepository.deleteAll();
		employeeRepository.flush();
	}

	@Test
	public void testNewEmployee() throws Exception {
		// create an employee with POST
		Response response = given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(new Employee(null, "new employee", 1000)).
		when().
			post("/api/employees/new");

		Employee saved = response.getBody().as(Employee.class);

		// read it back from the repository
		assertThat(employeeRepository.findById(saved.getId()).get())
			.isEqualTo(saved);
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		// create an employee with the repository
		Employee saved = employeeRepository
			.save(new Employee(null, "original name", 1000));

		// modify it with PUT
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(new Employee(null, "modified name", 2000)).
		when().
			put("/api/employees/update/" + saved.getId()).
		then().
			statusCode(200).
			body(
				// in the JSON response the id is an integer
				"id", equalTo(saved.getId().intValue()),
				"name", equalTo("modified name"),
				"salary", equalTo(2000)
			);
	}
}