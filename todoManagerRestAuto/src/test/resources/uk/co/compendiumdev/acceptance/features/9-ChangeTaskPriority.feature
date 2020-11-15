Feature: Change Task Priority
  As a student, I want to adjust the priority of a task, to help better manage my time.

  # FIXME what is the difference between this and the next one
  Scenario Outline: The user successfully adjusts the priority of a task in non-empty projects (Normal Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   The following tasks exist for each of the following priority levels:
      | task_name | priority  |
      | task_A    | HIGH      |
      | task_B    | LOW       |
      | task_C    | MEDIUM    |
    When  I categorize the task "<task_name>" to "<new_priority>" priority level
    Then  the category "<new_priority>" should  contains the task "<task_name>"
    And   the category "<priority>" should not contain the task "<task_name>"

    Examples:
      | task_name     | new_priority | priority |
      | task_A        | MEDIUM       | HIGH     |
      | task_B        | HIGH         | LOW      |
      | task_C        | LOW          | MEDIUM   |


  Scenario Outline: The user successfully adjusts the priority level of a task (Alternate Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   the following task exists for the following priority:
      | task    | priority |
      | task_A  | HIGH     |
    And   there are no tasks in following priority level:
      | priority |
      | MEDIUM   |
      | LOW      |
    When  I categorize the task "task_A" to "<new_priority>" priority level
    Then  the "<new_priority>" should contains only the task "task_A"
    And   the HIGH project should be empty

    Examples:
      | new_priority |
      | MEDIUM       |
      | LOW          |


  Scenario Outline: The user attempts to categorize a non-existing task as a given priority (Error Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   a non-existing tasks "<no_existing_task>"
    When  I adjust the non-existing task "<no_existing_task>" to "<existing_priority_1>" priority level
    Then  I should receive an error informing me that the task "<no_existing_task>"  does not exist

    Examples:
      | no_existing_task   | existing_priority_1 |
      | assignment         |  LOW                |
      | lab                |  MEDIUM             |
      | report             |  HIGH               |
