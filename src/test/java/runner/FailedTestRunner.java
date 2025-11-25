package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;


@CucumberOptions(features = "@src/test/test-output/rerun.txt", glue = "step_definitions", plugin = {
        "pretty",
        "json:src/test/test-output/Report/json-report/cucumber-reports.json",
        "html:src/test/test-output/Report/html-report/cucumber-reports.html"}, monochrome = true)
public class FailedTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
