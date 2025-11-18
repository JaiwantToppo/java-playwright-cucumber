package pages;

import browser.BrowserManager;
import pages.base.BasePage;

public class RecruitmentPage extends BasePage {
    public RecruitmentPage(BrowserManager browserManager) {
        super(browserManager);
    }

    public void clickAddCandidate() {
        clickWithGetByRole("button", "Add");
    }




}
