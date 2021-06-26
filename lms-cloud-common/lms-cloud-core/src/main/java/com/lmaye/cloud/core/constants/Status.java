package com.lmaye.cloud.core.constants;

import lombok.Getter;

import java.util.Objects;

/**
 * -- 状态枚举
 *
 * @author lmay.Zhou
 * @date 2020-12-01 15:23:22
 * @email lmay@lmaye.com
 */
@Getter
public enum Status {
    /**
     * 枚举对象
     */
    DISABLE(0, "禁用"),
    AVAILABLE(1, "可用");

    /**
     * 枚举编码
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    Status(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取枚举对象
     *
     * @param code 枚举编码
     * @return 枚举对象
     */
    public static Status valueOf(Integer code) {
        Objects.requireNonNull(code, "The matching value cannot be empty");
        for (Status obj : values()) {
            if (code.equals(obj.getCode())) {
                return obj;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }
}
