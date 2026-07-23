# ============================================================
# Feature : Add to Cart — WowFashions Staging
# Page    : https://staging.wowfashions.ae/in-en/products-list/readymade
# Flow    : Navigate to readymade listing → click first product
#           → add to cart → cart page
# ============================================================

Feature: Add to Cart on WowFashions

  Background:
    Given user opens a readymade product page

  # ── Product Page Load ─────────────────────────────────────

  Scenario: TC01 - Product page loads successfully
    Then the product page loads successfully

  Scenario: TC02 - Product name is displayed
    Then the product name is displayed

  Scenario: TC03 - Product price is displayed
    Then the product price is displayed

  Scenario: TC04 - Add to Cart button is visible and clickable
    Then the Add to Cart button is visible
    And  the Add to Cart button is clickable

  # ── Add to Cart Actions ───────────────────────────────────

  Scenario: TC05 - Clicking Add to Cart shows success notification
    When user clicks the Add to Cart button
    Then a success notification is displayed

  Scenario: TC06 - Cart badge count increments after adding
    When user clicks the Add to Cart button
    Then the cart badge count increments

  # ── Cart Page ─────────────────────────────────────────────

  Scenario: TC07 - Cart shows the added product
    When user clicks the Add to Cart button
    And  user navigates to the cart page
    Then the cart contains the added product

  Scenario: TC08 - Product name in cart matches product page
    When user clicks the Add to Cart button
    And  user navigates to the cart page
    Then the product name in cart matches the product page

  Scenario: TC09 - Product price in cart is correct
    When user clicks the Add to Cart button
    And  user navigates to the cart page
    Then the product price in cart is correct

  Scenario: TC10 - User can remove item from cart
    When user clicks the Add to Cart button
    And  user navigates to the cart page
    And  user removes the item from the cart
    Then the cart shows an empty message

  Scenario: TC11 - Empty cart shows appropriate message
    When user navigates to the cart page
    And  user clears all items from the cart
    Then the cart shows an empty message
