package com.lmaye.cloud.starter.swagger.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * -- SwaggerProperties
 *
 * @author lmay.Zhou
 * @qq 379839355
 * @email lmay@lmaye.com
 * @since 2020/7/5 10:24 星期日
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {
    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本
     */
    private String version;

    /**
     * 许可证
     */
    private String license;

    /**
     * 许可证URL
     */
    private String licenseUrl;

    /**
     * 服务条款URL
     */
    private String termsOfServiceUrl;

    /**
     * 联系方式
     */
    private Contact contact = new Contact();

    /**
     * swagger会解析的包路径
     */
    private String basePackage;

    /**
     * swagger会解析的url规则
     */
    private List<String> basePath = new ArrayList<>();

    /**
     * 在basePath基础上需要排除的url规则
     */
    private List<String> excludePath = new ArrayList<>();

    /**
     * host信息
     */
    private String host;

    /**
     * 分组文档
     */
    private Map<String, DocketInfo> docket = new LinkedHashMap<>();

    /**
     * 全局参数配置
     */
    private List<GlobalOperationParameter> globalOperationParameters;

    /**
     * 全局参数配置
     */
    @Data
    @NoArgsConstructor
    public static class GlobalOperationParameter {
        /**
         * 参数名
         */
        private String name;

        /**
         * 描述信息
         */
        private String description;

        /**
         * 指定参数类型
         */
        @Value("modelRef")
        private String type;

        /**
         * 指定参数类型
         */
        private String format;

        /**
         * 参数放在哪个地方:header,query,path,formData,cookie,form
         */
        private String parameterType;

        /**
         * 参数是否必须传
         */
        private Boolean required = false;
    }

    @Data
    @NoArgsConstructor
    public static class Contact {
        /**
         * 联系人
         */
        private String name;

        /**
         * 联系人url
         */
        private String url;

        /**
         * 联系人email
         */
        private String email;
    }

    /**
     * 分组信息
     */
    @Data
    @NoArgsConstructor
    public static class DocketInfo {
        /**
         * 标题
         */
        private String title;

        /**
         * 描述
         */
        private String description;

        /**
         * 版本
         */
        private String version;

        /**
         * 许可证
         */
        private String license;

        /**
         * 许可证URL
         */
        private String licenseUrl;

        /**
         * 服务条款URL
         */
        private String termsOfServiceUrl;

        /**
         * 联系方式
         */
        private Contact contact = new Contact();

        /**
         * swagger会解析的包路径
         */
        private String basePackage;

        /**
         * swagger会解析的url规则
         */
        private List<String> basePath = new ArrayList<>();

        /**
         * 在basePath基础上需要排除的url规则
         */
        private List<String> excludePath = new ArrayList<>();

        /**
         * 全局参数配置
         */
        private List<GlobalOperationParameter> globalOperationParameters;

        /**
         * 忽略的参数类型
         */
        private List<Class<?>> ignoredParameterTypes = new ArrayList<>();
    }
}
