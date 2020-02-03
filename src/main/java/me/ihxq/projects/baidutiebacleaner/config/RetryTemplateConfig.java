package me.ihxq.projects.baidutiebacleaner.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @author xq.h
 * 2019/10/10 16:53
 **/
@Slf4j
@Data
@ConfigurationProperties(prefix = "retry")
public class RetryTemplateConfig {
    public static final String TASK_NAME_ATTR_NAME = "TASK_NAME";

    private int maxRetryCount = 3;

    @Bean
    @Primary
    public RetryTemplate customRetryTemplate() {
        log.info("retryTemplate: maxRetryCount={}", maxRetryCount);
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.registerListener(new DefaultRetryListener());
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(maxRetryCount);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        return retryTemplate;
    }

    public static class DefaultRetryListener extends RetryListenerSupport {
        @Override
        public <T, E extends Throwable> void close(RetryContext context,
                                                   RetryCallback<T, E> callback, Throwable throwable) {
            log.trace("retry done for task: {}", context.getAttribute(TASK_NAME_ATTR_NAME));
            super.close(context, callback, throwable);
        }

        @Override
        public <T, E extends Throwable> void onError(RetryContext context,
                                                     RetryCallback<T, E> callback, Throwable throwable) {
            log.debug("retry for task: {}, count: {}", context.getAttribute(TASK_NAME_ATTR_NAME), context.getRetryCount());
            log.trace("retry for {}, error while trying", context.getAttribute(TASK_NAME_ATTR_NAME), throwable);
            super.onError(context, callback, throwable);
        }

        @Override
        public <T, E extends Throwable> boolean open(RetryContext context,
                                                     RetryCallback<T, E> callback) {
            return super.open(context, callback);
        }
    }
}
