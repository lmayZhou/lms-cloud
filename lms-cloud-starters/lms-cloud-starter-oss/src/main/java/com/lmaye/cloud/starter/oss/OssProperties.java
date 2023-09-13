package com.lmaye.cloud.starter.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * -- Oss Properties
 *
 * @author Lmay Zhou
 * @date 2021/7/14 15:11
 * @email lmay@lmaye.com
 */
@Data
@ConfigurationProperties("oss")
public class OssProperties {
    /**
     * 是否开启(默认: 开启)
     */
    private Boolean enabled = true;

    /**
     * 终端
     */
    private String endpoint;

    /**
     * 秘钥ID
     */
    private String accessKeyId;

    /**
     * 秘钥
     */
    private String secretAccessKey;

    /**
     * 令牌
     */
    private String securityToken;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 访问地址
     */
    private String accessUrl;

    /**
     * 设置缓存控制标头
     * - no-cache/no-store/public/private/only-if-cached
     */
    private String cacheControl = "no-cache";
}
