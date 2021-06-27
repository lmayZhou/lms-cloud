package com.lmaye.cloud.starter.swagger;

import com.lmaye.cloud.starter.swagger.constants.AuthType;
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
 * <pre>
 *     禁用方法（推荐使用 2）
 *     1：使用注解@Profile({"dev","test"}), 表示在开发或测试环境开启，而在生产关闭。
 *
 *     2：使用注解@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
 *      然后在测试配置或者开发配置中添加swagger.enable=true即可开启，生产环境不填则默认关闭Swagger.
 * </pre>
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
     *
     * TODO registerSingleton("defaultDocket", docket); 动态注册Bean存在问题
     *
     * @param properties SwaggerProperties
     * @return List<Docket>
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UiConfiguration.class)
    public List<Docket> createRestApi(SwaggerProperties properties) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> docketList = new LinkedList<>();
        Map<String, SwaggerProperties.DocketInfo> docketInfoMap = properties.getDocket();
        if (docketInfoMap.isEmpty()) {
            // 没有分组
            List<String> basePath = properties.getBasePath();
            if (CollectionUtils.isEmpty(basePath)) {
                // base-path处理, 当没有配置任何path的时候，解析
                basePath.add("/**");
            }
            ApiSelectorBuilder builder = new Docket(DocumentationType.OAS_30).host(properties.getHost())
                    .apiInfo(apiInfo(properties)).securityContexts(Collections.singletonList(securityContext(properties)))
                    .globalRequestParameters(globalRequestParameters(properties.getGlobalRequestParameters()))
                    .select().apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()));
            basePath.forEach(path -> builder.paths(PathSelectors.ant(path)));
            // exclude-path处理
            properties.getExcludePath().forEach(path -> builder.paths(PathSelectors.ant(path).negate()));
            Docket docket = builder.build().securitySchemes(Collections.singletonList(securityScheme(properties)));
            // 全局响应消息
            if (!properties.getApplyDefaultResponse()) {
                buildGlobalResponse(properties, docket);
            }
            // ignoredParameterTypes
            Class<?>[] array = new Class[properties.getIgnoredParameterTypes().size()];
            Class<?>[] ignoredParameterTypes = properties.getIgnoredParameterTypes().toArray(array);
            docket.ignoredParameterTypes(ignoredParameterTypes);
            configurableBeanFactory.registerSingleton("defaultDocket", docket);
            docketList.add(docket);
            return docketList;
        }
        // 分组创建
        for (String groupName : docketInfoMap.keySet()) {
            SwaggerProperties.DocketInfo docketInfo = docketInfoMap.get(groupName);
            SwaggerProperties.Contact dContact = docketInfo.getContact();
            SwaggerProperties.Contact sContact = properties.getContact();
            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title(docketInfo.getTitle().isEmpty() ? properties.getTitle() : docketInfo.getTitle())
                    .description(docketInfo.getDescription().isEmpty() ? properties.getDescription() : docketInfo.getDescription())
                    .version(docketInfo.getVersion().isEmpty() ? properties.getVersion() : docketInfo.getVersion())
                    .license(docketInfo.getLicense().isEmpty() ? properties.getLicense() : docketInfo.getLicense())
                    .licenseUrl(docketInfo.getLicenseUrl().isEmpty() ? properties.getLicenseUrl() : docketInfo.getLicenseUrl())
                    .contact(new Contact(dContact.getName().isEmpty() ? sContact.getName() : dContact.getName(),
                            dContact.getUrl().isEmpty() ? sContact.getUrl() : dContact.getUrl(),
                            dContact.getEmail().isEmpty() ? sContact.getEmail() : dContact.getEmail()))
                    .termsOfServiceUrl(docketInfo.getTermsOfServiceUrl().isEmpty() ? properties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
                    .build();
            List<String> basePath = docketInfo.getBasePath();
            if (CollectionUtils.isEmpty(basePath)) {
                // base-path处理, 当没有配置任何path的时候，解析
                basePath.add("/**");
            }
            ApiSelectorBuilder builder = new Docket(DocumentationType.OAS_30).host(properties.getHost()).apiInfo(apiInfo)
                    .securityContexts(Collections.singletonList(securityContext(properties)))
                    .globalRequestParameters(assemblyGlobalRequestParameters(properties.getGlobalRequestParameters(),
                            docketInfo.getGlobalRequestParameters())).groupName(groupName).select()
                    .apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()));
            basePath.forEach(path -> builder.paths(PathSelectors.ant(path)));
            // exclude-path处理
            docketInfo.getExcludePath().forEach(path -> builder.paths(PathSelectors.ant(path).negate()));
            Docket docket = builder.build().securitySchemes(Collections.singletonList(securityScheme(properties)));
            // 全局响应消息
            if (!properties.getApplyDefaultResponse()) {
                buildGlobalResponse(properties, docket);
            }
            // ignoredParameterTypes
            Class<?>[] array = new Class[docketInfo.getIgnoredParameterTypes().size()];
            Class<?>[] ignoredParameterTypes = docketInfo.getIgnoredParameterTypes().toArray(array);
            docket.ignoredParameterTypes(ignoredParameterTypes);
            configurableBeanFactory.registerSingleton(groupName, docket);
            docketList.add(docket);
        }
        return docketList;
    }

    /**
     * 获取 ApiInfo
     *
     * @param properties SwaggerProperties
     * @return ApiInfo
     */
    private ApiInfo apiInfo(SwaggerProperties properties) {
        SwaggerProperties.Contact contact = properties.getContact();
        return new ApiInfoBuilder().title(properties.getTitle()).description(properties.getDescription())
                .version(properties.getVersion()).license(properties.getLicense())
                .licenseUrl(properties.getLicenseUrl())
                .contact(new Contact(contact.getName(), contact.getUrl(), contact.getEmail()))
                .termsOfServiceUrl(properties.getTermsOfServiceUrl()).build();
    }

    /**
     * 配置基于 ApiKey 的鉴权对象
     *
     * @return ApiKey
     */
    private SecurityScheme securityScheme(SwaggerProperties properties) {
        if (AuthType.BasicAuth.name().equalsIgnoreCase(properties.getAuthorization().getType())) {
            // 配置基于 BasicAuth 的鉴权对象
            return new BasicAuth(properties.getAuthorization().getName());
        }
        // 配置基于 ApiKey 的鉴权对象
        return new ApiKey(properties.getAuthorization().getName(),
                properties.getAuthorization().getKeyName(), ApiKeyVehicle.HEADER.getValue());
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
     * @param parameters List<SwaggerProperties.GlobalOperationParameter>
     * @return List<RequestParameter>
     */
    private List<RequestParameter> globalRequestParameters(List<SwaggerProperties.GlobalRequestParameters> parameters) {
        if (Objects.isNull(parameters)) {
            return new ArrayList<>();
        }
        return parameters.stream().map(it -> new RequestParameterBuilder().name(it.getName())
                .description(it.getDescription()).in(it.getParameterType())
                .query(q -> q.model(m -> ScalarType.from(it.getModelType(),
                        it.getModelFormat()).ifPresent(m::scalarModel)
                )).required(Boolean.parseBoolean(it.getRequired())).build()).collect(Collectors.toList());
    }

    /**
     * 局部参数按照name覆盖局部参数
     *
     * @param globalParameters List<SwaggerProperties.GlobalOperationParameter>
     * @param docketInfoParameters List<SwaggerProperties.GlobalOperationParameter>
     * @return List<RequestParameter>
     */
    private List<RequestParameter> assemblyGlobalRequestParameters(List<SwaggerProperties.GlobalRequestParameters> globalParameters,
            List<SwaggerProperties.GlobalRequestParameters> docketInfoParameters) {
        if (Objects.isNull(docketInfoParameters) || docketInfoParameters.isEmpty()) {
            return globalRequestParameters(globalParameters);
        }
        Set<String> docketNames = docketInfoParameters.stream()
                .map(SwaggerProperties.GlobalRequestParameters::getName).collect(Collectors.toSet());
        List<SwaggerProperties.GlobalRequestParameters> resultParameters = new ArrayList<>();
        if (Objects.nonNull(globalParameters)) {
            for (SwaggerProperties.GlobalRequestParameters parameter : globalParameters) {
                if (!docketNames.contains(parameter.getName())) {
                    resultParameters.add(parameter);
                }
            }
        }
        resultParameters.addAll(docketInfoParameters);
        return globalRequestParameters(resultParameters);
    }

    /**
     * 设置全局响应消息
     * - 支持 POST,GET,PUT,PATCH,DELETE,HEAD,OPTIONS,TRACE
     *
     * @param properties SwaggerProperties
     * @param docket  swagger docket builder
     */
    private void buildGlobalResponse(SwaggerProperties properties, Docket docket) {
        SwaggerProperties.GlobalResponse globalResponse = properties.getGlobalResponse();
        // POST,GET,PUT,PATCH,DELETE,HEAD,OPTIONS,TRACE 响应消息体
        List<Response> postResponses = getResponseList(globalResponse.getPost());
        List<Response> getResponses = getResponseList(globalResponse.getGet());
        List<Response> putResponses = getResponseList(globalResponse.getPut());
        List<Response> patchResponses = getResponseList(globalResponse.getPatch());
        List<Response> deleteResponses = getResponseList(globalResponse.getDelete());
        List<Response> headResponses = getResponseList(globalResponse.getHead());
        List<Response> optionsResponses = getResponseList(globalResponse.getOptions());
        List<Response> trackResponses = getResponseList(globalResponse.getTrace());
        docket.useDefaultResponseMessages(properties.getApplyDefaultResponse())
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
     * @param globalResponseBodyList 全局Code消息返回集合
     * @return List<Response>
     */
    private List<Response> getResponseList(List<SwaggerProperties.GlobalResponseBody> globalResponseBodyList) {
        return globalResponseBodyList.stream().map(it -> new ResponseBuilder().code(it.getCode())
                .description(it.getMessage()).build()).collect(Collectors.toList());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
