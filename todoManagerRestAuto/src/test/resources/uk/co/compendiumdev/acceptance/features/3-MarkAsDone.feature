# IMPORTANT NOTE: SCENARIO OUTLINES ARE THE SAME BUT DATA SHOULD BE DIFFERENT

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
    # TODO Data Table here
   And  The following tasks exist with their respective statuses, courses and priority levels:
   # TODO Data Table Here
   When I mark task "<task>" as done
   Then The status of task "<task>" should be done
   And  The following tasks should exist with their respective course and priority levels:
   # TODO Data Table Here

   # TODO fill example table
   Examples:
    | task     |


   Scenario Outline: The user successfully marks an already completed  task as done (Alternate Flow)
    Given Categories exist for the following priority levels:
     | priority |
     | HIGH     |
     | MEDIUM   |
     | LOW      |
    And Projects exist for the following courses:
      # TODO Data Table here
    And  The following tasks exist with their respective statuses, courses and priority levels:
     # TODO Data Table Here
    When I mark task "<task>" as done
    Then The status of task "<task>" should be done
    And  The following tasks should exist with their respective course and priority levels:
     # TODO Data Table Here

     # TODO fill example table
    Examples:
     | task     |


   Scenario Outline: The user attempts mark a non-existent task as done (Alternate Flow)
    Given Categories exist for the following priority levels:
     | priority |
     | HIGH     |
     | MEDIUM   |
     | LOW      |
    And Projects exist for the following courses:
        # TODO Data Table here
    And  The following tasks exist with their respective statuses, courses and priority levels:
       # TODO Data Table Here
    When I mark task "<task>" as done
    Then The status of task "<task>" should be done
    And  The following tasks should exist with their respective course and priority levels:
       # TODO Data Table Here

       # TODO fill example table
    Examples:
     | task     |