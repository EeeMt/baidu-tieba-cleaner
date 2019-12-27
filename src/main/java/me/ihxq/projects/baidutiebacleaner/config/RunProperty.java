package me.ihxq.projects.baidutiebacleaner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xq.h
 * 2019/12/21 15:55
 **/
@Data
@ConfigurationProperties(prefix = "run")
public class RunProperty {
    @SuppressWarnings("SpellCheckingInspection")
    private String chromeDriverPath = "classpath:/driver/chromedriver";

    @SuppressWarnings("SpellCheckingInspection")
    private String bdussCookie;

    @SuppressWarnings("SpellCheckingInspection")
    private String sToken;

    private List<String> chromeOptions = Stream.of(
            "--window-size=1920,1080",
            //"--blink-settings=imagesEnabled=false",
            "--disable-gpu",
            //"--headless",
            "--no"
    ).collect(Collectors.toList());

    private boolean processDeleteMyPosts = false;
    private boolean processDeleteMyReplies = false;
    private boolean processDeleteSearchResults = true;


    private long timeOutInSeconds = 5;
    private long lookupTimeOutInSeconds = 1;

    private Pattern invalidContentPageTitlePattern = Pattern.compile("^(百度贴吧)|(贴吧404)$");

    public void setInvalidPageTitleRegex(String invalidPageTitleRegex) {
        this.invalidContentPageTitlePattern = Pattern.compile(invalidPageTitleRegex);
    }

}
