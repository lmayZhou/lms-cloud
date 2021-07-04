package com.lmaye.cloud.starter.canal.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * -- CanalProperties
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = CanalSimpleProperties.CANAL_PREFIX)
public class CanalSimpleProperties extends CanalProperties {
    private String userName;

    private String password;
}
