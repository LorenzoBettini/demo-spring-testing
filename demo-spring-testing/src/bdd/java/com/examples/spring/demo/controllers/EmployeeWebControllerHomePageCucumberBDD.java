package com.examples.spring.demo.controllers;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/bdd/resources")
public class EmployeeWebControllerHomePageCucumberBDD {

}
