package Bots;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ActionBot {
    private WebDriver webDriver;
    private WaitBot waitBot;

    public ActionBot(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.waitBot = new WaitBot(webDriver);
    }

    public boolean isElementDisplayed(By locator) {
        return webDriver.findElement(locator).isDisplayed();
    }

    public void clickElement(By locator)
    {
        waitBot.waitFor(isElementDisplayed(locator), 5);
        webDriver.findElement(locator).click();
    }

    public void typeText(By locator, String text){
        waitBot.waitFor(isElementDisplayed(locator), 5);
        webDriver.findElement(locator).sendKeys(text);
    }

    public String readText(By locator){
        waitBot.waitFor(isElementDisplayed(locator), 5);
        return webDriver.findElement(locator).getText();
    }



}
