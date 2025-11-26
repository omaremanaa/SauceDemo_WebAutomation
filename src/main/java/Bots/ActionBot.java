package Bots;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

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
       // waitBot.waitFor(isElementDisplayed(locator), 5);
        waitBot.waitFor(() -> isElementDisplayed(locator), 5);

        webDriver.findElement(locator).click();
    }

    public void typeText(By locator, String text){
        //waitBot.waitFor(isElementDisplayed(locator), 5);
        waitBot.waitFor(() -> isElementDisplayed(locator), 5);
        webDriver.findElement(locator).sendKeys(text);
    }

    public String readText(By locator){
        //waitBot.waitFor(isElementDisplayed(locator), 5);
        waitBot.waitFor(() -> isElementDisplayed(locator), 5);
        return webDriver.findElement(locator).getText();
    }

    // To read list of products
    public List<WebElement> findElements(By locator) {
        return webDriver.findElements(locator);
    }

    // All products AddToCart buttons are with same class, So wa want to click according to its order in page
    public void clickElement(By locator, int index) {
        //waitBot.waitFor(isElementDisplayed(locator), 5);
        waitBot.waitFor(() -> isElementDisplayed(locator), 5);
        List<WebElement> elems = webDriver.findElements(locator);
        if (index < 0 || index >= elems.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index + " for locator: " + locator);
        }
        elems.get(index).click();
    }

    // To read Fast according to index number in Elements List to avoid using Xpath
    public String readText(By locator, int index) {
        //waitBot.waitFor(isElementDisplayed(locator), 5);
        waitBot.waitFor(() -> isElementDisplayed(locator), 5);
        List<WebElement> elems = webDriver.findElements(locator);
        if (index < 0 || index >= elems.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index + " for locator: " + locator);
        }
        return elems.get(index).getText();
    }

    // To check the drop_down menu according to text of elements
    public void selectByVisibleText(By locator, String visibleText) {
        //waitBot.waitFor(isElementDisplayed(locator), 5);
        waitBot.waitFor(() -> isElementDisplayed(locator), 5);
        Select select = new Select(webDriver.findElement(locator));
        select.selectByVisibleText(visibleText);
    }
}
