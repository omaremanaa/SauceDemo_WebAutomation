package Bots;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitBot {
    private WebDriver webDriver;

    public WaitBot(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void waitFor(Boolean condition ,int seconds){
        new WebDriverWait(webDriver, Duration.ofSeconds(seconds))
                .until(d->condition);
    }
}
