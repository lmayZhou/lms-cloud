package com.lmaye.cloud.starter.drools;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * -- Drools Properties
 *
 * @author Lmay Zhou
 * @date 2021/10/22 11:10
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@ConfigurationProperties("drools")
public class DroolsProperties {
    /**
     * 是否启用
     **/
    private Boolean enabled;

    /**
     * 规则文件路径
     */
    private String rulesPath;
}
