package com.lmaye.cloud.starter.logs.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * -- 终端类型
 *
 * @author lmay.Zhou
 * @date 2021/6/23 17:15
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Getter
@AllArgsConstructor
public enum ClientType {
    /**
     * 枚举对象
     */
    UNKNOWN(0, "Unknown"),
    PC(1, "PC"),
    ANDROID(2, "Android"),
    IOS(3, "IOS");

    /**
     * 枚举编码
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    /**
     * 获取枚举对象
     *
     * @param code 枚举编码
     * @return 枚举对象
     */
    public static ClientType valueOf(Integer code) {
        Objects.requireNonNull(code, "The matching value cannot be empty");
        for (ClientType obj : values()) {
            if (code.equals(obj.getCode())) {
                return obj;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }
}
