package com.lmaye.cloud.starter.canal.patterns;

import org.springframework.stereotype.Component;

/**
 * -- Float Type
 *
 * @author Lmay Zhou
 * @date 2021/4/2 10:14
 * @email lmay@lmaye.com
 */
@Component
public class FloatType implements IType {
    /**
     * 获取类型名称
     *
     * @return String
     */
    @Override
    public String getTypeName() {
        return Float.class.getTypeName();
    }

    /**
     * 类型转换
     *
     * @param value 值
     * @return Float
     */
    @Override
    public Float convertType(String value) {
        return Float.parseFloat(value);
    }
}
