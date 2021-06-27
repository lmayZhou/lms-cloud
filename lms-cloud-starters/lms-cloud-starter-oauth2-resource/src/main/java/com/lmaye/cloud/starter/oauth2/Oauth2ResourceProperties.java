package com.lmaye.cloud.starter.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * -- Resource Server Properties
 *
 * @author lmay.Zhou
 * @date 2021/5/31 09:42
 * @qq 379839355
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@ConfigurationProperties("security.oauth2.resource")
public class Oauth2ResourceProperties {
    /**
     * 放行URI列表
     */
    private List<String> ignoreUris;
}
