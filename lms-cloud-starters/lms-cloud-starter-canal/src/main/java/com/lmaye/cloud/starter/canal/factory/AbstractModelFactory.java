package com.lmaye.cloud.starter.canal.factory;

import com.lmaye.cloud.starter.canal.enums.TableNameEnum;
import com.lmaye.cloud.starter.canal.handler.EntryHandler;
import com.lmaye.cloud.starter.canal.utils.GenericUtil;
import com.lmaye.cloud.starter.canal.utils.HandlerUtil;

/**
 * -- AbstractModelFactory
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public abstract class AbstractModelFactory<T> implements IModelFactory<T> {
    @Override
    public <R> R newInstance(EntryHandler entryHandler, T t) throws Exception {
        String canalTableName = HandlerUtil.getCanalTableName(entryHandler);
        if (TableNameEnum.ALL.name().toLowerCase().equals(canalTableName)) {
            return (R) t;
        }
        Class<R> tableClass = GenericUtil.getTableClass(entryHandler);
        if (tableClass != null) {
            return newInstance(tableClass, t);
        }
        return null;
    }

    abstract <R> R newInstance(Class<R> c, T t) throws Exception;
}
