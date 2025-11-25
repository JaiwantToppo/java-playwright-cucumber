package step_definitions;

import io.cucumber.java.en.And;
import pages.AdminPage;

public class AdminPageSteps {

    private final AdminPage adminPage;

    public AdminPageSteps(AdminPage adminPage) {
        this.adminPage = adminPage;
    }

    @And("I click the Add User button")
    public void i_click_add_user_button() {
        adminPage.clickAddUser();
    }

    @And("I set the user role to {string}")
    public void i_set_the_user_role_to(String userRole) {
        adminPage.selectOptionFromDropdown("-- Select --", userRole);
    }

    @And("I set the status to {string}")
    public void i_set_the_status_to(String status) {
        adminPage.selectOptionFromDropdown("-- Select --", status);
    }

    @And("I enter the employee name {string}")
    public void i_enter_the_employee_name(String employeeName) {
        adminPage.fillTextFieldWithGetByLabel("Employee Name", employeeName);
    }

    @And("I enter the username {string}")
    public void i_enter_the_username(String username) {
        adminPage.fillTextFieldWithGetByLabel("Username", username);
    }

    @And("I set the password {string}")
    public void i_enter_the_password(String password) {
        adminPage.fillTextFieldWithGetByLabel("Password", password);
    }

    @And("I confirm the password {string}")
    public void i_enter_the_confirm_password(String confirmPassword) {
        adminPage.fillTextFieldWithGetByLabel("Confirm Password", confirmPassword);
    }

    @And("I click the Save button")
    public void i_click_the_save_button() {
        adminPage.clickWithGetByRole("button", "Save");
    }

}
