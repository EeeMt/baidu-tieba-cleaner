package me.ihxq.projects.baidutiebacleaner.runner;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.baidutiebacleaner.config.AuthCookies;
import me.ihxq.projects.baidutiebacleaner.config.RunConfig;
import me.ihxq.projects.baidutiebacleaner.config.SelectorConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author xq.h
 * 2020/2/1 17:13
 **/
@Slf4j
public abstract class DeletionRunner {
    public static final String HOME_URL = "http://tieba.baidu.com/i/i/my_tie";
    protected final RunConfig runConfig;
    protected final ChromeDriver driver;
    protected final WebDriverWait longWait;
    protected final WebDriverWait shortWait;
    protected final AuthCookies authCookies;
    protected final SelectorConfig selectors;

    public DeletionRunner(RunConfig runConfig,
                          SelectorConfig selectorConfig) {
        this.runConfig = runConfig;
        this.selectors = selectorConfig;
        this.driver = runConfig.constructChromeDriver();
        this.longWait = runConfig.constructLongWait(this.driver);
        this.shortWait = runConfig.constructShortWait(this.driver);
        this.authCookies = runConfig.constructAuthCookies();
    }

    protected void signInHome() {
        this.getAndLogin(HOME_URL);
    }

    protected void getAndLogin(String url) {
        driver.get(url);
        addAuthCookies(driver);
        driver.get(url);
    }

    protected void addAuthCookies(WebDriver driver) {
        driver.manage().addCookie(authCookies.getBDUSS());
        driver.manage().addCookie(authCookies.getStoken());
    }

    protected String extractUsername() {
        return shortWait.until(selectors.getUsernameInHeader()).getText();
    }

    public void run(){
        try {
            this.execute();
        }finally {
            driver.quit();
        }
    };

    protected abstract void execute();
}
