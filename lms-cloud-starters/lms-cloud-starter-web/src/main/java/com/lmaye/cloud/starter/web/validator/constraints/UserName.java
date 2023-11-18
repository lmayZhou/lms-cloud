package com.lmaye.cloud.starter.web.validator.constraints;

import com.lmaye.cloud.starter.web.validator.UserNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * -- 用户名校验注解
 *
 * @author lmay.Zhou
 * @date 2020-12-01 15:23:22
 * @email lmay@lmaye.com
 */
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UserNameValidator.class)
public @interface UserName {

    String message() default "{username.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        UserName[] value();
    }
}
