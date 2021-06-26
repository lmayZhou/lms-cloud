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
     * 简单日期格式: yyyy-MM-dd HH:mm:ss
     */
    SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    /**
     * 时区: GMT-8
     */
    TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT-8:00");

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
}
