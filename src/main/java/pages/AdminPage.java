package pages;

import browser.BrowserManager;
import pages.base.BasePage;

public class AdminPage extends BasePage {

    public AdminPage(BrowserManager browserManager) {
        super(browserManager);
    }

    public void clickAddUser() {
        clickWithGetByRole("button", "Add");
    }
}
