package com.lmaye.cloud.starter.canal.handler.impl;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.lmaye.cloud.starter.canal.handler.AbstractFlatMessageHandler;
import com.lmaye.cloud.starter.canal.handler.EntryHandler;
import com.lmaye.cloud.starter.canal.handler.RowDataHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * -- AsyncFlatMessageHandlerImpl
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class AsyncFlatMessageHandlerImpl extends AbstractFlatMessageHandler {
    private final ExecutorService executor;

    public AsyncFlatMessageHandlerImpl(List<? extends EntryHandler> entryHandlers,
                                       RowDataHandler<List<Map<String, String>>> rowDataHandler, ExecutorService executor) {
        super(entryHandlers, rowDataHandler);
        this.executor = executor;
    }

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        executor.execute(() -> super.handleMessage(flatMessage));
    }
}
