@Insert
Feature: Testing out INSERT operations for the API

  ## This example uses inline JSON
  @Negative
  Scenario: Insert fails, invalid email
    Given I am a JSON API consumer
    And I am executing test "INSERT1"
    When I request POST "/vendor"
     And I set the JSON body to
    ```
    {
      "name": "Widgets. Incorporated",
      "description": "They are a widget company!",
      "contactDetails": [
        {
          "detailType": "Office",
          "email": "widgets@",
          "phone": "617-555-5555"
        }
      ]
    }
    ```
    Then I should get a status code of 406
    And the response value of "[0].code" should equal "contactDetails[].email"

  ## Uses data table view and
  ## showcases the faker library
  ## and the use of the cache, by
  ## validating the dynamic data sent over
  @Positive
  Scenario: Create an entity with dynamic values (INSERT2)
    Given I am a JSON API consumer
    And I am executing test "INSERT2"
    When I request POST "/vendor"
    And I set the JSON body from values
      | name                          | {{faker::Company.name,en-US::name}}            |
      | description                   | {{faker::Company.bs,en-US}}                    |
      | contactDetails[0].detailType  | Office                                         |
      | contactDetails[0].email       | example@example.com                            |
      | contactDetails[0].phone       | {{faker::phoneNumber.cellPhone,en-US}}         |
    Then I should get a status code of 200
    And the response value of "name" should equal "{{cache::name}}"


  ## Uses data table view and
  ## showcases how to match returned elements in list
  @Negative
  Scenario: Create fails due to missing contact details (INSERT3)
    Given I am a JSON API consumer
    And I am executing test "INSERT3"
    When I request POST "/vendor"
    And I set the JSON body from values
      | name                          | {{faker::Company.name,en-US::name}}            |
      | description                   | {{faker::Company.bs,en-US}}                    |
    Then I should get a status code of 406
     And the value "contactDetails" must occur at least 1 times for "code"


  ## Uses data table view and
  ## showcases another way to match returned elements in list
  @Negative
  Scenario: Create fails due to missing name & contact details (INSERT4)
    Given I am a JSON API consumer
    And I am executing test "INSERT4"
    When I request POST "/vendor"
    And I set the JSON body from values
      | description                   | {{faker::Company.bs,en-US}}                    |
    Then I should get a status code of 406
    Then path "code" must occur at least 2 times
