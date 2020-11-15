Feature: Add Course Task
  As a student, I add a task to a course to do list, so I can remember it.

  Background:
    Given Projects exist for the following courses:
      | course    |
      | ECSE 429  |
      | ECSE 420  |
      | COMP 360  |

  Scenario Outline: The user successfully adds a task to a course to do list (Normal Flow)
    Given No tasks exist for each course
    When  I create a task "<task_name>"
    And   I add the task "<task_name>" to the "<course>" course to do list
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should be under the "<course>" course to do list
    And   "<course>" course to do list should contain task "<task_name>"
    And   "<course>" course to do list should contain 1 tasks

    Examples:
      | course    | task_name   |
      | ECSE 429  | assignment1 |
      | ECSE 420  | lab5-2      |
      | COMP 360  | review      |


  Scenario Outline: The user successfully adds a task to a course to do list that already contains some tasks (Alternative Flow)
    Given  The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 429  | assignment0 |
      | ECSE 420  | lab5-1      |
      | COMP 360  | study       |
      | COMP 360  | assignment1 |
    When  I create a task "<task_name>"
    And   I add the task "<task_name>" to the "<course>" course to do list
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should be under the "<course>" course to do list
    And   "<course>" course to do list should contain task "<task_name>"
    And   "<course>" course to do list should contain <course_task_count> tasks

    Examples:
      | course    | task_name   | course_task_count |
      | ECSE 429  | assignment1 | 2                 |
      | ECSE 420  | lab5-2      | 2                 |
      | COMP 360  | review      | 3                 |

  Scenario Outline: The user attempts to add a task to a given course to do list when no to do list exists for that course (Error Flow)
    Given The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 429  | assignment0 |
      | ECSE 420  | lab5-1      |
      | COMP 360  | study       |
      | COMP 360  | assignment1 |
    When  I create a task "<task_name>"
    And   I add the task "<task_name>" to the "<course>" course to do list
    Then  I should receive an error informing me that the requested resource was not found
    And   Task "<task_name>" should not be under the "<course>" course to do list
    And   Course to do list "<course>" should not exist in the system

    Examples:
      | course    | task_name   |
      | ECSE 430  | assignment  |
      | GEOG 200  | present     |
      | COMP 361  | review      |