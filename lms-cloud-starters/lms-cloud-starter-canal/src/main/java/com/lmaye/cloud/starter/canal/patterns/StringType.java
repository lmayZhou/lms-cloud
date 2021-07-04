package com.lmaye.cloud.starter.canal.patterns;

import org.springframework.stereotype.Component;

/**
 * -- String Type
 *
 * @author Lmay Zhou
 * @date 2021/4/1 18:29
 * @email lmay@lmaye.com
 */
@Component
public class StringType implements IType {
    /**
     * 获取类型名称
     *
     * @return String
     */
    @Override
    public String getTypeName() {
        return String.class.getTypeName();
    }

    /**
     * 类型转换
     *
     * @param value 值
     * @return String
     */
    @Override
    public String convertType(String value) {
        return value;
    }
}
