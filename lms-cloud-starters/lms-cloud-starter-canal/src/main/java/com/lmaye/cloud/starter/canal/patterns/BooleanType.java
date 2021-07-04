package com.lmaye.cloud.starter.canal.patterns;

import org.springframework.stereotype.Component;

/**
 * -- Boolean Type
 *
 * @author Lmay Zhou
 * @date 2021/4/2 10:10
 * @email lmay@lmaye.com
 */
@Component
public class BooleanType implements IType {
    /**
     * 获取类型名称
     *
     * @return String
     */
    @Override
    public String getTypeName() {
        return Boolean.class.getTypeName();
    }

    /**
     * 类型转换
     *
     * @param value 值
     * @return Boolean
     */
    @Override
    public Boolean convertType(String value) {
        return "1".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
    }
}
