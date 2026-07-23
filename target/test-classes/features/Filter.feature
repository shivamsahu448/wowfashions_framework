# ============================================================
# Feature : Price Range Filter — WowFashions Staging
# Page    : https://staging.wowfashions.ae/in-en/brand/darziya
# UI      : Slider + Min Price input + Max Price input
# ============================================================

Feature: Price Range Filter on WowFashions

  Background:
    Given user opens the Darziya brand page

  # ── Happy Path ─────────────────────────────────────────────

  Scenario: Default price range is shown on page load
    Then the Min Price input should show "0"
    And  the Max Price input should show "50000"

  Scenario: User sets a custom Min Price using input box
    When user clears the Min Price input and types "500"
    And  user presses Tab to confirm
    Then the product grid should refresh
    And  the Min Price input should show "500"

  Scenario: User sets a custom Max Price using input box
    When user clears the Max Price input and types "2000"
    And  user presses Tab to confirm
    Then the product grid should refresh
    And  the Max Price input should show "2000"

  Scenario: User sets both Min and Max price range
    When user clears the Min Price input and types "300"
    And  user clears the Max Price input and types "1500"
    And  user presses Tab to confirm
    Then the product grid should refresh
    And  products displayed should be between "300" and "1500" in price

  Scenario: Setting Min Price higher than Max Price shows no results or error
    When user clears the Min Price input and types "9000"
    And  user clears the Max Price input and types "1000"
    And  user presses Tab to confirm
    Then either no products are shown or an error message is displayed

  Scenario: Setting Max Price to 0 shows no results
    When user clears the Max Price input and types "0"
    And  user presses Tab to confirm
    Then either no products are shown or an error message is displayed

  Scenario: Price slider moves when Min input value changes
    When user clears the Min Price input and types "1000"
    And  user presses Tab to confirm
    Then the left slider thumb position should reflect value "1000"

  # ── Boundary Values ────────────────────────────────────────

  Scenario Outline: Price range filter with boundary values
    When user clears the Min Price input and types "<min>"
    And  user clears the Max Price input and types "<max>"
    And  user presses Tab to confirm
    Then the product grid should refresh
    And  the Min Price input should show "<min>"
    And  the Max Price input should show "<max>"

    Examples:
      | min   | max   |
      | 0     | 50000 |
      | 100   | 500   |
      | 500   | 5000  |
      | 1000  | 10000 |
      | 5000  | 50000 |

  # ── Reset ──────────────────────────────────────────────────

  Scenario: Resetting price filter restores default range
    When user clears the Min Price input and types "1000"
    And  user clears the Max Price input and types "3000"
    And  user presses Tab to confirm
    And  user clicks the Reset or Clear Filters button
    Then the Min Price input should show "0"
    And  the Max Price input should show "50000"
    And  the product grid should refresh
