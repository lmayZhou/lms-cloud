package com.lmaye.cloud.starter.canal.patterns;

import org.springframework.stereotype.Component;

/**
 * -- Long 类型
 *
 * @author Lmay Zhou
 * @date 2021/4/2 9:34
 * @email lmay@lmaye.com
 */
@Component
public class LongType implements IType {
    /**
     * 获取类型名称
     *
     * @return String
     */
    @Override
    public String getTypeName() {
        return Long.class.getTypeName();
    }

    /**
     * 类型转换
     *
     * @param value 值
     * @return Integer
     */
    @Override
    public Long convertType(String value) {
        return Long.parseLong(value);
    }
}
