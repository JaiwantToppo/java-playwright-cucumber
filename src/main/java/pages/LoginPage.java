package pages;

import browser.BrowserManager;
import com.microsoft.playwright.Locator;
import pages.base.BasePage;
import util.ConfigReader;

public class LoginPage extends BasePage {
    public LoginPage(BrowserManager browserManager) {
        super(browserManager);
    }

    public void navigateToLoginPage() {
        String loginUrl = ConfigReader.getProperty("URL");
        navigateToURL(loginUrl);
    }

    public void enterCredentials(String username, String password) {
        fillTextFieldWithGetByPlaceholder("Username", username);
        fillTextFieldWithGetByPlaceholder("Password", password);
    }

    public void clickLoginButton() {
        clickWithGetByRole("button", "Login");
    }

    public void login(String username, String password) {
        navigateToLoginPage();
        enterCredentials(username, password);
        clickLoginButton();
    }
    public Locator getDashboardLocator() {
        return waitForLocator("header h6", 5000);
    }
}
