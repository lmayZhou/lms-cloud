package com.lmaye.cloud.starter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmaye.cloud.starter.web.utils.JsonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * -- Web Auto Configuration
 *
 * @author lmay.Zhou
 * @date 2021/5/19 22:05
 * @qq 379839355
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Configuration
@ConditionalOnClass(DispatcherServlet.class)
@EnableConfigurationProperties(WebProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebAutoConfiguration implements WebMvcConfigurer {
    /**
     * Web配置实例
     *
     * @return WebProperties
     */
    @Bean("webProperties")
    WebProperties webProperties() {
        return new WebProperties();
    }

    /**
     * Json序列化
     *
     * @return ObjectMapper
     */
    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        return JsonUtils.getObjectMapper();
    }

    /**
     * 过滤swagger资源
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
