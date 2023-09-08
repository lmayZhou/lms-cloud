package com.lmaye.cloud.starter.serialno;

import com.lmaye.cloud.core.utils.StringCoreUtils;
import com.lmaye.cloud.starter.serialno.service.ISerialNoService;
import com.lmaye.cloud.starter.serialno.service.impl.SerialNoServiceImpl;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
     * Redisson Client
     */
    @Autowired
    private RedissonClient redissonClient;

    /**
     * Serial No Properties
     *
     * @return SerialNoProperties
     */
    @Bean
    SerialNoProperties serialNoProperties() {
        return new SerialNoProperties();
    }

    /**
     * Serial No Service
     *
     * @return ISerialNoService
     */
    @Bean
    ISerialNoService serialNoService() {
        return new SerialNoServiceImpl();
    }

    /**
     * 初始Redis全局随机序号
     */
    @PostConstruct
    private void initGlobalRandomNo() {
        final SerialNoProperties serialNoProperties = serialNoProperties();
        final Stream<String> keysStreamByPattern = redissonClient.getKeys().getKeysStreamByPattern("GlobalRandomNo:*");
        if (!serialNoProperties.getIsOrderly() && Objects.equals(0L, keysStreamByPattern.count())) {
            final int globalIdLen = serialNoProperties.getGlobalIdLen();
            final int startNum = Integer.parseInt(StringCoreUtils.fillNumRight(globalIdLen, 1, "0"));
            final int endNum = Integer.parseInt(StringCoreUtils.fillNumRight(globalIdLen, 9, "9"));
            List<Integer> nums = new ArrayList<>();
            for (int i = startNum; i <= endNum; i++) {
                nums.add(i);
            }
            Collections.shuffle(nums);
            final int eachCacheSize = serialNoProperties.getEachCacheSize();
            final int keyCacheSize = (endNum + 1 - startNum) / eachCacheSize + 9;
            for (int i = 10; i <= keyCacheSize; i++) {
                final RList<Integer> rList = redissonClient.getList("GlobalRandomNo:" + i);
                final int x = (i - 10) * eachCacheSize;
                rList.addAll(nums.subList(x, x + eachCacheSize));
            }
        }
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
