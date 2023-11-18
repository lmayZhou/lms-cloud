package com.lmaye.cloud.starter.serialno;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * -- Serial No Properties
 *
 * @author Lmay Zhou
 * @date 2022/1/5 15:39
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@ConfigurationProperties("web.serial-no")
public class SerialNoProperties {
    /**
     * 全局ID长度，默认5位
     */
    private int globalIdLen = 5;

    /**
     * 日期格式，默认6位(e.g: 220105)
     */
    private String dateFormat = "yyMMdd";

    /**
     * 是否有序的全局ID，默认有序(true: 有序; false: 无序;)
     */
    private Boolean isOrderly = true;

    /**
     * 是否过期(当天失效)
     */
    private Boolean isExpire = true;

    /**
     * 无序时 - Redis每个缓存Key中存储序号的个数，默认1000
     */
    private int eachCacheSize = 1000;
}
