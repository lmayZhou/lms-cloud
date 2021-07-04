package com.lmaye.cloud.starter.canal.patterns;

import org.springframework.stereotype.Component;

/**
 * -- Integer 类型
 *
 * @author Lmay Zhou
 * @date 2021/4/1 18:29
 * @email lmay@lmaye.com
 */
@Component
public class IntegerType implements IType {
    /**
     * 获取类型名称
     *
     * @return String
     */
    @Override
    public String getTypeName() {
        return Integer.class.getTypeName();
    }

    /**
     * 类型转换
     *
     * @param value 值
     * @return Integer
     */
    @Override
    public Integer convertType(String value) {
        return Integer.parseInt(value);
    }
}
