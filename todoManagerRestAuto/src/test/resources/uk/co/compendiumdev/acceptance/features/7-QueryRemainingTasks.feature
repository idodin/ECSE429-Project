Feature: Query Remaining Tasks
  As a student, I query the incomplete tasks for a class I am taking, to help manage my time.


  Scenario Outline : The user successfulLy queries all incomplete task in a course to do list (Normal Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   Projects exist for the following courses:
      | projects |
      | ECSE_429 |
      | ECSE_420 |
    And   The following tasks exist with their respective statuses and courses:
      | task   | statuses | projects |
      | task_A | false    | ECSE_429 |
      | task_B | false    | ECSE_420 |
    When  I querry all task with status "<status>" of project "<project>"
    Then  the task "<output_task>" should be returned

    Examples:
      | status | project  | output_task |
      | false  | ECSE_429 | task_A      |
      | false  | ECSE_420 | task_B      |


  Scenario Outline: The user successfully queries all incomplete tasks in a course to do list with no incomplete projects (Alternate Flow)
    Given Categories exist for the following priority levels:
      | priority |
      | HIGH     |
      | MEDIUM   |
      | LOW      |
    And   Projects exist for the following courses:
      | projects |
      | ECSE_427 |
      | ECSE_211 |
    And   The following tasks exist with their respective statuses and courses:
      | task   | statuses | projects |
      | task_C | true     | ECSE_427 |
      | task_D | true     | ECSE_211 |
    When  I querry all task with status "<status>" of project "<project>"
    Then  no tasks should be returned

    Examples:
      | status | project  |
      | false  | ECSE_429 |
      | false  | ECSE_420 |


  Scenario Outline: The user queries tasks all incomplete tasks from a course to do list that doesn't exist (Error Flow)
    Given An empty project  "<existing_project_1>" exists
    And   An empty project  "<existing_project_2>" exists
    When  I querry all task with status "<status>" of project "<new_project>"
    Then  I should receive an error informing me that "<new_project>" project does not exist

    Examples:
      | existing_project_1 | existing_project_2  | status     |  new_project  |
      | FACC_300           | COMP_350            | false      |  PHYS_224     |
      | ECSE_205           | FACC_400            | false      |  ATOC_214     |
