package com.lmaye.cloud.starter.oss;

import com.lmaye.cloud.starter.oss.service.IOssService;
import com.lmaye.cloud.starter.oss.service.impl.OssServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * -- Oss Auto Configuration
 *
 * @author Lmay Zhou
 * @date 2021/7/14 14:55
 * @email lmay@lmaye.com
 */
@AllArgsConstructor
@Configuration
@ConditionalOnProperty(value = "enabled", prefix = "oss", matchIfMissing = true)
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {
    /**
     * Oss Properties
     */
    private final OssProperties ossProperties;

    @Bean
    IOssService ossService() {
        return new OssServiceImpl(ossProperties);
    }
}
