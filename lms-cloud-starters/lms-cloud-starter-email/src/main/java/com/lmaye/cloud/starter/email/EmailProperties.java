package com.lmaye.cloud.starter.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * -- Email Properties
 *
 * @author Lmay Zhou
 * @date 2021/4/15 11:44
 * @email lmay@lmaye.com
 */
@Data
@ConfigurationProperties("email")
public class EmailProperties {
    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * Host
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 账户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 协议
     */
    private String protocol = "smtp";

    /**
     * 默认编码
     */
    private String defaultEncoding;

    /**
     * 多账户配置
     */
    private List<EmailProperties> configs;
}
