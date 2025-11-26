package Tests;

import Drivers.WebDriverFactory;
import Pages.LoginPage;
import Utils.JsonReader;
import Utils.PropertyReader;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EndToEnd {
    WebDriver webDriver;
    JsonReader jsonReader,jsonLoginReader;
    String username, password,firstName,lastName,postalCode,validateTitle,validateSubtitle;

    @BeforeMethod
    public void setup() {
        webDriver = WebDriverFactory.initDriver();
        String baseUrl= PropertyReader.getProperty("baseUrl");
        jsonLoginReader = new JsonReader("login");
        jsonReader = new JsonReader("checkout");

        webDriver.get(baseUrl);
         username = jsonLoginReader.getJsonData("username");
         password = jsonLoginReader.getJsonData("password");
        firstName= jsonReader.getJsonData("firstname");
        lastName = jsonReader.getJsonData("lastname");
        postalCode = jsonReader.getJsonData("postalCode");
         validateTitle = jsonReader.getJsonData("validateTitle");
         validateSubtitle = jsonReader.getJsonData("validateSubtitle");

    }

    @Test
    public void shouldCompleteCheckout_whenSingleItemAdded(){
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Complete Checkout With Single Item Added")
        );
        new LoginPage(webDriver)
                .logIn(username, password)
                .isAtHomePage().addProductByIndex(0)
                .cartClick()
                .validateCart().clickCheckoutButton().verifyUserIsAtCheckoutPage()
                .insertCheckoutInfo(firstName,lastName,postalCode)
                .clickContinue()
                .validateTotalPrice()
                .clickFinish()
                .validateThanksPurchaseMessage(validateTitle,validateSubtitle);
    }

    @Test
    public void shouldCompleteCheckout_whenRemovingItem(){
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Complete Checkout After Removing an Item From Cart")
        );
        new LoginPage(webDriver)
                .logIn(username, password)
                .isAtHomePage().addProductByIndex(0).addProductByIndex(1).addProductByIndex(2)
                .removeProductByIndex(1)
                .cartClick()
                .validateCart()
                .assertCartBadgeCountIs(2)
                .clickCheckoutButton().verifyUserIsAtCheckoutPage()
                .insertCheckoutInfo(firstName,lastName,postalCode)
                .clickContinue()
                .validateTotalPrice()
                .clickFinish()
                .validateThanksPurchaseMessage(validateTitle,validateSubtitle);
    }

    @Test
    public void shouldCompleteCheckout_whenSortingLowToHigh(){
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Complete Checkout After Sorting Products Low To High")
        );
        new LoginPage(webDriver)
                .logIn(username, password)
                .isAtHomePage()
                .sortBy("Price (low to high)")
                .addProductByIndex(0)
                .addProductByIndex(1)
                .cartClick()
                .validateCart()
                .assertCartBadgeCountIs(2)
                .clickCheckoutButton()
                .verifyUserIsAtCheckoutPage()
                .insertCheckoutInfo(firstName,lastName,postalCode)
                .clickContinue()
                .validateTotalPrice()
                .clickFinish()
                .validateThanksPurchaseMessage(validateTitle,validateSubtitle);
    }

    @AfterMethod
    public void tearDown() {
        WebDriverFactory.quitDriver();
    }

}
