package com.lmaye.cloud.core.constants;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * -- 常量
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
public interface CoreConstants {
    /**
     * 版本号(默认)
     */
    long VERSION = 1L;

    /**
     * @ 符号
     */
    String AT = "@";

    /**
     * 日期时间格式(字符串): yyyy-MM-dd
     */
    String STR_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期时间格式(字符串): HH:mm:ss
     */
    String STR_HH_MM_SS = "HH:mm:ss";

    /**
     * 日期时间格式(字符串): yyyy-MM-dd HH:mm:ss
     */
    String STR_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 开始时间范围后缀: 00:00:00.000
     */
    String TIME_START_POSTFIX = "00:00:00.000";

    /**
     * 结束时间范围后缀: 23:59:59.999
     */
    String TIME_END_POSTFIX = "23:59:59.999";

    /**
     * 简单日期格式: yyyy-MM-dd HH:mm:ss
     */
    SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    /**
     * 时区: GMT+8
     */
    TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT+8:00");

    /**
     * 日期时间格式: yyyy-MM-dd
     */
    DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 日期时间格式: HH:mm:ss
     */
    DateTimeFormatter HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 日期时间格式: yyyy-MM-dd HH:mm:ss
     */
    DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * (字段) 接口权限认证
     */
    String FIELD_AUTHORIZATION = "Authorization";

    /**
     * (字段) 客户端ID
     */
    String FIELD_CLIENT_ID = "clientId";

    /**
     * (字段) 国际化语言
     */
    String FIELD_LANGUAGE = "language";

    /**
     * (字段) 时区
     */
    String FIELD_TIME_ZONE = "timeZone";
}
