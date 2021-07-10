package com.lmaye.cloud.starter.canal;

import com.lmaye.cloud.starter.canal.handler.CanalThreadUncaughtExceptionHandler;
import com.lmaye.cloud.starter.canal.properties.CanalProperties;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * -- ThreadPoolAutoConfiguration
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
//@Configuration
@ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
public class ThreadPoolAutoConfiguration {
    @Bean(destroyMethod = "shutdown")
    public ExecutorService executorService() {
        return new ScheduledThreadPoolExecutor(10, new BasicThreadFactory.Builder()
                .namingPattern("canal-execute-thread-%d")
                .uncaughtExceptionHandler(new CanalThreadUncaughtExceptionHandler()).build());
    }
}
