package com.lmaye.cloud.starter.canal.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * -- CanalThreadUncaughtExceptionHandler
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
@Slf4j
public class CanalThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("thread {} have a exception {}", t.getName(), e);
    }
}
