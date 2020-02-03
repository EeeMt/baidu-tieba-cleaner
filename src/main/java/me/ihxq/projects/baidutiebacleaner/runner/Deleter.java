package me.ihxq.projects.baidutiebacleaner.runner;

import me.ihxq.projects.baidutiebacleaner.model.Deletable;

/**
 * @author xq.h
 * 2020/1/22 12:35
 **/
public interface Deleter<T extends Deletable> {

    boolean delete(T e);
}
