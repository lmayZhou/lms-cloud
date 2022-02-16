package com.lmaye.cloud.starter.delay.queue;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * -- Delay Queue Properties
 *
 * @author Lmay Zhou
 * @date 2021/12/20 16:48
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@ConfigurationProperties("delay-queue")
public class DelayQueueProperties {
    /**
     * 是否启用
     **/
    private Boolean enabled = false;

    /**
     * 线程池大小
     */
    private Integer corePoolSize = 10;

    /**
     * 初始延迟时间(单位: s)，延迟第一次执行的时间
     */
    private Long initialDelay = 0L;

    /**
     * 线程延迟时间(单位: s)，一个执行的终止和下一个执行的开始之间的延迟
     */
    private Long delay = 30L;

    /**
     * 队列缓存Key
     */
    private String queueCacheKey;

    /**
     * 分布式锁Key
     */
    private String lockKey = "Locked";

    /**
     * 重试次数
     */
    private Long retryNums = 3L;

    /**
     * 重试睡眠时间(单位: s)
     */
    private Long retrySleepTime = 3L;
}
