package Pages;

import Bots.ActionBot;
import Utils.PropertyReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginPage {

    private WebDriver webDriver;
    private ActionBot actionBot;

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[class=\"error-message-container error\"]");

    public LoginPage (WebDriver webDriver){
        this.webDriver = webDriver;
        this.actionBot = new ActionBot(webDriver);
    }

    @Step("Login to sauce demo with username: {username} and password: {pass}")
    public LoginPage logIn(String username, String pass){
        actionBot.typeText(usernameField, username);
        actionBot.typeText(passwordField, pass);
        actionBot.clickElement(loginButton);
        return this;
    }

    @Step("Verify user is logged in and at home page")
    public HomePage isAtHomePage(){
        String url = PropertyReader.getProperty("homeUrl");
        Assert.assertTrue(webDriver.getCurrentUrl().contains(url), "User is at home page");
        return new HomePage(webDriver);
    }

    @Step("Validate error message text")
    public LoginPage getErrorMessageText(String errorMessageText){
        Assert.assertTrue(actionBot.isElementDisplayed(errorMessage), "Error message is displayed");
        return this;
    }


}