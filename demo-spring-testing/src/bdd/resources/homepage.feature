Feature: Employees listing and editing

Scenario:  List no employees
  Given The database is empty
  When The User is on Home Page
  Then A message "No employee" must be shown