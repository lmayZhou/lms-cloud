package com.lmaye.cloud.starter.serialno;

import com.lmaye.cloud.starter.serialno.service.ISerialNoService;
import com.lmaye.cloud.starter.serialno.service.impl.SerialNoServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * -- Serial No Auto Configuration
 *
 * @author Lmay Zhou
 * @date 2021/12/20 16:46
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@EnableAsync
@Configuration
@EnableConfigurationProperties(SerialNoProperties.class)
public class SerialNoAutoConfiguration {
    /**
     * Serial No Service
     */
    @Bean
    ISerialNoService serialNoService() {
        return new SerialNoServiceImpl();
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
}
