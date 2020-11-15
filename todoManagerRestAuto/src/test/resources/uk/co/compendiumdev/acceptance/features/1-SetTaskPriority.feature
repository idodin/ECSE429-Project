Feature: Set Task Priority
  As a student, I categorize tasks as HIGH, MEDIUM or LOW priority, so I
  can better manage my time.


  Background:
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   Projects exist for the following courses:
      | course    |
      | ECSE 429  |
      | ECSE 420  |
      | COMP 360  |
    And   The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 429  | assignment0 |
      | ECSE 420  | lab5-1      |
      | COMP 360  | study       |
      | COMP 360  | assignment1 |


  Scenario Outline: The user successfully categorizes a task as a given priority (Normal Flow)
    Given No tasks exist for each priority level
    When  I categorize task "<task_name>" as "<priority>" priority
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should be categorized as "<priority>" priority
    And   Category "<priority>" should contain task "<task_name>"

    Examples:
      | task_name   | priority  |
      | assignment0 | HIGH      |
      | lab5-1      | MEDIUM    |
      | study       | LOW       |


  Scenario Outline: The user successfully categorizes a task as a given priority when other tasks with that priority already exist (Alternate Flow)
    Given The following tasks exist with their respective statuses, courses and priority levels:
      | task                | completed           | course         | priority    |
      | task_A              | false               | ECSE 429       | HIGH        |
      | task_B              | false               | ECSE 420       | LOW         |
      | task_C              | false               | COMP 360       | MEDIUM      |
    When  I categorize task "<task_name>" as "<priority>" priority
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should be categorized as "<priority>" priority
    And   Category "<priority>" should contain task "<task_name>"
    And   Category "<priority>" should still contain its original tasks

    Examples:
      | task_name   | priority  |
      | assignment0 | HIGH      |
      | lab5-1      | MEDIUM    |
      | study       | LOW       |

  Scenario Outline: The user attempts to categorize a task as a given priority when no category exists for that priority level (Error Flow)
    When  I categorize task "<task_name>" as "<priority>" priority
    Then  I should receive an error informing me that the requested resource was not found
    And   Task "<task_name>" should not be categorized as any priority

    Examples:
      | task_name   | priority    |
      | assignment0 | ULTRA       |
      | lab5-1      | LO          |
      | study       | USELESS     |
