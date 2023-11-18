package com.lmaye.cloud.starter.delay.queue.producer;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.lmaye.cloud.starter.delay.queue.DelayQueueProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * -- Kafka Producer
 *
 * @author Lmay Zhou
 * @date 2021/12/16 14:04
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Slf4j
@Component
public class KafkaProducer {
    /**
     * Kafka Template
     */
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Delay Queue Properties
     */
    @Autowired
    private DelayQueueProperties properties;

    /**
     * 发送消息
     *
     * @param topic 主题
     * @param msg   消息
     */
    @Async
    public void send(String topic, String msg) throws Exception {
        Callable<SendResult<String, String>> callable = () -> {
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, msg);
            send.addCallback(result -> {
                if (!Objects.isNull(result) && !Objects.isNull(result.getRecordMetadata())) {
                    log.debug("消息发送成功 offset: {}", result.getRecordMetadata().offset());
                }
            }, throwable -> log.error("消息发送失败: {}", throwable.getMessage()));
            return send.get();
        };
        Retryer<SendResult<String, String>> retry = RetryerBuilder.<SendResult<String, String>>newBuilder()
                .retryIfResult(Objects::isNull)
                .retryIfResult(result -> Objects.isNull(result.getRecordMetadata()))
                .retryIfException()
                .withStopStrategy(StopStrategies.stopAfterAttempt(properties.getRetryNums().intValue()))
                .withWaitStrategy(WaitStrategies.fixedWait(properties.getRetrySleepTime(), TimeUnit.SECONDS))
                .withRetryListener(new KafkaRetryListener(properties)).build();
        retry.call(callable);
    }
}
