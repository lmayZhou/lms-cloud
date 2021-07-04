package com.lmaye.cloud.starter.canal.utils;

import java.lang.reflect.Field;

/**
 * -- FieldUtil
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class FieldUtil {
    public static void setFieldValue(Object object, String fieldName, String value) throws NoSuchFieldException,
            IllegalAccessException {
        Field field;
        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = object.getClass().getSuperclass().getDeclaredField(fieldName);
        }
        field.setAccessible(true);
        Class<?> type = field.getType();
        Object result = StringConvertUtil.convertType(type, value);
        field.set(object, result);
    }
}
