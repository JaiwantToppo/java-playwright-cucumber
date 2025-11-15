package step_definitions;

import browser.BrowserManager;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.HomePage;
import pages.base.BasePage;

public class HomePageSteps {
    private final HomePage homePage;
    public HomePageSteps(HomePage homePage) {
        this.homePage = homePage;
    }

    @Given("I navigate to the webdriveruniversity homepage")
    public void i_navigate_to_the_webdriveruniversity_homepage() {
        homePage.navigateToHomePage();
    }

    @When("I click on the contact us button")
    public void i_click_on_the_contact_us_button() {
        homePage.clickContactUsButton();
    }
}
