package com.lmaye.cloud.starter.mybatis.annotation;

import com.lmaye.cloud.starter.mybatis.constant.HandleStrategy;
import com.lmaye.cloud.starter.mybatis.constant.SensitiveType;

import java.lang.annotation.*;

/**
 * -- FieldSensitive
 * - 字段脱敏
 *
 * @author Lmay Zhou
 * @date 2023/11/16 18:43
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldSensitive {
    /**
     * 脱敏类型
     * - 默认全部
     *
     * @return SensitiveType
     */
    SensitiveType value() default SensitiveType.ALL;

    /**
     * 处理策略
     *
     * @return HandleStrategy
     */
    HandleStrategy strategy() default HandleStrategy.RESULT;
}
