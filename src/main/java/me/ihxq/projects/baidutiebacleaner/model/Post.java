package me.ihxq.projects.baidutiebacleaner.model;

import me.ihxq.projects.baidutiebacleaner.config.SelectorConfig;
import me.ihxq.projects.baidutiebacleaner.utils.ApplicationContextHolder;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.context.ApplicationContext;

/**
 * @author xq.h
 * 2020/2/1 16:25
 **/
public class Post extends SimplePage implements Deletable {
    public Post(ChromeDriver driver) {
        super(driver);
    }

    @Override
    public ExpectedCondition<WebElement> getDeleteLink() {
        ApplicationContext context = ApplicationContextHolder.get().orElseThrow(() -> new IllegalStateException("application may not ready"));
        SelectorConfig selectorConfig = context.getBean(SelectorConfig.class);
        return selectorConfig.getPostDelLink();
    }
}
