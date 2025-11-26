package Tests;

import Drivers.WebDriverFactory;
import Pages.HomePage;
import Pages.LoginPage;
import Utils.JsonReader;
import Utils.PropertyReader;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class HomeTest {

    WebDriver webDriver;
    JsonReader jsonReader;
    HomePage homePage;

    @BeforeMethod
    public void setup() {
        webDriver = WebDriverFactory.initDriver();
        jsonReader = new JsonReader("login");

        String baseUrl = PropertyReader.getProperty("baseUrl");
        webDriver.get(baseUrl);

        String username = jsonReader.getJsonData("username");
        String password = jsonReader.getJsonData("password");

        homePage = new LoginPage(webDriver)
                .logIn(username, password)
                .isAtHomePage();
    }

    // ===========================
    // SCENARIO 1: Home Page Loads
    // ===========================
    @Test
    public void homePageLoadsSuccessfully() {
        Allure.getLifecycle().updateTestCase(t -> t.setName("Home Page Loads Successfully"));

        int count = homePage.getProductsCount();
        Assert.assertEquals(count, 6, "Products count should be exactly 6");
    }

    // ===============================
    // SCENARIO 2: Add Single Product
    // ===============================
    @Test
    public void addSingleProduct_updatesButtonAndCartBadge() {
        Allure.getLifecycle().updateTestCase(t -> t.setName("Add Single Product"));

        homePage.addProductByIndex(0);

        String btnText = homePage.getButtonTextByIndex(0);
        Assert.assertTrue(btnText.equalsIgnoreCase("Remove"), "Button should change to 'Remove'");
        Assert.assertTrue(homePage.isCartBadgePresent(), "Cart badge should appear");
        Assert.assertEquals(homePage.getCartBadgeCount(), 1, "Cart count should be 1");
    }

    // ====================================
    // SCENARIO 3: Add Multiple Products
    // ====================================
    @Test
    public void addMultipleProducts_updatesCartBadge() {
        Allure.getLifecycle().updateTestCase(t -> t.setName("Add Multiple Products"));

        homePage.addProductByIndex(0)
                .addProductByIndex(1)
                .addProductByIndex(2);

        Assert.assertTrue(homePage.isCartBadgePresent());
        Assert.assertEquals(homePage.getCartBadgeCount(), 3);
    }

    // ==================================
    // SCENARIO 4: Remove Product
    // ==================================
    @Test
    public void removeProduct_updatesButtonAndCartBadge() {
        Allure.getLifecycle().updateTestCase(t -> t.setName("Remove Product"));

        homePage.addProductByIndex(0);
        Assert.assertEquals(homePage.getCartBadgeCount(), 1);

        homePage.removeProductByIndex(0);

        Assert.assertFalse(homePage.isCartBadgePresent(), "Cart badge should disappear after removing last item");

        String btnText = homePage.getButtonTextByIndex(0);
        Assert.assertTrue(btnText.equalsIgnoreCase("Add to cart"), "Button should be back to 'Add to cart'");
    }

    // ====================================
    // SCENARIO 5: Open Sidebar
    // ====================================
    @Test
    public void openSidebar_showsMenuLinks() {
        Allure.getLifecycle().updateTestCase(t -> t.setName("Open Sidebar Menu"));

        homePage.openSidebar();
        // We only assert sidebar opened because links have fixed IDs and will be clicked in other tests.
        Assert.assertTrue(homePage.isSidebarLinkVisible(),"Sidebar did not open or links are not visible");
    }

    // ==================================
    // SCENARIO 6: Logout From Sidebar
    // ==================================
    @Test
    public void logoutFromSidebar_returnsToLogin() {
        Allure.getLifecycle().updateTestCase(t -> t.setName("Logout From Sidebar"));

        homePage.openSidebar()
                .clickLogout();

        String currentUrl = webDriver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.endsWith("/") || currentUrl.contains("saucedemo.com"),
                "Should be redirected to login page"
        );
    }

    // ============================================
    // SCENARIO 7: Sorting (Low to High)
    // ============================================
    @Test
    public void sortByPrice_lowToHigh_reordersProducts() throws InterruptedException {
        Allure.getLifecycle().updateTestCase(t -> t.setName("Sort: Price Low to High"));

        homePage.sortBy("Price (low to high)");
        Thread.sleep(1000);
        List<Double> prices = homePage.getAllPrices();

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(
                    prices.get(i) <= prices.get(i + 1),
                    "Prices are not sorted from low to high"
            );
        }
    }

    // ============================================
    // SCENARIO 8: Product Details Page
    // ============================================
    @Test
    public void productDetailsPage_navigatesOnClick() {
        Allure.getLifecycle().updateTestCase(t -> t.setName("Open Product Details"));

        homePage.openProductDetailsByName("Sauce Labs Backpack");

        Assert.assertTrue(
                webDriver.getCurrentUrl().contains("inventory-item"),
                "Should navigate to product details page"
        );
    }

    // ============================================
    // SCENARIO 9: Sidebar About → SauceLabs.com
    // ============================================
    @Test
    public void sidebarAbout_opensSauceLabs() {
        Allure.getLifecycle().updateTestCase(t -> t.setName("Sidebar: About → SauceLabs.com"));

        homePage.openSidebar()
                .clickAbout();

        boolean found = false;

        for (String handle : webDriver.getWindowHandles()) {
            webDriver.switchTo().window(handle);
            if (webDriver.getCurrentUrl().contains("saucelabs.com")) {
                found = true;
                break;
            }
        }

        Assert.assertTrue(found, "About page should redirect to SauceLabs.com");
    }

    @AfterMethod
    public void tearDown() {
        WebDriverFactory.quitDriver();
    }

}
