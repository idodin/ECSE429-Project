Feature: Remove Course Task
  As a student, I remove an unnecessary task from my course to do list, so I can forget about it.

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

  Scenario Outline: The user successfully removes a task to from course to do list (Normal Flow)
    Given The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 429  | assignment0 |
      | ECSE 420  | lab5-1      |
      | COMP 360  | study       |
      | COMP 360  | assignment1 |
    When  I remove the task "<task_name>" from the "<course>" course to do list
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should not be under the "<course>" course to do list
    And   "<course>" course to do list should not contain task "<task_name>"
    And   "<course>" course to do list should contain <course_task_count> courses

    Examples:
      | course    | task_name   | course_task_count |
      | ECSE 429  | assignment0 | 0                 |
      | ECSE 420  | lab5-1      | 0                 |
      | COMP 360  | study       | 1                 |


  Scenario Outline: The user successfully removes a prioritized task from the course to do list (Alternative Flow)
    Given  The following tasks exist with their respective statuses, courses and priority levels:
      | course    | task_name   | priority_level  | done  |
      | ECSE 429  | assignment0 | HIGH            | false |
      | ECSE 420  | lab5-1      | LOW             | true  |
      | COMP 360  | study       | MEDIUM          | false |
      | COMP 360  | assignment1 | HIGH            | true  |
    When  I remove the task "<task_name>" from the "<course>" course to do list
    Then  I should receive a confirmation that the operation was successful
    And   "<course>" course to do list should not contain task "<task_name>"
    And   "<course>" course to do list should contain <course_task_count> courses
    And   Category "<priority>" should not contain task "<task_name>"

    Examples:
      | course    | task_name   | course_task_count | priority  |
      | ECSE 429  | assignment0 | 0                 | HIGH      |
      | ECSE 420  | lab5-1      | 0                 | LOW       |
      | COMP 360  | study       | 1                 | MEDIUM    |

  Scenario Outline: The user attempts to remove a task that has already been removed from the course to do list (Error Flow)
    Given The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 429  | assignment0 |
      | ECSE 420  | lab5-1      |
      | COMP 360  | study       |
      | COMP 360  | assignment1 |
    When  I remove the task "<task_name>" from the "<course>" course to do list
    And   I remove the task "<task_name>" from the "<course>" course to do list again
    Then  I should receive an error informing me that the requested resource was not found
    And   Task "<task_name>" should not be under the "<course>" course to do list
    And   "<course>" course to do list should not contain task "<task_name>"
    And   "<course>" course to do list should contain <course_task_count> courses

    Examples:
      | course    | task_name   | course_task_count |
      | ECSE 429  | assignment0 | 0                 |
      | ECSE 420  | lab5-1      | 0                 |
      | COMP 360  | study       | 1                 |