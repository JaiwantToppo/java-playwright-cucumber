package browser;

import com.microsoft.playwright.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public byte[] takeScreenshot() {
        if (page != null) {
            return getPage().screenshot();
        }
        return new byte[0];
    }

    public void setUp() {
        System.out.println("Setting up the browser");
        playwright.set(Playwright.create());
        String browserType = System.getProperty("BROWSER");
        if (browserType == null || browserType.isEmpty()) {
            browserType = properties.getProperty("browser", "chromium");
        }
        switch (browserType.toLowerCase()) {
            case "chromium":
                browser.set(playwright.get().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100)));
                break;
            case "firefox":
                browser.set(playwright.get().firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100)));
                break;
            case "webkit":
                browser.set(playwright.get().webkit().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100)));
                break;
            default:
                logger.warning("Invalid browser type: " + browserType + ", defaulting to chromium");
                browser.set(playwright.get().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100)));
                break;
        }
        browserContext.set(browser.get().newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080).
                setRecordVideoDir(Paths.get("src/test/test-output/Video")).
                setRecordVideoSize(1920, 1080)));
        setPage(getBrowserContext().newPage());
        int navigationTimeout = Integer.parseInt(properties.getProperty("navigation.timeout", "30000"));
        int actionTimeout = Integer.parseInt(properties.getProperty("action.timeout", "15000"));
        page.get().setDefaultNavigationTimeout(navigationTimeout);
        page.get().setDefaultTimeout(actionTimeout);
        System.out.println("Browser setup completed");
    }

    public void tearDown() {
        System.out.println("Tearing down the browser");
        if (page != null) getPage().close();
        if (browserContext != null) getBrowserContext().close();
        if (playwright != null) playwright.get().close();
        System.out.println("Browser teardown completed");
    }
}
