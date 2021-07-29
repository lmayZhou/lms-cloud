package com.lmaye.cloud.starter.rabbitmq.service;

/**
 * -- Rabbitmq Service
 *
 * @author Lmay Zhou
 * @date 2021/7/29 23:26
 * @email lmay@lmaye.com
 * @sine jdk1.8
 */
public interface IRabbitmqService {
    /**
     * 发送消息
     *
     * @param routingKey 路由键
     * @param message    消息
     */
    void send(String routingKey, Object message);

    /**
     * 发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     */
    void send(String exchange, String routingKey, Object message);
}
