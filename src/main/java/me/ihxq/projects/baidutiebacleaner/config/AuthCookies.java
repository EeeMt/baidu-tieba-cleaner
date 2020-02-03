package me.ihxq.projects.baidutiebacleaner.config;

import lombok.Getter;
import me.ihxq.projects.baidutiebacleaner.config.RunConfig.AuthConfig;
import org.openqa.selenium.Cookie;

import java.util.Date;

/**
 * @author xq.h
 * 2019/12/21 16:18
 **/
@Getter
public class AuthCookies {

    private final Cookie BDUSS;
    private final Cookie stoken;

    public AuthCookies(AuthConfig authConfig) {
        Date expiry = new Date(System.currentTimeMillis() + 1_000_000_000);
        BDUSS = new Cookie("BDUSS", authConfig.getBdussCookie(), ".baidu.com", "/", expiry);
        stoken = new Cookie("STOKEN", authConfig.getSToken(), ".tieba.baidu.com", "/", expiry);
    }

}

