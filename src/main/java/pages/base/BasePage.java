package pages.base;

import browser.BrowserManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class BasePage {
    private final BrowserManager browserManager;

    public BasePage(BrowserManager browserManager) {
        this.browserManager = browserManager;
    }

    protected BrowserManager getBrowserManager() {
        return browserManager;
    }

    public void navigateToURL(String url) {
        browserManager.getPage().navigate(url);
    }

    public void fillTextFieldWithGetByPlaceholder(String placeholderName, String value) {
        browserManager.getPage().getByPlaceholder(placeholderName).fill(value);
    }

    public void clickWithGetByRole(String roleType, String value) {
        browserManager.getPage().getByRole(AriaRole.valueOf(roleType.toUpperCase()), new Page.GetByRoleOptions().setName(value)).click();
    }

    public Locator waitForLocator(String selector, double timeout) {
        browserManager.getPage().waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout));
        return browserManager.getPage().locator(selector);
    }

    public void navigateToPage(String roleType, String value) {
        browserManager.setPage(browserManager.getBrowserContext().waitForPage(() -> clickWithGetByRole(roleType, value)));
    }
}
