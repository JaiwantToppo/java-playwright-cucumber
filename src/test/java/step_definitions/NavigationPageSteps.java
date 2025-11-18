package step_definitions;

import io.cucumber.java.en.When;
import pages.NavigationPage;

public class NavigationPageSteps {
    private final NavigationPage navigationPage;

    public NavigationPageSteps(NavigationPage navigationPage) {
        this.navigationPage = navigationPage;
    }

    @When("I navigate to section {string}")
    public void i_navigate_to_section(String sectionName) {
        navigationPage.clickSection(sectionName);
    }
}
