package Pages;

import Bots.ActionBot;
import Utils.PropertyReader;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class HomePage {
    private WebDriver webDriver;
    private ActionBot actionBot;


    public HomePage (WebDriver webDriver){
        this.webDriver = webDriver;
        this.actionBot = new ActionBot(webDriver);
    }

    // Locators
    private final By title = By.xpath("//span[text()='Products']");
    private final By inventoryItems = By.cssSelector(".inventory_item");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By addToCartButtons = By.cssSelector(".btn_inventory");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");
    private final By sortDropdown = By.cssSelector("[data-test=\"product-sort-container\"]");
    // private final By sortDropdown = By.xpath("//*[@id=\"header_container\"]/div[2]/div/span/select/option[3])");

    private final By burgerMenuBtn = By.id("react-burger-menu-btn");
    private final By sidebarLogout = By.id("logout_sidebar_link");
    private final By sidebarAbout = By.id("about_sidebar_link");
    private final By sidebarAllItems = By.id("inventory_sidebar_link");
    private final By sidebarReset = By.id("reset_sidebar_link");
    private final By exitSidebar = By.id("react-burger-cross-btn");

    // ====== Core Page Validation ======

    @Step("Verify user is on Home Page (inventory page)")
    public HomePage isAtHomePage(String expectedUrlContains) {
        Assert.assertTrue(
                webDriver.getCurrentUrl().contains(expectedUrlContains),
                "Home page URL mismatch"
        );
        Assert.assertTrue(
                actionBot.isElementDisplayed(title),
                "'Products' title is not visible"
        );
        return this;
    }

    // ====== Products ======

    @Step("Get total number of products")
    public int getProductsCount() {
        return actionBot.findElements(inventoryItems).size();
    }

    @Step("Add product by index: {index}")
    public HomePage addProductByIndex(int index) {
        actionBot.clickElement(addToCartButtons, index);
        return this;
    }

    @Step("Remove product by index: {index}")
    public HomePage removeProductByIndex(int index) {
        actionBot.clickElement(addToCartButtons, index); // same button toggles
        return this;
    }

    @Step("Get button text for product index {index}")
    public String getButtonTextByIndex(int index) {
        return actionBot.readText(addToCartButtons, index);
    }

    // ====== Cart ======

    @Step("Check if cart badge is present")
    public boolean isCartBadgePresent() {
        return actionBot.findElements(cartBadge).size() > 0;
    }

    @Step("Get cart badge count")
    public int getCartBadgeCount() {
        if (!isCartBadgePresent()) return 0;
        String text = actionBot.readText(cartBadge);
        try { return Integer.parseInt(text); }
        catch (Exception e) { return 0; }
    }

    public HomePage cartClick(){
        actionBot.clickElement(cartBadge);
        return this;
    }


    // ====== Sidebar ======

    @Step("Open sidebar menu")
    public HomePage openSidebar() {
        actionBot.clickElement(burgerMenuBtn);
        actionBot.clickElement(exitSidebar);
        return this;
    }

    @Step("Click Logout from sidebar")
    public HomePage clickLogout() {
        actionBot.clickElement(sidebarLogout);
        return this;
    }

    @Step("Click About from sidebar")
    public HomePage clickAbout() {
        actionBot.clickElement(sidebarAbout);
        return this;
    }

    @Step("Check Sidebar Is Opened")
    public boolean isSidebarLinkVisible() {
        return actionBot.isElementDisplayed(By.id("inventory_sidebar_link"));
    }


    // ====== Sorting ======

    @Step("Sort products by visible text: {visibleText}")
    public HomePage sortBy(String visibleText) {
        actionBot.selectByVisibleText(sortDropdown, visibleText);
        return this;
    }

    @Step("Get all product prices as double list")
    public List<Double> getAllPrices() {
        List<Double> prices = new ArrayList<>();
        List<?> elems = actionBot.findElements(itemPrices);

        for (int i = 0; i < elems.size(); i++) {
            String price = actionBot.readText(itemPrices, i)
                    .replace("$", "")
                    .trim();
            prices.add(Double.parseDouble(price));
        }
        return prices;
    }

    // ====== Product Details ======

    @Step("Open product details by name: {productName}")
    public HomePage openProductDetailsByName(String productName) {
        List<?> names = actionBot.findElements(itemNames);
        for (int i = 0; i < names.size(); i++) {
            String name = actionBot.readText(itemNames, i);
            if (name.equalsIgnoreCase(productName)) {
                actionBot.clickElement(itemNames, i);
                return this;
            }
        }
        Assert.fail("Product not found: " + productName);
        return this;
    }

    public CartPage validateCart(){
        String cartUrl = PropertyReader.getProperty("cartUrl");
        Assert.assertTrue(webDriver.getCurrentUrl().contains(cartUrl), "User is at cart page");
        return new CartPage(webDriver);
    }

}