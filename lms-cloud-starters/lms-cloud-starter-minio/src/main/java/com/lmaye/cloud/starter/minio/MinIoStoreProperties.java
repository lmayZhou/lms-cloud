package com.lmaye.cloud.starter.minio;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * -- MinIO Auto Properties
 *
 * @author lmay.Zhou
 * @date 2020/10/12 15:53 星期一
 * @since Email: lmay_zlm@meten.com
 */
@Data
@ConfigurationProperties("minio.store")
public class MinIoStoreProperties {
    /**
     * 终端
     */
    private String endpoint;

    /**
     * bucket
     */
    private String bucket;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 文件最大大小
     */
    private Long partMaxSize;

    /**
     * 清除缓存
     */
    private CleanCache cleanCache = new CleanCache();

    /**
     * 清除缓存
     */
    @Data
    @NoArgsConstructor
    public static class CleanCache {
        /**
         * 是否开启(默认: 开启)
         */
        private Boolean enabled = true;

        /**
         * 文件缓存目录
         */
        private String directory = "./tmp";

        /**
         * 清除周期(单位: millisecond)
         * - 1000L * 60 * 60 * 24
         */
        private Long cleanPeriod = 86400000L;

        /**
         * 存活时间(单位: millisecond)
         * - 1000L * 60 * 60 * 24
         */
        private Long aliveDuration = 86400000L;
    }
}
