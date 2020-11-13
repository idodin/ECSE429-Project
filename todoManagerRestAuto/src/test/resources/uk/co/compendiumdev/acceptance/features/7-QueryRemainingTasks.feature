Feature: querry all undone tasks

  As a student, I query the incomplete tasks for a class I am taking, to
  help manage my time.


  Scenario: query all imcomplete task in a class projects(normal flow)
  Given there is a ECSE429 project
  And there is a to do projectA with doneStatus false
  And there is a to do projectB with doneStatus true
  When I querry the incomplete task of ECSE429 project
  Then projectA is successfuly returned


  Scenario: query all imcomplete task in a class projects with all done task(alternate flow)
  Given there is a ECSE429 project
  And there is a to do projectA with doneStatus true
  And there is a to do projectB with doneStatus true
  When I querry the incomplete task of ECSE429 project
  Then empty to do list is successfuly returned

  Scenario: query tasks from project that doesn't exist (Error flow)
  Given there is no ECSE429 project
  When I querry the imcomplete ECSE429 prject tasks
  Then Bad request
