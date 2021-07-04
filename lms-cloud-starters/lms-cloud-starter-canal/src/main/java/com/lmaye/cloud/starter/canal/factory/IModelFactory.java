package com.lmaye.cloud.starter.canal.factory;

import com.lmaye.cloud.starter.canal.handler.EntryHandler;

import java.util.Set;

/**
 * -- IModelFactory
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public interface IModelFactory<T> {
    <R> R newInstance(EntryHandler entryHandler, T t) throws Exception;

    default <R> R newInstance(EntryHandler entryHandler, T t, Set<String> updateColumn) throws Exception {
        return null;
    }
}
