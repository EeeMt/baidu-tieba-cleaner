package me.ihxq.projects.baidutiebacleaner.run;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.baidutiebacleaner.config.RunConfig;
import me.ihxq.projects.baidutiebacleaner.config.SelectorConfig;
import me.ihxq.projects.baidutiebacleaner.runner.SearchResultDeletionRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author xq.h
 * 2020/2/1 17:57
 **/
@Slf4j
@Component
public class Scheduler {

    private final RunConfig runConfig;
    private final SelectorConfig selectorConfig;

    public Scheduler(RunConfig runConfig,
                     SelectorConfig selectorConfig) {
        this.runConfig = runConfig;
        this.selectorConfig = selectorConfig;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void dailyRun() {
        try {
            if (runConfig.isProcessMyPosts()) {
                log.info("process MyPosts");
            }
            if (runConfig.isProcessMyReplies()) {
                log.info("process MyReplies");
            }
        } catch (Exception e) {
            log.error("fatal error occurred while executing, quit task.", e);
        }
    }

    private void processSearchResults() {
        log.info("process SearchResults");
        try {
            new SearchResultDeletionRunner(runConfig, selectorConfig).run();
        } catch (Exception e) {
            log.error("fatal error occurred while processSearchResults, interrupt task.", e);
        }

    }

}
