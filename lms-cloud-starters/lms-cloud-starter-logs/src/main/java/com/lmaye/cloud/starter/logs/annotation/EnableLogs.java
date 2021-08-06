package com.lmaye.cloud.starter.logs.annotation;

import com.lmaye.cloud.starter.logs.LogAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * -- Enable Log
 *
 * @author lmay.Zhou
 * @date 2021/7/26 12:11
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ LogAutoConfiguration.class })
public @interface EnableLogs {
}
