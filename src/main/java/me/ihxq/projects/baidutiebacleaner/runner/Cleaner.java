package me.ihxq.projects.baidutiebacleaner.runner;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.baidutiebacleaner.config.AuthCookies;
import me.ihxq.projects.baidutiebacleaner.config.RunProperty;
import me.ihxq.projects.baidutiebacleaner.config.SelectorConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static me.ihxq.projects.baidutiebacleaner.utils.TabSwitcher.closeCurrentAndSwitchToPreviousTab;
import static me.ihxq.projects.baidutiebacleaner.utils.TabSwitcher.switchToNextTab;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * @author xq.h
 * 2019/12/21 02:11
 **/
@SuppressWarnings("UnstableApiUsage")
@Component
@Slf4j
public class Cleaner {

    private final RunProperty runProperty;
    private final AuthCookies authCookies;
    private final ChromeDriver driver;
    private final WebDriverWait wait;
    private final SelectorConfig selectors;


    public Cleaner(RunProperty runProperty, AuthCookies authCookies, SelectorConfig selectors) {
        this.runProperty = runProperty;
        this.authCookies = authCookies;
        this.selectors = selectors;

        String chromeDriverPath = Objects.requireNonNull(Cleaner.class.getClassLoader().getResource("driver/chromedriver")).getPath();
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions().addArguments(runProperty.getChromeOptions());
        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                .withSilent(true)
                .build();
        driver = new ChromeDriver(chromeDriverService, options);
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

    private void getAndLogin(String url) {
        driver.get(url);
        addAuthCookies(driver);
        driver.get(url);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void run() throws InterruptedException {
        try {
            getAndLogin("http://tieba.baidu.com/i/i/my_tie");

            String username = waitForElement(selectors.getUsernameInHeader()).getText();

            if (runProperty.isProcessDeleteMyPosts()) {
                log.info("process MyPosts");
                this.processPosts();
            }
            if (runProperty.isProcessDeleteMyReplies()) {
                log.info("process MyReplies");
                this.processReplies();
            }
            if (runProperty.isProcessDeleteSearchResults()) {
                log.info("process SearchResults");
                this.processSearchResults(username);
            }

            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private void processPosts() {
        List<WebElement> themes = waitForElements(selectors.getPosts());

        themes.forEach(this::execDelete);
    }

    private void processReplies() {
        waitForElement(selectors.getMyReply()).click();

        List<WebElement> replies = waitForElements(selectors.getReplies());
        replies.forEach(this::execDeleteReply);
    }

    private String genSearchResultPageLink(String encodedUsername, int pageNum) {
        //return "http://tieba.baidu.com/f/search/res?ie=utf-8&qw=" + encodedUsername + "&pn=" + pageNum;
        return "http://tieba.baidu.com/f/search/ures?ie=utf-8&kw=&qw=&rn=10&sm=1&un=" + encodedUsername;

    }

    private void processSearchResults(String username) {
        try {
            String encodedUsername = URLEncoder.encode(username, "utf-8");
            String url = "http://tieba.baidu.com/f/search/ures?ie=utf-8&kw=&qw=&rn=10&sm=1&un=" + encodedUsername;
            driver.get(url);

            do {
                this.pagingProcessSearchResults();
            } while (nextPage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private boolean nextPage() {
        try {
            waitForElement(selectors.getNextSearchPage()).click();
            log.info("next page");
        } catch (Exception e) {
            log.info("no next page button found, may be it's the end");
            return false;
        }
        return true;
    }

    private void pagingProcessSearchResults() {
        List<WebElement> results = null;
        try {
            results = waitForElements(selectors.getSearchResults());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("no thread found, may be there's no thread on the page");
        }
        if (results != null) {
            results.forEach(this::execDeleteReply);
        }
        try {
            waitForElement(selectors.getNextSearchPage()).click();
        } catch (Exception e) {
            log.info("no next page button found, may be it's the end");
            throw e;
        }

    }

    private boolean isValidPage() {
        return !runProperty.getInvalidContentPageTitlePattern()
                .matcher(driver.getTitle())
                .find();
    }

    private void execDeleteReply(WebElement element) {
        log.info("processing: {}", element.getText());
        try {
            element.click();
        } catch (Exception e) {
            log.error("can not click the link");
            return;
        }
        try {
            switchToNextTab(driver);
            if (isValidPage()) {
                if (tryDelete()) {
                    return;
                }
                if (tryDelete()) {
                    return;
                }
                WebElement moreLink = waitForElement(selectors.getMoreLink());
                new Actions(driver).moveToElement(moreLink).perform();
                this.scrollIntoView(moreLink);
                moreLink.click();
                if (tryDelete()) {
                    return;
                }
                System.out.println();

            } else {
                log.info("delete skip for page invalid: {}", driver.getTitle());
            }

        } catch (TimeoutException e) {
            log.error("delete failed for founding no deletion link");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("delete failed for unknown reason");
        } finally {
            try {
                closeCurrentAndSwitchToPreviousTab(driver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void pageDown() {
        new Actions(driver).sendKeys(Keys.PAGE_DOWN).build().perform();
    }

    private void scrollIntoView(WebElement element) {
        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        driver.executeScript(scrollElementIntoMiddle, element);
        //driver.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private boolean tryDelete() throws InterruptedException {
        try {
            List<WebElement> webElements = null;
            try {
                webElements = waitForElements(selectors.getReplyDelLink());
            } catch (Exception e) {
                pageDown();
                webElements = waitForElements(selectors.getReplyDelLink());
            }
            driver.executeScript("$('.lzl_jb').css('display','inline')");
            webElements = webElements.stream()
                    .filter(v -> v.getText().equals("删除"))
                    .collect(Collectors.toList());
            if (webElements.isEmpty()) {
                return false;
            }
            for (WebElement webElement : webElements) {
                this.scrollIntoView(webElement);
                wait.until(refreshed(elementToBeClickable(webElement)));
                webElement.click();
                waitForElement(selectors.getDialogConfirmBtn()).click();
                Thread.sleep(500);
                try {
                    String message = driver.findElement(By.cssSelector(".dialogJbody > .d_messager_txt")).getText();
                    log.warn("message: {}", message);
                    waitForElement(selectors.getDialogErrorConfirm()).click();
                } catch (NoSuchElementException e) {
                    log.info("success");
                    continue;
                }

            }
            return true;
        } catch (TimeoutException ignore) {
            //e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            throw e;
        }
    }


    private void execDelete(WebElement element) {
        log.info("processing: {}", element.getText());
        try {
            element.click();
            switchToNextTab(driver);
            scrollDown();
            //new Actions(driver).sendKeys(Keys.PAGE_DOWN).perform();
            //new Actions(driver).sendKeys(Keys.UP).perform();
            if (isValidPage()) {
                waitForElement(selectors.getPostDelLink()).click();
                waitForElement(selectors.getDialogConfirmBtn()).click();
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
