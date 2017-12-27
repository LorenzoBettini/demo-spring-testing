package com.examples.spring.demo.e2e;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * Executes the tests against a running Spring Boot application; if you run this
 * as test from Eclipse, make sure you first manually run the Spring Boot
 * application.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/bdd/resources")
public class EmployeeWebE2E {

	@BeforeClass
	public static void setupClass() {
		ChromeDriverManager.getInstance()
			.setup();
	}
}
