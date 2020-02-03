package me.ihxq.projects.baidutiebacleaner.utils;

import org.openqa.selenium.WebDriver;

import java.util.LinkedList;

/**
 * @author xq.h
 * 2019/12/21 17:22
 **/
public class TabSwitchUtil {
    private static final LinkedList<String> tabCache = new LinkedList<>();

    private static void loadIntoTabCache(WebDriver driver) {
        tabCache.clear();
        tabCache.addAll(driver.getWindowHandles());
    }

    public static void closeCurrentTab(WebDriver driver) {
        driver.close();
    }

    public static void closeCurrentAndSwitchToNextTab(WebDriver driver) {
        closeCurrentTab(driver);
        switchToNextTab(driver);
    }

    public static void closeCurrentAndSwitchToPreviousTab(WebDriver driver) {
        closeCurrentTab(driver);
        switchToPreviousTab(driver);
    }

    public static void switchToNextTab(WebDriver driver) {
        loadIntoTabCache(driver);
        if (tabCache.size() == 1) {
            driver.switchTo().window(tabCache.get(0));
            return;
        }
        String current = driver.getWindowHandle();
        int currentIndex = tabCache.indexOf(current);
        if (currentIndex == tabCache.size() - 1) {
            driver.switchTo().window(tabCache.get(0));
        } else {
            driver.switchTo().window(tabCache.get(currentIndex + 1));
        }
    }

    public static void switchToPreviousTab(WebDriver driver) {
        loadIntoTabCache(driver);
        if (tabCache.size() == 1) {
            driver.switchTo().window(tabCache.get(0));
            return;
        }
        String current = driver.getWindowHandle();
        int currentIndex = tabCache.indexOf(current);
        if (currentIndex == 0) {
            driver.switchTo().window(tabCache.get(tabCache.size() - 1));
        } else {
            driver.switchTo().window(tabCache.get(currentIndex - 1));
        }
    }


}
