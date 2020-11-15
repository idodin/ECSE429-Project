Feature: Mark Task As Done
  As a student, I mark a task as done on my course to do list, so I can
  track my accomplishments.


  Scenario Outline: The user successfully marks an incomplete task as done (Normal Flow)
   Given Categories exist for the following priority levels:
    | priority |
    | HIGH     |
    | MEDIUM   |
    | LOW      |
   And Projects exist for the following courses:
    | Projects |
    | ECSE_429 |
    | ECSE_420 |
    | COMP_360 |
   And  The following tasks exist with their respective statuses, courses and priority levels:
    | tasks               | statuses            | Projects       | priority    |
    | task_A              | false               | ECSE_429       | HIGH        |
    | task_B              | false               | ECSE_420       | LOW         |
    | task_C              | false               | COMP_360       | MEDIUM      |
   When I mark task "<task>" as  "<done_status>"
   Then The status of task "<task>" should be "<done_status>"

   Examples:
    | task       | done_status |
    | task_A     | true        |
    | task_B     | true        |
    | task_C     | true        |


  Scenario Outline: The user successfully marks an already completed  task as done (Alternate Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And Projects exist for the following courses:
      | Projects |
      | ECSE_223 |
      | ECSE_202 |
      | COMP_251 |
    And  The following tasks exist with their respective statuses, courses and priority levels:
      | tasks               | statuses            | Projects       | priority    |
      | task_D              | true                | ECSE_223       | HIGH        |
      | task_E              | true                | ECSE_202       | LOW         |
      | task_F              | true                | COMP_251       | MEDIUM      |
     When I mark task "<task>" as  "<done_status>"
     Then The status of task "<task>" should be "<done_status>"

    Examples:
    | task       | done_status |
    | task_D     | true        |
    | task_E     | true        |
    | task_F     | true        |


  Scenario Outline: The user attempts mark a non-existent task as done (Error Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   Projects exist for the following courses:
      | Projects |
      | ECSE_223 |
      | ECSE_202 |
      | COMP_251 |
    And  The following tasks exist with their respective statuses, courses and priority levels:
      | tasks   | statuses | Projects       | priority    |
      | task_D  | true     | ECSE_223       | HIGH        |
      | task_E  | true     | ECSE_202       | LOW         |
      | task_F  | true     | COMP_251       | MEDIUM      |
    When I mark task "<non_existing_task>" as "<done_status>"
    Then I should receive an error message informing me that the "<non_existing_task>" doesn't exist

    Examples:
    | non_existing_task | done_status   |
    | task_G            | true          |
    | task_H            | true          |
