package me.ihxq.projects.baidutiebacleaner.model;

import java.io.Closeable;

/**
 * @author xq.h
 * on 2020/2/1 16:35
 **/
public interface Page extends DriverHolder, Closeable {

    @Override
    default void close() {
        this.getDriver().close();
    }
}
