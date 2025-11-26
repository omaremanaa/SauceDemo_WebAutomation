package Pages;

import Bots.ActionBot;
import Utils.PropertyReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Properties;

public class CartPage {

    private WebDriver webDriver;
    private ActionBot actionBot;

    private final By removeFromCartButton = By.xpath("(//*[@class='btn btn_secondary btn_small cart_button'])[1]");
    private final By cartButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By removeButton = By
            .xpath("//*[@class='btn btn_secondary btn_small cart_button']");


    public CartPage (WebDriver webDriver){
        this.webDriver = webDriver;
        this.actionBot = new ActionBot(webDriver);
    }

    @Step("Remove {numberOfItems} items from the cart")
    public CartPage removeItemsFromCart(int numberOfItems){
        for (int i = 0; i < numberOfItems; i++){
            actionBot.clickElement(removeFromCartButton);
        }
        return this;
    };

    @Step("Click on continue shopping button")
    public CartPage clickContinueShoppingButton(){
        actionBot.clickElement(continueShoppingButton);
        return this;    }

    @Step("Click on checkout button" )
    public CartPage clickCheckoutButton(){
        actionBot.clickElement(cartButton);
        return this;
    }


    @Step("Verify that remove buttons are hidden")
    public CartPage assertRemoveButtonsAreHidden(){
        Assert.assertFalse(actionBot.isElementDisplayed(removeFromCartButton), "Remove button is not displayed");
        return this;
    }

    @Step("Verify that cart badge is hidden")
    public CartPage assertCartBadgeIsHidden(){
        Assert.assertFalse(actionBot.isElementDisplayed(cartBadge), "Cart badge is not displayed");
        return this;
    }

    @Step("Verify that cart badge has {itemCount} items")
    public CartPage assertCartBadgeCountIs(int itemCount){
        int badgeText = Integer.parseInt(actionBot.readText(cartBadge));

        Assert.assertEquals(badgeText, itemCount, "Cart badge has correct item count");
        return this;
    }

    @Step("Verify that cart has {itemCount} items")
    public CartPage assertCartItemCountIs(int itemCount){
        int countCartItems = webDriver.findElements(removeButton).size();
        Assert.assertEquals(countCartItems, itemCount, "Cart has correct item count");
        return this;
    }


    @Step("Verify user is at Home page")
    public HomePage verifyUserIsAtHomePage(){
        String homeUrl = PropertyReader.getProperty("homeUrl");
        Assert.assertTrue(webDriver.getCurrentUrl().contains(homeUrl), "User is at home page");
        return new HomePage(webDriver);
    }

    @Step("Verify user is at checkout page")
    public CheckoutPage verifyUserIsAtCheckoutPage(){
        String checkoutUrl = PropertyReader.getProperty("checkoutUrl");
        Assert.assertTrue(webDriver.getCurrentUrl().contains(checkoutUrl), "User is at Checkout page");
        return new CheckoutPage(webDriver);
    }



}
