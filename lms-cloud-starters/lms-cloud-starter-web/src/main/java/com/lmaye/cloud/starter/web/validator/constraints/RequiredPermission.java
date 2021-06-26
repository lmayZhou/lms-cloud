package com.lmaye.cloud.starter.web.validator.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 权限注解
 *
 * @author lmay.Zhou
 * @date 2020-12-01 15:23:22
 * @email lmay@lmaye.com
 */
@Target({TYPE, METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Inherited
@Documented
public @interface RequiredPermission {
    String value() default "";
}
