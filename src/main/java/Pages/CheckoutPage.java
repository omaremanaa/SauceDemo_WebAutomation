package Pages;

import Bots.ActionBot;
import Utils.PropertyReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class CheckoutPage {

    private WebDriver webDriver;
    private ActionBot actionBot;

    private final By cancelButton = By.id("cancel");
    private final By continueButton = By.id("continue");
    private final By firstNameLocator = By.id("first-name");
    private final By lastNameLocator = By.id("last-name");
    private final By postalCodeLocator = By.id("postal-code");
    private final By errorMessage = By.xpath("//*[@data-test=\"error\"]");
    private final By totalPrice = By.xpath("//*[@data-test=\"subtotal-label\"]");
    private final By itemPrice = By.className("inventory_item_price");
    private final By finishButton = By.id("finish");
    private final By thankYouHeader = By.className("complete-header");
    private final By thankYouSubtitle = By.className("complete-text");
    public final By backHomeButton = By.id("back-to-products");


    public CheckoutPage (WebDriver webDriver){
        this.webDriver = webDriver;
        this.actionBot = new ActionBot(webDriver);
    }

    @Step("Insert checkout info: First Name='{firstname}', Last Name='{lastname}', Postal Code='{postalCode}'")
    public CheckoutPage insertCheckoutInfo(String firstname, String lastname, String postalCode){
        actionBot.typeText(firstNameLocator,firstname);
        actionBot.typeText(lastNameLocator,lastname);
        actionBot.typeText(postalCodeLocator, postalCode);
        return this;
    }

    @Step("Click Continue button")
    public CheckoutPage clickContinue(){
        actionBot.clickElement(continueButton);
        return this;
    }

    @Step("Click Cancel button")
    public CheckoutPage clickCancel(){
        actionBot.clickElement(cancelButton);
        return this;
    }

    @Step("Click Finish button")
    public CheckoutPage clickFinish(){
        actionBot.clickElement(finishButton);
        return this;
    }

    @Step("Click Back Home button")
    public CheckoutPage clickBackHome(){
        actionBot.clickElement(backHomeButton);
        return this;
    }

    public int getProductsCount() {
        return actionBot.findElements(itemPrice).size();
    }

    @Step("Validate error message text is displayed")
    public CheckoutPage validateErrorMessageText(String errorMessageText){
        Assert.assertTrue(actionBot.isElementDisplayed(errorMessage), "Error message is correct");
        return this;
    }

    @Step("Validate total price of checkout matches sum of product prices")
    public CheckoutPage validateTotalPrice(){
        double total = 0;
        for (int i = 0 ; i<getProductsCount();i++){
            double productPrice = Double.parseDouble(actionBot.readText(itemPrice,i).replace("$", ""));
            total = total + productPrice;
        }
        double checkoutTotal = Double.parseDouble(
                actionBot.readText(totalPrice).replaceAll("[^0-9.]", "")
        );
        Assert.assertEquals(checkoutTotal, total,"Checkout total price is valid");
        return this;
    }

    @Step("Validate that user is at checkout summary page")
    public CheckoutPage validateCheckoutSummaryPage(){
        String url = PropertyReader.getProperty("checkoutSummaryUrl");
        Assert.assertTrue(webDriver.getCurrentUrl().contains(url), "User is at Summary page");
        return this;
    }

    @Step("Validate thank you purchase message: Title='{title}', Subtitle='{subtitle}'")
    public CheckoutPage validateThanksPurchaseMessage(String title, String subtitle){
        Assert.assertEquals(title, actionBot.readText(thankYouHeader), "Thanks message Title appeared");
        Assert.assertEquals(subtitle, actionBot.readText(thankYouSubtitle), "Thanks message subtitle appeared");
        return this;
    }

    @Step("Validate that user is at home page")
    public HomePage validateHomePage(){
        String homeUrl = PropertyReader.getProperty("homeUrl");
        Assert.assertTrue(webDriver.getCurrentUrl().contains(homeUrl), "User is at Home page");
        return new HomePage(webDriver);
    }



}