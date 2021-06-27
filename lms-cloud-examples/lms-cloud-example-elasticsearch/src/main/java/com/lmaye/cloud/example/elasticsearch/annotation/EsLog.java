package com.lmaye.cloud.example.elasticsearch.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * -- ES功能日志注解
 *
 * @author lmay.Zhou
 * @date 2020/12/7 15:53
 * @email lmay@lmaye.com
 */
@Target({TYPE, METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Inherited
@Documented
public @interface EsLog {
    /**
     * 索引名称
     *
     * @return String
     */
    String[] indexNames() default {};

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
     * 标题
     *
     * @return String
     */
    String title() default "";

    /**
     * 方法名称
     *
     * @return String
     */
    String functionName() default "";

    /**
     * 是否启用
     *
     * @return boolean
     */
    boolean isEnable() default true;
}
