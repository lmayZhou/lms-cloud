package com.lmaye.cloud.starter.logs.annotation;

import com.lmaye.cloud.core.constants.CoreConstants;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * -- Sys Log
 *
 * @author lmay.Zhou
 * @date 2021/7/26 12:24
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Inherited
@Documented
public @interface SysLog {
    /**
     * 索引名称
     *
     * @return String
     */
    String indexName() default "sys_log";

    /**
     * 应用ID
     *
     * @return String
     */
    String appId() default "";

    /**
     * 日志类型
     */
    String logType() default "";

    /**
     * 方法名称
     */
    String functionName() default "";

    /**
     * 描述
     *
     * @return String
     */
    String desc() default "";

    /**
     * Token认证
     * - 字段名称
     *
     * @return String
     */
    String tokenAttr() default CoreConstants.FIELD_AUTHORIZATION;

    /**
     * 用户ID
     * - 字段名称
     *
     * @return String
     */
    String userIdAttr() default "id";

    /**
     * 用户名称
     * - 字段名称
     *
     * @return String
     */
    String userNameAttr() default "username";
}
