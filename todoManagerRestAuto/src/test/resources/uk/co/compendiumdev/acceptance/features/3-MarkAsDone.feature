Feature: Mark Task As Done
  As a student, I mark a task as done on my course to do list, so I can
  track my accomplishments.

  Background:
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And Projects exist for the following courses:
      | course    |
      | ECSE 429  |
      | ECSE 420  |
      | COMP 360  |
      | ECSE 223  |
      | ECSE 202  |
      | COMP 251  |
    Given The following tasks exist with their respective statuses, courses and priority levels:
      | task                | completed           | course         | priority    |
      | task_A              | false               | ECSE 429       | HIGH        |
      | task_B              | false               | ECSE 420       | LOW         |
      | task_C              | false               | COMP 360       | MEDIUM      |
      | task_D              | true                | ECSE 223       | HIGH        |
      | task_E              | true                | ECSE 202       | LOW         |
      | task_F              | true                | COMP 251       | MEDIUM      |


  Scenario Outline: The user successfully marks an incomplete task as done (Normal Flow)
    When   I mark task "<task>"'s completion status as "<completed>"
    Then   I should receive a confirmation that my operation was successful
    And    The completion status of task "<task>" should be "<completed>"

    Examples:
      | task       | completed   |
      | task_A     | true        |
      | task_B     | true        |
      | task_C     | true        |


  Scenario Outline: The user successfully marks a task that has already been completed as done (Alternate Flow)
    When  I mark task "<task>"'s completion status as "<completed>"
    Then  I should receive a confirmation that my operation was successful
    And   The completion status of task "<task>" should be "<completed>"

    Examples:
      | task       | completed   |
      | task_D     | true        |
      | task_E     | true        |
      | task_F     | true        |


  Scenario Outline: The user attempts to mark a task as an invalid completion status (Error Flow)
    When  I mark task "<task>"'s completion status as "<completed>"
    Then  I should receive an error informing me that the passed information was invalid
    And   The completion status of task "<task>" should be false

    Examples:
      | task       | completed   |
      | task_D     | done        |
      | task_E     | 1           |
      | task_F     | yes         |
