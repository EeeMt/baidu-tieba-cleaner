package me.ihxq.projects.baidutiebacleaner.runner;

import com.google.common.io.Files;
import me.ihxq.projects.baidutiebacleaner.config.AuthCookies;
import me.ihxq.projects.baidutiebacleaner.config.RunProperty;
import me.ihxq.projects.baidutiebacleaner.config.RunProperty.Selectors;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static me.ihxq.projects.baidutiebacleaner.utils.TabSwitcher.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

/**
 * @author xq.h
 * 2019/12/21 02:11
 **/
@SuppressWarnings("UnstableApiUsage")
@Component
public class Cleaner {

    private final RunProperty runProperty;
    private final AuthCookies authCookies;
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Selectors selectors;


    public Cleaner(RunProperty runProperty, AuthCookies authCookies) {
        this.runProperty = runProperty;
        this.authCookies = authCookies;

        selectors = runProperty.getSelectors();

        System.setProperty("webdriver.chrome.driver", runProperty.getChromeDriverPath());
        driver = new ChromeDriver(new ChromeOptions().addArguments(runProperty.getChromeOptions()));
        wait = new WebDriverWait(driver, runProperty.getTimeOutInSeconds());
    }

    private void addAuthCookiesAndRefresh(WebDriver driver) {
        this.addAuthCookies(driver);
        driver.navigate().refresh();
    }

    private void addAuthCookies(WebDriver driver) {
        driver.manage().addCookie(authCookies.getBDUSS());
        driver.manage().addCookie(authCookies.getStoken());
    }


    private WebElement waitForElement(By selector) {
        return wait.until(presenceOfElementLocated(selector));
    }

    private WebElement waitForElement(ExpectedCondition<WebElement> condition) {
        return wait.until(condition);
    }

    private List<WebElement> waitForElements(By selector) {
        return wait.until(presenceOfAllElementsLocatedBy(selector));
    }

    private List<WebElement> waitForElements(ExpectedCondition<List<WebElement>> condition) {
        return wait.until(condition);
    }

    private void scrollDown() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,2)");
        //((JavascriptExecutor) driver).executeScript("window.scrollBy(0, window.innerHeight*0.8)");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() throws InterruptedException {
        try {
            driver.get("https://tieba.baidu.com/");

            //addCookies(driver);
            addAuthCookiesAndRefresh(driver);
            waitForElement(selectors.getUsernameInHeader()).click();

            addAuthCookiesAndRefresh(driver);
            waitForElement(selectors.getMyPublishes()).click();

            closeCurrentAndSwitchToNextTab(driver);

            List<WebElement> themes = waitForElements(selectors.getEntrancesInPostList());

            themes.stream()
                    //.skip(3)
                    .forEach(this::execDelete);

            waitForElement(selectors.getMyReplyPage()).click();

            List<WebElement> replies = waitForElements(selectors.getMyReplies());
            replies.stream()
                    .forEach(this::execDeleteReply);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private boolean isValidPage() {
        return !runProperty.getInvalidContentPageTitlePattern()
                .matcher(driver.getTitle())
                .find();
    }

    private void execDeleteReply(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            switchToNextTab(driver);
            scrollDown();
            if (isValidPage()) {
                waitForElement(selectors.getDelLinkInPost()).click();
                waitForElement(selectors.getConfirmDelBtnInPost()).click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCurrentAndSwitchToPreviousTab(driver);
        }
    }


    private void execDelete(WebElement element) {
        try {
            element.click();
            switchToNextTab(driver);
            scrollDown();
            //new Actions(driver).sendKeys(Keys.PAGE_DOWN).perform();
            //new Actions(driver).sendKeys(Keys.UP).perform();
            if (isValidPage()) {
                waitForElement(selectors.getDelLinkInPost()).click();
                waitForElement(selectors.getConfirmDelBtnInPost()).click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCurrentAndSwitchToPreviousTab(driver);
        }
    }

    public void shot(WebDriver driver) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            // now copy the  screenshot to desired location using copyFile method
            Files.copy(src, new File("selenium_" + System.currentTimeMillis() + ".png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
