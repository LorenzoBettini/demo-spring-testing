package com.examples.spring.demo.e2e.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.examples.spring.demo.controllers.webdriver.pages.AbstractPage;
import com.examples.spring.demo.controllers.webdriver.pages.EditPage;
import com.examples.spring.demo.controllers.webdriver.pages.HomePage;
import com.examples.spring.demo.model.Employee;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EmployeeWebE2ECucumberSteps {

	private RestTemplate restTemplate;

	private WebDriver webDriver;

	private static int port =
		Integer.parseInt(System.getProperty("server.port", "8080"));

	private static String url = "http://localhost:" + port;

	private HomePage homePage;

	private EditPage editPage;

	private AbstractPage redirectedPage;

	private List<Employee> savedEmployees;

	private static final Logger LOGGER = Logger.getLogger(EmployeeWebE2ECucumberSteps.class);

	private static final String API = "/api";

	private static final String GETALL_ENDPOINT = url + API + "/employees";

	private static final String SAVE_ENDPOINT = url + API + "/employees/new";

	private static final String DELETE_ENDPOINT = url + API + "/employees/delete";

	/**
	 * NOTE: it's {@link cucumber.api.java.Before}, NOT {@link org.junit.Before},
	 * which is not processed by Cucumber.
	 */
	@Before
	public void setup() {
		AbstractPage.port = port;
		savedEmployees = new ArrayList<>();
		restTemplate = new RestTemplate();
		webDriver = new ChromeDriver();
		LOGGER.info("Port set: " + port);
		LOGGER.info("URL: " + url);
	}

	/**
	 * NOTE: it's {@link cucumber.api.java.After}, NOT {@link org.junit.After},
	 * which is not processed by Cucumber.
	 */
	@After
	public void tearDown() {
		webDriver.quit();
	}

	private void logAllEmployees() {
		LOGGER.info("All employees: " + restTemplate.getForObject(GETALL_ENDPOINT, String.class));
	}

	@Given("^The database is empty$")
	public void the_database_is_empty() throws Throwable {
		getAllEmployees()
			.stream()
			.forEach(e -> deleteEmployee(e.getId()));
		logAllEmployees();
	}

	@When("^The User is on Home Page$")
	public void theUserIsOnHomePage() throws Throwable {
		homePage = HomePage.to(webDriver);
	}

	@Then("^A message \"([^\"]*)\" must be shown$")
	public void aMessageMustBeShown(String expectedMessage) throws Throwable {
		assertThat(homePage.getBody()).contains(expectedMessage);
	}

	@Given("^Some employees are in the database$")
	public void someEmployeesAreInTheDatabase() throws Throwable {
		postEmployee("test1", 1000);
		postEmployee("test2", 2000);
	}

	@Then("^A table must show the employees$")
	public void aTableMustShowTheEmployees() throws Throwable {
		assertThat(homePage.getEmployeeTableAsString()).contains(
			savedEmployees.get(0).getId()
			+ " test1 1000"
		);
		assertThat(homePage.getEmployeeTableAsString()).contains(
			savedEmployees.get(1).getId()
			+ " test2 2000"
		);
	}

	@And("^An Employee exists in the database$")
	public void anEmployeeExistsInTheDatabase() throws Throwable {
		postEmployee("test1", 1000);
	}

	@When("^The User navigates to \"([^\"]*)\" page with the id of the existing Employee$")
	public void theUserNavigatesToPageWithTheIdOfTheExistingEmployee(String arg1) throws Throwable {
		editPage = EditPage.to(webDriver,
			savedEmployees.get(0).getId());
	}

	@And("^Enters Employee name \"([^\"]*)\" and salary \"([^\"]*)\" and presses click$")
	public void entersEmployeeNameAndSalaryAndPressesClick(String name, String salary) throws Throwable {
		redirectedPage = editPage.submitForm(HomePage.class, name, Integer.parseInt(salary));
	}

	@Then("^The User is redirected to Home Page$")
	public void theUserIsRedirectedToHomePage() throws Throwable {
		assertThat(redirectedPage).isInstanceOf(HomePage.class);
	}

	@And("^A table must show the modified Employee \"([^\"]*)\"$")
	public void aTableMustShowTheModifiedEmployee(String expectedRepresentation) throws Throwable {
		assertThat(homePage.getEmployeeTableAsString()).
			contains(savedEmployees.get(0).getId() + " " + expectedRepresentation);
	}

	@When("^The User navigates to \"([^\"]*)\" page with id \"([^\"]*)\"$")
	public void theUserNavigatesToPageWithId(String arg, String id) throws Throwable {
		editPage = EditPage.to(webDriver, Long.parseLong(id));
	}

	@Then("^A message \"([^\"]*)\" \\+ \"([^\"]*)\" must be shown$")
	public void aMessageMustBeShown(String messagePart, String id) throws Throwable {
		assertThat(editPage.getBody())
			.contains(messagePart + id);
	}

	@When("^The User navigates to \"([^\"]*)\" page$")
	public void theUserNavigatesToPage(String newPage) throws Throwable {
		editPage = EditPage.to(webDriver);
	}

	@And("^A table must show the added Employee with name \"([^\"]*)\", salary \"([^\"]*)\" and id is positive$")
	public void aTableMustShowTheAddedEmployeeWithNameSalaryAndIdIsPositive(String name, String salary) throws Throwable {
		assertThat(homePage.getEmployeeTableAsString()).
			matches("(?s).*([1-9][0-9]*) " + name + " " + salary);
		// (?s) for "single line mode" makes the dot match all characters, including line breaks.
	}

	private void postEmployee(String name, int salary) throws JSONException {
		// just for demonstration, we manually build the
		// JSON body

		// create request body
		JSONObject request = new JSONObject();
		request.put("name", name);
		request.put("salary", salary);

		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

		Employee answer = restTemplate.postForObject(SAVE_ENDPOINT, entity, Employee.class);
		LOGGER.debug("answer for POST: " + answer);
		savedEmployees.add(answer);
	}

	private List<Employee> getAllEmployees() {
		ResponseEntity<Employee[]> response =
			restTemplate.getForEntity(GETALL_ENDPOINT, Employee[].class);
		return Arrays.asList(response.getBody());
	}

	private void deleteEmployee(long id) {
		restTemplate.delete(DELETE_ENDPOINT + "/" + id);
	}
}
