Feature: Search Functionality

  # ========== POSITIVE SCENARIOS ==========

  Scenario Outline: Search with valid keyword shows results

    When user searches for "<keyword>"
    Then search results should be displayed
    And results count should be greater than 0

    Examples:
      | keyword |
      | Zahrawin Jalabiya (ZJ442)   |
      | jalabiya  |
      | zahra     |


  # ========== NEGATIVE SCENARIOS ==========

  Scenario Outline: Search with invalid keyword shows no results

    When user searches for "<keyword>"
    Then user should see no results message

    Examples:
      | keyword          |
      | xyzinvalidprod99 |
      | aaaabbbbcccc1234 |




  Scenario Outline: Search with special characters does not crash

    When user searches for "<keyword>"
    Then no application error is displayed

    Examples:
      | keyword   |
      | @#$%^&*   |
      | 123456789 |


  # ========== CASE SENSITIVITY ==========

  Scenario Outline: Search is case-insensitive

    When user searches for "<keyword>"
    Then search results should be displayed

    Examples:
      | keyword  |
      | JALABIYA |
      | ZAHRA    |


  # ========== WHITESPACE HANDLING ==========

  Scenario Outline: Search trims whitespace and returns results

    When user searches for "<keyword>"
    Then search results should be displayed

    Examples:
      | keyword      |
      |  jalabiya    |
      |  zahra       |


  # ========== PARTIAL KEYWORD MATCH ==========

  Scenario Outline: Partial keyword search returns results

    When user searches for "<keyword>"
    Then search results should be displayed
    And results count should be greater than 0

    Examples:
      | keyword |
      | jala    |
      | zahra   |


  # ========== BOUNDARY TESTS ==========


  Scenario: Very long search keyword does not crash

    When user searches for "thisisaverylongkeywordthatisunlikelytomatchanyproductxyzabc12345"
    Then no application error is displayed


  Scenario: Single character search does not crash

    When user searches for "a"
    Then no application error is displayed
