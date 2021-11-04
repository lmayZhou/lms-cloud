package com.lmaye.cloud.starter.swagger;

import com.lmaye.cloud.starter.swagger.properties.SwaggerAuthorizationProperties;
import com.lmaye.cloud.starter.swagger.properties.SwaggerProperties;
import com.lmaye.cloud.starter.swagger.properties.SwaggerUiProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * -- Swagger Auto Configuration
 * <pre>
 *     禁用方法（推荐使用 2）
 *     1：使用注解@Profile({"dev","test"}), 表示在开发或测试环境开启，而在生产关闭。
 *
 *     2：使用注解@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
 *      然后在测试配置或者开发配置中添加springfox.documentation.enable=true即可开启，生产环境不填则默认关闭Swagger.
 * </pre>
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/7/5 10:28 星期日
 */
@Configuration
@EnableConfigurationProperties({SwaggerProperties.class, SwaggerUiProperties.class, SwaggerAuthorizationProperties.class})
@Import({SwaggerUiConfiguration.class, SwaggerAuthorizationConfiguration.class, DocketConfiguration.class})
@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerAutoConfiguration {
    /**
     * Docket Bean Factory Post Processor
     *
     * @return DocketBeanFactoryPostProcessor
     */
    @Bean
    public DocketBeanFactoryPostProcessor docketBeanFactoryPostProcessor() {
        return new DocketBeanFactoryPostProcessor();
    }
}
