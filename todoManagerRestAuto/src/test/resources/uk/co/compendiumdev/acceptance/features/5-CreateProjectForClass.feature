# SHOULD BE ABOUT CREATING A PROJECT NOT A TASK

Feature: Create Project for Class
  As a student, I create a to do list for a new class I am taking, so I can manage course work.


  Scenario Outline: The user successfully adds a task to a given class (Normal Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And  Projects exist for the following courses:
      | Projects |
      | ECSE_429 |
      | ECSE_420 |
      | COMP_360 |
    When I create a task "<todo>"
    And  I add the task "<todo>" to  "<project>"
    Then Project "<project>" should contain task "<todo>"
    And  task "<todo>" should be in "<project>"

    Examples:
      | todo                    | Projects |
      | to do my project part A | ECSE_429 |
      | to do my project part B | ECSE_420 |
      | to do the report        | COMP_360 |


  Scenario Outline: The user successfully creates a class project and add a task to that project (alternate flow)
    Given there is no project related to a new class
    When  I create a project "<project>"
    And   I create a task "<todo>"
    And   I add the task "<todo>" to "<project>"
    Then  the project"<project>" should contain the task "<todo>"
    And   task "<todo>" should be in "<project>"

    Examples:
      | todo                    | Projects |
      | to do my project part A | ECSE_429 |
      | to do my project part B | ECSE_420 |
      | to do the report        | COMP_360 |


  Scenario Outline: the user add a task to a non-existing project related to a specific class (Error flow)
    Given An empty project  "<existing_project_1>" exists
    And   An empty project  "<existing_project_2>" exists
    When  I create a task "<todo>"
    And   I add task "<todo>"  to "<new_project>" project
    Then  I should receive an error informing me that "<new_project>" project does not exist

  Examples:
    | existing_project_1 | existing_project_2 | new_project   | todo        |
    | ECSE_223           | FACC_300           | COMP_350      | assignment  |
    | MATH_240           | ECSE_205           | FACC_400      | lab         |
