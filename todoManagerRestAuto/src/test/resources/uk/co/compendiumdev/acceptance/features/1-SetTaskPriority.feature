Feature: Set Task Priority
  As a student, I categorize tasks as HIGH, MEDIUM or LOW priority, so I
  can better manage my time.

  Scenario Outline: The user successfully categorizes a task as a given priority (Normal Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   No tasks exist for each priority level
    When  I create a task "<task_name>"
    And  I categorize a task as "<priority>" priority level
    Then  Task "<task_name>" should be categorized "<priority>"
    And   Category "<priority>" should contain task "<task_name>"

    # TODO Expand to include task name example
    Examples:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |


  Scenario Outline: The user successfully categorizes a task as a given priority when other tasks with that priority already exist (Alternate Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   The following tasks exist for each of the following priority levels:
    # TODO Fill out data table
      | task_name | priority  |
    When  I create a task "<task_name>"
    And  I categorize a task as "<priority>" priority level
    Then  Task "<task_name>" should be categorized "<priority>"
    And   Category "<priority>" should contain task "<task_name>"
    And   The following tasks should exist for each of the following priority levels:
    # TODO Fill out data table
      | task_name | priority  |

    # TODO Expand to include task name example
    Examples:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |

  Scenario Outline: The user attempts to categorize a task as a given priority when no category exists for that priority level (Error Flow)
    Given An empty category exists for the "<existing_priority_1>" priority level
    And   An empty category exists for the "<existing_priority_2>" priority level
    When  I create a task "<task_name>"
    And I categorize a task as "<new_priority>" priority level
    Then  I should receive an error informing me that "<new_priority>" priority level does not exist

    Examples:
    | existing_priority_1 | existing_priority_2 | new_priority  | task_name   |
    | LOW                 | MEDIUM              | HIGH          | assignment  |
    | MEDIUM              | HIGH                | LOW           | lab         |
    | HIGH                | LOW                 | MEDIUM        | report      |