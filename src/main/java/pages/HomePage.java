package pages;

import browser.BrowserManager;
import pages.base.BasePage;

public class HomePage extends BasePage {
    public HomePage(BrowserManager browserManager) {
        super(browserManager);
    }

    public void navigateToHomePage() {
        navigateToURL("https://webdriveruniversity.com");
    }

    public void clickContactUsButton() {
        navigateToPage("LINK", "CONTACT US");
        getBrowserManager().getPage().bringToFront();
    }
}
