package me.ihxq.projects.baidutiebacleaner.model;

import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author xq.h
 * 2020/2/1 16:35
 **/
public class SimplePage implements Page {

    private final ChromeDriver driver;

    public SimplePage(ChromeDriver driver) {
        this.driver = driver;
    }

    @Override
    public ChromeDriver getDriver() {
        return driver;
    }
}
