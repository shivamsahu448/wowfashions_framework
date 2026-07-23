@fabricscart
Feature: Fabrics Product Add to Cart on WowFashions

  Background:
    Given user opens a fabrics product page

  # ── Product Page Assertions ─────────────────────────────────

  Scenario: TC_F01 - Fabrics product page loads successfully
    Then the fabrics product page loads successfully

  Scenario: TC_F02 - Product name is displayed on the fabrics page
    Then the fabrics product name is displayed

  Scenario: TC_F03 - Product price is displayed on the fabrics page
    Then the fabrics product price is displayed

  Scenario: TC_F04 - Add to Cart button is visible on fabrics page
    Then the fabrics Add to Cart button is visible

  Scenario: TC_F05 - Add to Cart button is clickable on fabrics page
    Then the fabrics Add to Cart button is visible
    And the fabrics Add to Cart button is clickable

  # ── Add to Cart Actions ─────────────────────────────────────

  Scenario: TC_F06 - Success notification appears after adding fabric to cart
    When user clicks the fabrics Add to Cart button
    Then a fabrics success notification is displayed

  Scenario: TC_F07 - Cart badge count increments after adding fabric product
    When user clicks the fabrics Add to Cart button
    Then the fabrics cart badge count increments

  # ── Cart Page Verification ──────────────────────────────────

  Scenario: TC_F08 - Cart contains the added fabric product
    When user clicks the fabrics Add to Cart button
    And user navigates to the fabrics cart
    Then the fabrics cart contains the added product

  Scenario: TC_F09 - Product name in cart matches the fabric product added
    When user clicks the fabrics Add to Cart button
    And user navigates to the fabrics cart
    Then the fabrics product name in cart matches

  Scenario: TC_F10 - Product price in cart is correct for fabric product
    When user clicks the fabrics Add to Cart button
    And user navigates to the fabrics cart
    Then the fabrics product price in cart is correct

  # ── Quantity Functionality (Positive) ──────────────────────

  Scenario: TC_F11 - Cart quantity increases when increment button is clicked
    When user clicks the fabrics Add to Cart button
    And user navigates to the fabrics cart
    When user increments the fabrics cart quantity
    Then the fabrics cart quantity should increase by one

  Scenario: TC_F12 - Cart subtotal updates after quantity increase
    When user clicks the fabrics Add to Cart button
    And user navigates to the fabrics cart
    When user increments the fabrics cart quantity
    Then the fabrics cart subtotal should update

  # ── Quantity and Remove (Negative) ─────────────────────────

  Scenario: TC_F13 - Cart quantity does not go below 1 when decrement clicked at minimum
    When user clicks the fabrics Add to Cart button
    And user navigates to the fabrics cart
    When user decrements the fabrics cart quantity to minimum
    Then the fabrics cart quantity should not go below one

  Scenario: TC_F14 - Cart shows empty message after removing all items
    When user clicks the fabrics Add to Cart button
    And user navigates to the fabrics cart
    When user removes all fabrics items from cart
    Then the fabrics cart shows empty message

  Scenario: TC_F15 - Cart item count decreases after removing one item
    When user clicks the fabrics Add to Cart button
    And user navigates to the fabrics cart
    Then the fabrics cart contains the added product
    When user removes one fabrics item from cart
    Then the fabrics cart item count should decrease
