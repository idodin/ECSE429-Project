
Feature: create project class

  As a student, I create a to do list for a new class I am taking, so I
  can manage course work.

Scenario Outline: successfuly add a task (normal flow)
Given there is an emtpy project ECSE429
When I create a task <"todo">
And I add the task <"todo"> to ECSE429 project
Then the project ECSE429 contain the task to do my project B

Examples:
| todo |
| to do my project part A |
| to do my project part B |
| to do the report |

Scenario: successfuly create a class project  and add task to it (alternate flow)
Given there is no project related to a new class
When I create a project ECSE420
And I create a task to do lab1
And I add the task to do lab1 to ECSE420 project
Then the project ECSE420 should contain the task to do lab1

Scenario: successfuly add task to a class project (alternate flow)
Given there is a COMP360 project
And there is a to do assignment1 in COMP360 project
When I create a task to do assignment2
And I add the task to do assignment2 to COMP360 project
Then the project COMP360 should contain the task to do assignment1 and to do assignment2


Scenario: add a task to a non-existing project related to the specific class (Error flow)
Given there is a non-existing ECSE223 project
When I create a task to do homework
And I add the task to do homework to ECSE223 project
Then Bad request
