# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

BDD test automation framework for [WowFashions](https://wowfashions.ae/) — an e-commerce web app. Tests search, product filter, cart, sign-in, and fabric product scenarios using Cucumber/Selenium/TestNG with Java 17.

## Commands

```bash
# Run all tests
mvn test

# Run a specific runner
mvn test -Dtest=TestRunner

# Build without running tests
mvn clean install -DskipTests

# Clean build artifacts
mvn clean
```

To run a single scenario by tag, add a tag to the feature file and pass `-Dcucumber.filter.tags="@tagName"` to `mvn test`. The `SignIn.feature` is tagged `@signin` and `CountryCurrency.feature` is tagged `@currencyswitch` at the feature level.

## Architecture

**Pattern**: Page Object Model (POM) + Cucumber BDD + TestNG

```
java/
  base/BaseTest.java                         # Shared WebDriver accessor
  hooks/Hooks.java                           # @BeforeAll (open browser) / @Before (navigate + Extent test) / @After (screenshot on fail) / @AfterAll (flush report, quit)
  pages/
    SearchPage.java                          # Search input interaction + results/error assertions
    FilterPage.java                          # Price slider/input, fabric checkboxes, accordion sections, reset
    ProductPage.java                         # Product listing/detail: name, price, Add to Cart, cart badge
    CartPage.java                            # Cart page: item count, name/price, remove, empty-cart state
    SignInPage.java                          # Email input, Send OTP, OTP entry (single field or 6-digit boxes), Verify, logout
    CountryCurrencyPage.java                 # Country selector widget, currency symbol verification on product prices
    FabricsProductPage.java                  # Fabric product listing/detail: name, price, Add to Cart on fabric page
  runners/TestRunner.java                    # Cucumber/TestNG entry point; glue = stepdefinitions + hooks
  stepdefinitions/
    SearchSteps.java                         # Maps Search.feature steps → SearchPage
    FilterSteps.java                         # Maps Filter.feature steps → FilterPage
    FabricMaterialFilterSteps.java           # Maps FabricMaterialFilter.feature steps → FilterPage
    AddToCartSteps.java                      # Maps AddToCart.feature steps → ProductPage + CartPage
    FabricsAddToCartSteps.java               # Maps FabricsAddToCart.feature steps → FabricsProductPage + CartPage
    SignInSteps.java                         # Maps SignIn.feature steps → SignInPage
    CountryCurrencySteps.java                # Maps CountryCurrency.feature steps → CountryCurrencyPage
  utils/
    ConfigReader.java                        # Reads config.properties: browser, URLs (base/cart/readymade/darziya/jalabiya/signin/ae-readymade/fabrics), demo credentials
    DriverFactory.java                       # Singleton Chrome WebDriver (lazy init via WebDriverManager)
    WaitUtils.java                           # Explicit wait helpers (10s default); also waitForDomStable() via JS readyState
    ScreenshotUtil.java                      # Timestamped failure screenshots → screenshots/{scenario}_{yyyyMMdd_HHmmss}.png
    ExtentReportManager.java                 # Singleton Spark HTML report (dark theme, ThreadLocal ExtentTest)
    Log.java                                 # SLF4J wrapper; Log.pass() / Log.fail() emit ✅/❌ prefixed messages

resources/
  config/config.properties                  # browser, all page URLs, demo credentials (email + OTP)
  features/
    Search.feature                           # Search positive/negative/boundary scenarios (~8 scenarios)
    Filter.feature                           # Price range filter scenarios, Background: Darziya page (~10 scenarios)
    FabricMaterialFilter.feature             # Fabric checkbox + Neck Pattern accordion, Background: Jalabiya page (~10 scenarios)
    AddToCart.feature                        # Add to cart, cart verification, item removal, Background: readymade page (~11 scenarios)
    SignIn.feature                           # OTP sign-in flow, validation, logout (~9 scenarios; @signin tag)
    CountryCurrency.feature                  # Country selector, INR/BHD/AED currency symbol verification (~9 scenarios; @currencyswitch tag)
    FabricsAddToCart.feature                 # Fabric product add-to-cart flow, Background: fabrics page (~8 scenarios)
```

**Driver lifecycle**: `DriverFactory` holds a static singleton. `Hooks.@BeforeAll` opens the browser once per suite; `@Before` navigates to the configured URL and creates an Extent test node; `@After` attaches a failure screenshot (as both byte array to scenario and PNG file to Extent); `@AfterAll` flushes the Extent report and quits the driver.

**Reporting**: Cucumber HTML (`reports/cucumber-report.html`) + Extent Spark HTML (`reports/ExtentReport.html`). Both written to `reports/` in the project root. Extent uses dark theme and records System Info (App, Browser, Environment, URL).

**Locator strategy**: All page object locators use `contains()`, `normalize-space()`, and `[class*='...']` patterns — never exact class names — so they survive CSS Module hash changes. Every locator has at least one XPath/CSS fallback. `FilterPage` fabric checkbox clicks use a 4-tier fallback chain: label→checkbox relay, direct label click, ancestor climb, then JS click. Hidden `<input type="checkbox">` checked state is read via JS `.checked` because `display:none` makes `isSelected()` unreliable.

**State tracking across steps**: `AddToCartSteps` captures product name, price, and cart badge count before navigating to the cart page — Cucumber step definitions are instantiated once per scenario so instance fields persist within a scenario.

**Hardcoded sleeps**: `FabricMaterialFilterSteps` uses `Thread.sleep(1000)` after checkbox clicks and `Thread.sleep(2000)` after deselect to allow UI re-render that `WebDriverWait` doesn't reliably detect for hidden checkboxes.

**Test data**: Demo credentials in `config.properties` (`user.email=demo@wowfashions.ae`, `user.otp=123456`). `SignInPage.enterOtp()` supports both a single 6-character OTP field and 6 individual digit input boxes — detection is automatic.

## Key Structural Note

Active source files live in `java/` and `resources/` at the project root (Eclipse-style classpath, declared in `pom.xml` as `<testSourceDirectory>java</testSourceDirectory>`). The `src/test/java/` tree mirrors this structure but is unused — it contains only a stub `AppTest.java` and an old `LoginPage.java`. Do not edit files under `src/test/`.
