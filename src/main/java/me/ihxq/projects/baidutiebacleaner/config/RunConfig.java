package me.ihxq.projects.baidutiebacleaner.config;

import lombok.Data;
import me.ihxq.projects.baidutiebacleaner.runner.Cleaner;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xq.h
 * 2019/12/21 15:55
 **/
@Data
@ConfigurationProperties(prefix = "run")
public class RunConfig {
    public static final String CHROMEDRIVER_PATH = "driver/chromedriver";

    private AuthConfig authConfig = new AuthConfig();

    private List<String> chromeOptions = Stream.of(
            "--window-size=1920,1080",
            //"--blink-settings=imagesEnabled=false",
            "--disable-gpu",
            //"--headless",
            "--no"
    ).collect(Collectors.toList());

    private boolean processMyPosts = false;
    private boolean processMyReplies = false;
    private boolean processSearchResults = true;

    private Duration longWait = Duration.ofSeconds(5);
    private Duration shortWait = Duration.ofSeconds(1);

    private Pattern invalidContentPageTitlePattern = Pattern.compile("^(百度贴吧)|(贴吧404)$");

    public void setInvalidPageTitleRegex(String invalidPageTitleRegex) {
        this.invalidContentPageTitlePattern = Pattern.compile(invalidPageTitleRegex);
    }

    public AuthCookies constructAuthCookies() {
        return new AuthCookies(authConfig);
    }

    public ChromeDriver constructChromeDriver() {
        String chromeDriverPath = Objects.requireNonNull(Cleaner.class.getClassLoader().getResource(CHROMEDRIVER_PATH)).getPath();
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions().addArguments(this.getChromeOptions());
        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                .withSilent(true)
                .build();
        return new ChromeDriver(chromeDriverService, options);
    }

    public WebDriverWait constructLongWait(ChromeDriver driver) {
        return new WebDriverWait(driver, this.getLongWait().getSeconds());
    }

    public WebDriverWait constructShortWait(ChromeDriver driver) {
        return new WebDriverWait(driver, this.getShortWait().getSeconds());
    }

    @Data
    public static class AuthConfig {
        private String bdussCookie;

        private String sToken;
    }

}
