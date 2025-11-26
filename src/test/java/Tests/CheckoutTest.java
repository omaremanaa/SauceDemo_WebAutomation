package Tests;

import Drivers.WebDriverFactory;
import Pages.CheckoutPage;
import Pages.LoginPage;
import Utils.JsonReader;
import Utils.PropertyReader;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTest {

    WebDriver webDriver;
    JsonReader jsonReader, jsonLoginReader;
    CheckoutPage checkoutPage;
    String firstName,lastName,postalCode;

    @BeforeMethod
    public void setup() {
        webDriver = WebDriverFactory.initDriver();
        String baseUrl= PropertyReader.getProperty("baseUrl");
        jsonReader = new JsonReader("checkout");
        jsonLoginReader = new JsonReader("login");
        firstName= jsonReader.getJsonData("firstname");
        lastName = jsonReader.getJsonData("lastname");
        postalCode = jsonReader.getJsonData("postalCode");

        webDriver.get(baseUrl);
        String username = jsonLoginReader.getJsonData("username");
        String password = jsonLoginReader.getJsonData("password");
        checkoutPage = new LoginPage(webDriver)
                .logIn(username, password)
                .isAtHomePage().addProductByIndex(0).addProductByIndex(1)
                .cartClick()
                .validateCart().clickCheckoutButton().verifyUserIsAtCheckoutPage();
    }

    @Test
    public void validCheckout(){
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Valid Checkout With Correct User Info")
        );
        checkoutPage.insertCheckoutInfo(firstName,lastName,postalCode)
                .clickContinue().validateCheckoutSummaryPage();

    }

    @Test
    public void invalidCheckoutByFirstName(){
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Invalid Checkout - Missing First Name")
        );
        String errorFirst = jsonReader.getJsonData("errorFirstName");
        checkoutPage.insertCheckoutInfo("",lastName,postalCode)
                .clickContinue().validateErrorMessageText(errorFirst);

    }

    @Test
    public void invalidCheckoutByLastName(){
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Invalid Checkout - Missing Last Name")
        );
        String errorLast = jsonReader.getJsonData("errorLastName");
        checkoutPage.insertCheckoutInfo(firstName,"",postalCode)
                .clickContinue()
                .validateErrorMessageText(errorLast);

    }
    @Test
    public void invalidCheckoutByPostalCode(){
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Invalid Checkout - Missing Postal Code")
        );
        String errorPostal = jsonReader.getJsonData("errorPostalCode");
        checkoutPage.insertCheckoutInfo(firstName,lastName,"")
                .clickContinue()
                .validateErrorMessageText(errorPostal);
    }

    @Test
    public void validateOrderSummaryPrices(){
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Validate Order Summary Prices Are Correct")
        );
        checkoutPage.insertCheckoutInfo(firstName,lastName,postalCode)
                .clickContinue().validateCheckoutSummaryPage()
                .validateTotalPrice();
    }

    @Test
    public void validateCompleteOrder(){
        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName("Complete Order And Verify Success Message")
        );
        String validateTitle = jsonReader.getJsonData("validateTitle");
        String validateSubtitle = jsonReader.getJsonData("validateSubtitle");

        checkoutPage.insertCheckoutInfo(firstName,lastName,postalCode)
                .clickContinue().validateCheckoutSummaryPage()
                .clickFinish().validateThanksPurchaseMessage(validateTitle,validateSubtitle)
                .clickBackHome().validateHomePage();
    }

    @AfterMethod
    public void tearDown() {
        WebDriverFactory.quitDriver();

    }
}
