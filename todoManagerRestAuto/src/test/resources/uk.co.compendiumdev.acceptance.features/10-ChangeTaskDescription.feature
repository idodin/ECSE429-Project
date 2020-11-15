Feature: Change Task Description
  As a student, I want to change a task description to better represent the work that I do

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

  Scenario Outline: The user successfully changes the description for a task with an existing description (Normal Flow)
    Given The following tasks exist with their respective courses and descriptions:
    | course    | task_name   | description       |
    | ECSE 420  | lab         | Parallel Lab 1    |
    | COMP 360  | assignment  | Min Cut Max Flow  |
    | ECSE 429  | test        | Midterm Exam      |
    When  I change the description of task "<task_name>" to "<new_description>"
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should have description "<new_description>"

    Examples:
      | task_name   | new_description   |
      | lab         | Parallel Lab 2    |
      | assignment  | NP-Completeness   |
      | test        | Final Exam        |


  Scenario Outline: The user successfully changes the description for a task without an existing description (Alternate Flow)
    Given  The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 420  | lab         |
      | COMP 360  | assignment  |
      | ECSE 429  | test        |
    When  I change the description of task "<task_name>" to "<new_description>"
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should have description "<new_description>"

    Examples:
      | task_name   | new_description   |
      | lab         | Parallel Lab 2    |
      | assignment  | NP-Completeness   |
      | test        | Final Exam        |

  Scenario Outline: The user attempts to change the description for a task that doesn't exist (Error Flow)
    Given  The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 420  | lab         |
      | COMP 360  | assignment  |
      | ECSE 429  | test        |
    When  I change the description of task "<task_name>" to "<new_description>"
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should have description "<new_description>"

    Examples:
      | task_name   | new_description   |
      | midterm     | Midterm           |
      | exam        | BFS               |
      | report      | Circuits          |