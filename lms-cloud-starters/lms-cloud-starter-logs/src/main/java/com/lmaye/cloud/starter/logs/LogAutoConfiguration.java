package com.lmaye.cloud.starter.logs;

import com.lmaye.cloud.starter.logs.aspect.LogAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * -- Log Auto Configuration
 *
 * @author lmay.Zhou
 * @date 2021/7/26 11:59
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
public class LogAutoConfiguration {
    /**
     * Log Aspect
     *
     * @return LogAspect
     */
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
