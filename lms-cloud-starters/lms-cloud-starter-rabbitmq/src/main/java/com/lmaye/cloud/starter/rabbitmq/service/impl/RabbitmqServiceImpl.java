package com.lmaye.cloud.starter.rabbitmq.service.impl;

import com.lmaye.cloud.core.utils.GsonUtils;
import com.lmaye.cloud.starter.rabbitmq.service.IRabbitmqService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

/**
 * -- Rabbitmq Service
 *
 * @author Lmay Zhou
 * @date 2021/7/29 23:26
 * @email lmay@lmaye.com
 * @sine jdk1.8
 */
@Slf4j
@AllArgsConstructor
public class RabbitmqServiceImpl implements IRabbitmqService {
    /**
     * Rabbit Template
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param routingKey 路由键
     * @param message    消息
     */
    @Override
    public void send(String routingKey, Object message) {
        rabbitTemplate.convertAndSend(routingKey, message);
        log.debug("routingKey [{}], message: {}", routingKey, GsonUtils.toJson(message));
    }

    /**
     * 发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     */
    @Override
    public void send(String exchange, String routingKey, Object message) {
        String id = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(exchange, routingKey, message, new CorrelationData(id));
        log.debug("exchange [{}], routingKey [{}], id [{}], message: {}", exchange, routingKey, id, GsonUtils.toJson(message));
    }
}
