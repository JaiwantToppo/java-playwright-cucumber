package pages;

import browser.BrowserManager;
import com.microsoft.playwright.Locator;
import pages.base.BasePage;

public class ContactUsPage extends BasePage {
    public ContactUsPage(BrowserManager browserManager) {
        super(browserManager);
    }

    public void fillFirstName(String firstName) {
        fillTextFieldWithGetByPlaceholder("First Name", firstName);
    }

    public void fillLastName(String lastName) {
        fillTextFieldWithGetByPlaceholder("Last Name", lastName);
    }

    public void fillEmailAddress(String emailAddress) {
        fillTextFieldWithGetByPlaceholder("Email Address", emailAddress);
    }

    public void fillComment(String comment) {
        fillTextFieldWithGetByPlaceholder("Comment", comment);
    }

    public void clickSubmitButton() {
        clickWithGetByRole("BUTTON", "Submit");
    }

    public Locator getContactUsMessageLocator() {
        return waitForLocator("#contact_reply h1", 5000);
    }
}
