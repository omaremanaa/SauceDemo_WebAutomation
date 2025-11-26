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


public class LoginTest {
    WebDriver webDriver;
    JsonReader jsonReader;

    @BeforeMethod
    public void setup() {
        webDriver = WebDriverFactory.initDriver();
        jsonReader = new JsonReader("login");
        String baseUrl= PropertyReader.getProperty("baseUrl");
        webDriver.get(baseUrl);
    }
    @Test
    public void validLoginTest() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName("Valid Login"));
        String username = jsonReader.getJsonData("username");
        String password = jsonReader.getJsonData("password");

        new LoginPage(webDriver).logIn(username, password)
                .isAtHomePage();

    }

    @Test
    public void invalidLoginByPasswordTest() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName("Invalid Login (Wrong Password)"));

        String username = jsonReader.getJsonData("username");
        String incorrectPassword = jsonReader.getJsonData("invalid_password");
        String errorMessage = jsonReader.getJsonData("error_invalid_credentials");
        new LoginPage(webDriver).logIn(username, incorrectPassword)
                .getErrorMessageText(errorMessage);
    }

    @Test
    public void invalidLoginByUsernameTest() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName("Invalid Login (Wrong Username)"));
        String incorrectUsername = jsonReader.getJsonData("invalid_username");
        String password = jsonReader.getJsonData("password");
        String errorMessage = jsonReader.getJsonData("error_invalid_credentials");
        new LoginPage(webDriver).logIn(incorrectUsername, password)
                .getErrorMessageText(errorMessage);

    }

    @Test
    public void invalidLoginByEmptyFieldsTest() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName("Invalid Login (Empty Fields)"));
        String errorMessage = jsonReader.getJsonData("error_username_required");
        new LoginPage(webDriver).logIn("", "")
                .getErrorMessageText(errorMessage);

    }

    @AfterMethod
    public void tearDown() {
        WebDriverFactory.quitDriver();
    }
}