package me.ihxq.projects.baidutiebacleaner.runner;

import me.ihxq.projects.baidutiebacleaner.config.RunConfig;
import me.ihxq.projects.baidutiebacleaner.config.SelectorConfig;

/**
 * @author xq.h
 * 2020/2/1 17:53
 **/
public class SearchResultDeletionRunner extends DeletionRunner {
    public SearchResultDeletionRunner(RunConfig runConfig, SelectorConfig selectorConfig) {
        super(runConfig, selectorConfig);
    }

    @Override
    protected void execute() {
        this.signInHome();

    }
}
