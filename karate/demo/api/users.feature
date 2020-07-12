
Feature: sample karate api test script
  for help, see: https://github.com/intuit/karate/wiki/ZIP-Release
  
  

  Background:
    * url 'http://localhost:8080/engine-rest'
    * configure proxy = 'http://de001-svr.zone2.proxy.allianz:8090'
  
  Scenario: get all deployments and look for camunda-invoice
    Given path 'deployment'
    When method get
    Then status 200

    * def first = response[1]

    Given path 'deployment', first.id
    When method get
    Then status 200

  Scenario: create a user and then get it by id
    * def user =
      """
      {
        "name": "Test User",
        "username": "testuser",
        "email": "test@user.com",
        "address": {
          "street": "Has No Name",
          "suite": "Apt. 123",
          "city": "Electri",
          "zipcode": "54321-6789"
        }
      }
      """

    Given url 'https://jsonplaceholder.typicode.com/users'
    And request user
    When method post
    Then status 201

    * def id = response.id
    * print 'created id is: ', id

    Given path id
    # When method get
    # Then status 200
    # And match response contains user
  