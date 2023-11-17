package com.lmaye.cloud.starter.mybatis.annotation;

import com.lmaye.cloud.starter.mybatis.constant.HandleStrategy;

import java.lang.annotation.*;

/**
 * -- FieldEncrypt
 * - 字段加/解密
 *
 * @author Lmay Zhou
 * @date 2023/11/16 18:46
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldEnDecrypt {
    /**
     * 处理策略
     *
     * @return HandleStrategy
     */
    HandleStrategy strategy() default HandleStrategy.SAVE;
}
