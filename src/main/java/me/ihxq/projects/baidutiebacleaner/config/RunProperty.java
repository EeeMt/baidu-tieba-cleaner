package me.ihxq.projects.baidutiebacleaner.config;

import lombok.Data;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * @author xq.h
 * 2019/12/21 15:55
 **/
@Data
@ConfigurationProperties(prefix = "run")
public class RunProperty {
    @SuppressWarnings("SpellCheckingInspection")
    private String chromeDriverPath = "/Users/xiquan/chromedriver";

    @SuppressWarnings("SpellCheckingInspection")
    private String bdussCookie = "VFKSTF4UXp4cldPVDRCSWZ3d1M1ZGQzOGkycjh2dzc2MnhraVNkU0Npa0dwU1JlRVFBQUFBJCQAAAAAAAAAAAEAAADIEisptuy27LbswvPRvwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAYY~V0GGP1dQ";

    @SuppressWarnings("SpellCheckingInspection")
    private String sToken = "9763d25be85c588cded87d21200ada29534ce398e7585419d09b9982ff9468df";

    private List<String> chromeOptions = Stream.of(
            "--window-size=1920,1080",
            //"--headless",
            "--blink-settings=imagesEnabled=false",
            "--disable-gpu"
    ).collect(Collectors.toList());

    private long timeOutInSeconds = 5;
    private long lookupTimeOutInSeconds = 1;

    private Pattern invalidContentPageTitlePattern = Pattern.compile("^(百度贴吧)|(贴吧404)$");

    public void setInvalidPageTitleRegex(String invalidPageTitleRegex) {
        this.invalidContentPageTitlePattern = Pattern.compile(invalidPageTitleRegex);
    }

    private Selectors selectors = new Selectors();

    @Data
    @Getter
    @SuppressWarnings("SpellCheckingInspection")
    public static class Selectors {
        private ExpectedCondition<WebElement> usernameInHeader = presenceOfElementLocated(By.cssSelector(".u_menu_username > a"));
        private ExpectedCondition<WebElement> myPublishes = presenceOfNestedElementLocatedBy(By.cssSelector(".ihome_nav_wrap"), By.linkText("帖子"));
        private ExpectedCondition<WebElement> myPosts = presenceOfNestedElementLocatedBy(By.cssSelector(".sub_nav"), By.linkText("我的帖子"));
        private ExpectedCondition<WebElement> myReplies = presenceOfNestedElementLocatedBy(By.cssSelector(".sub_nav"), By.linkText("我回复的"));
        private ExpectedCondition<List<WebElement>> postThreads = presenceOfAllElementsLocatedBy(By.cssSelector(".thread_title"));
        private By postDelLink = By.cssSelector(".p_post_del");
        private By replyDelLink = By.cssSelector(".j_lzl_del");
        private By dialogConfirmBtn = By.cssSelector(".dialogJbtn:nth-child(1)");
    }
}
