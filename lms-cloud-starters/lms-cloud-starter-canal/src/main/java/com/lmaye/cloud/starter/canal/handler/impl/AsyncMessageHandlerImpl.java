package com.lmaye.cloud.starter.canal.handler.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.lmaye.cloud.starter.canal.handler.AbstractMessageHandler;
import com.lmaye.cloud.starter.canal.handler.EntryHandler;
import com.lmaye.cloud.starter.canal.handler.RowDataHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * -- AsyncMessageHandlerImpl
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class AsyncMessageHandlerImpl extends AbstractMessageHandler {
    private final ExecutorService executor;

    public AsyncMessageHandlerImpl(List<? extends EntryHandler> entryHandlers,
                                   RowDataHandler<CanalEntry.RowData> rowDataHandler, ExecutorService executor) {
        super(entryHandlers, rowDataHandler);
        this.executor = executor;
    }

    @Override
    public void handleMessage(Message message) {
        executor.execute(() -> super.handleMessage(message));
    }
}
