package com.lmaye.cloud.starter.swagger;

import com.lmaye.cloud.starter.swagger.constants.AuthType;
import com.lmaye.cloud.starter.swagger.properties.SwaggerAuthorizationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.swagger.web.ApiKeyVehicle;

import java.util.Collections;
import java.util.List;


/**
 * -- Swagger Authorization Configuration
 *
 * @author Lmay Zhou
 * @date 2021/11/4 13:54
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Configuration
public class SwaggerAuthorizationConfiguration {
    /**
     * Swagger Authorization Properties
     */
    @Autowired
    public SwaggerAuthorizationProperties swaggerAuthorizationProperties;

    /**
     * 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配
     *
     * @return SecurityContext
     */
    public SecurityContext securityContext() {
        // 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配；默认 ^.*$ 匹配所有URL
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        List<SecurityReference> defaultAuth = Collections.singletonList(
                SecurityReference.builder().reference(swaggerAuthorizationProperties.getName()).scopes(authorizationScopes).build());
        // 其中 securityReferences 为配置启用的鉴权策略
        return SecurityContext.builder().securityReferences(defaultAuth).operationSelector(it ->
                PathSelectors.regex(swaggerAuthorizationProperties.getAuthRegex()).test(it.requestMappingPattern())).build();
    }

    /**
     * Authorization 配置项
     *
     * @return List<SecurityScheme>
     */
    public List<SecurityScheme> getSecuritySchemes() {
        String type = swaggerAuthorizationProperties.getType();
        if (AuthType.BasicAuth.name().equalsIgnoreCase(type)) {
            return Collections.singletonList(basicAuth());
        } else if (AuthType.ApiKey.name().equalsIgnoreCase(type)) {
            return Collections.singletonList(apiKey());
        }
        return null;
    }

    /**
     * 配置基于 ApiKey 的鉴权对象
     *
     * @return ApiKey
     */
    private ApiKey apiKey() {
        return new ApiKey(swaggerAuthorizationProperties.getName(), swaggerAuthorizationProperties.getKeyName(), ApiKeyVehicle.HEADER.getValue());
    }

    /**
     * 配置基于 BasicAuth 的鉴权对象
     *
     * @return BasicAuth
     */
    private BasicAuth basicAuth() {
        return new BasicAuth(swaggerAuthorizationProperties.getName());
    }
}
