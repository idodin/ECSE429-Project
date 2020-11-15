Feature: Remove Course
  As a student, I remove a to do list for a class which I am no longer taking, to declutter my schedule.

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
    When  I remove the "<course>" course to do list
    Then  I should receive a confirmation that my operation was successful
    And   Tasks that were under the "<course>" course to do list should not exist in the system
    And   Course to do list "<course>" should not exist in the system

    Examples:
    | course    |
    | ECSE 429  |
    | ECSE 420  |
    | COMP 360  |


  Scenario Outline: The user successfully removes a prioritized task from the course to do list (Alternative Flow)
    Given  The following tasks exist with their respective statuses, courses and priority levels:
      | task                | completed           | course         | priority    |
      | task_A              | false               | ECSE 429       | HIGH        |
      | task_B              | false               | ECSE 420       | LOW         |
      | task_C              | false               | COMP 360       | MEDIUM      |
    When  I remove the "<course>" course to do list
    Then  I should receive a confirmation that my operation was successful
    And   Tasks that were under the "<course>" course to do list should not exist in the system
    And   Course to do list "<course>" should not exist in the system

    Examples:
      | course    |
      | ECSE 429  |
      | ECSE 420  |
      | COMP 360  |

  Scenario Outline: The user attempts to remove a task that has already been removed from the course to do list (Error Flow)
    Given The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 429  | assignment0 |
      | ECSE 420  | lab5-1      |
      | COMP 360  | study       |
      | COMP 360  | assignment1 |
    When  I remove the "<course>" course to do list
    Then  I should receive an error informing me that the requested resource was not found
    And   Tasks that were under the "<course>" course to do list should not exist in the system
    And   Course to do list "<course>" should not exist in the system

    Examples:
      | course    |
      | ECSE 429  |
      | ECSE 420  |
      | COMP 360  |