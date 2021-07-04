package com.lmaye.cloud.starter.canal.patterns;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * -- Date Type
 *
 * @author Lmay Zhou
 * @date 2021/4/2 10:15
 * @email lmay@lmaye.com
 */
@Slf4j
@Component
public class DateType implements IType {
    private static final String[] PARSE_PATTERNS = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取类型名称
     *
     * @return String
     */
    @Override
    public String getTypeName() {
        return Date.class.getTypeName();
    }

    /**
     * 类型转换
     *
     * @param value 值
     * @return Integer
     */
    @Override
    public Date convertType(String value) {
        try {
            if (StringUtils.isBlank(value)) {
                return null;
            }
            return DateUtils.parseDate(value, PARSE_PATTERNS);
        } catch (ParseException e) {
            log.error("日期解析异常: ", e);
            return null;
        }
    }
}
