package com.lmaye.cloud.starter.rabbitmq;

import com.lmaye.cloud.starter.rabbitmq.service.IRabbitmqService;
import com.lmaye.cloud.starter.rabbitmq.service.impl.RabbitmqServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * -- RabbitMQ Auto Configuration
 *
 * @author lmay.Zhou
 * @date 2021/7/3 22:47
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@AllArgsConstructor
@Configuration
@ConditionalOnBean(RabbitProperties.class)
@AutoConfigureAfter(org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class)
public class RabbitmqAutoConfiguration {
    /**
     * Rabbit Template
     */
    private final RabbitTemplate rabbitTemplate;

    @Bean
    IRabbitmqService rabbitMqService() {
        return new RabbitmqServiceImpl(rabbitTemplate);
    }
}
