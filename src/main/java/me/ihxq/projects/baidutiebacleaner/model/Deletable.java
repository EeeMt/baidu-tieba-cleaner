package me.ihxq.projects.baidutiebacleaner.model;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author xq.h
 * 2020/2/1 16:26
 **/
public interface Deletable {

    ExpectedCondition<WebElement> getDeleteLink();
}
