# ============================================================
# Feature : Sign In — WowFashions Staging
# Page    : https://staging.wowfashions.ae/in-en/sign-in
# Flow    : Enter email → Click Send OTP
#           → Enter 6-digit OTP → Click Verify and Continue
#           → Verify user is signed in
# ============================================================

@signin
Feature: Sign In on WowFashions

  Background:
    Given user is on the sign-in page

  # ── Positive Scenarios ────────────────────────────────────

  Scenario: TC01 - Sign-in page loads successfully
    Then the email input field is visible
    And  the Send OTP button is visible

  Scenario: TC02 - Valid email triggers OTP section
    When user enters valid email and clicks Send OTP
    Then the OTP input section is displayed

  Scenario: TC03 - Valid email and valid OTP results in successful sign-in
    When user enters valid email and clicks Send OTP
    And  user enters valid OTP and clicks Verify and Continue
    Then the user is successfully signed in

  # ── Negative Scenarios ───────────────────────────────────

  Scenario: TC04 - Send OTP button is disabled when email is empty
    Then the Send OTP button is disabled

  Scenario: TC05 - Invalid email format shows form validation error
    When user enters "notanemail" and clicks Send OTP
    Then a validation or error message is displayed

  Scenario: TC06 - Empty OTP field keeps Verify button disabled
    When user enters valid email and clicks Send OTP
    And  user leaves the OTP field empty
    Then the Verify and Continue button is disabled

  Scenario: TC07 - Wrong OTP shows error message
    When user enters valid email and clicks Send OTP
    And  user enters OTP "000000" and clicks Verify and Continue
    Then a validation or error message is displayed

  Scenario: TC08 - Short OTP keeps Verify button disabled
    When user enters valid email and clicks Send OTP
    And  user enters short OTP "123"
    Then the Verify and Continue button is disabled

  # ── Logout ───────────────────────────────────────────────

  Scenario: TC09 - User can successfully logout
    When user enters valid email and clicks Send OTP
    And  user enters valid OTP and clicks Verify and Continue
    And  user clicks account menu and logs out
    Then the user is redirected to the home page
