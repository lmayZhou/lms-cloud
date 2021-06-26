package com.lmaye.cloud.starter.web.validator;

import com.lmaye.cloud.starter.web.validator.constraints.Ip;
import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * -- IP校验
 *
 * @author lmay.Zhou
 * @date 2020-12-01 15:23:22
 * @email lmay@lmaye.com
 */
public class IpValidator implements ConstraintValidator<Ip, String> {

    private static final Pattern IP_PATTERN = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");

    /**
     * 校验是否为Ip地址
     *
     * @param ip IP
     * @return boolean
     */
    public static boolean isIp(String ip) {
        return IP_PATTERN.matcher(ip).matches();
    }

    @Override
    public void initialize(Ip constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Strings.isBlank(value) || isIp(value);
    }
}
