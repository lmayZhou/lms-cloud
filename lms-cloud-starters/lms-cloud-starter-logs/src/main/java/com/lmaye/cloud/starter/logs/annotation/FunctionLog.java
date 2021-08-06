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
 * -- Function Log
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
public @interface FunctionLog {
    /**
     * 索引名称
     *
     * @return String
     */
    String indexName() default LogConstant.FUNCTION_LOG_INDEX;

    /**
     * 应用ID
     *
     * @return String
     */
    String appId() default "";

    /**
     * 模块ID
     *
     * @return String
     */
    String moduleId() default "";

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
}
