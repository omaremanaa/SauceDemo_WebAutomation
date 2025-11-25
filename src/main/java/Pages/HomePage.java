package Pages;

import Bots.ActionBot;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver webDriver;
    private ActionBot actionBot;


    public HomePage (WebDriver webDriver){
        this.webDriver = webDriver;
        this.actionBot = new ActionBot(webDriver);
    }

}