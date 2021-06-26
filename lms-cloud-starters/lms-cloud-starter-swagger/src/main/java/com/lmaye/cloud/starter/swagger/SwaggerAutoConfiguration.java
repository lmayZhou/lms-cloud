package com.lmaye.cloud.starter.swagger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * -- Swagger Auto Configuration
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/7/5 10:28 星期日
 */
@EnableOpenApi
@ConditionalOnProperty(value = "enabled", prefix = "swagger", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration implements BeanFactoryAware {
    /**
     * Bean Factory
     */
    private BeanFactory beanFactory;

    @Bean
    public UiConfiguration uiConfiguration(SwaggerProperties swaggerProperties) {
        SwaggerProperties.UiConfig config = swaggerProperties.getUiConfig();
        return UiConfigurationBuilder.builder()
                .deepLinking(config.getDeepLinking())
                .defaultModelExpandDepth(config.getDefaultModelExpandDepth())
                .defaultModelRendering(config.getDefaultModelRendering())
                .defaultModelsExpandDepth(config.getDefaultModelsExpandDepth())
                .displayOperationId(config.getDisplayOperationId())
                .displayRequestDuration(config.getDisplayRequestDuration())
                .docExpansion(config.getDocExpansion())
                .maxDisplayedTags(config.getMaxDisplayedTags())
                .operationsSorter(config.getOperationsSorter())
                .showExtensions(config.getShowExtensions())
                .tagsSorter(config.getTagsSorter())
                .validatorUrl(config.getValidatorUrl()).build();
    }

    /**
     * swagger3 动态产生Docket无效
     * TODO registerSingleton("defaultDocket", docket); 动态注册Bean存在问题
     *
     * @param swaggerProperties SwaggerProperties
     * @return List<Docket>
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UiConfiguration.class)
    public List<Docket> createRestApi(SwaggerProperties swaggerProperties) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> docketList = new LinkedList<>();
        // 没有分组
        if (swaggerProperties.getDocket().size() == 0) {
            SwaggerProperties.Contact contact = swaggerProperties.getContact();
            ApiInfo apiInfo = new ApiInfoBuilder().title(swaggerProperties.getTitle()).description(swaggerProperties.getDescription())
                    .version(swaggerProperties.getVersion()).license(swaggerProperties.getLicense()).licenseUrl(swaggerProperties.getLicenseUrl())
                    .contact(new Contact(contact.getName(), contact.getUrl(), contact.getEmail()))
                    .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl()).build();
            // base-path处理, 当没有配置任何path的时候，解析/**
            List<String> basePath = swaggerProperties.getBasePath();
            if (CollectionUtils.isEmpty(basePath)) {
                basePath.add("/**");
            }
            Docket docketForBuilder = new Docket(DocumentationType.OAS_30).host(swaggerProperties.getHost())
                    .apiInfo(apiInfo).securityContexts(Collections.singletonList(securityContext(swaggerProperties)))
                    .globalRequestParameters(buildGlobalOperationParameters(swaggerProperties.getGlobalOperationParameters()));
            if ("BasicAuth".equalsIgnoreCase(swaggerProperties.getAuthorization().getType())) {
                docketForBuilder.securitySchemes(Collections.singletonList(basicAuth(swaggerProperties)));
            } else if (!"None".equalsIgnoreCase(swaggerProperties.getAuthorization().getType())) {
                docketForBuilder.securitySchemes(Collections.singletonList(apiKey(swaggerProperties)));
            }
            // 全局响应消息
            if (!swaggerProperties.getApplyDefaultResponseMessages()) {
                buildGlobalResponse(swaggerProperties, docketForBuilder);
            }
            ApiSelectorBuilder selectorBuilder = docketForBuilder.select();
            basePath.forEach(path -> selectorBuilder.paths(PathSelectors.ant(path)));
            // exclude-path处理
            swaggerProperties.getExcludePath().forEach(path -> selectorBuilder.paths(PathSelectors.ant(path).negate()));
            Docket docket = docketForBuilder.select().apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage())).build();
            /* ignoredParameterTypes **/
            Class<?>[] array = new Class[swaggerProperties.getIgnoredParameterTypes().size()];
            Class<?>[] ignoredParameterTypes = swaggerProperties.getIgnoredParameterTypes().toArray(array);
            docket.ignoredParameterTypes(ignoredParameterTypes);
            configurableBeanFactory.registerSingleton("defaultDocket", docket);
            docketList.add(docket);
            return docketList;
        }
        // 分组创建
        for (String groupName : swaggerProperties.getDocket().keySet()) {
            SwaggerProperties.DocketInfo docketInfo = swaggerProperties.getDocket().get(groupName);
            SwaggerProperties.Contact dContact = docketInfo.getContact();
            SwaggerProperties.Contact sContact = swaggerProperties.getContact();
            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title(docketInfo.getTitle().isEmpty() ? swaggerProperties.getTitle() : docketInfo.getTitle())
                    .description(docketInfo.getDescription().isEmpty() ? swaggerProperties.getDescription() : docketInfo.getDescription())
                    .version(docketInfo.getVersion().isEmpty() ? swaggerProperties.getVersion() : docketInfo.getVersion())
                    .license(docketInfo.getLicense().isEmpty() ? swaggerProperties.getLicense() : docketInfo.getLicense())
                    .licenseUrl(docketInfo.getLicenseUrl().isEmpty() ? swaggerProperties.getLicenseUrl() : docketInfo.getLicenseUrl())
                    .contact(new Contact(dContact.getName().isEmpty() ? sContact.getName() : dContact.getName(),
                            dContact.getUrl().isEmpty() ? sContact.getUrl() : dContact.getUrl(),
                            dContact.getEmail().isEmpty() ? sContact.getEmail() : dContact.getEmail()))
                    .termsOfServiceUrl(docketInfo.getTermsOfServiceUrl().isEmpty() ? swaggerProperties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
                    .build();
            // base-path处理, 当没有配置任何path的时候，解析/**
            List<String> basePath = docketInfo.getBasePath();
            if (CollectionUtils.isEmpty(basePath)) {
                basePath.add("/**");
            }
            Docket docketForBuilder = new Docket(DocumentationType.OAS_30)
                    .host(swaggerProperties.getHost()).apiInfo(apiInfo)
                    .securityContexts(Collections.singletonList(securityContext(swaggerProperties)))
                    .globalRequestParameters(assemblyGlobalOperationParameters(swaggerProperties.getGlobalOperationParameters(),
                            docketInfo.getGlobalOperationParameters()));
            if ("BasicAuth".equalsIgnoreCase(swaggerProperties.getAuthorization().getType())) {
                docketForBuilder.securitySchemes(Collections.singletonList(basicAuth(swaggerProperties)));
            } else if (!"None".equalsIgnoreCase(swaggerProperties.getAuthorization().getType())) {
                docketForBuilder.securitySchemes(Collections.singletonList(apiKey(swaggerProperties)));
            }
            // 全局响应消息
            if (!swaggerProperties.getApplyDefaultResponseMessages()) {
                buildGlobalResponse(swaggerProperties, docketForBuilder);
            }
            ApiSelectorBuilder selectorBuilder = docketForBuilder.groupName(groupName).select();
            basePath.forEach(path -> selectorBuilder.paths(PathSelectors.ant(path)));
            // exclude-path处理
            docketInfo.getExcludePath().forEach(path -> selectorBuilder.paths(PathSelectors.ant(path).negate()));
            Docket docket = selectorBuilder.apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()))
                    .build();
            /* ignoredParameterTypes **/
            Class<?>[] array = new Class[docketInfo.getIgnoredParameterTypes().size()];
            Class<?>[] ignoredParameterTypes = docketInfo.getIgnoredParameterTypes().toArray(array);
            docket.ignoredParameterTypes(ignoredParameterTypes);

            configurableBeanFactory.registerSingleton(groupName, docket);
            docketList.add(docket);
        }
        return docketList;
    }

    /**
     * 配置基于 ApiKey 的鉴权对象
     *
     * @return ApiKey
     */
    private ApiKey apiKey(SwaggerProperties swaggerProperties) {
        return new ApiKey(swaggerProperties.getAuthorization().getName(),
                swaggerProperties.getAuthorization().getKeyName(), ApiKeyVehicle.HEADER.getValue());
    }

    /**
     * 配置基于 BasicAuth 的鉴权对象
     *
     * @return BasicAuth
     */
    private BasicAuth basicAuth(SwaggerProperties swaggerProperties) {
        return new BasicAuth(swaggerProperties.getAuthorization().getName());
    }

    /**
     * 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配；默认 ^.*$ 匹配所有URL
     * 其中 securityReferences 为配置启用的鉴权策略
     *
     * @return SecurityContext
     */
    private SecurityContext securityContext(SwaggerProperties swaggerProperties) {
        return SecurityContext.builder().securityReferences(defaultAuth(swaggerProperties))
                .operationSelector(it -> PathSelectors.regex(swaggerProperties.getAuthorization().getAuthRegex())
                        .test(it.requestMappingPattern())).build();
    }

    /**
     * 配置默认的全局鉴权策略；其中返回的 SecurityReference 中，reference 即为ApiKey对象里面的name，保持一致才能开启全局鉴权
     *
     * @return List<SecurityReference>
     */
    private List<SecurityReference> defaultAuth(SwaggerProperties swaggerProperties) {
        return Collections.singletonList(SecurityReference.builder().reference(
                swaggerProperties.getAuthorization().getName()).scopes(new AuthorizationScope[]{
                new AuthorizationScope("global", "accessEverything")}).build());
    }

    /**
     * 构建全局参数
     *
     * @param globalOperationParameters List<SwaggerProperties.GlobalOperationParameter>
     * @return List<RequestParameter>
     */
    private List<RequestParameter> buildGlobalOperationParameters(
            List<SwaggerProperties.GlobalOperationParameter> globalOperationParameters) {
        if (Objects.isNull(globalOperationParameters)) {
            return new ArrayList<>();
        }
        return globalOperationParameters.stream().map(it -> new RequestParameterBuilder()
                .name(it.getName()).description(it.getDescription()).in(it.getParameterType())
                .query(q -> q.model(m -> ScalarType.from(it.getModelType(),
                        it.getModelFormat()).ifPresent(m::scalarModel)
                )).required(Boolean.parseBoolean(it.getRequired())).build()).collect(Collectors.toList());
    }

    /**
     * 局部参数按照name覆盖局部参数
     *
     * @param globalOperationParameters List<SwaggerProperties.GlobalOperationParameter>
     * @param docketOperationParameters List<SwaggerProperties.GlobalOperationParameter>
     * @return List<RequestParameter>
     */
    private List<RequestParameter> assemblyGlobalOperationParameters(
            List<SwaggerProperties.GlobalOperationParameter> globalOperationParameters,
            List<SwaggerProperties.GlobalOperationParameter> docketOperationParameters) {
        if (Objects.isNull(docketOperationParameters) || docketOperationParameters.isEmpty()) {
            return buildGlobalOperationParameters(globalOperationParameters);
        }
        Set<String> docketNames = docketOperationParameters.stream()
                .map(SwaggerProperties.GlobalOperationParameter::getName).collect(Collectors.toSet());
        List<SwaggerProperties.GlobalOperationParameter> resultOperationParameters = new ArrayList<>();
        if (Objects.nonNull(globalOperationParameters)) {
            for (SwaggerProperties.GlobalOperationParameter parameter : globalOperationParameters) {
                if (!docketNames.contains(parameter.getName())) {
                    resultOperationParameters.add(parameter);
                }
            }
        }
        resultOperationParameters.addAll(docketOperationParameters);
        return buildGlobalOperationParameters(resultOperationParameters);
    }

    /**
     * 设置全局响应消息
     *
     * @param swaggerProperties swaggerProperties 支持 POST,GET,PUT,PATCH,DELETE,HEAD,OPTIONS,TRACE
     * @param docketForBuilder  swagger docket builder
     */
    private void buildGlobalResponse(SwaggerProperties swaggerProperties, Docket docketForBuilder) {
        SwaggerProperties.GlobalResponseMessage globalResponseMessages = swaggerProperties.getGlobalResponseMessage();
        // POST,GET,PUT,PATCH,DELETE,HEAD,OPTIONS,TRACE 响应消息体
        List<Response> postResponses = getResponseList(globalResponseMessages.getPost());
        List<Response> getResponses = getResponseList(globalResponseMessages.getGet());
        List<Response> putResponses = getResponseList(globalResponseMessages.getPut());
        List<Response> patchResponses = getResponseList(globalResponseMessages.getPatch());
        List<Response> deleteResponses = getResponseList(globalResponseMessages.getDelete());
        List<Response> headResponses = getResponseList(globalResponseMessages.getHead());
        List<Response> optionsResponses = getResponseList(globalResponseMessages.getOptions());
        List<Response> trackResponses = getResponseList(globalResponseMessages.getTrace());
        docketForBuilder.useDefaultResponseMessages(swaggerProperties.getApplyDefaultResponseMessages())
                .globalResponses(HttpMethod.POST, postResponses)
                .globalResponses(HttpMethod.GET, getResponses)
                .globalResponses(HttpMethod.PUT, putResponses)
                .globalResponses(HttpMethod.PATCH, patchResponses)
                .globalResponses(HttpMethod.DELETE, deleteResponses)
                .globalResponses(HttpMethod.HEAD, headResponses)
                .globalResponses(HttpMethod.OPTIONS, optionsResponses)
                .globalResponses(HttpMethod.TRACE, trackResponses);
    }

    /**
     * 获取返回消息体列表
     *
     * @param globalResponseMessageBodyList 全局Code消息返回集合
     * @return List<Response>
     */
    private List<Response> getResponseList(List<SwaggerProperties.GlobalResponseMessageBody> globalResponseMessageBodyList) {
        return globalResponseMessageBodyList.stream().map(it -> new ResponseBuilder().code(it.getCode())
                .description(it.getMessage()).build()).collect(Collectors.toList());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
