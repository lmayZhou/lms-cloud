package com.lmaye.cloud.starter.swagger;

import com.lmaye.cloud.starter.swagger.properties.SwaggerUiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

/**
 * -- Swagger Ui Configuration
 *
 * @author Lmay Zhou
 * @date 2021/11/4 13:40
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Configuration
public class SwaggerUiConfiguration {
    /**
     * 界面配置
     *
     * @param properties SwaggerProperties
     * @return UiConfiguration
     */
    @Bean
    public UiConfiguration uiConfiguration(SwaggerUiProperties properties) {
        return UiConfigurationBuilder.builder()
                .deepLinking(properties.getDeepLinking())
                .defaultModelExpandDepth(properties.getDefaultModelExpandDepth())
                .defaultModelRendering(properties.getDefaultModelRendering())
                .defaultModelsExpandDepth(properties.getDefaultModelsExpandDepth())
                .displayOperationId(properties.getDisplayOperationId())
                .displayRequestDuration(properties.getDisplayRequestDuration())
                .docExpansion(properties.getDocExpansion())
                .maxDisplayedTags(properties.getMaxDisplayedTags())
                .operationsSorter(properties.getOperationsSorter())
                .showExtensions(properties.getShowExtensions())
                .tagsSorter(properties.getTagsSorter())
                .validatorUrl(properties.getValidatorUrl()).build();
    }
}
