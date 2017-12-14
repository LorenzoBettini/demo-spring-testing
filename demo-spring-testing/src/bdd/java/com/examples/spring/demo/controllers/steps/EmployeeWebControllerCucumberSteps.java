package com.examples.spring.demo.controllers.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import com.examples.spring.demo.controllers.webdriver.pages.AbstractPage;
import com.examples.spring.demo.controllers.webdriver.pages.EditPage;
import com.examples.spring.demo.controllers.webdriver.pages.HomePage;
import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(loader = SpringBootContextLoader.class)
public class EmployeeWebControllerCucumberSteps {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private WebDriver webDriver;

	@LocalServerPort
	private int port;

	private HomePage homePage;

	private EditPage editPage;

	private AbstractPage redirectedPage;

	private List<Employee> savedEmployees;

	static final Logger LOGGER = Logger.getLogger(EmployeeWebControllerCucumberSteps.class);

	@TestConfiguration
	static class WebDriverConfiguration {
		@Bean
		public WebDriver getWebDriver() {
			return new HtmlUnitDriver();
		}
	}

	/**
	 * NOTE: it's {@link cucumber.api.java.Before}, NOT {@link org.junit.Before},
	 * which is not processed by Cucumber.
	 */
	@Before
	public void setup() {
		AbstractPage.port = port;
		savedEmployees = new ArrayList<>();
		LOGGER.info("Port set: " + port);
	}

	@Given("^The database is empty$")
	public void the_database_is_empty() throws Throwable {
		employeeService.deleteAll();
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
		savedEmployees.add(employeeService.saveEmployee(new Employee(null, "test1", 1000)));
		savedEmployees.add(employeeService.saveEmployee(new Employee(null, "test2", 2000)));
	}

	@Then("^A table must show the employees$")
	public void aTableMustShowTheEmployees() throws Throwable {
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary "
			+ savedEmployees.get(0).getId()
			+ " test1 1000 "
			+ savedEmployees.get(1).getId()
			+ " test2 2000"
		);
	}

	@When("^The User navigates to \"([^\"]*)\" page$")
	public void theUserNavigatesToPage(String newPage) throws Throwable {
		editPage = EditPage.to(webDriver);
	}

	@And("^Enters Employee name \"([^\"]*)\" and salary \"([^\"]*)\" and presses click$")
	public void entersEmployeeNameAndSalaryAndPressesClick(String name, String salary) throws Throwable {
		redirectedPage = editPage.submitForm(HomePage.class, name, Integer.parseInt(salary));
	}

	@Then("^The User is redirected to Home Page$")
	public void theUserIsRedirectedToHomePage() throws Throwable {
		assertThat(redirectedPage).isInstanceOf(HomePage.class);
	}

	@And("^A table must show the added Employee with name \"([^\"]*)\", salary \"([^\"]*)\" and id is positive$")
	public void aTableMustShowTheAddedEmployeeWithNameSalaryAndIdIsPositive(String name, String salary) throws Throwable {
		assertThat(homePage.getEmployeeTableAsString()).
			matches(".*([1-9][0-9]*) " + name + " " + salary);
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

	@And("^A table must show the modified Employee \"([^\"]*)\"$")
	public void aTableMustShowTheModifiedEmployee(String expectedRepresentation) throws Throwable {
		assertThat(homePage.getEmployeeTableAsString()).
			contains(savedEmployees.get(0).getId() + " " + expectedRepresentation);
	}

	@And("^An Employee exists in the database$")
	public void anEmployeeExistsInTheDatabase() throws Throwable {
		savedEmployees.add(employeeService.saveEmployee(new Employee(null, "test1", 1000)));
	}

	@When("^The User navigates to \"([^\"]*)\" page with the id of the existing Employee$")
	public void theUserNavigatesToPageWithTheIdOfTheExistingEmployee(String arg1) throws Throwable {
		editPage = EditPage.to(webDriver,
			savedEmployees.get(0).getId());
	}

}
