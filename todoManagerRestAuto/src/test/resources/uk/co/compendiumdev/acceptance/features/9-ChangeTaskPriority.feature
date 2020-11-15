# TODO add checks for project existence
Feature: Change Task Priority
  As a student, I want to adjust the priority of a task, to help better manage my time.

  Background:
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And Projects exist for the following courses:
      | Projects  |
      | ECSE 429  |
      | ECSE 420  |
      | COMP 360  |
      | ECSE 223  |
      | ECSE 202  |
      | COMP 251  |
    And The following tasks exist with their respective statuses, courses and priority levels:
    Given The following tasks exist with their respective statuses, courses and priority levels:
      | task                | completed           | course         | priority    |
      | task_A              | false               | ECSE 429       | HIGH        |
      | task_B              | false               | ECSE 420       | LOW         |
      | task_C              | false               | COMP 360       | MEDIUM      |
      | task_D              | true                | ECSE 223       | HIGH        |
      | task_E              | true                | ECSE 202       | LOW         |
      | task_F              | true                | COMP 251       | MEDIUM      |


  Scenario Outline: The user successfully changes the priority of a task to a new priority (Normal Flow)
    When  I categorize a task as "<priority>" priority level
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should be categorized "<priority>"
    And   Category "<priority>" should contain task "<task_name>"

    Examples:
      | priority  | task_name   |
      | LOW       | task_A      |
      | MEDIUM    | task_B      |
      | LOW       | task_C      |
      | MEDIUM    | task_D      |
      | HIGH      | task_C      |
      | LOW       | task_D      |


  Scenario Outline: The user successfully sets the priority of a task to the existing priority (Alternate Flow)
    When  I categorize a task as "<priority>" priority level
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should be categorized "<priority>"
    And   Category "<priority>" should contain task "<task_name>"

    Examples:
      | priority  | task_name   |
      | HIGH      | task_A      |
      | LOW       | task_B      |
      | MEDIUM    | task_C      |
      | HIGH      | task_D      |
      | LOW       | task_C      |
      | MEDIUM    | task_D      |


  Scenario Outline: The user attempts to changes the priority of a task to a non-existent priority (Error Flow)
    When  I categorize a task as "<new_priority>" priority level
    Then  I should receive an error informing me that the requested resource was not found
    And   Task "<task_name>" should be categorized "<existing_priority>"
    And   Category "<existing_priority>" should contain task "<task_name>"

    Examples:
      | new_priority  | task_name   | existing_priority |
      | EXTRA         | task_A      | HIGH              |
      | ULTRA         | task_B      | LOW               |
      | SOMEWHAT      | task_C      | MEDIUM            |
      | EXTREME       | task_D      | HIGH              |
      | USELESS       | task_C      | LOW               |
      | LO            | task_D      | MEDIUM            |
