package com.lmaye.cloud.starter.mybatis;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * -- MybatisExtProperties
 *
 * @author Lmay Zhou
 * @date 2023/11/17 17:29
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@ConfigurationProperties("mybatis-plus.ext")
public class MybatisExtProperties {
    /**
     * AES 加密算法
     */
    private Aes aes;

    /**
     * Des 加密算法
     */
    private Des des;

    /**
     * AES 加密算法
     */
    @Data
    @NoArgsConstructor
    public static class Aes {
        /**
         * 模式
         */
        private String mode = Mode.CBC.name();

        /**
         * 补码方式
         */
        private String padding = Padding.PKCS5Padding.name();

        /**
         * 密钥，支持三种密钥长度：128、192、256位
         */
        private String key;

        /**
         * 加盐
         */
        private String iv;
    }

    /**
     * Des 加密算法
     */
    @Data
    @NoArgsConstructor
    public static class Des {
        /**
         * 模式
         */
        private String mode = Mode.CBC.name();

        /**
         * 补码方式
         */
        private String padding = Padding.PKCS5Padding.name();

        /**
         * 密钥，支持三种密钥长度：128、192、256位
         */
        private String key;

        /**
         * 加盐
         */
        private String iv;
    }
}
