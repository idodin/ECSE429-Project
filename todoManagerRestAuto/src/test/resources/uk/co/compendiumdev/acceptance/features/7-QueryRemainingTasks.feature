Feature: Query Remaining Tasks
  As a student, I query the incomplete tasks for a class I am taking, to help manage my time.

  Background:
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   Projects exist for the following courses:
      | projects |
      | ECSE_429 |
      | ECSE_420 |
      | ECSE_427 |
      | ECSE_211 |
    And   The following tasks exist with their respective statuses and courses:
      | task   | statuses | projects |
      | task_A | false    | ECSE_429 |
      | task_B | false    | ECSE_420 |
      | task_C | true     | ECSE_427 |
      | task_D | true     | ECSE_211 |


  # TODO Maybe more tasks returned?
  Scenario Outline : The user successfulLy queries all incomplete task in a course to do list (Normal Flow)
    When  I query all incomplete tasks for the "<course>" course to do list
    Then  the task "<task>" should be returned

    Examples:
      | course    |
      | ECSE_427  |
      | ECSE_211  |


  Scenario Outline: The user successfully queries all incomplete tasks in a course to do list with no incomplete tasks (Alternate Flow)
    When  I query all incomplete tasks for the "<course>" course to do list
    Then  No tasks should be returned

    Examples:
      | course    |
      | ECSE_429  |
      | ECSE_420  |


  Scenario Outline: The user attempts to request all incomplete tasks for a course that does not exist (Error Flow)
    When  I query all incomplete tasks for the "<course>" course to do list
    Then  No tasks should be returned

    Examples:
      | course    |
      | ECSE_551  |
      | ECSE_451  |
