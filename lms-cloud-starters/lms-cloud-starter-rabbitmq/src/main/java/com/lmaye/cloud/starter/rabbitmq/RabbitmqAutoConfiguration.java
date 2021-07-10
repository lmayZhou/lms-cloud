package com.lmaye.cloud.starter.rabbitmq;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

/**
 * -- RabbitMQ Auto Configuration
 *
 * @author lmay.Zhou
 * @date 2021/7/3 22:47
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Configuration
@ConditionalOnBean(RabbitProperties.class)
@AutoConfigureAfter(org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class)
public class RabbitmqAutoConfiguration {
}
