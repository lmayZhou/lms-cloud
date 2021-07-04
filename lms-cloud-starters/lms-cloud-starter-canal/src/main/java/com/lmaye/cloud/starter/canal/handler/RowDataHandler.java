package com.lmaye.cloud.starter.canal.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;

/**
 * -- RowDataHandler
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public interface RowDataHandler<T> {
    <R> void handlerRowData(T t, EntryHandler<R> entryHandler, CanalEntry.EventType eventType) throws Exception;

    <R> void handlerDdl(String sql, EntryHandler<R> entryHandler) throws Exception;
}
