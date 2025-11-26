package Tests;

import Drivers.WebDriverFactory;
import Pages.CartPage;
import Pages.LoginPage;
import Utils.JsonReader;
import Utils.PropertyReader;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTest {

    WebDriver webDriver;
    JsonReader jsonReader;
    int amountOfItemsToAdd = 3;
    CartPage cartPage;
    String username, password;

    @BeforeMethod
    public void setup() {
        webDriver = WebDriverFactory.initDriver();
        String baseUrl= PropertyReader.getProperty("baseUrl");
        jsonReader = new JsonReader("login");
        webDriver.get(baseUrl);
         username = jsonReader.getJsonData("username");
         password = jsonReader.getJsonData("password");
        cartPage = new LoginPage(webDriver).logIn(username, password)
                .isAtHomePage().addProductByIndex(0).addProductByIndex(1).addProductByIndex(2).cartClick()
                .validateCart();
    }

    @Test
    public void cartShouldDisplayCorrectItems() {
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Cart Should Display Correct Items")
        );
        cartPage.assertCartBadgeCountIs(amountOfItemsToAdd).removeItemsFromCart(amountOfItemsToAdd);
    }

    @Test
    public void cartShouldRemoveSelectedItems() {
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Cart Should Remove Selected Items")
        );
        cartPage.removeItemsFromCart(amountOfItemsToAdd-2)
                .assertCartBadgeCountIs(2)
                .assertCartItemCountIs(2);
    }

    @Test
    public void cartShouldBeEmptyAfterRemovingAllItems() {
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Cart Should Be Empty After Removing All Items")
        );
        cartPage.removeItemsFromCart(amountOfItemsToAdd)
                .assertCartBadgeIsHidden()
                .assertRemoveButtonsAreHidden();
    }

    @Test
    public void continueShoppingShouldReturnToProducts() {
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Continue Shopping Should Return to Products Page")
        );
        cartPage.clickContinueShoppingButton()
                .verifyUserIsAtHomePage();
    }

    @Test
    public void checkoutShouldNavigateToCheckoutPage() {
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Checkout Should Navigate to Checkout Page")
        );
        cartPage.clickCheckoutButton()
                .verifyUserIsAtCheckoutPage();
    }


    @AfterMethod
    public void tearDown() {
        WebDriverFactory.quitDriver();
    }
}
