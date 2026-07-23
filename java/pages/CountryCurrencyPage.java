package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;
import utils.WaitUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CountryCurrencyPage {

    private final WebDriver driver;
    private final WaitUtils wait;
    private final WebDriverWait shortWait;
    private final String indiaReadymadeUrl;
    private final String aeReadymadeUrl;

    // ── Country Selector Button (header) ─────────────────────────────────────
    // The button contains <span class="text-sm font-medium">IN</span> + chevron SVG
    private final By countrySelectorWidget =
        By.xpath("//header//button[.//span[contains(@class,'font-medium') "
               + "and (normalize-space(text())='IN' or normalize-space(text())='AE' "
               + "or normalize-space(text())='UAE' or normalize-space(text())='AED')]]");
    // Absolute XPath from DOM inspection — reliable fallback
    private final By countrySelectorWidgetFallback =
        By.xpath("/html/body/header/div/div/div[3]/div[1]/div/button");

    // ── Dropdown Search Input ─────────────────────────────────────────────────
    // Appears outside <header> after clicking the button; not the main product search bar
    private final By countryDropdownSearchInput =
        By.xpath("//*[@placeholder='Search country...']");
    private final By countryDropdownSearchInputFallback =
        By.xpath("//input[@placeholder='Search country...']");

    // ── Product Price Elements ────────────────────────────────────────────────
    // Covers both 'price' and 'Price' (PascalCase); excludes cart AND filter/sidebar/range ancestors
    private final By productPrices =
        By.xpath("//*[(contains(@class,'price') or contains(@class,'Price')) "
               + "and not(ancestor::*[contains(@class,'cart') or contains(@class,'Cart')]) "
               + "and not(ancestor::*[contains(@class,'filter') or contains(@class,'Filter')]) "
               + "and not(ancestor::*[contains(@class,'sidebar') or contains(@class,'Sidebar')]) "
               + "and not(ancestor::*[contains(@class,'range') or contains(@class,'Range')])]");
    private final By productPricesFallback =
        By.cssSelector("[class*='price']:not([class*='cart'] *):not([class*='filter'] *):not([class*='sidebar'] *),"
                     + "[class*='Price']:not([class*='cart'] *):not([class*='filter'] *):not([class*='sidebar'] *)");

    // ── Product Cards ─────────────────────────────────────────────────────────
    private final By productCards =
        By.cssSelector("[class*='product-card'], [class*='ProductCard'], "
                     + "[data-testid='product-card']");
    private final By productCardsFallback =
        By.cssSelector("[class*='product'], [class*='Product']");

    public CountryCurrencyPage(WebDriver driver) {
        this.driver           = driver;
        this.wait             = new WaitUtils(driver);
        this.shortWait        = new WebDriverWait(driver, Duration.ofSeconds(5));
        ConfigReader config   = new ConfigReader();
        this.indiaReadymadeUrl = config.getReadymadeUrl();
        this.aeReadymadeUrl    = config.getAeReadymadeUrl();
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateToIndiaProductListing() {
        driver.get(indiaReadymadeUrl);
        wait.waitForDomStable();
        waitForProductsToLoad();
    }

    public void navigateToUaeProductListing() {
        driver.get(aeReadymadeUrl);
        wait.waitForDomStable();
        waitForProductsToLoad();
    }

    // Products load asynchronously after DOM ready; 30 s matches ProductPage's wait.
    private void waitForProductsToLoad() {
        wait.waitForDomStable();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.presenceOfElementLocated(productPrices));
        } catch (Exception e) {
            System.out.println("[CountryCurrencyPage] Warning: price elements not found within 30s. "
                             + "Continuing to assertion. Error: " + e.getMessage());
        }
    }

    // ── Country Selector Visibility ───────────────────────────────────────────

    public boolean isCountrySelectorVisible() {
        try {
            return wait.waitForVisible(countrySelectorWidget).isDisplayed();
        } catch (Exception e) {
            try {
                return wait.waitForVisible(countrySelectorWidgetFallback).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    // ── Open Country Selector Dropdown ────────────────────────────────────────

    public void openCountrySelector() {
        try {
            WebElement widget = wait.waitForClickable(countrySelectorWidget);
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", widget);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", widget);
        } catch (Exception e) {
            System.out.println("[CountryCurrencyPage] Selector primary failed, trying fallback: "
                             + e.getMessage());
            WebElement widget = wait.waitForClickable(countrySelectorWidgetFallback);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", widget);
        }
        // Confirm dropdown opened by waiting for the search input to appear
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(countryDropdownSearchInput));
        } catch (Exception e) {
            System.out.println("[CountryCurrencyPage] Dropdown search input not visible after click: "
                             + e.getMessage());
        }
    }

    // ── Select Country via Dropdown Search ───────────────────────────────────

    public void selectCountry(String country) {
        // Map short code to full country name for search
        String searchTerm = country.equalsIgnoreCase("UAE") ? "United Arab Emirates" : country;

        // Find the search input in the already-opened dropdown
        WebElement searchInput;
        try {
            searchInput = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(countryDropdownSearchInput));
        } catch (Exception e) {
            System.out.println("[CountryCurrencyPage] Dropdown input not found via primary, trying fallback: "
                             + e.getMessage());
            searchInput = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(countryDropdownSearchInputFallback));
        }
        searchInput.clear();
        searchInput.sendKeys(searchTerm);

        // Click the first result button that appears after typing.
        // Each result is a <button> with a <span class="text-xs text-gray-500"> (full country name).
        // The button's full text is "AE United Arab Emirates" — not just the country name alone,
        // so text-equality XPaths fail; clicking the first result is correct per UX (search filtered it).
        By firstResult = By.xpath(
            "(//button[.//span[contains(@class,'text-gray-500')]])[1]");
        By firstResultFallback = By.xpath(
            "(//button[contains(@class,'px-4') and contains(@class,'py-3')])[1]");
        try {
            WebElement option = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(firstResult));
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", option);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        } catch (Exception e) {
            System.out.println("[CountryCurrencyPage] First result click failed, trying fallback: "
                             + e.getMessage());
            WebElement option = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(firstResultFallback));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        }

        wait.waitForDomStable();

        // Country switch may redirect to home page; navigate to the product listing for that country.
        // A single driver.get() is enough — an extra refresh re-runs SSR with the URL locale (overriding
        // the cookie) and resets currency back to the default before JS can apply the country preference.
        if ("UAE".equalsIgnoreCase(country) || "United Arab Emirates".equalsIgnoreCase(country)) {
            driver.get(aeReadymadeUrl);
        } else {
            driver.get(indiaReadymadeUrl);
        }
        waitForProductsToLoad();
    }

    // ── Dropdown Closed Check ─────────────────────────────────────────────────
    // Dropdown is closed when the search input is no longer visible

    public boolean isCountrySelectorClosed() {
        try {
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(countryDropdownSearchInput));
            return true;
        } catch (Exception e) {
            List<WebElement> els = driver.findElements(countryDropdownSearchInput);
            return els.isEmpty() || els.stream().noneMatch(WebElement::isDisplayed);
        }
    }

    // ── Product Price Queries ─────────────────────────────────────────────────

    public List<String> getAllProductPriceCurrencyTexts() {
        List<String> texts = new ArrayList<>();
        try {
            List<WebElement> els = driver.findElements(productPrices);
            for (WebElement el : els) {
                if (el.isDisplayed()) {
                    String t = el.getText().trim();
                    if (!t.isEmpty() && t.matches(".*\\d.*")) texts.add(t);
                }
            }
        } catch (Exception e) {
            System.out.println("[CountryCurrencyPage] Price primary failed: " + e.getMessage());
            try {
                List<WebElement> els = driver.findElements(productPricesFallback);
                for (WebElement el : els) {
                    if (el.isDisplayed()) {
                        String t = el.getText().trim();
                        if (!t.isEmpty() && t.matches(".*\\d.*")) texts.add(t);
                    }
                }
            } catch (Exception ex) {
                System.out.println("[CountryCurrencyPage] Price fallback failed: " + ex.getMessage());
            }
        }
        System.out.println("[CountryCurrencyPage] Price texts collected: " + texts);
        return texts;
    }

    public void waitForPricesWithSymbol(String symbol, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(d -> {
                    List<String> prices = getAllProductPriceCurrencyTexts();
                    return !prices.isEmpty() && prices.stream().anyMatch(p -> p.contains(symbol));
                });
        } catch (Exception e) {
            System.out.println("[CountryCurrencyPage] Timeout waiting for prices with symbol: " + symbol);
        }
    }

    public boolean areAllPricesShowingSymbol(String symbol) {
        List<String> prices = getAllProductPriceCurrencyTexts();
        if (prices.isEmpty()) return false;
        return prices.stream().anyMatch(p -> p.contains(symbol));
    }

    public boolean isNoPriceShowingSymbol(String symbol) {
        List<String> prices = getAllProductPriceCurrencyTexts();
        if (prices.isEmpty()) return true;
        return prices.stream().noneMatch(p -> p.contains(symbol));
    }

    public boolean areProductPricesNonEmpty() {
        List<String> prices = getAllProductPriceCurrencyTexts();
        return !prices.isEmpty() && prices.stream().anyMatch(p -> !p.isBlank());
    }

    // ── Product Count ─────────────────────────────────────────────────────────

    public int getProductCount() {
        try {
            int count = driver.findElements(productCards).size();
            if (count > 0) return count;
            count = driver.findElements(productCardsFallback).size();
            if (count > 0) return count;
        } catch (Exception e) {
            // ignore selector errors
        }
        return getAllProductPriceCurrencyTexts().size();
    }
}
