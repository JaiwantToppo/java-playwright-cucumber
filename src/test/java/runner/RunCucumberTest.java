package runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@CucumberOptions(features = "src/test/resources/features", glue = "step_definitions",
        plugin = {"pretty", "json:src/test/test-output/Report/json-report/cucumber-reports.json",
                            "html:src/test/test-output/Report/html-report/cucumber-reports.html",
                            "rerun:src/test/test-output/Report/rerun-report/rerun.txt",
                            "timeline:src/test/test-output/Report/timeline",
                            "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}, monochrome = true, dryRun = false, publish = false)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
    private static final Logger logger = Logger.getLogger(RunCucumberTest.class.getName());
    private static final Properties properties = new Properties();

    static {
        Path configPath = Paths.get(System.getProperty("config.path",
                Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "config.properties").toString()));
        try(InputStream inputStream = Files.newInputStream(configPath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load config.properties");
        }
    }

//    public static void main(String[] args) {
//        // Create an instance of TestNG
//        TestNG testNG = new TestNG();
//
//        // Create an instance of XML Suite
//        XmlSuite xmlSuite = new XmlSuite();
//
//        int threadCount = getThreadCount();
//
//        // Setting the number of threads to the data provider
//        xmlSuite.setDataProviderThreadCount(threadCount);
//
//        // Creating a new TestNG test and adding to the suite
//        XmlTest test = new XmlTest(xmlSuite);
//        test.setName("Cucumber Tests");
//
//        // Adding the test class to the test
//        test.setXmlClasses(Collections.singletonList(new XmlClass(RunCucumberTest.class)));
//        testNG.setUseDefaultListeners(false);
//
//        // Adding the suite to the TestNG listener
//        testNG.setXmlSuites(Collections.singletonList(xmlSuite));
//
//        //Run TestNG with suite
//        testNG.run();
//    }

//    public static int getThreadCount() {
//        String threadCountStr = System.getProperty("thread.count");
//        if (threadCountStr != null && !threadCountStr.isEmpty()) {
//            try {
//                return Integer.parseInt(threadCountStr);
//            } catch (NumberFormatException e) {
//                logger.log(Level.WARNING, "Invalid thread.count system property, falling back to config.properties", e);
//            }
//        }
//        return Integer.parseInt(properties.getProperty("thread.count", "3"));
//    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
