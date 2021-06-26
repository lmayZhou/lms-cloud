package com.lmaye.cloud.core.constants;

import lombok.Getter;

import java.util.Objects;

/**
 * -- 性别枚举
 *
 * @author lmay.Zhou
 * @date 2020-12-01 15:23:22
 * @email lmay@lmaye.com
 */
@Getter
public enum Sex {
    /**
     * 枚举对象
     */
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    /**
     * 枚举编码
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    Sex(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取枚举对象
     *
     * @param code 枚举编码
     * @return 枚举对象
     */
    public static Sex valueOf(Integer code) {
        Objects.requireNonNull(code, "The matching value cannot be empty");
        for (Sex obj : values()) {
            if (code.equals(obj.getCode())) {
                return obj;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }
}
