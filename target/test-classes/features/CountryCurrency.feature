# ============================================================
# Feature : Country Currency Switch — WowFashions Staging
# Pages   : https://staging.wowfashions.ae/in-en/products-list/readymade
#           https://staging.wowfashions.ae/ae-en/products-list/readymade
# Flow    : Navigate to listing → open country selector in header
#           → click country → verify currency symbol on product prices
# ============================================================

@currencyswitch
Feature: Country Currency Switch on WowFashions

  Background:
    Given user is on the India product listing page

  # ── Positive Scenarios ──────────────────────────────────

  Scenario: TC01 - Country selector widget is visible in the header
    Then the country selector widget is visible in the header

  Scenario: TC02 - Default India page shows INR symbol on all product prices
    Then all product prices show the "₹" currency symbol

  Scenario: TC03 - Switching to BHD changes all product prices to BHD
    When user opens the country selector
    And  user selects country "Bahrain"
    Then all product prices show the "BHD" currency symbol

  Scenario: TC04 - Switching back to India from UAE restores INR prices
    When user opens the country selector
    And  user selects country "UAE"
    And  user opens the country selector
    And  user selects country "India"
    Then all product prices show the "₹" currency symbol

  Scenario: TC05 - All visible product prices (not just first) show BHD after INR switch
    When user opens the country selector
    And  user selects country "Bahrain"
    Then all visible product prices contain the "BHD" currency symbol

  Scenario Outline: TC06 - Country to currency mapping is correct
    When user opens the country selector
    And  user selects country "<country>"
    Then all product prices show the "<symbol>" currency symbol

    Examples:
      | country     | symbol |
      | India       | ₹      |

  Scenario: TC07 - Product listing still shows at least one product after country switch
    When user opens the country selector
    And  user selects country "Bahrain"
    Then the product listing shows at least one product

  # ── Negative Scenarios ──────────────────────────────────

  Scenario: TC08 - AED symbol is not visible when India is selected by default
    Then no product price shows the "AED" currency symbol

  Scenario: TC09 - INR symbol is not visible after switching to UAE
    When user opens the country selector
    And  user selects country "UAE"
    Then no product price shows the "₹" currency symbol

  Scenario: TC10 - Product prices are non-empty after country switch
    When user opens the country selector
    And  user selects country "UAE"
    Then product prices are non-empty after the country switch

  Scenario: TC11 - Country selector dropdown closes after a valid selection
    When user opens the country selector
    And  user selects country "UAE"
    Then the country selector dropdown is closed
