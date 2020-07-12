
Feature: sample karate api test script
  for help, see: https://github.com/intuit/karate/wiki/ZIP-Release
  
  

  Background:
    * url 'http://localhost:8080/engine-rest'
    #* configure proxy = 'http://de001-svr.zone2.proxy.allianz:8090'

  
    * def piStart =
      """
      {
        "variables": {
          "aVariable" : {
              "value" : "aStringValue","type": "String"
          },
          "forward" : {
              "value" : false,"type": "Boolean"
          }
        },
      "businessKey" : "1234"
      }
      """
      * def event1 =
      """
      {
        "messageName" : "dispatchEvent",
        "businessKey" : "1234",
        "processVariables" : {
          "event1Var" : {"value" : true, "type": "Boolean"}
        },
        "resultEnabled" : true
      }
      """
      * def event2 =
      """
      {
        "messageName" : "event2",
        "businessKey" : "1234",
        "processVariables" : {

          "event2_var" : {"value" : true, "type": "Boolean"}
        },
        
        "resultEnabled" : true,
        "variablesInResultEnabled" : true,
      }
      """
      * def event3 =
      """
      {
        "messageName" : "event3",
        "businessKey" : "1234",
        "processVariables" : {

          "event3_var" : {"value" : true, "type": "Boolean"}
        },
        
        "resultEnabled" : true,
        "variablesInResultEnabled" : true,
      }
      """
      * def event4 =
      """
      {
        "messageName" : "event4",
        "businessKey" : "1234",
        "processVariables" : {

          "event4_var" : {"value" : true, "type": "Boolean"}
        },
        
        "resultEnabled" : true,
        "variablesInResultEnabled" : true,
      }
      """

  Scenario: Start PI with businesskey and basic variables
    * def processDefinitionKey = 'event_dispatcher'
    Given path 'process-definition/key', processDefinitionKey ,'start'
    And request piStart
    When method post
    Then status 200
    
  Scenario: Deliver Event and set execution-local variable
    Given path 'message'
    And request event1
    When method post
    Then status 200
    * def executionID = response[0].execution.id

    Given path 'execution',executionID, 'localVariables/forward'
    And request {"value" : false, "type": "Boolean"}
    When method put
    Then status 204

    Given path 'execution',executionID, 'localVariables'
    When method get
    Then status 200
  Scenario: Start PI with businesskey and basic variables followed by 4 events
    * def processDefinitionKey = 'eventing_1'
    Given path 'process-definition/key', processDefinitionKey
    And request piStart
    When method get
    Then status 200
