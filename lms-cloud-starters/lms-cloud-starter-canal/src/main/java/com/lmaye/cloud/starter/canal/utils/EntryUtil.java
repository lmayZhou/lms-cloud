package com.lmaye.cloud.starter.canal.utils;

import org.apache.commons.lang3.reflect.FieldUtils;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * -- EntryUtil
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class EntryUtil {
    private static Map<Class, Map<String, String>> cache = new ConcurrentHashMap<>();

    /**
     * 获取字段名称和实体属性的对应关系
     *
     * @param c class
     * @return map
     */
    public static Map<String, String> getFieldName(Class c) {
        Map<String, String> map = cache.get(c);
        if (map == null) {
            List<Field> fields = FieldUtils.getAllFieldsList(c);
            //如果实体类中存在column 注解，则使用column注解的名称为字段名
            map = fields.stream().filter(EntryUtil::notTransient)
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toMap(EntryUtil::getColumnName, Field::getName));
            cache.putIfAbsent(c, map);
        }
        return map;
    }

    private static String getColumnName(Field field) {
        Column annotation = field.getAnnotation(Column.class);
        if (annotation != null) {
            return annotation.name();
        } else {
            return field.getName();
        }
    }

    private static boolean notTransient(Field field) {
        Transient annotation = field.getAnnotation(Transient.class);
        return annotation == null;
    }
}
