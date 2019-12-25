package me.ihxq.projects.baidutiebacleaner.config;

import lombok.Getter;
import org.openqa.selenium.Cookie;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author xq.h
 * 2019/12/21 16:18
 **/
@Configuration
@Getter
public class AuthCookies {

    private final Cookie BDUSS;
    private final Cookie stoken;

    public AuthCookies(RunProperty runProperty) {
        Date expiry = new Date(System.currentTimeMillis() + 1_000_000_000);
        BDUSS = new Cookie("BDUSS", runProperty.getBdussCookie(), ".baidu.com", "/", expiry);
        stoken = new Cookie("STOKEN", runProperty.getSToken(), ".tieba.baidu.com", "/", expiry);

    }

}

