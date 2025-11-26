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

    public void fillTextFieldWithGetByLabel(String labelName, String value) {
        Locator parentLabel = browserManager.getPage().locator(".oxd-input-group:has(label:text-is('" + labelName + "'))");
        Locator inputField = parentLabel.locator("input, textarea");
        try {
            inputField.fill(value);
        } catch (Exception e) {
            System.out.println("Error filling text field with label '" + labelName + "': " + e.getMessage());
        }
    }

    public void clickWithGetByRole(String roleType, String value) {
        browserManager.getPage().getByRole(AriaRole.valueOf(roleType.toUpperCase()), new Page.GetByRoleOptions().setName(value)).click();
    }

    public Locator waitForLocator(String selector, double timeout) {
        browserManager.getPage().waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout));
        return browserManager.getPage().locator(selector);
    }

    public Locator textLocator(String text) {
        return browserManager.getPage().getByText(text);
    }

    public void selectOptionFromDropdown(String dropdownSelector, String optionText) {
        try {
            textLocator(dropdownSelector).click();
            browserManager.getPage().getByText(optionText).click();
        } catch (Exception e) {
            System.out.println("Error selecting option from dropdown: " + e.getMessage());
        }
    }

    public void selectOptionFromDropdownByLabel(String labelName, String optionText) {
        try {
            Locator dropdownLabel = browserManager.getPage().locator(String.format("//label[text()='%s']/ancestor::div[contains(@class, 'oxd-input-group')]", labelName));
            dropdownLabel.getByText("-- Select --").click();
            browserManager.getPage().getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(optionText)).click();
        } catch (Exception e) {
            System.out.println("Error selecting option from dropdown with label '" + labelName + "': " + e.getMessage());
        }
    }
}