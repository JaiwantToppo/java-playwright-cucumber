package step_definitions.hooks;

import browser.BrowserManager;
import io.cucumber.java.*;
import io.qameta.allure.Allure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Hooks {
    private static final Logger logger = Logger.getLogger(Hooks.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static final String STORAGE_STATE_PATH = "src/test/test-output/storage-states/";

    // Track setup completion per auth context (feature or tag-based)
    private static final Map<String, Boolean> authContextSetup = new ConcurrentHashMap<>();

    private final BrowserManager browserManager;
    private boolean isTracingEnabled = false;
    private String scenarioName;

    public Hooks(BrowserManager browserManager) {
        this.browserManager = browserManager;
    }

    @BeforeAll
    public static void beforeAll() {
        logger.info("╔══════════════════════════════════════════════════════════╗");
        logger.info("║                                                          ║");
        logger.info("║          TEST SUITE EXECUTION STARTED                    ║");
        logger.info("║                                                          ║");
        logger.info("╚══════════════════════════════════════════════════════════╝");
        createDirectories();
    }

    @AfterAll
    public static void afterAll() {
        logger.info("╔══════════════════════════════════════════════════════════╗");
        logger.info("║                                                          ║");
        logger.info("║          TEST SUITE EXECUTION COMPLETED                  ║");
        logger.info("║                                                          ║");
        logger.info("╚══════════════════════════════════════════════════════════╝");
    }

    @Before(order = 0)
    public void setup(Scenario scenario) {
        scenarioName = scenario.getName();
        logger.info("┌─────────────────────────────────────────────────────────┐");
        logger.info("│ Starting Scenario: " + scenarioName);
        logger.info("│ Thread ID: " + Thread.currentThread().getId());
        logger.info("│ Tags: " + scenario.getSourceTagNames());
        logger.info("└─────────────────────────────────────────────────────────┘");

        browserManager.setUp();

        boolean traceOnFailure = Boolean.parseBoolean(
                browserManager.properties.getProperty("trace.on.failure", "true")
        );
        if (traceOnFailure) {
            String traceName = sanitizeFileName(scenarioName) + "_" + dateFormat.format(new Date());
            browserManager.startTracing(traceName);
            isTracingEnabled = true;
        }
    }

    @Before(value = "@RequiresAuth", order = 1)
    public void setupWithAuth(Scenario scenario) {
        logger.info("🔐 Setting up authentication for Scenario: " + scenario.getName());

        // Determine auth context - use custom tag if present, otherwise feature name
        String authContext = getAuthContext(scenario);
        String storageStatePath = STORAGE_STATE_PATH + authContext + "-auth.json";

        logger.info("Auth Context: " + authContext);
        logger.info("Storage State Path: " + storageStatePath);

        // Wait for auth file if this context hasn't been set up yet
        if (!isAuthContextReady(authContext)) {
            logger.info("⏳ Waiting for @Setup to complete for context: " + authContext);
            waitForAuthFile(storageStatePath, 60);
            markAuthContextReady(authContext);
        }

        // Load the storage state
        if (Files.exists(Paths.get(storageStatePath))) {
            if (browserManager.getBrowserContext() != null) {
                browserManager.getBrowserContext().close();
            }
            browserManager.createContextWithStorageState(storageStatePath);
            logger.info("✅ Loaded storage state from: " + storageStatePath);
        } else {
            logger.warning("⚠️ Storage state file not found at: " + storageStatePath);
        }
    }

    @After(order = 0)
    public void teardown(Scenario scenario) {
        String status = scenario.isFailed() ? "❌ FAILED" : "✅ PASSED";
        logger.info("┌─────────────────────────────────────────────────────────┐");
        logger.info("│ Finishing Scenario: " + scenarioName);
        logger.info("│ Status: " + status);
        logger.info("└─────────────────────────────────────────────────────────┘");

        try {
            if (scenario.isFailed()) {
                handleFailure(scenario);
            }

            boolean screenshotFailure = Boolean.parseBoolean(
                    browserManager.properties.getProperty("screenshot.on.failure", "true")
            );
            if (screenshotFailure && scenario.isFailed()) {
                attachScreenshot(scenario);
            }

            boolean videoRecording = Boolean.parseBoolean(
                    browserManager.properties.getProperty("video.recording", "false")
            );
            if (videoRecording && scenario.isFailed()) {
                attachVideo(scenario);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during teardown of Scenario: " + scenarioName, e);
        } finally {
            browserManager.tearDown();
        }
    }

    @After(value = "@SaveAuth", order = 10)
    public void saveAuthState(Scenario scenario) {
        if (!scenario.isFailed()) {
            String authContext = getAuthContext(scenario);
            String storageStatePath = STORAGE_STATE_PATH + authContext + "-auth.json";

            browserManager.saveStorageState(storageStatePath);
            logger.info("💾 Saved storage state to: " + storageStatePath);

            // Mark this auth context as ready
            markAuthContextReady(authContext);
        } else {
            logger.severe("❌ Authentication scenario failed - storage state NOT saved");
        }
    }

    /**
     * Get authentication context for a scenario.
     * Priority:
     * 1. Custom @AuthContext(name) tag
     * 2. Feature file name
     * 3. "default" as fallback
     */
    private String getAuthContext(Scenario scenario) {
        // Check for custom @AuthContext tag
        for (String tag : scenario.getSourceTagNames()) {
            if (tag.startsWith("@AuthContext:")) {
                String context = tag.substring("@AuthContext:".length());
                return sanitizeFileName(context);
            }
        }

        // Use feature name as default context
        String featureName = getFeatureName(scenario);
        return featureName.isEmpty() ? "default" : featureName;
    }

    private String getFeatureName(Scenario scenario) {
        String uri = scenario.getUri().toString();
        String featureName = uri.substring(uri.lastIndexOf("/") + 1).replace(".feature", "");
        return sanitizeFileName(featureName);
    }

    private boolean isAuthContextReady(String authContext) {
        return authContextSetup.getOrDefault(authContext, false);
    }

    private void markAuthContextReady(String authContext) {
        authContextSetup.put(authContext, true);
        logger.info("✓ Auth context marked as ready: " + authContext);
    }

    private void waitForAuthFile(String storagePath, int maxWaitSeconds) {
        int waited = 0;
        while (!Files.exists(Paths.get(storagePath)) && waited < maxWaitSeconds) {
            try {
                Thread.sleep(1000);
                waited++;
                if (waited % 5 == 0) {
                    logger.info("⏳ Waiting for auth file... " + waited + "s elapsed");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(Level.WARNING, "Interrupted while waiting for auth file", e);
                break;
            }
        }

        if (!Files.exists(Paths.get(storagePath))) {
            logger.warning("⚠️ Auth file not created after " + waited + " seconds");
        }
    }

    private void handleFailure(Scenario scenario) {
        logger.severe("❌ SCENARIO FAILED: " + scenarioName);

        if (isTracingEnabled) {
            String timestamp = dateFormat.format(new Date());
            String tracePath = String.format("src/test/test-output/Traces/%s_%s.zip",
                    sanitizeFileName(scenarioName), timestamp);
            try {
                browserManager.stopTracing(tracePath);
                byte[] traceData = Files.readAllBytes(Paths.get(tracePath));
                scenario.attach(traceData, "application/zip", "trace.zip");
                Allure.addAttachment("Playwright Trace", "application/zip",
                        new java.io.ByteArrayInputStream(traceData), ".zip");
                logger.info("📎 Trace attached: " + tracePath);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to attach trace", e);
            }
        }
    }

    private void attachScreenshot(Scenario scenario) {
        try {
            if (!browserManager.isPageAvailable()) {
                return;
            }

            byte[] screenshot = browserManager.takeScreenshot();
            if (screenshot != null && screenshot.length > 0) {
                // Attach to Cucumber report
                scenario.attach(screenshot, "image/png", "Failure Screenshot");

                // Attach to Allure report
                Allure.addAttachment("Failed Screenshot", "image/png",
                        new java.io.ByteArrayInputStream(screenshot), ".png");

                // Save to file system
                String timestamp = dateFormat.format(new Date());
                String screenshotPath = String.format("src/test/test-output/Screenshots/%s_%s.png",
                        sanitizeFileName(scenarioName), timestamp);
                Path path = Paths.get(screenshotPath);
                Files.createDirectories(path.getParent());
                Files.write(path, screenshot);

                logger.info("📸 Screenshot saved: " + screenshotPath);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to capture screenshot", e);
        }
    }

    private void attachVideo(Scenario scenario) {
        try {
            Path videoPath = browserManager.getVideoPath();
            if (videoPath != null && Files.exists(videoPath)) {
                // Close page to finalize video
                if (browserManager.isPageAvailable()) {
                    browserManager.getPage().close();
                }
                Thread.sleep(1000);

                byte[] videoBytes = Files.readAllBytes(videoPath);

                // Attach to Cucumber report
                scenario.attach(videoBytes, "video/webm", "Test Recording");

                // Attach to Allure report
                Allure.addAttachment("Video Recording", "video/webm",
                        new java.io.ByteArrayInputStream(videoBytes), ".webm");

                logger.info("🎥 Video attached: " + videoPath);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to attach video", e);
        }
    }

    private static void createDirectories() {
        try {
            Files.createDirectories(Paths.get("src/test/test-output/Screenshots"));
            Files.createDirectories(Paths.get("src/test/test-output/Traces"));
            Files.createDirectories(Paths.get("src/test/test-output/Videos"));
            Files.createDirectories(Paths.get("src/test/test-output/storage-states"));
            Files.createDirectories(Paths.get("src/test/test-output/Report/html-report"));
            Files.createDirectories(Paths.get("src/test/test-output/Report/json-report"));
            Files.createDirectories(Paths.get("target/allure-results"));
            logger.info("✓ Output directories created");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create output directories", e);
        }
    }

    private String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9]", "_");
    }
}