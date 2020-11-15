Feature: Query All Incomplete HIGH Priority Tasks
  As a student, I query all incomplete HIGH priority tasks from all my classes, to identify my short-term goals.

  Background:
    Given Categories exist for the following priority levels:
      | priority |
      | MEDIUM   |
      | LOW      |
    And   Projects exist for the following courses:
      | course    |
      | ECSE 429  |
      | ECSE 420  |
      | COMP 360  |

  Scenario Outline: The user successfully queries all incomplete HIGH priority tasks (Normal Flow)
    Given Categories exist for the following priority levels:
      | priority  |
      | HIGH      |
    And   Task "<task_name_1>" exists for course "<course1>" with completion status "<status1>" and priority "<priority1>"
    And   Task "<task_name_2>" exists for course "<course2>" with completion status "<status2>" and priority "<priority2>"
    And   Task "<task_name_3>" exists for course "<course3>" with completion status "<status3>" and priority "<priority3>"
    And   Task "<task_name_4>" exists for course "<course4>" with completion status "<status4>" and priority "<priority4>"
    When  I query all incomplete HIGH priority tasks for all courses
    Then  I should receive a confirmation that the operation was successful
    And   I should receive a list of all incomplete tasks from all courses as a response

    Examples:
    | task_name_1 | course1   | status1 | priority1 | task_name_2 | course2   | status2 | priority2 | task_name_3 | course3   | status3 | priority3 | task_name_4 | course4   | status4 | priority4 |
    | assignment  | ECSE 420  | true    | LOW       | lab         | COMP 360  | false   | HIGH      | test        | COMP 360  | false   | MEDIUM    | exam        | ECSE 429  | false   | HIGH      |
    | assignment  | ECSE 420  | true    | LOW       | lab         | COMP 360  | false   | LOW       | test        | COMP 360  | false   | MEDIUM    | exam        | ECSE 429  | false   | HIGH      |
    | assignment  | ECSE 420  | false   | HIGH      | lab         | COMP 360  | false   | HIGH      | test        | COMP 360  | false   | HIGH      | exam        | ECSE 429  | false   | HIGH      |

  Scenario Outline: The user successfully queries all incomplete HIGH priority tasks when non exist (Alternate Flow)
    Given Categories exist for the following priority levels:
      | priority  |
      | HIGH      |
    And   Task "<task_name_1>" exists for course "<course1>" with completion status "<status1>" and priority "<priority1>"
    And   Task "<task_name_2>" exists for course "<course2>" with completion status "<status2>" and priority "<priority2>"
    And   Task "<task_name_3>" exists for course "<course3>" with completion status "<status3>" and priority "<priority3>"
    And   Task "<task_name_4>" exists for course "<course4>" with completion status "<status4>" and priority "<priority4>"
    When  I query all incomplete HIGH priority tasks for all courses
    Then  I should receive a confirmation that the operation was successful
    And   I should receive a list of all incomplete tasks from all courses as a response

    Examples:
      | task_name_1 | course1   | status1 | priority1 | task_name_2 | course2   | status2 | priority2 | task_name_3 | course3   | status3 | priority3 | task_name_4 | course4   | status4 | priority4 |
      | assignment  | ECSE 420  | true    | LOW       | lab         | COMP 360  | true    | HIGH      | test        | COMP 360  | false   | MEDIUM    | exam        | ECSE 429  | false   | MEDIUM    |
      | assignment  | ECSE 420  | true    | HIGH      | lab         | COMP 360  | true    | HIGH      | test        | COMP 360  | true    | HIGH      | exam        | ECSE 429  | true    | HIGH      |
      | assignment  | ECSE 420  | false   | MEDIUM    | lab         | COMP 360  | false   | LOW       | test        | COMP 360  | false   | LOW       | exam        | ECSE 429  | false   | LOW       |


  Scenario Outline: The user attempts to query all incomplete HIGH priority tasks when the HIGH priority category does not exist (Error Flow)
    Given Task "<task_name_1>" exists for course "<course1>" with completion status "<status1>" and priority "<priority1>"
    And   Task "<task_name_2>" exists for course "<course2>" with completion status "<status2>" and priority "<priority2>"
    And   Task "<task_name_3>" exists for course "<course3>" with completion status "<status3>" and priority "<priority3>"
    And   Task "<task_name_4>" exists for course "<course4>" with completion status "<status4>" and priority "<priority4>"
    When  I query all incomplete HIGH priority tasks for all courses
    Then  I should receive an error informing me that the requested resource was not found

    Examples:
      | task_name_1 | course1   | status1 | priority1 | task_name_2 | course2   | status2 | priority2 | task_name_3 | course3   | status3 | priority3 | task_name_4 | course4   | status4 | priority4 |
      | assignment  | ECSE 420  | true    | LOW       | lab         | COMP 360  | true    | MEDIUM    | test        | COMP 360  | false   | MEDIUM    | exam        | ECSE 429  | false   | MEDIUM    |
      | assignment  | ECSE 420  | true    | MEDIUM    | lab         | COMP 360  | true    | MEDIUM    | test        | COMP 360  | true    | MEDIUM    | exam        | ECSE 429  | true    | MEDIUM    |
      | assignment  | ECSE 420  | false   | MEDIUM    | lab         | COMP 360  | false   | MEDIUM    | test        | COMP 360  | false   | LOW       | exam        | ECSE 429  | false   | LOW       |
