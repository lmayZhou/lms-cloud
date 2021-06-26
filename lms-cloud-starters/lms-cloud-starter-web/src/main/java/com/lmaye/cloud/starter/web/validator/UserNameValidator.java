package com.lmaye.cloud.starter.web.validator;

import com.lmaye.cloud.starter.web.validator.constraints.UserName;
import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * -- 用户名校验
 *
 * @author lmay.Zhou
 * @date 2020-12-01 15:23:22
 * @email lmay@lmaye.com
 */
public class UserNameValidator implements ConstraintValidator<UserName, String> {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("(?!_)\\w{2,32}");

    /**
     * 校验用户名
     *
     * @param username 用户名
     * @return 通过返回true，不通过返回false
     */
    public static boolean isUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    @Override
    public void initialize(UserName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Strings.isBlank(value) || isUsername(value);
    }
}
