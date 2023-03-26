package com.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.stepdefinitions.MyLogsAndSoftAssertions;
import io.cucumber.messages.internal.com.google.common.collect.Maps;

public class PlaywrightFactory {

    Properties prop;
    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();//ThreadLocal copies our browser
    private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static ThreadLocal<Map> tlMap = new ThreadLocal<>();
    static ThreadLocal<MyLogsAndSoftAssertions> tlLogAndAssert = new ThreadLocal<>();


    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }
    public static Browser getBrowser() {
        return tlBrowser.get();
    }
    public static BrowserContext getBrowserContext() {
        return tlBrowserContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }
    public static Map getThreadMap() {
        if (tlMap.get() == null) {
            LinkedHashMap<String, Object> linkedHashMap = Maps.newLinkedHashMap();
            tlMap.set(linkedHashMap);
        }
        return tlMap.get();
    }
    public static MyLogsAndSoftAssertions logOrAssert() {
        if (tlLogAndAssert.get() == null) {
            MyLogsAndSoftAssertions logAndAssert = new MyLogsAndSoftAssertions();
            tlLogAndAssert.set(logAndAssert);
        }
        return tlLogAndAssert.get();
    }
    public static void removeLogAndAssert() {
        tlLogAndAssert.remove();
    }
    public static void removeThreadMap() {
        tlMap.remove();
    }
    public Page initBrowser(Properties prop) {
        String browserName = prop.getProperty("browser").trim();
        boolean isHeadless = Boolean.parseBoolean(prop.getProperty("headless").trim());
        System.out.println("Browser name is : " + browserName);

        tlPlaywright.set(Playwright.create());//set a local copy of Playwright

        switch (browserName.toLowerCase()) {
            case "chromium":
                tlBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless)));
                break;
            case "firefox":
                tlBrowser.set(getPlaywright().firefox().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless)));
                break;
            case "safari":
                tlBrowser.set(getPlaywright().webkit().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless)));
                break;
            case "chrome":
                tlBrowser.set(
                        getPlaywright().chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(isHeadless)));
                break;
            case "edge":
                tlBrowser.set(
                        getPlaywright().chromium().launch(new LaunchOptions().setChannel("msedge").setHeadless(isHeadless)));
                break;

            default:
                System.out.println("**please pass the right browser name......");
                break;
        }

        tlBrowserContext.set(getBrowser().newContext());
        tlPage.set(getBrowserContext().newPage());
        //set default timeout for browser (default:30000)
        getPage().setDefaultTimeout(5000);
        getPage().navigate(prop.getProperty("url").trim());
        return getPage();
    }

    /**
     * this method is used to initialize the properties from config file
     */
    public Properties init_prop() {
        try {
            FileInputStream ip = new FileInputStream("src/test/resources/config/config.properties");
            prop = new Properties();
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * take screenshot
     */
    public static void takeScreenshot() {
        String date = BrowserUtilities.getDateAndTime();
        byte[] screenshot = PlaywrightFactory.getPage().screenshot(new Page.ScreenshotOptions());
        getThreadMap().put("screenShot_" + date, screenshot);
    }
}
