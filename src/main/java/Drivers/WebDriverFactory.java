package Drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import Utils.PropertyReader;

public class WebDriverFactory {
    private static ThreadLocal<WebDriver> driverThreadLocal= new ThreadLocal<>();

    private static WebDriver getDriver(){
        String browserType= PropertyReader.getProperty("browserType");
        Browser browser= Browser.valueOf(browserType);
        AbstractDriver abstractDriver=browser.getDriverFactory();
        return abstractDriver.createDriver();
    }


    public static WebDriver initDriver(){
        WebDriver driver= ThreadGuard.protect(getDriver());
        driverThreadLocal.set(driver);
        return driverThreadLocal.get();
    }

    public static WebDriver get(){
        return driverThreadLocal.get();
    }
    public static void quitDriver(){
        driverThreadLocal.get().quit();
    }

}