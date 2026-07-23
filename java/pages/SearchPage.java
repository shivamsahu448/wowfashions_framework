package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

public class SearchPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final ConfigReader config = new ConfigReader();

    private final By searchInput = By.cssSelector("input[type='search'], input[name='q'], input[placeholder*='earch']");
   // private final By searchButton = By.cssSelector("button[type='submit'], button[class*='search'], .search-btn");
    private final By searchResults = By.xpath("//div[contains(@class,'flex items-center gap-2 p-2')]");
    private final By noResultsMessage = By.cssSelector(".no-results, .empty-results, [class*='no-result'], [class*='empty']");
    private final By errorIndicator = By.cssSelector(".error-page, .server-error, [class*='error-500'], [class*='error-page']");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void searchFor(String keyword) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(keyword);
        input.sendKeys(Keys.ENTER);
    }

    public void submitEmptySearch() {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(Keys.ENTER);
    }

    public boolean isSearchResultsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchResults));
            List<WebElement> results = driver.findElements(searchResults);
            return !results.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public int getResultsCount() {
        try {
            List<WebElement> results = driver.findElements(searchResults);
            return results.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessage));
            return driver.findElement(noResultsMessage).isDisplayed();
        } catch (Exception e) {
            // also consider no results if result list is empty
            return driver.findElements(searchResults).isEmpty();
        }
    }

    public boolean isOnHomepage() {
        String currentUrl = driver.getCurrentUrl();
        String baseUrl = config.getUrl();
        return currentUrl.equals(baseUrl) || currentUrl.equals(baseUrl + "/") || currentUrl.equals(baseUrl + "/#");
    }

    public boolean isApplicationErrorDisplayed() {
        try {
            List<WebElement> errors = driver.findElements(errorIndicator);
            return !errors.isEmpty() && errors.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
