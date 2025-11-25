package util;

import browser.BrowserManager;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageStateUtil {
    private static final Logger logger = Logger.getLogger(StorageStateUtil.class.getName());
    private static final String DEFAULT_STORAGE_PATH = "src/test/test-output/storage-states/auth.json";

    public static void createAuthenticationState(BrowserManager browserManager,
                                                 String username,
                                                 String password,
                                                 String loginUrl) {
        logger.info("Creating authentication state for user: " + username);

        try {
            browserManager.getPage().navigate(loginUrl);
            performLogin(browserManager.getPage(), username, password);

            String storagePath = ConfigReader.getProperty("storage.state.path");
            if (storagePath == null || storagePath.isEmpty()) {
                storagePath = DEFAULT_STORAGE_PATH;
            }

            browserManager.saveStorageState(storagePath);
            logger.info("Authentication state saved to: " + storagePath);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating authentication state", e);
            throw new RuntimeException("Failed to create authentication state", e);
        }
    }

    public static boolean storageStateExists() {
        String storagePath = ConfigReader.getProperty("storage.state.path");
        if (storagePath == null || storagePath.isEmpty()) {
            storagePath = DEFAULT_STORAGE_PATH;
        }
        return Files.exists(Paths.get(storagePath));
    }

    public static boolean storageStateExists(String storagePath) {
        return Files.exists(Paths.get(storagePath));
    }

    public static void deleteStorageState() {
        String storagePath = ConfigReader.getProperty("storage.state.path");
        if (storagePath == null || storagePath.isEmpty()) {
            storagePath = DEFAULT_STORAGE_PATH;
        }
        deleteStorageState(storagePath);
    }

    public static void deleteStorageState(String storagePath) {
        try {
            Path path = Paths.get(storagePath);
            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("Storage state deleted: " + storagePath);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error deleting storage state", e);
        }
    }

    public static void refreshStorageState(BrowserManager browserManager,
                                           String username,
                                           String password,
                                           String loginUrl) {
        deleteStorageState();
        createAuthenticationState(browserManager, username, password, loginUrl);
    }

    public static String getStoragePathForRole(String role) {
        return String.format("src/test/test-output/storage-states/%s-auth.json", role.toLowerCase());
    }

    private static void performLogin(Page page, String username, String password) {
        try {
            page.waitForSelector("[placeholder='Username']",
                    new Page.WaitForSelectorOptions().setTimeout(10000));

            page.getByPlaceholder("Username").fill(username);
            page.getByPlaceholder("Password").fill(password);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();

            // Wait for successful login
            page.waitForSelector("header h6",
                    new Page.WaitForSelectorOptions().setTimeout(10000));

            logger.info("Login successful for user: " + username);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Login failed", e);
            throw new RuntimeException("Failed to perform login", e);
        }
    }
}
