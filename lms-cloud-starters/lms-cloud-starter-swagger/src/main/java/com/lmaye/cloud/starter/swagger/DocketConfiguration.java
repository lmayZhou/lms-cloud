package com.lmaye.cloud.starter.swagger;

import com.google.common.base.Predicates;
import com.lmaye.cloud.starter.swagger.properties.SwaggerProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

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
    private SwaggerProperties swaggerProperties;

    /**
     * Swagger Authorization Configuration
     */
    @Autowired
    private SwaggerAuthorizationConfiguration swaggerAuthorizationConfiguration;

    /**
     * bean name
     */
    private static final String BEAN_NAME = "spring-boot-starter-swagger-";

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Create the corresponding configuration for DocumentationPluginRegistry
     */
    @Bean
    public void createSpringFoxRestApi() {
        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry) beanFactory;
        // 没有分组
        if (swaggerProperties.getDocket().isEmpty()) {
            String beanName = BEAN_NAME + "default";
            BeanDefinition beanDefinition4Group = new GenericBeanDefinition();
            beanDefinition4Group.getConstructorArgumentValues().addIndexedArgumentValue(0, DocumentationType.OAS_30);
            beanDefinition4Group.setBeanClassName(Docket.class.getName());
            beanDefinition4Group.setRole(BeanDefinition.ROLE_SUPPORT);
            beanRegistry.registerBeanDefinition(beanName, beanDefinition4Group);
            Docket docket4Group = (Docket) beanFactory.getBean(beanName);
            ApiInfo apiInfo = apiInfo(swaggerProperties);
            docket4Group.host(swaggerProperties.getHost()).apiInfo(apiInfo)
                    .globalRequestParameters(
                            assemblyRequestParameters(swaggerProperties.getGlobalOperationParameters(), new ArrayList<>()))
                    .securityContexts(Collections.singletonList(swaggerAuthorizationConfiguration.securityContext()))
                    .securitySchemes(swaggerAuthorizationConfiguration.getSecuritySchemes()).select()
                    .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                    .paths(paths(swaggerProperties.getBasePath(), swaggerProperties.getExcludePath())).build();
            return;
        }
        // 分组创建
        for (Map.Entry<String, SwaggerProperties.DocketInfo> entry : swaggerProperties.getDocket().entrySet()) {
            String groupName = entry.getKey();
            SwaggerProperties.DocketInfo docketInfo = entry.getValue();
            String beanName = BEAN_NAME + groupName;
            SwaggerProperties.Contact contact = swaggerProperties.getContact();
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
            ApiInfo apiInfo = new ApiInfoBuilder().title(title.isEmpty() ? swaggerProperties.getTitle() : title)
                    .description(description.isEmpty() ? swaggerProperties.getDescription() : description)
                    .version(version.isEmpty() ? swaggerProperties.getVersion() : version)
                    .license(license.isEmpty() ? swaggerProperties.getLicense() : license)
                    .licenseUrl(licenseUrl.isEmpty() ? swaggerProperties.getLicenseUrl() : licenseUrl)
                    .contact(new Contact(contactName.isEmpty() ? contact.getName() : contactName,
                            contactUrl.isEmpty() ? contact.getUrl() : contactUrl,
                            contactEmail.isEmpty() ? contact.getEmail() : contactEmail))
                    .termsOfServiceUrl(termsOfServiceUrl.isEmpty() ? swaggerProperties.getTermsOfServiceUrl() : termsOfServiceUrl)
                    .build();
            // base-path处理，当没有配置任何path的时候，解析/**
            List<String> basePath = docketInfo.getBasePath();
            if (basePath.isEmpty()) {
                basePath.add("/**");
            }
            BeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, DocumentationType.OAS_30);
            beanDefinition.setBeanClassName(Docket.class.getName());
            beanDefinition.setRole(BeanDefinition.ROLE_SUPPORT);
            beanRegistry.registerBeanDefinition(beanName, beanDefinition);
            Docket docket = (Docket) beanFactory.getBean(beanName);
            String basePackage = docketInfo.getBasePackage();
            docket.groupName(groupName).host(basePackage).apiInfo(apiInfo)
                    .globalRequestParameters(assemblyRequestParameters(swaggerProperties.getGlobalOperationParameters(),
                            docketInfo.getGlobalOperationParameters()))
                    .securityContexts(Collections.singletonList(swaggerAuthorizationConfiguration.securityContext()))
                    .securitySchemes(swaggerAuthorizationConfiguration.getSecuritySchemes()).select()
                    .apis(RequestHandlerSelectors.basePackage(basePackage))
                    .paths(paths(basePath, docketInfo.getExcludePath())).build();
        }
    }

    /**
     * 全局请求参数
     *
     * @param properties {@link SwaggerProperties}
     * @return RequestParameter {@link RequestParameter}
     */
    private List<RequestParameter> getRequestParameters(List<SwaggerProperties.GlobalOperationParameter> properties) {
        return properties.stream()
                .map(param -> new RequestParameterBuilder().name(param.getName()).description(param.getDescription())
                        .in(ParameterType.from(param.getParameterType())).required(param.getRequired())
                        .query(q -> q.defaultValue(param.getType()))
                        .query(q -> q.model(m -> m.scalarModel(!ScalarType.from(param.getType(), param.getFormat()).isPresent()
                                ? ScalarType.STRING : ScalarType.from(param.getType(), param.getFormat()).get())))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 局部参数按照name覆盖局部参数
     *
     * @param globalRequestParameters 全局配置
     * @param groupRequestParameters  Group 的配置
     * @return List<RequestParameter>
     */
    private List<RequestParameter> assemblyRequestParameters(
            List<SwaggerProperties.GlobalOperationParameter> globalRequestParameters,
            List<SwaggerProperties.GlobalOperationParameter> groupRequestParameters) {
        if (Objects.isNull(groupRequestParameters) || groupRequestParameters.isEmpty()) {
            return getRequestParameters(globalRequestParameters);
        }
        Set<String> paramNames = groupRequestParameters.stream().map(SwaggerProperties.GlobalOperationParameter::getName)
                .collect(Collectors.toSet());
        List<SwaggerProperties.GlobalOperationParameter> requestParameters = newArrayList();
        if (Objects.nonNull(globalRequestParameters)) {
            for (SwaggerProperties.GlobalOperationParameter parameter : globalRequestParameters) {
                if (!paramNames.contains(parameter.getName())) {
                    requestParameters.add(parameter);
                }
            }
        }
        requestParameters.addAll(groupRequestParameters);
        return getRequestParameters(requestParameters);
    }

    /**
     * API接口路径选择
     *
     * @param basePath    basePath
     * @param excludePath excludePath
     * @return Predicate
     */
    private Predicate paths(List<String> basePath, List<String> excludePath) {
        // base-path处理，当没有配置任何path的时候，解析/**
        if (basePath.isEmpty()) {
            basePath.add("/**");
        }
        List<com.google.common.base.Predicate<String>> basePathList = new ArrayList<>();
        for (String path : basePath) {
            basePathList.add(PathSelectors.ant(path));
        }
        // exclude-path处理
        List<com.google.common.base.Predicate<String>> excludePathList = new ArrayList<>();
        for (String path : excludePath) {
            excludePathList.add(PathSelectors.ant(path));
        }
        return Predicates.and(Predicates.not(Predicates.or(excludePathList)), Predicates.or(basePathList));
    }

    /**
     * API文档基本信息
     *
     * @param properties SwaggerProperties
     * @return ApiInfo
     */
    private ApiInfo apiInfo(SwaggerProperties properties) {
        return new ApiInfoBuilder().title(properties.getTitle()).description(properties.getDescription())
                .version(properties.getVersion()).license(properties.getLicense())
                .licenseUrl(properties.getLicenseUrl())
                .contact(new Contact(properties.getContact().getName(), properties.getContact().getUrl(),
                        properties.getContact().getEmail()))
                .termsOfServiceUrl(properties.getTermsOfServiceUrl()).build();
    }
}
