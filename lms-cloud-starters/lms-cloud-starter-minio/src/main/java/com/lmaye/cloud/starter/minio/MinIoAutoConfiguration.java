package com.lmaye.cloud.starter.minio;

import com.lmaye.cloud.starter.minio.service.ICleanCacheService;
import com.lmaye.cloud.starter.minio.service.IMinIoClientService;
import com.lmaye.cloud.starter.minio.service.IMinIoFileStoreService;
import com.lmaye.cloud.starter.minio.service.impl.CleanCacheServiceImpl;
import com.lmaye.cloud.starter.minio.service.impl.MinIoClientServiceImpl;
import com.lmaye.cloud.starter.minio.service.impl.MinIoFileStoreServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * -- MinIO Auto Configuration
 *
 * @author lmay.Zhou
 * @date 2020/10/12 15:50 星期一
 * @since Email: lmay_zlm@meten.com
 */
@Configuration
@EnableConfigurationProperties(MinIoStoreProperties.class)
public class MinIoAutoConfiguration {
    /**
     * MinIo File Store Service
     * @return IMinIoFileStoreService
     */
    @Bean
    @ConditionalOnMissingBean(IMinIoFileStoreService.class)
    IMinIoFileStoreService minIoFileStoreService() {
        return new MinIoFileStoreServiceImpl();
    }

    /**
     * Clean Cache Service
     *
     * @return ICleanCacheService
     */
    @Bean
    @ConditionalOnMissingBean(ICleanCacheService.class)
    ICleanCacheService cleanCacheService() {
        return new CleanCacheServiceImpl();
    }

    /**
     * MinIo Client Service
     *
     * @return IMinIoClientService
     */
    @Bean
    @ConditionalOnMissingBean(IMinIoClientService.class)
    IMinIoClientService minIoClientService() {
        return new MinIoClientServiceImpl();
    }
}
