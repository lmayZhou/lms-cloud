package com.lmaye.cloud.starter.canal.patterns;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * -- BigDecimal Type
 *
 * @author Lmay Zhou
 * @date 2021/4/2 10:11
 * @email lmay@lmaye.com
 */
@Component
public class BigDecimalType implements IType {
    /**
     * 获取类型名称
     *
     * @return String
     */
    @Override
    public String getTypeName() {
        return BigDecimal.class.getTypeName();
    }

    /**
     * 类型转换
     *
     * @param value 值
     * @return BigDecimal
     */
    @Override
    public BigDecimal convertType(String value) {
        return new BigDecimal(value);
    }
}
