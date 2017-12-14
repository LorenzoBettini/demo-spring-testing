Feature: Employees editing

Scenario: Add a new Employee
  Given The User is on Home Page
  When The User navigates to "save" page
  And Enters Employee name "a new employee" and salary "1000" and presses click
  Then The User is redirected to Home Page
  And A table must show the added Employee with name "a new employee", salary "1000" and id is positive

Scenario: Edit a non existent Employee
  Given The User is on Home Page
  When The User navigates to "edit" page with id "-1"
  Then A message "No employee found with id: " + "-1" must be shown

Scenario: Edit an existent Employee
  Given The User is on Home Page
  And An Employee exists in the database
  When The User navigates to "edit" page with the id of the existing Employee
  And Enters Employee name "modified name" and salary "10000" and presses click
  Then The User is redirected to Home Page
  And A table must show the modified Employee "modified name 10000"