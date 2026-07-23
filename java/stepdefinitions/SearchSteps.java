package stepdefinitions;

import com.aventstack.extentreports.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.SearchPage;
import utils.DriverFactory;
import utils.ExtentReportManager;
import utils.Log;

public class SearchSteps {

    SearchPage searchPage = new SearchPage(DriverFactory.getDriver());

    @When("user searches for {string}")
    public void user_searches_for(String keyword) {
        Log.info("STEP: Searching for: " + keyword);
        ExtentReportManager.getTest().log(Status.INFO, "Searching for: " + keyword);
        try {
            searchPage.searchFor(keyword);
            Log.pass("Search submitted for: " + keyword);
            ExtentReportManager.getTest().log(Status.PASS, "Search submitted: " + keyword);
        } catch (Exception e) {
            Log.error("Search failed for: " + keyword, e);
            ExtentReportManager.getTest().log(Status.FAIL, "Search failed: " + keyword);
            throw e;
        }
    }

    @Then("search results should be displayed")
    public void search_results_should_be_displayed() {
        Log.info("STEP: Verifying search results are displayed");
        ExtentReportManager.getTest().log(Status.INFO, "Checking search results visibility");
        try {
            Assert.assertTrue(searchPage.isSearchResultsDisplayed(), "Search results should be displayed");
            Log.pass("Search results are visible");
            ExtentReportManager.getTest().log(Status.PASS, "Search results displayed");
        } catch (AssertionError e) {
            Log.fail("Search results not displayed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Search results not displayed");
            throw e;
        }
    }

    @And("results count should be greater than {int}")
    public void results_count_should_be_greater_than(int minCount) {
        Log.info("STEP: Verifying results count > " + minCount);
        ExtentReportManager.getTest().log(Status.INFO, "Checking results count > " + minCount);
        try {
            int count = searchPage.getResultsCount();
            Assert.assertTrue(count > minCount, "Expected results count > " + minCount + " but got: " + count);
            Log.pass("Results count is " + count);
            ExtentReportManager.getTest().log(Status.PASS, "Results count: " + count);
        } catch (AssertionError e) {
            Log.fail("Results count check failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Results count check failed");
            throw e;
        }
    }

    @Then("user should see no results message")
    public void user_should_see_no_results_message() {
        Log.info("STEP: Verifying no results message is shown");
        ExtentReportManager.getTest().log(Status.INFO, "Checking no results message");
        try {
            Assert.assertTrue(searchPage.isNoResultsMessageDisplayed(), "No results message should be displayed");
            Log.pass("No results message is shown");
            ExtentReportManager.getTest().log(Status.PASS, "No results message displayed");
        } catch (AssertionError e) {
            Log.fail("No results message not found", e);
            ExtentReportManager.getTest().log(Status.FAIL, "No results message not found");
            throw e;
        }
    }

    @When("user submits empty search")
    public void user_submits_empty_search() {
        Log.info("STEP: Submitting empty search");
        ExtentReportManager.getTest().log(Status.INFO, "Submitting empty search");
        try {
            searchPage.submitEmptySearch();
            Log.pass("Empty search submitted");
            ExtentReportManager.getTest().log(Status.PASS, "Empty search submitted");
        } catch (Exception e) {
            Log.error("Empty search submission failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Empty search submission failed");
            throw e;
        }
    }

    @Then("user should remain on the homepage")
    public void user_should_remain_on_the_homepage() {
        Log.info("STEP: Verifying user is still on homepage");
        ExtentReportManager.getTest().log(Status.INFO, "Checking homepage URL");
        try {
            Assert.assertTrue(searchPage.isOnHomepage(), "User should remain on the homepage after empty search");
            Log.pass("User is on homepage");
            ExtentReportManager.getTest().log(Status.PASS, "User on homepage");
        } catch (AssertionError e) {
            Log.fail("User navigated away from homepage", e);
            ExtentReportManager.getTest().log(Status.FAIL, "User not on homepage");
            throw e;
        }
    }

    @Then("no application error is displayed")
    public void no_application_error_is_displayed() {
        Log.info("STEP: Verifying no application error");
        ExtentReportManager.getTest().log(Status.INFO, "Checking for application errors");
        try {
            Assert.assertFalse(searchPage.isApplicationErrorDisplayed(), "No application error should be displayed");
            Log.pass("No application error found");
            ExtentReportManager.getTest().log(Status.PASS, "No application error");
        } catch (AssertionError e) {
            Log.fail("Application error detected", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Application error detected");
            throw e;
        }
    }
}
