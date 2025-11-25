package step_definitions;

import context.PersonContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import util.ConfigReader;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPageSteps {
    private final LoginPage loginPage;

    public LoginPageSteps(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    @Given("I navigate to the OrangeHRM Login page")
    public void i_navigate_to_the_orange_hrm_login_page() {
        loginPage.navigateToLoginPage();
    }

    @And("I enter username")
    public void i_enter_username() {
        loginPage.fillTextFieldWithGetByPlaceholder("Username", ConfigReader.getProperty("test.username"));
    }

    @And("I enter password")
    public void i_enter_password() {
        loginPage.fillTextFieldWithGetByPlaceholder("Password", ConfigReader.getProperty("test.password"));
    }

    @And("I click the Login button")
    public void i_click_the_login_button() {
        loginPage.clickLoginButton();
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        assertThat(loginPage.getDashboardLocator()).hasText("Dashboard");
    }

    @Given("I am on the dashboard")
    public void i_am_on_the_dashboard() {
        String baseUrl = ConfigReader.getProperty("URL");
        String dashboardUrl = baseUrl.replace("/auth/login", "/dashboard/index");
        loginPage.navigateToURL(dashboardUrl);

        assertThat(loginPage.getDashboardLocator()).hasText("Dashboard");
    }
}
