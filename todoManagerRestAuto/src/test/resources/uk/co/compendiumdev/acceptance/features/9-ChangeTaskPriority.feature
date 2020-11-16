Feature: Change Task Priority
  As a student, I want to adjust the priority of a task, to help better manage my time.

  Background:
    Given The application is running
    And   Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   Projects exist for the following courses:
      | Projects  |
      | ECSE 429  |
      | ECSE 420  |
      | COMP 360  |
      | ECSE 223  |
      | ECSE 202  |
      | COMP 251  |
    And   The following tasks exist with their respective statuses, courses and priority levels:
      | task                | completed           | course         | priority    |
      | task_A              | false               | ECSE 429       | HIGH        |
      | task_B              | false               | ECSE 420       | LOW         |
      | task_C              | false               | COMP 360       | MEDIUM      |
      | task_D              | true                | ECSE 223       | HIGH        |
      | task_E              | true                | ECSE 202       | LOW         |
      | task_F              | true                | COMP 251       | MEDIUM      |


  Scenario Outline: The user successfully changes the priority of a task to a new priority (Normal Flow)
    When  I categorize task "<task_name>" as "<priority>" priority
    Then  I should receive a confirmation that my operation was successful
    And   Category "<priority>" should contain task "<task_name>"
    And   Task "<task_name>" should be under the "<course>" course to do list
    And   "<course>" course to do list should contain task "<task_name>"

    Examples:
      | priority  | task_name   | course    |
      | LOW       | task_A      | ECSE 429  |
      | MEDIUM    | task_B      | ECSE 420  |
      | LOW       | task_C      | COMP 360  |
      | MEDIUM    | task_D      | ECSE 223  |
      | HIGH      | task_E      | ECSE 202  |
      | LOW       | task_F      | COMP 251  |


  Scenario Outline: The user successfully sets the priority of a task to the existing priority (Alternate Flow)
    When  I categorize task "<task_name>" as "<priority>" priority
    Then  I should receive a confirmation that my operation was successful
    And   Category "<priority>" should contain task "<task_name>"
    And   Task "<task_name>" should be under the "<course>" course to do list
    And   "<course>" course to do list should contain task "<task_name>"

    Examples:
      | priority  | task_name   | course    |
      | HIGH      | task_A      | ECSE 429  |
      | LOW       | task_B      | ECSE 420  |
      | MEDIUM    | task_C      | COMP 360  |
      | HIGH      | task_D      | ECSE 223  |
      | LOW       | task_E      | ECSE 202  |
      | MEDIUM    | task_F      | COMP 251  |


  Scenario Outline: The user attempts to changes the priority of a task to a non-existent priority (Error Flow)
    When  I categorize task "<task_name>" as "<new_priority>" priority
    Then  I should receive an error informing me that the requested resource was not found
    And   Category "<existing_priority>" should contain task "<task_name>"
    And   Task "<task_name>" should be under the "<course>" course to do list
    And   "<course>" course to do list should contain task "<task_name>"

    Examples:
      | new_priority  | task_name   | existing_priority | course    |
      | EXTRA         | task_A      | HIGH              | ECSE 429  |
      | ULTRA         | task_B      | LOW               | ECSE 420  |
      | SOMEWHAT      | task_C      | MEDIUM            | COMP 360  |
      | EXTREME       | task_D      | HIGH              | ECSE 223  |
      | USELESS       | task_E      | LOW               | ECSE 202  |
      | LO            | task_F      | MEDIUM            | COMP 251  |
