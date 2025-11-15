package step_definitions;

import context.PersonContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import net.datafaker.Faker;
import pages.ContactUsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ContactUsPageSteps {

    private final ContactUsPage contactUsPage;
    private final Faker faker = new Faker();
    private final PersonContext personContext;

    public ContactUsPageSteps(ContactUsPage contactUsPage, PersonContext personContext) {
        this.contactUsPage = contactUsPage;
        this.personContext = personContext;
    }

    @And("I type a first name {word}")
    public void i_type_a_first_name(String firstName) {
        contactUsPage.fillFirstName(firstName);
    }

    @And("I type a last name {word}")
    public void i_type_a_last_name(String lastName) {
        contactUsPage.fillLastName(lastName);
    }

    @And("I enter an email address {string}")
    public void i_enter_an_email_address(String emailAddress) {
        contactUsPage.fillEmailAddress(emailAddress);
    }

    @And("I type a comment {string}")
    public void i_type_a_comment(String comment) {
        contactUsPage.fillComment(comment);
    }

    @And("I click on the submit button")
    public void i_click_on_the_submit_button() {
        contactUsPage.clickSubmitButton();
    }

    @Then("I should be presented with a successful contact us submission message {string}")
    public void i_should_be_presented_with_a_successful_contact_us_submission_message(String submissionMessage) {
        assertThat(contactUsPage.getContactUsMessageLocator()).hasText(submissionMessage);
    }

    @And("I type a first name")
    public void i_type_a_first_name() {
        String firstName = faker.name().firstName();
        contactUsPage.fillFirstName(firstName);
        personContext.setFirstName(firstName);
    }

    @And("I type a last name")
    public void i_type_a_last_name() {
        String lastName = faker.name().lastName();
        contactUsPage.fillLastName(lastName);
        personContext.setLastName(lastName);
    }

    @And("I enter an email address")
    public void i_enter_an_email_address() {
        String emailAddress = faker.internet().emailAddress();
        contactUsPage.fillEmailAddress(emailAddress);
        personContext.setEmailAddress(emailAddress);
    }

    @And("I type a comment")
    public void i_type_a_comment() {
        String comment = "Person " + personContext.getFirstName() + " " + personContext.getLastName() + "'s email address is " + personContext.getEmailAddress();
        contactUsPage.fillComment(comment);
    }
}
