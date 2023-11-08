package com.lmaye.cloud.starter.swagger;

import com.lmaye.cloud.starter.swagger.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * -- Docket Configuration
 *
 * @author Lmay Zhou
 * @date 2021/11/4 13:51
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Configuration
public class DocketConfiguration implements BeanFactoryAware {
    /**
     * Bean Factory
     */
    private BeanFactory beanFactory;

    /**
     * Swagger Properties
     */
    @Autowired
    private SwaggerProperties properties;

    /**
     * bean name
     */
    private static final String BEAN_NAME = "spring-boot-starter-swagger-";

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * API文档基本信息
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI apiInfo() {
        final SwaggerProperties.Contact contact = properties.getContact();
        return new OpenAPI().info(new Info().title(properties.getTitle()).description(properties.getDescription())
                        .version(properties.getVersion())
                        .license(new License().name(properties.getLicense()).url(properties.getLicenseUrl()))
                        .contact(new Contact().name(contact.getName()).url(contact.getUrl()).email(contact.getEmail()))
                        .termsOfService(properties.getTermsOfServiceUrl()))
                .components(new Components().addSecuritySchemes("Authorization", new SecurityScheme()
                        .name("认证").type(SecurityScheme.Type.HTTP).description("JWT认证").scheme("Bearer").bearerFormat("JWT")));
    }

    /**
     * 分组API
     */
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry) beanFactory;
        if (properties.getDocket().isEmpty()) {
            // 没有分组
            return GroupedOpenApi.builder().group("default").displayName("测试接口")
                    .packagesToScan(properties.getBasePackage())
                    .pathsToExclude(String.join(",", properties.getExcludePath()).split(","))
                    .pathsToMatch(String.join(",", properties.getBasePath()).split(","))
                    .addOperationCustomizer(operationCustomizer())
                    .build();
        }
        for (Map.Entry<String, SwaggerProperties.DocketInfo> entry : properties.getDocket().entrySet()) {
            // 分组创建
            String groupName = entry.getKey();
            SwaggerProperties.DocketInfo docketInfo = entry.getValue();
            SwaggerProperties.Contact contact = properties.getContact();
            SwaggerProperties.Contact docketContact = docketInfo.getContact();
            String title = docketInfo.getTitle();
            String description = docketInfo.getDescription();
            String version = docketInfo.getVersion();
            String license = docketInfo.getLicense();
            String licenseUrl = docketInfo.getLicenseUrl();
            String contactName = docketContact.getName();
            String contactUrl = docketContact.getUrl();
            String contactEmail = docketContact.getEmail();
            String termsOfServiceUrl = docketInfo.getTermsOfServiceUrl();
            OpenAPI apiInfo = new OpenAPI().info(new Info().title(title.isEmpty() ? properties.getTitle() : title)
                    .description(description.isEmpty() ? properties.getDescription() : description)
                    .version(version.isEmpty() ? properties.getVersion() : version)
                    .license(license.isEmpty() ? new License().name(properties.getLicense())
                            .url(properties.getLicenseUrl()) : new License().name(license).url(licenseUrl))
                    .contact(new Contact().name(contactName.isEmpty() ? contact.getName() : contactName)
                            .url(contactUrl.isEmpty() ? contact.getUrl() : contactUrl)
                            .email(contactEmail.isEmpty() ? contact.getEmail() : contactEmail))
                    .termsOfService(termsOfServiceUrl.isEmpty() ? properties.getTermsOfServiceUrl() : termsOfServiceUrl));
            // base-path处理，当没有配置任何path的时候，解析/**
            List<String> basePath = docketInfo.getBasePath();
            if (basePath.isEmpty()) {
                basePath.add("/**");
            }
            BeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, SpecVersion.V30);
            beanDefinition.setBeanClassName(GroupedOpenApi.class.getName());
            beanDefinition.setRole(BeanDefinition.ROLE_SUPPORT);
            beanRegistry.registerBeanDefinition(BEAN_NAME + groupName, beanDefinition);
//            String basePackage = docketInfo.getBasePackage();
//            docket.groupName(groupName).host(basePackage).apiInfo(apiInfo)
//                    .globalRequestParameters(assemblyRequestParameters(swaggerProperties.getGlobalOperationParameters(),
//                            docketInfo.getGlobalOperationParameters()))
//                    .securityContexts(Collections.singletonList(swaggerAuthorizationConfiguration.securityContext()))
//                    .securitySchemes(swaggerAuthorizationConfiguration.getSecuritySchemes()).select()
//                    .apis(RequestHandlerSelectors.basePackage(basePackage))
//                    .paths(paths(basePath, docketInfo.getExcludePath())).build();
        }
        return null;
    }

    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            operation.addSecurityItem(new SecurityRequirement().addList("Authorization"));
            return operation;
        };
    }
}
