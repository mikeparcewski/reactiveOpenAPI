@Get
Feature: Testing out GET operations for the API

  @Negative
  Scenario: Can't find the entity (GET1)
    Given I am a JSON API consumer
    And I am executing test "GET1"
    When I request GET "/vendor/XXXXXX"
    Then I should get a status code of 404

  ## Need to create the entity first
  ## and then lookup, you could create
  ## separately, but this keeps each test
  ## self contained
  ## This example uses data tables
  @Positive
  Scenario: Found the entity (GET2)
    Given I am a JSON API consumer
    And I am executing test "GET2-0"
    When I request POST "/vendor"
    And I set the JSON body from values
    | name                          | Widgets Incorporated          |
    | description                   | They are a widget company!    |
    | contactDetails[0].detailType  | Office                        |
    | contactDetails[0].email       | test@test.com                 |
    | contactDetails[0].phone       | 617-555-5555                  |
    Then I should get a status code of 200
    And record the response as "GET2USER"
    And I am a JSON API consumer
    And I am executing test "GET2-1"
    Then I request GET "/vendor/{{response::GET2USER->id}}"
    And I should get a status code of 200



