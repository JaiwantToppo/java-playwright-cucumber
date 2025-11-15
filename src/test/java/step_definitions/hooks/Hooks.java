package step_definitions.hooks;

import browser.BrowserManager;
import io.cucumber.java.*;

import java.io.IOException;
import java.nio.file.Paths;

public class Hooks {
    private final BrowserManager browserManager;

    public Hooks(BrowserManager browserManager) {
        this.browserManager = browserManager;
    }

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Started execution of the Test Suite");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("Completed Execution of the Test Suite");
    }

    @Before
    public void setup() {
        browserManager.setUp();
    }

    @After
    public void teardown(Scenario scenario) throws IOException {
        if (scenario.isFailed()) {
            byte[] screenshot = browserManager.takeScreenshot();
            byte[] videoBytes = java.nio.file.Files.readAllBytes(Paths.get(browserManager.getPage().video().path().toString()));
            scenario.attach(screenshot, "image/png", "screenshot");
            scenario.attach(videoBytes, "video/webm", "Failed Scenario Video");
        }
        browserManager.tearDown();
    }
}
