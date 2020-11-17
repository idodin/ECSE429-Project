Feature: Query Remaining Tasks
  As a student, I query the incomplete tasks for a class I am taking, to help manage my time.

  Background:
    Given The application is running
    And   Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   Projects exist for the following courses:
      | projects |
      | ECSE 429 |
      | ECSE 420 |
      | ECSE 427 |
      | ECSE 211 |
    And   The following tasks exist with their respective statuses and courses:
      | task   | completed | projects |
      | task_A | false     | ECSE 429 |
      | task_B | false     | ECSE 429 |
      | task_C | true      | ECSE 429 |
      | task_D | true      | ECSE 429 |
      | task_E | false     | COMP 360 |
      | task_F | false     | COMP 360 |
      | task_G | true      | COMP 360 |
      | task_H | true      | COMP 360 |


  # TODO Maybe more tasks returned?
  Scenario Outline: The user successfulLy queries all incomplete task in a course to do list (Normal Flow)
    When  I query all incomplete tasks for the "<course>" course to do list
    Then  I should receive a confirmation that my operation was successful
    And   the task "<task1>" should be returned
    And   the task "<task2>" should be returned

    Examples:
      | course    | task1   | task2   |
      | ECSE 429  | task_A  | task_B  |
      | COMP 360  | task_E  | task_F  |


  Scenario Outline: The user successfully queries all incomplete tasks in a course to do list with no incomplete tasks (Alternate Flow)
    When  I query all incomplete tasks for the "<course>" course to do list
    Then  I should receive a confirmation that my operation was successful
    And   No tasks should be returned

    Examples:
      | course    |
      | ECSE 427  |
      | ECSE 211  |


  Scenario Outline: The user attempts to request all incomplete tasks for a course that does not exist (Error Flow)
    When  I query all incomplete tasks for the "<course>" course to do list
    Then  I should receive an error informing me that the requested resource was not found

    Examples:
      | course    |
      | ECSE 551  |
      | ECSE 451  |
