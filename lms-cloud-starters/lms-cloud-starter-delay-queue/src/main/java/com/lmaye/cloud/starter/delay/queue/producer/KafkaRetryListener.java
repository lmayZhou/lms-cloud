package com.lmaye.cloud.starter.delay.queue.producer;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import com.lmaye.cloud.core.utils.GsonUtils;
import com.lmaye.cloud.starter.delay.queue.DelayQueueProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * -- Kafka Retry Listener
 *
 * @author Lmay Zhou
 * @date 2021/12/16 14:04
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Slf4j
@AllArgsConstructor
public class KafkaRetryListener implements RetryListener {
    /**
     * Delay Queue Properties
     */
    private final DelayQueueProperties properties;

    @Override
    public <SendResult> void onRetry(Attempt<SendResult> attempt) {
        if (attempt.hasException()) {
            log.error("[Retry] exception: ", attempt.getExceptionCause());
        } else if (attempt.hasResult()) {
            if (Objects.isNull(attempt.getResult())) {
                log.error("[Retry] return data is null");
            } else {
                log.debug("[Retry] return data is: {}", GsonUtils.toJson(attempt.getResult()));
            }
        }
        if(Objects.equals(properties.getRetryNums(), attempt.getAttemptNumber())) {
            // TODO 重试[RetryNums]次做处理
            log.debug("-------------------> Send Massage <-------------------");
        }
    }
}
