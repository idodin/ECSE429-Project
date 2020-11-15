#TODO could add additional scenarios for tasks with priorities
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
    And   Task "<task_name>" should be under the "<course>" course to do list
    And   "<course>" course to do list should contain task "<task_name>"

    Examples:
      | task_name   | new_description   | course    |
      | lab         | Parallel Lab 2    | ECSE 420  |
      | assignment  | NP-Completeness   | COMP 360  |
      | test        | Final Exam        | ECSE 429  |


  Scenario Outline: The user successfully changes the description for a task without an existing description (Alternate Flow)
    Given  The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 420  | lab         |
      | COMP 360  | assignment  |
      | ECSE 429  | test        |
    When  I change the description of task "<task_name>" to "<new_description>"
    Then  I should receive a confirmation that my operation was successful
    And   Task "<task_name>" should have description "<new_description>"
    And   Task "<task_name>" should be under the "<course>" course to do list
    And   "<course>" course to do list should contain task "<task_name>"

    Examples:
      | task_name   | new_description   | course    |
      | lab         | Parallel Lab 2    | ECSE 420  |
      | assignment  | NP-Completeness   | COMP 360  |
      | test        | Final Exam        | ECSE 429  |

  Scenario Outline: The user attempts to change the description for a task that doesn't exist (Error Flow)
    Given  The following tasks exist for their respective courses:
      | course    | task_name   |
      | ECSE 420  | lab         |
      | COMP 360  | assignment  |
      | ECSE 429  | test        |
    When  I change the description of task "<task_name>" to "<new_description>"
    Then  I should receive an error informing me that the requested resource was not found
    And   Task "<task_name>" should have description "<new_description>"

    Examples:
      | task_name   | new_description   |
      | midterm     | Midterm           |
      | exam        | BFS               |
      | report      | Circuits          |