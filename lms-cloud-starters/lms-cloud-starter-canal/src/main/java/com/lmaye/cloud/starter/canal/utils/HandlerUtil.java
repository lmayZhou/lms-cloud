package com.lmaye.cloud.starter.canal.utils;

import com.lmaye.cloud.starter.canal.annotation.CanalTable;
import com.lmaye.cloud.starter.canal.enums.TableNameEnum;
import com.lmaye.cloud.starter.canal.handler.EntryHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * -- Handler Util
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class HandlerUtil {
    public static EntryHandler getEntryHandler(List<? extends EntryHandler> entryHandlers, String tableName) {
        EntryHandler globalHandler = null;
        for (EntryHandler handler : entryHandlers) {
            String canalTableName = getCanalTableName(handler);
            if (TableNameEnum.ALL.name().toLowerCase().equals(canalTableName)) {
                globalHandler = handler;
                continue;
            }
            if (tableName.equals(canalTableName)) {
                return handler;
            }
            String name = GenericUtil.getTableGenericProperties(handler);
            if (name != null) {
                if (name.equals(tableName)) {
                    return handler;
                }
            }
        }
        return globalHandler;
    }

    public static Map<String, EntryHandler> getTableHandlerMap(List<? extends EntryHandler> entryHandlers) {
        Map<String, EntryHandler> map = new ConcurrentHashMap<>();
        if (entryHandlers != null && entryHandlers.size() > 0) {
            for (EntryHandler handler : entryHandlers) {
                String canalTableName = getCanalTableName(handler);
                if (canalTableName != null) {
                    map.putIfAbsent(canalTableName.toLowerCase(), handler);
                } else {
                    String name = GenericUtil.getTableGenericProperties(handler);
                    if (name != null) {
                        map.putIfAbsent(name.toLowerCase(), handler);
                    }
                }
            }
        }
        return map;
    }

    public static EntryHandler getEntryHandler(Map<String, EntryHandler> map, String tableName) {
        EntryHandler entryHandler = map.get(tableName);
        if (entryHandler == null) {
            return map.get(TableNameEnum.ALL.name().toLowerCase());
        }
        return entryHandler;
    }

    public static String getCanalTableName(EntryHandler entryHandler) {
        CanalTable canalTable = entryHandler.getClass().getAnnotation(CanalTable.class);
        if (canalTable != null) {
            return canalTable.value();
        }
        return null;
    }

}
