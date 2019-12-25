package me.ihxq.projects.baidutiebacleaner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("me.ihxq.projects.baidutiebacleaner.config")
@SpringBootApplication
public class BaiduTiebaCleanerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaiduTiebaCleanerApplication.class, args);
    }

}
