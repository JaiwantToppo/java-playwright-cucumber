package step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import pages.RecruitmentPage;
import pages.base.BasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RecruitmentPageSteps {
    private final RecruitmentPage recruitmentPage;
    private final BasePage basePage;

    public RecruitmentPageSteps(BasePage basePage, RecruitmentPage recruitmentPage) {
        this.basePage = basePage;
        this.recruitmentPage = recruitmentPage;
    }

    @And("I click the Add Candidate button")
    public void i_click_the_add_candidate_button() {
        recruitmentPage.clickAddCandidate();
    }

    @And("I enter the candidate first name {string}")
    public void i_enter_the_candidate_first_name(String firstName) {
        basePage.fillTextFieldWithGetByPlaceholder("First Name", firstName);
    }

    @And("I enter the candidate last name {string}")
    public void i_enter_the_candidate_last_name(String lastName) {
        basePage.fillTextFieldWithGetByPlaceholder("Last Name", lastName);
    }

    @And("I set the vacancy to {string}")
    public void i_set_the_vacancy_to(String vacancy) {
        basePage.selectOptionFromDropdown("-- Select --", vacancy);
    }

    @And("I enter the candidate email address {string}")
    public void i_enter_the_candidate_email_address(String emailAddress) {
        basePage.fillTextFieldWithGetByLabel("Email", emailAddress);
    }

    @And("I enter the candidate contact number {string}")
    public void i_enter_the_candidate_contact_number(String contactNumber) {
        basePage.fillTextFieldWithGetByLabel("Contact Number", contactNumber);
    }

    @And("I set the date of application to {string}")
    public void i_enter_the_date_of_application(String applicationDate) {
        basePage.fillTextFieldWithGetByPlaceholder("yyyy-dd-mm", applicationDate);
    }

    @And("I enter the notes {string}")
    public void i_enter_the_notes(String vacancy) {
        String note = "I'm applying for the role of " + vacancy + " because I believe my skills and experience make me a strong fit for this position.";
        basePage.fillTextFieldWithGetByLabel("Notes", note);
    }

    @And("I click on the save button")
    public void i_click_on_the_save_button() {
        basePage.clickWithGetByRole("button", "Save");
    }
}
