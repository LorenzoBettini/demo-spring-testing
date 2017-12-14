Feature: Employees listing and editing

Scenario: List no employees
  Given The database is empty
  When The User is on Home Page
  Then A message "No employee" must be shown

Scenario: List current employees
  Given Some employees are in the database
  When The User is on Home Page
  Then A table must show the employees