@Insert
Feature: Testing out Insert operations for the API


  @Negative
  Scenario: Attempt delete with invalid ID (DELETE1)
    Given I am a JSON API consumer
    And I am executing test "DELETE1"
    When I request DELETE "/vendor/xxxxxxxxxxxxxxx"
    And I set the JSON body from file "src/test/resources/data/payload.json"
    Then I should get a status code of 404
    And the response value of "[0].message" should equal "Not Found"

  @Positive
  Scenario: Update non-nested element (DELETE2)
    Given I am a JSON API consumer
    And I am executing test "DELETE1-A"
    When I request POST "/vendor"
    And I set the JSON body from file "src/test/resources/data/payload.json"
    And record the response as "DELETE1"
    And I should get a status code of 200
    And I am a JSON API consumer
    And I am executing test "DELETE1-B"
   When I request DELETE "/vendor/{{response::DELETE1->id}}"
    Then I should get a status code of 200