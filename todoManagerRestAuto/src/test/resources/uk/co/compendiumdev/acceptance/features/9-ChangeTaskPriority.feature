Feature: change task priority

  As a student, I want to adjust the priority of a task, to help better
  manage my time.


  Scenario Outline: successfuly adjust the priority of a task in non-empty projects (normal flow)
  Given there are three non-empty projects for each priority level
  And there is a task dohomework1 in the "<priority>" project
  When I adjust the task dohomework1 to "<priority2>" priority level
  Then the "<priority2>" project contains the task dohomework1
  And the "<priority>" project should not contain the task dohomework1

  Examples:
    | priority | priority2 |
    | HIGH     | LOW       |
    | MEDIUM   | HIGH      |
    | LOW      | MEDIUM    |

    Scenario: successfuly adjust the priority of a task (alternate flow)
    Given there are three projects for each priority level
    And there is only one task, dohomework2, in the HIGH project
    And there are no tasks in LOW and MEDIUM projects
    When I adjust the task dohomework2 to LOW priority level
    Then the LOW project contains only the task dohomework1
    And the HIGH project should be empty


    Scenario: Adjust the priority level of a non-existing task (error flow)
    Given there are three empty projects for each priority level
    When I adjust the task dohomework3 to LOW priority level
    Then Bad request
