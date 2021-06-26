package com.lmaye.cloud.starter.web.validator;

import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.core.exception.CoreException;
import com.lmaye.cloud.starter.web.validator.constraints.Safe;
import org.apache.commons.lang3.StringUtils;
import org.owasp.validator.html.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.InputStream;

/**
 * -- 安全参数校验
 *
 * @author lmay.Zhou
 * @date 2020-12-01 15:23:22
 * @email lmay@lmaye.com
 */
public class SafeValidator implements ConstraintValidator<Safe, String> {
    private static final Policy POLICY;

    static {
        Resource resource = new ClassPathResource("antisamy.xml");
        try (InputStream is = resource.getInputStream()) {
            POLICY = Policy.getInstance(is);
        } catch (Exception e) {
            throw new CoreException(ResultCode.ANTISAMY_DATA_INIT_FAILED, e);
        }
    }

    @Override
    public void initialize(Safe constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isBlank(s) || isSafe(s);
    }

    /**
     * 校验是否存在不合法的脚本
     *
     * @param value 值
     * @return boolean
     */
    public boolean isSafe(String value) {
        AntiSamy antiSamy = new AntiSamy();
        try {
            final CleanResults cleanResults = antiSamy.scan(value, POLICY);
            // 判断是否存在不合法的字符
            return cleanResults.getNumberOfErrors() <= 0;
        } catch (ScanException | PolicyException e) {
            return false;
        }
    }

    /**
     * 获取安全的字符串
     *
     * @param value 值
     * @return String
     */
    public static String getSafeStr(String value) {
        AntiSamy antiSamy = new AntiSamy();
        try {
            final CleanResults cleanResults = antiSamy.scan(value, POLICY);
            // 安全的HTML输出
            return cleanResults.getCleanHTML();
        } catch (ScanException | PolicyException e) {
            return "";
        }
    }
}
