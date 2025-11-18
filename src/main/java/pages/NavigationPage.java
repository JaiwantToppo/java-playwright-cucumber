package pages;

import browser.BrowserManager;
import pages.base.BasePage;

public class NavigationPage extends BasePage {
    public NavigationPage(BrowserManager browserManager) {
        super(browserManager);
    }

    public void clickSection(String sectionName) {
        clickWithGetByRole("LINK", sectionName);
    }
}
