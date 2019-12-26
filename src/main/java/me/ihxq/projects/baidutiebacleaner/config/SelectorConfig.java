package me.ihxq.projects.baidutiebacleaner.config;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * @author xq.h
 * 2019/12/25 14:58
 **/
@Configuration
@Getter
public class SelectorConfig {
    private ExpectedCondition<WebElement> usernameInHeader = presenceOfElementLocated(By.cssSelector(".u_menu_username > a"));
    private ExpectedCondition<WebElement> myPublish = presenceOfNestedElementLocatedBy(By.cssSelector(".ihome_nav_wrap"), By.cssSelector(".nav_post"));
    private ExpectedCondition<WebElement> myPost = presenceOfNestedElementLocatedBy(By.cssSelector(".sub_nav"), By.linkText("我的帖子"));
    private ExpectedCondition<WebElement> myReply = presenceOfNestedElementLocatedBy(By.cssSelector(".sub_nav"), By.linkText("我回复的"));
    private ExpectedCondition<List<WebElement>> posts = presenceOfAllElementsLocatedBy(By.cssSelector(".thread_title"));
    private ExpectedCondition<List<WebElement>> replies = presenceOfAllElementsLocatedBy(By.cssSelector(".b_reply_txt > .b_reply"));
    private ExpectedCondition<WebElement> postDelLink = presenceOfElementLocated(By.cssSelector(".p_post_del"));
    private ExpectedCondition<WebElement> replyDelLink = presenceOfElementLocated(By.cssSelector(".j_jb_ele > a, .lzl_jb > a.lzl_jb_in, .j_lzl_del"));
    private ExpectedCondition<WebElement> dialogConfirmBtn = presenceOfElementLocated(By.cssSelector(".dialogJbtn:nth-child(1)"));
}
