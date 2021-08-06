package com.lmaye.cloud.starter.logs.annotation;

import com.lmaye.cloud.starter.logs.constant.LogConstant;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * -- User Log
 *
 * @author lmay.Zhou
 * @date 2021/7/26 12:24
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@Inherited
@Documented
public @interface UserLog {
    /**
     * 索引名称
     *
     * @return String
     */
    String indexName() default LogConstant.USER_LOG_INDEX;

    /**
     * 应用ID
     *
     * @return String
     */
    String appId() default "";

    /**
     * 操作类型
     * - 0.退出; 1.登录;
     *
     * @return int
     */
    int operateType() default 1;

    /**
     * 描述
     *
     * @return String
     */
    String desc() default "";
}
