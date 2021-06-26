package com.lmaye.cloud.starter.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * -- Web Properties
 *
 * @author lmay.Zhou
 * @date 2021/5/28 22:54
 * @qq 379839355
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@ConfigurationProperties("web")
public class WebProperties {
    /**
     * 国际化配置
     */
    private I18n i18n = new I18n();

    /**
     * 国际化配置
     */
    @Data
    @NoArgsConstructor
    public static class I18n {
        /**
         * 是否启用
         */
        private Boolean enabled = false;

        /**
         * local自定义名称
         * - eg: 请求路径上的参数名称，?locale=en-US
         */
        private String localeName;

        /**
         * local分隔符
         * - 与 localeName 一起使用
         */
        private String localeDelimiter;
    }
}
