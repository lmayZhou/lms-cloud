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
 * -- Service Log
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
public @interface ServiceLog {
    /**
     * 索引名称
     *
     * @return String
     */
    String indexName() default LogConstant.SERVICE_LOG_INDEX;

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
     * 业务类型
     */
    String businessType() default "";

    /**
     * 操作类型
     */
    String operationType() default "";

    /**
     * 标题
     *
     * @return String
     */
    String title() default "";

    /**
     * 描述
     *
     * @return String
     */
    String desc() default "";
}
