package com.lmaye.cloud.starter.canal.patterns;

/**
 * -- 类型接口
 *
 * @author Lmay Zhou
 * @date 2021/4/1 18:18
 * @email lmay@lmaye.com
 */
public interface IType {
    /**
     * 获取类型名称
     *
     * @return String
     */
    String getTypeName();

    /**
     * 类型转换
     *
     * @param value 值
     * @return Object
     */
    Object convertType(String value);
}
