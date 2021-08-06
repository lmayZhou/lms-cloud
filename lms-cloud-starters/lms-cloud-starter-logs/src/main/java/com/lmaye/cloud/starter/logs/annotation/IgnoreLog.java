package com.lmaye.cloud.starter.logs.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * -- Ignore Log
 * - 忽略日志
 *
 * @author lmay.Zhou
 * @date 2021/7/27 09:58
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@Inherited
@Documented
public @interface IgnoreLog {
}
