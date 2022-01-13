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
     * 全局ID长度，默认8位
     */
    private int globalIdLen = 8;

    /**
     * 日期格式，默认6位(e.g: 220105)
     */
    private String dateFormat = "yyMMdd";
}
