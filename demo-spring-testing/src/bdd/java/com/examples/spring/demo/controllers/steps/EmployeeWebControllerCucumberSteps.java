package com.examples.spring.demo.controllers.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EmployeeWebControllerCucumberSteps {

	@Given("^The database is empty$")
	public void the_database_is_empty() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@When("^The User is on Home Page$")
	public void theUserIsOnHomePage() throws Throwable {
		throw new PendingException();
	}

	@Then("^A message \"([^\"]*)\" must be shown$")
	public void aMessageMustBeShown(String arg1) throws Throwable {
		throw new PendingException();
	}
}
