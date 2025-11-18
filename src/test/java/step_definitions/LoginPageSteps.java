package step_definitions;

import context.PersonContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.LoginPage;

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

    @And("I enter username {string}")
    public void i_enter_username(String username) {
        loginPage.fillTextFieldWithGetByPlaceholder("Username", username);
    }

    @And("I enter password {string}")
    public void i_enter_password(String password) {
        loginPage.fillTextFieldWithGetByPlaceholder("Password", password);
    }

    @And("I click the Login button")
    public void i_click_the_login_button() {
        loginPage.clickLoginButton();
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        assertThat(loginPage.getDashboardLocator()).hasText("Dashboard");
    }
}
