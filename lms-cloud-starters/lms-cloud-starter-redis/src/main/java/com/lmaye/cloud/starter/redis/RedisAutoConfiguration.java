package com.lmaye.cloud.starter.redis;

import lombok.AllArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * -- Redis Auto Configuration
 *
 * @author lmay.Zhou
 * @date 2021/7/3 22:47
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Configuration
@AllArgsConstructor
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@AutoConfigureAfter(org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
public class RedisAutoConfiguration {
    /**
     * Redis Properties
     */
    private final RedisProperties redisProperties;

    /**
     * Redisson客户端
     *
     * @return RedissonClient
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient() {
        Config config = new Config();
        long timeout = 3000;
        if (!Objects.isNull(redisProperties.getTimeout())) {
            timeout = redisProperties.getTimeout().toMillis();
        }
        if (!Objects.isNull(redisProperties.getSentinel())) {
            List<String> nodes = redisProperties.getSentinel().getNodes();
            config.useSentinelServers()
                    .setMasterName(redisProperties.getSentinel().getMaster())
                    .setPingConnectionInterval(1000)
                    .addSentinelAddress(convert(nodes))
                    .setDatabase(redisProperties.getDatabase())
                    .setConnectTimeout((int) timeout)
                    .setPassword(redisProperties.getPassword());
        } else if (!Objects.isNull(redisProperties.getCluster())) {
            List<String> nodes = redisProperties.getCluster().getNodes();
            config.useClusterServers()
                    .addNodeAddress(convert(nodes))
                    .setPingConnectionInterval(1000)
                    .setConnectTimeout((int) timeout)
                    .setPassword(redisProperties.getPassword());
        } else {
            String prefix = "redis://";
            if (redisProperties.isSsl()) {
                prefix = "rediss://";
            }
            config.useSingleServer()
                    .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                    .setConnectTimeout((int) timeout)
                    .setDatabase(redisProperties.getDatabase())
                    .setPassword(redisProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 转换
     *
     * @param nodesObject 节点
     * @return String[]
     */
    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith("redis://") && !node.startsWith("rediss://")) {
                nodes.add("redis://" + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[0]);
    }
}
