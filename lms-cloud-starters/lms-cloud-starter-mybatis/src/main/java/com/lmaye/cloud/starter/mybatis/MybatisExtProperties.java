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
@ConfigurationProperties("mybatis-plus.extend")
public class MybatisExtProperties {
    /**
     * 非对称加密算法(二选一)
     */
    private Symmetric symmetric;

    /**
     * 对称加密算法(二选一)
     */
    private Asymmetric asymmetric;

    /**
     * 非对称加密算法
     */
    @Data
    @NoArgsConstructor
    public static class Symmetric {
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
     * 对称加密算法
     */
    @Data
    @NoArgsConstructor
    public static class Asymmetric {
        /**
         * 公钥
         */
        private String publicKey;

        /**
         * 私钥
         */
        private String privateKey;
    }
}
