#TODO maybe add more cases
#  - Alternate: create without specifying active / completion stauts
#  - Error:  create with incorrect active status
Feature: Create Project for Class
  As a student, I create a to do list for a new class I am taking, so I can manage course work.


  Background:
    Given The application is running
    And   Projects exist for the following courses:
      | course    |
      | ECSE 429  |
      | ECSE 420  |
      | COMP 360  |


  Scenario Outline: The user successfully creates a course to do list with no description (Normal Flow)
    When  I create a course to do list for course "<course>" with active status "<active>" and completion status "<complete>"
    Then  I should receive a confirmation that my operation was successful
    And   A course to do list for course "<course>" should exist
    And   The course to do list for course "<course>" should have active status "<active>"
    And   The course to do list for course "<course>" should have completion status "<complete>"
    And   The list of course to do lists should include a to do list for course "<course>"

    Examples:
      | course    | active  | complete  |
      | COMP 551  | true    | false     |
      | COMP 273  | false   | true      |
      | ECSE 437  | true    | true      |
      | ECSE 326  | false   | false     |


  Scenario Outline: The user successfully creates a course to do list with a description (Alternate Flow)
    When  I create a course to do list for course "<course>" with active status "<active>", completion status "<complete>" and description "<description>"
    Then  I should receive a confirmation that my operation was successful
    And   A course to do list for course "<course>" should exist
    And   The course to do list for course "<course>" should have active status "<active>"
    And   The course to do list for course "<course>" should have completion status "<complete>"
    And   The list of course to do lists should include a to do list for course "<course>"

    Examples:
      | course    | active  | complete  | description   |
      | COMP 551  | true    | false     | ML            |
      | COMP 273  | false   | true      | 273           |
      | ECSE 437  | true    | true      | Delivery      |
      | ECSE 326  | false   | false     | Reqs          |


  Scenario Outline: The user attempts to create a course to do list with an invalid completion status (Alternate Flow)
    When  I create a course to do list for course "<course>" with active status "<active>", completion status "<complete>" and description "<description>"
    Then  I should receive an error informing me that the passed information was invalid
    And   A course to do list for course "<course>" should not exist

    Examples:
      | course    | active  | complete  | description   |
      | COMP 551  | true    | done      | ML            |
      | COMP 273  | false   | 1         | 273           |
      | ECSE 437  | true    | adfaf     | Delivery      |
      | ECSE 326  | false   | tried     | Reqs          |
