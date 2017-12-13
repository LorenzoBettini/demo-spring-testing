Feature: Employees editing

Scenario: Add a new Employee
  Given The User is on Home Page
  When The User navigates to "save" page
  And Enters Employee name "a new employee" and salary "1000" and presses click
  Then The User is redirected to Home Page
  And A table must show the added Employee with name "a new employee", salary "1000" and id is positive