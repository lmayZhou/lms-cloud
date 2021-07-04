package com.lmaye.cloud.starter.canal.patterns;

import org.springframework.stereotype.Component;

/**
 * -- Double Type
 *
 * @author Lmay Zhou
 * @date 2021/4/2 10:13
 * @email lmay@lmaye.com
 */
@Component
public class DoubleType implements IType {
    /**
     * 获取类型名称
     *
     * @return String
     */
    @Override
    public String getTypeName() {
        return Double.class.getTypeName();
    }

    /**
     * 类型转换
     *
     * @param value 值
     * @return Double
     */
    @Override
    public Double convertType(String value) {
        return Double.parseDouble(value);
    }
}
