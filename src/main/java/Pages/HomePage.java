package Pages;

import Bots.ActionBot;
import Utils.AllureSoftAssert;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver webDriver;
    private ActionBot actionBot;
    private AllureSoftAssert softAssert;


    public HomePage (WebDriver webDriver){
        this.webDriver = webDriver;
        this.actionBot = new ActionBot(webDriver);
        this.softAssert = new AllureSoftAssert();
    }

}