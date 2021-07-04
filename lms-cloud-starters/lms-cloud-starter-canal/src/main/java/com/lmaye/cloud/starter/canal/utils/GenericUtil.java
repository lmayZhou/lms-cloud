package com.lmaye.cloud.starter.canal.utils;

import com.lmaye.cloud.starter.canal.handler.EntryHandler;

import javax.persistence.Table;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * -- GenericUtil
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class GenericUtil {
    private static final Map<Class<? extends EntryHandler>, Class<?>> CACHE = new ConcurrentHashMap<>();

    static String getTableGenericProperties(EntryHandler entryHandler) {
        Class<?> tableClass = getTableClass(entryHandler);
        if (tableClass != null) {
            Table annotation = tableClass.getAnnotation(Table.class);
            if (annotation != null) {
                return annotation.name();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getTableClass(EntryHandler object) {
        Class<? extends EntryHandler> handlerClass = object.getClass();
        Class tableClass = CACHE.get(handlerClass);
        if (tableClass == null) {
            Type[] interfacesTypes = handlerClass.getGenericInterfaces();
            for (Type t : interfacesTypes) {
                Class c = (Class) ((ParameterizedType) t).getRawType();
                if (c.equals(EntryHandler.class)) {
                    tableClass = (Class<T>) ((ParameterizedType) t).getActualTypeArguments()[0];
                    CACHE.putIfAbsent(handlerClass, tableClass);
                    return tableClass;
                }
            }
        }
        return tableClass;
    }
}