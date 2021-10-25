package com.lmaye.cloud.starter.web.config;

import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * -- Feign Encoder Config
 *
 * @author lmay.Zhou
 * @date 2020/12/2 11:39
 * @email lmay@lmaye.com
 */
@Slf4j
@AllArgsConstructor
public class FeignEncoderConfig {
    /**
     * ObjectFactory
     */
    private final ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * 请求头
     */
    private static final String[] HEADERS = {"Authorization", "clientId", "language", "timeZone"};

    /**
     * feign支持文件，同时实体类作为参数接收
     *
     * @return Encoder
     */
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    /**
     * feign请求头添加token
     *
     * @return RequestInterceptor
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (!Objects.isNull(attributes)) {
                HttpServletRequest request = attributes.getRequest();
                log.info("Feign request: {}", request.getRequestURI());
                for(String key : HEADERS) {
                    requestTemplate.header(key, request.getHeader(key));
                }
            }
        };
    }
}