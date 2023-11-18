package com.lmaye.cloud.starter.delay.queue;

import cn.hutool.core.thread.NamedThreadFactory;
import com.lmaye.cloud.starter.delay.queue.service.DelayQueueService;
import com.lmaye.cloud.starter.delay.queue.service.RedisDelayQueueTask;
import com.lmaye.cloud.starter.delay.queue.service.impl.DelayQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * -- Delay Queue Auto Configuration
 *
 * @author Lmay Zhou
 * @date 2021/12/20 16:46
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@EnableAsync
@Configuration
@EnableConfigurationProperties(DelayQueueProperties.class)
@ConditionalOnProperty(value = "enabled", prefix = "delay-queue", matchIfMissing = true)
public class DelayQueueAutoConfiguration {
    /**
     * Delay Queue Properties
     */
    @Autowired
    private DelayQueueProperties properties;

    /**
     * Delay Queue Service
     */
    @Bean
    DelayQueueService delayQueueService() {
        return new DelayQueueServiceImpl();
    }

    /**
     * 配置自定义 RedisTemplate
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @return RedisTemplate<String, Object>
     */
    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 执行服务
     *
     * @return ExecutorService
     */
    @Bean(destroyMethod = "shutdown")
    ExecutorService executorService() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(properties.getCorePoolSize(),
                new NamedThreadFactory("scheduleThreadPool", false), new ThreadPoolExecutor.AbortPolicy());
        executorService.scheduleWithFixedDelay(new RedisDelayQueueTask(delayQueueService()), properties.getInitialDelay(),
                properties.getDelay(), TimeUnit.SECONDS);
        return executorService;
    }
}
