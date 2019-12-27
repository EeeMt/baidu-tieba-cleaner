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
    private ExpectedCondition<List<WebElement>> searchResults = presenceOfAllElementsLocatedBy(By.cssSelector(".s_post > .p_title > a"));
    private ExpectedCondition<WebElement> nextSearchPage = presenceOfElementLocated(By.cssSelector(".next"));
    private ExpectedCondition<WebElement> postDelLink = presenceOfElementLocated(By.cssSelector(".p_post_del"));
    private ExpectedCondition<WebElement> moreLink = presenceOfElementLocated(By.cssSelector(".lzl_more > a"));
    private ExpectedCondition<List<WebElement>> replyDelLink = presenceOfAllElementsLocatedBy(By.cssSelector("a.p_post_del, a.p_post_del_my, .lzl_jb > a.lzl_jb_in, .j_lzl_del, .lzl_delate_in"));
    private ExpectedCondition<WebElement> dialogConfirmBtn = presenceOfElementLocated(By.cssSelector(".dialogJbtn:nth-child(1)"));
    private ExpectedCondition<WebElement> dialogErrorMessage = presenceOfElementLocated(By.cssSelector(".d_messager_txt"));
    private ExpectedCondition<WebElement> dialogErrorConfirm = presenceOfElementLocated(By.cssSelector(".j_messager_ok"));
}
