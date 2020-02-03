package me.ihxq.projects.baidutiebacleaner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableRetry
@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = BaiduTiebaCleanerApplication.class)
public class BaiduTiebaCleanerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaiduTiebaCleanerApplication.class, args);
    }

}
