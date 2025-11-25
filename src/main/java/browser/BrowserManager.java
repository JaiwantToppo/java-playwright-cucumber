package browser;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ScreenshotType;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BrowserManager {
    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();
    public Properties properties;
    public static final Logger logger = Logger.getLogger(BrowserManager.class.getName());
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public BrowserManager() {
        properties = new Properties();
        System.out.println("Config Path is: " + System.getProperty("config.path"));
        System.out.println("User Dir is: " + System.getProperty("user.dir"));
        Path configPath = Paths.get(System.getProperty("config.path",
                Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "config.properties").toString()));
        try(InputStream inputStream = Files.newInputStream(configPath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load config.properties", e);
        }
    }

    public Page getPage() {
        return page.get();
    }

    public void setPage(Page newPage) {
        page.set(newPage);
    }

    public BrowserContext getBrowserContext() {
        return browserContext.get();
    }

    public Browser getBrowser() {
        return browser.get();
    }

    public Playwright getPlaywright() {
        return playwright.get();
    }

    public byte[] takeScreenshot() {
        if (page.get() != null) {
            return getPage().screenshot(new Page.ScreenshotOptions().setFullPage(true).setType(ScreenshotType.PNG));
        }
        return new byte[0];
    }

    public void setUp() {
        logger.info("Setting up the browser for thread: " + Thread.currentThread().getName());
        playwright.set(Playwright.create());
        String browserType = System.getProperty("BROWSER");
        if (browserType == null || browserType.isEmpty()) {
            browserType = properties.getProperty("browser", "chromium");
        }

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", properties.getProperty("headless", "false")));
        int slowMo = Integer.parseInt(properties.getProperty("slowMo", "0"));

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(slowMo).
                setArgs(Arrays.asList("--start-maximized",
                        "--disable-notifications",
                        "--disable-infobars",
                        "--disable-blink-features=AutomationControlled"));

        switch (browserType.toLowerCase()) {
            case "firefox":
                browser.set(playwright.get().firefox().launch(launchOptions));
                break;
            case "webkit":
                browser.set(playwright.get().webkit().launch(launchOptions));
                break;
            case "chrome":
                launchOptions.setChannel("chrome");
                browser.set(playwright.get().chromium().launch(launchOptions));
                break;
            case "edge":
                launchOptions.setChannel("msedge");
                browser.set(playwright.get().chromium().launch(launchOptions));
                break;
            default:
                browser.set(playwright.get().chromium().launch(launchOptions));
                break;
        }

        logger.info("Launched browser: " + browserType + " in " + (headless ? "headless" : "headed") + " mode.");
        createContext();
        logger.info("Browser setup completed for thread: " + Thread.currentThread().getName());
    }

    public void createContext() {
        boolean recordVideo = Boolean.parseBoolean(properties.getProperty("video.recording", "false"));

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(screenSize.width, screenSize.height)
                .setAcceptDownloads(true)
                .setIgnoreHTTPSErrors(true);

        if (recordVideo) {
            Path videoDir = Paths.get("src/test/test-output/Videos");
            try {
                Files.createDirectories(videoDir);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to create video directory", e);
            }
            contextOptions.setRecordVideoDir(videoDir).setRecordVideoSize(1920, 1080);
        }

        browserContext.set(browser.get().newContext(contextOptions));
        setPage(getBrowserContext().newPage());

        int navigationTimeout = Integer.parseInt(properties.getProperty("navigation.timeout", "30000"));
        int actionTimeout = Integer.parseInt(properties.getProperty("action.timeout", "60000"));

        page.get().setDefaultNavigationTimeout(navigationTimeout);
        page.get().setDefaultTimeout(actionTimeout);
    }

    public void createContextWithStorageState(String storageStatePath) {
        logger.info("Creating browser context with storage state: " + storageStatePath);

        if (!Files.exists(Paths.get(storageStatePath))) {
            logger.warning("Storage state not found at: " + storageStatePath + ". Creating context without storage state.");
            createContext();
            return;
        }

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(screenSize.width, screenSize.height)
                .setAcceptDownloads(true)
                .setIgnoreHTTPSErrors(true)
                .setStorageStatePath(Paths.get(storageStatePath));

        browserContext.set(browser.get().newContext(contextOptions));
        setPage(getBrowserContext().newPage());

        int navigationTimeout = Integer.parseInt(properties.getProperty("navigation.timeout", "30000"));
        int actionTimeout = Integer.parseInt(properties.getProperty("action.timeout", "60000"));

        page.get().setDefaultNavigationTimeout(navigationTimeout);
        page.get().setDefaultTimeout(actionTimeout);
    }

    public void saveStorageState(String storageStatePath) {
        if (browserContext.get() != null) {
            try {
                Path storagePath = Paths.get(storageStatePath);
                Files.createDirectories(storagePath.getParent());
                browserContext.get().storageState(new BrowserContext.StorageStateOptions().setPath(storagePath));
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to save storage state to: " + storageStatePath, e);
            }
        }
    }

    public void startTracing(String traceName) {
        if (browserContext.get() != null) {
            browserContext.get().tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
            logger.info("Started tracing: " + traceName);
        }
    }

    public void stopTracing(String tracePath) {
        if (browserContext.get() != null) {
            try {
                Path traceFilePath = Paths.get(tracePath);
                Files.createDirectories(traceFilePath.getParent());
                browserContext.get().tracing().stop(new Tracing.StopOptions().setPath(traceFilePath));
                logger.info("Trace saved to: " + traceFilePath);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to save trace to: " + tracePath, e);
            }
        }
    }

    public Path getVideoPath() {
        if (page.get() != null && page.get().video() != null) {
            return page.get().video().path();
        }
        return null;
    }

    public boolean isPageAvailable() {
        return page.get() != null;
    }

    public void tearDown() {
        logger.info("Tearing down browser for thread: " + Thread.currentThread().getId());
        try {
            if (page.get() != null) {
                try {
                    getPage().close();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error closing page", e);
                }
                page.remove();
            }

            if (browserContext.get() != null) {
                try {
                    getBrowserContext().close();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error closing context", e);
                }
                browserContext.remove();
            }

            if (browser.get() != null) {
                try {
                    getBrowser().close();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error closing browser", e);
                }
                browser.remove();
            }

            if (playwright.get() != null) {
                try {
                    getPlaywright().close();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error closing playwright", e);
                }
                playwright.remove();
            }

            logger.info("Browser teardown completed for thread: " + Thread.currentThread().getId());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during teardown", e);
        }
    }
}
