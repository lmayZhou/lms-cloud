package com.lmaye.cloud.starter.web.annotation;

import com.lmaye.cloud.starter.web.config.FeignEncoderConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * -- Enable Feign Encoder
 *
 * @author lmay.Zhou
 * @date 2021/6/1 12:24
 * @qq 379839355
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({FeignEncoderConfig.class})
public @interface EnableFeignEncoder {
}
