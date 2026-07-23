package java.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ================= LOCATORS =================

    // Country
    By countryDropdown = By.id("ccTrigger");
    By countrySearch = By.id("ccSearch");
    By searchedCountry = By.xpath("//*[@id='ccList']/div[3]");

    // Role
    By saasAdminRole = By.xpath("//button[normalize-space()='SaaS Admin']");
    By workforceManagerRole = By.xpath("//button[normalize-space()='Workforce Manager']");

    // Login
    By mobile = By.id("mobile");
    By password = By.id("password");
    By loginBtn = By.id("btnLogin");

    // Messages
    By toastMessage = By.xpath("//*[@id='toast-area']//p[2]");

    // ================= METHODS =================

    // Country Methods

    public void clickCountryDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(countryDropdown)).click();
    }

    public void searchCountry(String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(countrySearch)).clear();
        driver.findElement(countrySearch).sendKeys(value);
    }

    public void selectSearchedCountry() {
        wait.until(ExpectedConditions.elementToBeClickable(searchedCountry)).click();
    }

    // Role Methods

    public void selectRole(String role) {

        if (role.equalsIgnoreCase("SaaS Admin")) {
            wait.until(ExpectedConditions.elementToBeClickable(saasAdminRole)).click();

        } else if (role.equalsIgnoreCase("Workforce Manager")) {
            wait.until(ExpectedConditions.elementToBeClickable(workforceManagerRole)).click();
        }
    }

    // Login Methods

    public void enterMobile(String mobileValue) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mobile)).clear();
        driver.findElement(mobile).sendKeys(mobileValue);
    }

    public void enterPassword(String pass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(password)).clear();
        driver.findElement(password).sendKeys(pass);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
    }

    // Message Methods

    public String getErrorMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(toastMessage))
                .getText();
    }

    public String getValidationMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(toastMessage))
                .getText();
    }
}

