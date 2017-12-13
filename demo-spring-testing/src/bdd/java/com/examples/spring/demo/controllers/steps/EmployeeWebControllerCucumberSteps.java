package com.examples.spring.demo.controllers.steps;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.examples.spring.demo.controllers.webdriver.pages.HomePage;
import com.examples.spring.demo.model.Employee;
import com.examples.spring.demo.services.EmployeeService;

import cucumber.api.java.Before;
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
		employeeService.saveEmployee(new Employee(1L, "test1", 1000));
		employeeService.saveEmployee(new Employee(2L, "test2", 2000));
	}

	@Then("^A table must show the employees$")
	public void aTableMustShowTheEmployees() throws Throwable {
		assertThat(homePage.getEmployeeTableAsString()).isEqualTo(
			"ID Name Salary 1 test1 1000 2 test2 2000"
		);
	}
}
