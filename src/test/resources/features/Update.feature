@Update
Feature: Testing out UPDATE operations for the API

  ## Uses file & data table view
  ## showcases the faker library
  ## and the use of the cache, by
  ## validating the dynamic data sent over
  @Positive
  Scenario: Update non-nested element (UPDATE1)
    Given I am a JSON API consumer
    And I am executing test "UPDATE1A"
    When I request POST "/vendor"
    And I set the JSON body from file "src/test/resources/data/payload.json"
    And record the response as "UPDATE1"
    And I should get a status code of 200
    And I am a JSON API consumer
    And I am executing test "UPDATE1-B"
   When I request POST "/vendor/{{response::UPDATE1->id}}"
    And I set the JSON body from values
      | name      | Used Widgets, Inc |
    Then I should get a status code of 200
    And the response value of "name" should equal "Used Widgets, Inc"

  ## Uses file & data table view
  ## showcases the faker library
  ## and the use of the cache, by
  ## validating the dynamic data sent over
  @Positive
  Scenario: Update nested element (UPDATE2)
    Given I am a JSON API consumer
    And I am executing test "UPDATE2A"
    When I request POST "/vendor"
    And I set the JSON body from file "src/test/resources/data/payload.json"
    And record the response as "UPDATE2"
    And I should get a status code of 200
    And I am a JSON API consumer
    And I am executing test "UPDATE2-B"
    When I request POST "/vendor/{{response::UPDATE2->id}}"
    And I set the JSON body from values
      | contactDetails[0].email       | test@test.com                                  |
    Then I should get a status code of 200
    And the response value of "contactDetails[0].email" should equal "test@test.com"

  @Negative
  Scenario: Attempt update with invalid ID (UPDATE3)
    Given I am a JSON API consumer
    And I am executing test "UPDATE3"
    When I request POST "/vendor/xxxxxxxxxxxxxxx"
    And I set the JSON body from file "src/test/resources/data/payload.json"
    Then I should get a status code of 404
    And the response value of "[0].message" should equal "Not Found"

