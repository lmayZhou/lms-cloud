package com.lmaye.cloud.starter.delay.queue.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * -- Redis Delay Queue Task
 *
 * @author Lmay Zhou
 * @date 2021/12/14 17:55
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Slf4j
@AllArgsConstructor
public class RedisDelayQueueTask implements Runnable {
    /**
     * Delay Queue Service
     */
    private final DelayQueueService delayQueueService;

    /**
     * 线程处理
     */
    @Override
    public void run() {
        delayQueueService.taskHandle();
    }
}
