package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.FilterPage;
import utils.ConfigReader;
import utils.DriverFactory;

public class FabricMaterialFilterSteps {

    private FilterPage filterPage;

    public FabricMaterialFilterSteps() {
        WebDriver driver = DriverFactory.getDriver();
        this.filterPage = new FilterPage(driver);
    }

    // ── Background ──────────────────────────────────────

    @Given("user opens the Jalabiya products page")
    public void user_opens_jalabiya_page() {
        filterPage.navigateTo(new ConfigReader().getJalabiyaUrl());
        System.out.println("[STEP] Jalabiya page khuli");
    }

    // ── Fabric Material Section Visibility ──────────────

    @Then("the Fabric Material filter section should be visible")
    public void fabric_material_section_visible() {
        boolean visible = filterPage.isFabricMaterialSectionVisible();
        Assert.assertTrue(visible, "Fabric Material section visible nahi hai!");
    }

    // ── Checkbox Select / Deselect ──────────────────────

    @When("user clicks on fabric material checkbox {string}")
    public void user_clicks_fabric_checkbox(String fabricName) throws InterruptedException {
        filterPage.clickFabricCheckbox(fabricName);
        System.out.println("[STEP] Fabric checkbox click kiya: " + fabricName);
        Thread.sleep(1000);
    }

    @Then("the checkbox {string} should be selected")
    public void checkbox_should_be_selected(String fabricName) throws InterruptedException {
    	Thread.sleep(2000);
        boolean selected = filterPage.isFabricCheckboxSelected(fabricName);
        Assert.assertTrue(selected, fabricName + " checkbox selected nahi hai!");
    }

    @Then("the checkbox {string} should be deselected")
    public void checkbox_should_be_deselected(String fabricName) {
        boolean selected = filterPage.isFabricCheckboxSelected(fabricName);
        Assert.assertFalse(selected, fabricName + " checkbox abhi bhi selected hai — deselect nahi hua!");
    }

    // ── Neck Pattern Accordion ──────────────────────────

    @Then("the Neck Pattern filter section should be collapsed")
    public void neck_pattern_should_be_collapsed() {
        boolean collapsed = filterPage.isFilterSectionCollapsed("Neck Pattern");
        Assert.assertTrue(collapsed, "Neck Pattern section collapsed nahi hai!");
    }

    @Then("the {string} filter section should be collapsed by default")
    public void filter_section_should_be_collapsed_by_default(String sectionName) {
        boolean collapsed = filterPage.isFilterSectionCollapsed(sectionName);
        Assert.assertTrue(collapsed, sectionName + " section collapsed nahi hai by default!");
    }

    @When("user clicks to expand the {string} filter section")
    public void user_expands_filter_section(String sectionName) {
        filterPage.expandFilterSection(sectionName);
        System.out.println("[STEP] Section expand kiya: " + sectionName);
    }

    @Then("the {string} filter options should be visible")
    public void filter_options_should_be_visible(String sectionName) {
        boolean visible = filterPage.isFilterSectionExpanded(sectionName);
        System.out.println(visible +"------------" + sectionName);
        Assert.assertTrue(visible, sectionName + " options visible nahi hain!");
    }

    @When("user selects any first option in {string}")
    public void user_selects_first_option_in_section(String sectionName) {
        filterPage.selectFirstOptionInSection(sectionName);
        System.out.println("[STEP] " + sectionName + " mein pehla option select kiya");
    }
}
